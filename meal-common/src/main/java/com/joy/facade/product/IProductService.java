package com.joy.facade.product;


import com.joy.req.order.CartDTO;
import com.joy.req.product.ProductCategoryDTO;
import com.joy.req.product.ProductInfoDTO;
import com.joy.resp.PageResult;
import com.joy.resp.product.ProductVO;

import java.util.List;

/**
 * Created by SongLiang on 2019-08-25
 */
public interface IProductService {

    /**
     * 根据商品 id 获取商品信息
     * @param productId
     * @return
     */
    ProductInfoDTO findById(String productId);

    /**
     *
     * @param categoryTypeList
     * @return
     */
    List<ProductInfoDTO> findByCatetoryTypeIn(List<Integer> categoryTypeList);

    /**
     * 查询所有商品（卖家）
     * @return
     */
    List<ProductVO> findAll();

    /**
     * 查询所有商品（卖家）
     * @return
     */
    List<ProductCategoryDTO> findAllCategory();

    /**
     * 查询所有商品类目 (卖家)
     * @return
     */
    List<ProductCategoryDTO> findProductCategory();

    /**
     * 查询商品类目 (卖家)
     * @return
     */
    ProductCategoryDTO findProductCategoryById(String categoryId);

    /**
     * 查询所有在架商品列表（买家）
     * @return
     */
    List<ProductInfoDTO> findUpAll();

    /**
     * 查询所有在架商品列表（买家）
     * @return
     */
    PageResult<ProductInfoDTO> findList(Integer page, Integer size);

    /**
     * 存储商品
     * @return
     */
    boolean save(ProductInfoDTO productInfoDTO);

    /**
     * 存储类目
     * @return
     */
    boolean save(ProductCategoryDTO productCategoryDTO);

//    boolean update(ProductInfoDTO productInfoDTO);

    void increaseStock(List<CartDTO> cartDTOList);

    void decreaseStock(List<CartDTO> cartDTOList);

    // 上架
    Boolean onSale(String productId);

    // 下架
    Boolean offSale(String productId);

}
