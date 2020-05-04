package cn.soyadokio.jrbbws.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.soyadokio.jrbbws.config.InvalidDelimiterException;

/**
 * String工具类
 * @author SoyaDokio
 * @date	2020-03-28
 */
public class StringUtils {

	private static final Pattern PATTERN = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

	private StringUtils() {
	}

	/**
	 * Unicode转中文方法
	 * @param unicode
	 * @return
	 */
	public static String unicodeToCn(String unicode) {
		Matcher matcher = PATTERN.matcher(unicode);
		char ch;
		while (matcher.find()) {
			ch = (char) Integer.parseInt(matcher.group(2), 16);
			unicode = unicode.replace(matcher.group(1), ch + "");
		}
		return unicode;
	}

	/**
	 * 中文转Unicode
	 * @param cn
	 * @return
	 */
	public static String cnToUnicode(String cn) {
		char[] utfBytes = cn.toCharArray();
		String unicodeBytes = "";
		for (int i = 0; i < utfBytes.length; i++) {
			String hexB = Integer.toHexString(utfBytes[i]);
			if (hexB.length() <= 2) {
				hexB = "00" + hexB;
			}
			unicodeBytes = unicodeBytes + "\\u" + hexB;
		}
		return unicodeBytes;
	}

	/**
	 * 验证指定字符串是否是合法日期
	 * @param datestamp		待验证的日期字符串，形如20200330
	 * @return
	 */
	public static boolean isDate(String datestamp) {
		if (datestamp.length() != 8 || datestamp.indexOf(" ") != -1) {
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		// 此处指定日期/时间解析是否不严格，false时为严格
		sdf.setLenient(false);
		try {
			sdf.parse(datestamp);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	/**
	 * 为日期字符串添加指定的分隔符，如：20200102 -> 2020-01-02或20200102 -> 2020/01/02
	 * @param datestamp		需要插入分隔符的日期字符串
	 * @param delimiter		日期字符串需要插入的分隔符
	 * @return
	 * @throws InvalidDelimiterException 非法分隔符异常，插入日期字符串的分隔符不属于" "、"-"、"/"、"."的任一种
	 */
	public static String delimitDatestring(String datestamp, String delimiter) throws InvalidDelimiterException {
		if (datestamp == null) {
			return null;
		}
		if (delimiter == null || delimiter.length() != 1 || !Pattern.matches("[ -/.]", delimiter)) {
			throw new InvalidDelimiterException("非法日期分隔符:" + delimiter);
		}
		StringBuffer sbuf = new StringBuffer(datestamp);
		sbuf.insert(6, "-");
		sbuf.insert(4, "-");
		return sbuf.toString();
	}

}
