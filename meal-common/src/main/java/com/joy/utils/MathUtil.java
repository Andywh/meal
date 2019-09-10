package com.joy.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by SongLiang on 2019-09-02
 */
@Slf4j
public class MathUtil {

    private static final Double MONEY_RANGE = 0.01;

    /**
     *
     * @param d1
     * @param d2
     * @return
     *
     */
    public static Boolean equals(Double d1, Double d2) {
        Double result = Math.abs(d1 - d2);
        if (result < MONEY_RANGE) {
            return true;
        } else {
            return false;
        }
    }

}
