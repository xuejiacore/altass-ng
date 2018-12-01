package org.chim.altass.core.executor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.chim.altass.core.annotation.AltassAutowired;
import org.chim.altass.core.constant.DataStructure;
import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.constant.StreamEvent;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.config.ConvergentConfig;
import org.chim.altass.toolkit.script.JsonHelper;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class Name: AbstractConvergentExecutor
 * Create Date: 11/23/18 8:10 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 数据合并型节点（往往与数据拆解型节点组合使用{@link AbstractDisassemblyExecutor}），可以将直接或间接前驱节点包含数据拆解型节点的拆
 * 解数据，在整个流式处理后进行数据合并，最终达到 n:1 的目标输出，一般可以用于数据聚合操作
 * <p>
 * 节点架构：
 * 节点自身是一个流式处理的节点
 * <p>
 * TODO：该节点不支持数据分片
 *
 * @see AbstractDisassemblyExecutor
 * @see AbstractStreamNodeExecutor
 * @see AbstractNodeExecutor
 */
public abstract class AbstractConvergentExecutor extends AbstractStreamNodeExecutor {

    @AltassAutowired
    protected ConvergentConfig convergentConfig = null;

    protected ConcurrentHashMap<String, JSONObject> jsonObjs = new ConcurrentHashMap<>();
    protected ConcurrentHashMap<String, JSONArray> jsonArrays = new ConcurrentHashMap<>();

    /**
     * To initialize streaming executor.
     *
     * @param executeId execute id
     */
    public AbstractConvergentExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }

    @Override
    public void onStreamProcessing(StreamData data) throws ExecuteException {
        byte event = data.getEvent();
        byte dataType = data.getDataType();
        String groupKey = data.getGroupKey();
        Object streamDataObj = data.getData();
        if (event == StreamEvent.EVENT_GROUP_DATA) {
            if (dataType == DataStructure.JSON) {

                JSONObject jsonObj = JSON.parseObject(String.valueOf(streamDataObj));
                Set<String> jsonKeys = jsonObj.keySet();

                for (String jsonKey : jsonKeys) {
                    if ("]L".equalsIgnoreCase(jsonKey)) {
                        // 如果被合并对象是一个list，那么也只能被重新合并为list，根据合并所需的键值取值合并
                        JSONArray cacheData = jsonArrays.get(groupKey);
                        if (cacheData == null) {
                            cacheData = new JSONArray();
                            jsonArrays.put(groupKey, cacheData);
                        }
                        JSONObject requireMergeData = jsonObj.getJSONObject("]L");
                        // 根据表达式获取对应的json值
                        cacheData.add(rebuild(requireMergeData, true));
                    } else {
                        // 如果被合并的对象是一个map对象，那么允许指定key进行新map的合并；
                        JSONObject cacheData = jsonObjs.get(groupKey);
                        if (cacheData == null) {
                            cacheData = new JSONObject();
                            jsonObjs.put(groupKey, cacheData);
                        }
                        JSONObject requireMergeData = jsonObj.getJSONObject(jsonKey);
                        // 根据表达式获取对应的json值，放到对应的jsonKey字段中
                        cacheData.put(jsonKey, rebuild(requireMergeData, false));
                    }
                }
            }
        } else {
            System.err.println("JSON:" + JSON.toJSONString(data));
        }
        postFinished();
    }

    @Override
    protected void onGroupFinish(StreamData coveredData) throws ExecuteException {
        // 数据合并分组结束的消息
        String groupKey = coveredData.getGroupKey();
        // 推送数据
        StreamData streamData = new StreamData(this.executeId, StreamEvent.EVENT_DATA,
                JSON.toJSONString(jsonArrays.get(groupKey)));
        streamData.setDataType(DataStructure.JSON);

        this.pushData(streamData);
        jsonArrays.remove(groupKey);
        postFinished();
    }

    /**
     * 根据指定的参数重建json对象，如果是数组类型，那么重建后是一个单纯的对象
     *
     * @param srcObj  原json Obj
     * @param isArray true 则为数组，false为JsonObj
     * @return 处理后的json数据
     * @throws ExecuteException -
     */
    protected Object rebuild(JSONObject srcObj, boolean isArray) throws ExecuteException {
        if (this.convergentConfig == null) {
            return srcObj;
        }
        Map<String, String> mergeMap = this.convergentConfig.getMergeMap();
        if (mergeMap != null && !mergeMap.isEmpty()) {
            JSONObject jsonObject = new JSONObject();
            try {
                for (String newObjKey : mergeMap.keySet()) {
                    Object data = JsonHelper.jsonget(srcObj, mergeMap.get(newObjKey));

                    if (isArray) {
                        return data;
                    }
                    jsonObject.put(newObjKey, data);
                }
            } catch (Exception e) {
                throw new ExecuteException(e);
            }
            return jsonObject;
        } else {
            return srcObj;
        }
    }
}
