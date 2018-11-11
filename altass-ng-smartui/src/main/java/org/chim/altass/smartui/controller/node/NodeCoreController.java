/*
 * Project: x-framework
 * Package Name: org.ike.controller.support.node
 * Author: Xuejia
 * Date Time: 2016/11/23 0:28
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.controller.node;

import com.alibaba.fastjson.JSON;
import org.chim.altass.core.configuration.NodeResource;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.exception.CyclicityException;
import org.chim.altass.core.exception.FlowDescException;
import org.chim.altass.core.executor.AbstractExecutor;
import org.chim.altass.smartui.common.BaseController;
import org.chim.altass.smartui.common.CommonResult;
import org.chim.altass.smartui.service.INodePainterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Class Name: NodeCoreController
 * Create Date: 2016/11/23 0:28
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 节点绘制核心控制器，用于绘制配置作业节点的情况，其中包括以下几种操作：
 * 1、添加节点；
 * 2、删除节点；
 * 3、添加连线；
 * 4、删除连线；
 * 5、修改节点配置；
 */
@SuppressWarnings("SpringAutowiredFieldsWarningInspection")
@Controller
@RequestMapping("nodePainter")
public class NodeCoreController extends BaseController {

    private static final String NODE_VIEWS_ROOT = "support/flow/nodeflow";

    /**
     * 节点绘制服务
     */
    @Autowired
    private INodePainterService painterService = null;

    @RequestMapping(method = RequestMethod.GET, value = "painter")
    public String nodePainterPanel() {
        return NODE_VIEWS_ROOT + "/nodePainter";
    }

