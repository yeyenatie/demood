# -*- coding: utf-8 -*-
"""
论文大纲生成：标准学术结构模板 + 关键词定制小节标题。
支持毕业论文 / 期刊论文 / 课程论文三种体裁。
返回结构：
{"sections": [
    {"title": "摘要", "children": [], "points": [...]},
    {"title": "第1章 绪论", "children": [{"title": "1.1 研究背景与意义", "points": [...]}, ...]},
    ...
]}
"""
from nlp_utils import extract_keywords


def generate_outline(title, keywords="", paper_type="毕业论文"):
    kws = extract_keywords(keywords or title, topk=3)
    kw1 = kws[0] if len(kws) > 0 else "相关技术"
    kw2 = kws[1] if len(kws) > 1 else kw1
    kw3 = kws[2] if len(kws) > 2 else kw2

    if paper_type == "期刊论文":
        sections = [
            {"title": "摘要", "children": [], "points": ["研究背景", "研究方法", "实验结果", "结论"]},
            {"title": "1 引言", "children": [
                {"title": "1.1 研究背景", "points": [f"{kw1}的研究背景与意义", "问题的提出"]},
                {"title": "1.2 相关工作", "points": ["国内外研究现状", "现有方法的不足"]},
            ]},
            {"title": "2 方法", "children": [
                {"title": "2.1 总体思路", "points": ["方法整体框架", "设计动机"]},
                {"title": f"2.2 基于{kw1}的关键技术", "points": [f"{kw1}的建模与处理", "算法流程"]},
            ]},
            {"title": "3 实验", "children": [
                {"title": "3.1 实验设置", "points": ["数据集说明", "评价指标"]},
                {"title": "3.2 结果与分析", "points": ["对比实验", "消融与鲁棒性分析"]},
            ]},
            {"title": "4 结论", "children": [], "points": ["工作总结", "未来展望"]},
            {"title": "参考文献", "children": [], "points": []},
        ]
    elif paper_type == "课程论文":
        sections = [
            {"title": "摘要", "children": [], "points": ["研究目的", "主要方法", "结论"]},
            {"title": "一、引言", "children": [
                {"title": "1.1 选题背景", "points": [f"{kw1}的背景", "选题意义"]},
                {"title": "1.2 现状概述", "points": ["国内外发展现状", "存在的问题"]},
            ]},
            {"title": "二、相关概念与技术", "children": [
                {"title": f"2.1 {kw1}概述", "points": [f"{kw1}的基本概念", "主要特点"]},
                {"title": f"2.2 {kw2}概述", "points": [f"{kw2}的基本原理", "典型应用"]},
            ]},
            {"title": "三、方案分析与设计", "children": [
                {"title": "3.1 需求分析", "points": ["功能需求", "性能需求"]},
                {"title": "3.2 方案设计", "points": ["总体思路", "关键环节设计"]},
            ]},
            {"title": "四、总结", "children": [], "points": ["工作总结", "收获与展望"]},
            {"title": "参考文献", "children": [], "points": []},
        ]
    else:  # 毕业论文
        sections = [
            {"title": "摘要", "children": [], "points": ["研究目的", "主要方法", "实验结果", "结论"]},
            {"title": "第1章 绪论", "children": [
                {"title": "1.1 研究背景与意义", "points": [f"{kw1}发展的时代背景", "研究价值与意义"]},
                {"title": "1.2 国内外研究现状", "points": ["国外研究进展", "国内研究进展", "现有不足"]},
                {"title": "1.3 研究内容与组织结构", "points": ["本文主要研究内容", "论文组织结构"]},
            ]},
            {"title": "第2章 相关技术介绍", "children": [
                {"title": f"2.1 {kw1}介绍", "points": [f"{kw1}的基本概念", f"{kw1}的技术特点"]},
                {"title": f"2.2 {kw2}介绍", "points": [f"{kw2}的基本原理", f"{kw2}的应用场景"]},
                {"title": "2.3 相关框架与工具", "points": ["开发框架", "开发工具"]},
            ]},
            {"title": "第3章 系统分析与设计", "children": [
                {"title": "3.1 需求分析", "points": ["功能性需求", "非功能性需求"]},
                {"title": "3.2 总体架构设计", "points": ["系统架构", "技术选型"]},
                {"title": "3.3 功能模块设计", "points": ["用户管理模块", "论文管理模块", "AI 写作模块"]},
                {"title": "3.4 数据库设计", "points": ["概念结构设计", "逻辑结构设计"]},
            ]},
            {"title": "第4章 系统实现与实验分析", "children": [
                {"title": "4.1 实验环境", "points": ["硬件环境", "软件环境"]},
                {"title": "4.2 数据准备与评价指标", "points": ["数据集说明", "评价指标"]},
                {"title": "4.3 实验结果与分析", "points": ["实验过程", "结果分析"]},
            ]},
            {"title": "第5章 总结与展望", "children": [
                {"title": "5.1 工作总结", "points": ["完成的主要工作", "取得的成果"]},
                {"title": "5.2 不足与展望", "points": ["存在的不足", "未来研究方向"]},
            ]},
            {"title": "参考文献", "children": [], "points": []},
        ]
    return {"sections": sections}
