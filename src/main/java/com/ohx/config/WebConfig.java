package com.ohx.config;

import com.ohx.interceptor.QueryIntercepor;
import com.ohx.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/** 
 * web拦截器
 * @author 贾榕福
 * @date 2022/10/25
 */

@Configuration
@ConfigurationProperties(prefix = "config.path")
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    QueryIntercepor queryIntercepor;

    //拦截地址
    private List<String> special = new ArrayList<>();
    //忽略拦截的路径
    private List<String> exclude = new ArrayList<>();
    
    public void setExclude(List<String> exclude){
        this.exclude =exclude;
    }

    public void setSpecial(List<String> special){
        this.special =special;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // addResourceHandler是指你想在url请求的路径
        // addResourceLocations是图片存放的真实路
        registry.addResourceHandler("/api/warship_img/**")
                .addResourceLocations("file:/home/warship_img/twitter/")
                .addResourceLocations("file:/home/warship_img/rimpeace/")
                .addResourceLocations("file:/home/warship_img/bing/")
                .addResourceLocations("file:/home/warship_img/google/")
                .addResourceLocations("file:/home/warship_img/alamy/");
        registry.addResourceHandler("/api/commander_img/**")
                .addResourceLocations("file:/home/warship_img/commander/");
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(TokenInterceptor())
                //拦截地址
                .addPathPatterns(special)
                //忽略拦截的路径
                .excludePathPatterns(exclude)
                .order(0)
            ;
//        registry.addInterceptor(queryIntercepor)
//                //拦截地址
//                .addPathPatterns(special)
//                //忽略拦截的路径
//                .excludePathPatterns(exclude)
//                .order(1);
    }

    /**
     * jwt拦截器
     * @Author: 贾榕福
     * @Date: 2022/10/28
     */
    @Bean
    public HandlerInterceptor TokenInterceptor(){
        return new TokenInterceptor();
    }

    /**
     * 允许跨域请求
     * @param registry
     * @Author: 贾榕福
     * @Date: 2022/10/27
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOrigins("*")
                // 设置允许的请求方式
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                // 跨域允许时间
                .maxAge(3600)
                // 是否允许cookie
//                .allowCredentials(true)
        ;
    }


}
