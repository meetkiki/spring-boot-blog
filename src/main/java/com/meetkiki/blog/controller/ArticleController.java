package com.meetkiki.blog.controller;

import com.meetkiki.blog.constants.TaleConst;
import com.meetkiki.blog.exception.ValidatorException;
import com.meetkiki.blog.extension.Commons;
import com.meetkiki.blog.model.dto.ErrorCode;
import com.meetkiki.blog.model.dto.RestResponse;
import com.meetkiki.blog.model.dto.Types;
import com.meetkiki.blog.model.entity.Comments;
import com.meetkiki.blog.model.entity.Contents;
import com.meetkiki.blog.service.CommentsService;
import com.meetkiki.blog.service.ContentsService;
import com.meetkiki.blog.service.SiteService;
import com.meetkiki.blog.utils.IpUtil;
import com.meetkiki.blog.utils.StringUtils;
import com.meetkiki.blog.validators.CommonValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

import static com.meetkiki.blog.constants.TaleConst.COMMENT_APPROVED;
import static com.meetkiki.blog.constants.TaleConst.COMMENT_NO_AUDIT;
import static com.meetkiki.blog.constants.TaleConst.OPTION_ALLOW_COMMENT_AUDIT;

/**
 * @author biezhi
 * @date 2018/6/4
 */

@Slf4j
@Controller
public class ArticleController extends BaseController {

    @Resource
    private ContentsService contentsService;

    @Resource
    private CommentsService commentsService;

    @Resource
    private SiteService siteService;

    /**
     * 自定义页面
     */
    @GetMapping(value = "{cid}")
    public String page(@PathVariable String cid, HttpServletRequest request, @RequestParam(defaultValue = "1") Integer cp) {
        Contents contents = contentsService.getContents(cid);
        if (null == contents) {
            return this.render_404();
        }
        if (contents.getAllowComment()) {
            request.setAttribute("cp", cp == null ? 1 : cp);
        }
        request.setAttribute("article", contents);
        Contents temp = new Contents();
        temp.setHits(contents.getHits() + 1);
        temp.updateById(contents.getCid());
        if (Types.ARTICLE.equals(contents.getType())) {
            return this.render("post");
        }
        if (Types.PAGE.equals(contents.getType())) {
            return this.render("page");
        }
        return this.render_404();
    }

    /**
     * 文章页
     */
    @GetMapping(value = {"article/{cid}", "article/{cid}.html"})
    public String post(HttpServletRequest request, @PathVariable String cid,@RequestParam(defaultValue = "1") Integer cp) {
        Contents contents = contentsService.getContents(cid);
        if (null == contents) {
            return this.render_404();
        }
        if (Types.DRAFT.equals(contents.getStatus())) {
            return this.render_404();
        }
        request.setAttribute("article", contents);
        request.setAttribute("is_post", true);
        if (contents.getAllowComment()) {
            request.setAttribute("cp", cp == null ? 1 : cp);
        }
        Contents temp = new Contents();
        temp.setHits(contents.getHits() + 1);
        temp.updateById(contents.getCid());
        return this.render("post");
    }

    /**
     * 评论操作
     */
    @PostMapping(value = "comment")
    @ResponseBody
    public RestResponse<?> comment(HttpServletRequest request, HttpServletResponse response,
                                   @RequestHeader(value="User-Agent") String userAgent,
                                   @RequestHeader(value="Referer") String referer,
                                   Comments comments) {

        if (StringUtils.isBlank(referer)) {
            return RestResponse.fail(ErrorCode.BAD_REQUEST);
        }

        if (!referer.startsWith(Commons.site_url())) {
            return RestResponse.fail("非法评论来源");
        }

        CommonValidator.valid(comments);

        String ipAddr = IpUtil.getIpAddr(request);
        String  val   = ipAddr + ":" + comments.getCid();
        Integer count = cache.hget(Types.COMMENTS_FREQUENCY, val);
        if (null != count && count > 0) {
            return RestResponse.fail("您发表评论太快了，请过会再试");
        }
        comments.setIp(ipAddr);
        comments.setAgent(userAgent);

        if (Boolean.parseBoolean(TaleConst.OPTIONS.getOrDefault(OPTION_ALLOW_COMMENT_AUDIT, "true"))) {
            comments.setStatus(COMMENT_NO_AUDIT);
        } else {
            comments.setStatus(COMMENT_APPROVED);
        }

        try {
            commentsService.saveComment(comments);
            Cookie tale_remember_author = new Cookie("tale_remember_author", URLEncoder.encode(comments.getAuthor(), "UTF-8"));
            tale_remember_author.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(tale_remember_author);
            Cookie tale_remember_mail = new Cookie("tale_remember_mail", URLEncoder.encode(comments.getMail(), "UTF-8"));
            tale_remember_mail.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(tale_remember_mail);
            if (StringUtils.isNotBlank(comments.getUrl())) {
                Cookie tale_remember_url = new Cookie("tale_remember_url", URLEncoder.encode(comments.getUrl(), "UTF-8"));
                tale_remember_url.setMaxAge(7 * 24 * 60 * 60);
                response.addCookie(tale_remember_url);
            }

            // 设置对每个文章30秒可以评论一次
            cache.hset(Types.COMMENTS_FREQUENCY, val, 1, 30);
            siteService.cleanCache(Types.SYS_STATISTICS);

            return RestResponse.ok();
        } catch (Exception e) {
            String msg = "评论发布失败";
            if (e instanceof ValidatorException) {
                msg = e.getMessage();
            } else {
                log.error(msg, e);
            }
            return RestResponse.fail(msg);
        }
    }

}
