package com.joy.enums;

import lombok.Getter;

import java.io.Serializable;

/**
 * Created by SongLiang on 2019-08-24
 */
@Getter
public enum ResultEnum implements Serializable {

    PRODUCT_STATUS_ERROR(8, "商品状态不正确"),

    PRODUCT_STOCK_ERROR(9, "商品库存不足"),

    PRODUCT_NOT_EXIST(10, "商品不存在"),

    ORDER_NOT_EXIST(11, "订单不存在"),

    ORDER_DETAIL_NOT_EXIST(12, "订单详情不存在"),

    ORDER_STATUS_ERROR(13, "订单状态不正确"),

    ORDER_UPDATE_FAIL(14, "订单更新失败"),

    ORDER_DETAIL_EMPTY(15, "订单详情为空"),

    ORDER_PAY_STATUS_ERROR(16, "订单支付状态不正确"),

    ORDER_OWNER_ERROR(19, "该订单不属于当前用户"),

    WECHAT_MP_ERROR(20, "微信公众账号方面错误"),

    WXPAY_NOTIFY_MONEY_VERIFY_ERROR(21, "微信支付异步通知金额校验不通过"),

    ORDER_CANCEL_SUCCESS(81, "订单取消成功"),

    ORDER_FINISH_SUCCESS(82, "订单完结成功"),

    PARAM_ERROR(1, "参数不正确"),
    ;
    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
