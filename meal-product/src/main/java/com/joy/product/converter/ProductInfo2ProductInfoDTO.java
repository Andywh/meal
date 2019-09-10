package com.joy.product.converter;

import com.joy.product.model.ProductInfo;
import com.joy.req.product.ProductInfoDTO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SongLiang on 2019-08-27
 */
public class ProductInfo2ProductInfoDTO {

    public static ProductInfoDTO convert(ProductInfo productInfo) {
        ProductInfoDTO productInfoDTO = new ProductInfoDTO();
        BeanUtils.copyProperties(productInfo, productInfoDTO);
        return productInfoDTO;
    }

    public static List<ProductInfoDTO> convert(List<ProductInfo> productInfoList) {
        List<ProductInfoDTO> productInfoDTOList = new ArrayList<>();
        for (ProductInfo productInfo : productInfoList) {
            productInfoDTOList.add(convert(productInfo));
        }
        return productInfoDTOList;
    }

}
