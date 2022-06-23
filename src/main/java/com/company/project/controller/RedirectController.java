package com.company.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedirectController {

    @RequestMapping("/check")
    public String index() {
        return "供应商发票对账";
    }

    @RequestMapping("/supply2")
    public String supply() {
        return "定义Excel导入供应商发票签收清单规则";
    }

    @RequestMapping("/tax2")
    public String tax() {
        return "定义Excel导入税局抵扣清单规则";
    }

    @RequestMapping("/audit")
    public String audit() {
        return "审核";
    }

    @RequestMapping("/config")
    public String config() {
        return "主动申请付款导入导出Excel配置";
    }
}
