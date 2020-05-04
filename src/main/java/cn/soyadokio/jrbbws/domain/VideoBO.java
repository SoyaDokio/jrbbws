package cn.soyadokio.jrbbws.domain;

/**
 * 业务对象Video
 * @author SoyaDokio
 * @date	2020-03-28
 */
public class VideoBO {

	/**
	 * 状态码，应该是表成功，目前所见均为1
	 */
	private Integer status;

	/**
	 * 应该是视频ID
	 */
	private Long vid;

	/**
	 * 标题，如“20200101今日播报.mp4”
	 */
	private String title;

	/**
	 * （未知用途字段，目前所见均为空字串）
	 */
	private String tags;

	/**
	 * 视频首帧图链接（不含域名）
	 */
	private String image;

	/**
	 * （未知用途字段，目前所见每次有较小差异，但有相似，如：2639、2606）
	 */
	private Integer time;

	/**
	 * （未知用途字段，目前所见均为1）
	 */
	private String playCount;

	/**
	 * 视频链接对象，含3个不同清晰度的链接
	 */
	private UrlBO file;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getVid() {
		return vid;
	}

	public void setVid(Long vid) {
		this.vid = vid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public String getPlayCount() {
		return playCount;
	}

	public void setPlayCount(String playCount) {
		this.playCount = playCount;
	}

	public UrlBO getFile() {
		return file;
	}

	public void setFile(UrlBO file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "VideoBO [status=" + status + ", vid=" + vid + ", title=" + title + ", tags=" + tags + ", image=" + image
				+ ", time=" + time + ", playCount=" + playCount + ", file=" + file + "]";
	}

}
