package com.joy.controller.product;

import com.alibaba.dubbo.config.annotation.Reference;
import com.joy.facade.product.IProductService;
import com.joy.resp.ResultVO;
import com.joy.resp.product.ProductVO;
import com.joy.utils.ResultVOUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by SongLiang on 2019-08-25
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Reference(interfaceClass = IProductService.class, check = false)
    private IProductService productService;

    @RequestMapping("/list")
    public ResultVO list() {
        List<ProductVO> list = productService.findAll();

        return ResultVOUtil.success(list);

//        return null;
    }

}
