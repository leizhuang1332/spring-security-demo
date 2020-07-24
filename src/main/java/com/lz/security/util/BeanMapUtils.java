package com.lz.security.util;

import org.springframework.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanMapUtils {

    /**
     * 将对象属性转化为map结合
     */
    @SuppressWarnings("unchecked")
    public static <T, K, V> Map<K, V> beanToMap(T bean) {
        Map<K, V> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            beanMap.keySet().forEach(key -> map.put((K) key, (V) beanMap.get(key)));
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <T, K, V> Map<K, V> beanToMap(T bean, List ignore) {
        Map<K, V> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            beanMap.keySet().stream()
                    .filter(key -> !ignore.contains(key))
                    .forEach(key -> map.put((K) key, (V) beanMap.get(key)));
        }
        return map;
    }

    /**
     * 将map集合中的数据转化为指定对象的同名属性中
     */
    public static <T> T mapToBean(Map map, Class<T> clazz) throws Exception {
        T bean = clazz.newInstance();
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    @SuppressWarnings("unchecked")
    public static <T> T mapToBean(Map map, Class<T> clazz, List ignore) throws Exception {
        T bean = clazz.newInstance();
        BeanMap beanMap = BeanMap.create(bean);
        map.keySet().stream()
                .filter(key -> !ignore.contains(key))
                .forEach(key -> beanMap.put(key, map.get(key)));
        return bean;
    }
}