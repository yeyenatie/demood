# 论文写作 AI 辅助系统

基于 **Spring Boot + Vue3 + Python AI** 的论文写作辅助系统，核心能力为 **AI 论文生成**：
输入研究方向 → AI 推荐题目 → 生成论文大纲 → 逐章生成正文（摘要 / 绪论 / 相关技术 / 系统设计 / 实验 / 总结）→ 全文预览与保存。

AI 部分采用 **机器学习 + 自然语言处理** 技术：基于 jieba 分词与 TF-IDF / TextRank 关键词提取，
结合大模型（OpenAI 兼容接口）或本地学术模板生成论文内容，未配置大模型密钥时也可完整离线演示。

---

## 一、技术栈

| 端 | 技术 |
|---|---|
| 前端 | Vue3 + Vite + Element Plus + Axios + Vue Router |
| 后端 | Spring Boot 2.7 + MyBatis-Plus + MySQL 8 + JWT（jjwt）+ BCrypt |
| AI 服务 | Python 3.8+ + FastAPI + jieba（NLP）+ 大模型 API（可选） |

## 二、系统架构

```
┌───────────────────────────┐
│  Vue3 + Element Plus 前端  │  http://localhost:5173
└─────────────┬─────────────┘
              │  REST /api（Vite 代理）
┌─────────────▼─────────────┐
│   Spring Boot 后端 (8080)   │   JWT 鉴权 · 业务逻辑 · MySQL 存取
└─────────────┬─────────────┘
              │  HTTP JSON
┌─────────────▼─────────────┐
│   Python AI 服务 (8000)     │   FastAPI · jieba 分词/关键词 · 大模型
└───────────────────────────┘
```

## 三、功能清单

- 用户注册 / 登录（JWT 鉴权、密码 BCrypt 加密）
- **AI 生成次数限制**：使用**默认模型**时，每个用户每周限 10 次 AI 生成（题目推荐 / 大纲 / 章节生成各计 1 次，跨周自动重置，可在 `application.yml` 的 `paper-ai.ai-gen-limit` 调整）；**使用自己的模型不消耗周次数**
- **多 AI 模型管理**：每个用户可添加多个自配模型（名称 / API Key / Base URL / 模型名称），支持逐个测试连接；创作中心生成时可随时选择「默认模型」或任意一个自配模型
- **AI 题目推荐**：输入研究方向，NLP 提取关键词后推荐 5 个候选题目并附理由
- **AI 大纲生成**：支持毕业论文 / 期刊论文 / 课程论文三种体裁，生成结构化大纲（可编辑、增删章节）
- **AI 章节生成**：摘要、绪论、相关技术、系统设计、实验、总结等逐章自动写作，生成后可自由编辑
- **全文预览 / 保存**：一键预览完整论文，保存到个人论文库
- **我的论文**：论文列表、继续创作、删除
- **Word 草稿下载**：一键导出 .doc 格式草稿

## 四、目录结构

```
paper-ai-system/
├── ai-service/                 # Python AI 服务（FastAPI）
│   ├── app.py                  # 服务入口（端口 8000）
│   ├── config.py               # 大模型 API 配置
│   ├── nlp_utils.py            # jieba 分词 + TF-IDF/TextRank 关键词提取
│   ├── llm_client.py           # 大模型调用客户端（可选）
│   ├── topic_generator.py      # 题目推荐
│   ├── outline_generator.py    # 大纲生成
│   ├── section_writer.py       # 章节内容生成（大模型优先、本地模板兜底）
│   └── requirements.txt
├── backend/                    # Spring Boot 后端（端口 8080）
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/paperai/   # 启动类 / controller / service / mapper / entity / dto / common / config
│       └── resources/
│           ├── application.yml # 数据库、AI 服务地址等配置
│           └── db/schema.sql   # 建库建表脚本
└── frontend/                   # Vue3 前端（端口 5173）
    ├── package.json
    ├── vite.config.js
    └── src/
        ├── api/                # axios 封装与接口
        ├── router/             # 路由与登录守卫
        ├── views/              # 登录 / 注册 / 创作中心 / 我的论文
        └── assets/
```

