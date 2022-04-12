package com.imooc.flashsale.controller;

import com.imooc.flashsale.domain.TestData;
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
}
