package com.overthinker.cloud.ai.service;

/**
 * AI 服务接口
 * <p>
 * 提供 AI 模型切换和对话功能
 * </p>
 *
 * @author overthinker
 * @since 2024-01-15
 */
public interface AiService {

    /**
     * 切换 AI 模型
     *
     * @param modelId 模型ID
     * @return 切换结果信息
     */
    String changeModel(String modelId);

    /**
     * 发送消息给 AI 进行对话
     *
     * @param prompt 用户输入的提示词
     * @return AI 的响应内容
     */
    String chat(String prompt);
}
