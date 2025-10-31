# HR系统 MVP架构框架文档

## 1. MVP范围定义

### 1.1 MVP目标
快速验证核心业务价值，在3-4个月内交付可用的HR系统核心功能，重点解决招聘和基础人事管理需求。

### 1.2 MVP功能范围

#### ✅ MVP v1.0 包含功能
**核心模块：**
1. **用户认证系统**
   - 用户注册/登录
   - 基础权限管理
   - 企业账号管理

2. **招聘管理系统**
   - 职位发布管理
   - 简历上传和基础解析
   - 候选人状态跟踪
   - 简单的AI简历筛选

3. **组织人事系统**
   - 基础员工档案
   - 简单组织架构
   - 员工入职流程

4. **基础数据看板**
   - 招聘数据统计
   - 简单报表展示

#### ❌ MVP v1.0 暂不包含
- 考勤管理
- 薪酬管理  
- 绩效管理
- 培训学习
- 复杂的第三方平台集成
- 高级AI分析功能

### 1.3 MVP用户角色
- **企业管理员**：系统配置、用户管理
- **HR专员**：日常招聘操作
- **部门主管**：参与面试、候选人评价

## 2. 技术架构设计

### 2.1 整体架构
```
前端层 (React SPA)
    ↓
API网关 (Nginx + Load Balancer)
    ↓
应用层 (Spring Boot)
    ↓
数据层 (PostgreSQL + Redis)
    ↓
基础设施 (Docker + 云服务)
```

### 2.2 技术栈选择

#### 前端技术栈
```javascript
// 核心框架
React 18.2+ 
TypeScript 5.0+
Vite 4.0+ (构建工具)

// UI组件库
Ant Design 5.0+ 
Ant Design Pro (企业级模板)

// 状态管理
Redux Toolkit
RTK Query (数据获取)

// 路由和工具
React Router 6
Axios (HTTP客户端)
Day.js (日期处理)
```

#### 后端技术栈
```java
// 核心框架
Spring Boot 3.1+
Spring Security 6.0+ (安全认证)
Spring Data JPA (数据访问)

// 数据库
PostgreSQL 15+ (主数据库)
Redis 7.0+ (缓存)

// 工具库
MapStruct (对象映射)
Validation (数据验证)
Jackson (JSON处理)
```

#### 基础设施
```yaml
# 容器化
Docker & Docker Compose

# 数据库
PostgreSQL 15+
Redis 7.0+

# 文件存储
Local File System (MVP阶段)
# 后期可升级为云存储(OSS/S3)

# AI服务
OpenAI API
# 后期可集成国产大模型
```

### 2.3 系统架构图

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Web Frontend  │    │  Mobile H5      │    │  Admin Panel    │
│   (React SPA)   │    │  (Responsive)   │    │  (System Mgmt)  │
└─────────┬───────┘    └─────────┬───────┘    └─────────┬───────┘
          │                      │                      │
          └──────────────────────┼──────────────────────┘
                                 │
              ┌─────────────────────────────────┐
              │         API Gateway             │
              │       (Nginx + CORS)            │
              └─────────────────┬───────────────┘
                                │
              ┌─────────────────────────────────┐
              │      Spring Boot Application    │
              │                                 │
              │  ┌─────────────┐ ┌─────────────┐│
              │  │Auth Service │ │User Service ││
              │  └─────────────┘ └─────────────┘│
              │  ┌─────────────┐ ┌─────────────┐│
              │  │Job Service  │ │Resume Service││
              │  └─────────────┘ └─────────────┘│
              │  ┌─────────────┐ ┌─────────────┐│
              │  │Employee Svc │ │AI Service   ││
              │  └─────────────┘ └─────────────┘│
              └─────────────────┬───────────────┘
                                │
              ┌─────────────────────────────────┐
              │         Data Layer              │
              │                                 │
              │ ┌─────────────┐ ┌─────────────┐ │
              │ │ PostgreSQL  │ │   Redis     │ │
              │ │ (Main DB)   │ │  (Cache)    │ │
              │ └─────────────┘ └─────────────┘ │
              └─────────────────────────────────┘
