package cn.soyadokio.jrbbws.domain.vo;

/**
 * 调用爬虫的返回对象
 * @author SoyaDokio
 * @date   2020-05-05
 */
public class HttpResult {

	/**
	 * HTTP请求返回的状态码
	 */
	private int code;

	/**
	 * HTTP请求返回的body
	 */
	private String body;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
