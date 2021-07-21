package com.annis.model.produce;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * author:Lee
 * date:2021/7/17
 * Describe:
 */
public class DateDateListGenerate {
    static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);

    public static List<String> createOrderDate(int size) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add(dateFormat.format(new Date()) + "    " + i);
        }
        return result;
    }
}
