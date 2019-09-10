package com.joy.order.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.joy.convert.OrderForm2OrderDTOConverter;
import com.joy.enums.OrderStatusEnum;
import com.joy.enums.PayStatusEnum;
import com.joy.enums.ResultEnum;
import com.joy.exception.SellException;
import com.joy.facade.order.IOrderService;
import com.joy.facade.order.IPayService;
import com.joy.facade.product.IProductService;
import com.joy.order.dao.OrderDetailMapper;
import com.joy.order.dao.OrderMasterMapper;
import com.joy.order.model.OrderDetail;
import com.joy.order.model.OrderMaster;
import com.joy.req.order.CartDTO;
import com.joy.req.order.OrderDTO;
import com.joy.req.order.OrderForm;
import com.joy.req.product.ProductInfoDTO;
import com.joy.resp.PageResult;
import com.joy.resp.order.OrderDetailVO;
import com.joy.resp.order.OrderIdVO;
import com.joy.resp.order.OrderVO;
import com.joy.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by SongLiang on 2019-08-24
 */
@Slf4j
@Component
@Service(interfaceClass = IOrderService.class, loadbalance = "roundrobin")
public class OrderService implements IOrderService {

    @Autowired
    private OrderMasterMapper orderMasterMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Reference(interfaceClass = IProductService.class, check = false)
    private IProductService productService;

    @Autowired
    private IPayService payService;

    @Override
    @Transactional
    public OrderIdVO create(OrderForm orderForm) {
        String orderId = KeyUtil.genUniqueKey();
        BigDecimal orderAmount = new BigDecimal(0);

        // 1. 查询商品（数量，价格）
        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);
        for (OrderDetailVO orderDetailVO : orderDTO.getOrderDetailList()) {
            ProductInfoDTO productInfoDTO = productService.findById(orderDetailVO.getProductId());
            if (productInfoDTO == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            // 2. 计算订单总价
            orderAmount = productInfoDTO.getProductPrice()
                    .multiply(new BigDecimal(orderDetailVO.getProductQuantity()))
                    .add(orderAmount);

            // 订单详情入库
            orderDetailVO.setDetailId(KeyUtil.genUniqueKey());
            orderDetailVO.setOrderId(orderId);
            BeanUtils.copyProperties(productInfoDTO, orderDetailVO);
            log.info(orderDetailVO.toString());
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(orderDetailVO, orderDetail);
            orderDetailMapper.insert(orderDetail);
        }

        // 3. 写入订单数据库（orderMaster 和 orderDetail）
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
//        orderMaster.setBuyerOpenid("oTgZpwcTd3dzLaO4BZLZpxRs-DvY");
        log.info(orderMaster.toString());
        orderMasterMapper.insert(orderMaster);

        // 4. 扣库存
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e ->
                new CartDTO(e.getProductId(), e.getProductQuantity())
        ).collect(Collectors.toList());
        log.info(cartDTOList.toString());
        productService.decreaseStock(cartDTOList);
        log.info("after decrease stock");
        OrderIdVO orderIdVO = new OrderIdVO();
        orderIdVO.setOrderId(orderId);
        return orderIdVO;
    }

    @Override
    public List<OrderVO> findList(String buyerOpenid, Integer page, Integer size) {
        EntityWrapper<OrderMaster> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("buyer_openid", buyerOpenid);
        List<OrderMaster> orderMasterList = orderMasterMapper.selectPage(new RowBounds(page, size), entityWrapper);
        List<OrderVO> orderVOList = new ArrayList<>();
        for (OrderMaster orderMaster : orderMasterList) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orderMaster, orderVO);
//            orderVO.setUpdateTime(orderMaster.getUpdateTime().getTime());
//            orderVO.setCreateTime(orderMaster.getCreateTime().getTime());
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }

    @Override
    public OrderVO findOne(String orderId) {
        // 1. 获取父单
        OrderMaster orderMaster = orderMasterMapper.selectById(orderId);
        if (orderMaster == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        // 2. 获取子单详情
        EntityWrapper<OrderDetail> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("order_id", orderId);
        List<OrderDetail> orderDetailList = orderDetailMapper.selectList(entityWrapper);
        if (orderDetailList == null) {
            throw new SellException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }

        // 3. 组装 VO
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orderMaster, orderVO);
        List<OrderDetailVO> orderDetailVOList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetailList) {
            OrderDetailVO orderDetailVO = new OrderDetailVO();
            BeanUtils.copyProperties(orderDetail, orderDetailVO);
//            orderDetailVO.setCreateTime(orderDetail.getCreateTime().getTime()/1000);
//            orderDetailVO.setUpdateTime(orderDetail.getUpdateTime().getTime()/1000);
            orderDetailVOList.add(orderDetailVO);
        }
        orderVO.setOrderDetailList(orderDetailVOList);
        return orderVO;
    }

    @Override
    @Transactional
    public boolean cancel(OrderVO orderVO) {
        OrderMaster orderMaster = new OrderMaster();

        // 判断订单状态
        if (!orderVO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【取消订单】订单状态不正确，orderId={}, orderStatus={}", orderVO.getOrderId(), orderVO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        // 修改订单状态
        orderVO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderVO, orderMaster);
        Integer result = orderMasterMapper.updateById(orderMaster);
        if (result == 0) {
            log.error("【取消订单】更新失败, orderMaster={}", orderVO);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        // 返回库存
        if (CollectionUtils.isEmpty(orderVO.getOrderDetailList())) {
            log.error("【取消订单】订单中无商品详情，orderVO={}", orderVO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList = orderVO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.increaseStock(cartDTOList);

        // 如果已支付，需要退款
        if (orderVO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            payService.refund(orderVO);
        }
        return true;
    }

    @Override
    public boolean finish(OrderVO orderVO) {
        // 判断订单状态
        if (!orderVO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.info("【完结订单】订单状态不正确，orderId={}，orderStatus={}", orderVO.getOrderId(), orderVO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        // 修改订单状态
        orderVO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderVO, orderMaster);
        Integer result = orderMasterMapper.updateById(orderMaster);
        if (result == 0) {
            log.error("【完结订单】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return result == 1 ? true : false;
    }

    @Override
    public boolean paid(OrderVO orderVO) {
        // 判断订单状态
        if (!orderVO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【支付订单】订单状态不正确，orderId={}, orderStatus={}", orderVO.getOrderId(), orderVO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        // 判断支付状态
        if (!orderVO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("【支付订单】订单支付状态不正确，orderId={}, orderStatus={}", orderVO.getOrderId(), orderVO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }

        // 修改支付状态
        orderVO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderVO, orderMaster);
        Integer result = orderMasterMapper.updateById(orderMaster);
        if (result == 0) {
            log.error("【支付订单】支付订单失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return result == 1 ? true : false;
    }

    @Override
    public PageResult<OrderVO> findList(Integer pageNum, Integer size) {
        Page<OrderMaster> page = new Page<>(pageNum, size);
        page.setRecords(orderMasterMapper.selectList(page));

        List<OrderVO> orderVOList = new ArrayList<>();
        for (OrderMaster orderMaster : page.getRecords()) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orderMaster, orderVO);
            orderVOList.add(orderVO);
        }
        PageResult<OrderVO> pageResult = new PageResult<>();
        pageResult.setRows(orderVOList);
        pageResult.setTotal(page.getPages());
        return pageResult;
    }

}
