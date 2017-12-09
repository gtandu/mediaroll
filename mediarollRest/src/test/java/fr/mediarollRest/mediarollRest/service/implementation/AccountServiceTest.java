package fr.mediarollRest.mediarollRest.service.implementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import fr.mediarollRest.mediarollRest.exception.AccountNotFoundException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.repository.AccountRepository;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private BCryptPasswordEncoder passwordEncoder;

	@InjectMocks
	private AccountService accountService;

	private String mail;
	private Account account1;
	private Account account2;

	@Before
	public void init() {
		mail = "user1@mail.fr";
		account1 = new Account(mail, "passwordaccount1", "Joe", "Biceps");
		account2 = new Account("user2@mail.fr", "account2", "Bibi", "Aumic");
	}

	@Test
	public void testFindByMail() throws Exception {
		// WHEN

		when(accountRepository.findByMail(eq(mail))).thenReturn(Optional.of(account1));

		// GIVEN
		Account account = accountService.findByMail(mail);
		
		assertThat(account).isNotNull();
		

		// THEN
		verify(accountRepository).findByMail(eq(mail));

	}

	@Test(expected = AccountNotFoundException.class)
	public void testFindByMailThrowAccountNotFoundException() throws Exception {
		// WHEN

		when(accountRepository.findByMail(eq(mail))).thenReturn(Optional.empty());

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
		boolean isDeleted = accountService.deleteByMail(mail);

		assertThat(isDeleted).isTrue();

		// THEN
		verify(accountRepository).deleteByMail(eq(mail));
	}

	@Test
	public void testDeleteByMailFailed() throws Exception {
		// WHEN
		int deleteOperationFailed = 0;
		when(accountRepository.deleteByMail(eq(mail))).thenReturn(deleteOperationFailed);

		// GIVEN
		boolean isDeleted = accountService.deleteByMail(mail);

		assertThat(isDeleted).isFalse();

		// THEN
		verify(accountRepository).deleteByMail(eq(mail));
	}

	@Test
	public void testFindAll() throws Exception {
		// WHEN
		when(accountRepository.findAll()).thenReturn(Arrays.asList(account1, account2));

		// GIVEN
		List<Account> accountsList = accountService.findAll();

		assertThat(accountsList).isNotEmpty();

		// THEN
		verify(accountRepository).findAll();

	}

	@Test
	public void testSaveAccountAndEncodePassword() throws Exception {
		// WHEN
		String passwordBeforeEncore = account1.getPassword();
		String encodedpassword = "azerty123456";

		when(passwordEncoder.encode(eq(passwordBeforeEncore))).thenReturn(encodedpassword);
		when(accountRepository.save(eq(account1))).thenReturn(account1);

		// GIVEN
		Account savedAccount = accountService.saveAccountAndEncodePassword(account1);

		assertThat(savedAccount).isNotNull();

		// THEN
		verify(passwordEncoder).encode(passwordBeforeEncore);
		verify(accountRepository).save(eq(account1));
	}

	@Test
	public void testAccountExistTrue() throws Exception {
		// WHEN
		when(accountRepository.findByMail(eq(mail))).thenReturn(Optional.of(account1));

		// GIVEN
		boolean accountExist = accountService.accountExist(account1);

		assertThat(accountExist).isTrue();

		// THEN
		verify(accountRepository).findByMail(eq(mail));

	}

	@Test
	public void testAccountExistFalse() throws Exception {
		// WHEN
		when(accountRepository.findByMail(eq(mail))).thenReturn(Optional.empty());

		// GIVEN
		boolean accountExist = accountService.accountExist(account1);

		assertThat(accountExist).isFalse();

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

	@Test(expected = AccountNotFoundException.class)
	public void testUpdateUserWithUserNotExist() throws Exception {
		// WHEN
		when(accountRepository.findByMail(eq(mail))).thenReturn(Optional.empty());

		// GIVEN
		accountService.updateUser(account1);

		// THEN
		verify(accountRepository).findByMail(eq(mail));
		verify(accountRepository, never()).save(eq(account1));
	}

	@Test
	public void testSaveAccount() throws Exception {
		when(accountRepository.save(any(Account.class))).thenReturn(new Account());

		Account savedAccount = accountService.saveAccount(new Account());

		assertThat(savedAccount).isNotNull();

		verify(accountRepository).save(any(Account.class));
	}

	@Test
	public void testIncreaseStorageSpace() throws Exception {

		// 1MB
		long fileSize = 1000000;
		double expectedStorageSpace = account1.getStorageSpace() + (fileSize / Account.STORAGE_SPACE_SIZE);
		Account savedAccount = new Account();
		savedAccount.setStorageSpace(expectedStorageSpace);

		when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

		double currentStorageSpace = accountService.increaseStorageSpace(account1, fileSize);

		assertThat(currentStorageSpace).isEqualTo(expectedStorageSpace);

		verify(accountRepository).save(any(Account.class));

	}

	@Test
	public void testDecreaseStorageSpace() throws Exception {
		// 1MB
		long fileSize = 1000000;
		double expectedStorageSpace = account1.getStorageSpace() - (fileSize / Account.STORAGE_SPACE_SIZE);
		Account savedAccount = new Account();
		savedAccount.setStorageSpace(expectedStorageSpace);

		when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

		double currentStorageSpace = accountService.decreaseStorageSpace(account1, fileSize);

		assertThat(currentStorageSpace).isEqualTo(expectedStorageSpace);

		verify(accountRepository).save(any(Account.class));
	}

}
