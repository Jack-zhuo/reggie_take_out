package com.zhuoyue.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhuoyue.reggie.common.R;
import com.zhuoyue.reggie.dto.SetmealDto;
import com.zhuoyue.reggie.entity.Category;
import com.zhuoyue.reggie.entity.Dish;
import com.zhuoyue.reggie.entity.Setmeal;
import com.zhuoyue.reggie.service.CategoryService;
import com.zhuoyue.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
          log.info("套餐信息：{}",setmealDto);
          setmealService.saveWithDish(setmealDto);
          return R.success("添加成功！");
    }

    @GetMapping("/page")
    public R<Page> getPage(Integer page,Integer pageSize,String name){

        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.like(name != null,Setmeal::getName,name);
        lqw.orderByDesc(Setmeal::getCreateTime);

        setmealService.page(setmealPage,lqw);

        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");

        List<Setmeal> records = setmealPage.getRecords();


        List<SetmealDto> setmealDtos = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);

            Category category = categoryService.getById(item.getCategoryId());

            if (category != null) setmealDto.setCategoryName(category.getName());

            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(setmealDtos);

        return R.success(setmealDtoPage);
    }

    @DeleteMapping
    public R<String> delete(Long[] ids){

        setmealService.deleteWithDish(ids);

        return R.success("删除成功！");
    }

    @PostMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable Integer status,Long[] ids){
        for (Long id : ids) {
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(status);
            setmealService.updateById(setmeal);
        }
        return R.success("修改成功！");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        lqw.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());

        List<Setmeal> list = setmealService.list(lqw);

        return R.success(list);
    }
}
