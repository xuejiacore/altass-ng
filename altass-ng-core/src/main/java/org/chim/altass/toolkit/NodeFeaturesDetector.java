package org.chim.altass.toolkit;

import com.alibaba.fastjson.JSON;
import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.annotation.RuntimeAutowired;
import org.chim.altass.core.executor.AbstractStreamNodeExecutor;
import org.chim.altass.core.executor.io.FileInputStreamExecutor;
import org.chim.altass.core.executor.io.FileOutputStreamExecutor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class Name: NodeFeaturesDetector
 * Create Date: 11/11/18 4:36 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 节点特性分析工具
 */
public class NodeFeaturesDetector {

    /**
     * 节点的完整特性分析
     *
     * @param clz 需要分析的节点信息
     * @return 节点完整的特性分析
     */
    public static Map<String, Object> fullAnalyze(Class<? extends AbstractStreamNodeExecutor> clz) {
        Map<String, Object> info = new HashMap<>();

        Map<String, Object> configOptions = configOptions(clz);
        Map<String, Object> features = features(clz);

        info.put("configOptions", configOptions);
        info.put("features", features);

        return info;
    }

    /**
     * 节点的参数分析
     *
     * @param clz 需要获取参数信息的节点
     * @return 节点参数，包含参数类型、是否必要、是否参与参数表达式处理等
     */
    public static Map<String, Object> configOptions(Class<? extends AbstractStreamNodeExecutor> clz) {
        Field[] declaredFields = clz.getDeclaredFields();
        Map<String, Object> configs = new TreeMap<>();
        for (Field declaredField : declaredFields) {
            RuntimeAutowired annotation = declaredField.getAnnotation(RuntimeAutowired.class);
            if (annotation != null) {
                Map<String, Object> info = new TreeMap<>();
                info.put("required", annotation.required());
                info.put("source", annotation.source());
                info.put("analyzable", annotation.analyzable());
                info.put("clz", declaredField.getType());
                configs.put(declaredField.getName(), info);
            }
        }
        return configs;
    }

    /**
     * 节点的执行特性分析
     *
     * @param clz 需要分析的执行节点
     * @return 节点的执行特性，包含支持的能力、名称等信息
     */
    public static Map<String, Object> features(Class<? extends AbstractStreamNodeExecutor> clz) {
        Map<String, Object> featureMap = new TreeMap<>();
        Executable executableInfo = clz.getAnnotation(Executable.class);

        Map<String, Object> info = new HashMap<>();
        if (executableInfo != null) {
            String[] ability = executableInfo.ability();
            boolean assemble = executableInfo.assemble();
            String name = executableInfo.name();
            info.put("name", name);
            info.put("ability", ability);
            info.put("assemble", assemble);
        }
        featureMap.put("executable", info);

        Resource resInfo = clz.getAnnotation(Resource.class);
        info = new HashMap<>();
        if (resInfo != null) {
            info.put("name", resInfo.name());
            info.put("clazz", resInfo.clazz());
            info.put("entry", resInfo.entry());
            info.put("groupName", resInfo.groupName());
            info.put("smallImage", resInfo.smallImage());
            info.put("midImage", resInfo.midImage());
            info.put("bigImage", resInfo.bigImage());
        }
        featureMap.put("resource", info);
        return featureMap;
    }

    public static void main(String[] args) {
        Map<String, Object> fullAnalyze = NodeFeaturesDetector.fullAnalyze(FileOutputStreamExecutor.class);
        System.err.println(JSON.toJSONString(fullAnalyze));
    }
}
