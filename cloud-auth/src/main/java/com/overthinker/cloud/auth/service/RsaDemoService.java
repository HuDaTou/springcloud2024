package com.overthinker.cloud.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA 非对称加密服务示例
 * 
 * 演示：
 * 1. 公钥加密，私钥解密
 * 2. 私钥签名，公钥验证
 */
@Slf4j
@Service
public class RsaDemoService {

    /**
     * 生成 RSA 密钥对
     * 
     * @return 密钥对（包含公钥和私钥）
     */
    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048); // 2048位密钥
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            log.info("RSA 密钥对生成成功");
            return keyPair;
        } catch (NoSuchAlgorithmException e) {
            log.error("生成 RSA 密钥对失败", e);
            throw new RuntimeException("生成 RSA 密钥对失败", e);
        }
    }

    /**
     * 获取公钥的 Base64 字符串
     * 
     * @param publicKey 公钥
     * @return Base64 编码的公钥字符串
     */
    public String getPublicKeyBase64(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    /**
     * 获取私钥的 Base64 字符串
     * 
     * @param privateKey 私钥
     * @return Base64 编码的私钥字符串
     */
    public String getPrivateKeyBase64(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    /**
     * 从 Base64 字符串恢复公钥
     * 
     * @param publicKeyBase64 Base64 编码的公钥字符串
     * @return 公钥对象
     */
    public PublicKey restorePublicKey(String publicKeyBase64) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(spec);
        } catch (Exception e) {
            log.error("恢复公钥失败", e);
            throw new RuntimeException("恢复公钥失败", e);
        }
    }

    /**
     * 从 Base64 字符串恢复私钥
     * 
     * @param privateKeyBase64 Base64 编码的私钥字符串
     * @return 私钥对象
     */
    public PrivateKey restorePrivateKey(String privateKeyBase64) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(privateKeyBase64);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(spec);
        } catch (Exception e) {
            log.error("恢复私钥失败", e);
            throw new RuntimeException("恢复私钥失败", e);
        }
    }

    // ==================== 加密解密 ====================

    /**
     * 使用公钥加密数据
     * 
     * @param data       明文数据
     * @param publicKey  公钥
     * @return Base64 编码的密文
     */
    public String encryptWithPublicKey(String data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            
            // RSA 单次加密有限制（约 245 字节），如果数据过长需要分段加密
            if (dataBytes.length > 245) {
                return encryptLargeData(data, publicKey);
            }
            
            byte[] encryptedBytes = cipher.doFinal(dataBytes);
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            log.error("公钥加密失败", e);
            throw new RuntimeException("公钥加密失败", e);
        }
    }

    /**
     * 使用私钥解密数据
     * 
     * @param encryptedData Base64 编码的密文
     * @param privateKey    私钥
     * @return 明文数据
     */
    public String decryptWithPrivateKey(String encryptedData, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
            
            // 检查是否为分段加密的数据
            if (encryptedBytes.length > 256) {
                return decryptLargeData(encryptedData, privateKey);
            }
            
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("私钥解密失败", e);
            throw new RuntimeException("私钥解密失败", e);
        }
    }

    /**
     * 分段加密（处理大于 245 字节的数据）
     */
    private String encryptLargeData(String data, PublicKey publicKey) throws Exception {
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        
        int blockSize = 245; // RSA 2048 位的最大加密块大小
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < dataBytes.length; i += blockSize) {
            int end = Math.min(i + blockSize, dataBytes.length);
            byte[] block = new byte[end - i];
            System.arraycopy(dataBytes, i, block, 0, block.length);
            
            byte[] encryptedBlock = cipher.doFinal(block);
            result.append(Base64.getEncoder().encodeToString(encryptedBlock));
            result.append("|"); // 用 | 分隔分段
        }
        
        return result.toString();
    }

    /**
     * 分段解密
     */
    private String decryptLargeData(String encryptedData, PrivateKey privateKey) throws Exception {
        String[] blocks = encryptedData.split("\\|");
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        
        StringBuilder result = new StringBuilder();
        
        for (String block : blocks) {
            byte[] encryptedBlock = Base64.getDecoder().decode(block);
            byte[] decryptedBlock = cipher.doFinal(encryptedBlock);
            result.append(new String(decryptedBlock, StandardCharsets.UTF_8));
        }
        
        return result.toString();
    }

    // ==================== 签名验证 ====================

    /**
     * 使用私钥对数据签名
     * 
     * @param data       要签名的数据
     * @param privateKey 私钥
     * @return Base64 编码的签名
     */
    public String signWithPrivateKey(String data, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            
            byte[] signatureBytes = signature.sign();
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (Exception e) {
            log.error("私钥签名失败", e);
            throw new RuntimeException("私钥签名失败", e);
        }
    }

    /**
     * 使用公钥验证签名
     * 
     * @param data      原始数据
     * @param signature Base64 编码的签名
     * @param publicKey 公钥
     * @return 验证结果（true = 签名有效，false = 签名无效）
     */
    public boolean verifyWithPublicKey(String data, String signature, PublicKey publicKey) {
        try {
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            sig.update(data.getBytes(StandardCharsets.UTF_8));
            
            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            return sig.verify(signatureBytes);
        } catch (Exception e) {
            log.error("公钥验证签名失败", e);
            return false;
        }
    }

    // ==================== 完整示例 ====================

    /**
     * 运行完整示例：展示 RSA 加密解密 + 签名验证
     */
    public void runFullDemo() {
        log.info("===== RSA 完整示例开始 =====");
        
        // 1. 生成密钥对
        KeyPair keyPair = generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        
        log.info("公钥:\n{}", getPublicKeyBase64(publicKey));
        log.info("私钥:\n{}", getPrivateKeyBase64(privateKey));
        
        // 2. 测试数据
        String originalData = "Hello, RSA! 这是一个测试消息。";
        log.info("\n原始数据: {}", originalData);
        
        // 3. 公钥加密
        String encryptedData = encryptWithPublicKey(originalData, publicKey);
        log.info("公钥加密后: {}", encryptedData);
        
        // 4. 私钥解密
        String decryptedData = decryptWithPrivateKey(encryptedData, privateKey);
        log.info("私钥解密后: {}", decryptedData);
        
        // 5. 验证解密结果
        if (originalData.equals(decryptedData)) {
            log.info("✅ 加密解密测试通过！");
        } else {
            log.error("❌ 加密解密测试失败！");
        }
        
        // 6. 私钥签名
        String signature = signWithPrivateKey(originalData, privateKey);
        log.info("\n私钥签名: {}", signature);
        
        // 7. 公钥验证签名
        boolean isValid = verifyWithPublicKey(originalData, signature, publicKey);
        if (isValid) {
            log.info("✅ 签名验证通过！");
        } else {
            log.error("❌ 签名验证失败！");
        }
        
        // 8. 测试篡改数据的情况
        String modifiedData = originalData + " (被篡改)";
        isValid = verifyWithPublicKey(modifiedData, signature, publicKey);
        if (!isValid) {
            log.info("✅ 篡改检测成功！数据被修改后签名验证失败。");
        }
        
        log.info("===== RSA 完整示例结束 =====");
    }

    /**
     * 演示分离场景：发送方用公钥加密，接收方用私钥解密
     */
    public void demoEncryptDecryptScenario() {
        log.info("\n===== 加密解密场景演示 =====");
        
        // 场景：A 生成密钥对，把公钥发给 B，B 用公钥加密数据，A 用私钥解密
        
        // A 生成密钥对
        KeyPair keyPair = generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        
        // A 把公钥发给 B（通过不安全通道）
        String publicKeyBase64 = getPublicKeyBase64(publicKey);
        log.info("A 发送公钥给 B:\n{}", publicKeyBase64);
        
        // B 收到公钥，恢复公钥对象
        PublicKey bPublicKey = restorePublicKey(publicKeyBase64);
        
        // B 用公钥加密敏感数据
        String secretData = "这是秘密数据，只有 A 能解密！";
        String encrypted = encryptWithPublicKey(secretData, bPublicKey);
        log.info("B 用公钥加密数据后发送给 A:\n{}", encrypted);
        
        // A 收到密文，用私钥解密
        String decrypted = decryptWithPrivateKey(encrypted, privateKey);
        log.info("A 用私钥解密得到:\n{}", decrypted);
        
        log.info("===== 加密解密场景演示结束 =====");
    }

    /**
     * 演示分离场景：发送方用私钥签名，接收方用公钥验证
     */
    public void demoSignVerifyScenario() {
        log.info("\n===== 签名验证场景演示 =====");
        
        // 场景：A 用私钥签名数据，B 用公钥验证签名
        
        // A 生成密钥对
        KeyPair keyPair = generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        
        // A 把公钥发给 B（公开）
        String publicKeyBase64 = getPublicKeyBase64(publicKey);
        log.info("A 发布公钥:\n{}", publicKeyBase64);
        
        // A 发送消息并签名
        String message = "这是 A 发送的消息，签名保证真实性！";
        String signature = signWithPrivateKey(message, privateKey);
        log.info("A 发送消息: {}", message);
        log.info("A 的签名: {}", signature);
        
        // B 收到消息和签名，验证
        PublicKey bPublicKey = restorePublicKey(publicKeyBase64);
        boolean isValid = verifyWithPublicKey(message, signature, bPublicKey);
        
        if (isValid) {
            log.info("B 验证签名: ✅ 有效！确认消息来自 A");
        } else {
            log.info("B 验证签名: ❌ 无效！消息可能被篡改或不是 A 发送的");
        }
        
        // 测试消息被篡改
        String tamperedMessage = message + " (被修改)";
        isValid = verifyWithPublicKey(tamperedMessage, signature, bPublicKey);
        if (!isValid) {
            log.info("B 检测到消息被篡改: ✅ 签名验证失败");
        }
        
        log.info("===== 签名验证场景演示结束 =====");
    }
}
