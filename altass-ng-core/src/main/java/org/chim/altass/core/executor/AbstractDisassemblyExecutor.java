package org.chim.altass.core.executor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.chim.altass.core.annotation.AltassAutowired;
import org.chim.altass.core.constant.DataStructure;
import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.constant.StreamEvent;
import org.chim.altass.core.domain.IEntry;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.config.DisassemblyConfig;
import org.chim.altass.toolkit.script.JsonHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Class Name: AbstractDisassemblyExecutor
 * Create Date: 11/23/18 8:16 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 数据拆解型节点（往往与数据合并型节点组合使用{@link AbstractConvergentExecutor}），该拆解型节点允许对流式或非流式节点进行数据拆解，
 * 形成 1:n 的数据拆解迭代
 * <p>
 * 节点架构：
 * 自身是一个支持流式处理的节点，在获得直接上级输出后，可对数据进行组判定如果以组的结构组织数据，那么会允许进行数据拆解（组的概念可以是数
 * 组，也可以是一个数据对象的部分或全部字段的遍历），在形成这种可迭代的数据后，利用流式处理的节点特性，将数据逐步推送到下一节点中进行处理。
 * <p>
 * 当前节点的直接后继节点，必须是一个流式处理的节点，至于流式拆解后的数据是否需要被合并成一个输出，这个取决于当前拆解节点的直接或间接后继
 * 节点中，是否包含有合并型节点{@link AbstractConvergentExecutor}，可以通过合并型节点对拆解后的数据（并且中间流程已被流式处理完成）进
 * 行数据的合并归一化，从而形成  1:n → n:n streaming → n:1 的流数据处理
 *
 * @see AbstractConvergentExecutor
 * @see AbstractStreamNodeExecutor
 * @see AbstractNodeExecutor
 */
public abstract class AbstractDisassemblyExecutor extends AbstractStreamNodeExecutor {

    // Disassembly Config
    @AltassAutowired
    protected DisassemblyConfig disassemblyConfig = null;

    /**
     * To initialize streaming executor.
     *
     * @param executeId execute id
     */
    public AbstractDisassemblyExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }

    @Override
    protected boolean onChildInit() throws ExecuteException {
        return disassemblyConfig != null && StringUtils.isNotBlank(disassemblyConfig.getExpress());
    }

    /**
     * Processing streaming data, as a disassembly executor, current method was not allowed to be change. All disassembly
     * must process in a same program.
     *
     * @param data a json structure data from last streaming node.
     * @throws ExecuteException Some data structure not support now.
     */
    @Override
    public final void onStreamProcessing(StreamData data) throws ExecuteException {
        Object groupId = null;
        if (data.dtis(DataStructure.JSON)) {
            Object streamData = data.getData();
            if (streamData == null) {
                return;
            }
            // Process json data.
            String json = String.valueOf(streamData);
            try {
                Object jsonData = JsonHelper.jsonget(json, disassemblyConfig.getExpress());
                groupId = disassemblyConfig.getGroupId();
                boolean groupIdNoSpecified = groupId == null;
                groupId = groupIdNoSpecified ? null : JsonHelper.jsonget(json, String.valueOf(groupId));

                if (jsonData instanceof JSONArray) {
                    int idx = 0;
                    // Iterate all array item if json data structure is json array.
                    for (Object jsonObject : (JSONArray) jsonData) {
                        // use idx if is data is simple array
                        groupId = groupIdNoSpecified ? data.getUniqueId() + "_" + idx : groupId;
                        groupMode.set(StreamEvent.EVENT_GROUP_DATA);
                        groupKey.set(String.valueOf(groupId));
                        Map<String, Object> arrayData = new HashMap<>();
                        arrayData.put("]L", jsonObject);
                        StreamData pushData = new StreamData(this.executeId, JSON.toJSONString(arrayData));
                        pushData(pushData);
                        idx++;
                    }

                } else if (jsonData instanceof JSONObject) {
                    // Get each field of object if json data structure is json object.
                    for (String key : ((JSONObject) jsonData).keySet()) {
                        groupId = groupIdNoSpecified ? data.getUniqueId() + "_" + (key) : groupId;
                        groupMode.set(StreamEvent.EVENT_GROUP_DATA);
                        groupKey.set(String.valueOf(groupId));
                        Map<String, Object> fieldData = new HashMap<>();
                        fieldData.put(key, ((JSONObject) jsonData).get(key));
                        String pushData = JSON.toJSONString(fieldData);
                        StreamData dataToPush = new StreamData(this.executeId, JSON.toJSONString(pushData));
                        dataToPush.setDataType(DataStructure.SERIALIZABLE);
                        pushData(dataToPush);
                    }

                } else {
                    // Simple push basic data type.
                    String pushData = JSON.toJSONString(jsonData);
                    pushData(new StreamData(this.executeId, StreamEvent.EVENT_DATA, pushData));

                }
            } catch (Exception e) {
                throw new ExecuteException(e);
            }
        } else {
            // TODO: Other data structure will be support in the future.
            throw new UnsupportedOperationException();
        }

        groupMode.set(StreamEvent.EVENT_GROUP_FINISHED);
        groupKey.set(String.valueOf(groupId));
        // Must be invoked after all group had been post to next streaming node.
        this.postFinished();
    }

    /**
     * Final Method.
     */
    @Override
    protected final void postFinished() throws ExecuteException {
        if (!previousIsFinished) {
            // Notify Data Group Finished.
            pushData(new StreamData(this.executeId, StreamEvent.EVENT_GROUP_FINISHED));
        } else {
            groupMode.remove();
            groupKey.remove();
            if (streamingInfo.isDistributeNext()) {
                for (IEntry entry : streamingInfo.getStreamSuccessorIdxMap().values()) {
                    Integer cnt = streamingInfo.getPushDataCntMap().get(entry.getNodeId());
                    altassChannel.publish(new StreamData(this.executeId, StreamEvent.EVENT_FINISHED, cnt));
                }
            } else {
                int dataPushCount = streamingInfo.getDataPushCount();
                StreamData streamData = new StreamData(this.executeId, StreamEvent.EVENT_FINISHED, dataPushCount);
                altassChannel.publish(streamData);
            }
        }
    }
}
