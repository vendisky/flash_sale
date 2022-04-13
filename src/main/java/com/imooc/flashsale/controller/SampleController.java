package com.imooc.flashsale.controller;

import com.imooc.flashsale.domain.TestData;
import com.imooc.flashsale.redis.RedisService;
import com.imooc.flashsale.redis.TestDataKey;
import com.imooc.flashsale.result.Result;
import com.imooc.flashsale.service.TestDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired private TestDataService testDataService;
    @Autowired private RedisService redisService;

    @RequestMapping("/test")
    public String test(Model model) {
        model.addAttribute("name", "flashSale");
        return "hello";
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<TestData> dbGet() {
        TestData testData = testDataService.getById(1);
        return Result.success(testData);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<TestData> redisGet() {
        TestData testData = redisService.get(TestDataKey.getById, "" + 1, TestData.class);
        return Result.success(testData);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        TestData testData = new TestData();
        testData.setId(1);
        testData.setName("1111");
        redisService.set(TestDataKey.getById, "" + 1, testData); // TestDataKey:id1
        return Result.success(true);
    }
}
