package com.company.project.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ConverterUtils;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;

import com.company.project.entity.Tax;
import com.company.project.entity.TaxRule;
import com.company.project.service.TaxRuleService;
import com.company.project.service.TaxService;
import lombok.extern.slf4j.Slf4j;

/**
 * 模板的读取类
 *
 * @author Jiaju Zhuang
 */
// 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
@Slf4j
public class TaxListener implements ReadListener<Tax> {
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    private List<Tax> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    private Map<Integer, String> hashMap = new HashMap<>();
    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private TaxService taxService;

    private TaxRuleService taxRuleService;

    public TaxListener() {
        // 这里是demo，所以随便new一个。实际使用如果到了spring,请使用下面的有参构造函数
//        uploadDAO = new UploadDAO();
    }

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param taxService
     */
    public TaxListener(TaxService taxService, TaxRuleService taxRuleService) {
        this.taxService = taxService;
        this.taxRuleService = taxRuleService;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(Tax data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        data.setResult("");
        data.setHistory("");
        data.setStatus("未对比");
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 这里会一行行的返回头
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        Map<Integer, String> map = ConverterUtils.convertToStringMap(headMap, context);
        hashMap = map;
        log.info("解析到一条头数据:{}", JSON.toJSONString(hashMap));
        // 如果想转成成 Map<Integer,String>
        // 方案1： 不要implements ReadListener 而是 extends AnalysisEventListener
        // 方案2： 调用 ConverterUtils.convertToStringMap(headMap, context) 自动会转换
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        taxService.saveBatch(cachedDataList);

        TaxRule taxRule = new TaxRule();
        taxRule.setId(1);
        taxRule.setMapping(JSON.toJSONString(hashMap));
        taxRuleService.updateById(taxRule);

        log.info("存储数据库成功！");
    }
}
