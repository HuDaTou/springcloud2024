---
alwaysApply: false
globs: **/service/**/*.java
---
# Service 层规范

## Service 接口

### 1. 继承关系

```java
public interface UserService extends IService<User> {
    // 业务方法
}
```

### 2. 方法命名

遵循主规范中的命名规范：

| 场景 | 方法名示例 |
|------|------------|
| 查询单个 | `getUserById(Long id)` / `findAccountById(Long id)` |
| 查询列表 | `listUsers()` / `listAllArticle()` |
| 分页查询 | `pageUsers(PageParams params)` |
| 新增 | `saveUser(User user)` / `createUser(UserDTO dto)` |
| 更新 | `updateUser(Long id, UserDTO dto)` |
| 删除 | `deleteUser(List<Long> ids)` |
| 业务操作 | `userRegister()`, `userLogin()`, `publish()` |

### 3. 返回类型

```java
// 查询类 - 返回业务对象
UserVO getUserById(Long id);
List<UserVO> listUsers();

// 增删改类 - 返回 ResultData
ResultData<Void> createUser(UserDTO dto);
ResultData<Void> updateStatus(Long id, Integer status);

// 分页类 - 返回 PageVO
PageVO<List<ArticleVO>> listAllArticle(Integer pageNum, Integer pageSize);
```

## Service 实现类

### 1. 基本结构

```java
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final MyRedisCache myRedisCache;
}
```

### 2. 依赖注入

```java
// ✅ 使用 @RequiredArgsConstructor（唯一推荐）
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleMapper articleMapper;
    private final CategoryMapper categoryMapper;
}

// ❌ 禁止使用 @Resource
// @Resource
// private CategoryMapper categoryMapper;
```

### 3. 查询单个

```java
@Override
public UserVO getUserById(Long id) {
    User user = userMapper.selectById(id);
    if (Objects.isNull(user)) {
        return null;
    }
    return user.copyProperties(UserVO.class);
}
```

### 4. 查询列表

```java
@Override
public List<UserVO> listUsers() {
    List<User> users = userMapper.selectList(null);
    return users.stream()
            .map(user -> user.copyProperties(UserVO.class))
            .toList();
}
```

### 5. 分页查询

```java
@Override
public PageVO<List<ArticleVO>> listAllArticle(Integer pageNum, Integer pageSize) {
    Page<Article> page = new Page<>(pageNum, pageSize);
    this.page(page, new LambdaQueryWrapper<Article>()
            .eq(Article::getStatus, SQLConst.PUBLIC_STATUS)
            .orderByDesc(Article::getCreateTime));

    List<ArticleVO> articleVOS = page.getRecords().stream()
            .map(article -> article.copyProperties(ArticleVO.class))
            .toList();

    return new PageVO<>(articleVOS, page.getTotal());
}
```

### 6. 条件查询

```java
@Override
public List<UserVO> searchUsers(UserSearchDTO dto) {
    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

    if (StringUtils.isNotBlank(dto.getUsername())) {
        queryWrapper.like(User::getUsername, dto.getUsername());
    }
    if (dto.getIsDisable() != null) {
        queryWrapper.eq(User::getIsDisable, dto.getIsDisable());
    }
    if (dto.getCreateTimeStart() != null) {
        queryWrapper.ge(User::getCreateTime, dto.getCreateTimeStart());
    }

    queryWrapper.orderByDesc(User::getCreateTime);

    List<User> users = userMapper.selectList(queryWrapper);
    return users.stream()
            .map(user -> user.copyProperties(UserVO.class))
            .toList();
}
```

### 7. 新增

```java
@Override
public ResultData<Void> createUser(UserCreateDTO dto) {
    // 1. 校验
    User existing = userMapper.selectOne(new LambdaQueryWrapper<User>()
            .eq(User::getUsername, dto.getUsername()));
    if (Objects.nonNull(existing)) {
        return ResultData.failure("用户名已存在");
    }

    // 2. 构建对象
    User user = new User();
    user.setUsername(dto.getUsername());
    user.setNickname(dto.getNickname());
    user.setCreateTime(LocalDateTime.now());
    user.setUpdateTime(LocalDateTime.now());

    // 3. 插入
    userMapper.insert(user);

    return ResultData.success();
}
```

### 8. 更新

```java
@Override
public ResultData<Void> updateUser(Long id, UserUpdateDTO dto) {
    User user = userMapper.selectById(id);
    if (Objects.isNull(user)) {
        return ResultData.failure("用户不存在");
    }

    user.setNickname(dto.getNickname());
    user.setUpdateTime(LocalDateTime.now());

    userMapper.updateById(user);
    return ResultData.success();
}
```

