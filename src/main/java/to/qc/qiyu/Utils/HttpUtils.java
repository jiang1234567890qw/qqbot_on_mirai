package to.qc.qiyu.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpUtils {

    private static final String DEFAULT_CHARSET = "UTF-8"; // 默认字符集编码

    /**
     * 发送HTTP GET请求
     * @param url 请求的URL地址
     * @return 响应结果字符串
     * @throws IOException
     */
    public static String sendGetRequest(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, DEFAULT_CHARSET);

        httpClient.close();

        return result;
    }

    /**
     * 发送HTTP POST请求
     * @param url 请求的URL地址
     * @param requestBody 请求体数据
     * @return 响应结果字符串
     * @throws IOException
     */
    public static String sendPostRequest(String url, String requestBody) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        // 设置请求体数据
        StringEntity stringEntity = new StringEntity(requestBody, DEFAULT_CHARSET);
        httpPost.setEntity(stringEntity);

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, DEFAULT_CHARSET);

        httpClient.close();

        return result;
    }
    /**
     * 发送包含JSON数据的HTTP POST请求
     * @param url 请求的URL地址
     * @param jsonBody 请求体中的JSON数据
     * @return 响应结果字符串
     * @throws IOException
     */
    public static String sendJsonPostRequest(String url, String jsonBody) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        // 设置请求体数据为JSON格式
        StringEntity stringEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, DEFAULT_CHARSET);

        httpClient.close();

        return result;
    }
}