package com.imooc.flashsale.service;

import com.imooc.flashsale.dao.GoodsDao;
import com.imooc.flashsale.domain.FlashSaleGoods;
import com.imooc.flashsale.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public void reduceStock(GoodsVo goods) {
        FlashSaleGoods g = new FlashSaleGoods();
        g.setGoodsId(goods.getId());
        goodsDao.reduceStock(g);
    }
}
