package cn.soyadokio.jrbbws.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 异常处理控制器
 * @author SoyaDokio
 * @date   2020-05-14
 */
@Controller
public class VideoErrorController implements ErrorController {

	private static final Logger logger = LoggerFactory.getLogger(VideoErrorController.class);

	@RequestMapping("/error")
	public String handleError(HttpServletRequest request) {
		// 获取statusCode:404,500
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		logger.info("HTTP状态码: [{}]", statusCode);
		if (statusCode == 404) {
			logger.info("跳转到[/error/404.html]");
			return "/error/404.html";
		} else {
			logger.info("跳转到[/error/500.html]");
			return "/error/500.html";
		}
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

}
