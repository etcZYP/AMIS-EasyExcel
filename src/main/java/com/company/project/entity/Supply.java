package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Supply {

    private String name;

    private String date1;

    private String num;

    private Double money;

    private String mark;

    private String person;

    private String type;

    private String date2;

    private String result;

    private String history;

    private String status;

    private String audit;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
}
