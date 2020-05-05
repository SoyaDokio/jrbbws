package cn.soyadokio.jrbbws.domain.dto;

/**
 * 数据传输对象Video
 * @author SoyaDokio
 * @date	2020-03-28
 */
public class VideoDto {

	/**
	 * 状态码，0表失败；1表成功
	 */
	private Integer status;

	/**
	 * 状态描述
	 */
	private String errMsg;

	/**
	 * 字符串日期，形如：20200101
	 */
	private String datestamp;

	/**
	 * 标题，形如：20200101今日播报
	 */
	private String title;

	/**
	 * 视频封面图链接
	 */
	private String poster;

	/**
	 * 视频链接
	 */
	private String url;

	public VideoDto() {
		this.status = 0;
	}

	/**
	 * 实例化Video数据传输对象，用于传递异常状态和异常描述
	 * @param status		状态码，0表失败；1表成功
	 * @param errMsg		状态描述
	 */
	public VideoDto(Integer status, String errMsg) {
		this.status = status;
		this.errMsg = errMsg;
	}

	/**
	 * 实例化Video数据传输对象
	 * @param status		状态码，0表失败；1表成功
	 * @param errMsg		状态描述
	 * @param datestamp		字符串日期，形如：20200101
	 * @param title			标题，形如：20200101今日播报
	 * @param poster		视频封面图链接
	 * @param url			视频链接
	 */
	public VideoDto(Integer status, String errMsg, String datestamp, String title, String poster, String url) {
		this.status = status;
		this.errMsg = errMsg;
		this.datestamp = datestamp;
		this.title = title;
		this.poster = poster;
		this.url = url;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getDatestamp() {
		return datestamp;
	}

	public void setDatestamp(String datestamp) {
		this.datestamp = datestamp;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "VideoDTO [status=" + status + ", errMsg=" + errMsg + ", datestamp=" + datestamp + ", title=" + title
				+ ", poster=" + poster + ", url=" + url + "]";
	}

}
