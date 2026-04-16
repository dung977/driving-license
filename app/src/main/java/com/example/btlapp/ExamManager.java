package com.example.btlapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExamManager {

    // --- DANH SÁCH ID HẠNG A1 ---
    public static final List<Integer> A1_RULES_IDS = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 19, 20, 21, 22, 24, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 43, 44, 45, 46, 47, 48, 49, 51, 52, 53, 54, 56, 57, 59, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 80, 81, 87, 88, 90, 91, 92, 93, 94, 96, 97, 98, 99, 100, 102, 103, 107, 109, 110, 111, 119, 123, 124, 125, 126, 137, 138, 140, 141, 142, 145, 146, 151, 155, 163, 167, 178);
    public static final List<Integer> A1_CULTURE_IDS = Arrays.asList(182, 185, 187, 189, 191, 192, 193, 194, 195, 200);
    public static final List<Integer> A1_TECH_IDS = Arrays.asList(206, 215, 219, 232, 233, 240, 241, 242, 254, 255, 257, 258, 259, 260, 261);
    public static final List<Integer> A1_SIGN_IDS = Arrays.asList(303, 304, 305, 306, 307, 313, 314, 315, 317, 318, 322, 323, 324, 325, 326, 329, 330, 335, 345, 346, 347, 348, 349, 350, 351, 354, 360, 362, 364, 366, 367, 368, 369, 370, 371, 372, 373, 374, 375, 376, 377, 380, 381, 382, 386, 387, 389, 390, 391, 393, 394, 395, 397, 398, 400, 401, 411, 412, 413, 415, 419, 422, 427, 430, 431);
    public static final List<Integer> A1_SITUATION_IDS = Arrays.asList(486, 487, 490, 492, 495, 499, 500, 503, 504, 505, 507, 508, 509, 517, 520, 525, 527, 528, 529, 538, 539, 540, 543, 548, 553, 556, 559, 560, 562, 565, 567, 568, 583, 592, 600);

    // --- DANH SÁCH ID HẠNG B1 (XE MÁY - 300 CÂU) ---
    public static final List<Integer> B1_RULES_IDS = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 19, 20, 21, 22, 24, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 43, 44, 45, 46, 47, 48, 49, 51, 52, 53, 54, 55, 56, 57, 59, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 80, 81, 82, 87, 88, 89, 90, 91, 92, 93, 94, 96, 97, 98, 99, 100, 102, 103, 107, 108, 109, 110, 111, 119, 123, 124, 125, 126, 137, 138, 139, 140, 141, 142, 145, 146, 151, 155, 157, 162, 163, 165, 166, 167, 178);
    public static final List<Integer> B1_CULTURE_IDS = Arrays.asList(182, 185, 187, 189, 191, 192, 193, 194, 195, 200);
    public static final List<Integer> B1_TECH_IDS = Arrays.asList(206, 215, 219, 232, 233, 240, 241, 242, 254, 255, 257, 258, 259, 260, 261, 266, 285);
    public static final List<Integer> B1_SIGN_IDS = Arrays.asList(303, 304, 305, 306, 307, 313, 314, 315, 317, 318, 322, 323, 324, 325, 326, 329, 330, 332, 333, 334, 335, 344, 345, 346, 347, 348, 349, 350, 351, 354, 355, 360, 361, 362, 364, 366, 367, 368, 369, 370, 371, 372, 373, 374, 375, 376, 377, 380, 381, 382, 383, 384, 385, 386, 387, 388, 389, 390, 391, 392, 393, 394, 395, 396, 397, 398, 400, 401, 402, 405, 406, 407, 408, 409, 410, 411, 412, 413, 415, 416, 418, 419, 420, 421, 422, 423, 424, 425, 426, 427, 430, 431, 432, 433, 434, 435, 436, 437, 438, 439, 440, 441, 442, 443, 445, 446, 450, 451, 452, 454, 455, 456, 457, 458, 459, 460, 461, 474, 475, 476, 477, 478, 479, 480, 481, 482, 483, 485);
    public static final List<Integer> B1_SITUATION_IDS = Arrays.asList(486, 487, 490, 492, 495, 499, 500, 503, 504, 505, 507, 508, 509, 517, 520, 525, 527, 528, 529, 538, 539, 540, 543, 548, 553, 556, 559, 560, 562, 565, 567, 568, 583, 592, 600);

    // --- DANH SÁCH 60 CÂU LIỆT Ô TÔ ---
    public static final List<Integer> CAR_CRITICAL_IDS = Arrays.asList(19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 30, 32, 34, 35, 47, 48, 52, 53, 55, 58, 63, 64, 65, 66, 67, 68, 70, 71, 72, 73, 74, 85, 86, 87, 88, 89, 90, 91, 92, 93, 97, 98, 102, 117, 163, 165, 167, 197, 198, 206, 215, 226, 234, 245, 246, 252, 253, 254, 255, 260);

    public static List<Question> generateExamA1(DatabaseHelper db, String licenseClass) {
        List<Question> all = db.getQuestionsByClass(licenseClass);
        List<Question> exam = new ArrayList<>();
        List<Question> crit = new ArrayList<>(), rules = new ArrayList<>(), cult = new ArrayList<>(), tech = new ArrayList<>(), signs = new ArrayList<>(), sit = new ArrayList<>();
        for (Question q : all) {
            if (q.isCritical()) crit.add(q);
            else {
                int id = q.getId();
                if (A1_RULES_IDS.contains(id)) rules.add(q);
                else if (A1_CULTURE_IDS.contains(id)) cult.add(q);
                else if (A1_TECH_IDS.contains(id)) tech.add(q);
                else if (A1_SIGN_IDS.contains(id)) signs.add(q);
                else if (A1_SITUATION_IDS.contains(id)) sit.add(q);
            }
        }
        addRandom(exam, crit, 1);
        addRandom(exam, rules, 8);
        addRandom(exam, cult, 1);
        addRandom(exam, tech, 1);
        addRandom(exam, signs, 8);
        addRandom(exam, sit, 6);
        fillToCount(exam, all, 25);
        Collections.shuffle(exam);
        return exam;
    }

    public static List<Question> generateExamB1(DatabaseHelper db) {
        List<Question> all = db.getQuestionsByClass("B1");
        List<Question> exam = new ArrayList<>();
        List<Question> crit = new ArrayList<>(), rules = new ArrayList<>(), cult = new ArrayList<>(), tech = new ArrayList<>(), signs = new ArrayList<>(), sit = new ArrayList<>();
        for (Question q : all) {
            if (q.isCritical()) crit.add(q);
            else {
                int id = q.getId();
                if (B1_RULES_IDS.contains(id)) rules.add(q);
                else if (B1_CULTURE_IDS.contains(id)) cult.add(q);
                else if (B1_TECH_IDS.contains(id)) tech.add(q);
                else if (B1_SIGN_IDS.contains(id)) signs.add(q);
                else if (B1_SITUATION_IDS.contains(id)) sit.add(q);
            }
        }
        addRandom(exam, crit, 1);
        addRandom(exam, rules, 8);
        addRandom(exam, cult, 1);
        addRandom(exam, tech, 1);
        addRandom(exam, signs, 8);
        addRandom(exam, sit, 6);
        fillToCount(exam, all, 25);
        Collections.shuffle(exam);
        return exam;
    }

    public static List<Question> generateExamCar(DatabaseHelper db, String licenseClass) {
        List<Question> all = db.getQuestionsByClass(licenseClass);
        List<Question> exam = new ArrayList<>();
        List<Question> crit = new ArrayList<>(), rules = new ArrayList<>(), cult = new ArrayList<>(), tech = new ArrayList<>(), repair = new ArrayList<>(), signs = new ArrayList<>(), sit = new ArrayList<>();
        for (Question q : all) {
            if (q.isCritical()) crit.add(q);
            else {
                int id = q.getId();
                if (id >= 1 && id <= 166) rules.add(q);
                else if (id >= 167 && id <= 205) cult.add(q);
                else if (id >= 206 && id <= 243) tech.add(q);
                else if (id >= 244 && id <= 294) repair.add(q);
                else if ((id >= 295 && id <= 475) || (id >= 286 && id <= 290)) signs.add(q);
                else if (id >= 476 && id <= 600) sit.add(q);
            }
        }
        int total = 30;
        int rC = 8, cuC = 1, tC = 1, reC = 1, siC = 9, sC = 9;
        if ("C1".equals(licenseClass)) { total = 35; rC = 10; tC = 2; siC = 10; sC = 10; }
        else if ("C".equals(licenseClass) || "D".equals(licenseClass)) { total = 40; rC = 10; tC = 2; siC = 14; sC = 11; }
        addRandom(exam, crit, 1); addRandom(exam, rules, rC); addRandom(exam, cult, cuC); addRandom(exam, tech, tC); addRandom(exam, repair, reC); addRandom(exam, signs, siC); addRandom(exam, sit, sC);
        fillToCount(exam, all, total);
        Collections.shuffle(exam);
        return exam;
    }

    private static void addRandom(List<Question> target, List<Question> source, int count) {
        if (source.isEmpty()) return;
        List<Question> copy = new ArrayList<>(source);
        Collections.shuffle(copy);
        for (int i = 0; i < Math.min(count, copy.size()); i++) {
            target.add(copy.get(i));
        }
    }

    private static void fillToCount(List<Question> target, List<Question> all, int total) {
        if (target.size() >= total) return;
        List<Question> pool = new ArrayList<>(all);
        Collections.shuffle(pool);
        for (Question q : pool) {
            boolean exists = false;
            for(Question t : target) if(t.getId() == q.getId()) { exists = true; break; }
            if (!exists) {
                target.add(q);
                if (target.size() == total) break;
            }
        }
    }
}
