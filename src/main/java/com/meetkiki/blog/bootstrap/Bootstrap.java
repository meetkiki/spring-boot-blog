package com.meetkiki.blog.bootstrap;

import com.meetkiki.blog.controller.BaseController;
import com.meetkiki.blog.extension.Commons;
import com.meetkiki.blog.model.dto.RememberMe;
import com.meetkiki.blog.model.dto.Types;
import com.meetkiki.blog.service.SiteService;
import com.meetkiki.blog.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Tale初始化进程
 *
 * @author biezhi
 */
@Component
public class Bootstrap implements BladeLoader {

    @Autowired
    private SiteService siteService;

    @Override
    public void preLoad(Blade blade) {


        Validators.useChinese();

        boolean devMode = true;
        if (blade.environment().hasKey("app.dev")) {
            devMode = blade.environment().getBoolean("app.dev", true);
        }
        if (blade.environment().hasKey("app.devMode")) {
            devMode = blade.environment().getBoolean("app.devMode", true);
        }
        SqliteJdbc.importSql(devMode);
        Anima.open(SqliteJdbc.DB_SRC);
        Commons.setSiteService(siteService);
    }

    @Override
    public void load(Blade blade) {
        JetbrickTemplateEngine templateEngine = new JetbrickTemplateEngine();

        List<String> macros = new ArrayList<>(8);
        macros.add(File.separatorChar + "comm" + File.separatorChar + "macros.html");
        // 扫描主题下面的所有自定义宏
        String themeDir = CLASSPATH + "templates" + File.separatorChar + "themes";
        File[] dir      = new File(themeDir).listFiles();
        if (null != dir) {
            for (File f : dir) {
                if (f.isDirectory() && Files.exists(Paths.get(f.getPath() + File.separatorChar + "macros.html"))) {
                    String macroName = File.separatorChar + "themes" + File.separatorChar + f.getName() + File.separatorChar + "macros.html";
                    macros.add(macroName);
                }
            }
        }

        StringBuffer sbuf = new StringBuffer();
        macros.forEach(s -> sbuf.append(',').append(s));
        templateEngine.addConfig("jetx.import.macros", sbuf.substring(1));

        GlobalResolver resolver = templateEngine.getGlobalResolver();
        resolver.registerFunctions(Commons.class);
        resolver.registerFunctions(Theme.class);
        resolver.registerFunctions(AdminCommons.class);
        resolver.registerTags(JetTag.class);

        JetGlobalContext context = templateEngine.getGlobalContext();
        context.set("version", environment.get("app.version", "v1.0"));
        context.set("enableCdn", environment.getBoolean("app.enableCdn", false));

        blade.templateEngine(templateEngine);

        TaleConst.ENABLED_CDN = environment.getBoolean("app.enableCdn", false);
        TaleConst.MAX_FILE_SIZE = environment.getInt("app.max-file-size", 20480);

        TaleConst.OPTIONS.addAll(optionsService.getOptions());
        String ips = TaleConst.OPTIONS.get(Types.BLOCK_IPS, "");
        if (StringUtils.hasText(ips)) {
            TaleConst.BLOCK_IPS.addAll(Arrays.asList(ips.split(",")));
        }
        if (Files.exists(Paths.get(CLASSPATH + "install.lock"))) {
            TaleConst.INSTALLED = Boolean.TRUE;
        }

        String rememberToken = optionsService.getOption(OPTION_SAFE_REMEMBER_ME);
        if (StringUtils.hasText(rememberToken)) {
            RememberMe rememberMe = JsonKit.formJson(rememberToken, RememberMe.class);
            TaleConst.REMEMBER_TOKEN = rememberMe.getToken();
        }

        BaseController.THEME = "themes/" + Commons.site_option("site_theme");

        TaleConst.BCONF = environment;
    }
}
