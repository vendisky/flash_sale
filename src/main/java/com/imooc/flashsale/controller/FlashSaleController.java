package com.imooc.flashsale.controller;

import com.imooc.flashsale.domain.FlashSaleOrder;
import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.rabbitmq.FlashSaleMessage;
import com.imooc.flashsale.rabbitmq.MQSender;
import com.imooc.flashsale.redis.FlashSaleKey;
import com.imooc.flashsale.redis.GoodsKey;
import com.imooc.flashsale.redis.OrderKey;
import com.imooc.flashsale.redis.RedisService;
import com.imooc.flashsale.result.CodeMsg;
import com.imooc.flashsale.result.Result;
import com.imooc.flashsale.service.FlashSaleService;
import com.imooc.flashsale.service.GoodsService;
import com.imooc.flashsale.service.OrderService;
import com.imooc.flashsale.service.UserService;
import com.imooc.flashsale.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/flashsale")
public class FlashSaleController implements InitializingBean {

    @Autowired UserService userService;

    @Autowired RedisService redisService;

    @Autowired GoodsService goodsService;

    @Autowired OrderService orderService;

    @Autowired FlashSaleService flashSaleService;

    @Autowired MQSender sender;

    private Map<Long, Boolean> localOverMap = new HashMap<>();

    /** 系统初始化 */
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if (goodsList == null) {
            return;
        }
        for (GoodsVo goods : goodsList) {
            redisService.set(
                    GoodsKey.getFlashSaleGoodsStock, "" + goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false);
        }
    }

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model) {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        for (GoodsVo goods : goodsList) {
            goods.setStockCount(1000);
            redisService.set(GoodsKey.getFlashSaleGoodsStock, "" + goods.getId(), 1000);
            localOverMap.put(goods.getId(), false);
        }
        redisService.delete(OrderKey.getFlashSaleOrderByUidGid);
        redisService.delete(FlashSaleKey.isGoodsOver);
        flashSaleService.reset(goodsList);
        return Result.success(true);
    }

    /** QPS： 11674 Threads: 5000*10 */
    @RequestMapping(value = "/do_flashsale", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> flashSale(
            Model model, User user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        // 内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.FLASH_SALE_OVER);
        }
        // 预减库存
        long stock = redisService.decr(GoodsKey.getFlashSaleGoodsStock, "" + goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.FLASH_SALE_OVER);
        }
        // 判断是否已经秒杀到了
        FlashSaleOrder order = orderService.getFlashSaleOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEAT_FLASH_SALE);
        }
        // 入队
        FlashSaleMessage mm = new FlashSaleMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        sender.sendFlashSaleMessage(mm);
        return Result.success(0); // 排队中
    }

    /** orderId：成功 -1：秒杀失败 0： 排队中 */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> flashSaleResult(
            Model model, User user, @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = flashSaleService.getFlashSaleResult(user.getId(), goodsId);
        return Result.success(result);
    }
}
