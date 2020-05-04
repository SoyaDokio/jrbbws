package cn.soyadokio.jrbbws.config;

/**
 * 为日期字符串插入分隔符时，若分隔符不合法则抛出此异常
 * 合法分隔符:" "、"-"、"/"、"."
 * @author SoyaDokio
 * @date	2020-03-28
 */
public class InvalidDelimiterException extends Exception {

	private static final long serialVersionUID = 1602539019252757260L;

	public InvalidDelimiterException() {
		super();
	}

	public InvalidDelimiterException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidDelimiterException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidDelimiterException(String message) {
		super(message);
	}

	public InvalidDelimiterException(Throwable cause) {
		super(cause);
	}

}
