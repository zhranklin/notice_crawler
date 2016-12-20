package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;


public class forPost {

	/**
	 * 获取要抓取页面的所有内容
	 * 
	 * @param url
	 *            网页地址
	 * @return
	 */
	public static String sendPost(String url, String format,HashMap<String,String> mheader) {

		BufferedReader in = null;
		String result = "";
		// 通过HttpClientBuilder创建HttpClient
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient client = httpClientBuilder.build();

		HttpPost httpPost = new HttpPost(url);
		Iterator iter = mheader.entrySet().iterator();
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			nvps.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));  
		}
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(5000).build();
		//配置HttpGet参数
		httpPost.setConfig(requestConfig);

		System.out.println(httpPost.getRequestLine());

		try {
			HttpResponse httpResponse = client.execute(httpPost);

			int statusCode = httpResponse.getStatusLine().getStatusCode();

			// 响应状态
			System.out.println("status:" + statusCode);
			if (statusCode == HttpStatus.SC_OK) {
				// 获取响应的消息实体
				HttpEntity entity = httpResponse.getEntity();

				// 判断实体是否为空
				if (entity != null) {
					System.out.println("contentEncoding:"
							+ entity.getContentLength());

					in = new BufferedReader(new InputStreamReader(
							entity.getContent(), format));

					String line;
					while ((line = in.readLine()) != null) {
						// 遍历抓取到的每一行并将其存储到result里面
						result += line;
					}
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			try {
				client.close();
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

}
