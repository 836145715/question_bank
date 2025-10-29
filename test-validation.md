# JSR303 验证功能测试

## 测试接口

### 1. 测试登录接口验证
**URL:** `POST http://localhost:8101/api/user/login`

**测试用例1 - 空参数**
```json
{
  "userAccount": "",
  "userPassword": ""
}
```
**预期结果:** 返回错误信息 "用户账号不能为空" 或 "用户密码不能为空"

**测试用例2 - 缺少参数**
```json
{
  "userAccount": "test"
}
```
**预期结果:** 返回错误信息 "用户密码不能为空"

**测试用例3 - 有效参数**
```json
{
  "userAccount": "test",
  "userPassword": "123456"
}
```
**预期结果:** 进入业务逻辑处理

### 2. 测试题库接口验证
**URL:** `POST http://localhost:8101/api/questionBank/add`

**测试用例 - 空标题**
```json
{
  "title": "",
  "description": "测试描述"
}
```
**预期结果:** 返回错误信息 "题库标题不能为空"

### 3. 测试简单验证接口
**URL:** `POST http://localhost:8101/api/test/validate`

**测试用例 - 空参数**
```json
{
  "userAccount": "",
  "userPassword": ""
}
```
**预期结果:** 返回验证错误信息

## 可能的问题排查

1. **验证不生效的可能原因:**
   - 缺少 `@Valid` 注解
   - 缺少 `ValidationConfig` 配置
   - 全局异常处理器未正确配置
   - Spring Boot 版本兼容性问题

2. **调试步骤:**
   - 检查日志中是否有验证异常信息
   - 确认异常处理器是否被调用
   - 验证 DTO 类中的注解是否正确

## 验证注解说明

- `@NotBlank`: 用于字符串，不能为 null 且长度必须大于 0
- `@NotEmpty`: 用于集合、数组、Map，不能为 null 且长度必须大于 0
- `@NotNull`: 不能为 null
- `@Min`: 数值最小值
- `@Max`: 数值最大值
- `@Size`: 字符串或集合长度范围
- `@Pattern`: 正则表达式校验