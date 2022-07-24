package com.zhuoyue.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhuoyue.reggie.common.BaseContext;
import com.zhuoyue.reggie.common.R;
import com.zhuoyue.reggie.entity.ShoppingCart;
import com.zhuoyue.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {

        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, currentId);

        if (shoppingCart.getDishId() != null) {
            lqw.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            lqw.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart cart = shoppingCartService.getOne(lqw);

        if (cart != null) {
            Integer number = cart.getNumber();
            cart.setNumber(number + 1);
            shoppingCartService.updateById(cart);
        } else {
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            cart = shoppingCart;
        }

        return R.success(cart);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> getList() {

        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartService.list(lqw);

        return R.success(list);
    }

    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart) {

        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, currentId);

        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();

        if (dishId != null)
            lqw.eq(ShoppingCart::getDishId, dishId);
        else
            lqw.eq(ShoppingCart::getSetmealId, setmealId);

        ShoppingCart cart = shoppingCartService.getOne(lqw);

        if (cart.getNumber() == 1) {
            shoppingCartService.remove(lqw);
        } else {
            Integer number = cart.getNumber();
            cart.setNumber(number - 1);
            shoppingCartService.updateById(cart);
        }
        return R.success("减少成功！");
    }

    @DeleteMapping("/clean")
    public R<String> clean(){

        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
//        15922476232
        shoppingCartService.remove(lqw);

        return R.success("清除成功！");
    }
}
