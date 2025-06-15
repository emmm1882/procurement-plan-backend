package com.ittry;

import com.ittry.entity.TryProcurementPlan;
import com.ittry.entity.TryProcurementDetail;
import com.ittry.service.TryProcurementPlanService;
import com.ittry.service.TryProcurementDetailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@SpringBootTest
public class ProcurementPlanApplicationTests {
    @Autowired
    private TryProcurementPlanService planService;
    @Autowired
    private TryProcurementDetailService detailService;

    @Test
    void testPlanCrud() {
        TryProcurementPlan plan = new TryProcurementPlan();
        plan.setPlanName("测试计划");
        plan.setYear("2024年/年度");
        planService.save(plan);
        List<TryProcurementPlan> list = planService.list();
        Assertions.assertTrue(!list.isEmpty()); // 修改此处
        plan.setPlanName("修改后计划");
        planService.updateById(plan);
        TryProcurementPlan dbPlan = planService.getById(plan.getId());
        Assertions.assertEquals("修改后计划", dbPlan.getPlanName());
        planService.removeById(plan.getId());
        Assertions.assertNull(planService.getById(plan.getId()));
    }

    @Test
    void testDetailCrud() {
        // 先插入主表
        TryProcurementPlan plan = new TryProcurementPlan();
        plan.setPlanName("测试计划");
        plan.setYear("2024年/年度");
        planService.save(plan);
        // 插入明细并赋值plan_id
        TryProcurementDetail detail = new TryProcurementDetail();
        detail.setItemName("测试明细");
        detail.setPlanId(plan.getId());
        detailService.save(detail);
        List<TryProcurementDetail> list = detailService.list();
        Assertions.assertTrue(list.size() > 0);
        detail.setItemName("修改后明细");
        detailService.updateById(detail);
        TryProcurementDetail dbDetail = detailService.getById(detail.getId());
        Assertions.assertEquals("修改后明细", dbDetail.getItemName());
        detailService.removeById(detail.getId());
        Assertions.assertNull(detailService.getById(detail.getId()));
        // 清理主表
        planService.removeById(plan.getId());
    }

    // 其他如Excel导入导出、附件上传下载、状态流转等建议用MockMvc做接口测试
} 