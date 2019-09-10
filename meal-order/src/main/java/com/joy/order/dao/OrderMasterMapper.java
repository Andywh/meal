package com.joy.order.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.joy.order.model.OrderMaster;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author songliang
 * @since 2019-08-24
 */
public interface OrderMasterMapper extends BaseMapper<OrderMaster> {

    List<OrderMaster> selectList(RowBounds rowBounds);

}
