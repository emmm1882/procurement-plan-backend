package com.ittry.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ittry.entity.TryProcurementDetail;
import com.ittry.mapper.TryProcurementDetailMapper;
import com.ittry.service.TryProcurementDetailService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;

@Service
public class TryProcurementDetailServiceImpl extends ServiceImpl<TryProcurementDetailMapper, TryProcurementDetail> implements TryProcurementDetailService {
    public List<TryProcurementDetail> listByPlanId(String planId) {
        QueryWrapper<TryProcurementDetail> qw = new QueryWrapper<>();
        qw.eq("plan_id", planId).eq("is_deleted", 0);
        return this.list(qw);
    }
}
