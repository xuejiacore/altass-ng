/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.executor
 * Author: Xuejia
 * Date Time: 2016/12/19 13:58
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.executor;

import com.alibaba.fastjson.JSON;
import org.chim.altass.core.constant.EurekaSystemRedisKey;
import org.chim.altass.core.domain.meta.InputParam;
import org.chim.altass.core.domain.meta.MetaData;
import org.chim.altass.core.domain.meta.OutputParam;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.ext.ContextMap;
import org.chim.altass.core.ext.DMap;
import org.chim.altass.core.ext.ExtContextParams;
import org.chim.altass.toolkit.RedissonToolkit;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class Name: ExecuteContext
 * Create Date: 2016/12/19 13:58
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 执行器前驱后继执行时上下文信息
 */
public class ExecuteContext implements Serializable {

    private static final long serialVersionUID = 5827366218475574465L;
    /**
     * 当前节点的所有上一个执行器的上下文
     * 注：上一个执行器的上下文，在阻塞汇聚型的执行器中，会存在有多个fluxContext，如果是穿透型节点，那么fluxContext中
     * 只拥有一个映射数据。
     * [分布式对象]
     */
    private ContextMap fluxContext = null;

    /**
     * 当前节点的上一层执行器的上下文
     * [分布式对象]
     */
    private ContextMap topContextMap = null;

    /**
     * 当前执行器的输入参数
     */
    private ExtContextParams inputParam = null;
    /**
     * 当前执行器的输出参数
     */
    private ExtContextParams outputParam = null;

    /**
     * 当前上下文所属的执行器id
     */
    private String executorId = null;

    /**
     * 当前执行器的执行属性
     * <p>
     * 目前已经定义的属性有：
     * 当前节点的ID，父执行器的ID，下一节点数量，下一节点列表，是否是作业等内容
     * 执行器的属性键参照 {@link AbstractExecutor} 中的定义
     */
    private DMap<Integer, Object> attributes = null;

    private RedissonToolkit toolkit = null;

    public ExecuteContext() {
        this.toolkit = RedissonToolkit.getInstance();
    }

    /**
     * 创建一个执行器上下文对象
     */
    @SuppressWarnings("unchecked")
    public ExecuteContext(String executorId) {
        this.executorId = executorId;
        this.toolkit = RedissonToolkit.getInstance();

        attributes = new DMap<>(EurekaSystemRedisKey.EUREKA$SYSTEM_CONTEXT_ATTR, executorId);
        inputParam = new ExtContextParams(EurekaSystemRedisKey.EUREKA$SYSTEM_IN_PARAM, executorId);
        outputParam = new ExtContextParams(EurekaSystemRedisKey.EUREKA$SYSTEM_OUT_PARAM, executorId);
    }

    /**
     * 获得当前执行器实例的上一层执行器上下文列表
     *
     * @return 当前执行器的上一层执行器列表
     */
    @SuppressWarnings("unchecked")
    public ContextMap getTopContextMap() {
        if (topContextMap == null) {
            EurekaSystemRedisKey key = EurekaSystemRedisKey.EUREKA$SYSTEM_TOP_CONTEXT;
            RelatedContextWrapper defaultVal = new RelatedContextWrapper();
            topContextMap = new ContextMap(key, this.executorId);
        }
        return topContextMap;
    }

    /**
     * 根据键值获得上一层执行器的上下文
     *
     * @param key 键值
     * @return 上一层执行器的上下文
     */
    public ExecuteContext getTopContextByKey(String key) {
        return this.topContextMap == null ? null : this.topContextMap.get(key);
    }

    /**
     * 添加当前执行器实例的上一层上下文信息
     *
     * @param key     键值
     * @param context 上一层上下文信息
     */
    @SuppressWarnings("unchecked")
    public void addTopContext(String key, ExecuteContext context) {
        if (context != null) {
            if (this.topContextMap == null) {
                EurekaSystemRedisKey redisKey = EurekaSystemRedisKey.EUREKA$SYSTEM_TOP_CONTEXT;
                RelatedContextWrapper defaultVal = new RelatedContextWrapper();
                topContextMap = new ContextMap(redisKey, this.executorId);
            }
            this.topContextMap.put(key, context);
        }
    }

    public void setTopContextMap(Map<String, ExecuteContext> topContextMap) {
        if (topContextMap != null) {
            if (this.topContextMap == null) {
                this.topContextMap = new ContextMap(EurekaSystemRedisKey.EUREKA$SYSTEM_TOP_CONTEXT, this.executorId);
            }
            this.topContextMap.putAll(topContextMap);
        }
    }

    public void setFluxContext(Map<String, ExecuteContext> fluxContext) {
        if (fluxContext != null) {
            if (this.fluxContext == null) {
                this.fluxContext = new ContextMap(EurekaSystemRedisKey.EUREKA$SYSTEM_FLUX_CONTEXT, this.executorId);
            }
            this.fluxContext.putAll(fluxContext);
        }
    }

    /**
     * 获得当前节点/作业的上一个执行器的上下文信息
     * <p>
     * <font color='yellow'>注：如果是阻塞型节点，通过这个方法可以获得所有到达本节点对应的上下文信息，<br/>
     * 但如果是通量穿透型的节点，那么在lastContextMap中只会存在上一次穿透通过本节点的上下文信息</font>
     *
     * @return 当前节点/作业的上一个执行器的上下文信息
     */
    @SuppressWarnings("unchecked")
    public ContextMap getFluxContext() {
        if (fluxContext == null) {
            EurekaSystemRedisKey key = EurekaSystemRedisKey.EUREKA$SYSTEM_FLUX_CONTEXT;
            fluxContext = new ContextMap(key, this.executorId);
        }
        return fluxContext;
    }

