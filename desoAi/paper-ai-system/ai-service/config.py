# -*- coding: utf-8 -*-
"""
AI 服务全局配置。
不配置大模型 API Key 时，系统自动使用「本地模板 + NLP 关键词」生成内容（可离线演示）；
配置后优先调用大模型（OpenAI 兼容接口，如 DeepSeek / 通义 / 智谱等），生成质量更高。
"""
import os

# ============ 大模型 API 配置（OpenAI 兼容格式）============
LLM_API_KEY = os.getenv("LLM_API_KEY", "")          # 例如 sk-xxxx，留空 = 本地模板模式
LLM_BASE_URL = os.getenv("LLM_BASE_URL", "https://api.deepseek.com/v1")
LLM_MODEL = os.getenv("LLM_MODEL", "deepseek-chat")

# ============ 服务端口 ============
PORT = int(os.getenv("AI_PORT", "8000"))
