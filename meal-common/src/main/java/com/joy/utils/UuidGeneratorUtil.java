package com.joy.utils;

import java.util.Date;
import java.util.Random;

/**
 * Created by SongLiang on 2019-08-24
 */
public class UuidGeneratorUtil {

    public static String gennId() {
        Date date = new Date();
        Random random = new Random();
        Integer randNum  = random.nextInt(4);
        return ""+ date.getTime() + "" + randNum;
    }
}
