package com.imooc.flashsale.vo;

import com.imooc.flashsale.domain.OrderInfo;
import lombok.Data;

@Data
public class OrderDetailVo {
    private GoodsVo goods;
    private OrderInfo order;
}
