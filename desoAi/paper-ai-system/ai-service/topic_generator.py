# -*- coding: utf-8 -*-
"""
题目推荐：基于 NLP 关键词提取 + 学术题目模板组合。
用户输入研究方向后，先抽取关键词，再组合出多个候选论文题目并附推荐理由。
"""
import random

from nlp_utils import extract_keywords

# 学术题目模板（{kw0}/{kw1} 为关键词占位，{domain} 领域，{task} 任务词，{scene} 场景词）
_TEMPLATES = [
    "基于{kw0}的{domain}{task}研究",
    "面向{scene}的{kw0}方法研究与应用",
    "融合{kw0}与{kw1}的{task}方法研究",
    "{domain}中{kw1}的改进与优化研究",
    "基于{kw0}的{domain}系统设计与实现",
    "{kw1}技术在{domain}中的应用研究",
]

_TASK_WORDS = ["关键问题", "关键技术", "智能处理", "自动生成", "高效方法", "综合分析"]
_SCENE_WORDS = ["实际场景", "复杂环境", "真实数据", "多模态场景", "大规模应用"]


def _build_reason(kw0, kw1, paper_type):
    """生成推荐理由。"""
    parts = [f"围绕您输入的关键词「{kw0}」"]
    if kw1 and kw1 != kw0:
        parts.append(f"并结合「{kw1}」")
    parts.append(f"符合{paper_type}选题要求，兼具研究价值与工程可实现性")
    return "，".join(parts)


def generate_topics(research_direction, paper_type="毕业论文", count=5):
    """
    根据研究方向生成 count 个候选题目。
    返回: [{"title": str, "reason": str}, ...]
    """
    kws = extract_keywords(research_direction, topk=4)
    # 关键词池：不足时用通用学术词补足，保证模板可组合
    kw_pool = kws + ["智能方法", "关键技术", "数据处理", "性能优化", "应用实践"]
    domain = kws[0] if kws else "相关领域"

    topics = []
    seen = set()
    tries = 0
    while len(topics) < count and tries < 80:
        tries += 1
        kw0 = random.choice(kw_pool)
        kw1 = random.choice(kw_pool)
        template = random.choice(_TEMPLATES)
        title = template.format(
            kw0=kw0,
            kw1=kw1,
            domain=domain,
            task=random.choice(_TASK_WORDS),
            scene=random.choice(_SCENE_WORDS),
        )
        if title in seen:
            continue
        seen.add(title)
        topics.append({"title": title, "reason": _build_reason(kw0, kw1, paper_type)})
    return topics
