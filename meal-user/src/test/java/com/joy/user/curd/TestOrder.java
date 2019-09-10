//package com.joy.order.curd;
//
//import com.alibaba.dubbo.config.annotation.Reference;
//import com.joy.order.service.IOrderService;
//import com.joy.order.vo.OrderVO;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
///**
// * Created by SongLiang on 2019-08-28
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Slf4j
//public class TestOrder {
//
//    @Reference(interfaceClass = IOrderService.class, check = false)
//    private IOrderService orderService;
//
//    @Test
//    public void testSave() {
//        System.out.println("testSave");
//        OrderVO orderVO = orderService.findOne( "15668301103583");
//        log.info(orderVO.toString());
//        System.out.println(orderVO.toString());
//        boolean cancel = orderService.cancel(orderVO);
//        System.out.println(cancel);
//    }
//
//    @Test
//    public void testFinish() {
//        System.out.println("testFinish");
//        OrderVO orderVO = orderService.findOne("15668301103583");
//        log.info(orderVO.toString());
//        System.out.println(orderVO.toString());
//        boolean finish = orderService.finish(orderVO);
//        System.out.println(finish);
//    }
//
//    @Test
//    public void testPaid() {
//        System.out.println("testPaid");
//        OrderVO orderVO = orderService.findOne( "15668301103583");
//        log.info(orderVO.toString());
//        System.out.println(orderVO.toString());
//        boolean finish = orderService.paid(orderVO);
//        System.out.println(finish);
//
//    }
//
//}
//
