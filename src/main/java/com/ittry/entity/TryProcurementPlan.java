package com.ittry.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@TableName("try_procurement_plan")
public class TryProcurementPlan {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    @NotBlank(message = "计划名称不能为空")
    @Size(max = 100, message = "计划名称长度不能超过100个字符")
    private String planName;
    
    @NotBlank(message = "所属年度不能为空")
    private String year;
    
    @Size(max = 100, message = "所属公司长度不能超过100个字符")
    private String company;
    
    @Size(max = 100, message = "编制部门长度不能超过100个字符")
    private String dept;
    
    @Size(max = 100, message = "编制人长度不能超过100个字符")
    private String creator;
    
    private Date createTime;
    
    @Size(max = 500, message = "附件路径长度不能超过500个字符")
    private String attachment;
    
    private String status;
    private String createUserId;
    private Date createDate;
    private String updateUserId;
    private Date updateDate;
    
    @TableLogic
    private Integer isDeleted;
}
