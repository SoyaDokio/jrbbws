package cn.soyadokio.jrbbws.service;

import cn.soyadokio.jrbbws.domain.dto.VideoDto;

/**
 * 服务层-接口定义
 * @author SoyaDokio
 * @date	2020-03-28
 */
public interface VideoService {

	/**
	 * 获取最近日期视频
	 * 若当前时间早于21:45则获取昨日视频；若当前时间晚于21:45则获取今日视频
	 * @return
	 */
	public VideoDto getLatestVideo();

	/**
	 * 根据日期获取视频
	 * @param datestamp	字符串日期，形如：20200101
	 * @return
	 */
	public VideoDto getVideo(String datestamp);

}
