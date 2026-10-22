# -*- coding: utf-8 -*-
"""
大模型调用客户端（OpenAI 兼容 Chat 接口）。

配置优先级：单次请求传入的 api_key/base_url/model > 全局配置（config.py / 环境变量）。
未配置 API Key 时返回 None，由上层自动降级为本地模板生成。
"""
import requests

from config import LLM_API_KEY, LLM_BASE_URL, LLM_MODEL

_SYSTEM_PROMPT = (
    "你是一名资深的学术论文写作助手，擅长中文论文写作与自然语言处理。"
    "请根据用户要求输出结构清晰、语言规范、符合学术风格的论文正文内容；"
    "直接给出正文段落，不要输出多余的解释、开场白或客套话。"
)


def _resolve(api_key="", base_url="", model=""):
    """解析最终生效的配置：请求参数优先，缺省回退全局配置。"""
    key = (api_key or LLM_API_KEY or "").strip()
    url = (base_url or LLM_BASE_URL or "").strip()
    mdl = (model or LLM_MODEL or "").strip()
    return key, url, mdl


def llm_enabled(api_key=""):
    """大模型是否可用：请求级密钥或全局密钥任一存在即可。"""
    key, _, _ = _resolve(api_key)
    return bool(key)


def chat(prompt, temperature=0.8, max_tokens=2048,
         api_key="", base_url="", model=""):
    """调用大模型生成文本；失败或未配置时返回 None。"""
    key, url, mdl = _resolve(api_key, base_url, model)
    if not key:
        return None
    try:
        resp = requests.post(
            f"{url.rstrip('/')}/chat/completions",
            headers={
                "Authorization": f"Bearer {key}",
                "Content-Type": "application/json",
            },
            json={
                "model": mdl,
                "messages": [
                    {"role": "system", "content": _SYSTEM_PROMPT},
                    {"role": "user", "content": prompt},
                ],
                "temperature": temperature,
                "max_tokens": max_tokens,
            },
            timeout=120,
        )
        resp.raise_for_status()
        data = resp.json()
        return data["choices"][0]["message"]["content"].strip()
    except Exception as exc:  # noqa: BLE001
        print(f"[llm_client] 大模型调用失败，已降级本地模板生成: {exc}")
        return None


def test_connection(api_key="", base_url="", model=""):
    """测试连接：用配置调用一次极简对话，返回 (是否成功, 详细提示信息)。"""
    key, url, mdl = _resolve(api_key, base_url, model)
    if not key:
        return False, "未配置 API Key，无法测试"
    try:
        resp = requests.post(
            f"{url.rstrip('/')}/chat/completions",
            headers={
                "Authorization": f"Bearer {key}",
                "Content-Type": "application/json",
            },
            json={
                "model": mdl,
                "messages": [{"role": "user", "content": "请只回复四个字：连接成功"}],
                "max_tokens": 32,
                "temperature": 0.2,
            },
            timeout=30,
        )
        if resp.status_code == 200:
            return True, f"连接成功（{mdl}）"
        if resp.status_code == 401:
            return False, f"认证失败（401）：API Key 无效、已过期或不属于该平台，请检查密钥"
        if resp.status_code == 402:
            return False, f"账户余额或额度不足（402）：请前往平台充值或检查额度"
        if resp.status_code == 404:
            return False, f"接口不存在（404）：请检查 Base URL，或模型名不支持（{mdl}）"
        if resp.status_code == 429:
            return False, "请求过于频繁（429）：请稍后再试"
        return False, f"接口返回异常（HTTP {resp.status_code}）：{resp.text[:200]}"
    except requests.exceptions.ConnectTimeout:
        return False, "连接超时：请检查网络或 Base URL 是否正确"
    except requests.exceptions.ConnectionError:
        return False, "无法连接：请检查 Base URL 与网络是否可达"
    except Exception as exc:  # noqa: BLE001
        return False, f"调用失败：{exc}"
