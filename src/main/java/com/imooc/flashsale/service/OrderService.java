package com.imooc.flashsale.service;

import com.imooc.flashsale.dao.OrderDao;
import com.imooc.flashsale.domain.FlashSaleOrder;
import com.imooc.flashsale.domain.OrderInfo;
import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.redis.OrderKey;
import com.imooc.flashsale.redis.RedisService;
import com.imooc.flashsale.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {

    @Autowired OrderDao orderDao;

    @Autowired RedisService redisService;

    public FlashSaleOrder getFlashSaleOrderByUserIdGoodsId(long userId, long goodsId) {
        // return orderDao.getFlashSaleOrderByUserIdGoodsId(userId, goodsId);
        return redisService.get(
                OrderKey.getFlashSaleOrderByUidGid,
                "" + userId + "_" + goodsId,
                FlashSaleOrder.class);
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }

    @Transactional
    public OrderInfo createOrder(User user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getFlashSalePrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderDao.insert(orderInfo);
        FlashSaleOrder flashSaleOrder = new FlashSaleOrder();
        flashSaleOrder.setGoodsId(goods.getId());
        flashSaleOrder.setOrderId(orderInfo.getId());
        flashSaleOrder.setUserId(user.getId());
        orderDao.insertFlashSaleOrder(flashSaleOrder);

        redisService.set(
                OrderKey.getFlashSaleOrderByUidGid,
                "" + user.getId() + "_" + goods.getId(),
                flashSaleOrder);

        return orderInfo;
    }

    public void deleteOrders() {
        orderDao.deleteOrders();
        orderDao.deleteFlashSaleOrders();
    }
}
