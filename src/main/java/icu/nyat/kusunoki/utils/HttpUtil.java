package icu.nyat.kusunoki.utils;

import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpGet;

import org.apache.http.entity.ContentType;

import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.protocol.BasicHttpContext;

import org.apache.http.util.EntityUtils;

public class HttpUtil {
    /**

     * @Method: 获取浏览器访问内容--get请求

     * @Result: 浏览器响应内容

     */

    public String GetHttpResponseBody(String URLStr){
        HttpClient httpClient = new DefaultHttpClient();

        BasicHttpContext httpContext = new BasicHttpContext();

        HttpGet httpGet = new HttpGet(URLStr);

        try {
//将HttpContext对象作为参数传给execute()方法,则HttpClient会把请求响应交互过程中的状态信息存储在HttpContext中

            HttpResponse response = httpClient.execute(httpGet, (org.apache.http.protocol.HttpContext) httpContext);

            org.apache.http.HttpEntity entity = response.getEntity();

            if(null != entity){
                String httpResponseBody = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());

                if(httpResponseBody!=null) {
                    return httpResponseBody;

                }

                EntityUtils.consume(entity);

            }

        } catch (Exception e) {
            return e.getLocalizedMessage();

        }finally{
            httpClient.getConnectionManager().shutdown();

        }

        return null;

    }

}