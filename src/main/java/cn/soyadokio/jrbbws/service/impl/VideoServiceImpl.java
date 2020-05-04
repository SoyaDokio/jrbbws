package cn.soyadokio.jrbbws.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.UnsupportedCharsetException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;
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
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import cn.soyadokio.jrbbws.config.InvalidDelimiterException;
import cn.soyadokio.jrbbws.constant.Constants;
import cn.soyadokio.jrbbws.domain.VideoBO;
import cn.soyadokio.jrbbws.domain.VideoDTO;
import cn.soyadokio.jrbbws.service.VideoService;
import cn.soyadokio.jrbbws.utils.StringUtils;

/**
 * 服务层-接口实现
 * @author SoyaDokio
 * @date	2020-03-28
 */
@Service("videoService")
public class VideoServiceImpl implements VideoService {

	private static final Logger logger = LoggerFactory.getLogger(VideoServiceImpl.class);

	private static final Pattern PATTERN_1 = Pattern.compile("<a href=\"(\\/p\\/\\d+\\.html)\">");

	private static final Pattern PATTERN_2 = Pattern.compile("<iframe src=\"(.+)\" .+></iframe>");

	/**
	 * 根据日期获取视频
	 * @param datestamp	字符串日期，形如：20200101
	 * @return
	 */
	@Override
	public VideoDTO getVideo(String datestamp) {
		if (datestamp == null) {
			logger.error("传入日期字符串参数非法: null");
			return new VideoDTO(0, "服务器内部错误");
		}
		datestamp = datestamp.trim();

		String LATEST = "latest";
		if (LATEST.equals(datestamp)) {
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
			LocalTime time = LocalTime.now();
			if (time.isAfter(LocalTime.of(Constants.BOUNDARY_HOUR, Constants.BOUNDARY_MINUTE))) {
				datestamp = LocalDate.now().format(fmt);
				logger.info("传入日期字符串参数为: latest，需智能修正。当前时间已过 21:45:00，则修正为: {}", datestamp);
			} else {
				datestamp = LocalDate.now().minusDays(1).format(fmt);
				logger.info("传入日期字符串参数为: latest，需智能修正。当前时间未到 21:45:00，则修正为: {}", datestamp);
			}
		}

		if (!StringUtils.isDate(datestamp)) {
			logger.warn("传入日期字符串参数非法: " + datestamp);
			return new VideoDTO(0, "传入日期字符串参数非法: " + datestamp);
		}
		String formattedDatestamp = null;
		try {
			formattedDatestamp = StringUtils.delimitDatestring(datestamp, "-");
		} catch (InvalidDelimiterException e) {
			logger.error("非法分隔符异常，插入日期字符串的分隔符不属于\" \"、\"-\"、\"/\"、\".\"的任一种");
			e.printStackTrace();
			return new VideoDTO(0, "服务器内部错误");
		}
		logger.info("日期字符串参数为: {}，则智能变换为: {}", datestamp, formattedDatestamp);

		CloseableHttpClient httpClient = HttpClients.createDefault();
		logger.debug("HttpClient初始化成功");

		String SEARCH_CONTENT = "今日播报";
		String SEARCH_URL = "http://xiangyang.cjyun.org/s?wd=";
		String encodedUrl = null;
		try {
			encodedUrl = SEARCH_URL + URLEncoder.encode(SEARCH_CONTENT, Constants.URL_CODE_CHARSET)
					+ formattedDatestamp;
		} catch (UnsupportedEncodingException e) {
			logger.error("当前JVM实例不支持指定编码: {}", Constants.URL_CODE_CHARSET);
			e.printStackTrace();
			return new VideoDTO(0, "服务器内部错误");
		}

		CloseableHttpResponse response1 = null;
		HttpGet request1 = new HttpGet(encodedUrl);
		int statusCode1 = -1;
		String html1 = null;
		try {
			logger.info("[1/3]开始请求-云上襄阳网站按关键词检索功能: {}", request1.toString());
			response1 = httpClient.execute(request1);
			statusCode1 = response1.getStatusLine().getStatusCode();
			html1 = EntityUtils.toString(response1.getEntity(), Constants.HTTP_ENTITY_CHARSET);
//			logger.info("请求成功，RESPONSE: {}", html1);
			logger.info("请求成功，RESPONSE: [内容过长，略]");
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
			HttpClientUtils.closeQuietly(response1);
			logger.debug("HttpResponse实例已成功关闭");
		}

		if (statusCode1 != HttpStatus.SC_OK) {
			logger.error("HTTP状态码异常: {}", statusCode1);
			return new VideoDTO(0, "HTTP状态码异常: " + statusCode1);
		}
		logger.debug("HTTP状态码: 200");

		String INDEXOF_CONTENT = "搜索结果&nbsp;共&nbsp;1&nbsp;个";
		int index = html1.indexOf(INDEXOF_CONTENT);
		if (index == -1) {
			logger.error("RESPONSE中未检索到目标内容: {}", INDEXOF_CONTENT);
			return new VideoDTO(0, "未检索到指定日期视频");
		}
		logger.info("RESPONSE中成功检索到目标内容: {}", INDEXOF_CONTENT);
		logger.debug("目标内容索引值: {}", index);

		Matcher m1 = PATTERN_1.matcher(html1);
		if (!m1.find()) {
			logger.error("RESPONSE中未检索到当日视频所在网页的链接");
			return new VideoDTO(0, "RESPONSE中未检索到当日视频所在网页的链接");
		}
		String prefixUrl = m1.group(1);
		logger.info("RESPONSE中成功检索到当日视频所在网页URL: {}", prefixUrl);

		CloseableHttpResponse response2 = null;
		String H5_URL = "https://m-xiangyang.cjyun.org";
		HttpGet request2 = new HttpGet(H5_URL + prefixUrl);
		int statusCode2 = -1;
		String html2 = null;
		try {
			logger.info("[2/3]开始请求-指定日期（{}）视频所在网页: {}", formattedDatestamp, request2.toString());
			response2 = httpClient.execute(request2);
			statusCode2 = response2.getStatusLine().getStatusCode();
			html2 = EntityUtils.toString(response2.getEntity(), Constants.HTTP_ENTITY_CHARSET);
//			logger.info("请求成功，RESPONSE: {}", html2);
			logger.info("请求成功，RESPONSE: [内容过长，略]");
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
			HttpClientUtils.closeQuietly(response2);
			logger.debug("HttpResponse实例已成功关闭");
		}

		if (statusCode2 != HttpStatus.SC_OK) {
			logger.error("HTTP状态码异常: {}", statusCode2);
			return new VideoDTO(0, "HTTP状态码异常: " + statusCode2);
		}
		logger.debug("HTTP状态码: 200");

		Matcher m2 = PATTERN_2.matcher(html2);
		if (!m2.find()) {
			logger.error("RESPONSE中未检索到iframe元素");
			return new VideoDTO(0, "RESPONSE中未检索到iframe元素");
		}
		String src = m2.group(1);
		logger.info("RESPONSE中成功检索到iframe元素，iframe.src={}", src);

		if (src.indexOf("?") == -1) {
			logger.error("iframe节点src属性中检索参数失败");
			return new VideoDTO(0, "iframe节点src属性中检索参数失败");
		}
		String paramsStr = src.substring(src.indexOf("?") + 1);
		logger.info("iframe节点src属性值中链接的参数部分: {}", paramsStr);

		try {
			paramsStr = URLDecoder.decode(paramsStr, Constants.URL_CODE_CHARSET);
		} catch (UnsupportedEncodingException e) {
			logger.error("当前JVM实例不支持指定编码: {}", Constants.URL_CODE_CHARSET);
			e.printStackTrace();
			return new VideoDTO(0, "服务器内部错误");
		}
		String[] params = paramsStr.split("&");
		Map<String, String> paramsMap = new HashMap<String, String>(16);
		for (int i = 0; i < params.length; i++) {
			String param = params[i];
			String key = param.split("=")[0];
			String val = param.split("=").length == 1 ? "" : param.split("=")[1];
			paramsMap.put(key, val);
		}
		Gson gson = new Gson();
		logger.info("iframe节点src属性值中链接的参数部分解析结果: {}", gson.toJson(paramsMap));

		CloseableHttpResponse response3 = null;
		String GETJSON_URL = "https://app.cjyun.org/video/player/video?sid=";
		String GETJSON_PARAM_2 = "&vid=";
		String GETJSON_PARAM_3 = "&type=video";
		HttpGet request3 = new HttpGet(
				GETJSON_URL + paramsMap.get("sid") + GETJSON_PARAM_2 + paramsMap.get("vid") + GETJSON_PARAM_3);
		int statusCode3 = -1;
		String jsonstring = null;
		try {
			logger.info("[3/3]开始请求-视频信息API: {}", request3.toString());
			response3 = httpClient.execute(request3);
			statusCode3 = response3.getStatusLine().getStatusCode();
			jsonstring = EntityUtils.toString(response3.getEntity(), Constants.HTTP_ENTITY_CHARSET);
			logger.info("请求成功，RESPONSE: {}", jsonstring);
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
			HttpClientUtils.closeQuietly(response3);
			logger.debug("HttpResponse实例已成功关闭");
			HttpClientUtils.closeQuietly(httpClient);
			logger.debug("HttpClient实例已成功关闭");
		}

		if (statusCode3 != HttpStatus.SC_OK) {
			logger.error("HTTP状态码异常: {}", statusCode3);
			return new VideoDTO(0, "HTTP状态码异常: " + statusCode3);
		}
		logger.debug("HTTP状态码: 200");

		jsonstring = jsonstring.replace("\\/", "/");
		jsonstring = StringUtils.unicodeToCn(jsonstring);
		logger.info("JSON解析结果: {}", jsonstring);
		VideoBO videoBO = gson.fromJson(jsonstring, VideoBO.class);
		VideoDTO videoDTO = new VideoDTO(1, "success", datestamp, videoBO.getTitle().replace(".mp4", ""),
				paramsMap.get("thumb"), videoBO.getFile().getHd());
		return videoDTO;
	}

//	public static void main(String[] args) throws UnsupportedEncodingException {
//		new VideoServiceImpl();
//	}
//
//	public VideoServiceImpl() {
//		System.out.println(new Gson().toJson(getVideo("latest")));
//	}

}