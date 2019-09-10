package com.joy.order.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.joy.enums.ResultEnum;
import com.joy.exception.SellException;
import com.joy.facade.order.IOrderService;
import com.joy.facade.order.IPayService;
import com.joy.resp.order.OrderVO;
import com.joy.resp.order.PayResponseDTO;
import com.joy.resp.order.RefundResponseDTO;
import com.joy.utils.MathUtil;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.lly835.bestpay.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by SongLiang on 2019-09-01
 */
@Slf4j
@Component
@Service(interfaceClass = IPayService.class, loadbalance = "roundrobin")
public class PayService implements IPayService {

    private static final String ORDER_NAME = "微信点餐订单";

    @Autowired
    private BestPayServiceImpl bestPayService;

    @Autowired
    private IOrderService orderService;

    @Override
    public PayResponseDTO create(OrderVO orderVO) {
        PayRequest payRequest = new PayRequest();
        payRequest.setOpenid(orderVO.getBuyerOpenid());
        payRequest.setOrderAmount(orderVO.getOrderAmount().doubleValue());
        payRequest.setOrderId(orderVO.getOrderId());
        payRequest.setOrderName(ORDER_NAME);
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("【微信支付】发起支付 request={}", JsonUtil.toJson(payRequest));
        PayResponse payResponse = bestPayService.pay(payRequest);
        log.info("【微信支付】发起支付 response={}", JsonUtil.toJson(payResponse));
        PayResponseDTO payResponseDTO = new PayResponseDTO();
        BeanUtils.copyProperties(payResponse, payResponseDTO);
        return payResponseDTO;
    }

    @Override
    public PayResponseDTO notify(String notifyData) {
        //1. 验证签名
        //2. 支付的状态
        //3. 支付金额
        //4. 支付人（下单人 == 支付人）
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("【微信支付】异步通知，payResponse={}", JsonUtil.toJson(payResponse));

        // 查询订单
        OrderVO orderVO = orderService.findOne(payResponse.getOrderId());
        if (orderVO == null) {
            log.info("【微信支付】异步通知，订单不存在，orderId={}", payResponse.getOrderId());
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        // 判断金额是否一致
        if (!MathUtil.equals(payResponse.getOrderAmount(), orderVO.getOrderAmount().doubleValue())) {
            log.info("【微信支付】异步通知，订单金额不一致，orderId={}, 微信通知金额={}, 系统金额={}",
                    payResponse.getOrderId(),
                    payResponse.getOrderAmount(),
                    orderVO.getOrderAmount());
            throw new SellException(ResultEnum.WXPAY_NOTIFY_MONEY_VERIFY_ERROR);
        }

        // 修改订单的支付状态
        orderService.paid(orderVO);

        PayResponseDTO payResponseDTO = new PayResponseDTO();
        BeanUtils.copyProperties(payResponse, payResponseDTO);
        return payResponseDTO;
    }

    /**
     * 退款
     * @param orderVO
     */
    @Override
    public RefundResponseDTO refund(OrderVO orderVO) {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderId(orderVO.getOrderId());
        refundRequest.setOrderAmount(orderVO.getOrderAmount().doubleValue());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("【微信退款】request={}", refundRequest);
        RefundResponse refund = bestPayService.refund(refundRequest);
        log.info("【微信图款】response={}", refundRequest);
        RefundResponseDTO refundResponseDTO = new RefundResponseDTO();
        BeanUtils.copyProperties(refund, refundResponseDTO);
        return refundResponseDTO;
    }

}
