package com.durandal.QASystem.configuration;

import com.durandal.QASystem.interceptor.LoginRequiredinterceptor;
import com.durandal.QASystem.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


@Configuration
public class QASystemConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    LoginRequiredinterceptor loginRequiredinterceptor;

    @Autowired
    PassportInterceptor passportInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequiredinterceptor).addPathPatterns("/user/*");
        super.addInterceptors(registry);
    }
}
