package com.zhuoyue.reggie.controller;


import ch.qos.logback.core.util.InvocationGate;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zhuoyue.reggie.common.BaseContext;
import com.zhuoyue.reggie.common.R;
import com.zhuoyue.reggie.entity.AddressBook;
import com.zhuoyue.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    public R<String> save(@RequestBody AddressBook addressBook){
        Long currentId = BaseContext.getCurrentId();
        addressBook.setUserId(currentId);

        addressBookService.save(addressBook);

        return R.success("添加成功");
    }


    @GetMapping("/list")
    public R<List<AddressBook>> getList() {
        Long currentId = BaseContext.getCurrentId();

        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(currentId != null, AddressBook::getUserId, currentId);
        lqw.orderByDesc(AddressBook::getCreateTime);

        List<AddressBook> list = addressBookService.list(lqw);

        return R.success(list);
    }

    @GetMapping("/{id}")
    public R<AddressBook> get(@PathVariable Long id){
        AddressBook addressBook = addressBookService.getById(id);
        return R.success(addressBook);
    }

    @DeleteMapping
    public R<String> delete(Long[] ids){

        for (Long id: ids) {
            addressBookService.removeById(id);
        }

            return R.success("删除成功！");
    }

    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook){

        addressBookService.updateById(addressBook);
        return R.success("更新成功");
    }

    @PutMapping("/default")
    public R<String> changeDefault(@RequestBody AddressBook addressBook){
        Long currentId = BaseContext.getCurrentId();
        LambdaUpdateWrapper<AddressBook> luw = new LambdaUpdateWrapper<>();
        luw.eq(AddressBook::getUserId, currentId);
        luw.set(AddressBook::getIsDefault,0);

        addressBookService.update(luw);

        addressBook.setIsDefault(1);

        addressBookService.updateById(addressBook);

        return R.success("修改默认地址成功");
    }

    @GetMapping("/default")
    public R<AddressBook> getAddress(){

        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();

        lqw.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        lqw.eq(AddressBook::getIsDefault,1);
//        15922476232
        AddressBook addressBook = addressBookService.getOne(lqw);

        return R.success(addressBook);

    }
}
