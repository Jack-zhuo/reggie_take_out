package com.zhuoyue.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhuoyue.reggie.dto.DishDto;
import com.zhuoyue.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    void putWithFlavor(DishDto dishDto);
}
