package com.imooc.flashsale.service;

import com.imooc.flashsale.domain.FlashSaleOrder;
import com.imooc.flashsale.domain.OrderInfo;
import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.redis.FlashSaleKey;
import com.imooc.flashsale.redis.RedisService;
import com.imooc.flashsale.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FlashSaleService {

    @Autowired GoodsService goodsService;

    @Autowired OrderService orderService;

    @Autowired RedisService redisService;

    @Transactional
    public OrderInfo flashsale(User user, GoodsVo goods) {
        // 减库存 下订单 写入秒杀订单
        boolean success = goodsService.reduceStock(goods);
        if (success) {
            // order_info
            return orderService.createOrder(user, goods);
        } else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    public long getFlashSaleResult(Long userId, long goodsId) {
        FlashSaleOrder order = orderService.getFlashSaleOrderByUserIdGoodsId(userId, goodsId);
        if (order != null) { // 秒杀成功
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(FlashSaleKey.isGoodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(FlashSaleKey.isGoodsOver, "" + goodsId);
    }

    public void reset(List<GoodsVo> goodsList) {
        goodsService.resetStock(goodsList);
        orderService.deleteOrders();
    }
}
