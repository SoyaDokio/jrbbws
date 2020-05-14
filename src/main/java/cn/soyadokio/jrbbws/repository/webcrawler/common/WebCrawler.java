package cn.soyadokio.jrbbws.repository.webcrawler.common;

import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.soyadokio.jrbbws.constant.Constants;
import cn.soyadokio.jrbbws.domain.vo.HttpResult;

/**
 * 爬虫层，与DAO层并列，均服务于服务层
 * @author SoyaDokio
 * @date   2020-05-05
 */
public class WebCrawler {

	private static final Logger logger = LoggerFactory.getLogger(WebCrawler.class);

	public HttpResult doGet(String encodedUrl) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		logger.debug("HttpClient初始化成功");

		CloseableHttpResponse response = null;
		HttpGet request = new HttpGet(encodedUrl);
		int statusCode = -1;
		String html = null;
		try {
			logger.debug("请求开始，请求: {}", request.toString());
			response = httpClient.execute(request);
			statusCode = response.getStatusLine().getStatusCode();
			html = EntityUtils.toString(response.getEntity(), Constants.HTTP_ENTITY_CHARSET);
			logger.debug("请求结束，RESPONSE: {}", html.replace("\n", "").replace("\r", ""));
		} catch (ClientProtocolException e) {
			logger.debug("HttpClient遇到未知客户端协议异常，连接终止");
			e.printStackTrace();
		} catch (ParseException e) {
			logger.debug("HTTP的header子元素无法解析");
			e.printStackTrace();
		} catch (UnsupportedCharsetException e) {
			logger.debug("当前JVM实例不支持指定编码: {}", Constants.HTTP_ENTITY_CHARSET);
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			logger.debug("返回实体为: null，或长度超出最大范围: {}", Integer.MAX_VALUE);
			e.printStackTrace();
		} catch (IOException e) {
			logger.debug("HttpClient遇到HTTP协议错误 或 读取输入流时发生未知错误");
			e.printStackTrace();
		} finally {
			HttpClientUtils.closeQuietly(response);
			logger.debug("HttpResponse实例已成功关闭");
			HttpClientUtils.closeQuietly(httpClient);
			logger.debug("HttpClient实例已成功关闭");
		}
		HttpResult result = new HttpResult();
		result.setCode(statusCode);
		result.setBody(html);
		return result;
	}

}
