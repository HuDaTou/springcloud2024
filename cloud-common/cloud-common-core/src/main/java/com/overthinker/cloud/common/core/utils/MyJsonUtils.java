package com.overthinker.cloud.common.core.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.TypeReference;

import java.util.List;
import java.util.Map;

/**
 * JSON 工具类
 * <p>
 * 提供基于 Fastjson2 的 JSON 序列化/反序列化操作方法
 * </p>
 *
 * @author overthinker
 * @since 2024-01-15
 */
public class MyJsonUtils {

    /**
     * 私有构造器，禁止实例化
     */
    private MyJsonUtils() {
    }

    /**
     * 将对象序列化为 JSON 字符串
     *
     * @param object 要序列化的对象
     * @return JSON 字符串，如果对象为 null 则返回 null
     */
    public static <T> String toJson(T object) {
        if (object == null) {
            return null;
        }
        return JSON.toJSONString(object);
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的对象
     *
     * @param json  JSON 字符串
     * @param clazz 目标类型
     * @param <T>   目标类型
     * @return 反序列化后的对象，json 为空或 null 时返回 null
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return JSON.parseObject(json, clazz);
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的列表
     *
     * @param json  JSON 字符串
     * @param clazz 列表元素类型
     * @param <T>   列表元素类型
     * @return 反序列化后的列表，json 为空或 null 时返回 null
     */
    public static <T> List<T> fromJsonArray(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return JSON.parseArray(json, clazz);
    }

    /**
     * 将 JSON 字符串解析为 Map
     *
     * @param json JSON 字符串
     * @return 解析后的 Map，json 为空或 null 时返回 null
     */
    public static Map<String, Object> parseMap(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return JSON.parseObject(json, new TypeReference<Map<String, Object>>() {});
    }

    /**
     * 将 JSON 字符串解析为指定值类型的 Map
     *
     * @param json       JSON 字符串
     * @param valueClass Map 值类型
     * @param <T>        Map 值类型
     * @return 解析后的 Map，json 为空或 null 时返回 null
     */
    public static <T> Map<String, T> parseMap(String json, Class<T> valueClass) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return JSON.parseObject(json, new TypeReference<Map<String, T>>() {});
    }

    /**
     * 将一个对象从一种类型转换为另一种类型
     *
     * @param fromValue   来源对象
     * @param toValueType 目标类型
     * @param <T>         目标类型
     * @return 转换后的对象，fromValue 或 toValueType 为 null 时返回 null
     * @throws IllegalArgumentException 当 toValueType 为 null 时抛出
     */
    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        if (fromValue == null) {
            return null;
        }
        if (toValueType == null) {
            throw new IllegalArgumentException("目标类型不能为 null");
        }
        String json = toJson(fromValue);
        return fromJson(json, toValueType);
    }

    /**
     * 将对象序列化为 JSON 字节数组
     *
     * @param object 要序列化的对象
     * @return JSON 字节数组，如果对象为 null 则返回 null
     */
    public static byte[] toJsonBytes(Object object) {
        if (object == null) {
            return null;
        }
        return JSON.toJSONBytes(object);
    }

    /**
     * 验证字符串是否是有效的 JSON
     *
     * @param str 要验证的字符串
     * @return 如果是有效的 JSON 返回 true，字符串为空或 null 返回 false
     */
    public static boolean isValidJson(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            JSON.parse(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将对象序列化为格式化的 JSON 字符串（美化输出）
     *
     * @param object 要序列化的对象
     * @return 美化后的 JSON 字符串，如果对象为 null 则返回 null
     */
    public static String toJsonPretty(Object object) {
        if (object == null) {
            return null;
        }
        return JSON.toJSONString(object, JSONWriter.Feature.PrettyFormat);
    }
}