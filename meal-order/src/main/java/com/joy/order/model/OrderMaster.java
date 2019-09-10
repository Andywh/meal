package com.joy.order.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author songliang
 * @since 2019-08-24
 */
@TableName("order_master")
public class OrderMaster extends Model<OrderMaster> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "order_id", type = IdType.INPUT)
    private String orderId;
    /**
     * 买家名字
     */
    @TableField("buyer_name")
    private String buyerName;
    /**
     * 买家电话
     */
    @TableField("buyer_phone")
    private String buyerPhone;
    /**
     * 买家地址
     */
    @TableField("buyer_address")
    private String buyerAddress;
    /**
     * 买家微信openid
     */
    @TableField("buyer_openid")
    private String buyerOpenid;
    /**
     * 订单总金额
     */
    @TableField("order_amount")
    private BigDecimal orderAmount;
    /**
     * 订单状态, 默认为新下单
     */
    @TableField("order_status")
    private Integer orderStatus;
    /**
     * 支付状态, 默认未支付
     */
    @TableField("pay_status")
    private Integer payStatus;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public String getBuyerOpenid() {
        return buyerOpenid;
    }

    public void setBuyerOpenid(String buyerOpenid) {
        this.buyerOpenid = buyerOpenid;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }

    @Override
    public String toString() {
        return "OrderMaster{" +
        ", orderId=" + orderId +
        ", buyerName=" + buyerName +
        ", buyerPhone=" + buyerPhone +
        ", buyerAddress=" + buyerAddress +
        ", buyerOpenid=" + buyerOpenid +
        ", orderAmount=" + orderAmount +
        ", orderStatus=" + orderStatus +
        ", payStatus=" + payStatus +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
