package com.meetkiki.blog.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommonController extends BaseController{

    /**
     * 404 error
     * @return
     */
    @RequestMapping("/404")
    public String error404() {
        return "/comm/error_404.html";
    }

    /**
     * 500 error
     * @return
     */
    @RequestMapping("/500")
    public String error500() {
        return "/comm/error_500.html";
    }
}
