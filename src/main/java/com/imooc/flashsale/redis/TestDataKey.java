package com.imooc.flashsale.redis;

public class TestDataKey extends BasePrefix {

    private TestDataKey(String prefix) {
        super(prefix);
    }

    public static TestDataKey getById = new TestDataKey("id");
    public static TestDataKey getByName = new TestDataKey("name");
}
