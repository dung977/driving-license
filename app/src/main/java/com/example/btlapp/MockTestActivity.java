package com.example.btlapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockTestActivity extends AppCompatActivity {

    private List<Question> questions;
    private int currentIndex = 0;
    private Map<Integer, Integer> userAnswers = new HashMap<>(); // Question Index -> Selected Option
    private DatabaseHelper dbHelper;

    private TextView tvTimer;
    private TextView tvCount;
    private TextView tvContent;
    private ImageView ivQuestionImage;
    private RadioGroup rgOptions;
    private RadioButton rbA;
    private RadioButton rbB;
    private RadioButton rbC;
    private RadioButton rbD;
    private Button btnPrev;
    private Button btnNext;
    private Button btnSubmit;
    
    private int totalQuestions = 25; 
    private int passingScore = 21;   
    private int timeLimitMinutes = 19; 
    private String licenseClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mock_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);
        initViews();
        loadMockQuestions();
        startTimer();
        displayQuestion();

        btnNext.setOnClickListener(v -> {
            saveAnswer();
            if (questions != null && currentIndex < questions.size() - 1) {
                currentIndex++;
                displayQuestion();
            }
        });

        btnPrev.setOnClickListener(v -> {
            saveAnswer();
            if (currentIndex > 0) {
                currentIndex--;
                displayQuestion();
            }
        });

        btnSubmit.setOnClickListener(v -> {
            saveAnswer();
            showSubmitDialog();
        });
    }

    private void initViews() {
        tvTimer = findViewById(R.id.tvTimer);
        tvCount = findViewById(R.id.tvMockQuestionCount);
        tvContent = findViewById(R.id.tvMockQuestionContent);
        ivQuestionImage = findViewById(R.id.ivMockQuestionImage);
        rgOptions = findViewById(R.id.rgMockOptions);
        rbA = findViewById(R.id.rbMockOptionA);
        rbB = findViewById(R.id.rbMockOptionB);
        rbC = findViewById(R.id.rbMockOptionC);
        rbD = findViewById(R.id.rbMockOptionD);
        btnPrev = findViewById(R.id.btnMockPrevious);
        btnNext = findViewById(R.id.btnMockNext);
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private void startTimer() {
        new CountDownTimer(timeLimitMinutes * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 1000 / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                tvTimer.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                tvTimer.setText("00:00");
                calculateResult();
            }
        }.start();
    }

    private void displayQuestion() {
        if (questions == null || questions.isEmpty()) return;

        Question q = questions.get(currentIndex);
        tvCount.setText("Câu " + (currentIndex + 1) + "/" + questions.size());
        tvContent.setText(q.getContent());
        
        // Handle Image
        if (q.getImageName() != null && !q.getImageName().isEmpty()) {
            try {
                String folder = "images/carb1/";
                if (licenseClass != null && licenseClass.startsWith("A")) {
                    folder = "images/motoAA1/";
                }
                InputStream is = getAssets().open(folder + q.getImageName());
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                ivQuestionImage.setImageBitmap(bitmap);
                ivQuestionImage.setVisibility(View.VISIBLE);
                is.close();
            } catch (IOException e) {
                ivQuestionImage.setVisibility(View.GONE);
            }
        } else if (q.getImageResId() != null && q.getImageResId() != 0) {
            ivQuestionImage.setVisibility(View.VISIBLE);
            ivQuestionImage.setImageResource(q.getImageResId());
        } else {
            ivQuestionImage.setVisibility(View.GONE);
        }

        // Display answers with labels A, B, C, D
        rbA.setText("1. " + q.getOptionA());
        rbB.setText("2. " + q.getOptionB());
        
        if (q.getOptionC() != null && !q.getOptionC().isEmpty()) {
            rbC.setVisibility(View.VISIBLE);
            rbC.setText("3. " + q.getOptionC());
        } else {
            rbC.setVisibility(View.GONE);
        }
        
        if (q.getOptionD() != null && !q.getOptionD().isEmpty()) {
            rbD.setVisibility(View.VISIBLE);
            rbD.setText("4. " + q.getOptionD());
        } else {
            rbD.setVisibility(View.GONE);
        }

        rgOptions.clearCheck();
        Integer answer = userAnswers.get(currentIndex);
        if (answer != null) {
            if (answer == 1) rbA.setChecked(true);
            else if (answer == 2) rbB.setChecked(true);
            else if (answer == 3) rbC.setChecked(true);
            else if (answer == 4) rbD.setChecked(true);
        }
    }

    private void saveAnswer() {
        int selectedId = rgOptions.getCheckedRadioButtonId();
        if (selectedId != -1) {
            int answer = 0;
            if (selectedId == R.id.rbMockOptionA) answer = 1;
            else if (selectedId == R.id.rbMockOptionB) answer = 2;
            else if (selectedId == R.id.rbMockOptionC) answer = 3;
            else if (selectedId == R.id.rbMockOptionD) answer = 4;
            
            userAnswers.put(currentIndex, answer);
        }
    }

    private void loadMockQuestions() {
        licenseClass = getIntent().getStringExtra("LICENSE_CLASS");
        if (licenseClass == null) licenseClass = "A1";

        // Thiết lập thông số theo luật
        if (licenseClass.equals("B2")) {
            totalQuestions = 35;
            passingScore = 32;
            timeLimitMinutes = 22;
        } else if (licenseClass.equals("B1")) {
            totalQuestions = 30;
            passingScore = 27;
            timeLimitMinutes = 20;
        } else { // A1, A2...
            totalQuestions = 25;
            passingScore = 21;
            timeLimitMinutes = 19;
        }

        List<Question> allQuestions = dbHelper.getQuestionsByClass(licenseClass);
        if (allQuestions != null && !allQuestions.isEmpty()) {
            List<Question> criticalQuestions = new ArrayList<>();
            List<Question> normalQuestions = new ArrayList<>();

            for (Question q : allQuestions) {
                if (q.isCritical()) criticalQuestions.add(q);
                else normalQuestions.add(q);
            }

            Collections.shuffle(criticalQuestions);
            Collections.shuffle(normalQuestions);

            List<Question> testList = new ArrayList<>();
            // Lấy 1-2 câu điểm liệt
            int numCritical = Math.min(2, criticalQuestions.size());
            testList.addAll(criticalQuestions.subList(0, numCritical));

            // Lấy số câu thường còn lại
            int numNormal = totalQuestions - numCritical;
            testList.addAll(normalQuestions.subList(0, Math.min(numNormal, normalQuestions.size())));

            Collections.shuffle(testList);
            questions = testList;
        }
    }

    private void showSubmitDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Nộp bài")
            .setMessage("Bạn có chắc chắn muốn nộp bài?")
            .setPositiveButton("Có", (dialog, which) -> calculateResult())
            .setNegativeButton("Không", null)
            .show();
    }

    private void calculateResult() {
        int correctCount = 0;
        boolean failedCritical = false;

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            Integer userAnswer = userAnswers.get(i);
            if (userAnswer != null && userAnswer == question.getCorrectAnswer()) {
                correctCount++;
            } else {
                if (question.isCritical()) {
                    failedCritical = true;
                }
            }
        }

        String resultMessage;
        if (failedCritical && correctCount >= passingScore) {
            resultMessage = "Bạn TRƯỢT do trả lời sai câu hỏi điểm liệt (Đạt " + correctCount + "/" + questions.size() + ").";
        } else if (correctCount >= passingScore) {
            resultMessage = "Chúc mừng! Bạn đã ĐẠT (" + correctCount + "/" + questions.size() + ").";
        } else {
            resultMessage = "Bạn KHÔNG ĐẠT (" + correctCount + "/" + questions.size() + ").";
        }

        new AlertDialog.Builder(this)
            .setTitle("Kết quả")
            .setMessage(resultMessage)
            .setPositiveButton("Đóng", (dialog, which) -> finish())
            .setCancelable(false)
            .show();
    }
}
