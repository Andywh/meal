package com.joy.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SongLiang on 2019-09-03
 */
@Data
public class PageResult<T> implements Serializable {

    private List<T> rows;

    private long total;

}
