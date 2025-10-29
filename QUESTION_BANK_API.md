# 题库管理API文档

## 功能概述

实现了完整的题库管理系统，包括：

### 管理员功能
- 创建题库
- 更新题库信息
- 审核题库（通过/拒绝）
- 删除题库
- 查看题库列表（分页）
- 查看题库详情
- 题库题目管理（添加/移除题目）

### 用户功能
- 查看题库列表（分页，只显示已审核通过的）
- 查看题库详情
- 查看题库下的题目列表

## API接口

### 管理员接口（需要管理员权限）

#### 1. 创建题库
- **接口**: `POST /api/questionBank/add`
- **权限**: 管理员
- **请求体**:
  ```json
  {
    "title": "题库标题",
    "description": "题库描述",
    "picture": "题库图片URL",
    "priority": 1
  }
  ```

#### 2. 更新题库
- **接口**: `POST /api/questionBank/update`
- **权限**: 管理员
- **请求体**:
  ```json
  {
    "id": 1,
    "title": "更新后的标题",
    "description": "更新后的描述",
    "picture": "更新后的图片URL",
    "priority": 2
  }
  ```

#### 3. 审核题库
- **接口**: `POST /api/questionBank/review`
- **权限**: 管理员
- **请求体**:
  ```json
  {
    "id": 1,
    "reviewStatus": 1,
    "reviewMessage": "审核通过"
  }
  ```
- **reviewStatus**: 0-待审核, 1-通过, 2-拒绝

#### 4. 删除题库
- **接口**: `POST /api/questionBank/delete`
- **权限**: 管理员
- **请求体**:
  ```json
  {
    "id": 1
  }
  ```

#### 5. 获取题库详情（管理员）
- **接口**: `GET /api/questionBank/get?id={id}`
- **权限**: 管理员

#### 6. 分页获取题库列表（管理员）
- **接口**: `POST /api/questionBank/list/page`
- **权限**: 管理员
- **请求体**:
  ```json
  {
    "current": 1,
    "pageSize": 10,
    "title": "搜索标题",
    "reviewStatus": 1
  }
  ```

### 题库题目管理接口

#### 7. 添加题目到题库
- **接口**: `POST /api/questionBankQuestion/add`
- **权限**: 管理员
- **参数**:
  - `questionBankId`: 题库ID
  - `questionId`: 题目ID
  - `questionOrder`: 题目顺序（可选）

#### 8. 从题库移除题目
- **接口**: `POST /api/questionBankQuestion/remove`
- **权限**: 管理员
- **参数**:
  - `questionBankId`: 题库ID
  - `questionId`: 题目ID

#### 9. 批量添加题目到题库
- **接口**: `POST /api/questionBankQuestion/batchAdd`
- **权限**: 管理员
- **参数**:
  - `questionBankId`: 题库ID
- **请求体**: 题目ID列表 `[1, 2, 3]`

### 用户接口（无需权限）

#### 10. 分页获取题库列表（用户）
- **接口**: `POST /api/questionBank/list/page/vo`
- **权限**: 无需权限
- **说明**: 只显示审核通过的题库
- **请求体**:
  ```json
  {
    "current": 1,
    "pageSize": 10,
    "title": "搜索标题"
  }
  ```

#### 11. 获取题库详情（用户）
- **接口**: `GET /api/questionBank/get/vo?id={id}`
- **权限**: 无需权限
- **说明**: 只能查看审核通过的题库

#### 12. 获取题库下的题目列表
- **接口**: `GET /api/questionBank/questions?questionBankId={id}&current={page}&pageSize={size}`
- **权限**: 无需权限
- **说明**: 只显示题库中已审核通过的题目

#### 13. 获取题库关联的题目ID列表
- **接口**: `GET /api/questionBankQuestion/questionIds?questionBankId={id}`
- **权限**: 无需权限

## 数据模型

### QuestionBank（题库）
- `id`: 主键ID
- `title`: 标题
- `description`: 描述
- `picture`: 图片URL
- `userId`: 创建用户ID
- `reviewStatus`: 审核状态（0-待审核, 1-通过, 2-拒绝）
- `reviewMessage`: 审核信息
- `reviewerId`: 审核人ID
- `reviewTime`: 审核时间
- `priority`: 优先级
- `viewNum`: 浏览量
- `createTime`: 创建时间
- `updateTime`: 更新时间
- `editTime`: 编辑时间
- `isDelete`: 逻辑删除标记

### Question（题目）
- `id`: 主键ID
- `title`: 标题
- `content`: 内容
- `tags`: 标签（JSON数组）
- `answer`: 答案
- `userId`: 创建用户ID
- `reviewStatus`: 审核状态
- `reviewMessage`: 审核信息
- `reviewerId`: 审核人ID
- `viewNum`: 浏览量
- `thumbNum`: 点赞数
- `favourNum`: 收藏数
- `priority`: 优先级
- `source`: 来源
- `needVip`: 是否需要会员

### QuestionBankQuestion（题库题目关联）
- `id`: 主键ID
- `questionBankId`: 题库ID
- `questionId`: 题目ID
- `userId`: 创建用户ID
- `questionOrder`: 题目顺序
- `createTime`: 创建时间
- `updateTime`: 更新时间

## 使用说明

1. 管理员可以创建、编辑、审核、删除题库
2. 管理员可以管理题库中的题目关联
3. 普通用户只能查看已审核通过的题库和题目
4. 所有接口都支持分页查询，默认每页10条，最大20条
5. 系统使用了逻辑删除，数据安全可靠
6. 支持按标题、审核状态等条件进行筛选查询