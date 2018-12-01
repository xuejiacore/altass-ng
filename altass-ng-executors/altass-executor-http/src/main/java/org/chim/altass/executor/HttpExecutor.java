package org.chim.altass.executor;

import com.alibaba.fastjson.JSON;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.chim.altass.core.annotation.AltassAutowired;
import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.constant.ExecutorAbility;
import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.domain.buildin.attr.CommonStreamConfig;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.domain.meta.MetaData;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractStreamNodeExecutor;
import org.chim.altass.core.executor.RestoreContext;
import org.chim.altass.executor.http.bean.HttpConfig;
import org.chim.altass.executor.http.support.HttpClient;
import org.chim.altass.toolkit.job.UpdateAnalysis;

import javax.transaction.NotSupportedException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class Name: HttpExecutor
 * Create Date: 2017/10/14 18:32
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * Http 执行器，接收一个http请求，允许配置请求参数，请求 Http Header
 */
@Executable(name = "httpExecutor", assemble = true, ability = {ExecutorAbility.ABILITY_STREAMING})
@Resource(name = "Http", clazz = HttpExecutor.class, midImage = "res/images/executor/http_bg.png", pageUrl = "nodeConfigs/ext/httpNodeConfig.jsp")
public class HttpExecutor extends AbstractStreamNodeExecutor {

    /**
     * HttpClient
     */
    private HttpClient httpClient = null;

    /**
     * Http 请求所需要的参数，包括 Http Parameters、 Method、 Header、 Data Type 等请求基础配置
     */
    @AltassAutowired
    private HttpConfig httpConfig = null;

    @AltassAutowired
    private CommonStreamConfig commonStreamConfig = null;

    /**
     * 初始化一个节点执行器
     *
     * @param executeId 执行id
     */
    public HttpExecutor(String executeId) throws ExecuteException {
        super(executeId);
        commonStreamConfig = new CommonStreamConfig();
    }

    @Override
    protected void onArrange(UpdateAnalysis analysis) {

    }

    @Override
    protected boolean onChildInit() throws ExecuteException {
        httpClient = HttpClient.getInstance();
        String url = httpConfig.getUrl();
        if (url == null || url.length() == 0 || !url.contains("http")) {
            throw new ExecuteException("不合法的请求 URL");
        }
        return httpClient != null;
    }


