package com.zhuoyue.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuoyue.reggie.common.CustomException;
import com.zhuoyue.reggie.dto.SetmealDto;
import com.zhuoyue.reggie.entity.Setmeal;
import com.zhuoyue.reggie.entity.SetmealDish;
import com.zhuoyue.reggie.mapper.SetmealMapper;
import com.zhuoyue.reggie.service.SetmealDishService;
import com.zhuoyue.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {

        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes = setmealDishes.stream().map((item) ->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    @Transactional
    public void deleteWithDish(Long[] ids) {

        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.in(Setmeal::getId,ids);
        lqw.eq(Setmeal::getStatus,1);

        int count = this.count(lqw);

        if (count > 0){
            throw new CustomException("套餐正在售卖，不能删除。");
        }

        for (Long id: ids) {
            this.removeById(id);
        }

        LambdaQueryWrapper<SetmealDish> lqw2 = new LambdaQueryWrapper<>();
        lqw2.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(lqw2);

    }
}
