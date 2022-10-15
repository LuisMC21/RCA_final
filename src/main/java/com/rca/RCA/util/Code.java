package com.rca.RCA.util;

public class Code {
    public static final String CATEGORY_CODE = "CAT";
    public static final int CATEGORY_LENGTH = 6;

    public static final String GRADE_CODE = "GR";
    public static final int GRADE_LENGTH = 5;

    public static final String SECTION_CODE = "SEC";
    public static final int SECTION_LENGTH = 6;

    public static final String SXG_CODE = "SG";
    public static final int SXG_LENGTH = 5;

    public static String generateCode(String prefix, long current, int maxLength) {
        String complement =  completeZero(prefix, maxLength - (prefix.length() + String.valueOf(current).length()));
        return complement + current;
    }

    private static String completeZero(String text, int quantity) {
        return text + "0".repeat(Math.max(0, quantity));
    }

}
