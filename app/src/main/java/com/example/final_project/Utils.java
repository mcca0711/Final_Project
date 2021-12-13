package com.example.final_project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static String formatDate(String dateString){
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date date = inputDateFormat.parse(dateString);
            if (date != null)
                return outputDateFormat.format(date);
            else
                return "";
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
