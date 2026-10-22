package com.paperai.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.paperai.common.BizException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI 服务：转发请求到 Python AI 服务（FastAPI），完成题目推荐 / 大纲生成 / 章节生成
 * 支持携带用户自设的大模型配置（llm_api_key / llm_base_url / llm_model）
 */
@Service
@SuppressWarnings("unchecked")
public class AiService {

    @Value("${paper-ai.ai-service-url}")
    private String aiServiceUrl;

    /**
     * 题目推荐
     */
    public List<Map<String, Object>> generateTopics(String researchDirection, String paperType,
                                                    String apiKey, String baseUrl, String model) {
        Map<String, Object> body = new HashMap<>();
        body.put("research_direction", researchDirection);
        body.put("paper_type", paperType == null ? "毕业论文" : paperType);
        fillLlmConfig(body, apiKey, baseUrl, model);
        Map<String, Object> resp = post("/api/ai/topics", body);
        Object topics = resp.get("topics");
        return topics instanceof List ? (List<Map<String, Object>>) topics : new ArrayList<>();
    }

    /**
     * 大纲生成
     */
    public Map<String, Object> generateOutline(String title, String keywords, String paperType,
                                               String apiKey, String baseUrl, String model) {
        Map<String, Object> body = new HashMap<>();
        body.put("title", title);
        body.put("keywords", keywords);
        body.put("paper_type", paperType == null ? "毕业论文" : paperType);
        fillLlmConfig(body, apiKey, baseUrl, model);
        Map<String, Object> resp = post("/api/ai/outline", body);
        Object outline = resp.get("outline");
        return outline instanceof Map ? (Map<String, Object>) outline : new HashMap<>();
    }

    /**
     * 章节内容生成
     */
    public String generateSection(String title, String sectionName, String keywords,
                                  String points, String context,
                                  String apiKey, String baseUrl, String model) {
        Map<String, Object> body = new HashMap<>();
        body.put("title", title);
        body.put("section_name", sectionName);
        body.put("keywords", keywords);
        body.put("points", points);
        body.put("context", context);
        fillLlmConfig(body, apiKey, baseUrl, model);
        Map<String, Object> resp = post("/api/ai/section", body);
        Object content = resp.get("content");
        return content == null ? "" : content.toString();
    }

    /**
     * 测试用户自设的大模型配置是否可用
     */
    public Map<String, Object> testConnection(String apiKey, String baseUrl, String model) {
        Map<String, Object> body = new HashMap<>();
        fillLlmConfig(body, apiKey, baseUrl, model);
        Map<String, Object> resp = post("/api/ai/test", body);
        Object ok = resp.get("ok");
        Object message = resp.get("message");
        Map<String, Object> result = new HashMap<>();
        result.put("ok", Boolean.TRUE.equals(ok));
        result.put("message", message == null ? "" : message.toString());
        return result;
    }

    private void fillLlmConfig(Map<String, Object> body, String apiKey, String baseUrl, String model) {
        body.put("llm_api_key", apiKey == null ? "" : apiKey);
        body.put("llm_base_url", baseUrl == null ? "" : baseUrl);
        body.put("llm_model", model == null ? "" : model);
    }

    /**
     * 通用 POST 请求封装
     */
    private Map<String, Object> post(String path, Map<String, Object> body) {
        try {
            String url = aiServiceUrl + path;
            HttpResponse response = HttpRequest.post(url)
                    .timeout(180_000)
                    .header("Content-Type", "application/json")
                    .body(JSONUtil.toJsonStr(body))
                    .execute();
            if (!response.isOk()) {
                throw new BizException(502, "AI 服务返回异常（HTTP " + response.getStatus() + "）");
            }
            Map<String, Object> result = JSONUtil.toBean(response.body(), Map.class);
            if (result == null) {
                throw new BizException(502, "AI 服务返回数据为空");
            }
            return result;
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new BizException(502, "AI 服务连接失败，请先启动 Python AI 服务（ai-service，端口 8000）");
        }
    }
}
