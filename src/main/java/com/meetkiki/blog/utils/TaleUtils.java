package com.meetkiki.blog.utils;

import com.meetkiki.blog.constants.TaleConst;
import com.meetkiki.blog.extension.Commons;
import com.meetkiki.blog.extension.Theme;
import com.meetkiki.blog.model.dto.RememberMe;
import com.meetkiki.blog.model.entity.Contents;
import com.meetkiki.blog.model.entity.Options;
import com.meetkiki.blog.model.entity.Users;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Content;
import com.sun.syndication.feed.rss.Item;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.WireFeedOutput;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.meetkiki.blog.constants.TaleConst.*;
import static io.github.biezhi.anima.Anima.select;
import static io.github.biezhi.anima.Anima.update;

/**
 * Tale工具类
 * <p>
 * Created by biezhi on 2017/2/21.
 */
public class TaleUtils {

    /**
     * 一周
     */
    private static final int ONE_MONTH = 7 * 24 * 60 * 60;

    /**
     * 匹配邮箱正则
     */
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern SLUG_REGEX = Pattern.compile("^[A-Za-z0-9_-]{3,50}$", Pattern.CASE_INSENSITIVE);

    /**
     * 设置记住密码 cookie
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, Integer uid) {
        boolean isSSL = Commons.site_url().startsWith("https");

        String     token      = EncryptUtils.md5(UUID.UU64());
        RememberMe rememberMe = new RememberMe();
        rememberMe.setUid(uid);
        rememberMe.setExpires(DateUtils.nowUnix() + ONE_MONTH);
        rememberMe.setRecentIp(Collections.singletonList(IpUtil.getIpAddr(request)));
        rememberMe.setToken(token);

        long count = select().from(Options.class).where(Options::getName, OPTION_SAFE_REMEMBER_ME).count();
        if (count == 0) {
            Options options = new Options();
            options.setName(OPTION_SAFE_REMEMBER_ME);
            options.setValue(JsonUtils.toString(rememberMe));
            options.setDescription("记住我 Token");
            options.save();
        } else {
            update().from(Options.class).set(Options::getValue, JsonUtils.toString(rememberMe))
                    .where(Options::getName, OPTION_SAFE_REMEMBER_ME)
                    .execute();
        }

        Cookie cookie = new Cookie(REMEMBER_IN_COOKIE,token);
        cookie.setHttpOnly(true);
        cookie.setSecure(isSSL);
        cookie.setMaxAge(ONE_MONTH);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static Cookie getCookie(HttpServletRequest request,String cookieName){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().endsWith(REMEMBER_IN_COOKIE)){
                return cookie;
            }
        }
        return null;
    }

    public static Integer getCookieUid(HttpServletRequest request) {
        Cookie cookie = getCookie(request, REMEMBER_IN_COOKIE);
        if (cookie == null){
            return null;
        }
        String rememberToken = cookie.getValue();
        if (null == rememberToken || rememberToken.isEmpty() || REMEMBER_TOKEN.isEmpty()) {
            return null;
        }
        if (!REMEMBER_TOKEN.equals(rememberToken)) {
            return null;
        }
        Options options = select().from(Options.class).where(Options::getName, OPTION_SAFE_REMEMBER_ME).one();
        if (null == options) {
            return null;
        }
        RememberMe rememberMe = JsonUtils.formJson(options.getValue(), RememberMe.class);
        if (rememberMe.getExpires() < DateUtils.nowUnix()) {
            return null;
        }
        if (!rememberMe.getRecentIp().contains(IpUtil.getIpAddr(request))) {
            return null;
        }
        return rememberMe.getUid();
    }

    public static String bodyToString(HttpServletRequest request){
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try (
                InputStream inputStream = request.getInputStream();
        ){
            if (inputStream != null ) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 返回当前登录用户
     */
    public static Users getLoginUser() {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        if (null == session) {
            return null;
        }
        return (Users) session.getAttribute(TaleConst.LOGIN_SESSION_KEY);
    }


    /**
     * markdown转换为html
     */
    public static String mdToHtml(String markdown) {
        if (!StringUtils.hasText(markdown)) {
            return "";
        }

        List<Extension> extensions = Collections.singletonList(TablesExtension.create());
        Parser          parser     = Parser.builder().extensions(extensions).build();
        Node            document   = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .attributeProviderFactory(context -> new LinkAttributeProvider())
                .extensions(extensions).build();

        String content = renderer.render(document);
        content = Commons.emoji(content);

        // 支持网易云音乐输出
        if (Boolean.parseBoolean(OPTIONS.getOrDefault(ENV_SUPPORT_163_MUSIC, "true")) && content.contains(MP3_PREFIX)) {
            content = content.replaceAll(MUSIC_REG_PATTERN, MUSIC_IFRAME);
        }
        // 支持gist代码输出
        if (Boolean.parseBoolean(OPTIONS.getOrDefault(ENV_SUPPORT_GIST, "true")) && content.contains(GIST_PREFIX_URL)) {
            content = content.replaceAll(GIST_REG_PATTERN, GIST_REPLATE_PATTERN);
        }
        return content;
    }

