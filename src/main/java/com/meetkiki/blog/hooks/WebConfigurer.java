package com.meetkiki.blog.hooks;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


//@Configuration
public class WebConfigurer implements WebMvcConfigurer {
	 @Override
	 public void addInterceptors(InterceptorRegistry registry) {
        // 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
        registry.addInterceptor(BaseWebInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns("/install")
				.excludePathPatterns("/admin/**.css")
				.excludePathPatterns("/admin/**.png")
				.excludePathPatterns("/admin/**.js**")
				.excludePathPatterns("/admin/plugins/**")
				.excludePathPatterns("/templates/themes/**")
				.excludePathPatterns("/error");
	 }

	@Bean
	public BaseWebInterceptor BaseWebInterceptor() {
		return new BaseWebInterceptor();
	}

}