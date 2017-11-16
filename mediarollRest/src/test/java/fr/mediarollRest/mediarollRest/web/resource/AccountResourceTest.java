package fr.mediarollRest.mediarollRest.web.resource;

import static fr.mediarollRest.mediarollRest.constant.Paths.ACCOUNTS;
import static fr.mediarollRest.mediarollRest.constant.Paths.MAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
	private IAccountService accountService;

	private Account account;

	private ObjectMapper objectMapper = new ObjectMapper();
	
	private String mail;

	@Before
	public void init() {
		mail = "test@gmail.com";
		account = new Account(mail, "test", "test", "test");
	}

	@Test
	@WithMockUser
	public void testFindAllAtLeastOneUser() throws Exception {
		List<Account> usersList = Arrays.asList(account);

		when(accountService.findAll()).thenReturn(usersList);

		MvcResult result = mockMvc.perform(get(ACCOUNTS).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print()).andReturn();

		String accountJSON = objectMapper.writeValueAsString(usersList);
		JSONAssert.assertEquals(accountJSON, result.getResponse().getContentAsString(), false);
		
		verify(accountService).findAll();
	}
	
	@Test
	@WithMockUser
	public void testFindAllNoUserExist() throws Exception {
		List<Account> usersList = Arrays.asList();

		when(accountService.findAll()).thenReturn(usersList);

		MvcResult result = mockMvc.perform(get(ACCOUNTS).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent()).andDo(print()).andReturn();
		
		assertThat(result.getResponse().getContentLength()).isEqualTo(0);
		
		verify(accountService).findAll();
	}

	@Test
	@WithMockUser
	public void testGetUserByMailUserExist() throws Exception {
		
		when(accountService.findByMail(eq(mail))).thenReturn(Optional.of(account));
		
		mockMvc.perform(get(ACCOUNTS+MAIL,mail)).andExpect(status().isOk()).andDo(print()).andReturn();
		
		verify(accountService).findByMail(eq(mail));
	}
	
	@Test
	@WithMockUser
	public void testGetUserByMailUserNotExist() throws Exception {
		
		when(accountService.findByMail(eq(mail))).thenReturn(Optional.empty());
		
		MvcResult result = mockMvc.perform(get(ACCOUNTS+MAIL,mail)).andExpect(status().isNotFound()).andDo(print()).andReturn();
		
		assertThat(result.getResponse().getContentLength()).isEqualTo(0);
		
		verify(accountService).findByMail(eq(mail));
	}

	@Test
	@WithMockUser
	public void testCreateUserNotExist() throws Exception {
		when(accountService.isAccountExist(eq(account))).thenReturn(false);
		when(accountService.saveAccount(eq(account))).thenReturn(account);
		
		String accountJSON = objectMapper.writeValueAsString(account);
		mockMvc.perform(post(ACCOUNTS).contentType(MediaType.APPLICATION_JSON_UTF8).content(accountJSON)).andExpect(status().isCreated()).andDo(print());
		
		verify(accountService).isAccountExist(eq(account));
		verify(accountService).saveAccount(eq(account));
	}
	
	@Test
	@WithMockUser
	public void testCreateUserAlreadyExist() throws Exception {
		String accountJSON = objectMapper.writeValueAsString(account);

		when(accountService.isAccountExist(eq(account))).thenReturn(true);
		
		mockMvc.perform(post(ACCOUNTS).contentType(MediaType.APPLICATION_JSON_UTF8).content(accountJSON)).andExpect(status().isConflict()).andDo(print());
		
		verify(accountService).isAccountExist(eq(account));
	}

	@Test
	@WithMockUser
	public void testUpdateUserExist() throws Exception {
		
		String accountJSON = objectMapper.writeValueAsString(account);
		when(accountService.updateUser(eq(account))).thenReturn(account);
		
		mockMvc.perform(put(ACCOUNTS).contentType(MediaType.APPLICATION_JSON_UTF8).content(accountJSON)).andExpect(status().isOk()).andDo(print());
		
		verify(accountService).updateUser(eq(account));
	}
	
	@Test
	@WithMockUser
	public void testUpdateUserNotExist() throws Exception {
		
		String accountJSON = objectMapper.writeValueAsString(account);
		when(accountService.updateUser(eq(account))).thenReturn(null);
		
		mockMvc.perform(put(ACCOUNTS).contentType(MediaType.APPLICATION_JSON_UTF8).content(accountJSON)).andExpect(status().isNotFound()).andDo(print());
		
		verify(accountService).updateUser(eq(account));
	}

	@Test
	@WithMockUser
	public void testDeleteUserByMailExist() throws Exception {

		when(accountService.deleteByMail(eq(mail))).thenReturn(true);
		
		mockMvc.perform(delete(ACCOUNTS+MAIL,mail)).andExpect(status().isNoContent()).andDo(print()).andReturn();
		
		verify(accountService).deleteByMail(eq(mail));
	}
	
	@Test
	@WithMockUser
	public void testDeleteUserByMailNotExist() throws Exception {

		when(accountService.deleteByMail(eq(mail))).thenReturn(false);
		
		mockMvc.perform(delete(ACCOUNTS+MAIL,mail)).andExpect(status().isNotFound()).andDo(print()).andReturn();
		
		verify(accountService).deleteByMail(eq(mail));
	}

}
