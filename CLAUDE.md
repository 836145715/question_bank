# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 在处理本仓库代码时提供指导。

## 项目概述

这是一个 SpringBoot 2.7.2 项目模板,提供了包含常用框架和业务功能的综合后端基础。项目遵循标准的 Maven 结构,使用 Java 8。

## 构建和开发命令

### Maven 命令

- **构建项目**: `mvn clean package`
- **运行应用**: `mvn spring-boot:run`
- **运行测试**: `mvn test`
- **清理构建产物**: `mvn clean`

### 运行应用程序

- **主类**: `com.hu.wink.MainApplication`
- **默认端口**: 8101
- **上下文路径**: `/api`
- **Swagger UI**: `http://localhost:8101/api/doc.html`

## 架构和结构

### 包结构

- `com.hu.wink.controller` - REST API 控制器
- `com.hu.wink.service` - 业务逻辑层
- `com.hu.wink.service.impl` - 服务实现
- `com.hu.wink.mapper` - MyBatis 数据访问层
- `com.hu.wink.model.entity` - 数据库实体
- `com.hu.wink.model.dto` - 数据传输对象
- `com.hu.wink.model.vo` - 视图对象
- `com.hu.wink.config` - 配置类
- `com.hu.wink.utils` - 工具类
- `com.hu.wink.exception` - 异常处理
- `com.hu.wink.annotation` - 自定义注解
- `com.hu.wink.aop` - AOP 拦截器
- `com.hu.wink.wxmp` - 微信小程序集成

### 核心技术

- **框架**: Spring Boot 2.7.2 with Spring MVC
- **数据库**: MySQL with MyBatis Plus (支持分页)
- **缓存**: Redis (默认禁用,需要配置)
- **搜索**: Elasticsearch (默认禁用,需要配置)
- **文档**: Knife4j (Swagger)
- **文件存储**: 腾讯云 COS
- **微信**: 微信公众号集成
- **工具**: Hutool, Apache Commons Lang3, EasyExcel, Lombok

## 数据库配置

### MySQL 设置

1. 在 `application.yml` 中更新数据库配置:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/my_db
    username: root
    password: 123456
```

2. 按顺序执行 SQL 脚本:
   - `sql/create_table.sql` - 创建数据库和表

### Redis 集成 (可选)

要启用 Redis 会话管理:

1. 在 `application.yml` 中配置 Redis
2. 从 `MainApplication.java:14` 的 `@SpringBootApplication(exclude = ...)` 中移除 `RedisAutoConfiguration.class`
3. 在会话配置中取消注释 `store-type: redis`

### Elasticsearch 集成 (可选)

1. 在 `application.yml` 中配置 Elasticsearch
2. 使用 `sql/post_es_mapping.json` 创建 ES 索引映射
3. 在 job 类中取消注释 `@Component` 注解以进行数据同步

## 关键配置文件

- `application.yml` - 主配置文件,包含环境配置
- `application-dev.yml` - 开发环境设置
- `application-prod.yml` - 生产环境设置
- `pom.xml` - Maven 依赖和构建配置

## 业务功能

### 核心实体

- **用户管理**: 注册、登录、基于角色的身份验证
- **题目管理**: 题目和题库的 CRUD 操作
- **文件上传**: 与 COS 集成的业务文件处理

### 认证与授权

- 自定义 `@AuthCheck` 注解用于方法级安全
- 基于角色的访问控制 (`user`, `admin`, `ban`)
- 支持 Redis 的会话管理

### 全局功能

- 通过 `GlobalExceptionHandler` 进行全局异常处理
- 使用 AOP 记录请求/响应日志
- CORS 配置
- 使用 `BaseResponse` 和 `ResultUtils` 的标准化 API 响应

## 开发指南

### 代码生成

项目在 `generate.CodeGenerator` 类中包含代码生成器,用于创建样板 CRUD 代码。

### API 文档

所有 REST 端点都使用 Knife4j (Swagger) 进行文档化。访问地址: `http://localhost:8101/api/doc.html`

### 错误处理

通过 `BusinessException` 使用自定义异常,在 `ErrorCode` 中使用标准化错误代码。

### 数据库访问

- MyBatis Plus 提供增强的 ORM 功能
- 启用逻辑删除 (字段: `isDelete`)
- 通过 `MybatisPlusInterceptor` 配置分页

### 测试

- 单元测试应放在 `src/test/java` 中
- 使用 JUnit5 和 Spring Boot Test 框架
