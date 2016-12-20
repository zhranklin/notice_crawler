package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;


public class HttpUtil {

	/**
	 * 获取要抓取页面的所有内容
	 * 
	 * @param url
	 *            网页地址
	 * @return
	 */
	public static String sendGet(String url, String format) {

		BufferedReader in = null;
		String result = "";
		// 通过HttpClientBuilder创建HttpClient
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient client = httpClientBuilder.build();

		HttpGet httpGet = new HttpGet(url);
		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(5000).build();
		//配置HttpGet参数
		httpGet.setConfig(requestConfig);

		System.out.println(httpGet.getRequestLine());

		try {
			HttpResponse httpResponse = client.execute(httpGet);

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
