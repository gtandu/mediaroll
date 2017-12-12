package fr.mediarollRest.mediarollRest.web.controller;

import static fr.mediarollRest.mediarollRest.constant.Paths.ACCOUNTS;
import static fr.mediarollRest.mediarollRest.constant.Paths.MAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.mediarollRest.mediarollRest.exception.AccountNotFoundException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.resource.AccountResource;
import fr.mediarollRest.mediarollRest.service.IAccountService;
import fr.mediarollRest.mediarollRest.web.controller.AccountController;
import fr.mediarollRest.mediarollRest.web.resource.assembler.AccountAssembler;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IAccountService accountService;

	@MockBean
	private AccountAssembler accountAssembler;

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
		when(accountAssembler.toResource(any(Account.class))).thenReturn(new AccountResource(account));

		ResultActions result = mockMvc.perform(get(ACCOUNTS).contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andDo(print()).andReturn();

		verify(accountService).findAll();
		verify(accountAssembler).toResource(any(Account.class));
	}

	@Test
	@WithMockUser
	public void testFindAllNoUserExist() throws Exception {
		List<Account> usersList = Arrays.asList();

		when(accountService.findAll()).thenReturn(usersList);

		ResultActions result = mockMvc.perform(get(ACCOUNTS).contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNoContent());
		result.andDo(print());

		assertThat(result.andReturn().getResponse().getContentLength()).isEqualTo(0);

		verify(accountService).findAll();
		verify(accountAssembler, never()).toResource(any(Account.class));
	}

	@Test
	@WithMockUser
	public void testGetUserByMailUserExist() throws Exception {

		when(accountService.findByMail(eq(mail))).thenReturn(account);
		when(accountAssembler.toResource(any(Account.class))).thenReturn(new AccountResource(account));

		mockMvc.perform(get(ACCOUNTS + MAIL, mail)).andExpect(status().isOk()).andDo(print()).andReturn();

		verify(accountService).findByMail(eq(mail));
		verify(accountAssembler).toResource(any(Account.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	public void testGetUserByMailUserNotExist() throws Exception {

		when(accountService.findByMail(eq(mail))).thenThrow(AccountNotFoundException.class);

		ResultActions result = mockMvc.perform(get(ACCOUNTS + MAIL, mail));

		result.andExpect(status().isNotFound());
		result.andDo(print());

		assertThat(result.andReturn().getResponse().getContentLength()).isEqualTo(0);

		verify(accountService).findByMail(eq(mail));
		verify(accountAssembler, never()).toResource(any(Account.class));
	}

	@Test
	@WithMockUser
	public void testCreateUserNotExist() throws Exception {
		String accountJSON = objectMapper.writeValueAsString(account);

		when(accountService.accountExist(eq(account))).thenReturn(false);
		when(accountService.saveAccountAndEncodePassword(eq(account))).thenReturn(account);
		when(accountAssembler.toResource(any(Account.class))).thenReturn(new AccountResource(account));

		ResultActions result = mockMvc
				.perform(post(ACCOUNTS).contentType(MediaType.APPLICATION_JSON_UTF8).content(accountJSON));

		result.andExpect(status().isOk()).andDo(print());

		verify(accountService).accountExist(eq(account));
		verify(accountService).saveAccountAndEncodePassword(eq(account));
		verify(accountAssembler).toResource(any(Account.class));
	}

	@Test
	@WithMockUser
	public void testCreateUserAlreadyExist() throws Exception {
		String accountJSON = objectMapper.writeValueAsString(account);

		when(accountService.accountExist(eq(account))).thenReturn(true);

		ResultActions result = mockMvc
				.perform(post(ACCOUNTS).contentType(MediaType.APPLICATION_JSON_UTF8).content(accountJSON));

		result.andExpect(status().isConflict()).andDo(print());

		verify(accountService).accountExist(eq(account));
		verify(accountAssembler, never()).toResource(any(Account.class));
	}

	@Test
	@WithMockUser
	public void testUpdateUserExist() throws Exception {

		String accountJSON = objectMapper.writeValueAsString(account);

		when(accountService.updateUser(eq(account))).thenReturn(account);
		when(accountAssembler.toResource(any(Account.class))).thenReturn(new AccountResource(account));

		ResultActions result = mockMvc
				.perform(put(ACCOUNTS).contentType(MediaType.APPLICATION_JSON_UTF8).content(accountJSON));

		result.andExpect(status().isOk()).andDo(print());

		verify(accountService).updateUser(eq(account));
		verify(accountAssembler).toResource(any(Account.class));
	}

	@Test
	@WithMockUser
	public void testUpdateUserNotExist() throws Exception {

		String accountJSON = objectMapper.writeValueAsString(account);
		when(accountService.updateUser(eq(account))).thenThrow(new AccountNotFoundException());

		ResultActions result = mockMvc.perform(put(ACCOUNTS).contentType(MediaType.APPLICATION_JSON_UTF8).content(accountJSON));
			
		result.andExpect(status().isNotFound()).andDo(print());

		verify(accountService).updateUser(eq(account));
		verify(accountAssembler, never()).toResource(any(Account.class));
	}

	@Test
	@WithMockUser
	public void testDeleteUserByMailExist() throws Exception {

		when(accountService.deleteByMail(eq(mail))).thenReturn(true);

		ResultActions result = mockMvc.perform(delete(ACCOUNTS + MAIL, mail));
		
		result.andExpect(status().isNoContent()).andDo(print());

		verify(accountService).deleteByMail(eq(mail));
	}

	@Test
	@WithMockUser
	public void testDeleteUserByMailNotExist() throws Exception {

		when(accountService.deleteByMail(eq(mail))).thenReturn(false);

		ResultActions result = mockMvc.perform(delete(ACCOUNTS + MAIL, mail));
		
		result.andExpect(status().isNotFound()).andDo(print());

		verify(accountService).deleteByMail(eq(mail));
	}

}
