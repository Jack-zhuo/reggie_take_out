package com.zhuoyue.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhuoyue.reggie.common.R;
import com.zhuoyue.reggie.dto.DishDto;
import com.zhuoyue.reggie.entity.Category;
import com.zhuoyue.reggie.entity.Dish;
import com.zhuoyue.reggie.entity.DishFlavor;
import com.zhuoyue.reggie.service.CategoryService;
import com.zhuoyue.reggie.service.DishFlavorService;
import com.zhuoyue.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);

        return R.success("添加成功！");
    }

    @GetMapping("/page")
    public R<Page> selectAll(Integer page, Integer pageSize, String name) {

        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> dlqw = new LambdaQueryWrapper<>();
        dlqw.like(name != null, Dish::getName, name);
        dlqw.orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage, dlqw);

        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");

        List<Dish> records = dishPage.getRecords();

        List<DishDto> dishDtos = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) dishDto.setCategoryName(category.getName());

            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(dishDtos);

        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id) {

        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }

    @PutMapping
    public R<String> put(@RequestBody DishDto dishDto) {

        dishService.putWithFlavor(dishDto);

        return R.success("修改成功！");
    }

    @DeleteMapping
    public R<String> delete(Long[] ids) {

        for (Long id : ids) {
            dishService.removeById(id);
        }


        return R.success("删除成功！");
    }

    @PostMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable Integer status, Long[] ids) {

        for (Long id : ids) {
            Dish dish = dishService.getById(id);
            dish.setStatus(status);
            dishService.updateById(dish);
        }

        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){

        LambdaQueryWrapper<Dish> dlqw = new LambdaQueryWrapper<>();
        dlqw.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        dlqw.eq(Dish::getStatus,1);

        dlqw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(dlqw);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
            lqw.eq(item.getId() != null,DishFlavor::getDishId,item.getId());
            List<DishFlavor> flavorList = dishFlavorService.list(lqw);
            dishDto.setFlavors(flavorList);


            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }
}
