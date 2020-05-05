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
@RequestMapping("/jrbb")
public class VideoController {

	@Resource(name = "videoService")
	private VideoService videoService;

	private static final Logger logger = LoggerFactory.getLogger(VideoController.class);

	private Gson gson = new GsonBuilder().serializeNulls().create();

	/**
	 * 查询视频列表
	 * @return
	 */
	@RequestMapping(value = "/latest", method = RequestMethod.GET)
	public VideoDto getVideoList() {
		VideoDto videoDTO = videoService.getLatestVideo();
		logger.info("回传最终结果: {}", gson.toJson(videoDTO) + System.lineSeparator() + System.lineSeparator());
		return videoDTO;
	}

	/**
	 * 根据指定日期查询视频
	 * @param datestamp
	 * @return
	 */
	@RequestMapping(value = "/{datestamp}", method = RequestMethod.GET)
	public VideoDto getVideo(@PathVariable String datestamp) {
		VideoDto videoDTO = videoService.getVideo(datestamp);
		logger.info("回传最终结果: {}", gson.toJson(videoDTO) + System.lineSeparator() + System.lineSeparator());
		return videoDTO;
	}

}