package com.meetkiki.blog.controller.admin;

import com.alibaba.fastjson.JSON;
import com.meetkiki.blog.controller.BaseController;
import com.meetkiki.blog.extension.Commons;
import com.meetkiki.blog.service.ContentsService;
import com.meetkiki.blog.service.MetasService;
import com.meetkiki.blog.service.OptionsService;
import com.meetkiki.blog.service.SiteService;
import com.meetkiki.blog.utils.JsonUtils;
import com.meetkiki.blog.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.meetkiki.blog.constants.TaleConst.CLASSPATH;

/**
 * @author biezhi
 * @date 2018/6/5
 */
@Slf4j
@Controller
@RequestMapping("admin")
public class PagesController extends BaseController {

    @Resource
    private ContentsService contentsService;

    @Resource
    private MetasService metasService;

    @Resource
    private OptionsService optionsService;

    @Resource
    private SiteService siteService;

    @GetMapping("{page}")
    public String commonPage(@PathVariable String page) {
        return "admin/" + page + ".html";
    }

    @GetMapping("article/new")
    public String articlePage(){
        return "admin/article/new";
    }

    @GetMapping("articles")
    public String articlesPage(){
        return "admin/articles";
    }

    @GetMapping("pages")
    public String pages(){
        return "admin/pages";
    }

    @GetMapping("attaches")
    public String attaches(){
        return "admin/attaches";
    }

    @GetMapping("comments")
    public String comments(){
        return "admin/comments";
    }

    @GetMapping("categories")
    public String categories(){
        return "admin/categories";
    }

    @GetMapping("themes")
    public String themes(){
        return "admin/themes";
    }

    @GetMapping("setting")
    public String setting(){
        return "admin/setting";
    }

    @GetMapping("{module}/{page}")
    public String commonPage(@PathVariable String module, @PathVariable String page) {
        return "admin/" + module + "/" + page + ".html";
    }

    @GetMapping("article/edit/{cid}")
    public String editArticle(@PathVariable String cid) {
        return "admin/article/edit";
    }

    @GetMapping("page/new")
    public String pageNew(){
        return "admin/page/new";
    }

    @GetMapping("page/edit/{cid}")
    public String editPage(@PathVariable String cid) {
        return "admin/page/edit";
    }

    @GetMapping("login")
    public String login(HttpServletResponse response) {
        if (null != this.user()) {
            return "redirect:/admin/index";
        }
        return "admin/login";
    }

    @GetMapping("template")
    public String index(HttpServletRequest request) {
        String themePath = CLASSPATH + File.separatorChar + "templates" + File.separatorChar + "themes" + File.separatorChar + Commons.site_theme();
        try {
            List<String> files = Files.list(Paths.get(themePath))
                    .map(path -> path.getFileName().toString())
                    .filter(path -> path.endsWith(".html"))
                    .collect(Collectors.toList());

            List<String> partial = Files.list(Paths.get(themePath + File.separatorChar + "partial"))
                    .map(path -> path.getFileName().toString())
                    .filter(path -> path.endsWith(".html"))
                    .map(fileName -> "partial/" + fileName)
                    .collect(Collectors.toList());

            List<String> statics = Files.list(Paths.get(themePath + File.separatorChar + "static"))
                    .map(path -> path.getFileName().toString())
                    .filter(path -> path.endsWith(".js") || path.endsWith(".css"))
                    .map(fileName -> "static/" + fileName)
                    .collect(Collectors.toList());

            files.addAll(partial);
            files.addAll(statics);

            request.setAttribute("tpls", files);
        } catch (IOException e) {
            log.error("找不到模板路径");
        }
        return "admin/tpl_list";
    }

    @GetMapping("template/content")
    public void getContent(@RequestParam String fileName, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        try (PrintWriter writer = response.getWriter()){
            String themePath = CLASSPATH + File.separatorChar + "templates" + File.separatorChar + "themes" + File.separatorChar + Commons.site_theme();
            String filePath  = themePath + File.separatorChar + fileName;
            String content   = Files.readAllLines(Paths.get(filePath)).stream().collect(Collectors.joining("\n"));
            writer.print(content);
        } catch (IOException e) {
            log.error("获取模板文件失败", e);
        }
    }

    /**
     * 主题设置页面
     */
    @GetMapping("theme/setting")
    public String setting(HttpServletRequest request) {
        String currentTheme = Commons.site_theme();
        String key          = "theme_" + currentTheme + "_options";

        String              option = optionsService.getOption(key);
        Map<String, Object> map    = new HashMap<>();
        try {
            if (StringUtils.isNotBlank(option)) {
                map = JSON.parseObject(option);
            }
            request.setAttribute("options", map);
        } catch (Exception e) {
            log.error("解析主题设置出现异常", e);
        }
        request.setAttribute("theme_options", map);
        return this.render("setting");
    }

}
