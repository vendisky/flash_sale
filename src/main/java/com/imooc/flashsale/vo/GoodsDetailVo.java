package com.imooc.flashsale.vo;

import com.imooc.flashsale.domain.User;
import lombok.Data;

@Data
public class GoodsDetailVo {
    private int flashsaleStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods;
    private User user;
}
