package com.overthinker.cloud.web.filter;

/**
 * @author overH
 * <p>
 * 创建时间：2023/9/7 0:22
 * 处理跨域请求
 */
//@Component

import com.overthinker.cloud.web.entity.constants.Const;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;

import java.io.IOException;

//// 优先级
@Order(Const.ORDER_CORS)
public class CorsFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.addCorsHeader(request, response);
        chain.doFilter(request,response);
    }
    private void addCorsHeader(HttpServletRequest request, HttpServletResponse response){
        response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Authorization,Content-Type,X-Client-Type,x-requested-with");
    }
}
