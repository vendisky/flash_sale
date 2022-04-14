package com.imooc.flashsale.controller;

import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.redis.RedisService;
import com.imooc.flashsale.service.GoodsService;
import com.imooc.flashsale.service.UserService;
import com.imooc.flashsale.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired UserService userService;

    @Autowired RedisService redisService;

    @Autowired GoodsService goodsService;

    /** QPS： 4784 Threads: 5000*10 */
    @RequestMapping("/to_list")
    public String list(Model model, User user) {
        model.addAttribute("user", user);
        // 查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, User user, @PathVariable("goodsId") long goodsId) {
        model.addAttribute("user", user);

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int flashsaleStatus = 0;
        int remainSeconds = 0;
        if (now < startAt) { // 秒杀还没开始，倒计时
            flashsaleStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) { // 秒杀已经结束
            flashsaleStatus = 2;
            remainSeconds = -1;
        } else { // 秒杀进行中
            flashsaleStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("flashsaleStatus", flashsaleStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }
}
