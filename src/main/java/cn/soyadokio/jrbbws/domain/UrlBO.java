package cn.soyadokio.jrbbws.domain;

/**
 * 业务对象URL
 * @author SoyaDokio
 * @date	2020-03-28
 */
public class UrlBO {

	/**
	 * 标清视频链接
	 */
	private String sd;

	/**
	 * 高清视频链接
	 */
	private String ed;

	/**
	 * 超清视频链接
	 */
	private String hd;

	public String getSd() {
		return sd;
	}

	public void setSd(String sd) {
		this.sd = sd;
	}

	public String getEd() {
		return ed;
	}

	public void setEd(String ed) {
		this.ed = ed;
	}

	public String getHd() {
		return hd;
	}

	public void setHd(String hd) {
		this.hd = hd;
	}

	@Override
	public String toString() {
		return "UrlBO [sd=" + sd + ", ed=" + ed + ", hd=" + hd + "]";
	}

}
