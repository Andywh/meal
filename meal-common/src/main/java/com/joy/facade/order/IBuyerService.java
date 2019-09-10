package com.joy.facade.order;

import com.joy.resp.order.OrderVO;

/**
 * Created by SongLiang on 2019-08-28
 */
public interface IBuyerService {

    /*  查询一个订单. */
    OrderVO findOrderOne(String openid, String orderId);

    /* 取消订单. */
    boolean cancelOrder(String openid, String orderId);
}
