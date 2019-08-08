package com.panport.logCloud.utils;




import com.alibaba.fastjson.JSONException;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.RequestEntity;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by minisheep on 2019/7/25.
 */
public class HttpUtils {
    private static final HttpClient httpClient = HttpClientBuilder.create().build();

    /**
     * 发送get请求，参数放在url后面
     */
    public static String sendGetRequest(String strURL) throws IOException {
        String result = "";
        BufferedReader in = null;
        String urlNameString = strURL;
        URL realUrl = new URL(urlNameString);
        // 打开和URL之间的连接
        URLConnection connection = realUrl.openConnection();
        // 设置通用的请求属性
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> map = connection.getHeaderFields();
        // 定义 BufferedReader输入流来读取URL的响应
        in = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        if (in != null) {
            in.close();
        }

        return result;
    }

    /**
     * 发送post请求，参数单独发送到服务器端
     */
    public static String sendPostRequest(String url, String params) {
        StringBuilder result = new StringBuilder();
        String realUrl = url;
        InputStream in = null;
        BufferedReader br = null;
        try {
            // 与服务器建立连接
            URL u = new URL(realUrl);
            URLConnection conn = u.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "keep-alive");

            // post请求必须设置请求头
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            // 发送参数到服务器
            OutputStream out = conn.getOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(out,
                    "utf-8"));
            pw.print(params);
            pw.flush();
            pw.close();

            // 获取响应头
            Map<String, List<String>> map = conn.getHeaderFields();
            Set<Map.Entry<String, List<String>>> entry = map.entrySet();
            Iterator<Map.Entry<String, List<String>>> it = entry.iterator();
            while (it.hasNext()) {
                Map.Entry<String, List<String>> en = it.next();
                System.out.println(en.getKey() + ":::" + en.getValue());
            }

            // 获取响应数据
            in = conn.getInputStream();
            br = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }


    public static String httpPut(String urlPath, String data, String charSet) {
        String result = null;
        URL url = null;
        HttpURLConnection httpurlconnection = null;
        try {
            url = new URL(urlPath);
            httpurlconnection = (HttpURLConnection) url.openConnection();
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(true);
            httpurlconnection.setConnectTimeout(2000000);// 设置连接主机超时（单位：毫秒）
            httpurlconnection.setReadTimeout(2000000);// 设置从主机读取数据超时（单位：毫秒）


            httpurlconnection.setRequestMethod("PUT");
            httpurlconnection.setRequestProperty("Content-Type", "application/json");

            if (StringUtils.isNotBlank(data)) {
                httpurlconnection.getOutputStream().write(data.getBytes("UTF-8"));
            }
            httpurlconnection.getOutputStream().flush();
            httpurlconnection.getOutputStream().close();
            int code = httpurlconnection.getResponseCode();

            if (code == 200) {
                DataInputStream in = new DataInputStream(httpurlconnection.getInputStream());
                int len = in.available();
                byte[] by = new byte[len];
                in.readFully(by);
                if (StringUtils.isNotBlank(charSet)) {
                    result = new String(by, Charset.forName(charSet));
                } else {
                    result = new String(by);
                }
                in.close();
            } else {
                System.out.println("请求地址：" + urlPath + "返回状态异常，异常号为：" + code);
            }
        } catch (Exception e) {
            System.out.println("访问url地址：" + urlPath + "发生异常");
        } finally {
            if (httpurlconnection != null) {
                httpurlconnection.disconnect();
            }
        }
        return result;
    }


    public static String delete(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
        httpDelete.setConfig(requestConfig);
        httpDelete.setHeader("Content-type", "application/json");
        httpDelete.setHeader("DataEncoding", "UTF-8");

        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpDelete);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
