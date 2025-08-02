package com.overthinker.cloud.entity;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * 接口用于浅拷贝属性，提供了将属性从源对象复制到目标对象的实用方法。
 * (版本 2.0 - 经过优化)
 */
public interface BasecopyProperties {

    Logger LOG = LoggerFactory.getLogger(BasecopyProperties.class);
    ConcurrentHashMap<Class<?>, Field[]> FIELD_CACHE = new ConcurrentHashMap<>();

    /**
     * 将源对象中的属性值浅拷贝到当前对象中。
     * 它会遍历当前对象的所有非静态字段（包括父类的），并尝试从源对象中查找同名字段进行赋值。
     *
     * @param sourceObj 源对象。
     * @return 当前对象 (this)。
     */
    default <T> T copyFrom(Object sourceObj) {
        if (sourceObj == null) {
            return (T) this;
        }
        // 总是遍历目标对象的字段，确保填充完整
        Field[] targetFields = getFields(this.getClass());
        for (Field targetField : targetFields) {
            copyField(targetField, this, sourceObj);
        }
        return (T) this;
    }

    /**
     * 创建指定类的新实例，并将当前对象的属性值浅拷贝到新实例中。
     *
     * @param clazz 目标VO的Class对象。
     * @param <V>   目标VO的类型。
     * @return 一个填充了属性的新VO实例。
     */
    default <V> V copyProperties(Class<V> clazz) {
        try {
            V targetObj = clazz.getConstructor().newInstance();
            Field[] targetFields = getFields(clazz);
            for (Field targetField : targetFields) {
                copyField(targetField, targetObj, this);
            }
            return targetObj;
        } catch (ReflectiveOperationException exception) {
            LOG.error("在创建实例或复制属性时出错", exception);
            throw new RuntimeException("属性复制失败: " + exception.getMessage(), exception);
        }
    }

    /**
     * copyProperties 的重载版本，允许在复制后对目标对象进行额外的处理。
     *
     * @param clazz    目标VO的Class对象。
     * @param consumer 用于处理已填充属性的VO实例的Lambda表达式。
     * @param <V>      目标VO的类型。
     * @return 经过额外处理的新VO实例。
     */
    default <V> V copyProperties(Class<V> clazz, Consumer<V> consumer) {
        V v = this.copyProperties(clazz);
        consumer.accept(v);
        return v;
    }

    // --- 内部辅助方法 ---

    /**
     * 内部核心方法，负责将单个字段的值从源对象复制到目标对象。
     *
     * @param targetField 目标对象的字段。
     * @param target      目标对象实例。
     * @param source      源对象实例。
     */
    private void copyField(Field targetField, Object target, Object source) {
        try {
            // 在源对象中查找同名字段（注意：这里依然用 getDeclaredField，因为 getFields 已经处理了父类）
            Field sourceField = source.getClass().getDeclaredField(targetField.getName());

            targetField.setAccessible(true);
            sourceField.setAccessible(true);

            targetField.set(target, sourceField.get(source));
        } catch (NoSuchFieldException e) {
            // 源对象没有这个字段，正常跳过
            LOG.debug("跳过字段 '{}'：在源对象 {} 中无法找到。", targetField.getName(), source.getClass().getSimpleName());
        } catch (IllegalArgumentException e) {
            // 字段类型不匹配，打印警告并跳过
            LOG.warn("跳过字段 '{}'：源与目标类型不匹配或无法转换。", targetField.getName());
        } catch (Exception e) {
            // 捕获其他所有可能的反射异常
            LOG.error("复制字段 '{}' 时发生未知错误。", targetField.getName(), e);
        }
    }

    /**
     * 检索一个类的所有非静态声明字段（包括其所有父类），并使用缓存以提高效率。
     *
     * @param clazz 需要分析的类。
     * @return 该类所有非静态字段的数组。
     */
    private Field[] getFields(Class<?> clazz) {
        return FIELD_CACHE.computeIfAbsent(clazz, c -> {
            LOG.debug("缓存类 {} 及其父类的所有非静态字段", c.getName());
            List<Field> fields = new ArrayList<>();
            Class<?> currentClass = c;
            while (currentClass != null && currentClass != Object.class) {
                // 过滤掉静态字段
                Arrays.stream(currentClass.getDeclaredFields())
                        .filter(field -> !Modifier.isStatic(field.getModifiers()))
                        .forEach(fields::add);
                // 继续向上查找父类
                currentClass = currentClass.getSuperclass();
            }
            return fields.toArray(new Field[0]);
        });
    }
}