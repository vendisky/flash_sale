package com.imooc.flashsale.domain;

import lombok.Data;

@Data
public class FlashSaleOrder {
    private Long id;
    private Long userId;
    private Long orderId;
    private Long goodsId;
}
