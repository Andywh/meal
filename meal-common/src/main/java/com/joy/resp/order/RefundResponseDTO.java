package com.joy.resp.order;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by SongLiang on 2019-09-03
 */
@Data
public class RefundResponseDTO implements Serializable {

    private String orderId;

    private Double orderAmount;

    private String outTradeNo;

    private String refundId;

    private String outRefundNo;

}
