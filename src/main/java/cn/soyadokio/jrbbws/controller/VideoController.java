package cn.soyadokio.jrbbws.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cn.soyadokio.jrbbws.domain.dto.VideoDto;
import cn.soyadokio.jrbbws.service.VideoService;

/**
 * 控制器层
 * @author SoyaDokio
 * @date	2020-03-28
 */
@RestController
@RequestMapping("/jrbbws")
public class VideoController {

	@Resource(name = "videoService")
	private VideoService videoService;

	private static final Logger logger = LoggerFactory.getLogger(VideoController.class);

	private Gson gson = new GsonBuilder().serializeNulls().create();

	/**
	 * 查询最近的一个视频
	 * @return
	 */
	@RequestMapping(value = "/latest", method = RequestMethod.GET)
	public VideoDto getLatestVideo() {
		logger.info("↓ getLatestVideo() 开始查询 ↓");
		logger.info("指定日期[latest]视频信息");
		VideoDto videoDto = videoService.getLatestVideo();
		logger.info("指定日期[latest]视频信息: {}", gson.toJson(videoDto));
		logger.info("↑ getLatestVideo() 查询结果 ↑ {}", System.lineSeparator());
		return videoDto;
	}

	/**
	 * 根据指定日期查询视频
	 * @param datestamp
	 * @return
	 */
	@RequestMapping(value = "/{datestamp}", method = RequestMethod.GET)
	public VideoDto getVideo(@PathVariable String datestamp) {
		logger.info("↓ getVideo() 开始查询 ↓");
		logger.info("指定日期[" + datestamp + "]视频信息");
		VideoDto videoDto = videoService.getVideo(datestamp);
		logger.info("指定日期[" + datestamp + "]视频信息: {}", gson.toJson(videoDto));
		logger.info("↑ getVideo() 查询结果 ↑ {}", System.lineSeparator());
		return videoDto;
	}

}