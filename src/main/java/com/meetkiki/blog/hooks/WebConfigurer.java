package com.meetkiki.blog.hooks;

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
				.excludePathPatterns("/install")
				.excludePathPatterns("/error")
				// 排除静态资源
				.excludePathPatterns("/static/**")
				.excludePathPatterns("/templates/**");
	 }

	@Bean
	public BaseWebInterceptor BaseWebInterceptor() {
		return new BaseWebInterceptor();
	}

}