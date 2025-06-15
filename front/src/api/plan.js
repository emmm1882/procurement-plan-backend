import request from '@/utils/request'

// 获取采购计划列表
export function getPlanList(params) {
    if (params && params.name !== undefined) {
        params.planName = params.name;
        delete params.name;
    }
    return request({
        url: '/api/procurement-plans/page',
        method: 'get',
        params
    })
}

// 获取采购计划详情
export function getPlanDetail(id) {
    return request({
        url: `/api/procurement-plans/${id}`,
        method: 'get'
    })
}

// 新增采购计划
export function addPlan(data) {
    return request({
        url: '/api/procurement-plans',
        method: 'post',
        data
    })
}

// 修改采购计划
export function updatePlan(data) {
    return request({
        url: '/api/procurement-plans/update',
        method: 'post',
        data,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

// 删除采购计划
export function deletePlan(id) {
    return request({
        url: `/api/procurement-plans/${id}`,
        method: 'delete'
    })
}

// 获取计划明细列表
export function getPlanDetailList(planId) {
    return request({
        url: `/api/detail/list/${planId}`,
        method: 'get'
    })
}

// 导入计划明细
export function importPlanDetails(planId, data) {
    return request({
        url: `/api/excel/import/${planId}`,
        method: 'post',
        data
    })
}

// 导出计划明细
export function exportPlanDetails(planId) {
    return request({
        url: `/api/excel/export/${planId}`,
        method: 'get',
        responseType: 'blob'
    })
}

// 上传附件
export function uploadAttachment(data) {
    return request({
        url: '/api/attachment/upload',
        method: 'post',
        data
    })
}

// 下载附件
export function downloadAttachment(filename) {
    return request({
        url: `/api/attachment/download/${filename}`,
        method: 'get',
        responseType: 'blob'
    })
}

// 更新计划状态
export function updatePlanStatus(id, status) {
    return request({
        url: '/api/status/change',
        method: 'post',
        params: { id, status }
    })
}

// 上传Excel文件
export function uploadExcel(data) {
    return request({
        url: '/api/procurement-plans/upload',
        method: 'post',
        data,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}