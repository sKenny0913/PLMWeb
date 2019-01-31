package com.plm.web;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;




@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WebApplication.class)
@WebAppConfiguration
public class WebApplicationTests {
	
//	@Autowired
//	private MemberApiRepository memberApiRepository; //加入MemberRepository
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	@Autowired
	ObjectMapper objectMapper;
	MockMvc mvc; //創建MockMvc類的物件
	
//	Memberaccount memberaccount ;
	@Before
	public void setup(){
//		memberaccount = new Memberaccount();
//		memberaccount.setCellphone("02135121");
//		memberaccount.setEmail("qewq@qwe.asd.s");
//		memberaccount.setPassword("qweqqq");
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	
	
	@Test
	public void contextLoads() throws Exception {
		String uri = "/PLMWeb/productLine?productCode=123";
	
		try{
			MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
//			MvcResult result = mvc.perform(MockMvcRequestBuilders.post(uri).content(objectMapper.writeValueAsString(memberaccount)).accept(MediaType.APPLICATION_JSON)).andReturn();
			int status = result.getResponse().getStatus();
			Assert.assertEquals("錯誤",200,status);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void Case2() {
		System.out.println("test");
	}

}
