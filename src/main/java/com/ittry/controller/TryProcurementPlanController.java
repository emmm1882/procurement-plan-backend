package com.ittry.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ittry.entity.TryProcurementPlan;
import com.ittry.entity.TryProcurementDetail;
import com.ittry.service.TryProcurementPlanService;
import com.ittry.service.TryProcurementDetailService;
import com.ittry.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/procurement-plans")
public class TryProcurementPlanController {
    @Autowired
    private TryProcurementPlanService planService;
    @Autowired
    private TryProcurementDetailService detailService;
    @Autowired
    private HttpServletRequest request;
    @Autowired(required = false)
    private UploadService uploadService;

    @GetMapping("/page")
    public Object page(@RequestParam int pageNum, @RequestParam int pageSize, @RequestParam(required = false) String planName, @RequestParam(required = false) String status, @RequestParam(required = false) String createUserId) {
        QueryWrapper<TryProcurementPlan> qw = new QueryWrapper<>();
        if (planName != null && !planName.isEmpty()) qw.like("plan_name", planName);
        if (status != null && !status.isEmpty() && !"不限".equals(status)) {
            // 兼容中英文状态
            if ("EFFECTIVE".equals(status) || "已生效".equals(status)) {
                qw.in("status", "EFFECTIVE", "已生效");
            } else if ("SAVED".equals(status) || "已保存".equals(status)) {
                qw.in("status", "SAVED", "已保存");
            } else if ("APPROVING".equals(status) || "审批中".equals(status)) {
                qw.in("status", "APPROVING", "审批中");
            } else if ("REJECTED".equals(status) || "审批退回".equals(status)) {
                qw.in("status", "REJECTED", "审批退回");
            } else {
                qw.eq("status", status);
            }
        }
        if (createUserId != null && !createUserId.isEmpty()) qw.eq("create_user_id", createUserId);
        qw.eq("is_deleted", 0);
        Page<TryProcurementPlan> page = new Page<>(pageNum, pageSize);
        Page<TryProcurementPlan> result = planService.page(page, qw);
        Map<String, Object> data = new HashMap<>();
        data.put("list", result.getRecords());
        data.put("total", result.getTotal());
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 0);
        resp.put("msg", "success");
        resp.put("data", data);
        return resp;
    }

    @GetMapping("/{id}")
    public Object detail(@PathVariable String id) {
        TryProcurementPlan plan = planService.getById(id);
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 0);
        resp.put("msg", "success");
        resp.put("data", plan);
        return resp;
    }

    @PostMapping
    public Object save(@RequestBody Map<String, Object> param) {
        // 1. 解析主表
        TryProcurementPlan plan = new TryProcurementPlan();
        plan.setPlanName((String) param.get("planName"));
        if (plan.getPlanName() == null || plan.getPlanName().trim().isEmpty()) {
            throw new IllegalArgumentException("采购计划名称不能为空");
        }
        plan.setYear((String) param.get("year"));
        plan.setCompany((String) param.get("company"));
        plan.setDept((String) param.get("dept"));
        plan.setCreator((String) param.get("creator"));
        plan.setAttachment((String) param.get("attachment"));
        plan.setStatus(param.get("status") != null ? mapStatusToDb(param.get("status").toString()) : "SAVED");
        plan.setCreateUserId((String) param.get("createUserId"));
        plan.setCreateDate(new Date());
        plan.setUpdateUserId((String) param.get("updateUserId"));
        plan.setUpdateDate(new Date());
        if (param.get("createTime") != null && !"".equals(param.get("createTime"))) {
            try {
                String dateStr = param.get("createTime").toString();
                if (dateStr.contains("T")) {
                    dateStr = dateStr.replace("T", " ");
                }
                if (dateStr.contains(".")) {
                    dateStr = dateStr.substring(0, dateStr.indexOf("."));
                }
                if (dateStr.length() == 10) {
                    dateStr += " 00:00:00";
                }
                plan.setCreateTime(java.sql.Timestamp.valueOf(dateStr));
            } catch (Exception e) {
                plan.setCreateTime(new Date());
            }
        } else {
            plan.setCreateTime(new Date());
        }
        planService.save(plan);
        // 2. 解析明细并保存
        if (param.get("details") instanceof List) {
            List<Map<String, Object>> details = (List<Map<String, Object>>) param.get("details");
            for (Map<String, Object> d : details) {
                TryProcurementDetail detail = new TryProcurementDetail();
                detail.setPlanId(plan.getId());
                detail.setItemName(
                    d.get("itemName") != null ? d.get("itemName").toString() :
                    (d.get("name") != null ? d.get("name").toString() : null)
                );
                detail.setCategory(d.get("category") != null ? d.get("category").toString() : null);
                detail.setMethod(d.get("method") != null ? d.get("method").toString() : null);
                if (d.get("estimate") != null && !"".equals(d.get("estimate"))) {
                    detail.setEstimate(Double.valueOf(d.get("estimate").toString()));
                } else if (d.get("estimatedAmount") != null && !"".equals(d.get("estimatedAmount"))) {
                    detail.setEstimate(Double.valueOf(d.get("estimatedAmount").toString()));
                }
                if (d.get("planTime") != null && !"".equals(d.get("planTime"))) {
                    String dateStr = d.get("planTime").toString();
                    if (dateStr.length() > 10) dateStr = dateStr.substring(0, 10);
                    detail.setPlanTime(java.sql.Date.valueOf(dateStr));
                } else if (d.get("plannedTime") != null && !"".equals(d.get("plannedTime"))) {
                    String dateStr = d.get("plannedTime").toString();
                    if (dateStr.length() > 10) dateStr = dateStr.substring(0, 10);
                    detail.setPlanTime(java.sql.Date.valueOf(dateStr));
                }
                detail.setFundSource(d.get("fundSource") != null ? d.get("fundSource").toString() : null);
                detail.setRemark(d.get("remark") != null ? d.get("remark").toString() : null);
                detailService.save(detail);
            }
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 0);
        resp.put("msg", "success");
        return resp;
    }

    @PutMapping
    public Object update(@RequestBody Map<String, Object> param) {
        String id = (String) param.get("id");
        TryProcurementPlan plan = planService.getById(id);
        if (plan == null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("code", 1);
            resp.put("msg", "数据不存在");
            return resp;
        }
        if (param.get("planName") != null) plan.setPlanName((String) param.get("planName"));
        if (param.get("year") != null) plan.setYear((String) param.get("year"));
        if (param.get("company") != null) plan.setCompany((String) param.get("company"));
        if (param.get("dept") != null) plan.setDept((String) param.get("dept"));
        if (param.get("creator") != null) plan.setCreator((String) param.get("creator"));
        if (param.get("attachment") != null) plan.setAttachment((String) param.get("attachment"));
        if (param.get("status") != null) plan.setStatus(mapStatusToDb(param.get("status").toString()));
        if (param.get("createTime") != null && !"".equals(param.get("createTime"))) {
            try {
                String dateStr = param.get("createTime").toString();
                if (dateStr.contains("T")) dateStr = dateStr.replace("T", " ");
                if (dateStr.contains(".")) dateStr = dateStr.substring(0, dateStr.indexOf("."));
                if (dateStr.length() == 10) dateStr += " 00:00:00";
                plan.setCreateTime(java.sql.Timestamp.valueOf(dateStr));
            } catch (Exception e) {
                // ignore
            }
        }
        plan.setUpdateDate(new Date());
        planService.updateById(plan);
        // 明细同步更新
        if (param.get("details") instanceof List) {
            // 先删除原有明细
            detailService.remove(new QueryWrapper<TryProcurementDetail>().eq("plan_id", plan.getId()));
            // 再插入新明细
            List<Map<String, Object>> details = (List<Map<String, Object>>) param.get("details");
            for (Map<String, Object> d : details) {
                TryProcurementDetail detail = new TryProcurementDetail();
                detail.setPlanId(plan.getId());
                detail.setItemName(d.get("itemName") != null ? d.get("itemName").toString() : (d.get("name") != null ? d.get("name").toString() : null));
                detail.setCategory(d.get("category") != null ? d.get("category").toString() : null);
                detail.setMethod(d.get("method") != null ? d.get("method").toString() : null);
                if (d.get("estimate") != null && !"".equals(d.get("estimate"))) {
                    detail.setEstimate(Double.valueOf(d.get("estimate").toString()));
                } else if (d.get("estimatedAmount") != null && !"".equals(d.get("estimatedAmount"))) {
                    detail.setEstimate(Double.valueOf(d.get("estimatedAmount").toString()));
                }
                if (d.get("planTime") != null && !"".equals(d.get("planTime"))) {
                    String dateStr = d.get("planTime").toString();
                    if (dateStr.length() > 10) dateStr = dateStr.substring(0, 10);
                    detail.setPlanTime(java.sql.Date.valueOf(dateStr));
                } else if (d.get("plannedTime") != null && !"".equals(d.get("plannedTime"))) {
                    String dateStr = d.get("plannedTime").toString();
                    if (dateStr.length() > 10) dateStr = dateStr.substring(0, 10);
                    detail.setPlanTime(java.sql.Date.valueOf(dateStr));
                }
                detail.setFundSource(d.get("fundSource") != null ? d.get("fundSource").toString() : null);
                detail.setRemark(d.get("remark") != null ? d.get("remark").toString() : null);
                detailService.save(detail);
            }
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 0);
        resp.put("msg", "success");
        return resp;
    }

    @PostMapping("/update")
    public Object updateByPost(@RequestBody Map<String, Object> param) {
        return update(param);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable String id) {
        boolean removed = planService.removeById(id);
        Map<String, Object> resp = new HashMap<>();
        if (removed) {
            resp.put("code", 0);
            resp.put("msg", "success");
        } else {
            resp.put("code", 1);
            resp.put("msg", "删除失败，数据不存在或已被删除");
        }
        return resp;
    }

    @PostMapping("/import")
    public Object importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        return planService.importExcel(file);
    }

    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response, 
                          @RequestParam(required = false) String planName,
                          @RequestParam(required = false) String status) throws IOException {
        planService.exportExcel(response, planName, status);
    }

    @PostMapping("/upload")
    public Object uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new HashMap<String, Object>() {{
                put("code", 1);
                put("msg", "文件为空");
            }};
        }
        
        // 检查文件大小
        long fileSize = file.getSize();
        if (fileSize > 100 * 1024 * 1024) { // 100MB
            return new HashMap<String, Object>() {{
                put("code", 1);
                put("msg", "文件大小不能超过100MB");
            }};
        }
        
        // 检查文件数量
        String planId = request.getParameter("planId");
        if (planId != null) {
            TryProcurementPlan plan = planService.getById(planId);
            if (plan != null && plan.getAttachment() != null) {
                String[] existingFiles = plan.getAttachment().split(",");
                if (existingFiles.length >= 5) {
                    return new HashMap<String, Object>() {{
                        put("code", 1);
                        put("msg", "每个计划最多上传5个附件");
                    }};
                }
            }
        }
        
        try {
            // 处理文件上传逻辑
            String originalFilename = file.getOriginalFilename();
            String filePath = uploadService.saveFile(file);
            
            // 更新计划的附件字段
            if (planId != null) {
                TryProcurementPlan plan = planService.getById(planId);
                if (plan != null) {
                    String attachment = plan.getAttachment();
                    if (attachment == null || attachment.isEmpty()) {
                        attachment = filePath;
                    } else {
                        attachment += "," + filePath;
                    }
                    plan.setAttachment(attachment);
                    planService.updateById(plan);
                }
            }
            
            return new HashMap<String, Object>() {{
                put("code", 0);
                put("msg", "success");
                put("data", new HashMap<String, String>() {{
                    put("filePath", filePath);
                    put("fileName", originalFilename);
                }});
            }};
        } catch (Exception e) {
            return new HashMap<String, Object>() {{
                put("code", 1);
                put("msg", "文件上传失败：" + e.getMessage());
            }};
        }
    }

    @PutMapping("/{id}/status")
    public Object updateStatus(@PathVariable String id, @RequestParam String status) {
        TryProcurementPlan plan = planService.getById(id);
        if (plan == null) {
            return new HashMap<String, Object>() {{
                put("code", 1);
                put("msg", "计划不存在");
            }};
        }
        
        // 状态流转校验
        if (!isValidStatusTransition(plan.getStatus(), status)) {
            return new HashMap<String, Object>() {{
                put("code", 1);
                put("msg", "非法的状态转换");
            }};
        }
        
        plan.setStatus(status);
        plan.setUpdateDate(new Date());
        planService.updateById(plan);
        
        return new HashMap<String, Object>() {{
            put("code", 0);
            put("msg", "success");
        }};
    }

    private boolean isValidStatusTransition(String currentStatus, String targetStatus) {
        // 定义状态流转规则
        Map<String, List<String>> statusFlow = new HashMap<>();
        statusFlow.put("已保存", Arrays.asList("审批中", "已删除"));
        statusFlow.put("审批中", Arrays.asList("已生效", "审批退回"));
        statusFlow.put("审批退回", Arrays.asList("已保存", "已删除"));
        statusFlow.put("已生效", Arrays.asList());
        
        List<String> allowedTransitions = statusFlow.get(currentStatus);
        return allowedTransitions != null && allowedTransitions.contains(targetStatus);
    }

    private String mapStatusToDb(String status) {
        if ("已生效".equals(status)) return "EFFECTIVE";
        if ("已保存".equals(status)) return "SAVED";
        if ("审批中".equals(status)) return "APPROVING";
        if ("审批退回".equals(status)) return "REJECTED";
        return status;
    }
}
