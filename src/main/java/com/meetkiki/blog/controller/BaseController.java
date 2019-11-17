package com.meetkiki.blog.controller;

import com.meetkiki.blog.model.entity.Users;
import com.meetkiki.blog.utils.MapCache;
import com.meetkiki.blog.utils.TaleUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by biezhi on 2017/2/21.
 */
public abstract class BaseController {

    public static String THEME = "themes/default";

    protected MapCache cache = MapCache.single();

    public String render(String viewName) {
        return THEME + "/" + viewName;
    }


    public BaseController title(HttpServletRequest request, String title) {
        request.setAttribute("title", title);
        return this;
    }

    public BaseController keywords(HttpServletRequest request, String keywords) {
        request.setAttribute("keywords", keywords);
        return this;
    }

    public Users user() {
        return TaleUtils.getLoginUser();
    }

    public Integer getUid(){
        return this.user().getUid();
    }

    public String render_404() {
        return "/comm/error_404";
    }

}
