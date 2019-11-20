package com.meetkiki.blog.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfigurer implements WebMvcConfigurer {
	 @Override
	 public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(BaseWebInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns("/500")
				.excludePathPatterns("/404")
				// 排除静态资源
				.excludePathPatterns("/static/**")
				.excludePathPatterns("/templates/**");
	 }

	@Bean
	public BaseWebInterceptor BaseWebInterceptor() {
		return new BaseWebInterceptor();
	}

}