package com.joy.order.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.joy.enums.ResultEnum;
import com.joy.exception.SellException;
import com.joy.facade.order.IBuyerService;
import com.joy.resp.order.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by SongLiang on 2019-08-28
 */
@Slf4j
@Component
@Service(interfaceClass = IBuyerService.class, loadbalance = "roundrobin")
public class BuyerService implements IBuyerService {

    @Autowired
    private OrderService orderService;

    @Override
    public OrderVO findOrderOne(String openid, String orderId) {
        return checkOrderOwner(openid, orderId);
    }

    @Override
    public boolean cancelOrder(String openid, String orderId) {
        OrderVO orderVO = checkOrderOwner(openid, orderId);
        if (orderVO == null) {
            log.error("【取消订单】查不到该订单, orderId={}", orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        return orderService.cancel(orderVO);
    }

    private OrderVO checkOrderOwner(String openid, String orderId) {
        OrderVO orderVO = orderService.findOne(orderId);
        if (orderVO == null) {
            return null;
        }
        // 判断是否是自己的订单
        if (!orderVO.getBuyerOpenid().equalsIgnoreCase(openid)) {
            log.error("【查询订单】订单的 openid 不一致，openid={}, orderVO={}", openid, orderVO);
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }
        return orderVO;
    }
}