    /**
     * process http request as streaming
     *
     * @param data a json structure data from last streaming node.
     * @return 如果操作成功，必须返回一个非 null 的流数据实例，如果失败，直接返回null，对应的推流数据将会被回滚到上级节
     * @throws ExecuteException Execute Exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onStreamProcessing(StreamData data) throws ExecuteException {
        // send process result to next streaming node if existed
        StreamData toNextPipeline = null;

        // receive stream data
        try {
            Object dataObj = data.getData();
            if (dataObj instanceof Map && ((Map) dataObj).size() != 0) {
                // use script parser to parse variables with stream data mapper
                String requestUrl = scriptParse((Map<String, Object>) dataObj, this.httpConfig.getUrl());
                Map<String, String[]> parameters = this.httpConfig.getParams();

                // to build http request parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                StringBuilder queryStr = buildRequestString((Map<String, Object>) dataObj, parameters, params);

                String response = null;
                String method = this.httpConfig.getMethod().toUpperCase();
                if ("GET".equals(method)) {
                    response = this.doGet(params, queryStr, requestUrl, data.getStreamSrc());
                } else if ("POST".equals(method)) {
                    response = this.doPost(params, requestUrl, data.getStreamSrc());
                }

                // send process result to next streaming node
                if ("JSON".equals(httpConfig.getDataType().toUpperCase())) {
                    toNextPipeline = new StreamData(((Entry) this.entry).getNodeId());
                    toNextPipeline.setData(JSON.parseObject(response, Map.class));
                    pushData(toNextPipeline);
                } else {
                    throw new NotSupportedException();
                }
            } else {
                throw new ExecuteException("Unsupported pipeline input data type. Map is allowed.");
            }
        } catch (Exception e) {
            throw new ExecuteException(e);
        } finally {
            postFinished();
        }
    }

    /**
     * To build a http request string
     *
     * @param dataObj    variable parameters
     * @param parameters parameters from http config
     * @param params     list to save parameters
     * @return a query string
     * @throws Exception -
     */
    private StringBuilder buildRequestString(Map<String, Object> dataObj, Map<String, String[]> parameters, List<NameValuePair> params) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        for (String key : parameters.keySet()) {
            String[] valueArr = parameters.get(key);
            String value = valueArr.length == 1 ? valueArr[0] : String.join(",", valueArr);
            if (key == null || key.length() == 0 || value == null || value.length() == 0) {
                continue;
            }
            // parse variable request parameter key and value.
            String newKey = scriptParse(dataObj, key);
            String newValue = scriptParse(dataObj, value);
            params.add(new BasicNameValuePair(newKey, newValue));
            queryStr.append("&").append(newKey).append("=").append(newValue);
        }
        return queryStr;
    }

    /**
     * 处理 基本的 Http 请求，根据配置的 请求参数，请求 Header 等信息
     *
     * @return 如果处理成功，那么返回值为true，否则返回值为false
     * @throws ExecuteException 节点执行异常
     */
    @Override
    public boolean onNodeNormalProcessing() throws ExecuteException {
        // 获得请求的方法以及请求的响应数据类型，目前只支持 GET 和 POST 方法
        String method = this.httpConfig.getMethod();
        String dataType = this.httpConfig.getDataType();
        dataType = dataType == null || dataType.length() == 0 ? "TEXT/HTML" : dataType.toUpperCase();
        Map<String, String[]> parameters = this.httpConfig.getParams();

        // 构建请求参数
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        StringBuilder queryStr = new StringBuilder();
        for (String key : parameters.keySet()) {
            String[] valueArr = parameters.get(key);
            String value = valueArr.length == 1 ? valueArr[0] : String.join(",", valueArr);
            if (key == null || key.length() == 0 || value == null || value.length() == 0) {
                continue;
            }
            params.add(new BasicNameValuePair(key, value));
            queryStr.append("&").append(key).append("=").append(value);
        }

        String url = this.httpConfig.getUrl();
        String response = null;
        if ("GET".equals(method.toUpperCase())) {
            response = this.doGet(params, queryStr, url, null);
        } else if ("POST".equals(method.toUpperCase())) {
            response = this.doPost(params, url, null);
        }

        // 将请求的响应数据作为下一个节点执行器的输入参数
        if ("JSON".equals(dataType)) {
            addOutputParam(new MetaData("HttpResp", response));
        }

        return true;
    }

    /**
     * 执行GET请求
     *
     * @param params   请求参数
     * @param queryStr 查询参数
     * @param url      请求URL
     * @return 返回请求响应数据
     */
    private String doGet(List<NameValuePair> params, StringBuilder queryStr, String url, String source) {
        String response;// 执行 GET 方法
        // 处理 GET 请求的查询参数
        String queryString = "";
        if (queryStr.length() > 0) {
            queryString = url.contains("?") ? (
                    url.endsWith("?") ? queryStr.substring(1) : queryStr.toString()
            ) : "?" + queryStr.substring(1);
        }

        EXECUTOR_LOGGER("msg", "HttpExecutor send request", "url", this.httpConfig.getUrl(),
                "params", params, "method", "GET", "timestamp", System.currentTimeMillis(),
                "source", source);

        HttpGet httpGet = new HttpGet(url + queryString);
        response = httpClient.executeMethod(httpGet);
        return response;
    }

    /**
     * 执行POST请求
     *
     * @param params 请求参数
     * @param url    请求URL
     * @return 返回请求的响应数据
     * @throws ExecuteException 执行异常
     */
    private String doPost(List<NameValuePair> params, String url, String source) throws ExecuteException {
        String response;// 执行 POST 方法
        HttpPost httpPost = new HttpPost(url);
        try {
            // 构造 POST 请求的请求参数
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(params, "UTF-8");
            httpPost.setEntity(uefEntity);

            EXECUTOR_LOGGER("msg", "HttpExecutor send request", "url", this.httpConfig.getUrl(),
                    "params", params, "method", "POST", "timestamp", System.currentTimeMillis(),
                    "source", source);

            response = httpClient.executeMethod(httpPost);
        } catch (UnsupportedEncodingException e) {
            throw new ExecuteException(e);
        }
        return response;
    }

    @Override
    public RestoreContext onRestore() throws ExecuteException {
        return null;
    }

    @Override
    public void onPause() throws ExecuteException {

    }

    @Override
    public void rollback(StreamData data) {

    }

    @Override
    public void onStreamOpen(StreamData data) throws ExecuteException {
        EXECUTOR_LOGGER("msg", "Stream had opened: $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$",
                "openedEvent", data.getData(),
                "source", data.getStreamSrc());
    }

    @Override
    public boolean isStreamingProcessing() throws ExecuteException {
        // 直接前驱包含有流式处理节点，当前节点采用流式处理
        return streamingInfo.getPrecursorStreamCnt() > 0;
    }

    @Override
    public void onStreamClose(StreamData data) throws ExecuteException {
        EXECUTOR_LOGGER("msg", "Stream had closed: $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$",
                "closedEvent", data.getData(),
                "source", data.getStreamSrc());
    }

    @Override
    public boolean retryIfFail() throws ExecuteException {
        return false;
    }

    // 此方法用于参数注入
    public void setHttpConfig(HttpConfig httpConfig) {
        this.httpConfig = httpConfig;
    }

    public void setCommonStreamConfig(CommonStreamConfig commonStreamConfig) {
        this.commonStreamConfig = commonStreamConfig;
    }
}
