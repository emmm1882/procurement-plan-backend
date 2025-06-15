package com.ittry.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Min;
import java.util.Date;
import com.alibaba.excel.annotation.ExcelProperty;

@Data
@TableName("try_procurement_detail")
public class TryProcurementDetail {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    @NotBlank(message = "计划ID不能为空")
    private String planId;
    
    @ExcelProperty("序号")
    private Integer seq;
    
    @NotBlank(message = "采购名称不能为空")
    @Size(max = 100, message = "采购名称长度不能超过100个字符")
    @ExcelProperty("采购名称")
    private String itemName;
    
    @NotBlank(message = "采购类别不能为空")
    @Size(max = 50, message = "采购类别长度不能超过50个字符")
    @ExcelProperty("采购类别")
    private String category;
    
    @NotBlank(message = "采购方式不能为空")
    @Size(max = 50, message = "采购方式长度不能超过50个字符")
    @ExcelProperty("采购方式")
    private String method;
    
    @NotNull(message = "拟采购估价不能为空")
    @Min(value = 0, message = "拟采购估价不能小于0")
    @ExcelProperty("拟采购估价")
    private Double estimate;
    
    @NotNull(message = "计划采购时间不能为空")
    @ExcelProperty("计划采购时间")
    private Date planTime;
    
    @NotBlank(message = "资金来源不能为空")
    @Size(max = 50, message = "资金来源长度不能超过50个字符")
    @ExcelProperty("资金来源")
    private String fundSource;
    
    @Size(max = 200, message = "备注长度不能超过200个字符")
    @ExcelProperty("备注")
    private String remark;
    
    private String createUserId;
    private Date createDate;
    private String updateUserId;
    private Date updateDate;
    
    @TableLogic
    private Integer isDeleted;
}
