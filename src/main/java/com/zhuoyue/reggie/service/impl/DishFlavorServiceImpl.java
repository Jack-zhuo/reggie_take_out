package com.zhuoyue.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuoyue.reggie.entity.DishFlavor;
import com.zhuoyue.reggie.mapper.DishFlavorMapper;
import com.zhuoyue.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;


@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
