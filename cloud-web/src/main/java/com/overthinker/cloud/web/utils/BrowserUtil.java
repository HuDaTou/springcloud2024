package com.overthinker.cloud.web.utils;

import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.UserAgent;
import jakarta.servlet.http.HttpServletRequest;

public class BrowserUtil {

    /**
     * 获取浏览器名称及版本
     * @param request request
     * @return 名称 - 版本号
     */
    public static String browserName(HttpServletRequest request){
        String userAgent = request.getHeader("User-Agent");
        UserAgent ua = UserAgent.parseUserAgentString(userAgent);
        Browser browser = ua.getBrowser();
        return browser.getName() + "-" + browser.getVersion(userAgent);
    }

    /**
     * 获取操作系统名称
     * @param request request
     * @return 名称
     */
    public static String osName(HttpServletRequest request){
        String userAgent = request.getHeader("User-Agent");
        UserAgent ua = UserAgent.parseUserAgentString(userAgent);
        OperatingSystem os = ua.getOperatingSystem();
        return os.getName();
    }

}