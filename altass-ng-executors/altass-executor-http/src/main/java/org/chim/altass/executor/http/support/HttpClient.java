package org.chim.altass.executor.http.support;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class Name: HttpClient
 * Create Date: 2017/9/19 20:38
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@SuppressWarnings({"Duplicates", "FieldCanBeLocal"})
public class HttpClient {

    public interface Constants {
        String X_PROTOBUF = "application/x-protobuf";
        String OCTET_STREAM_CONTENT_TYPE = "application/octet-onStreamProcessing";
        String X_JSON = "application/json";
        String URLENCODED = "application/x-www-form-urlencoded";
    }


    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private static RequestConfig defaultRequestConfig = null;
    private static CloseableHttpClient closeableHttpClient = null;
    private static HttpClient httpClient = null;

    private static Integer connectTimeout = 20000;                  // 建立http连接的超时时间
    private static Integer connectRequestTimeout = 6000;            // 从连接池获取连接的超时间
    private static Integer socketTimeout = 20000;                   // socket读取的超时时间（0为无限）
    private static Integer maxTotal = 1000;                         // 池中最大连接数
    private static Integer maxPerRoute = 200;                       // HttpClient中每个远程host最大连接数，一个host可能有多个连接

    private HttpClient() {
        PoolingHttpClientConnectionManager poolingManager = new PoolingHttpClientConnectionManager();
        defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectRequestTimeout)
                .setSocketTimeout(socketTimeout)
                .build();

        poolingManager.setMaxTotal(maxTotal);
        poolingManager.setDefaultMaxPerRoute(maxPerRoute);

        HttpClientBuilder clientBuilder = HttpClients.custom().setConnectionManager(poolingManager);

        closeableHttpClient = clientBuilder.build();
    }

    /**
     * 获得HttpClient单例
     *
     * @return HttpClient
     */
    public static HttpClient getInstance() {
        if (httpClient == null) {
            synchronized (HttpClient.class) {
                if (httpClient == null) {
                    httpClient = new HttpClient();
                }
            }
        }
        return httpClient;
    }

    /**
     * 执行一个简单的http请求并获得字符串类型的返回数据
     *
     * @param requestBase 请求对象
     * @return 如果返回200，那么返回请求的响应字符串数据
     */
    public String executeMethod(HttpRequestBase requestBase) {
        CloseableHttpResponse response = null;
        String result = null;
        setDefaultRequestConfig(requestBase);
        try {
            response = closeableHttpClient.execute(requestBase);

            HttpEntity entity = response.getEntity();
            result = inputStream2String(entity.getContent());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 设置默认的配置
     *
     * @param requestBase -
     */
    private void setDefaultRequestConfig(HttpRequestBase requestBase) {
        RequestConfig config = requestBase.getConfig();
        if (config == null) {
            requestBase.setConfig(defaultRequestConfig);
            return;
        }
        RequestConfig.Builder b = RequestConfig.custom();
        if (config.getConnectionRequestTimeout() == -1) {
            b.setConnectionRequestTimeout(defaultRequestConfig.getConnectionRequestTimeout());
        }
        if (config.getConnectTimeout() == -1) {
            b.setConnectTimeout(defaultRequestConfig.getConnectTimeout());
        }
        if (config.getSocketTimeout() == -1) {
            b.setSocketTimeout(defaultRequestConfig.getSocketTimeout());
        }

        config = b
                .setExpectContinueEnabled(config.isExpectContinueEnabled())
                .setAuthenticationEnabled(config.isAuthenticationEnabled())
                .setRedirectsEnabled(config.isRedirectsEnabled())
                .setRelativeRedirectsAllowed(config.isRelativeRedirectsAllowed())
                .setCircularRedirectsAllowed(config.isCircularRedirectsAllowed())
                .setMaxRedirects(config.getMaxRedirects())
                .setCookieSpec(config.getCookieSpec())
                .setLocalAddress(config.getLocalAddress())
                .setProxy(config.getProxy())
                .setTargetPreferredAuthSchemes(config.getTargetPreferredAuthSchemes())
                .setProxyPreferredAuthSchemes(config.getProxyPreferredAuthSchemes()).build();

        requestBase.setConfig(config);
    }

    /**
     * 将输入流转化为字符串数据
     *
     * @param in 输入流
     * @return 将输入流中的内容转化为字符串数据
     */
    private String inputStream2String(InputStream in) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024 * 256);
        byte[] temp = new byte[1024 * 256];
        int i;
        while ((i = in.read(temp)) != -1) {
            byteArrayOutputStream.write(temp, 0, i);
        }
        return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
    }

    /**
     * 执行一个HttPost请求
     *
     * @param url        请求地址
     * @param parameters 自动参数按utf-8编码
     * @return response正确返回后的字符串
     */
    public String doPost(String url, Map<String, String> parameters) {
        HttpPost httpRequestBase = new HttpPost(url);
        httpRequestBase.setHeader("Content-Type", Constants.URLENCODED);
        if (parameters != null && !parameters.isEmpty()) {
            try {
                httpRequestBase.setEntity(new UrlEncodedFormEntity(
                        this.toNameValuePairs(parameters), "UTF-8"));
            } catch (UnsupportedEncodingException e1) {
                logger.error("UnsupportedEncodingException", e1);
            }
        }
        return this.executeMethod(httpRequestBase);
    }

    /**
     * 执行一个HttpGet方法
     *
     * @param url 请求地址
     * @return response正确返回后的字符串
     */
    public String doGet(String url) {
        HttpGet get = new HttpGet(url);
        return this.executeMethod(get);
    }

    /**
     * 将map类型转化为请求的键值对
     *
     * @param params 需要发出的请求参数map
     * @return 返回对应的http请求参数键值对
     */
    public List<NameValuePair> toNameValuePairs(Map<String, String> params) {
        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
        Set<Map.Entry<String, String>> entries = params.entrySet();
        for (Map.Entry<String, String> entry : entries) {

            NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), entry.getValue());
            list.add(nameValuePair);
        }
        return list;
    }

    /**
     * 将map类型转化为请求的键值对，Object值类型
     *
     * @param params 需要发出的请求参数键值对
     * @return 返回对应的http请求键值对
     */
    public static List<NameValuePair> toNameValuePairsObject(Map<String, Object> params) {

        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
        Set<Map.Entry<String, Object>> entries = params.entrySet();
        for (Map.Entry<String, Object> entry : entries) {

            NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), (String) entry.getValue());
            list.add(nameValuePair);
        }
        return list;
    }
}
