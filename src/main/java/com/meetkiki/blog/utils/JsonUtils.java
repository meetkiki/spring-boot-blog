package com.meetkiki.blog.utils;

import com.alibaba.fastjson.JSON;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Type;

/**
 * Json kit
 *
 * @author biezhi
 * 2017/6/2
 */
@UtilityClass
public class JsonUtils {

    public static String toString(Object object) {
        return JSON.toJSONString(object);
    }

    public static <T> T formJson(String json, Class<T> clz) {
        return JSON.parseObject(json,clz);
    }

}