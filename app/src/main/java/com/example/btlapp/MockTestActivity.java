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
    private Map<Integer, Integer> userAnswers = new HashMap<>(); 
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
    private TextView tvToolbarTitle;
    
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
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
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
        if (questions == null || questions.isEmpty()) {
            tvContent.setText("Không thể tải đề thi cho hạng " + licenseClass + ". Vui lòng kiểm tra lại dữ liệu.");
            return;
        }

        Question q = questions.get(currentIndex);
        tvCount.setText("Câu " + (currentIndex + 1) + "/" + questions.size());
        tvContent.setText(q.getContent());
        
        if (q.getImageName() != null && !q.getImageName().isEmpty()) {
            try {
                InputStream is = getAssets().open("images/600cauhoi/" + q.getImageName());
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                ivQuestionImage.setImageBitmap(bitmap);
                ivQuestionImage.setVisibility(View.VISIBLE);
                is.close();
            } catch (IOException e) {
                ivQuestionImage.setVisibility(View.GONE);
            }
        } else {
            ivQuestionImage.setVisibility(View.GONE);
        }

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

        tvToolbarTitle.setText("Đề thi hạng " + licenseClass);

        if (licenseClass.startsWith("A")) {
            totalQuestions = 25; passingScore = 21; timeLimitMinutes = 19;
            questions = ExamManager.generateExamA1(dbHelper, licenseClass);
        } else {
            // Thiết lập thông số cho Ô tô
            if (licenseClass.startsWith("B")) {
                totalQuestions = (licenseClass.equals("B1")) ? 30 : 30; // Giả định B=30 theo yêu cầu cũ, điều chỉnh nếu cần
                if (licenseClass.equals("B")) totalQuestions = 30;
                passingScore = (totalQuestions == 30) ? 27 : 27;
                timeLimitMinutes = 25; // Theo yêu cầu mới: B, B1 là 25 phút
            } else if (licenseClass.startsWith("C")) {
                if (licenseClass.equals("C1")) {
                    totalQuestions = 35; passingScore = 32;
                } else if (licenseClass.equals("C")) {
                    totalQuestions = 40; passingScore = 36;
                }
                timeLimitMinutes = 25; // Theo yêu cầu mới: C, C1 là 25 phút
            } else {
                totalQuestions = 30; passingScore = 27; timeLimitMinutes = 25;
            }
            questions = ExamManager.generateExamCar(dbHelper, licenseClass);
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
                if (question.isCritical()) failedCritical = true;
            }
        }

        String resultMessage;
        if (failedCritical) {
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
