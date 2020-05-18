package com.example.csgs.configs;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.entity.UserSign;
import com.example.csgs.mapper.PermissionMapper;
import com.example.csgs.mapper.UserMapper;
import com.example.csgs.utils.JwtUtils;
import com.example.csgs.utils.RedisUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 登录拦截配置
 */
@Configuration
@Log4j
public class WebSecurityConfig extends WebMvcConfigurationSupport {
    final
    UserMapper userMapper;
    final
    PermissionMapper permissionMapper;

    private final RedisUtils redisUtils;
    private final String[] allWhiteList = new String[]{
            "/user/*", "/sign/*"
    };

    public WebSecurityConfig(RedisUtils redisUtils, UserMapper userMapper, PermissionMapper permissionMapper) {
        this.redisUtils = redisUtils;
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.addExposedHeader(HttpHeaders.SET_COOKIE);
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(configSource);
    }

    /**
     * 注入Bean 让 Spring 扫描 SecurityInterceptor
     * 不然过滤器不起作用
     */
    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    /**
     * 配置自定义拦截拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        List<String> passList = new ArrayList<>();
        Collections.addAll(passList, allWhiteList);
        addInterceptor.excludePathPatterns(passList);
        addInterceptor.addPathPatterns("/**");//拦截其他所有请求
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {
        /**
         * 在业务处理器处理请求之前被调用
         */
        private OutputStreamWriter ow;
        private JSONObject interceptorRes;

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

            String URIString = request.getRequestURI();  //获取URI
            log.info("URIString：" + URIString);
            String second = URIString.substring(1);
            String fatherURI = second.substring(0, second.indexOf("/"));
            log.info("fatherURI：" + fatherURI);

            ServletOutputStream out = response.getOutputStream();//创建一个输出流
            ow = new OutputStreamWriter(out);
            interceptorRes = new JSONObject();

            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=utf-8");//指定浏览器以什么码表打开服务器发送的数据
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Allow-Headers", "csgs_token");

            log.info("Origin:" + request.getHeader("Origin"));
            log.info("Cookie:" + Arrays.toString(request.getCookies()));
            log.info("csgs_token:" + request.getHeader("csgs_token"));
            log.info("Request:" + request.getRequestURI());

            if (request.getHeader("csgs_token") != null) {
                if (redisUtils.hasKey(request.getHeader("csgs_token"))) {
                    Claims claims = JwtUtils.parserToken(request.getHeader("csgs_token"));//解析token
                    if (claims != null) {
                        String userTypes = String.valueOf(claims.get("userType"));
                        log.info(userTypes);
                        String ids = String.valueOf(claims.get("id"));

                        if ("".equals(ids)) {
                            returnMsg("用户不存在！");
                            return false;
                        } else {
                            Long id = Long.parseLong(ids);
                            log.info("id:" + id);
                            UserSign token_user = userMapper.queryUserByName(id);  //根据id查询用户
                            if (token_user != null) {  //如果用户存在
                                if (!fatherURI.equals("profile") && !fatherURI.equals("pwdPro")) {  //除了公共接口外的接口
                                    if (permissionMapper.findUserTypeByPermissionName(fatherURI) > token_user.getUserType()) {  //没有访问此接口的权限
                                        returnMsg("没有权限！");
                                        return false;
                                    }
                                }
                                redisUtils.expire(request.getHeader("csgs_token"), JwtUtils.TOKEN_EXPIRE_TIME); //如果 Token 存在重新刷新过期时间并放行
                                return true;
                            }
                        }
                    }
                } else {
                    returnMsg("token 不正确!");
                    return false;
                }
            }
            // 遍历完毕也没找到则报空
            returnMsg("token 为空, 请先登录!");
            return false;//拦截
        }

        private void returnMsg(String msg) throws IOException {
            interceptorRes.put("msg", msg);
            ow.write(interceptorRes.toJSONString());//要返回的信息
            ow.flush();//冲刷出流，将所有缓冲的数据发送到目的地
            ow.close();//关闭流
        }
    }
}
//            Cookie[] cookies = request.getCookies();//获取 Token
//            if (request.getCookies() != null) {
//                for (Cookie c : cookies) {
//                    if (c.getName().equals("csgs_token")) { //找到 Token Cookie
//                        if (redisUtils.hasKey(c.getValue())) {
//                            Claims claims = JwtUtils.parserToken(c.getValue());//解析token
//                            if (claims != null) {
//                                String userTypes = String.valueOf(claims.get("userType"));
//                                String ids = String.valueOf(claims.get("id"));
//                                /**
//                                 * 根据获取的userTypes和id、接口路径名来做是否允许改用户访问这个接口
//                                 */
//                                redisUtils.expire(c.getValue(), JwtUtils.TOKEN_EXPIRE_TIME); //如果 Token 存在重新刷新过期时间并放行
//                                return true;
//                            }
//                        } else {
//                            interceptorRes.put("msg", "token 不正确!");
//                            ow.write(interceptorRes.toJSONString());//要返回的信息
//                            ow.flush();//冲刷出流，将所有缓冲的数据发送到目的地
//                            ow.close();//关闭流
//                        }
//                        return false;
//                    }
//                }
//            }