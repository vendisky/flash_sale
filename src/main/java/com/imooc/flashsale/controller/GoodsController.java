package com.imooc.flashsale.controller;

import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.redis.GoodsKey;
import com.imooc.flashsale.redis.RedisService;
import com.imooc.flashsale.result.Result;
import com.imooc.flashsale.service.GoodsService;
import com.imooc.flashsale.service.UserService;
import com.imooc.flashsale.vo.GoodsDetailVo;
import com.imooc.flashsale.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired UserService userService;

    @Autowired RedisService redisService;

    @Autowired GoodsService goodsService;

    @Autowired ThymeleafViewResolver thymeleafViewResolver;

    /** QPS： 15206 Threads: 5000*10 */
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String list(
            HttpServletRequest request, HttpServletResponse response, Model model, User user) {
        model.addAttribute("user", user);
        // 取缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        IWebContext ctx =
                new WebContext(
                        request,
                        response,
                        request.getServletContext(),
                        request.getLocale(),
                        model.asMap());
        // 手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model,
            User user,
            @PathVariable("goodsId") long goodsId) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
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
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setFlashsaleStatus(flashsaleStatus);
        return Result.success(vo);
    }
}
