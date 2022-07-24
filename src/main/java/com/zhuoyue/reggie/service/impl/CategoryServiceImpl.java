package com.zhuoyue.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuoyue.reggie.common.CustomException;
import com.zhuoyue.reggie.entity.Category;
import com.zhuoyue.reggie.entity.Dish;
import com.zhuoyue.reggie.entity.Setmeal;
import com.zhuoyue.reggie.mapper.CategoryMapper;
import com.zhuoyue.reggie.service.CategoryService;
import com.zhuoyue.reggie.service.DishService;
import com.zhuoyue.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {

        LambdaQueryWrapper<Setmeal> slqw = new LambdaQueryWrapper<>();
        slqw.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(slqw);

        if (count2 > 0){
            throw new CustomException("该分类已关联套餐，不可删除！");
        }


        LambdaQueryWrapper<Dish> dlqw = new LambdaQueryWrapper<>();
        dlqw.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dlqw);

        if (count1 > 0){
           throw new CustomException("该分类有已关联的菜品，不可删除！");
        }


        super.removeById(id);

    }
}
