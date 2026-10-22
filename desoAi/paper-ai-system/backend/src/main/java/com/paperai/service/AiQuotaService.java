package com.paperai.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.paperai.common.BizException;
import com.paperai.entity.AiUsage;
import com.paperai.mapper.AiUsageMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.temporal.WeekFields;

/**
 * AI 生成次数配额服务：每周每个用户默认限 10 次，跨周自动重置
 */
@Service
public class AiQuotaService {

    @Value("${paper-ai.ai-gen-limit:10}")
    private int limit;

    @Resource
    private AiUsageMapper aiUsageMapper;

    public int getLimit() {
        return limit;
    }

    /**
     * 本周已用次数
     */
    public int getUsed(Long userId) {
        AiUsage usage = find(userId);
        return usage == null ? 0 : usage.getCount();
    }

    /**
     * 本周剩余次数
     */
    public int getRemaining(Long userId) {
        return Math.max(0, limit - getUsed(userId));
    }

    /**
     * 校验配额，不足则抛出业务异常
     */
    public void checkQuota(Long userId) {
        if (getRemaining(userId) <= 0) {
            throw new BizException(400, "本周默认模型生成次数已用尽（上限 " + limit + " 次），可配置自己的 API Key 或下周再试");
        }
    }

    /**
     * 消耗一次额度（生成成功后调用）
     */
    public void consume(Long userId) {
        String weekKey = weekKey();
        AiUsage usage = aiUsageMapper.selectOne(new LambdaQueryWrapper<AiUsage>()
                .eq(AiUsage::getUserId, userId)
                .eq(AiUsage::getWeekKey, weekKey));
        if (usage == null) {
            usage = new AiUsage();
            usage.setUserId(userId);
            usage.setWeekKey(weekKey);
            usage.setCount(1);
            aiUsageMapper.insert(usage);
        } else {
            usage.setCount(usage.getCount() + 1);
            aiUsageMapper.updateById(usage);
        }
    }

    private AiUsage find(Long userId) {
        return aiUsageMapper.selectOne(new LambdaQueryWrapper<AiUsage>()
                .eq(AiUsage::getUserId, userId)
                .eq(AiUsage::getWeekKey, weekKey()));
    }

    /**
     * 当前周标识，如 2026-W42（JDK8 兼容写法）
     */
    private String weekKey() {
        LocalDate today = LocalDate.now();
        WeekFields weekFields = WeekFields.ISO;
        int year = today.get(weekFields.weekBasedYear());
        int week = today.get(weekFields.weekOfWeekBasedYear());
        return year + "-W" + week;
    }
}
