package com.overthinker.cloud.system.redis.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类
 * <p>
 * 提供完善的 Redis 操作封装，支持：
 * <ul>
 *   <li>String/Value 操作</li>
 *   <li>Hash 操作</li>
 *   <li>List 操作</li>
 *   <li>Set 操作</li>
 *   <li>业务常用方法</li>
 * </ul>
 *
 * @author overthinker
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisCacheUtils {

    private final RedisTemplate<String, Object> redisTemplate;

    // ==================== 通用 Key 操作 ====================

    /**
     * 检查 key 是否存在
     *
     * @param key 键
     * @return true-存在，false-不存在
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 删除 key
     *
     * @param key 键（支持单个或多个）
     * @return 删除的数量
     */
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    /**
     * 批量删除 keys
     *
     * @param keys 键集合
     * @return 删除的数量
     */
    public long delete(Collection<String> keys) {
        return Optional.ofNullable(keys)
                .map(k -> redisTemplate.delete(k))
                .orElse(0L);
    }

    /**
     * 设置 key 的过期时间
     *
     * @param key      键
     * @param timeout  时间
     * @param timeUnit 时间单位
     * @return true-设置成功，false-设置失败
     */
    public boolean expire(String key, long timeout, TimeUnit timeUnit) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, timeUnit));
    }

    /**
     * 设置 key 的过期时间（默认秒）
     *
     * @param key      键
     * @param timeout  时间（秒）
     * @return true-设置成功，false-设置失败
     */
    public boolean expire(String key, long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置 key 在指定时间点过期
     *
     * @param key  键
     * @param date 过期时间点
     * @return true-设置成功，false-设置失败
     */
    public boolean expireAt(String key, Date date) {
        return Boolean.TRUE.equals(redisTemplate.expireAt(key, date));
    }

    /**
     * 获取 key 的剩余生存时间
     *
     * @param key      键
     * @param timeUnit 时间单位
     * @return 剩余时间（秒），-1表示永久，-2表示不存在
     */
    public long getExpire(String key, TimeUnit timeUnit) {
        Long expire = redisTemplate.getExpire(key, timeUnit);
        return expire == null ? -2 : expire;
    }

    /**
     * 获取 key 的剩余生存时间（默认秒）
     *
     * @param key 键
     * @return 剩余时间（秒）
     */
    public long getExpire(String key) {
        return getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 查找所有匹配模式的 key
     *
     * @param pattern 模式（如：user:*）
     * @return 匹配的 key 集合
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    // ==================== String/Value 操作 ====================

    /**
     * 设置字符串值
     *
     * @param key   键
     * @param value 值
     */
    public <T> void set(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置字符串值，并指定过期时间
     *
     * @param key      键
     * @param value    值
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     */
    public <T> void set(String key, T value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置字符串值，并指定过期时间（默认秒）
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间（秒）
     */
    public <T> void set(String key, T value, long timeout) {
        set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置字符串值，并指定过期时间
     *
     * @param key      键
     * @param value    值
     * @param duration 过期时间
     */
    public <T> void set(String key, T value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    /**
     * 当 key 不存在时设置值
     *
     * @param key   键
     * @param value 值
     * @return true-设置成功，false-key已存在
     */
    public <T> boolean setIfAbsent(String key, T value) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value));
    }

    /**
     * 当 key 不存在时设置值，并指定过期时间
     *
     * @param key      键
     * @param value    值
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @return true-设置成功，false-key已存在
     */
    public <T> boolean setIfAbsent(String key, T value, long timeout, TimeUnit timeUnit) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit));
    }

    /**
     * 当 key 存在时设置值
     *
     * @param key   键
     * @param value 值
     * @return true-设置成功，false-key不存在
     */
    public <T> boolean setIfPresent(String key, T value) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfPresent(key, value));
    }

    /**
     * 获取字符串值
     *
     * @param key 键
     * @param <T> 值类型
     * @return 值，不存在返回 null
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取字符串值，如果不存在则设置默认值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @param <T>          值类型
     * @return 值
     */
    public <T> T getOrDefault(String key, T defaultValue) {
        T value = get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取字符串值，如果不存在则通过 supplier 获取并设置
     *
     * @param key      键
     * @param supplier 值供应者
     * @param timeout  过期时间（秒）
     * @param <T>      值类型
     * @return 值
     */
    public <T> T getOrSupply(String key, java.util.function.Supplier<T> supplier, long timeout) {
        T value = get(key);
        if (value == null) {
            value = supplier.get();
            if (value != null) {
                set(key, value, timeout);
            }
        }
        return value;
    }

    /**
     * 覆盖指定位置的值
     *
     * @param key    键
     * @param value  值
     * @param offset 偏移量
     */
    public void setRange(String key, Object value, long offset) {
        redisTemplate.opsForValue().set(key, value, offset);
    }

    /**
     * 获取指定范围的值
     *
     * @param key    键
     * @param start  起始位置
     * @param end    结束位置（-1表示到末尾）
     * @return 子字符串
     */
    public String getRange(String key, long start, long end) {
        return redisTemplate.opsForValue().get(key, start, end);
    }

    /**
     * 获取字符串值的长度
     *
     * @param key 键
     * @return 长度
     */
    public long size(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().size(key)).orElse(0L);
    }

    /**
     * 批量获取值
     *
     * @param keys 键集合
     * @return 值列表
     */
    public List<Object> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 批量设置值
     *
     * @param map 键值对 map
     */
    public void multiSet(Map<String, Object> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    /**
     * 原子递增
     *
     * @param key 键
     * @param delta 增量
     * @return 递增后的值
     */
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 原子递增（默认+1）
     *
     * @param key 键
     * @return 递增后的值
     */
    public Long increment(String key) {
        return increment(key, 1L);
    }

    /**
     * 原子递减
     *
     * @param key 键
     * @param delta 减量
     * @return 递减后的值
     */
    public Long decrement(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * 原子递减（默认-1）
     *
     * @param key 键
     * @return 递减后的值
     */
    public Long decrement(String key) {
        return decrement(key, 1L);
    }

    /**
     * 浮点型递增
     *
     * @param key   键
     * @param delta 增量
     * @return 递增后的值
     */
    public Double incrementFloat(String key, double delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 追加字符串
     *
     * @param key   键
     * @param value 追加的值
     * @return 追加后的长度
     */
    public int append(String key, String value) {
        return Optional.ofNullable(redisTemplate.opsForValue().append(key, value)).orElse(0);
    }

    // ==================== Hash 操作 ====================

    /**
     * 设置 Hash 值
     *
     * @param key      键
     * @param hashKey  Hash 键
     * @param value    值
     */
    public <T> void hashPut(String key, String hashKey, T value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 设置 Hash 值，当 hashKey 不存在时
     *
     * @param key      键
     * @param hashKey  Hash 键
     * @param value    值
     * @return true-设置成功，false-hashKey已存在
     */
    public <T> boolean hashPutIfAbsent(String key, String hashKey, T value) {
        return Boolean.TRUE.equals(redisTemplate.opsForHash().putIfAbsent(key, hashKey, value));
    }

    /**
     * 批量设置 Hash 值
     *
     * @param key 键
     * @param map Hash 键值对
     */
    public <T> void hashPutAll(String key, Map<String, T> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 获取 Hash 值
     *
     * @param key     键
     * @param hashKey Hash 键
     * @param <T>     值类型
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public <T> T hashGet(String key, String hashKey) {
        return (T) redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取所有 Hash 值
     *
     * @param key  键
     * @param <T>  值类型
     * @return 所有键值对
     */
    @SuppressWarnings("unchecked")
    public <T> Map<String, T> hashGetAll(String key) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        Map<String, T> result = new HashMap<>();
        entries.forEach((k, v) -> result.put(String.valueOf(k), (T) v));
        return result;
    }

    /**
     * 获取 Hash 中所有字段
     *
     * @param key 键
     * @return 字段集合
     */
    public Set<Object> hashKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 获取 Hash 中所有值
     *
     * @param key  键
     * @param <T>  值类型
     * @return 值列表
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> hashValues(String key) {
        return (List<T>) redisTemplate.opsForHash().values(key);
    }

    /**
     * 获取 Hash 中字段数量
     *
     * @param key 键
     * @return 字段数量
     */
    public long hashSize(String key) {
        return Optional.ofNullable(redisTemplate.opsForHash().size(key)).orElse(0L);
    }

    /**
     * 检查 Hash 中是否存在字段
     *
     * @param key     键
     * @param hashKey Hash 键
     * @return true-存在，false-不存在
     */
    public boolean hashHasKey(String key, String hashKey) {
        return Boolean.TRUE.equals(redisTemplate.opsForHash().hasKey(key, hashKey));
    }

    /**
     * 删除 Hash 中字段
     *
     * @param key     键
     * @param hashKeys Hash 键（支持多个）
     * @return 删除的数量
     */
    public long hashDelete(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * Hash 字段递增
     *
     * @param key     键
     * @param hashKey Hash 键
     * @param delta   增量
     * @return 递增后的值
     */
    public long hashIncrement(String key, String hashKey, long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * Hash 字段递增（默认+1）
     *
     * @param key     键
     * @param hashKey Hash 键
     * @return 递增后的值
     */
    public long hashIncrement(String key, String hashKey) {
        return hashIncrement(key, hashKey, 1L);
    }

    /**
     * Hash 浮点型字段递增
     *
     * @param key     键
     * @param hashKey Hash 键
     * @param delta   增量
     * @return 递增后的值
     */
    public double hashIncrementFloat(String key, String hashKey, double delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * 批量获取 Hash 值
     *
     * @param key     键
     * @param hashKeys Hash 键集合
     * @param <T>     值类型
     * @return 值列表
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> hashMultiGet(String key, Collection<Object> hashKeys) {
        return (List<T>) redisTemplate.opsForHash().multiGet(key, hashKeys);
    }

    // ==================== List 操作 ====================

    /**
     * 从左边（头部）插入元素
     *
     * @param key   键
     * @param value 值
     * @return 列表长度
     */
    public <T> long listLeftPush(String key, T value) {
        return Optional.ofNullable(redisTemplate.opsForList().leftPush(key, value)).orElse(0L);
    }

    /**
     * 从左边（头部）批量插入元素
     *
     * @param key   键
     * @param values 值集合
     * @return 列表长度
     */
    public <T> long listLeftPushAll(String key, Collection<T> values) {
        return Optional.ofNullable(redisTemplate.opsForList().leftPushAll(key, values)).orElse(0L);
    }

    /**
     * 从右边（尾部）插入元素
     *
     * @param key   键
     * @param value 值
     * @return 列表长度
     */
    public <T> long listRightPush(String key, T value) {
        return Optional.ofNullable(redisTemplate.opsForList().rightPush(key, value)).orElse(0L);
    }

    /**
     * 从右边（尾部）批量插入元素
     *
     * @param key   键
     * @param values 值集合
     * @return 列表长度
     */
    public <T> long listRightPushAll(String key, Collection<T> values) {
        return Optional.ofNullable(redisTemplate.opsForList().rightPushAll(key, values)).orElse(0L);
    }

    /**
     * 获取列表中指定范围的所有元素
     *
     * @param key   键
     * @param start 起始位置（0开始）
     * @param end   结束位置（-1表示到末尾）
     * @param <T>   值类型
     * @return 值列表
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> listRange(String key, long start, long end) {
        return (List<T>) redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取列表所有元素
     *
     * @param key 键
     * @param <T> 值类型
     * @return 值列表
     */
    public <T> List<T> listAll(String key) {
        return listRange(key, 0, -1);
    }

    /**
     * 获取列表长度
     *
     * @param key 键
     * @return 长度
     */
    public long listSize(String key) {
        return Optional.ofNullable(redisTemplate.opsForList().size(key)).orElse(0L);
    }

    /**
     * 从左边（头部）弹出元素
     *
     * @param key 键
     * @param <T> 值类型
     * @return 值，列表为空返回 null
     */
    @SuppressWarnings("unchecked")
    public <T> T listLeftPop(String key) {
        return (T) redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从右边（尾部）弹出元素
     *
     * @param key 键
     * @param <T> 值类型
     * @return 值，列表为空返回 null
     */
    @SuppressWarnings("unchecked")
    public <T> T listRightPop(String key) {
        return (T) redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 获取指定索引位置的元素
     *
     * @param key   键
     * @param index 索引（0开始，负数从末尾计算）
     * @param <T>   值类型
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public <T> T listIndex(String key, long index) {
        return (T) redisTemplate.opsForList().index(key, index);
    }

    /**
     * 设置指定索引位置的值
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     */
    public <T> void listSet(String key, long index, T value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 修剪列表，保留指定范围
     *
     * @param key   键
     * @param start 起始位置
     * @param end   结束位置
     */
    public void listTrim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    /**
     * 删除列表中指定数量的值
     *
     * @param key   键
     * @param count 删除数量（正数从左开始，负数从右开始）
     * @param value 要删除的值
     * @return 删除的数量
     */
    public long listRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    // ==================== Set 操作 ====================

    /**
     * 添加 Set 成员
     *
     * @param key    键
     * @param values 值（支持多个）
     * @return 添加的数量
     */
    public <T> long setAdd(String key, T... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 添加 Set 成员
     *
     * @param key   键
     * @param value 值
     * @return true-添加成功，false-已存在
     */
    public <T> boolean setAddMember(String key, T value) {
        return redisTemplate.opsForSet().add(key, value) > 0;
    }

    /**
     * 获取 Set 所有成员
     *
     * @param key  键
     * @param <T>  值类型
     * @return 成员集合
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> setMembers(String key) {
        return (Set<T>) redisTemplate.opsForSet().members(key);
    }

    /**
     * 获取 Set 成员数量
     *
     * @param key 键
     * @return 成员数量
     */
    public long setSize(String key) {
        return Optional.ofNullable(redisTemplate.opsForSet().size(key)).orElse(0L);
    }

    /**
     * 检查值是否是 Set 成员
     *
     * @param key   键
     * @param value 值
     * @return true-是成员，false-不是
     */
    public boolean setIsMember(String key, Object value) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
    }

    /**
     * 删除 Set 成员
     *
     * @param key    键
     * @param values 要删除的值
     * @return 删除的数量
     */
    public <T> long setRemove(String key, T... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 随机弹出一个成员
     *
     * @param key 键
     * @param <T> 值类型
     * @return 弹出的值
     */
    @SuppressWarnings("unchecked")
    public <T> T setPop(String key) {
        return (T) redisTemplate.opsForSet().pop(key);
    }

    /**
     * 获取两个 Set 的交集
     *
     * @param key      键
     * @param otherKey 另一个键
     * @param <T>      值类型
     * @return 交集集合
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> setIntersect(String key, String otherKey) {
        return (Set<T>) redisTemplate.opsForSet().intersect(key, otherKey);
    }

    /**
     * 获取多个 Set 的交集
     *
     * @param key      键
     * @param otherKeys 其他键集合
     * @param <T>      值类型
     * @return 交集集合
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> setIntersect(String key, Collection<String> otherKeys) {
        return (Set<T>) redisTemplate.opsForSet().intersect(key, otherKeys);
    }

    /**
     * 获取两个 Set 的并集
     *
     * @param key      键
     * @param otherKey 另一个键
     * @param <T>      值类型
     * @return 并集集合
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> setUnion(String key, String otherKey) {
        return (Set<T>) redisTemplate.opsForSet().union(key, otherKey);
    }

    /**
     * 获取两个 Set 的差集
     *
     * @param key      键
     * @param otherKey 另一个键
     * @param <T>      值类型
     * @return 差集集合
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> setDifference(String key, String otherKey) {
        return (Set<T>) redisTemplate.opsForSet().difference(key, otherKey);
    }

    // ==================== 业务常用方法 ====================

    /**
     * 缓存对象（永久）
     *
     * @param key   键
     * @param value 值
     */
    public <T> void cacheObject(String key, T value) {
        set(key, value);
    }

    /**
     * 缓存对象（带过期时间）
     *
     * @param key      键
     * @param value    值
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     */
    public <T> void cacheObject(String key, T value, long timeout, TimeUnit timeUnit) {
        set(key, value, timeout, timeUnit);
    }

    /**
     * 缓存对象（默认秒）
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间（秒）
     */
    public <T> void cacheObject(String key, T value, long timeout) {
        cacheObject(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 获取缓存对象
     *
     * @param key  键
     * @param <T>  值类型
     * @return 值
     */
    public <T> T getCacheObject(String key) {
        return get(key);
    }

    /**
     * 删除缓存对象
     *
     * @param key 键
     * @return true-删除成功
     */
    public boolean deleteCacheObject(String key) {
        return delete(key);
    }

    /**
     * 缓存列表数据
     *
     * @param key      键
     * @param dataList 数据列表
     * @return 缓存的对象数量
     */
    public <T> long cacheList(String key, List<T> dataList) {
        return listRightPushAll(key, dataList);
    }

    /**
     * 获取缓存列表数据
     *
     * @param key  键
     * @param <T>  值类型
     * @return 列表数据
     */
    public <T> List<T> getCacheList(String key) {
        return listAll(key);
    }

    /**
     * 缓存 Set 数据
     *
     * @param key     键
     * @param dataSet 数据集合
     * @param <T>     值类型
     * @return 缓存的对象
     */
    public <T> long cacheSet(String key, Set<T> dataSet) {
        return setAdd(key, dataSet.toArray());
    }

    /**
     * 获取缓存 Set 数据
     *
     * @param key  键
     * @param <T>  值类型
     * @return Set 数据
     */
    public <T> Set<T> getCacheSet(String key) {
        return setMembers(key);
    }

    /**
     * 缓存 Map 数据
     *
     * @param key    键
     * @param dataMap 数据 Map
     */
    public <T> void cacheMap(String key, Map<String, T> dataMap) {
        hashPutAll(key, dataMap);
    }

    /**
     * 获取缓存 Map 数据
     *
     * @param key  键
     * @param <T>  值类型
     * @return Map 数据
     */
    public <T> Map<String, T> getCacheMap(String key) {
        return hashGetAll(key);
    }

    /**
     * 向 Map 中添加字段
     *
     * @param key    键
     * @param hKey   Hash 键
     * @param value  值
     */
    public <T> void cacheMapValue(String key, String hKey, T value) {
        hashPut(key, hKey, value);
    }

    /**
     * 获取 Map 中的字段值
     *
     * @param key   键
     * @param hKey  Hash 键
     * @param <T>   值类型
     * @return 值
     */
    public <T> T getCacheMapValue(String key, String hKey) {
        return hashGet(key, hKey);
    }

    /**
     * 删除 Map 中的字段
     *
     * @param key  键
     * @param hKey Hash 键
     */
    public void deleteCacheMapValue(String key, Object hKey) {
        hashDelete(key, hKey);
    }

    /**
     * 获取 Map 中的多个字段值
     *
     * @param key   键
     * @param hKeys Hash 键集合
     * @param <T>   值类型
     * @return 值列表
     */
    public <T> List<T> getMultiCacheMapValue(String key, Collection<Object> hKeys) {
        return hashMultiGet(key, hKeys);
    }

    /**
     * 递增统计值
     *
     * @param key    键
     * @param delta  增量
     * @return 递增后的值
     */
    public Long incrementCounter(String key, long delta) {
        return increment(key, delta);
    }

    /**
     * 获取统计值
     *
     * @param key   键
     * @param delta 默认值
     * @return 统计值
     */
    public Long getCounter(String key, long delta) {
        Long value = get(key);
        return value != null ? value : delta;
    }
}
