package io.github.azanx.shopping_list.controller.mvc;

import io.github.azanx.shopping_list.config.WebMvcConfig;
import io.github.azanx.shopping_list.config.security.WebSecurityConfig;
import io.github.azanx.shopping_list.controller.ServiceMockProvider;
import io.github.azanx.shopping_list.service.RepositoryService;
import io.github.azanx.shopping_list.service.exception.ListNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("MvcController-test")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {WebMvcConfig.class, WebSecurityConfig.class, ServiceMockProvider.class})
@WebAppConfiguration
public class MvcControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    RepositoryService repositoryService;

    private MockMvc mockMvc;

    @Before
    public void setup() {

        this.mockMvc = MockMvcBuilders//
                .webAppContextSetup(webApplicationContext)//
                .apply(springSecurity())
                .build();
    }

    @After
    public void cleanup() {
        reset(repositoryService);
    }

    @Test
    @WithMockUser
    public void whenRequestsNonexistentListThenRedirectsToHomePage() throws Exception {
        when(repositoryService.getShoppingListWithItems(any(), any())).thenThrow(ListNotFoundException.class);
        RequestBuilder request = get("/list/999");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithMockUser
    public void whenGeneralExceptionThrownThenRedirectsTo500InternalErrorPage() throws Exception {
        when(repositoryService.getUser(any())).thenThrow(Exception.class);
        RequestBuilder request = get("/profile");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect((redirectedUrl("/internalError")));

    }
}
