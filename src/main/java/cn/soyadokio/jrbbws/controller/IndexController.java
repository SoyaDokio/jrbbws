package cn.soyadokio.jrbbws.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制器层
 * @author SoyaDokio
 * @date   2020-05-14
 */
@RestController
public class IndexController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	/**
	 * 访问根目录时自动跳转
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public void index(HttpServletRequest request, HttpServletResponse response) {
		logger.info("访问目录: [/]，自动跳转到 API 说明页");
		try {
			response.sendRedirect("/readme.html");
		} catch (IOException e) {
			logger.warn("跳转到 API 说明页失败");
			e.printStackTrace();
		}
	}

}
