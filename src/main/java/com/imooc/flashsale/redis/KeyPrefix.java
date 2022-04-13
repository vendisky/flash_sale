package com.imooc.flashsale.redis;

public interface KeyPrefix {

    public int expireSeconds();

    public String getPrefix();
}
