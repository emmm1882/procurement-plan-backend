package com.ittry.controller;

import com.alibaba.excel.EasyExcel;
import com.ittry.entity.TryProcurementDetail;
import com.ittry.service.TryProcurementDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/excel")
public class ExcelController {
    @Autowired
    private TryProcurementDetailService detailService;

    // 导出明细
    @GetMapping("/export/{planId}")
    public void export(@PathVariable String planId, HttpServletResponse response) throws IOException {
        List<TryProcurementDetail> list = detailService.listByPlanId(planId);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("明细导出", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), TryProcurementDetail.class).sheet("明细").doWrite(list);
    }

    // 导入明细
    @PostMapping("/import/{planId}")
    public Object importExcel(@PathVariable String planId, @RequestParam("file") MultipartFile file) throws IOException {
        List<TryProcurementDetail> list = EasyExcel.read(file.getInputStream()).head(TryProcurementDetail.class).sheet().doReadSync();
        // 先全部置空id，防止主键冲突
        for (TryProcurementDetail d : list) {
            d.setId(null);
        }
        for (int i = 0; i < list.size(); i++) {
            TryProcurementDetail d = list.get(i);
            int row = i + 1;
            // 校验必填字段
            if (d.getItemName() == null || d.getItemName().trim().isEmpty()) {
                return new java.util.HashMap<String, Object>() {{
                    put("code", 1);
                    put("msg", "第" + row + "行采购名称（itemName）不能为空，请检查导入文件！");
                }};
            }
            if (d.getCategory() == null || d.getCategory().trim().isEmpty()) {
                return new java.util.HashMap<String, Object>() {{
                    put("code", 1);
                    put("msg", "第" + row + "行采购类别（category）不能为空，请检查导入文件！");
                }};
            }
            if (d.getMethod() == null || d.getMethod().trim().isEmpty()) {
                return new java.util.HashMap<String, Object>() {{
                    put("code", 1);
                    put("msg", "第" + row + "行采购方式（method）不能为空，请检查导入文件！");
                }};
            }
            d.setPlanId(planId);
            detailService.save(d);
        }
        return new java.util.HashMap<String, Object>() {{
            put("code", 0);
            put("msg", "success");
        }};
    }
}