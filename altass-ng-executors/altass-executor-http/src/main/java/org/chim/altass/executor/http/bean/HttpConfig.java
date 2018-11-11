package org.chim.altass.executor.http.bean;

import java.util.Map;

/**
 * Class Name: HttpParameter
 * Create Date: 2017/10/18 17:58
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class HttpConfig {
    private String method = null;                                   // 执行的http方法
    private String url = null;                                      // 需要请求的url
    private String transType = null;                                // 请求的类型，row/url
    private Map<String, String> header = null;                      // 请求的http头
    private Map<String, String[]> params = null;                    // 请求的参数
    private String dataType = "text";                               // 响应数据的数据格式

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public Map<String, String[]> getParams() {
        return params;
    }

    public void setParams(Map<String, String[]> params) {
        this.params = params;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        return "HttpConfig{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", transType='" + transType + '\'' +
                ", header=" + header +
                ", params=" + params +
                '}';
    }
}
