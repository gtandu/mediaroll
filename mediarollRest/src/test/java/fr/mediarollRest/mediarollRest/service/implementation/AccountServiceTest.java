package fr.mediarollRest.mediarollRest.service.implementation;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.repository.AccountRepository;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

	@Mock
	private AccountRepository accountRepository;
	@InjectMocks
	private AccountService accountService;

	private String mail;
	private Account account1;
	private Account account2;

	@Before
	public void init() {
		mail = "user1@mail.fr";
		account1 = new Account(mail, "account1", "Joe", "Biceps");
		account2 = new Account("user2@mail.fr", "account2", "Bibi", "Aumic");
	}

	@Test
	public void testFindByMail() throws Exception {
		// WHEN

		when(accountRepository.findByMail(eq(mail))).thenReturn(Optional.of(account1));

		// GIVEN
		accountService.findByMail(mail);

		// THEN
		verify(accountRepository).findByMail(eq(mail));

	}

	@Test
	public void testDeleteByMailSuccess() throws Exception {
		// WHEN
		int deleteOperationSuccess = 1;
		when(accountRepository.deleteByMail(eq(mail))).thenReturn(deleteOperationSuccess);

		// GIVEN
		accountService.deleteByMail(mail);

		// THEN
		verify(accountRepository).deleteByMail(eq(mail));
	}

	@Test
	public void testDeleteByMailFailed() throws Exception {
		// WHEN
		int deleteOperationFailed = 0;
		when(accountRepository.deleteByMail(eq(mail))).thenReturn(deleteOperationFailed);

		// GIVEN
		accountService.deleteByMail(mail);

		// THEN
		verify(accountRepository).deleteByMail(eq(mail));
	}

	@Test
	public void testFindAll() throws Exception {
		// WHEN
		when(accountRepository.findAll()).thenReturn(Arrays.asList(account1, account2));

		// GIVEN
		accountService.findAll();

		// THEN
		verify(accountRepository).findAll();

	}

	@Test
		public void testSaveAccount() throws Exception {
			// WHEN
			when(accountRepository.save(eq(account1))).thenReturn(account1);
	
			// GIVEN
			accountService.saveAccount(account1);
	
			// THEN
			verify(accountRepository).save(eq(account1));
		}

	@Test
		public void testIsAccountExistTrue() throws Exception {
			// WHEN
			when(accountRepository.findByMail(eq(mail))).thenReturn(Optional.of(account1));
	
			// GIVEN
			accountService.isAccountExist(account1);
	
			// THEN
			verify(accountRepository).findByMail(eq(mail));
	
		}

	@Test
		public void testIsAccountExistFalse() throws Exception {
			// WHEN
			when(accountRepository.findByMail(eq(mail))).thenReturn(Optional.empty());
	
			// GIVEN
			accountService.isAccountExist(account1);
	
			// THEN
			verify(accountRepository).findByMail(eq(mail));
		}

	@Test
	public void testUpdateUserWithUserExist() throws Exception {
		// WHEN
		when(accountRepository.findByMail(eq(mail))).thenReturn(Optional.of(account1));
		when(accountRepository.save(eq(account1))).thenReturn(account1);

		// GIVEN
		accountService.updateUser(account1);

		// THEN
		verify(accountRepository).findByMail(eq(mail));
		verify(accountRepository).save(eq(account1));
	}

	@Test
	public void testUpdateUserWithUserNotExist() throws Exception {
		// WHEN
		when(accountRepository.findByMail(eq(mail))).thenReturn(Optional.empty());

		// GIVEN
		accountService.updateUser(account1);

		// THEN
		verify(accountRepository).findByMail(eq(mail));
	}

}
