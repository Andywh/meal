package com.joy.exception;

import com.joy.enums.ResultEnum;

import java.io.Serializable;

/**
 * Created by SongLiang on 2019-08-24
 */
public class SellException extends RuntimeException implements Serializable {

    private Integer code;

    public SellException() {

    }

    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public SellException(Integer code, String message) {
        super(message);
        this.code = code;
    }

}
