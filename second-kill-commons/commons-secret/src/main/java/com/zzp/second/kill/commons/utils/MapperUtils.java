package com.zzp.second.kill.commons.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 佐斯特勒
 * @Description 处理Json的工具类
 * @Date 2020/1/4
 * @Time 1:09
 * @ClassName MapperUtils
 **/
public class MapperUtils {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getInstance() {
        return objectMapper;
    }

    /**
     * 转换为 JSON 字符串
     *
     * @param obj 对象
     * @return json数据
     * @throws Exception 无法转换
     */
    public static String obj2json(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * 转换为 JSON 字符串，忽略空值
     *
     * @param obj 对象
     * @return 无控制的json数据
     * @throws Exception 无法转换
     */
    public static String obj2jsonIgnoreNull(Object obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(obj);
    }

    /**
     * 转换为 JavaBean
     *
     * @param jsonString json字符串
     * @param clazz bean的反射对象
     * @return 对应的bean
     * @throws Exception 无法转换
     */
    public static <T> T json2pojo(String jsonString, Class<T> clazz) throws Exception {
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return objectMapper.readValue(jsonString, clazz);
    }

    /**
     * 字符串转换为 Map<String, Object>
     *
     * @param jsonString json字符串
     * @return Map
     * @throws Exception 无法转换
     */
    public static <T> Map<String, Object> json2map(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.readValue(jsonString, Map.class);
    }

    /**
     * 字符串转换为 Map<String, T>
     * @param jsonString json字符串
     * @param clazz bean的反射对象
     * @return Map<String, T>
     * @throws Exception 无法转换
     */
    public static <T> Map<String, T> json2map(String jsonString, Class<T> clazz) throws Exception {
        Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) objectMapper.readValue(jsonString, new TypeReference<Map<String, T>>() {
        });
        Map<String, T> result = new HashMap<String, T>();
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
        }
        return result;
    }

    /**
     * 深度转换 JSON 成 Map
     *
     * @param jsonString json字符串
     * @return Map<String, Object>
     */
    public static Map<String, Object> json2mapDeeply(String jsonString) throws Exception {
        return json2MapRecursion(jsonString, objectMapper);
    }

    /**
     * 把 JSON 解析成 List，如果 List 内部的元素存在 jsonString，继续解析
     *
     * @param json JSON字符串
     * @param mapper 解析工具
     * @return List<Object>
     * @throws Exception 无法转换
     */
    private static List<Object> json2ListRecursion(String json, ObjectMapper mapper) throws Exception {
        if (json == null) {
            return null;
        }

        var list = mapper.readValue(json, List.class);

        for (Object obj : list) {
            if (obj instanceof String) {
                String str = (String) obj;
                if (str.startsWith("[")) {
                    obj = json2ListRecursion(str, mapper);
                } else if (obj.toString().startsWith("{")) {
                    obj = json2MapRecursion(str, mapper);
                }
            }
        }

        return list;
    }

    /**
     * 把 JSON 解析成 Map，如果 Map 内部的 Value 存在 jsonString，继续解析
     *
     * @param json json字符串
     * @param mapper json解析工具
     * @return Map<String, Object>
     * @throws Exception 无法转换
     */
    private static Map<String, Object> json2MapRecursion(String json, ObjectMapper mapper) throws Exception {
        if (json == null) {
            return null;
        }

        Map<String, Object> map = mapper.readValue(json, Map.class);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object obj = entry.getValue();
            if (obj != null && obj instanceof String) {
                String str = ((String) obj);

                if (str.startsWith("[")) {
                    List<?> list = json2ListRecursion(str, mapper);
                    map.put(entry.getKey(), list);
                } else if (str.startsWith("{")) {
                    Map<String, Object> mapRecursion = json2MapRecursion(str, mapper);
                    map.put(entry.getKey(), mapRecursion);
                }
            }
        }

        return map;
    }

    /**
     * 将 JSON 数组转换为集合
     *
     * @param jsonArrayStr json数组字符串
     * @param clazz list泛型对象
     * @return List<T>
     * @throws Exception 无法转换
     */
    public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz) throws Exception {
        JavaType javaType = getCollectionType(ArrayList.class, clazz);
        List<T> list = (List<T>) objectMapper.readValue(jsonArrayStr, javaType);
        return list;
    }


    /**
     * 获取泛型的 Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类
     * @return JavaType Java类型
     * @since 1.0
     */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * 将 Map 转换为 JavaBean
     *
     * @param map map对象
     * @param clazz 反射对象
     * @return map
     */
    public static <T> T map2pojo(Map map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }

    /**
     * 将 Map 转换为 JSON
     *
     * @param map map集合
     * @return JSON字符串
     */
    public static String mapToJson(Map map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将 JSON 对象转换为 JavaBean
     *
     * @param obj 对象
     * @param clazz JavaBean
     * @return T
     */
    public static <T> T obj2pojo(Object obj, Class<T> clazz) {
        return objectMapper.convertValue(obj, clazz);
    }

    /**
     * 将指定节点的 JSON 数据转换为 JavaBean
     * @param jsonString json字符串
     * @param treeNode 某个节点名
     * @param clazz 发射对象
     * @return T
     * @throws Exception 无法转换
     */
    public static <T> T json2pojoByTree(String jsonString, String treeNode, Class<T> clazz) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        JsonNode data = jsonNode.findPath(treeNode);
        return json2pojo(data.toString(), clazz);
    }

    /**
     * 将指定节点的 JSON 数组转换为集合
     *
     * @param jsonStr  JSON 字符串
     * @param treeNode 查找 JSON 中的节点
     * @return List<T>
     * @throws Exception 无法转换
     */
    public static <T> List<T> json2listByTree(String jsonStr, String treeNode, Class<T> clazz) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(jsonStr);
        JsonNode data = jsonNode.findPath(treeNode);
        return json2list(data.toString(), clazz);
    }
}

