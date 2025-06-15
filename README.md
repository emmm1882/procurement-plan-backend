# 采购计划管理系统

## 项目说明
本项目是一个基于Spring Boot + Vue的采购计划管理系统，实现了采购计划的增删改查、明细导入导出、附件上传等功能。

## 技术栈
- 后端：Spring Boot 2.7.12、Mybatis-Plus、EasyExcel、MySQL8
- 前端：Vue2、Element UI、Axios

## 环境要求
- JDK 1.8+
- Maven 3.6+
- Node.js 14+
- MySQL 8.0+

## 快速开始

### 1. 数据库配置
1. 创建数据库：
```sql
CREATE DATABASE procurement_plan DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

2. 执行SQL脚本：
- 执行 `sql/schema.sql` 创建表结构
- 执行 `sql/data.sql` 导入测试数据

### 2. 后端启动
1. 修改数据库配置（src/main/resources/application.yml）
2. 执行Maven打包：
```bash
mvn clean package
```
3. 运行jar包：
```bash
java -jar target/procurement-plan-1.0.0.jar
```

### 3. 前端启动
1. 安装依赖：
```bash
cd front
npm install
```

2. 启动开发服务器：
```bash
npm run serve
```

## 访问地址
- 前端：http://localhost:8080
- 后端接口文档：http://localhost:8080/api/swagger-ui/index.html

## 默认账号
- 用户名：admin
- 密码：123456

## 功能特性
- 采购计划管理（增删改查）
- 计划明细导入导出
- 附件上传下载
- 状态流转管理
- 权限控制

## 注意事项
1. 确保MySQL服务已启动
2. 文件上传大小限制为100MB
3. 单个计划最多上传5个附件

## 目录结构
```
├── src/       # 后端源码（SpringBoot）
├── docs/      # 技术文档、ER图
├── sql/       # SQL脚本（schema.sql、data.sql）
└── README.md  # 项目说明
```

## 数据库初始化
1. 创建数据库（如：`procurement_plan`）。
2. 执行 `sql/schema.sql` 创建表结构。
3. 执行 `sql/data.sql` 导入测试数据。

## 后端启动
1. 进入 `src/` 目录，使用IDEA导入为Maven项目。
2. 配置 `application.yml` 数据库连接。
3. 运行主类启动SpringBoot服务。

## 主要功能
- 采购计划增删改查、明细导入导出、附件上传、状态流转、逻辑删除等。

## 其他
- 详细接口文档、ER图、前端代码、技术文档见 docs/ 目录。
