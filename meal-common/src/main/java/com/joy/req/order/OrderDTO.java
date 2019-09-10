package com.joy.req.order;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.joy.resp.order.OrderDetailVO;
import com.joy.utils.serializer.Date2LongSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by SongLiang on 2019-08-24
 */
@Data
public class OrderDTO {

    /**
     * 订单 id
     */
    private String orderId;

    /**
     * 买家名字
     */
    private String buyerName;

    /**
     * 买家手机号
     */
    private String buyerPhone;

    /**
     * 买家地址
     */
    private String buyerAddress;

    /**
     * 买家微信 Openid
     */
    private String buyerOpenid;

    /**
     * 订单总金额
     */
    private BigDecimal orderAmount;

    /**
     * 订单状态，默认为 0 新下单
     */
    private Integer orderStatus;

    /**
     * 支付状态，默认为 0 未支付
     */
    private Integer payStatus;

    /**
     * 创建时间
     */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    List<OrderDetailVO> orderDetailList;

}
