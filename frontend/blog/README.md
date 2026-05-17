# Overthinker Blog Frontend

> 一个基于 Vue 3 + TypeScript 构建的现代化个人博客前端项目

## 📋 项目简介

这是一个功能完整的博客前台应用，提供文章浏览、用户交互、多媒体展示等功能，采用现代化的技术栈和响应式设计。

## 🛠️ 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | ^3.3.4 | 核心前端框架 |
| TypeScript | ^5.0.2 | 类型安全语言 |
| Vite | ^4.4.5 | 快速构建工具 |
| Element Plus | ^2.8.6 | UI 组件库 |
| Pinia | ^2.1.6 | 状态管理 |
| Vue Router | ^4.2.4 | 路由管理 |
| Tailwind CSS | ^3.4.1 | 样式框架 |
| SCSS | ^1.66.1 | CSS 预处理器 |
| VueUse | ^10.4.1 | Vue 组合式工具库 |

## ✨ 功能特性

- **文章系统**：文章列表、详情阅读、字数统计、目录导航
- **用户系统**：登录/注册、第三方登录（Gitee/GitHub）、个人设置
- **互动功能**：点赞、收藏、评论、分享
- **内容管理**：分类、标签、时间轴归档
- **娱乐模块**：留言板、心灵树洞
- **多媒体**：音乐播放器、视频、相册
- **UI 体验**：深色/浅色模式、阅读模式、响应式布局

## 📁 项目结构

```
src/
├── apis/              # API 接口管理
│   ├── article/       # 文章接口
│   ├── user/          # 用户接口
│   ├── category/      # 分类接口
│   ├── tag/           # 标签接口
│   └── ...
├── components/        # 公共组件
│   ├── Layout/        # 布局组件
│   ├── Card/          # 卡片组件
│   ├── Music/         # 音乐组件
│   └── ...
├── directives/        # 自定义指令
│   ├── vLazy.ts       # 图片懒加载
│   ├── vSlideIn.ts    # 滑动进入动画
│   └── vViewRequest.ts # 视图请求指令
├── router/            # 路由配置
│   ├── index.ts       # 路由实例
│   └── routers.ts     # 路由定义
├── store/             # Pinia 状态管理
│   └── modules/       # Store 模块
├── styles/            # 样式文件
│   ├── variable.scss  # 变量定义
│   ├── theme.scss     # 主题样式
│   └── ...
├── utils/             # 工具函数
│   ├── http.ts        # HTTP 请求封装
│   ├── auth.ts        # 认证工具
│   └── ...
├── views/             # 页面组件
│   ├── Home/          # 首页
│   ├── Article/       # 文章详情
│   ├── Pigeonhole/    # 归档页面
│   ├── Amusement/     # 娱乐功能
│   └── ...
├── App.vue            # 根组件
└── main.ts            # 入口文件
```

## 🚀 快速开始

### 环境要求

- Node.js >= 18.x
- pnpm >= 8.x

### 安装依赖

```bash
pnpm install
```

### 开发模式

```bash
pnpm dev
```

访问 http://localhost:99 查看效果。

### 构建生产版本

```bash
pnpm build
```

### 预览生产版本

```bash
pnpm preview
```

## 🔧 配置说明

### 环境变量

在 `.env.development` 或 `.env.production` 中配置：

```env
# 后端 API 地址
VITE_SERVE=http://localhost:8080/api

# 前端地址
VITE_FRONTEND_URL=http://localhost:99

# 音乐页面地址
VITE_MUSIC_FRONTEND_URL=http://localhost:3000
```

### Vite 配置

主要配置项：

- **路径别名**：`@` 指向 `src/`
- **SVG 图标**：自动加载 `src/assets/icons/` 目录下的图标
- **自动导入**：Vue、Pinia、Vue Router 自动导入
- **Gzip 压缩**：构建产物自动压缩

## 🎨 主题配置

项目支持深色/浅色模式切换，在 `src/styles/variable.scss` 中配置主题变量：

```scss
// 侧边栏卡片宽度
$card-width: 85%;

// 圆角
$border-radius: 0.6em;

// 自定义主题颜色
$menuActiveText: var(--themeColor);
```

## 📱 响应式设计

项目支持多设备适配：

- **移动端**：< 768px
- **平板**：768px - 910px
- **桌面端**：> 910px

## 📦 组件说明

### 核心组件

| 组件 | 说明 | 路径 |
|------|------|------|
| Header | 顶部导航栏 | `components/Layout/Header/` |
| Footer | 页脚 | `components/Layout/Footer/` |
| Card | 侧边栏卡片 | `components/Card/` |
| CardEssay | 文章列表卡片 | `components/CardEssay/` |
| BottomRightLayout | 右下角工具栏 | `components/BottomRightLayout/` |
| SvgIcon | SVG 图标组件 | `components/SvgIcon/` |

### 页面组件

| 页面 | 路径 | 说明 |
|------|------|------|
| 首页 | `views/Home/` | 文章列表、推荐文章 |
| 文章详情 | `views/Article/` | 文章内容、评论区 |
| 时间轴 | `views/Pigeonhole/TimeLine/` | 文章时间线归档 |
| 分类 | `views/Pigeonhole/Category/` | 分类列表 |
| 标签 | `views/Pigeonhole/Tags/` | 标签云 |
| 留言板 | `views/Amusement/Message/` | 用户留言 |
| 树洞 | `views/Amusement/TreeHole/` | 匿名树洞 |
| 音乐 | `views/Music/` | 音乐播放器 |
| 相册 | `views/Photo/` | 图片相册 |
| 视频 | `views/Video/` | 视频播放 |
| 友链 | `views/Link/` | 友情链接 |
| 关于 | `views/About/` | 关于网站 |
| 登录/注册 | `views/Welcome/` | 用户认证 |
| 设置 | `views/Setting/` | 用户设置 |

## 📡 API 接口

### 接口目录

```
apis/
├── article/           # 文章接口
│   ├── index.ts       # 接口定义
│   └── type.ts        # 类型定义
├── user/              # 用户接口
├── category/          # 分类接口
├── tag/               # 标签接口
├── comment/           # 评论接口
├── like/              # 点赞接口
├── favorite/          # 收藏接口
└── website/           # 网站配置接口
```

### 接口规范

所有接口返回统一格式：

```typescript
interface ResponseBody<T = any> {
  code: number
  data?: T
  msg: string
}
```

## 🧪 代码规范

### ESLint

```bash
pnpm lint
```

### TypeScript 检查

```bash
pnpm typecheck
```

### Git 提交规范

```
<type>(<scope>): <subject>

<body>

<footer>
```

**type 类型**：

- `feat` - 新功能
- `fix` - Bug 修复
- `docs` - 文档更新
- `style` - 代码格式
- `refactor` - 重构
- `perf` - 性能优化
- `test` - 测试相关
- `chore` - 构建/工具

## 📄 许可证

MIT License

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

---

**Author**: Overthinker  
**Version**: 1.0.0  
**Last Updated**: 2024
