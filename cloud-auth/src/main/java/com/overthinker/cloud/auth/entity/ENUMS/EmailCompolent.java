package com.overthinker.cloud.auth.entity.ENUMS;

import com.overthinker.cloud.auth.config.mq.RabbitEmailConfig;
import com.overthinker.cloud.auth.entity.DTO.MailDTO;
import com.overthinker.cloud.auth.entity.DTO.UserRegisterDTO;
import com.overthinker.cloud.auth.utils.SpringContextUtils;
import com.overthinker.cloud.common.core.constants.NumberConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * 邮件模板配置
 */
@Getter
@AllArgsConstructor
public enum EmailCompolent {
    /**
     * 注册验证码邮件
     * 对应 type: "register"
     */
    REGISTER_CODE("register","register-email-template") {
        @Override
        public MailDTO send(String  userEmail,String code) {
            Map<String, Object> vars = new HashMap<>();
            vars.put("code", code);
            vars.put("expirationTime", NumberConst.FIVE + "分钟");
            vars.put("toUrl", "https://overthinker.top");
            vars.put("openSourceAddress", "https://github.com/your-project");
            return new MailDTO()
                    .setVariables(vars)
                    .setSubject("注册验证码")
                    .setTemplateName(getTemplateName())
                    .setTo(userEmail);
        }
    },

    /**
     * 密码重置邮件
     * 对应 type: "reset_password"
     */
    RESET_PASSWORD("reset_password","reset-password-template") {
        @Override
        public MailDTO send(String userEmail,String code) {
            Map<String, Object> vars = new HashMap<>();

// 2. 填充业务参数
            vars.put("code", code); // 调用你封装的混合验证码方法
            vars.put("expirationTime", NumberConst.FIVE + "分钟");
            vars.put("toUrl", "https://overthinker.top/login");
            vars.put("openSourceAddress", "https://github.com/your-repo");
return  new MailDTO()
        .setTemplateName(getTemplateName())
        .setTo(userEmail)
        .setSubject("重置密码")
        .setVariables(vars);
            // 入队：使用专门的重置模板（如果没有，可以先共用 welcome_email 测试）
        }


    };

    private final String type;
    private final String templateName;

    /**
     * 抽象发送方法
     * @param userEmail 用户邮箱
     */


    public abstract MailDTO send(String userEmail,String code);


    /**
     * 根据 type 查找对应的枚举项
     */
    public static EmailCompolent getByType(String type) {
        return Arrays.stream(values())
                .filter(item -> item.getType().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未知的发送类型: " + type));
    }
}