    /**
     * 获得节点资源
     *
     * @return -
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "getNodeRes")
    public CommonResult getNodeResource() {
        CommonResult commonResult = new CommonResult();
        commonResult.setFlag(true);
        commonResult.setCode(HttpStatus.OK.value());
        commonResult.setResult(painterService.obtainNodeResource());
        commonResult.setMsg("success");
        return commonResult;
    }

    /**
     * 初始化节点面板
     *
     * @return 初始化数据
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "initNodePainter")
    public CommonResult initNodePainter() {
        log.debug("---------------- 初始化节点面板 ----------------");
        CommonResult result = new CommonResult(true);
        result.setCode(HttpStatus.OK.value());
        // 获取节点面板的初始化数据
        result.setResult(painterService.initPainter());
        result.setMsg("初始化成功");
        return result;
    }

    /**
     * 创建一个节点
     *
     * @param resId  执行节点的执行资源ID，对应执行器的类名MD5值
     * @param jobId  作业号
     * @param nodeId 当前操作的节点ID
     * @param title  当前操作节点的名称
     * @param x      当前创建节点的横坐标
     * @param y      当前创建节点的纵坐标
     * @return -
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "createNode")
    public CommonResult createNode(String resId, String jobId, String nodeId, String title,
                                   Integer x, Integer y, String entryJson) throws ClassNotFoundException {
        log.debug("---------------- 创建节点 ----------------");
        // 从节点创建数据中实例化一个执行节点元素对象
        CommonResult result = new CommonResult();
        NodeResource nodeResource = painterService.obtainResource(resId);
        if (nodeResource == null) {
            result.setFlag(false);
            result.setMsg("资源错误，无法找到节点执行器!");
            return result;
        }

        Entry entry = JSON.parseObject(entryJson, Entry.class);
        if (entry == null) {
            entry = new Entry();
        } else {
            entry.setNodeId(nodeId);
        }
        entry.setX(x);
        entry.setY(y);

        entry.setExecutorClz((Class<? extends AbstractExecutor>) Class.forName(nodeResource.getClazz()));

        boolean createNodeSuccessful;
        result.setFlag(true);
        entry.setNodeName(title);
        // 提交创建一个节点
        createNodeSuccessful = painterService.createNode(jobId, entry);
        result.setMsg("节点创建成功");
        result.setCode(HttpStatus.OK.value());
        result.setResult(createNodeSuccessful);
        return result;
    }

    /**
     * 删除节点
     *
     * @param jobId  当前操作的作业ID
     * @param nodeId 当前操作的节点ID
     * @return -
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "deleteNode")
    public CommonResult deleteNode(String jobId, String nodeId) {
        log.debug("---------------- 删除节点 ----------------");
        boolean deleteNodeSuccessful = painterService.deleteNode(jobId, nodeId);
        CommonResult result = new CommonResult(deleteNodeSuccessful);
        result.setResult(deleteNodeSuccessful);
        result.setCode(HttpStatus.OK.value());
        result.setMsg(deleteNodeSuccessful ? "节点删除成功!" : "节点删除失败，作业或节点不存在!");
        return result;
    }

    /**
     * @param jobId    需要操作的作业ID
     * @param nodeId   节点ID
     * @param jsonData 配置的json数据
     * @return -
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "configNode")
    public CommonResult configNode(String jobId, String nodeId, String jsonData) {
        log.debug("---------------- 配置节点 ----------------");

        boolean configSuccessful = painterService.configNode(jobId, nodeId, jsonData);
        CommonResult result = new CommonResult(configSuccessful);
        result.setResult(configSuccessful);
        result.setCode(HttpStatus.OK.value());
        return result;
    }


    /**
     * 获得节点的配置信息
     *
     * @param jobId  操作的作业ID
     * @param nodeId 操作的节点ID
     * @return -
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "nodeConfiguration")
    public CommonResult getNodeConfiguration(String jobId, String nodeId) {
        log.debug("---------------- 配置节点 ----------------");

        Entry entry = painterService.getNodeConfiguration(jobId, nodeId);
        CommonResult result = new CommonResult(entry != null);
        result.setResult(entry);
        result.setCode(HttpStatus.OK.value());
        return result;
    }

    /**
     * 创建节点的连线
     *
     * @param jobId    当前操作的作业号
     * @param sourceId 连线的源ID
     * @param targetId 连线的目标ID
     * @return -
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "connectNodes")
    public CommonResult connectNodes(String sourceId, String targetId, String jobId) {
        log.debug("---------------- 创建连线 ----------------");
        boolean connectSuccessful = false;
        CommonResult result = new CommonResult(true);
        try {
            connectSuccessful = painterService.connect(jobId, sourceId, targetId);
        } catch (FlowDescException e) {
            if (e instanceof CyclicityException) {
                List<String> troubleNode = ((CyclicityException) e).getTroubleNode();
                result.setResult(troubleNode);
                result.setFlag(false);
                result.setCode(HttpStatus.OK.value());
                result.setMsg("连线创建失败，不允许流程闭环");
            }
            return result;
        }
        result.setResult(connectSuccessful);
        result.setCode(HttpStatus.OK.value());
        result.setMsg("连线创建成功");
        return result;
    }

    /**
     * 删除节点连线
     *
     * @param jobId    当前操作的作业号
     * @param sourceId 所删除连线的源ID
     * @param targetId 所删除连线的目标ID
     * @return -
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "disconnectNodes")
    public CommonResult disconnectNodes(String jobId, String sourceId, String targetId) {
        log.debug("---------------- 删除节点连线 ----------------");
        boolean deleteConnectorSuccessful = painterService.deleteConnector(jobId, sourceId, targetId);
        CommonResult result = new CommonResult(deleteConnectorSuccessful);
        result.setCode(HttpStatus.OK.value());
        result.setResult(deleteConnectorSuccessful);
        return result;
    }

    /**
     * 移动节点，更新节点的坐标信息
     *
     * @return -
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "moveNode")
    public CommonResult moveNodePosition(HttpServletRequest request) {
        Map parameterMap = request.getParameterMap();
        log.debug("---------------- 移动节点 ----------------");
        int size = parameterMap.size() / 5;
        boolean moveSuccessful = false;
        for (int i = 0; i < size; i++) {
            String jobId = ((String[]) parameterMap.get("data[" + i + "][jobId]"))[0];
            String nodeId = ((String[]) parameterMap.get("data[" + i + "][nodeId]"))[0];
            String title = ((String[]) parameterMap.get("data[" + i + "][title]"))[0];
            Integer toX = Integer.parseInt(((String[]) parameterMap.get("data[" + i + "][toX]"))[0]);
            Integer toY = Integer.parseInt(((String[]) parameterMap.get("data[" + i + "][toY]"))[0]);
            moveSuccessful = painterService.moveNode(jobId, nodeId, title, toX, toY);
        }
        CommonResult result = new CommonResult(moveSuccessful);
        result.setResult(moveSuccessful);
        result.setCode(HttpStatus.OK.value());
        return result;
    }

    /**
     * 检查节点的合法性
     *
     * @param jobId 当前操作的作业号
     * @return -
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "validateGraphic")
    public CommonResult validateGraphic(String jobId) {
        log.debug("---------------- 检查作业合法性 ----------------");
        boolean isGraphicValidated = painterService.validateGraphic(jobId) == 0;
        // 检验作业节点的合法性，如果合法，那么返回值为0，否则返回值为对应的错误代号
        CommonResult result = new CommonResult(isGraphicValidated);
        result.setResult(isGraphicValidated);
        result.setCode(HttpStatus.OK.value());
        result.setMsg("检验通过");
        return result;
    }

    /**
     * TODO：调试使用：测试节点的运行
     *
     * @param jobId 需要运行的作业ID
     * @return -
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "testRunning")
    public CommonResult testRunningJob(String jobId) {
        log.debug("---------------- 测试运行作业 ----------------");
        boolean runningSuccessful = painterService.testRunningJob(jobId);
        CommonResult result = new CommonResult(runningSuccessful);
        result.setCode(HttpStatus.OK.value());
        result.setMsg("作业运行成功");
        result.setResult(runningSuccessful);
        return result;
    }

    /**
     * 修改连线的类型
     *
     * @param jobId         操作的作业ID
     * @param sourceId      连线源ID
     * @param targetId      连线目标ID
     * @param connectorType 连线的类型
     * @return -
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "changeConnectorType")
    public CommonResult changeConnectorType(String jobId, String sourceId, String targetId, String connectorType) {
        log.debug("---------------- 修改连线类型 ----------------");

        boolean changeSuccessful = painterService.changeConnectorType(jobId, sourceId, targetId, connectorType);
        CommonResult result = new CommonResult(changeSuccessful);
        result.setCode(HttpStatus.OK.value());
        result.setResult(changeSuccessful);
        return result;
    }
}
