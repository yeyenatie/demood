# -*- coding: utf-8 -*-
"""
论文写作 AI 辅助服务（FastAPI）入口。

运行方式（在 ai-service 目录下）：
    pip install -r requirements.txt
    python app.py
"""
import os
import sys

# 保证从任意目录启动时都能正确导入同目录模块
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field

from config import PORT
from llm_client import llm_enabled, test_connection
from topic_generator import generate_topics
from outline_generator import generate_outline
from section_writer import write_section

app = FastAPI(title="论文写作 AI 辅助服务", version="1.0.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=False,
    allow_methods=["*"],
    allow_headers=["*"],
)


# ---------------- 请求模型 ----------------
class TopicsRequest(BaseModel):
    research_direction: str = Field(..., description="研究方向描述")
    paper_type: str = "毕业论文"
    llm_api_key: str = ""
    llm_base_url: str = ""
    llm_model: str = ""


class OutlineRequest(BaseModel):
    title: str = Field(..., description="论文题目")
    keywords: str = ""
    paper_type: str = "毕业论文"
    llm_api_key: str = ""
    llm_base_url: str = ""
    llm_model: str = ""


class SectionRequest(BaseModel):
    title: str = Field(..., description="论文题目")
    section_name: str = Field(..., description="章节名称，如：1 绪论")
    keywords: str = ""
    points: str = ""
    context: str = ""
    llm_api_key: str = ""
    llm_base_url: str = ""
    llm_model: str = ""


class TestRequest(BaseModel):
    llm_api_key: str = ""
    llm_base_url: str = ""
    llm_model: str = ""


# ---------------- 接口 ----------------
@app.get("/health")
def health():
    return {"status": "ok", "llm_enabled": llm_enabled()}


@app.post("/api/ai/test")
def test(req: TestRequest):
    """测试用户自设的大模型配置是否可用。"""
    ok, message = test_connection(req.llm_api_key, req.llm_base_url, req.llm_model)
    return {"ok": ok, "message": message}


@app.post("/api/ai/topics")
def topics(req: TopicsRequest):
    """题目推荐：输入研究方向，返回候选题目列表。"""
    return {"topics": generate_topics(req.research_direction, req.paper_type)}


@app.post("/api/ai/outline")
def outline(req: OutlineRequest):
    """大纲生成：输入题目与关键词，返回结构化论文大纲。"""
    return {"outline": generate_outline(req.title, req.keywords, req.paper_type)}


@app.post("/api/ai/section")
def section(req: SectionRequest):
    """章节内容生成：输入章节名与论文信息，返回正文内容。"""
    content = write_section(
        section_name=req.section_name,
        title=req.title,
        keywords=req.keywords,
        points=req.points,
        context=req.context,
        llm_api_key=req.llm_api_key,
        llm_base_url=req.llm_base_url,
        llm_model=req.llm_model,
    )
    return {"content": content}


if __name__ == "__main__":
    import uvicorn

    print(f"论文写作 AI 辅助服务已启动: http://localhost:{PORT}")
    print(f"大模型模式: {'已启用（配置了 LLM_API_KEY）' if llm_enabled() else '未启用，使用本地模板生成'}")
    uvicorn.run(app, host="0.0.0.0", port=PORT)
