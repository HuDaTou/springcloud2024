---
alwaysApply: false
globs: 
  - "**/controller/*Controller.java"
  - "!**/test/**/*"
---
# API 设计规范

## RESTful URL 设计

| 功能 | 方法 | URL | 说明 |
|------|------|-----|------|
| 查询列表 | GET | `/api/users` | 查询所有 |
| 查询单个 | GET | `/api/users/{id}` | 根据ID查询 |
| 创建 | POST | `/api/users` | 新增 |
| 更新全量 | PUT | `/api/users/{id}` | 完整更新 |
| 更新部分 | PATCH | `/api/users/{id}` | 部分更新 |
| 删除 | DELETE | `/api/users/{id}` | 删除 |
| 批量操作 | POST | `/api/users/batch/delete` | 批量删除 |

## 控制器示例

详细内容请参考：[Controller 接口编写规范](file:///root/code/java/gitcode/springcloud2024/.trae/rules/后端全局规范/controller接口编写规范.md)

## 统一返回格式

详细内容请参考：[ResultData.java](file:///root/code/java/gitcode/springcloud2024/cloud-common/cloud-common-core/src/main/java/com/overthinker/cloud/common/core/resp/ResultData.java)
