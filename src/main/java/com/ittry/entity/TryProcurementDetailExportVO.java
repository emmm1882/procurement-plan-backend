package com.ittry.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import com.ittry.excel.StringTextConverter;

@Data
public class TryProcurementDetailExportVO {
    @ExcelProperty("id")
    private String id;
    @ExcelProperty("planId")
    private String planId;
    @ExcelProperty("序号")
    private Integer seq;
    @ExcelProperty("采购名称")
    private String itemName;
    @ExcelProperty("采购类别")
    private String category;
    @ExcelProperty("采购方式")
    private String method;
    @ExcelProperty("拟采购估价")
    private Double estimate;
    @ExcelProperty("计划采购时间")
    private String planTime;
    @ExcelProperty("资金来源")
    private String fundSource;
    @ExcelProperty("备注")
    private String remark;
} 