package com.meetkiki.blog.interceptor;

import com.meetkiki.blog.constants.TaleConst;
import com.meetkiki.blog.model.entity.Users;
import com.meetkiki.blog.utils.IpUtil;
import com.meetkiki.blog.utils.TaleUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static com.meetkiki.blog.constants.TaleConst.INSTALL;
import static io.github.biezhi.anima.Anima.select;

@Slf4j
public class BaseWebInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String ip  = IpUtil.getIpAddr(request);
        // 禁止该ip访问
        if (TaleConst.BLOCK_IPS.contains(ip)) {
            try (PrintWriter writer = response.getWriter()) {
                writer.println("You have been banned, brother");
            }
            return false;
        }
        // 中文乱码
        response.setContentType("text/html;charset=utf-8");
        log.info("IP: {}, UserAgent: {}", ip, request.getHeader("User-Agent"));
        boolean install = Boolean.parseBoolean(TaleConst.OPTIONS.getOrDefault(INSTALL, "false"));
        // 如果未安装
        if (!install){
            response.sendRedirect(TaleConst.INSTALL_URI);
            return false;
        }
        return isRedirect(request,response);
    }


    private boolean isRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri  = request.getRequestURI();
        if (uri.startsWith(TaleConst.ADMIN_URI) && !uri.startsWith(TaleConst.LOGIN_URI)) {
            request.setAttribute(TaleConst.PLUGINS_MENU_NAME, TaleConst.PLUGIN_MENUS);
            Users user = TaleUtils.getLoginUser();
            if(null != user){
                return true;
            }

            Integer uid = TaleUtils.getCookieUid(request);
            if (null != uid) {
                user = select().from(Users.class).byId(uid);
                request.getSession().setAttribute(TaleConst.LOGIN_SESSION_KEY, user);
            }
            if (null == user) {
                response.sendRedirect(TaleConst.LOGIN_URI);
                return false;
            }
        }
        return true;
    }

}
