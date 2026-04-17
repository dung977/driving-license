package com.example.btlapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Ôn thi giấy phép lái xe");
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize database and load questions from assets
        initDatabase();

        Button btnMotorbike = findViewById(R.id.btnMotorbike);
        Button btnCar = findViewById(R.id.btnCar);

        btnMotorbike.setOnClickListener(v -> {
            Intent intent = new Intent(this, LicenseSelectionActivity.class);
            intent.putExtra("LICENSE_TYPE", "MOTORBIKE");
            startActivity(intent);
        });

        btnCar.setOnClickListener(v -> {
            Intent intent = new Intent(this, LicenseSelectionActivity.class);
            intent.putExtra("LICENSE_TYPE", "CAR");
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.theme_light) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            return true;
        } else if (id == R.id.theme_dark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            return true;
        } else if (id == R.id.theme_system) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initDatabase() {
        DatabaseHelper db = new DatabaseHelper(this);

        // Danh sách các hạng bằng cần nạp dữ liệu đầy đủ
        String[] classes = {"A1", "B1", "B", "C1", "C", "D"};
        for (String licenseClass : classes) {
            if (db.getQuestionsByClass(licenseClass).isEmpty()) {
                loadQuestionsFromJson(db, "600question.json", licenseClass);
            }
        }

        if (db.getAllTrafficSigns().isEmpty()) {
            loadTrafficSignsFromJson(db, "bien_bao.json");
        }
    }

    private void loadQuestionsFromJson(DatabaseHelper db, String fileName, String licenseClass) {
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                int id = obj.getInt("id");

                // KIỂM TRA ID:
                // Đối với A1 và B1: Lọc theo danh sách ID cụ thể đã định nghĩa trong DatabaseHelper
                // Đối với B, C, C1, D: Nạp full 600 câu để bốc đề thi thử và lọc lý thuyết
                if (licenseClass.equals("A1") || licenseClass.equals("B1")) {
                    if (!db.isIdInClass(id, licenseClass)) {
                        continue;
                    }
                }

                JSONObject optionsObj = obj.getJSONObject("options");
                String optionA = optionsObj.optString("A", "");
                String optionB = optionsObj.optString("B", "");
                String optionC = optionsObj.optString("C", "");
                String optionD = optionsObj.optString("D", null);

                String ansChar = obj.getString("correctAnswer");
                int ansInt = 1;
                if ("B".equalsIgnoreCase(ansChar)) ansInt = 2;
                else if ("C".equalsIgnoreCase(ansChar)) ansInt = 3;
                else if ("D".equalsIgnoreCase(ansChar)) ansInt = 4;

                String imageName = obj.optString("image", null);
                String content = obj.getString("content");

                Question q = new Question(
                        id,
                        content,
                        optionA,
                        optionB,
                        optionC,
                        optionD,
                        ansInt,
                        obj.optString("explanation", ""),
                        null,
                        imageName,
                        false
                );
                db.addQuestion(q, licenseClass);
            }
            Log.d("DB", "Loaded questions for " + licenseClass + " - Total: " + db.getQuestionsByClass(licenseClass).size());
        } catch (Exception e) {
            Log.e("DB", "Error loading JSON from " + fileName, e);
        }
    }

    private void loadTrafficSignsFromJson(DatabaseHelper db, String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                String name = obj.getString("ten_bien");
                String description = obj.getString("thong_tin");
                String imageName = obj.getString("image");

                String category = "Biển báo";
                if (name.contains("Cấm")) category = "Biển báo cấm";
                else if (name.contains("nguy hiểm")) category = "Biển báo nguy hiểm";
                else if (name.contains("hiệu lệnh")) category = "Biển báo hiệu lệnh";
                else if (name.contains("chỉ dẫn")) category = "Biển báo chỉ dẫn";

                TrafficSign sign = new TrafficSign(name, description, imageName, category);
                db.addTrafficSign(sign);
            }
        } catch (Exception e) {
            Log.e("DB", "Error loading traffic signs", e);
        }
    }
}
