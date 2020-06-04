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

}
