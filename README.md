# 智能化人力资源管理系统 (HRMS)

## 项目简介
一套覆盖人力资源全生命周期的智能化管理系统，集成AI大模型能力，对接主流招聘平台，提供从招聘到离职的全流程HR管理解决方案。

## 技术栈
- **前端**: React 18 + TypeScript + Ant Design Pro + Vite
- **后端**: Spring Boot 3 + PostgreSQL + Redis
- **AI集成**: OpenAI API + 向量搜索
- **部署**: Docker + Docker Compose

## 项目结构
```
hrms/
├── frontend/          # React前端项目
├── backend/           # Spring Boot后端项目
├── docs/             # 项目文档
├── scripts/          # 部署和工具脚本
├── docker-compose.yml # Docker容器编排
└── README.md         # 项目说明
```

## 快速开始

### 环境要求
- Node.js 18+
- Java 17+
- Docker & Docker Compose
- PostgreSQL 15+
- Redis 7+

### 开发环境启动

1. **启动基础服务**
```bash
docker-compose up -d postgres redis
```

2. **启动后端服务**
```bash
cd backend
./mvnw spring-boot:run
```

3. **启动前端服务**
```bash
cd frontend
npm install
npm run dev
```

### 访问地址
- 前端应用: http://localhost:3000
- 后端API: http://localhost:8080
- API文档: http://localhost:8080/swagger-ui.html

## MVP功能
- ✅ 用户认证和权限管理
- ✅ 招聘管理（职位、候选人、简历）
- ✅ AI简历解析和智能筛选
- ✅ 基础组织人事管理
- ✅ 数据统计和报表

## 开发规范
- [前端开发规范](./docs/前端开发规范.md)
- [后端开发规范](./docs/后端开发规范.md)
- [API设计规范](./docs/API设计规范.md)
- [数据库设计规范](./docs/数据库设计规范.md)

## 部署指南
- [开发环境部署](./docs/开发环境部署.md)
- [生产环境部署](./docs/生产环境部署.md)

## 贡献指南
1. Fork 项目
2. 创建特性分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

## 许可证
MIT License