/**
 * 
 */
package io.github.azanx.shopping_list.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import io.github.azanx.shopping_list.config.WebMvcConfig;
import io.github.azanx.shopping_list.config.WebSecurityConfig;

/**
 * Tests if unlogged users have access only to login and register pages
 * @author Kamil Piwowarski
 *
 */
@ActiveProfiles("MvcController-test")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { WebMvcConfig.class, WebSecurityConfig.class, ServiceMockProvider.class})
@WebAppConfiguration
public class MvcControllerSecurityTest {

	@Autowired
	WebApplicationContext webApplicationContext;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders//
				.webAppContextSetup(webApplicationContext)//
				.apply(springSecurity())
				.build();
	}
	
	@Test
	public void loginForm_availableforAnonymous() throws Exception {
		mockMvc.perform(get("/login"))//
				.andDo(print())//
				.andExpect(view().name("login"));
	}

	@Test
	public void registerForm_availableforAnonymous() throws Exception {
		mockMvc.perform(get("/register"))//
				.andDo(print())//
				.andExpect(view().name("register"))//
				.andExpect(status().isOk())//
				.andExpect(model().attributeExists("newUser"));
	}

	@Test
	@WithMockUser
	public void loginForm_redirectsLoggedUser() throws Exception {
		mockMvc.perform(get("/login"))//
				.andDo(print())//
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/"));
	}
	
	@Test
	@WithMockUser
	public void registerForm_redirectsLoggedUser() throws Exception {
		mockMvc.perform(get("/register"))//
				.andDo(print())//
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/"));
	}
	
	@Test
	public void list_redirectsAnonymous() throws Exception {
		mockMvc.perform(get("/list"))//
				.andDo(print())//
				.andExpect(status().is3xxRedirection())//
				.andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	public void list_id_redirectsAnonymous() throws Exception {
		mockMvc.perform(get("/list/2"))//
				.andDo(print())//
				.andExpect(status().is3xxRedirection())//
				.andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	public void profile_redirectsAnonymous() throws Exception {
		mockMvc.perform(get("/profile"))//
				.andDo(print())//
				.andExpect(status().is3xxRedirection())//
				.andExpect(redirectedUrlPattern("**/login"));
	}
	
/////////////////////////////////////////
	
	@Test
	@WithMockUser
	public void list_availableForLoggedUser() throws Exception {
		mockMvc.perform(get("/list"))//
				.andDo(print())//
				.andExpect(status().isOk())//
				.andExpect(view().name("showAllLists"));//
	}
	
	@Test
	@WithMockUser
	public void list_id_availableForLoggedUser() throws Exception {
		mockMvc.perform(get("/list/2"))//
				.andDo(print())//
				.andExpect(status().isOk())//
				.andExpect(view().name("showList"));//
	}
	
	@Test
	@WithMockUser
	public void profile_availableForLoggedUser() throws Exception {
		mockMvc.perform(get("/profile"))//
				.andDo(print())//
				.andExpect(status().isOk())//
				.andExpect(view().name("userProfile"));//
	}
}
