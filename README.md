# 采购计划管理系统后端

## 项目简介
本项目是采购计划管理系统的后端服务，基于 Spring Boot 框架开发，提供采购计划的增删改查等功能。

## 技术栈
- 后端框架：Spring Boot 2.7.12
- 持久层框架：MyBatis-Plus 3.5.3.1
- 数据库：MySQL 8.0.33
- 接口文档：Swagger 3.0.0
- Excel处理：EasyExcel 3.3.2
- 安全框架：Spring Security
- 参数校验：Hibernate Validator 6.0.13.Final
- 开发工具：Lombok
- 服务器：Tomcat 9

## 项目结构
```
procurement-plan-backend/
├── pom.xml                        # Maven配置文件
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── ittry/
    │   │           ├── controller/    # 控制器层
    │   │           ├── service/       # 服务层
    │   │           ├── mapper/        # 数据访问层
    │   │           ├── entity/        # 实体类
    │   │           ├── excel/         # Excel处理相关
    │   │           ├── config/        # 配置类
    │   │           └── ProcurementPlanApplication.java  # 启动类
    │   └── resources/
    │       └── application.yml        # 应用配置文件
```

## 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Tomcat 9.0+

## 快速开始

### 1. 克隆项目
```bash
git clone https://github.com/emmm1882/procurement-plan-backend
```

### 2. 配置数据库
1. 创建数据库
2. 执行 `sql/schema.sql` 创建表结构
3. 执行 `sql/data.sql` 导入初始数据

### 3. 修改配置
修改 `application.yml` 中的数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/procurement_plan?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 4. 启动项目
```bash
mvn spring-boot:run
```

### 5. 访问接口文档
启动成功后访问：http://localhost:8080/api/swagger-ui.html

## 主要功能
1. 采购计划查询列表
2. 新增采购计划
3. 修改采购计划
4. 查看采购计划
5. 删除采购计划
6. 计划明细导入导出

## 接口规范
- 所有接口遵循 RESTful 风格
- 统一返回格式：
```json
{
    "code": 200,
    "message": "success",
    "data": {}
}
```

