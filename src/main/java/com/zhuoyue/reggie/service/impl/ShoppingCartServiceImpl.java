package com.zhuoyue.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuoyue.reggie.entity.ShoppingCart;
import com.zhuoyue.reggie.mapper.ShoppingCartMapper;
import com.zhuoyue.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
