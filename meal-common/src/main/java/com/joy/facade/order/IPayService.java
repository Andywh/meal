package com.joy.facade.order;

import com.joy.resp.order.PayResponseDTO;
import com.joy.resp.order.RefundResponseDTO;
import com.joy.resp.order.OrderVO;

/**
 * 支付
 * Created by SongLiang on 2019-09-01
 */
public interface IPayService {

    PayResponseDTO create(OrderVO orderVO);

    PayResponseDTO notify(String notifyData);

    RefundResponseDTO refund(OrderVO orderVO);

}