```

## 3. 数据库设计

### 3.1 核心数据表

#### 用户相关表
```sql
-- 企业表
CREATE TABLE companies (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) UNIQUE,
    contact_email VARCHAR(100),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 用户表  
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT REFERENCES companies(id),
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    real_name VARCHAR(50),
    role VARCHAR(20) NOT NULL, -- ADMIN, HR, MANAGER, EMPLOYEE
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### 招聘相关表
```sql
-- 职位表
CREATE TABLE jobs (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT REFERENCES companies(id),
    title VARCHAR(100) NOT NULL,
    department VARCHAR(50),
    description TEXT,
    requirements TEXT,
    salary_min INTEGER,
    salary_max INTEGER,
    status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, PAUSED, CLOSED
    created_by BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 候选人表
CREATE TABLE candidates (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT REFERENCES companies(id),
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    source VARCHAR(50), -- UPLOAD, BOSS, LIEPIN
    status VARCHAR(20) DEFAULT 'NEW', -- NEW, SCREENING, INTERVIEW, OFFER, HIRED, REJECTED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 简历表
CREATE TABLE resumes (
    id BIGSERIAL PRIMARY KEY,
    candidate_id BIGINT REFERENCES candidates(id),
    file_name VARCHAR(255),
    file_path VARCHAR(500),
    file_size BIGINT,
    parsed_content JSONB, -- 解析后的结构化数据
    ai_score INTEGER, -- AI评分(0-100)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 求职申请表
CREATE TABLE job_applications (
    id BIGSERIAL PRIMARY KEY,
    job_id BIGINT REFERENCES jobs(id),
    candidate_id BIGINT REFERENCES candidates(id),
    status VARCHAR(20) DEFAULT 'APPLIED',
    ai_match_score INTEGER, -- 匹配度评分
    hr_notes TEXT,
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### 员工相关表
```sql
-- 部门表
CREATE TABLE departments (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT REFERENCES companies(id),
    name VARCHAR(100) NOT NULL,
    parent_id BIGINT REFERENCES departments(id),
    manager_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 员工表
CREATE TABLE employees (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT REFERENCES companies(id),
    user_id BIGINT REFERENCES users(id),
    employee_no VARCHAR(50),
    department_id BIGINT REFERENCES departments(id),
    position VARCHAR(100),
    hire_date DATE,
    status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, RESIGNED, TERMINATED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 3.2 索引策略
```sql
-- 性能优化索引
CREATE INDEX idx_users_company_id ON users(company_id);
CREATE INDEX idx_jobs_company_status ON jobs(company_id, status);
CREATE INDEX idx_candidates_company_status ON candidates(company_id, status);
CREATE INDEX idx_applications_job_id ON job_applications(job_id);
CREATE INDEX idx_applications_candidate_id ON job_applications(candidate_id);
```

## 4. API设计

### 4.1 API规范

#### 统一响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": "2024-01-01T00:00:00Z"
}

// 错误响应
{
  "code": 400,
  "message": "参数验证失败",
  "errors": [
    {
      "field": "email",
      "message": "邮箱格式不正确"
    }
  ],
  "timestamp": "2024-01-01T00:00:00Z"
}
```

#### 分页响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "items": [],
    "pagination": {
      "page": 1,
      "size": 20,
      "total": 100,
      "pages": 5
    }
  }
}
```

### 4.2 核心API接口

#### 认证相关API
```http
POST   /api/auth/login          # 用户登录
POST   /api/auth/logout         # 用户登出
POST   /api/auth/refresh        # 刷新令牌
GET    /api/auth/profile        # 获取用户信息
PUT    /api/auth/profile        # 更新用户信息
```

#### 招聘管理API
```http
# 职位管理
GET    /api/jobs                # 获取职位列表
POST   /api/jobs                # 创建职位
GET    /api/jobs/{id}           # 获取职位详情
PUT    /api/jobs/{id}           # 更新职位
DELETE /api/jobs/{id}           # 删除职位

# 候选人管理
GET    /api/candidates          # 获取候选人列表
POST   /api/candidates          # 创建候选人
GET    /api/candidates/{id}     # 获取候选人详情
PUT    /api/candidates/{id}     # 更新候选人

# 简历管理
POST   /api/resumes/upload      # 上传简历
GET    /api/resumes/{id}        # 获取简历详情
POST   /api/resumes/{id}/parse  # 解析简历

# 求职申请
GET    /api/jobs/{jobId}/applications     # 获取职位申请
POST   /api/jobs/{jobId}/applications     # 申请职位
PUT    /api/applications/{id}/status      # 更新申请状态
```

#### 组织人事API
```http
# 部门管理
GET    /api/departments         # 获取部门列表
POST   /api/departments         # 创建部门
PUT    /api/departments/{id}    # 更新部门

# 员工管理
GET    /api/employees           # 获取员工列表
POST   /api/employees           # 创建员工
GET    /api/employees/{id}      # 获取员工详情
PUT    /api/employees/{id}      # 更新员工信息
```

#### AI功能API
```http
POST   /api/ai/resume/analyze   # AI简历分析
POST   /api/ai/job/match        # 职位匹配分析
POST   /api/ai/interview/questions # 生成面试问题
```

## 5. 前端架构设计

### 5.1 项目结构
```
frontend/
├── public/
├── src/
│   ├── components/          # 通用组件
│   │   ├── Layout/         # 布局组件
│   │   ├── Forms/          # 表单组件
│   │   └── Charts/         # 图表组件
│   ├── pages/              # 页面组件
│   │   ├── Auth/           # 认证页面
│   │   ├── Jobs/           # 招聘管理
│   │   ├── Candidates/     # 候选人管理
│   │   ├── Employees/      # 员工管理
│   │   └── Dashboard/      # 仪表盘
│   ├── services/           # API服务
│   ├── store/              # 状态管理
│   ├── utils/              # 工具函数
│   ├── types/              # TypeScript类型
│   └── styles/             # 样式文件
├── package.json
└── vite.config.ts
```

### 5.2 核心组件设计

#### 路由配置
```typescript
// src/router/index.tsx
const routes = [
  {
    path: '/login',
    element: <LoginPage />
  },
  {
    path: '/',
    element: <MainLayout />,
    children: [
      { path: 'dashboard', element: <Dashboard /> },
      { path: 'jobs', element: <JobList /> },
      { path: 'jobs/create', element: <JobCreate /> },
      { path: 'candidates', element: <CandidateList /> },
      { path: 'employees', element: <EmployeeList /> }
    ]
  }
];
```

#### 状态管理
```typescript
// src/store/slices/authSlice.ts
interface AuthState {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  loading: boolean;
}

// src/store/slices/jobSlice.ts  
interface JobState {
  jobs: Job[];
  currentJob: Job | null;
  loading: boolean;
  pagination: Pagination;
}
```

### 5.3 UI组件库配置
```typescript
// src/theme/index.ts - Ant Design主题配置
export const theme = {
  token: {
    colorPrimary: '#1890ff',
    borderRadius: 6,
    wireframe: false,
  },
  components: {
    Layout: {
      siderBg: '#001529',
      triggerBg: '#1890ff',
    }
  }
};
```

## 6. 后端架构设计

### 6.1 项目结构
```
backend/
├── src/main/java/com/hrms/
│   ├── HrmsApplication.java
│   ├── config/             # 配置类
│   │   ├── SecurityConfig.java
│   │   ├── DatabaseConfig.java
│   │   └── RedisConfig.java
│   ├── controller/         # 控制器
│   │   ├── AuthController.java
│   │   ├── JobController.java
│   │   └── CandidateController.java
│   ├── service/            # 业务服务
│   │   ├── AuthService.java
│   │   ├── JobService.java
│   │   └── ResumeService.java
│   ├── repository/         # 数据访问
│   ├── entity/             # 实体类
│   ├── dto/                # 数据传输对象
│   ├── exception/          # 异常处理
│   └── utils/              # 工具类
├── src/main/resources/
│   ├── application.yml
│   └── db/migration/       # 数据库迁移脚本
└── pom.xml
```

### 6.2 核心配置

#### 应用配置
```yaml
# application.yml
spring:
  profiles:
    active: dev
  
  datasource:
    url: jdbc:postgresql://localhost:5432/hrms
    username: ${DB_USERNAME:hrms_user}
    password: ${DB_PASSWORD:hrms_pass}
    
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    
  redis:
    host: localhost
    port: 6379
    
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB

server:
  port: 8080
  
jwt:
  secret: ${JWT_SECRET:your-secret-key}
  expiration: 86400000 # 24小时

ai:
  openai:
    api-key: ${OPENAI_API_KEY}
    base-url: https://api.openai.com/v1
```

#### 安全配置
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated())
            .addFilterBefore(jwtAuthenticationFilter(), 
                UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
```

## 7. 部署架构

### 7.1 开发环境部署 (Docker Compose)
```yaml
# docker-compose.yml
version: '3.8'
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: hrms
      POSTGRES_USER: hrms_user
      POSTGRES_PASSWORD: hrms_pass
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"

  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      DB_USERNAME: hrms_user
      DB_PASSWORD: hrms_pass
      REDIS_HOST: redis
    depends_on:
      - postgres
      - redis

  frontend:
    build: ./frontend
    ports:
      - "3000:80"
    depends_on:
      - backend

volumes:
  postgres_data:
```

### 7.2 生产环境考虑
- **容器编排**：Kubernetes 或 Docker Swarm
- **负载均衡**：Nginx 或云厂商LB
- **数据库**：PostgreSQL集群 + 读写分离
- **缓存**：Redis集群
- **存储**：云对象存储(OSS/S3)
- **监控**：Prometheus + Grafana
- **日志**：ELK Stack

## 8. MVP开发计划

### 8.1 迭代计划 (3个月)

#### Sprint 1 (2周) - 基础框架
- ✅ 项目初始化和环境搭建
- ✅ 数据库设计和初始化
- ✅ 用户认证系统
- ✅ 基础权限管理
- ✅ 前端框架搭建

#### Sprint 2 (2周) - 招聘核心功能
- ✅ 职位管理CRUD
- ✅ 候选人信息管理
- ✅ 简历上传功能
- ✅ 基础的简历解析

#### Sprint 3 (2周) - AI功能集成
- ✅ AI服务集成
- ✅ 简历智能解析
- ✅ 简单的匹配评分
- ✅ 候选人筛选功能

#### Sprint 4 (2周) - 组织人事
- ✅ 部门管理
- ✅ 员工档案管理
- ✅ 入职流程
- ✅ 权限细化

#### Sprint 5 (2周) - 数据展示
- ✅ 招聘数据统计
- ✅ 基础报表功能
- ✅ 数据可视化
- ✅ 用户体验优化

#### Sprint 6 (2周) - 测试和优化
- ✅ 功能测试
- ✅ 性能优化
- ✅ 安全加固
- ✅ 部署准备

### 8.2 人员配置建议
- **项目经理** 1人 - 项目管理、需求对接
- **前端开发** 2人 - React开发、UI实现
- **后端开发** 2人 - Spring Boot开发、API设计
- **AI工程师** 1人 - AI功能集成、算法优化
- **测试工程师** 1人 - 功能测试、自动化测试
- **UI/UX设计师** 1人 - 界面设计、用户体验

### 8.3 关键里程碑
- **Week 2**: 基础框架完成，可以登录和基本导航
- **Week 4**: 职位和候选人管理功能完成
- **Week 6**: AI简历解析功能上线
- **Week 8**: 组织人事功能完成
- **Week 10**: 数据统计和报表功能完成
- **Week 12**: MVP版本上线就绪

## 9. 风险控制

### 9.1 技术风险
- **AI API限制**：准备备用方案，如本地模型
- **第三方依赖**：关键功能避免强依赖第三方
- **性能瓶颈**：早期进行压力测试

### 9.2 业务风险  
- **需求变更**：固定MVP范围，变更放入下版本
- **用户体验**：定期用户测试，快速迭代
- **数据安全**：严格的权限控制和数据加密

### 9.3 项目风险
- **人员风险**：关键模块多人掌握，文档完善
- **进度风险**：每周进度检查，及时调整
- **质量风险**：代码评审制度，自动化测试

这个MVP架构框架为HR系统的快速开发提供了清晰的技术路线和实施计划。