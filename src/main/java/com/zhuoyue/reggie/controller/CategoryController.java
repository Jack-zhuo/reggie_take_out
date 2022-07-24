package com.zhuoyue.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhuoyue.reggie.common.R;
import com.zhuoyue.reggie.entity.Category;
import com.zhuoyue.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category){

        categoryService.save(category);

        return R.success("添加成功");
    }

    @GetMapping("/page")
    public R<Page> page(Integer page, int pageSize){
        Page<Category> categoryPage = new Page<>();

        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.orderByAsc(Category::getSort);

        categoryService.page(categoryPage);
        return R.success(categoryPage);
    }

    @DeleteMapping
    public R<String> deleteById(Long ids){

        categoryService.remove(ids);

        return R.success("删除成功。");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){

        categoryService.updateById(category);

        return R.success("更新成功。");
    }

    @GetMapping("/list")
    public R<List> list(Category category){
        LambdaQueryWrapper<Category> clqw = new LambdaQueryWrapper<>();

        clqw.eq(category.getType() != null,Category::getType,category.getType());

        clqw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(clqw);
        return R.success(list);
    }

}
