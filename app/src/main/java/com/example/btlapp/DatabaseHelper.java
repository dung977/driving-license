package com.example.btlapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "driving_license.db";
    private static final int DATABASE_VERSION = 16;

    private static final String TABLE_QUESTIONS = "questions";
    private static final String COLUMN_Q_ID = "id";
    private static final String COLUMN_Q_CONTENT = "content";
    private static final String COLUMN_Q_OPTION_A = "option_a";
    private static final String COLUMN_Q_OPTION_B = "option_b";
    private static final String COLUMN_Q_OPTION_C = "option_c";
    private static final String COLUMN_Q_OPTION_D = "option_d";
    private static final String COLUMN_Q_CORRECT_ANSWER = "correct_answer";
    private static final String COLUMN_Q_EXPLANATION = "explanation";
    private static final String COLUMN_Q_IMAGE_NAME = "image_name";
    private static final String COLUMN_Q_IS_CRITICAL = "is_critical";
    private static final String COLUMN_Q_LICENSE_CLASS = "license_class";
    private static final String COLUMN_Q_USER_ANSWER = "user_answer";
    private static final String COLUMN_Q_IS_ANSWERED = "is_answered";

    private static final String TABLE_SIGNS = "traffic_signs";
    private static final String COLUMN_S_ID = "id";
    private static final String COLUMN_S_NAME = "name";
    private static final String COLUMN_S_DESCRIPTION = "description";
    private static final String COLUMN_S_IMAGE_NAME = "image_name";
    private static final String COLUMN_S_CATEGORY = "category";

    // Danh sách 60 câu hỏi điểm liệt cho Ô tô (B, C1, C, D)
    public static final List<Integer> CAR_CRITICAL_IDS = Arrays.asList(
            19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 30, 32, 34, 35, 47, 48, 52, 53, 55, 58, 63, 64, 65, 66, 67, 68, 70, 71, 72, 73, 74, 85, 86, 87, 88, 89, 90, 91, 92, 93, 97, 98, 102, 117, 163, 165, 167, 197, 198, 206, 215, 226, 234, 245, 246, 252, 253, 254, 255, 260
    );

    // Danh sách ID cho hạng A1 (225 câu)
    public static final List<Integer> A1_RULES = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 19, 20, 21, 22, 24, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 43, 44, 45, 46, 47, 48, 49, 51, 52, 53, 54, 56, 57, 59, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 80, 81, 87, 88, 90, 91, 92, 93, 94, 96, 97, 98, 99, 100, 102, 103, 107, 109, 110, 111, 119, 123, 124, 125, 126, 137, 138, 140, 141, 142, 145, 146, 151, 155, 163, 167, 178);
    public static final List<Integer> A1_CULTURE = Arrays.asList(182, 185, 187, 189, 191, 192, 193, 194, 195, 200);
    public static final List<Integer> A1_TECH = Arrays.asList(206, 215, 219, 232, 233, 240, 241, 242, 254, 255, 257, 258, 259, 260, 261);
    public static final List<Integer> A1_SIGNS = Arrays.asList(303, 304, 305, 306, 307, 313, 314, 315, 317, 318, 322, 323, 324, 325, 326, 329, 330, 335, 345, 346, 347, 348, 349, 350, 351, 354, 360, 362, 364, 366, 367, 368, 369, 370, 371, 372, 373, 374, 375, 376, 377, 380, 381, 382, 386, 387, 389, 390, 391, 393, 394, 395, 397, 398, 400, 401, 411, 412, 413, 415, 419, 422, 427, 430, 431);
    public static final List<Integer> A1_SITUATIONS = Arrays.asList(486, 487, 490, 492, 495, 499, 500, 503, 504, 505, 507, 508, 509, 517, 520, 525, 527, 528, 529, 538, 539, 540, 543, 548, 553, 556, 559, 560, 562, 565, 567, 568, 583, 592, 600);
    public static final List<Integer> A1_CRITICAL = Arrays.asList(19, 20, 21, 22, 24, 26, 27, 28, 30, 47, 48, 52, 53, 63, 64, 65, 68, 70, 71, 72);

    // Danh sách ID cho hạng B1 (300 câu)
    public static final List<Integer> B1_RULES = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 19, 20, 21, 22, 24, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 43, 44, 45, 46, 47, 48, 49, 51, 52, 53, 54, 55, 56, 57, 59, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 80, 81, 82, 87, 88, 89, 90, 91, 92, 93, 94, 96, 97, 98, 99, 100, 102, 103, 107, 108, 109, 110, 111, 119, 123, 124, 125, 126, 137, 138, 139, 140, 141, 142, 145, 146, 151, 155, 157, 162, 163, 165, 166, 167, 178);
    public static final List<Integer> B1_CULTURE = Arrays.asList(182, 185, 187, 189, 191, 192, 193, 194, 195, 200);
    public static final List<Integer> B1_TECH = Arrays.asList(206, 215, 219, 232, 233, 240, 241, 242, 254, 255, 257, 258, 259, 260, 261, 266, 285);
    public static final List<Integer> B1_SIGNS = Arrays.asList(303, 304, 305, 306, 307, 313, 314, 315, 317, 318, 322, 323, 324, 325, 326, 329, 330, 332, 333, 334, 335, 344, 345, 346, 347, 348, 349, 350, 351, 354, 355, 360, 361, 362, 364, 366, 367, 368, 369, 370, 371, 372, 373, 374, 375, 376, 377, 380, 381, 382, 383, 384, 385, 386, 387, 388, 389, 390, 391, 392, 393, 394, 395, 396, 397, 398, 400, 401, 402, 405, 406, 407, 408, 409, 410, 411, 412, 413, 415, 416, 418, 419, 420, 421, 422, 423, 424, 425, 426, 427, 430, 431, 432, 433, 434, 435, 436, 437, 438, 439, 440, 441, 442, 443, 445, 446, 450, 451, 452, 454, 455, 456, 457, 458, 459, 460, 461, 474, 475, 476, 477, 478, 479, 480, 481, 482, 483, 485);
    public static final List<Integer> B1_SITUATIONS = Arrays.asList(486, 487, 490, 492, 495, 499, 500, 503, 504, 505, 507, 508, 509, 517, 520, 525, 527, 528, 529, 538, 539, 540, 543, 548, 553, 556, 559, 560, 562, 565, 567, 568, 583, 592, 600);
    public static final List<Integer> B1_CRITICAL = Arrays.asList(19, 20, 21, 22, 24, 26, 27, 28, 30, 47, 48, 52, 53, 63, 64, 65, 68, 70, 71, 72, 73, 74, 87, 89, 90, 91, 92, 215, 254, 255);

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuestionsTable = "CREATE TABLE " + TABLE_QUESTIONS + " (" +
                COLUMN_Q_ID + " INTEGER, " +
                COLUMN_Q_CONTENT + " TEXT, " +
                COLUMN_Q_OPTION_A + " TEXT, " +
                COLUMN_Q_OPTION_B + " TEXT, " +
                COLUMN_Q_OPTION_C + " TEXT, " +
                COLUMN_Q_OPTION_D + " TEXT, " +
                COLUMN_Q_CORRECT_ANSWER + " INTEGER, " +
                COLUMN_Q_EXPLANATION + " TEXT, " +
                COLUMN_Q_IMAGE_NAME + " TEXT, " +
                COLUMN_Q_IS_CRITICAL + " INTEGER, " +
                COLUMN_Q_LICENSE_CLASS + " TEXT, " +
                COLUMN_Q_USER_ANSWER + " INTEGER DEFAULT 0, " +
                COLUMN_Q_IS_ANSWERED + " INTEGER DEFAULT 0)";
        db.execSQL(createQuestionsTable);

        String createSignsTable = "CREATE TABLE " + TABLE_SIGNS + " (" +
                COLUMN_S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_S_NAME + " TEXT, " +
                COLUMN_S_DESCRIPTION + " TEXT, " +
                COLUMN_S_IMAGE_NAME + " TEXT, " +
                COLUMN_S_CATEGORY + " TEXT)";
        db.execSQL(createSignsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIGNS);
        onCreate(db);
    }

    public void addQuestion(Question question, String licenseClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_Q_ID, question.getId());
        values.put(COLUMN_Q_CONTENT, question.getContent());
        values.put(COLUMN_Q_OPTION_A, question.getOptionA());
        values.put(COLUMN_Q_OPTION_B, question.getOptionB());
        values.put(COLUMN_Q_OPTION_C, question.getOptionC());
        values.put(COLUMN_Q_OPTION_D, question.getOptionD());
        values.put(COLUMN_Q_CORRECT_ANSWER, question.getCorrectAnswer());
        values.put(COLUMN_Q_EXPLANATION, question.getExplanation());
        values.put(COLUMN_Q_IMAGE_NAME, question.getImageName());
        
        boolean isCritical = question.isCritical();
        if (licenseClass.equals("A1")) {
             if (A1_CRITICAL.contains(question.getId())) isCritical = true;
        } else if (licenseClass.equals("B1")) {
             if (B1_CRITICAL.contains(question.getId())) isCritical = true;
        } else {
             // Ô tô (B, C, C1, D)
             if (CAR_CRITICAL_IDS.contains(question.getId())) isCritical = true;
        }
        
        values.put(COLUMN_Q_IS_CRITICAL, isCritical ? 1 : 0);
        values.put(COLUMN_Q_LICENSE_CLASS, licenseClass);
        db.insert(TABLE_QUESTIONS, null, values);
    }

    public List<Question> getQuestionsByIdList(List<Integer> ids, String licenseClass) {
        List<Question> list = new ArrayList<>();
        if (ids == null || ids.isEmpty()) return list;
        SQLiteDatabase db = this.getReadableDatabase();
        String idListStr = TextUtils.join(",", ids);
        String query = "SELECT * FROM " + TABLE_QUESTIONS + " WHERE " + COLUMN_Q_ID + " IN (" + idListStr + ") AND " + COLUMN_Q_LICENSE_CLASS + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{licenseClass});
        if (cursor.moveToFirst()) {
            do { list.add(parseQuestion(cursor)); } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<Question> getFilteredQuestions(String licenseClass, String filterType) {
        List<Question> questionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_Q_LICENSE_CLASS + "=?";
        List<String> selectionArgs = new ArrayList<>();
        selectionArgs.add(licenseClass);

        if (licenseClass.equals("A1")) {
            switch (filterType) {
                case "CRITICAL": selection += " AND " + COLUMN_Q_IS_CRITICAL + "=1"; break;
                case "RULES": selection += " AND " + COLUMN_Q_ID + " IN (" + TextUtils.join(",", A1_RULES) + ")"; break;
                case "CULTURE": selection += " AND " + COLUMN_Q_ID + " IN (" + TextUtils.join(",", A1_CULTURE) + ")"; break;
                case "TECHNIQUES": selection += " AND " + COLUMN_Q_ID + " IN (" + TextUtils.join(",", A1_TECH) + ")"; break;
                case "SIGNS": selection += " AND " + COLUMN_Q_ID + " IN (" + TextUtils.join(",", A1_SIGNS) + ")"; break;
                case "SITUATIONS": selection += " AND " + COLUMN_Q_ID + " IN (" + TextUtils.join(",", A1_SITUATIONS) + ")"; break;
                case "WRONG": selection += " AND " + COLUMN_Q_IS_ANSWERED + "=1 AND " + COLUMN_Q_USER_ANSWER + "!=" + COLUMN_Q_CORRECT_ANSWER; break;
                case "DONE": selection += " AND " + COLUMN_Q_IS_ANSWERED + "=1"; break;
                case "NOT_DONE": selection += " AND " + COLUMN_Q_IS_ANSWERED + "=0"; break;
            }
        } else if (licenseClass.equals("B1")) {
            switch (filterType) {
                case "CRITICAL": selection += " AND " + COLUMN_Q_IS_CRITICAL + "=1"; break;
                case "RULES": selection += " AND " + COLUMN_Q_ID + " IN (" + TextUtils.join(",", B1_RULES) + ")"; break;
                case "CULTURE": selection += " AND " + COLUMN_Q_ID + " IN (" + TextUtils.join(",", B1_CULTURE) + ")"; break;
                case "TECHNIQUES": selection += " AND " + COLUMN_Q_ID + " IN (" + TextUtils.join(",", B1_TECH) + ")"; break;
                case "SIGNS": selection += " AND " + COLUMN_Q_ID + " IN (" + TextUtils.join(",", B1_SIGNS) + ")"; break;
                case "SITUATIONS": selection += " AND " + COLUMN_Q_ID + " IN (" + TextUtils.join(",", B1_SITUATIONS) + ")"; break;
                case "WRONG": selection += " AND " + COLUMN_Q_IS_ANSWERED + "=1 AND " + COLUMN_Q_USER_ANSWER + "!=" + COLUMN_Q_CORRECT_ANSWER; break;
                case "DONE": selection += " AND " + COLUMN_Q_IS_ANSWERED + "=1"; break;
                case "NOT_DONE": selection += " AND " + COLUMN_Q_IS_ANSWERED + "=0"; break;
            }
        } else {
            // Ô tô (B, C, C1, D)
            switch (filterType) {
                case "CRITICAL": selection += " AND " + COLUMN_Q_IS_CRITICAL + "=1"; break;
                case "RULES": selection += " AND " + COLUMN_Q_ID + " BETWEEN 1 AND 166"; break;
                case "CULTURE": selection += " AND " + COLUMN_Q_ID + " BETWEEN 167 AND 205"; break;
                case "TECHNIQUES": selection += " AND " + COLUMN_Q_ID + " BETWEEN 206 AND 243"; break;
                case "REPAIR": selection += " AND " + COLUMN_Q_ID + " BETWEEN 244 AND 294"; break;
                case "SIGNS": selection += " AND (" + COLUMN_Q_ID + " BETWEEN 295 AND 475 OR " + COLUMN_Q_ID + " BETWEEN 286 AND 290)"; break;
                case "SITUATIONS": selection += " AND " + COLUMN_Q_ID + " BETWEEN 476 AND 600"; break;
                case "WRONG": selection += " AND " + COLUMN_Q_IS_ANSWERED + "=1 AND " + COLUMN_Q_USER_ANSWER + "!=" + COLUMN_Q_CORRECT_ANSWER; break;
                case "DONE": selection += " AND " + COLUMN_Q_IS_ANSWERED + "=1"; break;
                case "NOT_DONE": selection += " AND " + COLUMN_Q_IS_ANSWERED + "=0"; break;
            }
        }

        Cursor cursor = db.query(TABLE_QUESTIONS, null, selection, selectionArgs.toArray(new String[0]), null, null, null);
        if (cursor.moveToFirst()) {
            do { questionList.add(parseQuestion(cursor)); } while (cursor.moveToNext());
        }
        cursor.close();
        return questionList;
    }

    private Question parseQuestion(Cursor cursor) {
        return new Question(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_Q_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Q_CONTENT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Q_OPTION_A)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Q_OPTION_B)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Q_OPTION_C)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Q_OPTION_D)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_Q_CORRECT_ANSWER)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Q_EXPLANATION)),
                null,
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Q_IMAGE_NAME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_Q_IS_CRITICAL)) == 1
        );
    }

    public List<Question> getQuestionsByClass(String licenseClass) {
        return getFilteredQuestions(licenseClass, "ALL");
    }

    public int getUserAnswer(int questionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUESTIONS, new String[]{COLUMN_Q_USER_ANSWER}, COLUMN_Q_ID + "=?",
                new String[]{String.valueOf(questionId)}, null, null, null);
        int answer = 0;
        if (cursor.moveToFirst()) answer = cursor.getInt(0);
        cursor.close();
        return answer;
    }

    public void updateUserAnswer(int questionId, int userAnswer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_Q_USER_ANSWER, userAnswer);
        values.put(COLUMN_Q_IS_ANSWERED, 1);
        db.update(TABLE_QUESTIONS, values, COLUMN_Q_ID + "=?", new String[]{String.valueOf(questionId)});
    }

    public void addTrafficSign(TrafficSign sign) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_S_NAME, sign.getName());
        values.put(COLUMN_S_DESCRIPTION, sign.getDescription());
        values.put(COLUMN_S_IMAGE_NAME, sign.getImageName());
        values.put(COLUMN_S_CATEGORY, sign.getCategory());
        db.insert(TABLE_SIGNS, null, values);
    }

    public List<TrafficSign> getAllTrafficSigns() {
        List<TrafficSign> signList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SIGNS, null);
        if (cursor.moveToFirst()) {
            do {
                signList.add(new TrafficSign(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_S_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_S_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_S_IMAGE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_S_CATEGORY))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return signList;
    }
    
    public boolean isIdInClass(int id, String licenseClass) {
        if (licenseClass.equals("A1")) {
            return A1_RULES.contains(id) || A1_CULTURE.contains(id) || A1_TECH.contains(id) || A1_SIGNS.contains(id) || A1_SITUATIONS.contains(id);
        } else if (licenseClass.equals("B1")) {
            return B1_RULES.contains(id) || B1_CULTURE.contains(id) || B1_TECH.contains(id) || B1_SIGNS.contains(id) || B1_SITUATIONS.contains(id);
        }
        return true;
    }
}
