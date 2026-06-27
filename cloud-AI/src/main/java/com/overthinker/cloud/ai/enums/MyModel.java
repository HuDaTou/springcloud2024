package com.overthinker.cloud.ai.enums;

import lombok.Getter;
import org.springframework.ai.model.ChatModelDescription;
import org.springframework.lang.NonNull;

/**
 * AI 模型枚举
 * <p>
 * 定义 Ollama 支持的所有模型
 * </p>
 *
 * @author overthinker
 * @since 2024-01-15
 */
@Getter
public enum MyModel implements ChatModelDescription {

    /**
     * Qwen 2.5 7B 模型
     */
    QWEN_2_5_7B("qwen2.5"),

    /**
     * QWQ 模型
     */
    QWQ("qwq"),

    /**
     * Llama 2 模型
     */
    LLAMA2("llama2"),

    /**
     * Llama 3 模型
     */
    LLAMA3("llama3"),

    /**
     * Llama 3.1 模型
     */
    LLAMA3_1("llama3.1"),

    /**
     * Llama 3.2 模型
     */
    LLAMA3_2("llama3.2"),

    /**
     * Llama 3.2 Vision 11B 模型
     */
    LLAMA3_2_VISION_11b("llama3.2-vision"),

    /**
     * Llama 3.2 Vision 90B 模型
     */
    LLAMA3_2_VISION_90b("llama3.2-vision:90b"),

    /**
     * Llama 3.2 1B 模型
     */
    LLAMA3_2_1B("llama3.2:1b"),

    /**
     * Llama 3.2 3B 模型
     */
    LLAMA3_2_3B("llama3.2:3b"),

    /**
     * Mistral 模型
     */
    MISTRAL("mistral"),

    /**
     * Mistral Nemo 模型
     */
    MISTRAL_NEMO("mistral-nemo"),

    /**
     * Moondream 模型
     */
    MOONDREAM("moondream"),

    /**
     * Dolphin Phi 模型
     */
    DOLPHIN_PHI("dolphin-phi"),

    /**
     * Phi 模型
     */
    PHI("phi"),

    /**
     * Phi 3 模型
     */
    PHI3("phi3"),

    /**
     * Neural Chat 模型
     */
    NEURAL_CHAT("neural-chat"),

    /**
     * Starling LM 模型
     */
    STARLING_LM("starling-lm"),

    /**
     * CodeLlama 模型
     */
    CODELLAMA("codellama"),

    /**
     * Orca Mini 模型
     */
    ORCA_MINI("orca-mini"),

    /**
     * LLaVA 多模态模型
     */
    LLAVA("llava"),

    /**
     * Gemma 模型
     */
    GEMMA("gemma"),

    /**
     * Gemma 3 4B 模型
     */
    GEMMA3("gemma3:4b"),

    /**
     * Llama 2 Uncensored 模型
     */
    LLAMA2_UNCENSORED("llama2-uncensored"),

    /**
     * Nomic Embed Text 嵌入模型
     */
    NOMIC_EMBED_TEXT("nomic-embed-text"),

    /**
     * MXBAI Embed Large 嵌入模型
     */
    MXBAI_EMBED_LARGE("mxbai-embed-large");

    /**
     * 模型ID
     */
    private final String id;

    MyModel(String id) {
        this.id = id;
    }

    @Override
    @NonNull
    public String getName() {
        return this.id;
    }
}
