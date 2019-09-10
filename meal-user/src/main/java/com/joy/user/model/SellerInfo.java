package com.joy.user.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 卖家信息表
 * </p>
 *
 * @author songliang
 * @since 2019-09-06
 */
@TableName("seller_info")
public class SellerInfo extends Model<SellerInfo> {

    private static final long serialVersionUID = 1L;

    @TableId("seller_id")
    private String sellerId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */

    private String password;
    /**
     * 微信openid
     */
    private String openid;
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


    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
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
        return this.sellerId;
    }

    @Override
    public String toString() {
        return "SellerInfo{" +
        ", sellerId=" + sellerId +
        ", username=" + username +
        ", password=" + password +
        ", openid=" + openid +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
