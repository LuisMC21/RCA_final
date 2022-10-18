package com.rca.RCA.util;

public class Code {
    public static final String GRADE_CODE = "GR";
    public static final int GRADE_LENGTH = 5;

    public static final String ROL_CODE = "ROL";
    public static final int ROL_LENGTH = 6;

    public static final String USUARIO_CODE = "USU";
    public static final int USUARIO_LENGTH = 6;

    public static final String IMAGEN_CODE = "IMG";
    public static final int IMAGEN_LENGTH = 6;

    public static final String NEWS_CODE = "NEWS";
    public static final int NEWS_LENGTH = 6;

    public static final String APO_CODE = "APO";
    public static final int APO_LENGTH = 6;

    public static final String ALU_CODE = "ALU";
    public static final int ALU_LENGTH = 6;

    public static final String ASIS_CODE = "ASIS";
    public static final int ASIS_LENGTH = 6;
    public static final String SECTION_CODE = "SEC";
    public static final int SECTION_LENGTH = 6;
    public static final String SXG_CODE = "SG";
    public static final int SXG_LENGTH = 5;

    public static final String CLASS_CODE = "CLASS";
    public static final int CLASS_LENGTH = 6;

    public static String generateCode(String prefix, long current, int maxLength) {
        String complement =  completeZero(prefix, maxLength - (prefix.length() + String.valueOf(current).length()));
        return complement + current;
    }

    private static String completeZero(String text, int quantity) {
        return text + "0".repeat(Math.max(0, quantity));
    }

}
