package com.proofIt.bicycleInsurance.utils;

import java.util.Calendar;

public class DateUtil {

    public static int getAge(int manufacturerYear){
        return Calendar.getInstance().get(Calendar.YEAR) - manufacturerYear;
    }
}
