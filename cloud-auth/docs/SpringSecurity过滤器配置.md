# Spring Security 过滤器配置指南

## 目录

- [1. 概述](#1-概述)
- [2. Spring Security 过滤器链](#2-spring-security-过滤器链)
- [3. 添加过滤器的常见方式](#3-添加过滤器的常见方式)
  - [3.1 addFilterBefore](#31-addfilterbefore)
  - [3.2 addFilterAfter](#32-addfilterafter)
  - [3.3 addFilterAt](#33-addfilterat)
  - [3.4 替换现有过滤器](#34-替换现有过滤器)
- [4. AuthenticationProvider 方式](#4-authenticationprovider-方式)
- [5. WebSecurityConfigurerAdapter 方式](#5-websecurityconfigureradapter-方式)
- [6. 完整示例](#6-完整示例)
- [7. 最佳实践](#7-最佳实践)

---

## 1. 概述

Spring Security 是基于过滤器的安全框架，所有的安全控制都通过过滤器链实现。了解如何正确添加和配置过滤器是掌握 Spring Security 的关键。

---

## 2. Spring Security 过滤器链

Spring Security 的过滤器链按照特定顺序执行，常见过滤器包括：

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          Spring Security 过滤器链                           │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │ 1. ChannelProcessingFilter                                           │  │
│  │    - 决定使用 HTTP 还是 HTTPS                                         │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
│                                  ↓                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │ 2. SecurityContextPersistenceFilter                                  │  │
│  │    - 从 Session 加载 SecurityContext                                │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
│                                  ↓                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │ 3. ConcurrentSessionFilter                                           │  │
│  │    - 检查并发会话                                                    │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
│                                  ↓                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │ 4. HeaderWriterFilter                                               │  │
│  │    - 添加安全相关的 HTTP 头                                          │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
│                                  ↓                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │ 5. CsrfFilter                                                       │  │
│  │    - CSRF 防护                                                      │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
│                                  ↓                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │ 6. LogoutFilter                                                     │  │
│  │    - 处理退出登录请求                                                │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
│                                  ↓                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │ 7. UsernamePasswordAuthenticationFilter                              │  │
│  │    - 表单登录认证                                                    │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
│                                  ↓                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │ 8. RequestCacheAwareFilter                                          │  │
│  │    - 缓存请求以便认证后重定向                                        │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
│                                  ↓                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │ 9. SecurityContextHolderAwareRequestFilter                          │  │
│  │    - 包装请求为 Security 友好的形式                                  │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
│                                  ↓                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │ 10. RememberMeFilter                                                │  │
│  │    - 记住我功能                                                     │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
│                                  ↓                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │ 11. AnonymousAuthenticationFilter                                   │  │
│  │    - 为匿名用户创建认证 Token                                       │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
│                                  ↓                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │ 12. SessionManagementFilter                                         │  │
│  │    - 会话管理                                                        │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
│                                  ↓                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │ 13. ExceptionTranslationFilter                                      │  │
│  │    - 转换安全异常为 HTTP 响应                                        │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
│                                  ↓                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │ 14. FilterSecurityInterceptor                                       │  │
│  │    - 权限校验                                                        │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. 添加过滤器的常见方式

### 3.1 addFilterBefore

在指定过滤器**之前**添加新的过滤器。

**语法**：

```java
http.addFilterBefore(自定义过滤器, 目标过滤器.class);
```

**示例：添加验证码校验过滤器**

```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(
                new CaptchaAuthenticationFilter(), 
                UsernamePasswordAuthenticationFilter.class
            )
            .formLogin(form -> form
                .loginProcessingUrl("/auth/login")
                .successHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":200,\"msg\":\"登录成功\"}");
                })
                .failureHandler((request, response, exception) -> {
                    response.setStatus(401);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":401,\"msg\":\"登录失败\"}");
                })
            );
        
        return http.build();
    }
}
```

**验证码过滤器实现**：

```java
public class CaptchaAuthenticationFilter extends OncePerRequestFilter {
    
    private final CaptchaService captchaService;
    
    public CaptchaAuthenticationFilter(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response,
                                    FilterChain filterChain) 
            throws ServletException, IOException {
        
        // 只处理登录请求
        if ("/auth/login".equals(request.getRequestURI()) && "POST".equals(request.getMethod())) {
            String captcha = request.getParameter("captcha");
            String captchaKey = request.getParameter("captchaKey");
            
            if (!captchaService.validate(captchaKey, captcha)) {
                response.setStatus(401);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":401,\"msg\":\"验证码错误\"}");
                return;
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
```

**执行流程**：

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     addFilterBefore 执行顺序                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌─────────────────┐                                                       │
│  │  CaptchaFilter  │ ← 新增的过滤器                                         │
│  └────────┬────────┘                                                       │
│           ↓                                                                 │
│  ┌─────────────────────────┐                                               │
│  │ UsernamePasswordFilter  │ ← 目标过滤器                                    │
│  └─────────────────────────┘                                               │
│                                                                             │
│  顺序：CaptchaFilter → UsernamePasswordFilter                             │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

### 3.2 addFilterAfter

在指定过滤器**之后**添加新的过滤器。

**语法**：

```java
http.addFilterAfter(自定义过滤器, 目标过滤器.class);
```

**示例：添加登录日志过滤器**

```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .formLogin(form -> form
                .loginProcessingUrl("/auth/login")
                // ... 配置
            )
            .addFilterAfter(
                new LoginLogFilter(), 
                UsernamePasswordAuthenticationFilter.class
            );
        
        return http.build();
    }
}
```

**登录日志过滤器实现**：

```java
public class LoginLogFilter extends OncePerRequestFilter {
    
    private final LoginLogService loginLogService;
    
    public LoginLogFilter(LoginLogService loginLogService) {
        this.loginLogService = loginLogService;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response,
                                    FilterChain filterChain) 
            throws ServletException, IOException {
        
        long startTime = System.currentTimeMillis();
        
        // 先执行后续过滤器
        filterChain.doFilter(request, response);
        
        // 过滤器执行完成后记录日志
        long duration = System.currentTimeMillis() - startTime;
        
        // 判断是否为登录请求
        if ("/auth/login".equals(request.getRequestURI()) && "POST".equals(request.getMethod())) {
            LoginLog log = new LoginLog();
            log.setRequestUri(request.getRequestURI());
            log.setMethod(request.getMethod());
            log.setDuration(duration);
            log.setIp(getClientIp(request));
            
            loginLogService.save(log);
        }
    }
    
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
```

**执行流程**：

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     addFilterAfter 执行顺序                                 │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌─────────────────────────┐                                               │
│  │ UsernamePasswordFilter  │ ← 目标过滤器                                    │
│  └────────┬────────────────┘                                               │
│           ↓                                                                 │
│  ┌─────────────────┐                                                       │
│  │   LoginLogFilter│ ← 新增的过滤器                                         │
│  └─────────────────┘                                                       │
│                                                                             │
│  顺序：UsernamePasswordFilter → LoginLogFilter                             │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

### 3.3 addFilterAt

在指定过滤器的**位置**添加新的过滤器，**替换**该位置的原有过滤器。

**语法**：

```java
http.addFilterAt(自定义过滤器, 目标过滤器.class);
```

**示例：替换 UsernamePasswordAuthenticationFilter**

```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .addFilterAt(
                customAuthenticationFilter(), 
                UsernamePasswordAuthenticationFilter.class
            )
            .formLogin(form -> form
                .loginProcessingUrl("/auth/login")
                // successHandler 和 failureHandler 会被自定义过滤器使用
            );
        
        return http.build();
    }
    
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":200,\"msg\":\"登录成功\"}");
        });
        filter.setAuthenticationFailureHandler((request, response, exception) -> {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"登录失败\"}");
        });
        return filter;
    }
}
```

**自定义认证过滤器**：

```java
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    private final CaptchaService captchaService;
    private final UserService userService;
    
    public CustomAuthenticationFilter(CaptchaService captchaService, UserService userService) {
        this.captchaService = captchaService;
        this.userService = userService;
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, 
                                               HttpServletResponse response) 
            throws AuthenticationException {
        
        // 1. 验证码校验
        String captcha = request.getParameter("captcha");
        String captchaKey = request.getParameter("captchaKey");
        
        if (!captchaService.validate(captchaKey, captcha)) {
            throw new BadCredentialsException("验证码错误");
        }
        
        // 2. 获取用户名密码
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        
        // 3. 创建认证 Token
        UsernamePasswordAuthenticationToken authRequest = 
            new UsernamePasswordAuthenticationToken(username, password);
        
        setDetails(request, authRequest);
        
        // 4. 执行认证
        return this.getAuthenticationManager().authenticate(authRequest);
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest request, 
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        
        // 1. 更新登录状态
        LoginUser loginUser = (LoginUser) authResult.getPrincipal();
        userService.updateLoginStatus(loginUser.getUser().getId(), 0);
        
        // 2. 调用父类方法继续过滤器链
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
```

**执行流程**：

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       addFilterAt 执行顺序                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  替换前：                                                                   │
│  ┌─────────────────────────────────────────────────────────────┐          │
│  │ UsernamePasswordAuthenticationFilter (原有)                  │          │
│  └─────────────────────────────────────────────────────────────┘          │
│                                                                             │
│  替换后：                                                                   │
│  ┌─────────────────────────────────────────────────────────────┐          │
│  │ CustomAuthenticationFilter (替换)                           │          │
│  │  - 自定义验证码校验                                          │          │
│  │  - 自定义用户名校验                                          │          │
│  │  - 调用父类认证逻辑                                          │          │
│  └─────────────────────────────────────────────────────────────┘          │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

### 3.4 替换现有过滤器

通过 `addFilterAt` 可以完全替换 Spring Security 的内置过滤器。

**示例：替换 CsrfFilter**

```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 完全禁用 CSRF（不推荐）
            .csrf(AbstractHttpConfigurer::disable);
        
        return http.build();
    }
}
```

**或者替换为自定义 CSRF 过滤器**：

```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .addFilterAt(customCsrfFilter(), CsrfFilter.class);
        
        return http.build();
    }
    
    @Bean
    public CustomCsrfFilter customCsrfFilter() {
        return new CustomCsrfFilter();
    }
}
```

---

## 4. AuthenticationProvider 方式

`AuthenticationProvider` 是另一种扩展认证逻辑的方式，它在过滤器之后、认证管理器中执行。

### 4.1 创建自定义 AuthenticationProvider

```java
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CaptchaService captchaService;
    
    public CustomAuthenticationProvider(UserDetailsService userDetailsService, 
                                       PasswordEncoder passwordEncoder,
                                       CaptchaService captchaService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.captchaService = captchaService;
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) 
            throws AuthenticationException {
        
        // 1. 获取请求对象（用于获取验证码）
        HttpServletRequest request = 
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        
        // 2. 验证码校验
        String captcha = request.getParameter("captcha");
        String captchaKey = request.getParameter("captchaKey");
        
        if (!captchaService.validate(captchaKey, captcha)) {
            throw new BadCredentialsException("验证码错误");
        }
        
        // 3. 获取用户名密码
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        // 4. 加载用户信息
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        // 5. 密码校验
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("密码错误");
        }
        
        // 6. 返回认证成功的 Token
        return new UsernamePasswordAuthenticationToken(
            userDetails, 
            password, 
            userDetails.getAuthorities()
        );
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
```

### 4.2 注册 AuthenticationProvider

```java
@Configuration
public class SecurityConfig {
    
    private final CustomAuthenticationProvider customAuthenticationProvider;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(customAuthenticationProvider)
            .formLogin(form -> form
                .loginProcessingUrl("/auth/login")
                .successHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":200,\"msg\":\"登录成功\"}");
                })
                .failureHandler((request, response, exception) -> {
                    response.setStatus(401);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":401,\"msg\":\"" + exception.getMessage() + "\"}");
                })
            );
        
        return http.build();
    }
}
```

### 4.3 AuthenticationProvider vs Filter

| 特性 | AuthenticationProvider | Filter |
|------|----------------------|--------|
| 执行位置 | 认证管理器内部 | 过滤器链中 |
| 职责 | 核心认证逻辑 | 请求预处理/后处理 |
| 灵活性 | 专注于认证 | 可处理任何请求 |
| 适用场景 | 密码校验、额外验证 | 验证码、日志、参数处理 |

---

## 5. WebSecurityConfigurerAdapter 方式

在旧版 Spring Security 中，可以使用 `WebSecurityConfigurerAdapter` 配置过滤器。

### 5.1 继承 WebSecurityConfigurerAdapter（已过时）

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginProcessingUrl("/auth/login")
                .successHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":200,\"msg\":\"登录成功\"}");
                })
                .failureHandler((request, response, exception) -> {
                    response.setStatus(401);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":401,\"msg\":\"登录失败\"}");
                })
                .and()
            .addFilterBefore(new CaptchaFilter(), UsernamePasswordAuthenticationFilter.class)
            .csrf().disable();
    }
}
```

**注意**：`WebSecurityConfigurerAdapter` 在 Spring Security 5.7+ 已过时，推荐使用组件化配置。

---

## 6. 完整示例

### 6.1 需求

实现一个带有多重校验的登录流程：

1. **前置校验**：验证码校验、IP 黑名单校验
2. **主校验**：用户名密码校验
3. **后置校验**：登录日志、登录状态更新

### 6.2 实现代码

**SecurityConfig.java**

```java
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtEncoder jwtEncoder;
    private final CaptchaService captchaService;
    private final BlackListService blackListService;
    private final UserService userService;
    private final LoginLogService loginLogService;
    
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. 配置端点权限
            .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/actuator/**", "/css/**", "/js/**", "/favicon.ico", "/error", "/internal/**").permitAll()
                .requestMatchers("/oauth2/jwks", "/oauth2/token", "/oauth2/authorize", "/oauth2/logout").permitAll()
                .requestMatchers("/auth/register", "/auth/send-code", "/auth/captcha").permitAll()
                .anyRequest().authenticated()
            )
            // 2. 添加验证码过滤器
            .addFilterBefore(captchaFilter(), UsernamePasswordAuthenticationFilter.class)
            // 3. 添加 IP 黑名单过滤器
            .addFilterBefore(ipBlacklistFilter(), UsernamePasswordAuthenticationFilter.class)
            // 4. 表单登录配置
            .formLogin(form -> form
                .loginProcessingUrl("/auth/login")
                .successHandler(this::loginSuccessHandler)
                .failureHandler(this::loginFailureHandler)
            )
            // 5. 退出登录配置
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler(this::logoutSuccessHandler)
            )
            // 6. 禁用 CSRF
            .csrf(AbstractHttpConfigurer::disable);
        
        return http.build();
    }
    
    // ============ 过滤器 ============
    
    @Bean
    public CaptchaAuthenticationFilter captchaFilter() {
        CaptchaAuthenticationFilter filter = new CaptchaAuthenticationFilter(captchaService);
        return filter;
    }
    
    @Bean
    public IpBlacklistFilter ipBlacklistFilter() {
        IpBlacklistFilter filter = new IpBlacklistFilter(blackListService);
        return filter;
    }
    
    // ============ Handler ============
    
    private void loginSuccessHandler(HttpServletRequest request, HttpServletResponse response, 
                                    Authentication authentication) throws IOException {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        
        // 1. 更新登录状态
        userService.updateLoginStatus(loginUser.getUser().getId(), 0);
        
        // 2. 生成 Token
        String token = generateToken(loginUser);
        Instant expireTime = Instant.now().plus(30, ChronoUnit.MINUTES);
        
        // 3. 返回响应
        response.setContentType("application/json;charset=UTF-8");
        String json = String.format("{\"code\":200,\"msg\":\"登录成功\",\"token\":\"%s\",\"expire\":\"%s\"}", 
            token, expireTime.toString());
        response.getWriter().write(json);
        
        // 4. 异步记录日志
        CompletableFuture.runAsync(() -> {
            loginLogService.saveLoginLog(loginUser.getUser().getUsername(), request, "成功");
        });
        
        log.info("用户登录成功: {}", authentication.getName());
    }
    
    private void loginFailureHandler(HttpServletRequest request, HttpServletResponse response, 
                                     AuthenticationException exception) throws IOException {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        
        String message = getAuthExceptionMessage(exception);
        
        response.getWriter().write(String.format("{\"code\":401,\"msg\":\"%s\"}", message));
        
        // 异步记录失败日志
        String username = request.getParameter("username");
        CompletableFuture.runAsync(() -> {
            loginLogService.saveLoginLog(username, request, "失败: " + exception.getMessage());
        });
        
        log.warn("用户登录失败: {}", exception.getMessage());
    }
    
    private void logoutSuccessHandler(HttpServletRequest request, HttpServletResponse response, 
                                     Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":200,\"msg\":\"退出成功\"}");
        
        if (authentication != null) {
            log.info("用户退出登录: {}", authentication.getName());
        }
    }
    
    // ============ Token 生成 ============
    
    private String generateToken(LoginUser loginUser) {
        Instant now = Instant.now();
        
        String authorities = loginUser.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.joining(","));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("http://localhost:9123")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .subject(loginUser.getUser().getUsername())
                .claim("user_id", loginUser.getUser().getId())
                .claim("username", loginUser.getUser().getUsername())
                .claim("authorities", authorities)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    
    // ============ 辅助方法 ============
    
    private String getAuthExceptionMessage(AuthenticationException exception) {
        return switch (exception.getClass().getSimpleName()) {
            case "UsernameNotFoundException" -> "用户不存在或已被禁用";
            case "BadCredentialsException" -> "验证码错误或用户名密码错误";
            case "AccountExpiredException" -> "账户已过期";
            case "CredentialsExpiredException" -> "凭证已过期，请修改密码";
            case "DisabledException" -> "账户已被禁用";
            case "LockedException" -> "账户已被锁定";
            default -> "登录失败: " + exception.getMessage();
        };
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

**CaptchaAuthenticationFilter.java**

```java
public class CaptchaAuthenticationFilter extends OncePerRequestFilter {
    
    private final CaptchaService captchaService;
    
    public CaptchaAuthenticationFilter(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response,
                                    FilterChain filterChain) 
            throws ServletException, IOException {
        
        // 只处理登录请求
        if ("/auth/login".equals(request.getRequestURI()) && "POST".equals(request.getMethod())) {
            String captcha = request.getParameter("captcha");
            String captchaKey = request.getParameter("captchaKey");
            
            if (captcha == null || captcha.isEmpty()) {
                sendErrorResponse(response, "验证码不能为空");
                return;
            }
            
            if (!captchaService.validate(captchaKey, captcha)) {
                sendErrorResponse(response, "验证码错误");
                return;
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(String.format("{\"code\":401,\"msg\":\"%s\"}", message));
    }
}
```

**IpBlacklistFilter.java**

```java
public class IpBlacklistFilter extends OncePerRequestFilter {
    
    private final BlackListService blackListService;
    
    public IpBlacklistFilter(BlackListService blackListService) {
        this.blackListService = blackListService;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response,
                                    FilterChain filterChain) 
            throws ServletException, IOException {
        
        String clientIp = getClientIp(request);
        
        // 检查 IP 是否在黑名单
        if (blackListService.isIpBlocked(clientIp)) {
            response.setStatus(403);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":403,\"msg\":\"IP已被封禁\"}");
            return;
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多个 IP，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
```

---

## 7. 最佳实践

### 7.1 过滤器设计原则

| 原则 | 说明 | 示例 |
|------|------|------|
| **单一职责** | 每个过滤器只负责一件事 | 验证码过滤器只验证验证码 |
| **快速失败** | 尽早拦截无效请求 | IP 黑名单过滤器放在最前面 |
| **异步处理** | 非关键逻辑使用异步 | 登录日志使用 CompletableFuture |
| **异常处理** | 统一异常处理机制 | 返回标准 JSON 错误响应 |

### 7.2 过滤器执行顺序

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       过滤器执行顺序建议                                    │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  1. ChannelProcessingFilter      - 协议选择 (HTTP/HTTPS)                   │
│  2. ConcurrentSessionFilter      - 并发会话控制                            │
│  3. ⚠️ 自定义: IpBlacklistFilter - IP 黑名单校验（前置）                   │
│  4. ⚠️ 自定义: CaptchaFilter     - 验证码校验（前置）                       │
│  5. SecurityContextPersistenceFilter                                      │
│  6. HeaderWriterFilter           - HTTP 头写入                             │
│  7. CsrfFilter                   - CSRF 防护                              │
│  8. LogoutFilter                 - 退出登录                                │
│  9. UsernamePasswordAuthenticationFilter - 用户名密码认证                   │
│  10. ⚠️ 自定义: LoginLogFilter  - 登录日志（后置）                         │
│  11. RequestCacheAwareFilter     - 请求缓存                                 │
│  12. SecurityContextHolderAwareRequestFilter                               │
│  13. RememberMeFilter           - 记住我                                   │
│  14. AnonymousAuthenticationFilter                                       │
│  15. SessionManagementFilter     - 会话管理                                │
│  16. ExceptionTranslationFilter   - 异常转换                                 │
│  17. FilterSecurityInterceptor    - 权限校验                                 │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 7.3 选择合适的扩展方式

| 场景 | 推荐方式 | 原因 |
|------|----------|------|
| 验证码校验 | `addFilterBefore` | 在认证前拦截，无需改变认证逻辑 |
| 登录日志 | `addFilterAfter` | 在认证后执行，不影响认证流程 |
| 完全替换认证逻辑 | `addFilterAt` | 替换现有过滤器，完全控制认证流程 |
| 添加认证提供者 | `AuthenticationProvider` | 专注于认证逻辑，与过滤器解耦 |
| 异步任务 | `ApplicationListener` | 事件驱动，不阻塞响应 |

### 7.4 性能优化

1. **使用 OncePerRequestFilter**：确保每个请求只执行一次
2. **避免重复校验**：如验证码在校验失败后删除
3. **使用缓存**：如 IP 黑名单使用 Redis 缓存
4. **异步处理**：非关键逻辑使用 `@Async` 或 `CompletableFuture`

```java
@Async
public void saveLoginLogAsync(String username, HttpServletRequest request, String result) {
    // 异步保存日志
}
```

### 7.5 安全注意事项

1. **参数校验**：所有外部输入都需要校验
2. **防止暴力破解**：添加登录失败次数限制
3. **敏感信息**：不要在日志中记录密码
4. **超时处理**：设置合理的请求超时时间

```java
@Override
protected void doFilterInternal(HttpServletRequest request, 
                               HttpServletResponse response,
                               FilterChain filterChain) throws ServletException, IOException {
    try {
        // 添加超时保护
        long startTime = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        long duration = System.currentTimeMillis() - startTime;
        
        if (duration > 5000) {
            log.warn("请求处理时间过长: {}ms, URI: {}", duration, request.getRequestURI());
        }
    } catch (Exception e) {
        log.error("过滤器执行异常", e);
        throw e;
    }
}
```

---

## 附录：常见过滤器类

| 过滤器类 | 说明 |
|----------|------|
| `UsernamePasswordAuthenticationFilter` | 用户名密码认证过滤器 |
| `BasicAuthenticationFilter` | HTTP Basic 认证过滤器 |
| `BearerTokenAuthenticationFilter` | Bearer Token 认证过滤器 |
| `CasAuthenticationFilter` | CAS 单点登录过滤器 |
| `OpenIDAuthenticationFilter` | OpenID 认证过滤器 |
| `DigestAuthenticationFilter` | HTTP Digest 认证过滤器 |
| `RememberMeAuthenticationFilter` | 记住我认证过滤器 |
| `ConcurrentSessionFilter` | 并发会话控制过滤器 |
| `CsrfFilter` | CSRF 防护过滤器 |
| `LogoutFilter` | 退出登录过滤器 |

---

**文档版本**：1.0  
**最后更新**：2024-XX-XX  
**维护者**：overH
