package cn.soyadokio.jrbbws.service;

import cn.soyadokio.jrbbws.domain.VideoDTO;

/**
 * 服务层-
 * @author SoyaDokio
 * @date	2020-03-28
 */
public interface VideoService {

	/**
	 * 根据日期获取视频
	 * @param datestamp	字符串日期，形如：20200101
	 * @return
	 */
	public VideoDTO getVideo(String datestamp);

}
