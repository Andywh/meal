package com.joy.product.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.joy.product.model.ProductInfo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * <p>
 * 商品信息表 Mapper 接口
 * </p>
 *
 * @author songliang
 * @since 2019-08-25
 */
public interface ProductInfoMapper extends BaseMapper<ProductInfo> {

    List<ProductInfo> selectList(RowBounds rowBounds);

}
