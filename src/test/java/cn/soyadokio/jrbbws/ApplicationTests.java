package cn.soyadokio.jrbbws;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
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
	public void testVideoController() throws Exception {

		// 测试VideoController
		RequestBuilder request = null;

		// get查询datestamp为20200326的视频
		request = get("/jrbb/20200101");
		mvc.perform(request).andExpect(content().string(equalTo(
				"{\"status\":1,\"errMsg\":\"success\",\"datestamp\":\"20200101\",\"title\":\"20200101今日播报 \",\"poster\":\"http://img.cjyun.org/a/10125/202001/d12430cb3aee9d014f16491380385f2f.png\",\"url\":\"http://videoplus.cjyun.org/20200101/605546_605546_1577881046_transv.mp4\"}")));

	}

}
