package com.ittry.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ittry.entity.TryProcurementPlan;
import com.ittry.mapper.TryProcurementPlanMapper;
import com.ittry.service.TryProcurementPlanService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;

@Service
public class TryProcurementPlanServiceImpl extends ServiceImpl<TryProcurementPlanMapper, TryProcurementPlan> implements TryProcurementPlanService {
    
    @Override
    public Object importExcel(MultipartFile file) throws IOException {
        try {
            List<TryProcurementPlan> list = EasyExcel.read(file.getInputStream())
                    .head(TryProcurementPlan.class)
                    .sheet()
                    .doReadSync();
            
            // 设置默认值
            for (TryProcurementPlan plan : list) {
                plan.setStatus("DRAFT");
                plan.setIsDeleted(0);
            }
            
            // 批量保存
            this.saveBatch(list);
            return "success";
        } catch (Exception e) {
            throw new RuntimeException("导入失败：" + e.getMessage());
        }
    }

    @Override
    public void exportExcel(HttpServletResponse response, String planName, String status) throws IOException {
        try {
            // 查询数据
            QueryWrapper<TryProcurementPlan> qw = new QueryWrapper<>();
            if (planName != null && !planName.isEmpty()) qw.like("plan_name", planName);
            if (status != null && !status.isEmpty() && !"不限".equals(status)) qw.eq("status", status);
            qw.eq("is_deleted", 0);
            List<TryProcurementPlan> list = this.list(qw);

            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=procurement_plan.xlsx");

            // 导出数据
            EasyExcel.write(response.getOutputStream(), TryProcurementPlan.class)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("采购计划")
                    .doWrite(list);
        } catch (Exception e) {
            throw new RuntimeException("导出失败：" + e.getMessage());
        }
    }
}