    static class LinkAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            if (node instanceof Link) {
                attributes.put("target", "_blank");
            }
            if (node instanceof org.commonmark.node.Image) {
                attributes.put("title", attributes.get("alt"));
            }
        }
    }

    /**
     * 提取html中的文字
     */
    public static String htmlToText(String html) {
        if (StringUtils.hasText(html)) {
            return html.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
        }
        return "";
    }

    /**
     * 判断文件是否是图片类型
     */
    public static boolean isImage(File imageFile) {
        if (!imageFile.exists()) {
            return false;
        }
        try {
            Image img = ImageIO.read(imageFile);
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断是否是邮箱
     */
    public static boolean isEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    /**
     * 判断是否是合法路径
     */
    public static boolean isPath(String slug) {
        if (StringUtils.hasText(slug)) {
            if (slug.contains("/") || slug.contains(" ") || slug.contains(".")) {
                return false;
            }
            Matcher matcher = SLUG_REGEX.matcher(slug);
            return matcher.find();
        }
        return false;
    }

    /**
     * 获取RSS输出
     */
    public static String getRssXml(List<Contents> articles) throws FeedException {
        Channel channel = new Channel("rss_2.0");
        channel.setTitle(TaleConst.OPTIONS.getOrDefault("site_title", ""));
        channel.setLink(Commons.site_url());
        channel.setDescription(TaleConst.OPTIONS.getOrDefault("site_description", ""));
        channel.setLanguage("zh-CN");
        List<Item> items = new ArrayList<>();
        articles.forEach(post -> {
            Item item = new Item();
            item.setTitle(post.getTitle());
            Content content = new Content();
            String  value   = Theme.article(post.getContent());

            char[] xmlChar = value.toCharArray();
            for (int i = 0; i < xmlChar.length; ++i) {
                if (xmlChar[i] > 0xFFFD) {
                    //直接替换掉0xb
                    xmlChar[i] = ' ';
                } else if (xmlChar[i] < 0x20 && xmlChar[i] != 't' & xmlChar[i] != 'n' & xmlChar[i] != 'r') {
                    //直接替换掉0xb
                    xmlChar[i] = ' ';
                }
            }

            value = new String(xmlChar);

            content.setValue(value);
            item.setContent(content);
            item.setLink(Theme.permalink(post.getCid(), post.getSlug()));
            item.setPubDate(DateUtils.toDate(post.getCreated()));
            items.add(item);
        });
        channel.setItems(items);
        WireFeedOutput out = new WireFeedOutput();
        return out.outputString(channel);
    }

    private static final String SITEMAP_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<urlset xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd\" xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">";

    static class Url {
        String loc;
        String lastmod;

        public Url(String loc) {
            this.loc = loc;
        }
    }

    public static String getSitemapXml(List<Contents> articles) {
        List<Url> urls = articles.stream()
                .map(TaleUtils::parse)
                .collect(Collectors.toList());
        urls.add(new Url(Commons.site_url() + "/archives"));

        String urlBody = urls.stream()
                .map(url -> {
                    String s = "<url><loc>" + url.loc + "</loc>";
                    if (null != url.lastmod) {
                        s += "<lastmod>" + url.lastmod + "</lastmod>";
                    }
                    return s + "</url>";
                })
                .collect(Collectors.joining("\n"));

        return SITEMAP_HEAD + urlBody + "</urlset>";
    }

    private static Url parse(Contents contents) {
        Url url = new Url(Commons.site_url() + "/article/" + contents.getCid());
        url.lastmod = DateUtils.toString(contents.getModified(), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        return url;
    }

    /**
     * 替换HTML脚本
     */
    public static String cleanXSS(String value) {
        //You'll need to remove the spaces from the html entities below
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        return value;
    }

    /**
     * 获取某个范围内的随机数
     *
     * @param max 最大值
     * @param len 取多少个
     * @return
     */
    public static int[] random(int max, int len) {
        int values[] = new int[max];
        int temp1, temp2, temp3;
        for (int i = 0; i < values.length; i++) {
            values[i] = i + 1;
        }
        //随机交换values.length次
        for (int i = 0; i < values.length; i++) {
            temp1 = Math.abs(ThreadLocalRandom.current().nextInt()) % (values.length - 1); //随机产生一个位置
            temp2 = Math.abs(ThreadLocalRandom.current().nextInt()) % (values.length - 1); //随机产生另一个位置
            if (temp1 != temp2) {
                temp3 = values[temp1];
                values[temp1] = values[temp2];
                values[temp2] = temp3;
            }
        }
        return Arrays.copyOf(values, len);
    }

    /**
     * 将list转为 (1, 2, 4) 这样的sql输出
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> String listToInSql(List<T> list) {
        StringBuffer sbuf = new StringBuffer();
        list.forEach(item -> sbuf.append(',').append(item.toString()));
        sbuf.append(')');
        return '(' + sbuf.substring(1);
    }

    public static final String UP_DIR = CLASSPATH.substring(0, CLASSPATH.length() - 1);

    public static String getFileKey(String name) {
        String prefix = "/upload/" + DateUtils.toString(new Date(), "yyyy/MM");
        String dir    = UP_DIR + prefix;
        if (!Files.exists(Paths.get(dir))) {
            new File(dir).mkdirs();
        }
        return prefix + "/" + UUID.UU32() + "." + fileExt(name);
    }

    public static String fileExt(String fname) {
        if (StringUtils.isEmpty(fname) || fname.indexOf('.') == -1) {
            return "temp";
        }
        return fname.substring(fname.lastIndexOf('.') + 1);
    }

    public static String getFileName(String path) {
        File   tempFile = new File(path.trim());
        String fileName = tempFile.getName();

        return fileName;
    }

    public static String buildURL(String url) {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        if (!url.startsWith("http")) {
            url = "http://".concat(url);
        }
        return url;
    }
}
