package com.example.csgs.configs;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.utils.JwtUtils;
import com.example.csgs.utils.RedisUtils;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
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
@EnableEncryptableProperties
@Slf4j
public class WebSecurityConfig extends WebMvcConfigurationSupport {

    private final RedisUtils redisUtils;
    private final String[] allWhiteList = new String[]{
            "/user/*", "/sign/*"
    };
    private final List<String> GetWhiteList = Arrays.asList(
            "/admin/getAllAreaList"
    );

    public WebSecurityConfig(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    //跨域配置
    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        super.addCorsMappings(registry);
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("POST", "GET", "DELETE", "PUT")
                .allowedOrigins("*")
                .allowCredentials(true) //带上cookie信息
                .maxAge(3600);
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        config.addExposedHeader("csgs_token");
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
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
            // 在 GET 请求的白名单中
            if (request.getMethod().equals("GET") && GetWhiteList.contains(request.getRequestURI())) {
                return true;
            }

            ServletOutputStream out = response.getOutputStream();//创建一个输出流
            response.setCharacterEncoding("UTF-8");
            //指定浏览器以什么码表打开服务器发送的数据
            response.setContentType("text/html;charset=utf-8");

            OutputStreamWriter ow = new OutputStreamWriter(out);
            JSONObject interceptorRes = new JSONObject();

            Cookie[] cookies = request.getCookies();//获取 Token
            if (request.getCookies() != null) {
                for (Cookie c : cookies) {
                    if (c.getName().equals("csgs_token")) { //找到 Token Cookie
                        if (redisUtils.hasKey(c.getValue())) {
                            Claims claims = JwtUtils.parserToken(c.getValue());//解析token
                            if (claims != null) {
                                String userTypes = String.valueOf(claims.get("userType"));
                                String ids = String.valueOf(claims.get("id"));
                                /**
                                 * 根据获取的userTypes和id、接口路径名来做是否允许改用户访问这个接口
                                 */
                                redisUtils.expire(c.getValue(), JwtUtils.TOKEN_EXPIRE_TIME); //如果 Token 存在重新刷新过期时间并放行
                                return true;
                            }
                        } else {
                            interceptorRes.put("msg", "token 不正确!");
                            ow.write(interceptorRes.toJSONString());//要返回的信息
                            ow.flush();//冲刷出流，将所有缓冲的数据发送到目的地
                            ow.close();//关闭流
                        }
                        return false;
                    }
                }
            }

            // 遍历完毕也没找到则报空
            interceptorRes.put("msg", "token 为空, 请先登录!");
            ow.write(interceptorRes.toJSONString());//要返回的信息
            ow.flush();//冲刷出流，将所有缓冲的数据发送到目的地
            ow.close();//关闭流
            return false;//拦截
        }
    }
}