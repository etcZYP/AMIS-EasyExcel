package com.company.project.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.entity.*;
import com.company.project.excel.SupplyListener;
import com.company.project.service.SupplyRuleService;
import com.company.project.service.SupplyService;
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
public class SupplyController {

    @Autowired
    private SupplyService supplyService;

    @Autowired
    private SupplyRuleService supplyRuleService;

    @Autowired
    private TaxService taxService;

    @PostMapping("/uploadSupply")
    public Result<File> uploadSupplier(MultipartFile file) throws IOException {
        SupplyRule supplyRule = supplyRuleService.getById(1);
        EasyExcel.read(file.getInputStream(), Supply.class, new SupplyListener(supplyService, supplyRuleService))
                .sheet().headRowNumber(supplyRule.getStart()).doRead();

        List<SupplyConfig> list = JSONObject.parseArray(supplyRule.getConfig(), SupplyConfig.class);
        Map map = (Map) JSON.parse(supplyRule.getMapping());
        for (SupplyConfig supplyImportConfig : list) {
            supplyImportConfig.setTitle((String) map.get(supplyImportConfig.getColumn()));
        }
        supplyRule.setConfig(JSON.toJSONString(list));
        supplyRuleService.saveOrUpdate(supplyRule);

        return ResultGenerator.genSuccessResult(new File());
    }

    @GetMapping("/supply")
    public Result<List<Supply>> list() {
        return ResultGenerator.genSuccessResult(supplyService.list());
    }

    @PostMapping("/compare")
    public Result compare() {
        UpdateWrapper<Supply> supplyUpdateWrapper = new UpdateWrapper<>();
        supplyUpdateWrapper.lambda().set(Supply::getResult, "不符合")
                .set(Supply::getHistory, "不符合")
                .set(Supply::getStatus, "不符合")
                .ne(Supply::getStatus, "符合");
        supplyService.update(supplyUpdateWrapper);
        UpdateWrapper<Tax> taxUpdateWrapper = new UpdateWrapper<>();
        taxUpdateWrapper.lambda().set(Tax::getResult, "不符合")
                .set(Tax::getHistory, "不符合")
                .set(Tax::getStatus, "已对比")
                .eq(Tax::getStatus, "未对比");
        taxService.update(taxUpdateWrapper);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/supplyRule")
    public Result rule(@RequestBody SupplyRule supplyRule) {
        List<SupplyConfig> list = supplyRule.getConfigReq();

        SupplyRule rule = supplyRuleService.getById(1);
        if (rule.getMapping() != null) {
            Map map = (Map) JSON.parse(rule.getMapping());
            for (SupplyConfig supplyConfig : list) {
                supplyConfig.setTitle((String) map.get(Integer.valueOf((supplyConfig.getColumn()))));
            }
        }
        supplyRule.setConfig(JSON.toJSONString(list));
        supplyRule.setConfigRes(supplyRule.getConfigReq());

        supplyRuleService.saveOrUpdate(supplyRule);
        return ResultGenerator.genSuccessResult(supplyRule);
    }

    @GetMapping("/supplyRule")
    public Result<SupplyRule> rule() {
        SupplyRule supplyRule = supplyRuleService.getById(1);
        List<SupplyConfig> list = JSONObject.parseArray(supplyRule.getConfig(), SupplyConfig.class);
        supplyRule.setConfigRes(list);
        return ResultGenerator.genSuccessResult(supplyRule);
    }

    @PostMapping("/mark/{id}")
    public Result mark(@PathVariable("id") Integer id, @RequestBody Supply supply) {
        UpdateWrapper<Supply> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        supplyService.update(supply, updateWrapper);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/audit")
    public Result audit(@RequestBody Supply supply) {
        supplyService.updateById(supply);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/audit/{num}")
    public Result<Audit> check(@PathVariable("num") String num) {
        QueryWrapper<Supply> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Supply::getNum, num);
        List<Supply> list = supplyService.list(queryWrapper);
        double sum = 0;
        for (Supply supply : list) {
            sum += supply.getMoney();
        }
        Audit audit = new Audit();
        audit.setSupplyList(list);
        audit.setSum(sum);
        return ResultGenerator.genSuccessResult(audit);
    }
}
