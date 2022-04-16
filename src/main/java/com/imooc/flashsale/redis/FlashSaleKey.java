package com.imooc.flashsale.redis;

public class FlashSaleKey extends BasePrefix {

    private FlashSaleKey(String prefix) {
        super(prefix);
    }

    public static FlashSaleKey isGoodsOver = new FlashSaleKey("go");
}
