CREATE TABLE try_procurement_plan (
    id VARCHAR(64) PRIMARY KEY COMMENT '主键ID',
    plan_name VARCHAR(100) NOT NULL COMMENT '计划名称',
    year VARCHAR(20) NOT NULL COMMENT '所属年度',
    company VARCHAR(100) COMMENT '所属公司',
    dept VARCHAR(100) COMMENT '编制部门',
    creator VARCHAR(100) COMMENT '编制人',
    create_time DATETIME COMMENT '编制时间',
    attachment VARCHAR(500) COMMENT '附件（逗号分隔）',
    status VARCHAR(20) COMMENT '状态',
    create_user_id VARCHAR(64) COMMENT '创建人员',
    create_date DATETIME COMMENT '创建时间',
    update_user_id VARCHAR(64) COMMENT '修改人员',
    update_date DATETIME COMMENT '修改时间',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购计划主表';

CREATE TABLE try_procurement_detail (
    id VARCHAR(64) PRIMARY KEY COMMENT '主键ID',
    plan_id VARCHAR(64) NOT NULL COMMENT '采购计划ID',
    seq INT COMMENT '序号',
    item_name VARCHAR(100) NOT NULL COMMENT '采购名称',
    category VARCHAR(50) COMMENT '采购类别',
    method VARCHAR(50) COMMENT '采购方式',
    estimate DECIMAL(18,2) COMMENT '拟采购估价',
    plan_time DATETIME COMMENT '计划采购时间',
    fund_source VARCHAR(50) COMMENT '资金来源',
    remark VARCHAR(200) COMMENT '备注',
    create_user_id VARCHAR(64) COMMENT '创建人员',
    create_date DATETIME COMMENT '创建时间',
    update_user_id VARCHAR(64) COMMENT '修改人员',
    update_date DATETIME COMMENT '修改时间',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    CONSTRAINT fk_plan_id FOREIGN KEY (plan_id) REFERENCES try_procurement_plan(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购计划明细表'; 