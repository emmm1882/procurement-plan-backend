package com.ittry.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ittry.entity.TryProcurementDetail;
import java.util.List;

public interface TryProcurementDetailService extends IService<TryProcurementDetail> {
    List<TryProcurementDetail> listByPlanId(String planId);
}
