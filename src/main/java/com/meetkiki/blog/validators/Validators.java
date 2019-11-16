//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.meetkiki.blog.validators;

import com.meetkiki.blog.utils.PatternUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Validators {
    private static final Map<String, String> I18N_MAP = new HashMap();
    private static String i18nPrefix = "EN_";

    public static void useChinese() {
        i18nPrefix = "CN_";
    }

    public static <T> Validation<T> notNull() {
        return notNull((String)I18N_MAP.get(i18nPrefix + "NOT_NULL"));
    }

    public static <T> Validation<T> notNull(String msg) {
        return SimpleValidation.from(Objects::nonNull, msg);
    }

    public static Validation<String> notEmpty() {
        return notEmpty((String)I18N_MAP.get(i18nPrefix + "NOT_EMPTY"));
    }

    public static Validation<String> notEmpty(String msg) {
        return SimpleValidation.from((s) -> {
            return null != s && !s.isEmpty();
        }, msg);
    }

    public static Validation<String> moreThan(int size) {
        return notEmpty().and(moreThan(size, (String)I18N_MAP.get(i18nPrefix + "MORE_THAN")));
    }

    public static Validation<String> moreThan(int size, String msg) {
        return notEmpty().and(SimpleValidation.from((s) -> {
            return s.length() >= size;
        }, String.format(msg, size)));
    }

    public static Validation<String> lessThan(int size) {
        return lessThan(size, (String)I18N_MAP.get(i18nPrefix + "LESS_THAN"));
    }

    public static Validation<String> lessThan(int size, String msg) {
        return notEmpty().and(SimpleValidation.from((s) -> {
            return s.length() <= size;
        }, String.format(msg, size)));
    }

    public static Validation<String> length(int minSize, int maxSize) {
        return moreThan(minSize).and(lessThan(maxSize));
    }

    public static Validation<String> contains(String c) {
        return contains(c, (String)I18N_MAP.get(i18nPrefix + "CONTAINS"));
    }

    public static Validation<String> contains(String c, String msg) {
        return notEmpty().and(SimpleValidation.from((s) -> {
            return s.contains(c);
        }, String.format(msg, c)));
    }

    public static Validation<Integer> lowerThan(int max) {
        return lowerThan(max, (String)I18N_MAP.get(i18nPrefix + "LOWER_THAN"));
    }

    public static Validation<Integer> lowerThan(int max, String msg) {
        return SimpleValidation.from((i) -> {
            return i < max;
        }, String.format(msg, max));
    }

    public static Validation<Integer> greaterThan(int min) {
        return greaterThan(min, (String)I18N_MAP.get(i18nPrefix + "GREATER_THAN"));
    }

    public static Validation<Integer> greaterThan(int min, String msg) {
        return SimpleValidation.from((i) -> {
            return i > min;
        }, String.format(msg, min));
    }

    public static Validation<Integer> range(int min, int max) {
        return greaterThan(min).and(lowerThan(max));
    }

    public static Validation<String> isEmail() {
        return isEmail((String)I18N_MAP.get(i18nPrefix + "IS_EMAIL"));
    }

    public static Validation<String> isEmail(String msg) {
        return notEmpty().and(SimpleValidation.from(PatternUtils::isEmail, msg));
    }

    public static Validation<String> isURL() {
        return isURL((String)I18N_MAP.get(i18nPrefix + "IS_URL"));
    }

    public static Validation<String> isURL(String msg) {
        return notEmpty().and(SimpleValidation.from(PatternUtils::isURL, msg));
    }

    private Validators() {
    }

    static {
        I18N_MAP.put("CN_NOT_NULL", "不允许为 NULL");
        I18N_MAP.put("CN_NOT_EMPTY", "不允许为空");
        I18N_MAP.put("CN_MORE_THAN", "必须大于等于 %s 个字符");
        I18N_MAP.put("CN_LESS_THAN", "必须小于等于 %s 个字符");
        I18N_MAP.put("CN_CONTAINS", "必须包含 %s 字符");
        I18N_MAP.put("CN_LOWER_THAN", "必须小于 %s");
        I18N_MAP.put("CN_GREATER_THAN", "必须大于 %s");
        I18N_MAP.put("CN_IS_EMAIL", "不是一个合法的邮箱");
        I18N_MAP.put("CN_IS_URL", "不是一个合法的URL");
        I18N_MAP.put("EN_NOT_NULL", "must not be null.");
        I18N_MAP.put("EN_NOT_EMPTY", "must not be empty.");
        I18N_MAP.put("EN_MORE_THAN", "must have more than %s chars.");
        I18N_MAP.put("EN_LESS_THAN", "must have less than %s chars.");
        I18N_MAP.put("EN_CONTAINS", "must contain %s");
        I18N_MAP.put("EN_LOWER_THAN", "must be lower than %s.");
        I18N_MAP.put("EN_GREATER_THAN", "must be greater than %s.");
        I18N_MAP.put("EN_IS_EMAIL", "must be a email.");
        I18N_MAP.put("EN_IS_URL", "must be a url.");
    }
}
