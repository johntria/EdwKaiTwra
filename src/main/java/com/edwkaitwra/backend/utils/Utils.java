package com.edwkaitwra.backend.utils;

import java.util.Calendar;
import java.util.Date;

public record Utils() {

    public static Date currentDatePlusDay(int days) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, days);
        Date date = c.getTime();
        return date;
    }

    public static String getStringBeforeSpecialCharacterAsUppercase(String str, char specialCharacter) {
        return str.substring(0, str.indexOf(specialCharacter)).toUpperCase();
    }
}
