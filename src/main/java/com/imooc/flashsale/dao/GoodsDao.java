package com.imooc.flashsale.dao;

import com.imooc.flashsale.domain.FlashSaleGoods;
import com.imooc.flashsale.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsDao {

    @Select(
            "select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.flashsale_price from flashsale_goods mg left join goods g on mg.goods_id = g.id")
    public List<GoodsVo> listGoodsVo();

    @Select(
            "select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.flashsale_price from flashsale_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodsId}")
    public GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    @Update(
            "update flashsale_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
    public int reduceStock(FlashSaleGoods g);

    @Update("update flashsale_goods set stock_count = #{stockCount} where goods_id = #{goodsId}")
    public int resetStock(FlashSaleGoods g);
}
