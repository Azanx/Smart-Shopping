/**
 * 
 */
package io.github.azanx.shopping_list.controller;

import static io.github.azanx.shopping_list.controller.GlobalErrorModelResultMatchers.globalBindingErrors;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.ServletContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import io.github.azanx.shopping_list.config.WebMvcConfig;
import io.github.azanx.shopping_list.service.RepositoryService;
import io.github.azanx.shopping_list.service.exception.DuplicateUserException;

/**
 * Tests of register form available for unlogged users<br/> Without
 * configuring securtity, Principal would be null and we'll get null pointer
 * exception for other MvcControllers methods
 * 
 * @author Kamil Piwowarski
 *
 */
@ActiveProfiles("MvcController-test")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { WebMvcConfig.class, ServiceMockProvider.class})
@WebAppConfiguration
public class MvcControllerRegistrationTest {

	@Autowired
	WebApplicationContext webApplicationContext;
	
	@Autowired
	RepositoryService repositoryService;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup() {
		
		this.mockMvc = MockMvcBuilders//
				.webAppContextSetup(webApplicationContext)//
				.build();
	}
	
	@After
	public void cleanup() {
		reset(repositoryService); // RepositoryService Mock Bean is a singleton,
									// I want addUser to behave differently in
									// different tests so need to reset it
		//could've used @FixMethodOrder and multiple thenReturn in mock class,
		//but better not to rely on test execution order
	}

	/**
	 * checking if test configured properly
	 */
	@Test
	public void webApplicationContext_ProvidesServletContextAndMvcController() {
		ServletContext servletContext = webApplicationContext.getServletContext();

		assertNotNull(servletContext);
		assertTrue(servletContext instanceof MockServletContext);
		assertNotNull(webApplicationContext.getBean("mvcController"));
	}

	@Test
	public void registerForm_ForValidDataSucceeds() throws Exception {
		RequestBuilder request = preparePostForRegisterForm("userName", "validUserName");
		request = performPostRedirect(request);
		mockMvc.perform(request)//
				.andDo(print())//
				.andExpect(status().isOk())//
				.andExpect(model().attributeHasNoErrors("newUser"))//
				.andExpect(model().attributeExists("registered"))//
				.andExpect(model().attribute("registered", true));
	}

	@Test
	public void registerForm_tooShortUserNameFails() throws Exception {
		registerForm_checkField("userName", "");
		registerForm_checkField("userName", "1234");
	}

	@Test
	public void registerForm_notValidEmailFails() throws Exception {
		registerForm_checkField("email", "");
		registerForm_checkField("email", "test");
		registerForm_checkField("email", "test@test");
		registerForm_checkField("email", "test.pl");
	}

	@Test
	public void registerForm_notValidPasswordFails() throws Exception {
		registerForm_checkField("password", "");
		registerForm_checkField("password", "12");
	}

	@Test
	public void registerForm_differentPasswordsFails() throws Exception {
		// preparePostForRegisterForm sets passwordValidation to 123456 so
		// setting another valid value
		RequestBuilder request = preparePostForRegisterForm("password", "01234567");
		request = performPostRedirect(request);
		mockMvc.perform(request)//
				.andDo(print())//
				.andExpect(status().isOk())//
				.andExpect(globalBindingErrors().hasGlobalErrorCode("newUser", "FieldsVerification"))
				//field and its verification field have different values
				.andExpect(model().hasErrors());//
	}
	
	@Test
	public void registerForm_ifDuplicateUserFails() throws Exception {
		when(repositoryService.addUser(any())).thenThrow(new DuplicateUserException("test"));
		RequestBuilder request = preparePostForRegisterForm("userName", "validUserName");
		request = performPostRedirect(request);
		mockMvc.perform(request)//
				.andDo(print())//
				.andExpect(model().attributeHasFieldErrorCode("newUser", "userName", "DuplicateUser"));
	}

	/**
	 * Checks if posting register form for given field and value has validation
	 * error
	 * 
	 * @param fieldName
	 *            name of the AppUserDTO field under test
	 * @param fieldValue
	 *            value of the field
	 */
	private void registerForm_checkField(String fieldName, String fieldValue) throws Exception {
		RequestBuilder request = preparePostForRegisterForm(fieldName, fieldValue);
		request = performPostRedirect(request);
		mockMvc.perform(request)//
				.andDo(print())//
				.andExpect(status().isOk())//
				.andExpect(model().attributeHasFieldErrors("newUser", fieldName));
	}

	/**
	 * Prepare post statement for registering user, prepopulates params with
	 * valid data and changes one parameter to given value
	 * 
	 * @param paramName
	 *            name of AppUserDTO property
	 * @param paramValue
	 *            value of given property
	 * @return RequestBuilder
	 */
	private RequestBuilder preparePostForRegisterForm(String paramName, String paramValue) {
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.set("userName", "test1");
		parameters.set("email", "test@test.pl");
		parameters.set("password", "123456");
		parameters.set("passwordVerification", "123456");

		// replace value for parameter given in arguments
		parameters.set(paramName, paramValue);

		return post("/register")//
				.params(parameters);
	}

	/**
	 * Performs given requests, checks if it performs redirection, then returns
	 * new request containing flashAttributes from the redirection for further
	 * testing
	 * 
	 * @param request
	 *            request containing flash attributes to redirect method
	 * @return RequestBuilder
	 */
	private RequestBuilder performPostRedirect(RequestBuilder request) throws Exception {
		MvcResult postResult = mockMvc.perform(request)//
				.andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/register"))
				.andReturn();
		return MockMvcRequestBuilders.get("/register").flashAttrs(postResult.getFlashMap());
	}
}
