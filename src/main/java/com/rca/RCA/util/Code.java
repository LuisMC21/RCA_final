package com.rca.RCA.util;

public class Code {
    public static final String GRADE_CODE = "GR";
    public static final int GRADE_LENGTH = 5;

    public static final String ROL_CODE = "ROL";
    public static final int ROL_LENGTH = 6;

    public static final String USUARIO_CODE = "USU";
    public static final int USUARIO_LENGTH = 8;

    public static final String IMAGEN_CODE = "IMG";
    public static final int IMAGEN_LENGTH = 6;

    public static final String NEWS_CODE = "NEWS";
    public static final int NEWS_LENGTH = 6;

    public static final String APO_CODE = "APO";
    public static final int APO_LENGTH = 8;

    public static final String ALU_CODE = "ALU";
    public static final int ALU_LENGTH = 8;

    public static final String ASIS_CODE = "ASIS";
    public static final int ASIS_LENGTH = 6;

    public static final String SCHOOL_YEAR_CODE = "ANIO";
    public static final int SCHOOL_YEAR_LENGTH = 7;

    public static final String SECTION_CODE = "SEC";
    public static final int SECTION_LENGTH = 6;

    public static final String CLASS_CODE = "CLASS";
    public static final int CLASS_LENGTH = 6;

    public static final String CLASSROOM_CODE = "AUL";
    public static final int CLASSROOM_LENGTH = 6;

    public static final String PERIOD_CODE = "PER";
    public static final int PERIOD_LENGTH = 6;
    
    public static final String COURSE_CODE = "CURS";
    public static final int COURSE_LENGTH = 7;

    public static final String TEACHER_CODE = "DCN";
    public static final int TEACHER_LENGTH = 6;

    public static final String CXD_CODE = "CXD";
    public static final int CXD_LENGTH = 6;


    public static final String EVA_CODE = "EVA";

    public static final int EVA_LENGTH = 6;

    public static final String MAT_CODE = "MAT";
    public static final int MAT_LENGTH = 9;

    public static final String RUTA_IMAGENES = "src/main/resources/images";

    public static final String RUTA_SERVIDOR = "http://localhost:8080/";


    public static String generateCode(String prefix, long current, int maxLength) {
        String complement =  completeZero(prefix, maxLength - (prefix.length() + String.valueOf(current).length()));
        return complement + current;
    }

    private static String completeZero(String text, int quantity) {
        return text + "0".repeat(Math.max(0, quantity));
    }

}
