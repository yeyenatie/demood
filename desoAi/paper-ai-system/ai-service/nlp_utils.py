# -*- coding: utf-8 -*-
"""
NLP 工具模块：中文分词、关键词提取（TF-IDF / TextRank）。
体现机器学习 + 自然语言处理的技术点：对用户输入的研究方向进行分词与关键词抽取，
为题目推荐、大纲定制、章节写作提供核心特征。
"""
import re

import jieba
import jieba.analyse

# 补充停用词（过滤常见无实义词）
_EXTRA_STOP_WORDS = {
    "基于", "一种", "方法", "研究", "的", "了", "与", "和", "及", "以及",
    "面向", "应用", "实现", "设计", "系统", "平台", "技术", "分析", "相关",
    "the", "a", "an", "of", "and", "for", "based", "using", "in", "on",
}


def extract_keywords(text, topk=6):
    """
    提取文本关键词。
    策略：TF-IDF 为主（jieba.analyse.extract_tags），TextRank 兜底补充，
    两者结果按重要性合并去重，返回前 topk 个关键词。
    """
    if not text or not text.strip():
        return []
    text = re.sub(r"\s+", " ", text).strip()
    tfidf = jieba.analyse.extract_tags(text, topK=topk * 2)
    textrank = jieba.analyse.textrank(text, topK=topk * 2)

    keywords = []
    for kw in tfidf + textrank:
        kw = kw.strip()
        if not kw or kw.lower() in _EXTRA_STOP_WORDS:
            continue
        if kw not in keywords:
            keywords.append(kw)
        if len(keywords) >= topk:
            break
    return keywords if keywords else [text[:8]]
