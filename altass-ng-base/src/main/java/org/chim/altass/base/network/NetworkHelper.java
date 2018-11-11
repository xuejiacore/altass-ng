/**
 * Project: Cloud-Harbor
 * Package Name: org.ike.wechat.network
 * Author: Xuejia
 * Date Time: 2016/8/15 13:32
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.network;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.chim.altass.base.network.parser.Parameters;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Class Name: NetworkHelper
 * Create Date: 2016/8/15 13:32
 * Creator: Xuejia
 * Version: v1.0
 * Updater: 詹学佳
 * Date Time: 2016年12月12日18:04:01
 * Description:
 * 过期注释
 * 针对微信公众号接口调用的网络请求核心类，该网络请求主要以返回调用接口产生的字符串以及简单的流式
 * <p>
 * 该工具类已经从微信项目中迁移到框架核心包作为支持使用
 */
public class NetworkHelper {

    private static Logger logger = Logger.getLogger(NetworkHelper.class);

    // https请求
    private static CloseableHttpAsyncClient httpsClient = null;
    // http请求
    private static CloseableHttpAsyncClient httpClient = null;

    static {
        // 初始化请求配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(3000)                 // 设置SOCKET超时
                .setConnectTimeout(3000)                // 设置连接超时
                .build();


        // -*- 初始化ssl请求证书开始
        FileInputStream inputStream = null;
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            inputStream = new FileInputStream(new File("F:\\Tool Kit\\Oracle\\Java\\jdk1.8.0_71\\jre\\lib\\security\\jssecacerts"));
            keyStore.load(inputStream, "changeit".toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SSLIOSessionStrategy sslSessionStrategy = null;
        PoolingNHttpClientConnectionManager connectionManager = null;
        try {
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(keyStore, new TrustSelfSignedStrategy())
                    .build();
            sslSessionStrategy = new SSLIOSessionStrategy(
                    sslContext,
                    new String[]{"TLSv1"},
                    null,
                    SSLIOSessionStrategy.getDefaultHostnameVerifier());
            ConnectingIOReactor ioReactor;
            ioReactor = new DefaultConnectingIOReactor();
            connectionManager = new PoolingNHttpClientConnectionManager(ioReactor);
            connectionManager.setMaxTotal(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // -*- 初始化ssl请求证书结束


        // 初始化普通的HttpClient
        httpClient = HttpAsyncClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();

        // 初始化SSL HttpsClient
        httpsClient = HttpAsyncClients.custom()
                .setSSLStrategy(sslSessionStrategy)
                .build();
        // 启用连接
        httpClient.start();
        httpsClient.start();
    }

    /**
     * 发起一个普通的get请求
     *
     * @param url        请求的url
     * @param parameters 请求的参数
     * @return 返回请求响应的内容
     * @throws IOException
     */
    public static String get(String url, Parameters parameters) throws IOException {
        String result;
        try {
            HttpGet httpGet = new HttpGet(buildParams(url, parameters));
            Future<HttpResponse> responseFuture = httpClient.execute(httpGet, null);
            result = EntityUtils.toString(responseFuture.get().getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    /**
     * 发出一个http post请求
     *
     * @param url        请求的url地址
     * @param parameters 请求的参数
     * @param postData   需要post的格式化数据
     * @return 返回请求成功的响应内容
     */
    public static String post(String url, Parameters parameters, String postData) {
        String result = null;
        HttpPost httpPost = new HttpPost(buildParams(url, parameters));
        try {
            StringEntity entity = new StringEntity(postData);
            httpPost.setEntity(entity);
            Future<HttpResponse> responseFuture = httpClient.execute(httpPost, null);
            result = EntityUtils.toString(responseFuture.get().getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送一个https get请求
     *
     * @param url        请求的url
     * @param parameters 请求的参数
     * @return 返回请求的响应内容
     */
    public static String sslGet(String url, Parameters parameters) {
        String result = null;
        HttpGet httpGet = new HttpGet(buildParams(url, parameters));
        Future<HttpResponse> responseFuture = httpsClient.execute(httpGet, null);
        try {
            result = EntityUtils.toString(responseFuture.get().getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送一个https post请求
     *
     * @param url        请求的url
     * @param parameters 请求的参数
     * @return 返回请求的响应内容
     */
    public static String sslPost(String url, Parameters parameters) {
        String result = null;
        HttpPost httpPost = new HttpPost(buildParams(url, parameters));
        Future<HttpResponse> responseFuture = httpsClient.execute(httpPost, null);
        try {
            result = EntityUtils.toString(responseFuture.get().getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 使用http post 一个结构化的json数据
     *
     * @param url      post地址
     * @param jsonData 需要post的json结构化数据
     * @return 返回响应结果
     */
    public static String jsonPost(String url, String jsonData) {
        try {
            return structPost(url, jsonData, "json", "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用https Post一个结构化json数据
     *
     * @param url      post地址
     * @param jsonData 需要post的json结构化数据
     * @return 返回响应结果
     */
    public static String sslJsonPost(String url, String jsonData) {
        try {
            return sslStructPost(url, jsonData, "json", "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用http post一个结构化xml数据
     *
     * @param url     post地址
     * @param xmlData 需要post的xml结构化数据
     * @return 返回响应结果
     */
    public static String xmlPost(String url, String xmlData) {
        try {
            return structPost(url, xmlData, "xml", "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用https post 一个结构化的xml数据
     *
     * @param url     post地址
     * @param xmlData 需要post的xml结构化数据
     * @return 返回响应结果
     */
    public static String sslXmlPost(String url, String xmlData) {
        try {
            return sslStructPost(url, xmlData, "xml", "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * post一个结构化数据到目标url中
     *
     * @param url         需要Post 的url 地址
     * @param postData    需要post的结构化数据
     * @param contentType 结构化数据类型，目前支持xml/json
     * @param encoding    结构化数据的编码格式
     * @return 返回请求响应
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static String structPost(String url, String postData, String contentType, String encoding)
            throws IOException, ExecutionException, InterruptedException {
        return structPost(url, postData, contentType, encoding, httpClient);
    }

    /**
     * 使用https post一个结构化数据到目标url中
     *
     * @param url         需要Post的url地址
     * @param postData    需要post的结构化数据
     * @param contentType 结构化数据的类型，目前支持xml/json
     * @param encoding    结构化数据的编码格式
     * @return 返回请求响应
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws IOException
     */
    private static String sslStructPost(String url, String postData, String contentType, String encoding)
            throws InterruptedException, ExecutionException, IOException {
        return structPost(url, postData, contentType, encoding, httpsClient);
    }

    /**
     * 使用httpclient对象post一个结构化数据到目标url中
     *
     * @param url         需要post的url地址
     * @param postData    需要post的结构化数据
     * @param contentType 结构化数据的类型，目前支持xml/json
     * @param encoding    结构化数据的编码格式
     * @param client      请求的http client
     * @return 返回请求响应
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static String structPost(String url, String postData, String contentType, String encoding,
                                     CloseableHttpAsyncClient client)
            throws IOException, ExecutionException, InterruptedException {
        logger.debug("PostData => " + postData);
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(postData);
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-Type", "text/" + contentType);
        httpPost.setHeader("charset", encoding);

        Future<HttpResponse> response = client.execute(httpPost, null);
        return EntityUtils.toString(response.get().getEntity(), "UTF-8");
    }

    /**
     * 将参数类进行Name-Value键值对进行转化
     *
     * @param parameters 参数类
     * @return 返回一个键值对列表
     */
    private static List<NameValuePair> paramsTrans(Parameters parameters) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair nameValuePair;
        for (Object key : parameters.keySet()) {
            nameValuePair = new BasicNameValuePair(key.toString(), parameters.get(key).getValue().toString());
            params.add(nameValuePair);
        }
        return params;
    }

    /**
     * 打印请求的日志
     *
     * @param url        请求的url
     * @param parameters 请求的参数
     */
    private static void reqInfo(String url, Parameters parameters) {
        if (logger.isDebugEnabled()) {
            StringBuilder paramsBuilder = new StringBuilder("?");
            for (Object key : parameters.keySet()) {
                paramsBuilder.append(key).append("=").append(parameters.getOrDef(key, ""))
                        .append("&");
            }
            paramsBuilder.deleteCharAt(paramsBuilder.length() - 1);
            logger.debug("* 【 url: " + url + " 】");
            logger.debug("* *【 params: " + (parameters.size() == 0 ? "" : paramsBuilder.toString()) + " 】");
        }
    }

    /**
     * 构造post的get参数
     *
     * @param parameters 需要构造携带的参数。不管是post还是get请求，参数都会被做为url的一部分
     * @return 返回组装完整的参数
     */
    private static String buildParams(String url, Parameters parameters) {
        if (parameters == null) {
            return url;
        }
        reqInfo(url, parameters);
        StringBuilder paramsBuilder = new StringBuilder(url + "?");
        for (Object key : parameters.keySet()) {
            paramsBuilder.append(key).append("=").append(parameters.getOrDef(key, ""))
                    .append("&");
        }
        paramsBuilder.deleteCharAt(paramsBuilder.length() - 1);
        return paramsBuilder.toString();
    }

}
