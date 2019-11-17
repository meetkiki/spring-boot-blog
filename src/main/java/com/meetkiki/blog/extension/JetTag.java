package com.meetkiki.blog.extension;

import jetbrick.template.JetAnnotations;
import jetbrick.template.runtime.JetTagContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 主题公共标签
 * <p>
 * Created by biezhi on 2017/2/23.
 */
@JetAnnotations.Functions
public class JetTag {

    public static void social(JetTagContext ctx, String name) throws IOException {
        String value = Commons.site_option("social_" + name);
        if (StringUtils.hasText(value)) {
            value = ctx.getBodyContent();
        }
        ctx.getWriter().print(value.toString());
    }

}
