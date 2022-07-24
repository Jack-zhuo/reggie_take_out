package com.zhuoyue.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuoyue.reggie.entity.User;
import com.zhuoyue.reggie.mapper.UserMapper;
import com.zhuoyue.reggie.service.UserService;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
