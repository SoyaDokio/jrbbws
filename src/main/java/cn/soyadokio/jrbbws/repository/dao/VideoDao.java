package cn.soyadokio.jrbbws.repository.dao;

import java.util.List;

import cn.soyadokio.jrbbws.domain.po.VideoPo;

/**
 * DAO层-接口定义
 * @author SoyaDokio
 * @date   2020-05-05
 */
public interface VideoDao {

	/**
	 * 根据日期从DB获取视频
	 * @param datestamp		字符串日期，形如：20200101
	 * @return
	 */
	public VideoPo queryVideo(String datestamp);

	/**
	 * 从DB获取指定日期范围的所有视频
	 * @param datestampFrom		字符串日期，形如：20200101
	 * @param datestampTo		字符串日期，形如：20200105
	 * @return
	 */
	public List<VideoPo> queryVideos(String datestampFrom, String datestampTo);

	/**
	 * 将给定的Video对象插入DB
	 * @param videoPo	待插入DB的Video对象
	 * @return			受影响记录的条数
	 */
	public int addVideo(VideoPo videoPo);

	/**
	 * 将给定的list中的所有Video对象插入DB
	 * @param videoPos		待插入DB的Video对象的List
	 * @return				受影响记录的条数
	 */
	public int addVideos(List<VideoPo> videoPos);

	/**
	 * 更新给定的Video对象的datestamp字段所对应的记录的其它字段
	 * @param videoPo	定位记录所需datestamp和更新值所在的JavaBean
	 * @return			受影响记录的条数
	 */
	public int updateVideo(VideoPo videoPo);

}
