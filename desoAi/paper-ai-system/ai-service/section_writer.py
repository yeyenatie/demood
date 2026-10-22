# -*- coding: utf-8 -*-
"""
章节内容生成：大模型（LLM）优先，本地模板 + NLP 关键词兜底。

本地模板模式原理：根据章节名关键词路由到对应学术写作模板，
将 NLP 抽取的关键词与大纲要点填入模板句库，生成结构完整、可直接编辑的论文草稿，
保证未配置大模型 API Key 时系统仍可完整演示。
"""
import random

from nlp_utils import extract_keywords
from llm_client import chat, llm_enabled


# ============================================================
# 对外主入口
# ============================================================
def write_section(section_name, title, keywords="", points="", context="",
                  llm_api_key="", llm_base_url="", llm_model=""):
    """生成某一章节的正文内容。llm_* 为用户自设的大模型配置（可选，缺省用全局配置）。"""
    kws = extract_keywords(f"{keywords} {title}", topk=6) or ["相关技术"]
    kw1, kw2 = kws[0], (kws[1] if len(kws) > 1 else kws[0])

    # 1) 优先调用大模型（请求级密钥或全局密钥任一存在时）
    if llm_enabled(llm_api_key):
        prompt = _build_llm_prompt(section_name, title, kws, points, context)
        text = chat(prompt, api_key=llm_api_key, base_url=llm_base_url, model=llm_model)
        if text:
            return text

    # 2) 本地模板兜底
    name = (section_name or "").lower()
    if "摘要" in name:
        return _write_summary(title, kw1, kw2)
    if "绪论" in name or "引言" in name or "背景" in name:
        return _write_introduction(title, kw1, kw2, points)
    if "技术" in name or "介绍" in name or "相关" in name or "概念" in name:
        return _write_tech(title, kw1, kw2, points)
    if "设计" in name or "分析" in name or "架构" in name or "方法" in name or "实现" in name:
        return _write_design(title, kw1, kw2, points)
    if "实验" in name or "结果" in name or "测试" in name:
        return _write_experiment(title, kw1, kw2, points)
    if "总结" in name or "结论" in name or "展望" in name:
        return _write_conclusion(title, kw1, kw2, points)
    if "参考" in name:
        return _write_references()
    return _write_generic(title, kw1, kw2, points)


# ============================================================
# 大模型 Prompt 构造
# ============================================================
def _build_llm_prompt(section_name, title, kws, points, context):
    lines = [
        f"请用中文撰写论文《{title}》中「{section_name}」这一章节的正文内容。",
        f"论文关键词：{'、'.join(kws)}。",
        "写作要求：结构清晰、逻辑连贯、语言规范，符合学术论文风格；篇幅约 400~800 字；"
        "直接输出正文段落，不要重复章节标题，不要输出任何解释或说明。",
    ]
    if points:
        lines.append(f"本节应覆盖的要点：{points}")
    if context:
        lines.append(f"前文内容参考（用于保持行文连贯）：{context[:500]}")
    return "\n".join(lines)


# ============================================================
# 本地模板：各章节写作函数
# ============================================================
def _write_summary(title, kw1, kw2):
    return "\n\n".join([
        f"随着{kw1}相关技术的快速发展，{title}已成为当前学术界与工业界共同关注的热点问题。"
        f"围绕这一问题，本文开展了系统性的研究工作，并取得了阶段性成果。",
        f"本文首先梳理了{kw1}的研究背景与国内外发展现状，分析了现有方法在{kw2}方面存在的不足；"
        f"在此基础上，提出了基于{kw1}的改进方案，完成了系统的整体设计、实现与测试。",
        f"实验结果表明，本文所提出的方法在有效性、稳定性与运行效率等指标上均取得了良好表现，"
        f"验证了方案的科学性与可行性，可为{kw2}相关领域的进一步研究与应用提供参考。",
    ])


def _write_introduction(title, kw1, kw2, points):
    paras = [
        f"随着信息技术与{kw1}的不断发展，{title}具有重要的理论研究价值与广阔的应用前景。"
        f"近年来，{kw2}相关研究受到越来越多的关注，各行业对{kw1}的需求日益增长，"
        f"推动了该方向的快速发展。",
        f"在此背景下，如何有效利用{kw1}解决实际问题、提升系统性能，成为研究者需要面对的关键挑战。"
        f"本文研究的目标正是针对上述挑战，提出一种切实可行、兼顾效果与效率的解决方案。",
    ]
    if points:
        paras.append(f"本节要点包括：{points}。以上问题将在后续章节中逐一展开分析。")
    paras.append(
        f"本文的组织结构如下：首先介绍研究背景与意义，梳理国内外研究现状；"
        f"随后介绍相关技术；然后给出系统的整体分析与设计；"
        f"接着介绍实现过程与实验分析；最后对全文工作进行总结与展望。"
    )
    return "\n\n".join(paras)


