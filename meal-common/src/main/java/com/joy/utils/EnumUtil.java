package com.joy.utils;

import com.joy.enums.CodeEnum;

/**
 * Created by SongLiang on 2019-09-04
 */
public class EnumUtil {

    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T each : enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }

}
