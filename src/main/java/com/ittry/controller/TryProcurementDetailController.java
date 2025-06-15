package com.ittry.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ittry.entity.TryProcurementDetail;
import com.ittry.service.TryProcurementDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/detail")
public class TryProcurementDetailController {
    @Autowired
    private TryProcurementDetailService detailService;

    @GetMapping("/list/{planId}")
    public Object listByPlan(@PathVariable String planId) {
        QueryWrapper<TryProcurementDetail> qw = new QueryWrapper<>();
        qw.eq("plan_id", planId).eq("is_deleted", 0);
        List<TryProcurementDetail> list = detailService.list(qw);
        java.util.Map<String, Object> resp = new java.util.HashMap<>();
        resp.put("code", 0);
        resp.put("msg", "success");
        resp.put("data", list);
        return resp;
    }

    @PostMapping
    public Object save(@RequestBody TryProcurementDetail detail) {
        detailService.save(detail);
        return "success";
    }

    @PutMapping
    public Object update(@RequestBody TryProcurementDetail detail) {
        detailService.updateById(detail);
        return "success";
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable String id) {
        detailService.removeById(id);
        return "success";
    }
}