def _write_tech(title, kw1, kw2, points):
    paras = [
        f"{kw1}是{title}所涉及的核心技术之一。{kw1}通过对关键特征进行建模与分析，"
        f"能够为系统提供稳定、高效的处理能力，其发展经历了从传统方法到智能化方法的演进过程。",
        f"在具体应用中，{kw1}主要解决以下几类问题：一是数据的高效表示与特征提取；"
        f"二是模型的可训练性与泛化能力；三是复杂场景下的鲁棒性。"
        f"针对上述问题，现有研究提出了多种改进策略，并取得了较为显著的效果。",
    ]
    if points:
        paras.append(f"本节要点包括：{points}。")
    paras.append(
        f"此外，{kw2}作为重要的支撑技术，在系统实现中同样发挥着关键作用。"
        f"本文在方案设计中综合运用了{kw1}与{kw2}，兼顾了算法效果与实现复杂度，"
        f"为后续系统开发奠定了坚实的技术基础。"
    )
    return "\n\n".join(paras)


def _write_design(title, kw1, kw2, points):
    paras = [
        f"本章围绕{title}展开系统层面的分析与设计。结合研究目标与业务需求，"
        f"系统采用 B/S 架构，整体分为三层：前端展示层、后端业务层与 AI 服务层。"
        f"前端基于 Vue 实现，负责交互与展示；后端基于 Spring Boot 提供业务接口与数据管理；"
        f"AI 服务层基于 Python 提供论文写作相关的智能生成能力。",
        f"在功能模块设计上，系统主要包括用户管理模块、论文管理模块与 AI 写作模块："
        f"用户管理模块负责注册、登录与身份校验；论文管理模块负责论文的创建、保存与历史记录；"
        f"AI 写作模块负责题目推荐、大纲生成与章节内容生成。",
    ]
    if points:
        paras.append(f"本节要点包括：{points}。")
    paras.append(
        f"在数据设计上，系统核心数据表包括用户表和论文表：用户表保存账号信息，"
        f"论文表保存题目、关键词、大纲与正文内容，通过用户标识建立关联，"
        f"保证每位用户只能访问自己的论文数据。"
    )
    return "\n\n".join(paras)


def _write_experiment(title, kw1, kw2, points):
    paras = [
        f"为验证{title}所提方案的有效性，本章设计了系统的实验方案并给出结果分析。"
        f"实验环境方面，系统运行于 Windows 平台，后端采用 JDK 8 与 Spring Boot，"
        f"AI 服务采用 Python 3.8 及以上版本，数据库采用 MySQL 8，前端采用 Vue3。",
        f"实验数据来源于系统实际运行过程中收集的测试用例，评价指标主要关注功能完整性、"
        f"生成内容的可用性以及系统响应时间等维度。",
    ]
    if points:
        paras.append(f"本节要点包括：{points}。")
    paras.append(
        f"实验结果表明，系统各功能模块运行稳定，AI 写作模块能够根据研究方向与选题"
        f"快速生成结构完整、内容连贯的论文草稿，整体响应时间满足使用要求，"
        f"达到了预期的设计目标。"
    )
    return "\n\n".join(paras)


def _write_conclusion(title, kw1, kw2, points):
    paras = [
        f"本文围绕{title}开展了研究、设计与实现工作，完成了从需求分析、总体设计、"
        f"功能实现到实验验证的完整流程，达到了预期的研究目标。",
        f"本文的主要工作包括：一是调研了{kw1}的国内外研究现状，明确了研究问题；"
        f"二是基于{kw1}与{kw2}设计了系统的整体方案；三是完成了系统的编码实现与实验测试，"
        f"验证了方案的可行性与有效性。",
    ]
    if points:
        paras.append(f"本节要点包括：{points}。")
    paras.append(
        f"受时间与实验条件限制，本文仍存在一些不足：例如数据集规模有限、"
        f"部分模块功能有待进一步完善等。未来可以从引入更丰富的{kw2}方法、"
        f"扩大实验规模、优化生成质量等方向继续深入研究。"
    )
    return "\n\n".join(paras)


def _write_references():
    return (
        "（本处为参考文献格式示例，请根据论文实际引用的文献进行替换、增删与排序）\n\n"
        "[1] 作者1, 作者2. 文献标题[J]. 期刊名称, 2022, 48(5): 120-130.\n"
        "[2] 作者. 著作名称[M]. 北京: 出版社, 2021.\n"
        "[3] 作者. 学位论文标题[D]. 某大学, 2023.\n"
        "[4] 作者. 会议论文标题[C]// 国际会议论文集, 2020: 1-8."
    )


def _write_generic(title, kw1, kw2, points):
    paras = [
        f"本章围绕{title}中的相关环节展开论述。结合前文的研究基础，"
        f"本节主要从{kw1}、{kw2}等角度对问题进行深入分析与阐述。",
    ]
    if points:
        paras.append(f"本节要点包括：{points}。")
    paras.append(
        f"综上，通过对{kw1}与{kw2}的有机结合，本部分内容为系统的整体实现"
        f"提供了必要的支撑，也为后续章节的实验与总结奠定了基础。"
    )
    return "\n\n".join(paras)
