package com.meetkiki.blog.bootstrap;

import com.meetkiki.blog.constants.TaleConst;
import com.meetkiki.blog.controller.BaseController;
import com.meetkiki.blog.extension.AdminCommons;
import com.meetkiki.blog.extension.Commons;
import com.meetkiki.blog.extension.JetTag;
import com.meetkiki.blog.extension.Theme;
import com.meetkiki.blog.model.dto.RememberMe;
import com.meetkiki.blog.model.dto.Types;
import com.meetkiki.blog.service.OptionsService;
import com.meetkiki.blog.service.SiteService;
import com.meetkiki.blog.utils.JsonUtils;
import com.meetkiki.blog.validators.Validators;
import io.github.biezhi.anima.Anima;
import jetbrick.io.resource.ClasspathResource;
import jetbrick.io.resource.Resource;
import jetbrick.template.JetEngine;
import jetbrick.template.JetGlobalContext;
import jetbrick.template.JetTemplate;
import jetbrick.template.loader.AbstractResourceLoader;
import jetbrick.template.resolver.GlobalResolver;
import jetbrick.template.web.springmvc.JetTemplateViewResolver;
import jetbrick.util.PathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static com.meetkiki.blog.constants.TaleConst.CLASSPATH;
import static com.meetkiki.blog.constants.TaleConst.OPTION_SAFE_REMEMBER_ME;


/**
 * Tale初始化进程
 *
 * @author biezhi
 */
@Configuration
public class Bootstrap {

    @Autowired
    private SiteService siteService;
    @Autowired
    private OptionsService optionsService;

    @Value("${app.dev}")
    private Boolean devMode;
    @Value("${app.version}")
    private String appVersion;
    @Value("${app.enableCdn}")
    private Boolean appEnableCdn;
    @Value("${app.max.file.size}")
    private Integer appMaxFileSize;

    @PostConstruct
    public void onApplicationEvent() {
        Validators.useChinese();
        SqliteJdbc.importSql(devMode);
        Anima.open(SqliteJdbc.DB_SRC);
        Commons.setSiteService(siteService);
    }

    @Bean("jetTemplateViewResolver")
    public JetTemplateViewResolver jetTemplateViewResolver() {
        JetTemplateViewResolver templateViewResolver = new JetTemplateViewResolver();
        templateViewResolver.setOrder(1);
        templateViewResolver.setCache(true);
        Properties config = new Properties();
        templateViewResolver.setConfigProperties(config);
        // 	在指定的包中进行自动扫描
        config.put("jetx.autoscan.packages","com.meetkiki.blog.extension");
        // 默认模板文件扩展名
        config.put("jetx.template.suffix",".html");
        // 模板源文件的编码格式
        config.put("jetx.input.encoding","utf-8");
        // 模板输出编码格式
        config.put("jetx.output.encoding","utf-8");
        // 模板加载器
        config.put("jetx.template.loaders",MyResourceLoader.class.getName());
        templateViewResolver.setContentType("text/html");
        return templateViewResolver;
    }


    @Bean("jetEngine")
    @ConditionalOnClass(JetTemplateViewResolver.class)
    public JetEngine jetEngine() {
        JetTemplateViewResolver jetTemplateViewResolver = jetTemplateViewResolver();
        JetEngine engine = jetTemplateViewResolver.getJetEngine();
        GlobalResolver resolver = engine.getGlobalResolver();
        resolver.registerFunctions(Commons.class);
        resolver.registerFunctions(Theme.class);
        resolver.registerFunctions(AdminCommons.class);
        resolver.registerTags(JetTag.class);

        List<String> macros = new ArrayList<>(8);
        macros.add("templates" + File.separatorChar +  "comm" + File.separatorChar + "macros.html");
        // 扫描主题下面的所有自定义宏
        String themeDir = CLASSPATH + "templates" + File.separatorChar + "themes";
        File[] dir = new File(themeDir).listFiles();
        if (null != dir) {
            for (File f : dir) {
                if (f.isDirectory() && Files.exists(Paths.get(f.getPath() + File.separatorChar + "macros.html"))) {
                    String macroName = File.separatorChar + "themes" + File.separatorChar + f.getName() + File.separatorChar + "macros.html";
                    macros.add(macroName);
                }
            }
        }
        for (String macro : macros) {
            JetTemplate template = engine.getTemplate(macro);
            resolver.registerMacros(template);
        }

        JetGlobalContext context = engine.getGlobalContext();
        context.set("version", Optional.ofNullable(appVersion).orElse("v1.0"));
        context.set("enableCdn", Optional.ofNullable(appEnableCdn).orElse(false));


        TaleConst.ENABLED_CDN = Optional.ofNullable(appEnableCdn).orElse(false);
        TaleConst.MAX_FILE_SIZE = Optional.ofNullable(appMaxFileSize).orElse(20480);

        TaleConst.OPTIONS.putAll(optionsService.getOptions());
        String ips = TaleConst.OPTIONS.getOrDefault(Types.BLOCK_IPS, "");
        if (StringUtils.hasText(ips)) {
            TaleConst.BLOCK_IPS.addAll(Arrays.asList(ips.split(",")));
        }
        if (Files.exists(Paths.get(CLASSPATH + "install.lock"))) {
            TaleConst.INSTALLED = Boolean.TRUE;
        }

        String rememberToken = optionsService.getOption(OPTION_SAFE_REMEMBER_ME);
        if (StringUtils.hasText(rememberToken)) {
            RememberMe rememberMe = JsonUtils.formJson(rememberToken, RememberMe.class);
            TaleConst.REMEMBER_TOKEN = rememberMe.getToken();
        }

        BaseController.THEME = "themes/" + Commons.site_option("site_theme");

        return engine;
    }

}