## 五、环境要求

- JDK 8+（推荐 8 或 11）
- Maven 3.6+
- Node.js 18+
- Python 3.8+
- MySQL 8

## 六、启动步骤

### 1. 初始化数据库

用 Navicat / 命令行执行 `backend/src/main/resources/db/schema.sql`，自动创建数据库 `paper_ai` 及 `user`、`paper` 两张表。

> 记得修改 `backend/src/main/resources/application.yml` 中的数据库账号密码（默认 root / 123456）。

### 2. 启动 Python AI 服务（端口 8000）

```bash
cd ai-service
pip install -r requirements.txt
python app.py
```

看到 `论文写作 AI 辅助服务已启动: http://localhost:8000` 即成功。

**（可选）接入大模型，提升生成质量：**

配置 `LLM_API_KEY` 环境变量（OpenAI 兼容接口，默认 DeepSeek）：

```bash
# Windows
setx LLM_API_KEY sk-你的密钥
# macOS / Linux
export LLM_API_KEY=sk-你的密钥
```

也可以直接改 `ai-service/config.py` 中的 `LLM_API_KEY / LLM_BASE_URL / LLM_MODEL`。
不配置密钥时自动使用本地模板 + NLP 关键词生成，可正常演示。

### 3. 启动 Spring Boot 后端（端口 8080）

用 IDEA 打开 `backend` 目录，等待 Maven 下载依赖后，运行 `PaperAiApplication` 主类。

> 若 IDEA 中文乱码：Settings → Editor → File Encodings 全部设为 UTF-8。

### 4. 启动 Vue3 前端（端口 5173）

```bash
cd frontend
npm install
npm run dev
```

浏览器访问 **http://localhost:5173**，注册账号即可开始使用。

## 七、接口一览

| 方法 | 路径 | 说明 | 鉴权 |
|---|---|---|---|
| POST | /api/user/register | 注册 | 否 |
| POST | /api/user/login | 登录（返回 token） | 否 |
| GET | /api/paper/list | 我的论文列表 | 是 |
| POST | /api/paper | 创建论文 | 是 |
| GET | /api/paper/{id} | 论文详情 | 是 |
| PUT | /api/paper/{id} | 保存论文 | 是 |
| DELETE | /api/paper/{id} | 删除论文 | 是 |
| POST | /api/ai/topics | AI 题目推荐 | 是 |
| POST | /api/ai/outline | AI 大纲生成（可携带 paperId 自动保存） | 是 |
| POST | /api/ai/section | AI 章节生成 | 是 |
| GET | /api/ai/setting | 获取当前用户的 AI 模型配置 | 是 |
| PUT | /api/ai/setting | 保存当前用户的 AI 模型配置 | 是 |
| POST | /api/ai/test | 测试当前用户输入的大模型配置 | 是 |
| GET | /api/ai/quota | 查询本周 AI 生成配额（used/limit/remaining） | 是 |

> 说明：用户自设的 API Key 保存在本系统数据库中（课程项目演示用途）。正式对外部署时应改为加密存储或服务端统一管理。

## 八、常见问题

- **报错「AI 服务连接失败」**：请确认 Python AI 服务已启动（`python app.py`），且 `application.yml` 中 `paper-ai.ai-service-url` 与 AI 服务端口一致。
- **MySQL 连接失败**：检查 MySQL 是否启动、账号密码是否正确、schema.sql 是否已执行。
- **端口被占用**：分别修改 `application.yml`（server.port）、`vite.config.js`（server.port）、`config.py`（PORT）。
- **前端页面打不开 / 白屏**：确认已在 `frontend` 目录执行 `npm install`，并已启动 `npm run dev`。
