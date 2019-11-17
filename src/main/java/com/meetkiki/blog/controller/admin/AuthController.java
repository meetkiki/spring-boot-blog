package com.meetkiki.blog.controller.admin;

import com.meetkiki.blog.annotation.SysLog;
import com.meetkiki.blog.constants.TaleConst;
import com.meetkiki.blog.controller.BaseController;
import com.meetkiki.blog.exception.ValidatorException;
import com.meetkiki.blog.model.dto.RestResponse;
import com.meetkiki.blog.model.entity.Users;
import com.meetkiki.blog.model.params.LoginParam;
import com.meetkiki.blog.utils.DateUtils;
import com.meetkiki.blog.utils.EncryptUtils;
import com.meetkiki.blog.utils.StringUtils;
import com.meetkiki.blog.utils.TaleUtils;
import com.meetkiki.blog.validators.CommonValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.meetkiki.blog.constants.TaleConst.LOGIN_ERROR_COUNT;
import static io.github.biezhi.anima.Anima.select;

/**
 * 登录，退出
 * <p>
 * Created by biezhi on 2017/2/21.
 */
@Slf4j
@RestController
@RequestMapping("admin")
public class AuthController extends BaseController {

    @SysLog("登录后台")
    @PostMapping("login")
    public RestResponse<?> doLogin(LoginParam loginParam, HttpServletRequest request, HttpServletResponse response) {

        CommonValidator.valid(loginParam);

        Integer errorCount = cache.get(LOGIN_ERROR_COUNT);
        try {
            errorCount = null == errorCount ? 0 : errorCount;
            if (errorCount > 3) {
                return RestResponse.fail("您输入密码已经错误超过3次，请10分钟后尝试");
            }

            long count = new Users().where("username", loginParam.getUsername()).count();
            if (count < 1) {
                errorCount += 1;
                return RestResponse.fail("不存在该用户");
            }
            String pwd = EncryptUtils.md5(loginParam.getUsername(), loginParam.getPassword());

            Users user = select().from(Users.class)
                    .where(Users::getUsername, loginParam.getUsername())
                    .and(Users::getPassword, pwd).one();

            if (null == user) {
                errorCount += 1;
                return RestResponse.fail("用户名或密码错误");
            }
            HttpSession session = request.getSession();
            session.setAttribute(TaleConst.LOGIN_SESSION_KEY, user);

            if (StringUtils.isNotBlank(loginParam.getRememberMe())) {
                TaleUtils.setCookie(request,response, user.getUid());
            }

            Users temp = new Users();
            temp.setLogged(DateUtils.nowUnix());
            temp.updateById(user.getUid());
            log.info("登录成功：{}", loginParam.getUsername());

            cache.set(LOGIN_ERROR_COUNT, 0);

            return RestResponse.ok();

        } catch (Exception e) {
            errorCount += 1;
            cache.set(LOGIN_ERROR_COUNT, errorCount, 10 * 60);
            String msg = "登录失败";
            if (e instanceof ValidatorException) {
                msg = e.getMessage();
            } else {
                log.error(msg, e);
            }
            return RestResponse.fail(msg);
        }
    }

}
