package com.imooc.flashsale.controller;

import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.redis.RedisService;
import com.imooc.flashsale.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired UserService userService;

    @Autowired RedisService redisService;

    @RequestMapping("/to_list")
    public String list(Model model, User user) {
        model.addAttribute("user", user);
        return "goods_list";
    }
}
