package com.example.saurabh.driver.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GlobalValidation {

    public static boolean isEmailValid(String email) {

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPasswordLength(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            if (target.length() < 6 || target.length() > 15) {
                return false;
            } else {
                return true;
            }
        }

    }
}
