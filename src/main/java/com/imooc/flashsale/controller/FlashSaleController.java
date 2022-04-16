package com.imooc.flashsale.controller;

import com.imooc.flashsale.domain.FlashSaleOrder;
import com.imooc.flashsale.domain.OrderInfo;
import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.redis.RedisService;
import com.imooc.flashsale.result.CodeMsg;
import com.imooc.flashsale.result.Result;
import com.imooc.flashsale.service.FlashSaleService;
import com.imooc.flashsale.service.GoodsService;
import com.imooc.flashsale.service.OrderService;
import com.imooc.flashsale.service.UserService;
import com.imooc.flashsale.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/flashsale")
public class FlashSaleController {

    @Autowired UserService userService;

    @Autowired RedisService redisService;

    @Autowired GoodsService goodsService;

    @Autowired OrderService orderService;

    @Autowired FlashSaleService flashSaleService;

    /** QPS： 4041 Threads: 5000*10 */
    @RequestMapping(value = "/do_flashsale", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> list(Model model, User user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        // 判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return Result.error(CodeMsg.FLASH_SALE_OVER);
        }
        // 判断是否已经秒杀到了
        FlashSaleOrder order = orderService.getFlashSaleOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEAT_FLASH_SALE);
        }
        // 减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = flashSaleService.flashsale(user, goods);
        return Result.success(orderInfo);
    }
}
