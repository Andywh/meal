package com.joy.controller.product;

import com.alibaba.dubbo.config.annotation.Reference;
import com.joy.exception.SellException;
import com.joy.facade.product.IProductService;
import com.joy.req.product.CategoryForm;
import com.joy.req.product.ProductCategoryDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Created by SongLiang on 2019-09-06
 */
@Controller
@RequestMapping("/seller/category")
public class SellerCategoryController {

    @Reference(interfaceClass = IProductService.class, check = false)
    private IProductService productService;

    @GetMapping("/list")
    public ModelAndView list(Map<String, Object> map) {
        List<ProductCategoryDTO> categoryList = productService.findProductCategory();
        map.put("categoryList", categoryList);
        System.out.println("23");
        return new ModelAndView("category/list", map);
    }

    @GetMapping("/index")
    public ModelAndView index(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                              Map<String, Object> map) {
        System.out.println("1434");
        if (categoryId != null) {
            ProductCategoryDTO productCategoryDTO = productService.findProductCategoryById(String.valueOf(categoryId));
            map.put("category", productCategoryDTO);
        }
        System.out.println("123");
        return new ModelAndView("category/index", map);
    }

    @PostMapping("/save")
    public ModelAndView save(@Valid CategoryForm categoryForm,
                             BindingResult bindingResult,
                             Map<String, Object> map) {
        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/sell/seller/category/index");
            return new ModelAndView("common/error", map);
        }
        ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO();
        BeanUtils.copyProperties(categoryForm, productCategoryDTO);
        try {
            productService.save(productCategoryDTO);
        } catch (SellException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/category/index");
            return new ModelAndView("common/error", map);
        }
        map.put("url", "/sell/seller/category/list");
        return new ModelAndView("common/success", map);
    }
}