### 9. 删除

```java
@Override
@Transactional
public ResultData<Void> deleteUser(List<Long> ids) {
    // 删除关联数据
    userRoleMapper.delete(new LambdaQueryWrapper<UserRole>()
            .in(UserRole::getUserId, ids));
    // 删除主数据
    userMapper.deleteBatchIds(ids);
    return ResultData.success();
}
```

## 事务处理

### 1. 什么时候需要事务

| 场景 | 是否需要 @Transactional |
|------|-------------------------|
| 单个简单插入/更新/删除 | ❌ 不需要 |
| 多个表操作（如插入主表+明细表） | ✅ 需要 |
| 需要保证原子性的业务逻辑 | ✅ 需要 |
| 调用多个 Mapper 方法 | ✅ 需要 |
| 异常时需要回滚数据 | ✅ 需要 |

### 2. 事务使用示例

```java
// ❌ 不需要事务：单表简单插入
@Override
public ResultData<Void> createUser(UserDTO dto) {
    User user = new User();
    user.setUsername(dto.getUsername());
    userMapper.insert(user);
    return ResultData.success();
}

// ✅ 需要事务：多表操作需要原子性保证
@Override
@Transactional
public ResultData<Void> createUserWithRole(UserDTO dto) {
    userMapper.insert(user);
    userRoleMapper.insert(userRole);
    return ResultData.success();
}

// ✅ 需要事务：批量删除关联数据
@Override
@Transactional
public ResultData<Void> deleteUser(List<Long> ids) {
    userRoleMapper.delete(new LambdaQueryWrapper<UserRole>()
            .in(UserRole::getUserId, ids));
    userMapper.deleteBatchIds(ids);
    return ResultData.success();
}

// ✅ 事务回滚条件
@Transactional(rollbackFor = Exception.class)
public ResultData<Void> operation() {
    // ...
}
```

## VO 转换

```java
// ✅ 使用 copyProperties（项目统一方式）
UserVO vo = user.copyProperties(UserVO.class);

// ✅ 复杂转换使用 lambda
return article.copyProperties(ArticleDetailVO.class, vo -> {
    vo.setCategoryName(category.getCategoryName());
    vo.setTags(tags.stream().map(tag -> tag.copyProperties(TagVO.class)).toList());
});
```



### 1. Service 层使用链式组装对象

```java
// ✅ 推荐：使用 @Accessors(chain = true) 链式组装对象
@Override
public ResultData<Void> createUser(UserCreateDTO dto) {
    User user = new User()
            .setUsername(dto.getUsername())
            .setNickname(dto.getNickname())
            .setEmail(dto.getEmail())
            .setPhone(dto.getPhone());
    
    userMapper.insert(user);
    return ResultData.success();
}

❌ 禁止：不使用链式调用，代码冗长
// User user = new User();
// user.setUsername(dto.getUsername());
// user.setNickname(dto.getNickname());
// user.setEmail(dto.getEmail());
```

### 3. 复杂对象组装

```java
// ✅ 复杂对象组装示例（多表操作需要事务）
@Override
@Transactional
public ResultData<Void> createOrder(OrderCreateDTO dto) {
    Order order = new Order()
            .setUserId(dto.getUserId())
            .setOrderNo(generateOrderNo())
            .setTotalAmount(dto.getTotalAmount())
            .setStatus(OrderStatus.PENDING)
            .setCreateTime(LocalDateTime.now());
    
    orderMapper.insert(order);
    
    // 批量插入订单明细
    List<OrderItem> items = dto.getItems().stream()
            .map(itemDTO -> new OrderItem()
                    .setOrderId(order.getId())
                    .setProductId(itemDTO.getProductId())
                    .setQuantity(itemDTO.getQuantity())
                    .setPrice(itemDTO.getPrice()))
            .toList();
    
    orderItemMapper.insertBatchSomeColumn(items);
    return ResultData.success();
}
```

## 禁止行为

❌ **禁止**：在 Service 层直接返回 Entity，应转换为 VO
❌ **禁止**：在 Service 层处理 HTTP 请求/响应
❌ **禁止**：在 Service 层使用 `System.out.println()`
❌ **禁止**：业务逻辑写在 Controller 层
❌ **禁止**：不使用 `@Accessors(chain = true)` 链式调用组装对象
❌ **禁止**：给所有增删改方法都加 `@Transactional`，只在多表操作或需要原子性时使用
❌ **禁止**：使用 `@Resource` 注入依赖，必须使用 `@RequiredArgsConstructor`
