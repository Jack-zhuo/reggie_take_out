package com.zhuoyue.reggie.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhuoyue.reggie.common.R;
import com.zhuoyue.reggie.entity.Orders;
import com.zhuoyue.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){

        ordersService.submit(orders);

        return R.success("提交成功！");
    }

}
