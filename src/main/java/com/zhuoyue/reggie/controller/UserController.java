package com.zhuoyue.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhuoyue.reggie.common.R;
import com.zhuoyue.reggie.entity.User;
import com.zhuoyue.reggie.service.UserService;
import com.zhuoyue.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;


@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {

        String phone = user.getPhone();

        if (phone != null) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            log.info("你的手机号是：{}，你的验证码是：{}",phone, code);

            session.setAttribute(phone, code);

            return R.success("验证码发送成功！");
        }
        return R.error("发送失败！");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {

        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        String codeSession = session.getAttribute(phone).toString();

        if (code != null && code.equals(codeSession)) {

            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
            lqw.eq(User::getPhone, phone);

            User user = userService.getOne(lqw);
            if (user == null){

                user = new User();
                user.setPhone(phone);

                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }
    @PostMapping("/loginout")
    public R<String> loginout(HttpSession session){

        session.removeAttribute("user");

        return R.success("退出成功！");
    }
}
