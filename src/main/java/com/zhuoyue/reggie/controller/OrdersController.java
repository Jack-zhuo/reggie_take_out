package com.zhuoyue.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhuoyue.reggie.common.R;
import com.zhuoyue.reggie.dto.OrderDto;
import com.zhuoyue.reggie.entity.OrderDetail;
import com.zhuoyue.reggie.entity.Orders;
import com.zhuoyue.reggie.service.OrderDetailService;
import com.zhuoyue.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){

        ordersService.submit(orders);

        return R.success("提交成功！");
    }

    @GetMapping("/userPage")
    public R<Page> getPage(Integer page,Integer pageSize){

        Page<Orders> ordersPage = new Page<>(page, pageSize);
        Page<OrderDto> orderDtoPage = new Page<>();

        LambdaQueryWrapper<Orders> olqw = new LambdaQueryWrapper<>();
        olqw.orderByDesc(Orders::getOrderTime);

        ordersService.page(ordersPage,olqw);

        BeanUtils.copyProperties(ordersPage,orderDtoPage,"records");

        //获取order的records赋给，orderDto类
        List<Orders> records = ordersPage.getRecords();

        List<OrderDto> orderDtos = records.stream().map((item) -> {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(item, orderDto);

            LambdaQueryWrapper<OrderDetail> lqw = new LambdaQueryWrapper<>();
            lqw.eq(OrderDetail::getOrderId,item.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(lqw);

            orderDto.setOrderDetails(orderDetails);
            return orderDto;
        }).collect(Collectors.toList());

        orderDtoPage.setRecords(orderDtos);

        return R.success(orderDtoPage);
    }

}
