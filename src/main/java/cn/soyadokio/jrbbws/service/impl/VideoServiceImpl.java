package cn.soyadokio.jrbbws.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import cn.soyadokio.jrbbws.config.InvalidDelimiterException;
import cn.soyadokio.jrbbws.constant.Constants;
import cn.soyadokio.jrbbws.domain.bo.VideoBo;
import cn.soyadokio.jrbbws.domain.dto.VideoDto;
import cn.soyadokio.jrbbws.domain.po.VideoPo;
import cn.soyadokio.jrbbws.domain.vo.HttpResult;
import cn.soyadokio.jrbbws.repository.dao.VideoDao;
import cn.soyadokio.jrbbws.repository.webcrawler.common.WebCrawler;
import cn.soyadokio.jrbbws.service.VideoService;
import cn.soyadokio.jrbbws.utils.StringUtils;

/**
 * 服务层-接口实现
 * @author SoyaDokio
 * @date	2020-03-28
 */
@Service("videoService")
public class VideoServiceImpl implements VideoService {

	@Resource(name = "videoDao")
	private VideoDao videoDao;

	private static final Logger logger = LoggerFactory.getLogger(VideoServiceImpl.class);

	private static final Pattern PATTERN_1 = Pattern.compile("<a href=\"(\\/p\\/\\d+\\.html)\">");

	private static final Pattern PATTERN_2 = Pattern.compile("<iframe src=\"(.+)\" .+></iframe>");

	/**
	 * 获取最近日期视频
	 * 若当前时间早于21:45则获取昨日视频；若当前时间晚于21:45则获取今日视频
	 * @return
	 */
	@Override
	public VideoDto getLatestVideo() {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalTime time = LocalTime.now();
		String datestamp = null;
		if (time.isAfter(LocalTime.of(Constants.BOUNDARY_HOUR, Constants.BOUNDARY_MINUTE))) {
			datestamp = LocalDate.now().format(fmt);
			logger.info("传入日期字符串参数为: latest，需智能判断。当前时刻已过 21:45:00，则转换为: {}", datestamp);
		} else {
			datestamp = LocalDate.now().minusDays(1).format(fmt);
			logger.info("传入日期字符串参数为: latest，需智能判断。当前时刻未到 21:45:00，则转换为: {}", datestamp);
		}
		return getVideo(datestamp);
	}

