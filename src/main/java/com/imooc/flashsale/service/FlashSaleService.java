package com.imooc.flashsale.service;

import com.imooc.flashsale.domain.OrderInfo;
import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlashSaleService {

    @Autowired GoodsService goodsService;

    @Autowired OrderService orderService;

    @Transactional
    public OrderInfo flashsale(User user, GoodsVo goods) {
        // 减库存 下订单 写入秒杀订单
        goodsService.reduceStock(goods);
        // order_info
        return orderService.createOrder(user, goods);
    }
}
