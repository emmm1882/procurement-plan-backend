package com.ittry.controller;

import com.ittry.entity.TryProcurementPlan;
import com.ittry.service.TryProcurementPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/status")
public class StatusController {
    @Autowired
    private TryProcurementPlanService planService;

    @PostMapping("/change")
    public Object changeStatus(@RequestParam String id, @RequestParam String status) {
        TryProcurementPlan plan = planService.getById(id);
        if (plan == null) return "not found";
        plan.setStatus(status);
        planService.updateById(plan);
        return "success";
    }
}