package cn.soyadokio.jrbbws.repository.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cn.soyadokio.jrbbws.domain.po.VideoPo;
import cn.soyadokio.jrbbws.repository.dao.VideoDao;

/**
 * blah_blah
 * @author SoyaDokio
 * @date   2020-05-08
 */
public class VideoDaoImplTests {

	private VideoDao dao;

	@Before
	public void setUp() {
		dao = new VideoDaoImpl();
	}

	@Test
	public void testVideoDaoImpl() throws Exception {
		System.out.println("query:" + dao.queryVideo("20200501").toString());

		VideoPo videoPo0 = new VideoPo(7, "222", "20200501", "333", "444", "555");
		System.out.println("add    === effected row:" + dao.addVideo(videoPo0));

		System.out.println("query:" + dao.queryVideo("20200501").toString());

		videoPo0 = new VideoPo(1, "success", "20200501", "20200501今日播报",
				"http://img.cjyun.org/a/10125/202005/33f9383b67923c9d4e0786099269b345.jpg",
				"http://videoplus.cjyun.org/20200501/836344_836344_1588335817_transv.mp4");
		System.out.println("update === effected row:" + dao.updateVideo(videoPo0));

		System.out.println("query:" + dao.queryVideo("20200501").toString());

		System.out.println("query list:");
		List<VideoPo> videoPos = dao.queryVideos("20200401", "20200403");
		for (VideoPo videoPo : videoPos) {
			System.out.println(videoPo.toString());
		}
		System.out.println();

		VideoPo videoPo1 = new VideoPo(1, "success", "20200401", "20200401今日播报中",
				"http://img.cjyun.org/a/10125/202004/b4b59ee8035a8b2ba4144bfe6e4ba69f.png",
				"http://videoplus.cjyun.org/20200401/787126_787126_1585743273_transv.mp4");
		VideoPo videoPo2 = new VideoPo(1, "success", "20200402", "20200402今日播报(2)",
				"http://img.cjyun.org/a/10125/202004/d6a7871d4999dfd127af3270d1c52232.jpg",
				"http://videoplus.cjyun.org/20200402/789294_789294_1585832187_transv.mp4");
		VideoPo videoPo3 = new VideoPo(1, "success", "20200403", "20200403今日播报",
				"http://img.cjyun.org/a/10125/202004/865d4e4d659dbf267119992703c53bf8.png",
				"http://videoplus.cjyun.org/20200403/791178_791178_1585918044_transv.mp4");
		@SuppressWarnings("serial")
		List<VideoPo> videoPos2 = new ArrayList<VideoPo>() {
			{
				add(videoPo1);
				add(videoPo2);
				add(videoPo3);
			}
		};
		System.out.println("add list === effected row:" + dao.addVideos(videoPos2));

		System.out.println("query list:");
		List<VideoPo> videoPos3 = dao.queryVideos("20200401", "20200403");
		for (VideoPo videoPo : videoPos3) {
			System.out.println(videoPo.toString());
		}
	}

}
