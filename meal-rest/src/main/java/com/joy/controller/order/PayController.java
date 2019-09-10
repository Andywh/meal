package com.joy.controller.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.joy.enums.ResultEnum;
import com.joy.exception.SellException;
import com.joy.facade.order.IOrderService;
import com.joy.facade.order.IPayService;
import com.joy.resp.order.OrderVO;
import com.joy.resp.order.PayResponseDTO;
import com.lly835.bestpay.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by SongLiang on 2019-09-01
 */
@Slf4j
@Controller
@RequestMapping("/pay")
public class PayController {

    @Reference(interfaceClass = IPayService.class, check = false)
    private IPayService payService;

    @Reference(interfaceClass = IOrderService.class, check = false)
    private IOrderService orderService;

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl,
                               Map<String, Object> map) {
        // 1. 查询订单
        OrderVO orderVO = orderService.findOne(orderId);
        if (orderVO == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        // 2. 发起支付
        PayResponseDTO payResponse = payService.create(orderVO);
        log.info("【微信支付】request={}", JsonUtil.toJson(payResponse));
        log.info("【微信支付】returnUrl={}", returnUrl);
        map.put("payResponse", payResponse);
        map.put("returnUrl", URLDecoder.decode(returnUrl));


        return new ModelAndView("pay/create", map);
    }

    @PostMapping("/notify")
    public ModelAndView notify(@RequestBody String notifyData) {
        log.info("【微信支付】 notify，notifyData={}", notifyData);
        payService.notify(notifyData);

        // 返回给微信处理结果
        return new ModelAndView("pay/success");
    }

}
