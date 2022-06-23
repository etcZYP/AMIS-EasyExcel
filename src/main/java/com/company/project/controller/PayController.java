package com.company.project.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.entity.*;
import com.company.project.excel.PayListener;
import com.company.project.service.PayRuleService;
import com.company.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zyp
 * @since 2022-05-20
 */
@RestController
public class PayController {

    @Autowired
    private PayRuleService payRuleService;

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        PayRule payRule = payRuleService.getById(1);
        EasyExcel.read(file.getInputStream(), Pay.class, new PayListener(payRuleService)).sheet()
                .headRowNumber(payRule.getImportStart()).doRead();
        return ResultGenerator.genSuccessResult(new File());
    }

    @PostMapping("/payRule")
    public Result rule(@RequestBody PayRule payRule) {
        List<PayImportConfig> list = payRule.getImportConfigReq();

        PayRule rule = payRuleService.getById(1);
        if (rule.getMapping() != null) {
            Map map = (Map) JSON.parse(rule.getMapping());
            for (PayImportConfig payImportConfig : list) {
                payImportConfig.setImportTitle((String) map.get((payImportConfig.getImportColumn())));
            }
        }
        payRule.setImportConfig(JSON.toJSONString(list));
        payRule.setImportConfigRes(payRule.getImportConfigReq());

        payRuleService.saveOrUpdate(payRule);
        return ResultGenerator.genSuccessResult(payRule);
    }

    @GetMapping("/payRule")
    public Result rule() {
        PayRule payRule = payRuleService.getById(1);
        List<PayImportConfig> list = JSONObject.parseArray(payRule.getImportConfig(), PayImportConfig.class);
        payRule.setImportConfigRes(list);
        return ResultGenerator.genSuccessResult(payRule);
    }

}
