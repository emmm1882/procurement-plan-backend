package com.ittry.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ittry.entity.TryProcurementPlan;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface TryProcurementPlanService extends IService<TryProcurementPlan> {
    Object importExcel(MultipartFile file) throws IOException;
    void exportExcel(HttpServletResponse response, String planName, String status) throws IOException;
}
