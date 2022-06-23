package com.company.project.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.entity.*;
import com.company.project.excel.TaxListener;
import com.company.project.service.TaxRuleService;
import com.company.project.service.TaxService;
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
 * @since 2022-05-24
 */
@RestController
public class TaxController {

    @Autowired
    private TaxService taxService;

    @Autowired
    private TaxRuleService taxRuleService;

    @PostMapping("/uploadTax")
    public Result uploadTax(MultipartFile file) throws IOException {
        TaxRule taxRule = taxRuleService.getById(1);
        EasyExcel.read(file.getInputStream(), Tax.class, new TaxListener(taxService, taxRuleService))
                .sheet().headRowNumber(taxRule.getStart()).doRead();
        if(taxRule.getConfig() != null) {
            List<TaxConfig> list = JSONObject.parseArray(taxRule.getConfig(), TaxConfig.class);
            Map map = (Map) JSON.parse(taxRule.getMapping());
            for (TaxConfig taxImportConfig : list) {
                taxImportConfig.setTitle((String) map.get(taxImportConfig.getColumn()));
            }
            taxRule.setConfig(JSON.toJSONString(list));
            taxRuleService.saveOrUpdate(taxRule);
        }

        return ResultGenerator.genSuccessResult(new File());
    }

    @GetMapping("/tax")
    public Result<List<Tax>> list() {
        return ResultGenerator.genSuccessResult(taxService.list());
    }

    @PostMapping("/taxRule")
    public Result rule(@RequestBody TaxRule taxRule) {
        List<TaxConfig> list = taxRule.getConfigReq();

        TaxRule rule = taxRuleService.getById(1);
        if (rule.getMapping() != null) {
            Map map = (Map) JSON.parse(rule.getMapping());
            for (TaxConfig taxConfig : list) {
                taxConfig.setTitle((String) map.get(Integer.valueOf((taxConfig.getColumn()))));
            }
        }
        taxRule.setConfig(JSON.toJSONString(list));
        taxRule.setConfigRes(taxRule.getConfigReq());

        taxRuleService.saveOrUpdate(taxRule);
        return ResultGenerator.genSuccessResult(taxRule);
    }

    @GetMapping("/taxRule")
    public Result<TaxRule> rule() {
        TaxRule taxRule = taxRuleService.getById(1);
        List<TaxConfig> list = JSONObject.parseArray(taxRule.getConfig(), TaxConfig.class);
        taxRule.setConfigRes(list);
        return ResultGenerator.genSuccessResult(taxRule);
    }

}
