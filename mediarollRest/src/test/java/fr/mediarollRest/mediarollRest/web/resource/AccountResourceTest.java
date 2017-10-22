package fr.mediarollRest.mediarollRest.web.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.service.IAccountService;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountResource.class)
public class AccountResourceTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IAccountService userService;

	private Account user;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Before
	public void init() {
		user = new Account("test@gmail.com", "test", "test", "test");
	}

	@Test
	public void testFindAllAtLeastOneUser() throws Exception {
		List<Account> usersList = Arrays.asList(user);

		when(userService.findAll()).thenReturn(usersList);

		MvcResult result = mockMvc.perform(get("/accounts").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print()).andReturn();

		String userJSON = objectMapper.writeValueAsString(usersList);
		JSONAssert.assertEquals(userJSON, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void testFindAllNoUserExist() throws Exception {
		List<Account> usersList = Arrays.asList();

		when(userService.findAll()).thenReturn(usersList);

		MvcResult result = mockMvc.perform(get("/accounts").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent()).andDo(print()).andReturn();
		
		assertThat(result.getResponse().getContentLength()).isEqualTo(0);

	}

	@Test
	public void testGetUserByMail() throws Exception {
		
		
	}

	@Test
	public void testCreateUser() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public void testUpdateUser() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public void testDeleteUserByMail() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

}