	/**
	 * 根据日期获取视频
	 * @param datestamp	字符串日期，形如：20200101
	 * @return
	 */
	@Override
	public VideoDto getVideo(String datestamp) {
		if (datestamp == null) {
			logger.error("传入日期字符串参数非法: null");
			return new VideoDto(0, "服务器内部错误");
		}
		datestamp = datestamp.trim();

		if (!StringUtils.isDate(datestamp)) {
			logger.warn("传入日期字符串参数非法: " + datestamp);
			return new VideoDto(0, "传入日期字符串参数非法: " + datestamp);
		}

		// 先在DB查询，若有直接返回，若无则使用爬虫爬取
		logger.info("先尝试在DB中查询是否存在缓存...");
		VideoPo videoPo = videoDao.queryVideo(datestamp);
		if (videoPo.getStatus() != -1) {
			logger.info("DB缓存存在，直接返回查询结果");
			return videoPo.toVideoDto();
		}
		logger.info("DB缓存不存在，尝试通过网站解析方式获取");

		String formattedDatestamp = null;
		try {
			formattedDatestamp = StringUtils.delimitDatestring(datestamp, "-");
		} catch (InvalidDelimiterException e) {
			logger.error("非法分隔符异常，插入日期字符串的分隔符不属于\" \"、\"-\"、\"/\"、\".\"的任一种");
			e.printStackTrace();
			return new VideoDto(0, "服务器内部错误");
		}
		logger.info("日期字符串参数为: {}，增加分隔符后为: {}", datestamp, formattedDatestamp);

		final String SEARCH_URL = "http://xiangyang.cjyun.org/s?wd=";
		final String SEARCH_CONTENT = "今日播报";
		String encodedUrl = null;
		try {
			encodedUrl = SEARCH_URL + URLEncoder.encode(SEARCH_CONTENT, Constants.URL_CODE_CHARSET)
					+ formattedDatestamp;
		} catch (UnsupportedEncodingException e) {
			logger.error("当前JVM实例不支持指定编码: {}", Constants.URL_CODE_CHARSET);
			e.printStackTrace();
			return new VideoDto(0, "服务器内部错误");
		}

		WebCrawler wc = new WebCrawler();
		logger.info("[1/3]请求开始-云上襄阳网站按关键词检索功能检索《今日播报》节目，请求地址: {}", encodedUrl);
		HttpResult result1 = wc.doGet(encodedUrl);
		String html1 = result1.getBody();
		logger.info("[1/3]请求结束，RESPONSE: [内容过长，略]");

		int statusCode1 = result1.getCode();
		if (statusCode1 != HttpStatus.SC_OK) {
			logger.error("HTTP状态码异常: {}", statusCode1);
			return new VideoDto(0, "HTTP状态码异常: " + statusCode1);
		}
		logger.debug("HTTP状态码: 200");

		final String INDEXOF_CONTENT = "搜索结果&nbsp;共&nbsp;1&nbsp;个";
		int index = html1.indexOf(INDEXOF_CONTENT);
		if (index == -1) {
			logger.error("RESPONSE中未检索到当日视频信息");
			return new VideoDto(0, "未检索到指定日期视频");
		}
		logger.info("RESPONSE中成功检索到当日视频信息");
		logger.debug("目标内容[{}]索引值: {}", INDEXOF_CONTENT, index);

		Matcher m1 = PATTERN_1.matcher(html1);
		if (!m1.find()) {
			logger.error("RESPONSE中未检索到当日视频所在网页的链接");
			return new VideoDto(0, "RESPONSE中未检索到当日视频所在网页的链接");
		}
		String prefixUrl = m1.group(1);
		logger.info("RESPONSE中成功检索到当日视频所在网页URL: {}", prefixUrl);

		final String H5_URL = "https://m-xiangyang.cjyun.org";
		String requestUrl3 = H5_URL + prefixUrl;

		logger.info("[2/3]请求开始-指定日期（{}）视频所在网页，请求地址: {}", formattedDatestamp, requestUrl3);
		HttpResult result2 = wc.doGet(requestUrl3);
		String html2 = result2.getBody();
		logger.info("[2/3]请求结束，RESPONSE: [内容过长，略]");

		int statusCode2 = result2.getCode();
		if (statusCode2 != HttpStatus.SC_OK) {
			logger.error("HTTP状态码异常: {}", statusCode2);
			return new VideoDto(0, "HTTP状态码异常: " + statusCode2);
		}
		logger.debug("HTTP状态码: 200");

		Matcher m2 = PATTERN_2.matcher(html2);
		if (!m2.find()) {
			logger.error("RESPONSE中未检索到iframe元素");
			return new VideoDto(0, "RESPONSE中未检索到iframe元素");
		}
		String src = m2.group(1);
		logger.info("RESPONSE中成功检索到iframe元素，iframe元素src属性：{}", src);

		if (src.indexOf("?") == -1) {
			logger.error("iframe元素src属性中检索参数失败");
			return new VideoDto(0, "iframe元素src属性中检索参数失败");
		}
		String paramsStr = src.substring(src.indexOf("?") + 1);
		logger.info("iframe元素src属性值中链接的参数部分: {}", paramsStr);

		try {
			paramsStr = URLDecoder.decode(paramsStr, Constants.URL_CODE_CHARSET);
		} catch (UnsupportedEncodingException e) {
			logger.error("当前JVM实例不支持指定编码: {}", Constants.URL_CODE_CHARSET);
			e.printStackTrace();
			return new VideoDto(0, "服务器内部错误");
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
		logger.info("iframe元素src属性值中链接的参数部分解析结果: {}", gson.toJson(paramsMap));

		final String GETJSON_URL = "https://app.cjyun.org/video/player/video?sid=";
		final String GETJSON_PARAM_2 = "&vid=";
		final String GETJSON_PARAM_3 = "&type=video";
		String requestUrl = GETJSON_URL + paramsMap.get("sid") + GETJSON_PARAM_2 + paramsMap.get("vid")
				+ GETJSON_PARAM_3;

		logger.info("[3/3]请求开始-视频信息API，请求地址: {}", requestUrl);
		HttpResult result3 = wc.doGet(requestUrl);
		String jsonstring = result3.getBody();
		logger.info("[3/3]请求结束，RESPONSE: {}", jsonstring);

		int statusCode3 = result3.getCode();
		if (statusCode3 != HttpStatus.SC_OK) {
			logger.error("HTTP状态码异常: {}", statusCode3);
			return new VideoDto(0, "HTTP状态码异常: " + statusCode3);
		}
		logger.debug("HTTP状态码: 200");

		jsonstring = jsonstring.replace("\\/", "/");
		jsonstring = StringUtils.unicodeToCn(jsonstring);
		logger.info("视频信息API返回值解析结果: {}", jsonstring);
		VideoBo videoBo = gson.fromJson(jsonstring, VideoBo.class);
		VideoDto videoDto = new VideoDto(1, "success", datestamp, videoBo.getTitle().replace(".mp4", ""),
				paramsMap.get("thumb"), videoBo.getFile().getHd());

		// 使用爬虫爬取后存入DB，最后返回
		int row = videoDao.addVideo(videoDto.toVideoPo());
		if (row == 1) {
			logger.info("将解析结果存入DB -> 成功");
		} else {
			logger.info("将解析结果存入DB -> 失败");
		}

		return videoDto;
	}

}