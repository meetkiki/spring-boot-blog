package com.meetkiki.blog.controller.admin;

import com.meetkiki.blog.annotation.SysLog;
import com.meetkiki.blog.bootstrap.TaleConst;
import com.meetkiki.blog.controller.BaseController;
import com.meetkiki.blog.model.entity.Users;
import com.meetkiki.blog.service.OptionsService;
import com.meetkiki.blog.service.SiteService;
import com.meetkiki.blog.utils.EncryptUtils;
import com.meetkiki.blog.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;

/**
 * 后台控制器
 * Created by biezhi on 2017/2/21.
 */
@Slf4j
@Controller(value = "admin")
public class SystemController extends BaseController {

    @Resource
    private OptionsService optionsService;

    @Resource
    private SiteService siteService;

    @SysLog("保存个人信息")
    @PostMapping("profile")
    public RestResponse saveProfile(@Param String screenName, @Param String email) {
        Users users = this.user();
        if (StringUtils.isNotBlank(screenName) && StringUtils.isNotBlank(email)) {
            Users temp = new Users();
            temp.setScreenName(screenName);
            temp.setEmail(email);
            temp.updateById(users.getUid());
        }
        return RestResponse.ok();
    }

    @SysLog("修改登录密码")
    @PostMapping("password")
    public RestResponse upPwd(@Param String old_password, @Param String password) {
        Users users = this.user();
        if (StringUtils.isBlank(old_password) || StringUtils.isBlank(password)) {
            return RestResponse.fail("请确认信息输入完整");
        }

        if (!users.getPassword().equals(EncryptUtils.md5(users.getUsername() + old_password))) {
            return RestResponse.fail("旧密码错误");
        }
        if (password.length() < 6 || password.length() > 14) {
            return RestResponse.fail("请输入6-14位密码");
        }

        Users  temp = new Users();
        String pwd  = EncryptUtils.md5(users.getUsername() + password);
        temp.setPassword(pwd);
        temp.updateById(users.getUid());
        optionsService.deleteOption(TaleConst.OPTION_SAFE_REMEMBER_ME);
        return RestResponse.ok();
    }

}
