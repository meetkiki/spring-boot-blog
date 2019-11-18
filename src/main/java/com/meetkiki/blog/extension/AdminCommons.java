package com.meetkiki.blog.extension;

import com.meetkiki.blog.constants.TaleConst;
import com.meetkiki.blog.model.dto.Types;
import jetbrick.template.JetAnnotations;
import org.springframework.stereotype.Component;

/**
 * 后台公共函数
 * <p>
 * Created by biezhi on 2017/2/21.
 */
@JetAnnotations.Functions
public class AdminCommons {

    public static String attachURL(){
        return Commons.site_option(Types.ATTACH_URL, Commons.site_url());
    }

    public static int maxFileSize(){
        return TaleConst.MAX_FILE_SIZE / 1024;
    }

    public static String cdnURL(){
        return Commons.site_option(Types.CDN_URL, "/static");
    }

    public static String siteTheme() {
        return Commons.site_theme();
    }

}
