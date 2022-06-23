package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PayRule {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer exportTotal;

    private String exportConfig;

//    @TableField(exist = false)
//    private List<Map<String, String>> exportConfigReq;

    @TableField(exist = false)
    private List<Map<String, String>> exportConfigRes;

    private Integer importStart;

    private Integer importTotal;

    private String importConfig;

    @TableField(exist = false)
    private List<PayImportConfig> importConfigReq;

    @TableField(exist = false)
    private List<PayImportConfig> importConfigRes;

    private String mapping;
}