    /**
     * 根据键获得当前节点的所有上一个节点上下文信息
     * <p>
     * <font color='yellow'>注：如果是阻塞型节点，通过这个方法可以获得所有到达本节点对应的上下文信息，<br/>
     * 但如果是通量穿透型的节点，那么在lastContextMap中只会存在上一次穿透通过本节点的上下文信息</font>
     *
     * @param key 需要获取的键
     * @return 返回键值对应的上一个节点/作业的上下文信息
     */
    public ExecuteContext getFluxContextByKey(String key) {
        return this.fluxContext.get(key);
    }

    /**
     * 添加当前节点的上一个上下文信息
     *
     * @param key     需要添加的键
     * @param context 需要添加的上下文信息
     */
    @SuppressWarnings("unchecked")
    public void addFluxContext(String key, ExecuteContext context) {
        if (this.fluxContext == null) {
            EurekaSystemRedisKey redisKey = EurekaSystemRedisKey.EUREKA$SYSTEM_FLUX_CONTEXT;
            fluxContext = new ContextMap(redisKey, this.executorId);
        }
        this.fluxContext.put(key, context);
    }

    /**
     * 向执行器上下文中添加属性
     * <p>
     * <font>只能够使用二进制[16:]后的值作为属性的键，[0:15]为保留键</font>
     *
     * @param key       属性的名称
     * @param attribute 属性值
     * @return 返回当前执行器属性数量
     */
    public int addAttribute(Integer key, Object attribute) throws ExecuteException {
        if (key > 0xFFFF) {
            throw new ExecuteException(new IllegalArgumentException("必须使用保留键之外的值作为属性键"));
        } else {
            if (attribute != null) {
                this.attributes.put(key, attribute);
            }
        }
        return this.attributes.size();
    }

    /**
     * 获得当前执行器的输入参数
     *
     * @return 当前执行器的输入参数
     */
    public InputParam getInputParam() {
        if (inputParam == null) {
            return null;
        }
        return inputParam.buildInputParams();
    }

    /**
     * 设置输入参数
     *
     * @param inputParam 需要设置的输入参数
     */
    public void setInputParam(InputParam inputParam) {
        this.inputParam.setParams(inputParam, null);
    }

    /**
     * 添加输入参数
     *
     * @param param 需要添加的输入参数
     */
    @SuppressWarnings("unchecked")
    public void addInputParam(MetaData param) {
        InputParam inputParamObj = this.inputParam.buildInputParams();
        inputParamObj.addParameter(param);
        this.inputParam.setParams(inputParamObj, null);
    }

    /**
     * 获得当前执行器的输出参数
     *
     * @return 当前执行器的输出参数
     */
    public OutputParam getOutputParam() {
        return outputParam.buildOutputParams();
    }

    /**
     * 设置当前执行器的输出参数
     *
     * @param outputParam 需要设置的输出参数
     */
    public void setOutputParam(OutputParam outputParam) {
        this.outputParam.setParams(null, outputParam);
    }

    /**
     * 添加输出参数
     *
     * @param param 需要添加的输出参数
     */
    @SuppressWarnings("unchecked")
    public void addOutputParam(MetaData param) {
        OutputParam outputParamObj = this.outputParam.buildOutputParams();
        outputParamObj.addParameter(param);
        this.outputParam.setParams(null, outputParamObj);
    }

    public void addAllOutputParams(List<MetaData> params) {
        this.outputParam.addAll(params);
    }

    /**
     * 获得当前执行器的执行属性
     *
     * @return 当前执行器的执行属性
     */
    public Map<Integer, Object> getAttributes() {
        return attributes;
    }

    /**
     * 设置当前执行器的执行属性
     * <font>只能够使用二进制[16:]后的值作为属性的键，[0:15]为保留键</font>
     *
     * @param attributes 执行属性
     */
    public void setAttributes(Map<Integer, Object> attributes) throws ExecuteException {
        for (Integer key : attributes.keySet()) {
            if (key > 0xFFFF) {
                throw new ExecuteException(new IllegalArgumentException("必须使用保留键之外的值作为属性键"));
            }
        }
        this.attributes = new DMap<>(EurekaSystemRedisKey.EUREKA$SYSTEM_CONTEXT_ATTR, executorId, attributes);
    }

    /**
     * 根据键获得上下文属性
     *
     * @param key 需要获得的键
     * @return 返回上下文属性中的指定键对应的值，如果不存在，返回值为null
     */
    public Object getAttribute(Integer key) {
        return this.attributes.get(key);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(buildMapObj());
    }

    public Map buildMapObj() {
        Map<Object, Object> data = new HashMap<>();
        data.put("attributes", attributes.build());
        data.put("outputParam", outputParam == null ? null : outputParam.buildOutputParams());
        data.put("inputParam", inputParam == null ? null : inputParam.buildInputParams());
        data.put("fluxContext", fluxContext == null ? null : fluxContext.build());
        data.put("topContextMap", topContextMap == null ? null : topContextMap.build());
        return data;
    }
}
