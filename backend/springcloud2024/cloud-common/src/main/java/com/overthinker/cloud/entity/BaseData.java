package com.overthinker.cloud.entity;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * 浅拷贝工具
 */

public interface BaseData {


    /**
     * 创建指定的VO类并将当前DTO对象中的所有成员变量值直接复制到VO对象中
     *
     * @param clazz    指定VO类型
     * @param consumer 返回VO对象之前可以使用Lambda进行额外处理
     * @param <V>      指定VO类型
     * @return 指定VO对象
     */
    default <V> V asViewObject(Class<V> clazz, Consumer<V> consumer) {
        V v = this.asViewObject(clazz);
        consumer.accept(v);
        return v;
    }

    /**
     * 创建指定的VO类并将当前DTO对象中的所有成员变量值直接复制到VO对象中 也可以是po
     *
     * @param clazz 指定VO类型
     * @param <V>   指定VO类型
     * @return 指定VO对象
     */
    default <V> V asViewObject(Class<V> clazz) {
        try {
//            获取 clazz 类型的所有声明字段（包括私有字段），并将其存储在一个 Field[] 数组中。
            Field[] fields = clazz.getDeclaredFields();
//            获取 clazz 类型的默认构造方法，并使用该构造方法创建一个新实例。尝试获取 clazz 类型的无参构造函数，并将其存储在 constructor 变量中。
            Constructor<V> constructor = clazz.getConstructor();
//            使用无参构造函数创建 clazz 类型的新实例，并将其存储在变量 v 中。
            V v = constructor.newInstance();
//            遍历 fields 数组中的所有字段，并将当前对象中的同名字段的值复制到 v 对象中。
            Arrays.asList(fields).forEach(field -> convert(field, v));
            return v;
        } catch (ReflectiveOperationException exception) {
            Logger logger = LoggerFactory.getLogger(BaseData.class);
            logger.error("在转换时出现了一些错误", exception);
            throw new RuntimeException(exception.getMessage());
        }
    }

    /**
     * 内部使用，快速将当前类中目标对象字段同名字段的值复制到目标对象字段上
     *
     * @param field  目标对象字段
     * @param target 目标对象
     */
    private void convert(Field field, Object target) {
        try {
            Field source = this.getClass().getDeclaredField(field.getName());
            field.setAccessible(true);
            source.setAccessible(true);
            field.set(target, source.get(this));
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
        }
    }
}