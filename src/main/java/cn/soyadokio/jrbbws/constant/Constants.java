package cn.soyadokio.jrbbws.constant;

/**
 * 常量类
 * @author SoyaDokio
 * @date	2020-03-28
 */
public class Constants {

	/**
	 * 临界时间点-小时，与临界时间点-分钟组合使用，该时间点以前视频可能尚未更新
	 */
	public static final int BOUNDARY_HOUR = 21;

	/**
	 * 临界时间点-分钟，与临界时间点-小时组合使用，该时间点以前视频可能尚未更新
	 */
	public static final int BOUNDARY_MINUTE = 45;

	/**
	 * 指定HTTP返回实体编码
	 */
	public static final String HTTP_ENTITY_CHARSET = "UTF-8";

	/**
	 * 指定URLEncode/URLDecode编码
	 */
	public static final String URL_CODE_CHARSET = "UTF-8";

//	/**
//	 * 目标站关键词检索的请求链接
//	 */
//	public static final String SEARCH_URL = "http://xiangyang.cjyun.org/s?wd=";

//	/**
//	 * 目标站关键词检索时的关键词
//	 */
//	public static final String SEARCH_CONTENT = "今日播报";
//
//	/**
//	 * HTML中确定是否检索成功的关键词
//	 */
//	public static final String INDEXOF_CONTENT = "搜索结果&nbsp;共&nbsp;1&nbsp;个";

//	/**
//	 * URL中请求地址和参数的分隔符
//	 */
//	public static final String URL_PARAM_DELIMITER = "?";
	
//	/**
//	 * 目标站的H5域名（PC端视频通过Flash播放，无法获取视频链接）
//	 */
//	public static final String H5_URL = "https://m-xiangyang.cjyun.org";
//
//	/**
//	 * 目标站获取视频信息JSON的请求链接
//	 */
//	public static final String GETJSON_URL = "https://app.cjyun.org/video/player/video?sid=";
//
//	/**
//	 * 目标站获取视频信息JSON的请求链接的第二个参数
//	 */
//	public static final String GETJSON_PARAM_2 = "&vid=";
//
//	/**
//	 * 目标站获取视频信息JSON的请求链接的第三个参数
//	 */
//	public static final String GETJSON_PARAM_3 = "&type=video";

}
