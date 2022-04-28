package com.edwkaitwra.backend.utills;

import java.util.Calendar;
import java.util.Date;

public record Utills() {
    public static Date currentDatePlusDay(int days) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, days);
        Date date = c.getTime();
        return date;
    }
}
