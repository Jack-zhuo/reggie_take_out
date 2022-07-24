package com.zhuoyue.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuoyue.reggie.dto.DishDto;
import com.zhuoyue.reggie.entity.Dish;
import com.zhuoyue.reggie.entity.DishFlavor;
import com.zhuoyue.reggie.mapper.DishMapper;
import com.zhuoyue.reggie.service.DishFlavorService;
import com.zhuoyue.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {


    @Autowired
    private DishFlavorService dishFlavorService;

    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);


        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());


        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {

        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        LambdaQueryWrapper<DishFlavor> dflqw = new LambdaQueryWrapper<>();
        dflqw.eq(DishFlavor::getDishId, id);
        List<DishFlavor> dishFlavors = dishFlavorService.list(dflqw);

        dishDto.setFlavors(dishFlavors);

        return dishDto;
    }

    @Override
    public void putWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> dflqw = new LambdaQueryWrapper<>();
        dflqw.eq(DishFlavor::getDishId, dishDto.getId());

        dishFlavorService.remove(dflqw);

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {

//            DishFlavor df = new DishFlavor();
//            BeanUtils.copyProperties(item,df);
//            df.setDishId(dishDto.getId());
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);

    }
}
