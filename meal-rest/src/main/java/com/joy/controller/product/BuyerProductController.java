package com.joy.controller.product;

import com.alibaba.dubbo.config.annotation.Reference;
import com.joy.facade.product.IProductService;
import com.joy.resp.ResultVO;
import com.joy.resp.product.ProductVO;
import com.joy.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by SongLiang on 2019-08-28
 */
@RestController
@RequestMapping("/buyer/product")
@Slf4j
public class BuyerProductController {

    @Reference(interfaceClass = IProductService.class, check = false)
    private IProductService productService;

    @GetMapping("/list")
    public ResultVO list(@RequestParam(value = "sellerId", required = false) String sellerId) {
        List<ProductVO> list = productService.findAll();
        return ResultVOUtil.success(list);
    }

}
