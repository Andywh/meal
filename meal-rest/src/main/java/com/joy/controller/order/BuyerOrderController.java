package com.joy.controller.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.joy.enums.ResultEnum;
import com.joy.exception.SellException;
import com.joy.facade.order.IBuyerService;
import com.joy.facade.order.IOrderService;
import com.joy.req.order.OrderForm;
import com.joy.resp.ResultVO;
import com.joy.resp.order.OrderIdVO;
import com.joy.resp.order.OrderVO;
import com.joy.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by SongLiang on 2019-08-26
 */
@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {

    @Reference(interfaceClass = IOrderService.class, check = false)
    private IOrderService orderService;

    @Reference(interfaceClass = IBuyerService.class, check = false)
    private IBuyerService buyerService;

    // 创建订单
    @RequestMapping("/create")
    public ResultVO create(@Valid OrderForm orderForm,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("【创建订单】参数不正确，orderForm={}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }

        OrderIdVO list = orderService.create(orderForm);
        return ResultVOUtil.success(list);
    }

    // 订单列表
    @RequestMapping("/list")
    public ResultVO list(@RequestParam("openid") String openid,
                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        List<OrderVO> list = orderService.findList(openid, page, size);
        return ResultVOUtil.success(list);
    }

    // 订单详情
    @RequestMapping("/detail")
    public ResultVO detail(@RequestParam("openid") String openid,
                           @RequestParam(value = "orderId", required = true) String orderId) {
        OrderVO orderVO = buyerService.findOrderOne(openid, orderId);
        return ResultVOUtil.success(orderVO);
    }

    // 取消订单
    @PostMapping("/cancel")
    public ResultVO cancel(@RequestParam("openid") String openid,
                           @RequestParam("orderId") String orderId) {
        buyerService.cancelOrder(openid, orderId);
        return ResultVOUtil.success();
    }

}
