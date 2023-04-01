package com.rca.RCA.util;

public class Operations {

    public  static String trimBrackets(String message){
        return message.replaceAll("[\\[\\]]", "");
    }
}
