package com.zhuoyue.reggie.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhuoyue.reggie.dto.SetmealDto;
import com.zhuoyue.reggie.entity.Dish;
import com.zhuoyue.reggie.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {

    public void saveWithDish(SetmealDto setmealDto);

    public void deleteWithDish(Long[] ids);
}
