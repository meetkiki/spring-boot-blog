package com.meetkiki.blog.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Paths;


@Slf4j
@RestController("install")
public class InstallController extends BaseController {

    @Resource
    private SiteService siteService;

    @Resource
    private OptionsService optionsService;

    /**
     * 安装页
     */
    @GetMapping
    public String index(HttpServletRequest request) {
        boolean existInstall   = Files.exists(Paths.get(CLASSPATH + "install.lock"));
        boolean allowReinstall = TaleConst.OPTIONS.getBoolean(OPTION_ALLOW_INSTALL, false);
        request.setAttribute("is_install", !allowReinstall && existInstall);
        return "install";
    }

    @PostRoute
    @JSON
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

        TaleConst.OPTIONS = Environment.of(optionsService.getOptions());

        return RestResponse.ok();
    }

    private boolean isRepeatInstall() {
        return Files.exists(Paths.get(CLASSPATH + "install.lock"))
                && TaleConst.OPTIONS.getInt("allow_install", 0) != 1;
    }

}
