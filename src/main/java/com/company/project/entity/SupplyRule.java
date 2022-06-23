package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class SupplyRule {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer start;

    private Integer total;

    private String config;

    @TableField(exist = false)
    private List<SupplyConfig> configReq;

    @TableField(exist = false)
    private List<SupplyConfig> configRes;

    private String mapping;
}
