package com.joy.facade.order;

import com.joy.resp.PageResult;
import com.joy.req.order.OrderForm;
import com.joy.resp.order.OrderIdVO;
import com.joy.resp.order.OrderVO;

import java.util.List;

/**
 * Created by SongLiang on 2019-08-24
 */
public interface IOrderService {

    /** 创建订单. */
    OrderIdVO create(OrderForm orderFrom);

    /** 查询单个订单 */
    OrderVO findOne(String orderId);

    /** 查询订单列表 */
    List<OrderVO> findList(String buyerOpenid, Integer page, Integer size);

    /** 取消订单. */
    boolean cancel(OrderVO orderVO);

    /** 完结订单. */
    boolean finish(OrderVO orderVO);

    /** 支付订单. */
    boolean paid(OrderVO orderVO);

    /** 查询订单列表. */
    PageResult<OrderVO> findList(Integer page, Integer size);

}
