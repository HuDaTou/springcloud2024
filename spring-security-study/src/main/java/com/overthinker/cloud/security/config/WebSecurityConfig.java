package com.overthinker.cloud.security.config;

import com.overthinker.cloud.security.handler.MyAuthenticationHandler;
import com.overthinker.cloud.security.handler.MySessionInformationExpiredStrategy;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurityConfig {

//    @Bean
//    public UserDetailsService userDetailsService() {
////        创建基于内存的用户信息管理器
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
////        使用manager管理userDetails对象
//        manager.createUser(
////                创建userDetails对象，用于管理用户名，密码用户角色，用户权限等内容
//                User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build());
//        return manager;
//    }
//
//@Bean
//public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    http
//            .authorizeHttpRequests(
//                    authorize -> authorize
//                    //对所有的请求开启授权保护
//                    .anyRequest()
//                    //已认证的请求会被自动授权
//                    .authenticated()
//            )
//            //使用表单授权方式
//            .formLogin( form -> {
//                form
//                        .loginPage("/login").permitAll()
//                        .usernameParameter("username")
//                        .passwordParameter("password")
//                        .failureUrl("/login?error")
//                        .successHandler(new MyAuthenticationSuccessHandler())//认证成功时的处理
//                        .failureHandler(new MyAuthenticationFailureHandler())//认证失败时的处理
//
//                        ;
//
//                    }
//
//            )
//            .logout(logout -> {
//                logout.logoutSuccessHandler(new MyLogoutSuccessHandler()) //注销成功时的
//                ;
//            })
//            //使用基本授权方式
//            .httpBasic(
//                    withDefaults()
//            )
//            .exceptionHandling(exception -> {
//                exception.authenticationEntryPoint(new MyAuthenticationEntryPoint())
//                        ;
//            })
//            .cors(withDefaults())
//
//
//            ;
//
//    //关闭csrf攻击防御
//    http.csrf((csrf) -> {
//        csrf.disable();
//    });
//    return http.build();
//}

@Resource
private MyAuthenticationHandler MyAuthenticationHandler;

@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            .authorizeHttpRequests(
                    authorize -> authorize
                            //对所有的请求开启授权保护
                            .anyRequest()
                            //已认证的请求会被自动授权
                            .authenticated()
            )
            //使用表单授权方式
            .formLogin( form -> {
                        form
                                .loginPage("/login").permitAll()
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .failureUrl("/login?error")
                                .successHandler(MyAuthenticationHandler::onAuthenticationSuccess)//认证成功时的处理
                                .failureHandler(MyAuthenticationHandler::onAuthenticationFailure)//认证失败时的处理

                        ;

                    }

            )
            .logout(logout -> {
                logout.logoutSuccessHandler(MyAuthenticationHandler::onLogoutSuccess) //注销成功时的
                ;
            })
            //使用基本授权方式
            .httpBasic(
                    withDefaults()
            )
            .exceptionHandling(exception -> {
                exception.authenticationEntryPoint(MyAuthenticationHandler::commence)
                ;
            })
            .cors(withDefaults())

            .sessionManagement(session -> {
                session
                        .maximumSessions(1)
                        .expiredSessionStrategy(new MySessionInformationExpiredStrategy());
            })


    ;

    //关闭csrf攻击防御
    http.csrf(AbstractHttpConfigurer::disable);
    return http.build();
}



//@Bean
//public UserDetailsService userDetailsService() {
//    DBUserDetailsManager manager = new DBUserDetailsManager();
//    return manager;
//}
}