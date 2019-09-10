package com.joy.product.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.joy.enums.ProductStatusEnum;
import com.joy.enums.ResultEnum;
import com.joy.exception.SellException;
import com.joy.facade.product.IProductService;
import com.joy.product.converter.ProductInfo2FoodVOConverter;
import com.joy.product.converter.ProductInfo2ProductInfoDTO;
import com.joy.product.dao.ProductCategoryMapper;
import com.joy.product.dao.ProductInfoMapper;
import com.joy.product.model.ProductCategory;
import com.joy.product.model.ProductInfo;
import com.joy.req.order.CartDTO;
import com.joy.req.product.ProductCategoryDTO;
import com.joy.req.product.ProductInfoDTO;
import com.joy.resp.PageResult;
import com.joy.resp.order.OrderVO;
import com.joy.resp.product.ProductInfoVO;
import com.joy.resp.product.ProductVO;
import com.joy.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by SongLiang on 2019-08-25
 */
@Slf4j
@Component
@Service(interfaceClass = IProductService.class, loadbalance = "roundrobin")
public class ProductService implements IProductService {

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public List<ProductVO> findAll() {
        // 1. 查询所有的上架商品
        EntityWrapper ewp = new EntityWrapper<ProductInfo>();
        ewp.eq("product_status", 0);
        List<ProductInfo> productInfoList = productInfoMapper.selectList(ewp);

        // 2. 查询类目（一次性查询）
        List<Integer> categoryTypeList = productInfoList.stream()
                .map(e -> e.getCategoryType())
                .collect(Collectors.toList());
        EntityWrapper ew = new EntityWrapper<ProductCategory>();
        ew.in("category_type", categoryTypeList);
        List<ProductCategory> productCategoryList = productCategoryMapper.selectList(ew);

        // 3. 数据拼接
        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryName(productCategory.getCategoryName());
            productVO.setCategoryType(productCategory.getCategoryType());
            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo : productInfoList) {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
                    productInfoVOList.add(ProductInfo2FoodVOConverter.convert(productInfo));
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }
        log.info(productVOList.toString());
        return productVOList;
    }

    @Override
    public List<ProductCategoryDTO> findAllCategory() {
        List<ProductCategory> productCategoryList = productCategoryMapper.selectList(null);
        List<ProductCategoryDTO> productVOList = new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList) {
            ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO();
            BeanUtils.copyProperties(productCategory, productCategoryDTO);
            productVOList.add(productCategoryDTO);
        }
        return productVOList;
    }

    @Override
    public List<ProductCategoryDTO> findProductCategory() {
        List<ProductCategory> productCategoryList = productCategoryMapper.selectList(null);
        List<ProductCategoryDTO> categoryList = new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList) {
            ProductCategoryDTO category = new ProductCategoryDTO();
            BeanUtils.copyProperties(productCategory, category);
            categoryList.add(category);
            log.info("category: {}", category);
        }
        return categoryList;
    }

    @Override
    public ProductCategoryDTO findProductCategoryById(String categoryId) {
        ProductCategory productCategory = productCategoryMapper.selectById(categoryId);
        ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO();
        BeanUtils.copyProperties(productCategory, productCategoryDTO);
        return productCategoryDTO;
    }

    @Override
    public List<ProductInfoDTO> findUpAll() {
        EntityWrapper ewp = new EntityWrapper<ProductInfo>();
        ewp.eq("product_status", ProductStatusEnum.UP.getCode());
        List<ProductInfo> productInfoList = productInfoMapper.selectList(ewp);
        return ProductInfo2ProductInfoDTO.convert(productInfoList);
    }

    @Override
    public PageResult<ProductInfoDTO> findList(Integer pageNum, Integer size) {
        Page<ProductInfo> page = new Page<>(pageNum, size);
        page.setRecords(productInfoMapper.selectList(page));

        List<ProductInfoDTO> productInfoDTOList = new ArrayList<>();
        for (ProductInfo productInfo : page.getRecords()) {
            ProductInfoDTO productInfoDTO = new ProductInfoDTO();
            BeanUtils.copyProperties(productInfo, productInfoDTO);
            productInfoDTOList.add(productInfoDTO);
        }
        PageResult<ProductInfoDTO> pageResult = new PageResult<>();
        pageResult.setRows(productInfoDTOList);
        pageResult.setTotal(page.getPages());
        return pageResult;
    }

    @Override
    public boolean save(ProductInfoDTO productInfoDTO) {
        ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(productInfoDTO, productInfo);
        Integer result = null;
        if (StringUtils.isEmpty(productInfoDTO.getProductId())) {
            productInfo.setProductId(KeyUtil.genUniqueKey());
            result = productInfoMapper.insert(productInfo);
        } else {
            result = productInfoMapper.updateById(productInfo);
        }
        return result == 1 ? true : false;
    }

    @Override
    public boolean save(ProductCategoryDTO productCategoryDTO) {
        ProductCategory productCategory = new ProductCategory();
        BeanUtils.copyProperties(productCategoryDTO, productCategory);
        Integer result = null;
        if (productCategory.getCategoryId() != null) {
            result = productCategoryMapper.updateById(productCategory);
        } else {
            result = productCategoryMapper.insert(productCategory);
        }
        return result == 1 ? true : false;
    }

    @Override
    @Transactional
    public void increaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList) {
            ProductInfo productInfo = productInfoMapper.selectById(cartDTO.getProductId());
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            log.info("productQuantity:{}", cartDTO.getProductQuantity());
            log.info("productInfo.getProductStock():{}", productInfo.getProductStock());
            Integer result = productInfo.getProductStock() + cartDTO.getProductQuantity();
            log.info("result={}", result);
            productInfo.setProductStock(result);
            productInfoMapper.updateById(productInfo);
        }
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList) {
            ProductInfo productInfo = productInfoMapper.selectById(cartDTO.getProductId());
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            Integer result = productInfo.getProductStock() - cartDTO.getProductQuantity();
            if (result < 0) {
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }

            productInfo.setProductStock(result);
            productInfoMapper.updateById(productInfo);
        }
    }

    @Override
    public Boolean onSale(String productId) {
        ProductInfo productInfo = productInfoMapper.selectById(productId);
        if (productInfo == null) {
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        if (productInfo.getProductStatus() == ProductStatusEnum.UP.getCode()) {
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        // 更新
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        Integer result = productInfoMapper.updateById(productInfo);
        return result == 1 ? true : false;
    }

    @Override
    public Boolean offSale(String productId) {
        ProductInfo productInfo = productInfoMapper.selectById(productId);
        if (productInfo == null) {
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        if (productInfo.getProductStatus() == ProductStatusEnum.DOWN.getCode()) {
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        // 更新
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        Integer result = productInfoMapper.updateById(productInfo);
        return result == 1 ? true : false;
    }

    @Override
    public ProductInfoDTO findById(String productId) {
        ProductInfoDTO productInfoDTO = new ProductInfoDTO();
        ProductInfo productInfo = productInfoMapper.selectById(productId);
        BeanUtils.copyProperties(productInfo, productInfoDTO);
        return productInfoDTO;
    }

    @Override
    public List<ProductInfoDTO> findByCatetoryTypeIn(List<Integer> categoryTypeList) {
        EntityWrapper ew = new EntityWrapper<ProductCategory>();
        ew.in("category_type", categoryTypeList);
        List<ProductInfo> productInfoList = productInfoMapper.selectList(ew);
        return ProductInfo2ProductInfoDTO.convert(productInfoList);
    }

}
