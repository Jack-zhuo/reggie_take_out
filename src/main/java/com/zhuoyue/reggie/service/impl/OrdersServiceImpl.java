package com.zhuoyue.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuoyue.reggie.common.BaseContext;
import com.zhuoyue.reggie.common.CustomException;
import com.zhuoyue.reggie.entity.*;
import com.zhuoyue.reggie.mapper.OrdersMapper;
import com.zhuoyue.reggie.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Transactional
    public void submit(Orders orders) {
//      当前用户id
        Long currentId = BaseContext.getCurrentId();

//        查询购物车数据
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,currentId);

        List<ShoppingCart> shoppingCarts = shoppingCartService.list(lqw);

        if (shoppingCarts == null || shoppingCarts.size() == 0){
            throw new CustomException("购物车不能为空。");
        }

//        查询用户
        User user = userService.getById(currentId);
//        查询地址
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if (addressBook == null){
            throw new CustomException("地址有误，不能下单！");
        }
//        向订单表插入一条数据
        long orderId = IdWorker.getId();

        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());


        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserId(currentId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName((user.getName()));
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                +(addressBook.getCityName() == null ? "" : addressBook.getCityName())
                +(addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                +(addressBook.getDetail() == null ? "" : addressBook.getDetail())
        );

        this.save(orders);
//       插入订单明细表
       orderDetailService.saveBatch(orderDetails);
//      清空购物车
        shoppingCartService.remove(lqw);
//          15922476232
    }
}



















