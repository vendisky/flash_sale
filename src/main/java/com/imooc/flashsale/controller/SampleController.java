package com.imooc.flashsale.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/demo")
public class SampleController {

    @RequestMapping("/test")
    public String test(Model model) {
        model.addAttribute("name", "flashSale");
        return "hello";
    }
}
