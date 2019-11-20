package com.meetkiki.blog.controller;


import com.meetkiki.blog.constants.TaleConst;
import com.meetkiki.blog.model.dto.RestResponse;
import com.meetkiki.blog.model.entity.Users;
import com.meetkiki.blog.model.params.InstallParam;
import com.meetkiki.blog.service.OptionsService;
import com.meetkiki.blog.service.SiteService;
import com.meetkiki.blog.utils.TaleUtils;
import com.meetkiki.blog.validators.CommonValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.meetkiki.blog.constants.TaleConst.*;


@Slf4j
@Controller
public class InstallController extends BaseController{

    @Resource
    private SiteService siteService;

    @Resource
    private OptionsService optionsService;

    /**
     * 安装页
     */
    @GetMapping("install")
    public String index(HttpServletRequest request) {
        boolean install = Boolean.parseBoolean(OPTIONS.getOrDefault(INSTALL, "false"));
        boolean allowReinstall = Boolean.parseBoolean(TaleConst.OPTIONS.getOrDefault(OPTION_ALLOW_INSTALL, "false"));
        request.setAttribute("is_install", !allowReinstall && install);
        return "install";
    }

    @PostMapping("install")
    @ResponseBody
    public RestResponse<?> doInstall(InstallParam installParam) {
        if (isRepeatInstall()) {
            return RestResponse.fail("请勿重复安装");
        }

        CommonValidator.valid(installParam);

        Users temp = new Users();
        temp.setUsername(installParam.getAdminUser());
        temp.setPassword(installParam.getAdminPwd());
        temp.setEmail(installParam.getAdminEmail());

        siteService.initSite(temp);

        String siteUrl = TaleUtils.buildURL(installParam.getSiteUrl());
        optionsService.saveOption("site_title", installParam.getSiteTitle());
        optionsService.saveOption("site_url", siteUrl);

        TaleConst.OPTIONS = optionsService.getOptions();

        return RestResponse.ok();
    }

    private boolean isRepeatInstall() {
        return Boolean.parseBoolean(TaleConst.OPTIONS.getOrDefault(INSTALL, "false"))
                && !Boolean.parseBoolean(TaleConst.OPTIONS.getOrDefault(OPTION_ALLOW_INSTALL, "false"));
    }

}
