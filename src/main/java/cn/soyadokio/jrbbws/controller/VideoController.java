package cn.soyadokio.jrbbws.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
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

	static Map<String, VideoDto> videos = Collections.synchronizedMap(new HashMap<String, VideoDto>());

	/**
	 * 查询视频列表
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public List<VideoDto> getVideoList() {
		List<VideoDto> vs = new ArrayList<VideoDto>(videos.values());
		return vs;
	}

	/**
	 * 创建一个视频
	 * @param video
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String postVideo(@ModelAttribute VideoDto video) {
		videos.put(video.getDatestamp(), video);
		return "{\"status\":1,\"errMsg\":\"create success\"}";
	}

	/**
	 * 根据datestamp查询一个视频
	 * @param datestamp
	 * @return
	 */
	@RequestMapping(value = "/{datestamp}", method = RequestMethod.GET)
	public VideoDto getVideo(@PathVariable String datestamp) {
		VideoDto videoDTO = videoService.getVideo(datestamp);
//		GsonBuilder gsonBuilder = new GsonBuilder();
//		gsonBuilder.serializeNulls();
//		Gson gson = gsonBuilder.create();
		Gson gson = new GsonBuilder().serializeNulls().create();
		logger.info("回传最终结果: {}", gson.toJson(videoDTO) + System.lineSeparator() + System.lineSeparator());
		return videoDTO;
	}

	/**
	 * 根据datestamp更新一个视频
	 * @param datestamp
	 * @param video
	 * @return
	 */
	@RequestMapping(value = "/{datestamp}", method = RequestMethod.PUT)
	public String putVideo(@PathVariable String datestamp, @ModelAttribute VideoDto video) {
		VideoDto v = videos.get(datestamp);
		v.setTitle(video.getTitle());
		v.setPoster(video.getPoster());
		v.setUrl(video.getUrl());
		videos.put(datestamp, v);
		return "{\"status\":3,\"errMsg\":\"update success\"}";
	}

	/**
	 * 根据datestamp删除一个视频
	 * @param datestamp
	 * @return
	 */
	@RequestMapping(value = "/{datestamp}", method = RequestMethod.DELETE)
	public String deleteUser(@PathVariable String datestamp) {
		videos.remove(datestamp);
		return "{\"status\":4,\"errMsg\":\"delete success\"}";
	}

}