package com.company.project.entity;

import lombok.Data;

import java.util.List;

@Data
public class Audit {

    private List<Supply> supplyList;

    private List<Tax> taxList;

    private Double sum;

    private Double more;
}
