package com.meetkiki.blog.interceptor;

import com.meetkiki.blog.annotation.SysLog;
import com.meetkiki.blog.model.entity.Logs;
import com.meetkiki.blog.utils.DateUtils;
import com.meetkiki.blog.utils.IpUtil;
import com.meetkiki.blog.utils.TaleUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Order(3)
@Component
public class SubscribeAop {
    public static final String PREFIX = "templates/";

    // 对以下com.web.controller.Controller类中的所有方法进行切入
    @Pointcut("@annotation(com.meetkiki.blog.annotation.SysLog)")
    private void method(){}

    // @After("method() && args(jsonData ,request)")
    @Around("method()&&@annotation(sysLog)")  // 使用上面定义的切入点
    public Object Interceptor(ProceedingJoinPoint joinPoint, SysLog sysLog) throws Throwable {
        // 这里记录日志 , 这里处理的内容会切入controller中
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(null != TaleUtils.getLoginUser()){
            if (null != sysLog) {
                Logs logs = new Logs();
                logs.setAction(sysLog.value());
                logs.setAuthorId(TaleUtils.getLoginUser().getUid());
                logs.setIp(IpUtil.getIpAddr(request));
                if(!request.getRequestURI().contains("upload")){
                    logs.setData(TaleUtils.bodyToString(request));
                }
                logs.setCreated(DateUtils.nowUnix());
                logs.save();
            }
        }
        return joinPoint.proceed();
    }

    // 对以下com.web.controller.Controller类中的所有方法进行切入
    @Pointcut("execution(public * com.meetkiki.blog.controller..*.*(..))")
    private void view(){}

    @Around("view()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = joinPoint.proceed();
        if (proceed instanceof String){
            String view = (String) proceed;
            if (!view.startsWith("redirect:")){
                return template(view);
            }
        }
        return proceed;
    }


    public String template(String view){
        return PREFIX + view;
    }
}