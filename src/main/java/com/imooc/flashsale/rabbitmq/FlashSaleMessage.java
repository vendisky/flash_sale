package com.imooc.flashsale.rabbitmq;

import com.imooc.flashsale.domain.User;
import lombok.Data;

@Data
public class FlashSaleMessage {
    private User user;
    private long goodsId;
}
