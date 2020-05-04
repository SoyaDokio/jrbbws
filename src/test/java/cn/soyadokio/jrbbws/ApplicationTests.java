package cn.soyadokio.jrbbws;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Application的测试类
 * @author SoyaDokio
 * @date	2020-03-28
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ApplicationTests {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void getHello() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/hello").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(
						equalTo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))));
	}

	@Test
	public void testVideoController() throws Exception {

		// 测试VideoController
		RequestBuilder request = null;

		// 1、get查询video列表，应该为空
		request = get("/jrbb/");
		mvc.perform(request).andExpect(status().isOk()).andExpect(content().string(equalTo("[]")));

		// 2、post创建一个视频
		request = post("/jrbb/").param("datestamp", "20200326").param("title", "20200326今日播报")
				.param("poster", "http://img.cjyun.org/a/10125/202003/0edefb7f600add28dab22f45eb60aa4e.jpg")
				.param("url", "http://videoplus.cjyun.org/20200326/774865_774865_1585224984_transv.mp4");
		mvc.perform(request)
//				.andDo(MockMvcResultHandlers.print())// 想要看到更多的细节信息则在.perform()后加本行代码，下同
				.andExpect(content().string(equalTo("{\"status\":1,\"errMsg\":\"create success\"}")));

		// 3、get查询视频列表，应该有刚才插入的数据
		request = get("/jrbb/");
		mvc.perform(request).andExpect(status().isOk()).andExpect(content().string(equalTo(
				"[{\"status\":0,\"errMsg\":null,\"datestamp\":\"20200326\",\"title\":\"20200326今日播报\",\"poster\":\"http://img.cjyun.org/a/10125/202003/0edefb7f600add28dab22f45eb60aa4e.jpg\",\"url\":\"http://videoplus.cjyun.org/20200326/774865_774865_1585224984_transv.mp4\"}]")));

		// 4、put更新datestamp为20200326的视频
		request = put("/jrbb/20200326").param("datestamp", "20200326").param("title", "测试终极大师")
				.param("poster", "http://img.cjyun.org/a/10125/202003/0edefb7f600add28dab22f45eb60aa4e.jpg")
				.param("url", "http://videoplus.cjyun.org/20200326/774865_774865_1585224984_transv.mp4");
		mvc.perform(request).andExpect(content().string(equalTo("{\"status\":3,\"errMsg\":\"update success\"}")));

		// 5、get查询datestamp为20200326的视频
		request = get("/jrbb/20200326");
		mvc.perform(request).andExpect(content().string(equalTo(
				"{\"status\":1,\"errMsg\":\"success\",\"datestamp\":\"20200326\",\"title\":\"20200326今日播报\",\"poster\":\"http://img.cjyun.org/a/10125/202003/7f4d949d068e9c62400c07179b760f44.png\",\"url\":\"http://videoplus.cjyun.org/20200326/774865_774865_1585224984_transv.mp4\"}")));

		// 6、del删除datestamp为20200326的视频
		request = delete("/jrbb/20200326");
		mvc.perform(request).andExpect(content().string(equalTo("{\"status\":4,\"errMsg\":\"delete success\"}")));

		// 7、get查询视频列表，应该为空
		request = get("/jrbb/");
		mvc.perform(request).andExpect(status().isOk()).andExpect(content().string(equalTo("[]")));

	}

}
