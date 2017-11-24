package fr.mediarollRest.mediarollRest.service.implementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.repository.AccountRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {
	@Mock
	private AccountRepository accountRepository;
	@InjectMocks
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Test
	public void testLoadUserByUsername() throws Exception {
		String mail = "test@mail.fr";
		Account account = new Account();
		account.setMail(mail);
		account.setPassword("test");
		
		when(accountRepository.findByMail(eq(mail))).thenReturn(Optional.of(account));
		
		UserDetails user = this.userDetailsServiceImpl.loadUserByUsername(mail);
		
		assertThat(user.getUsername()).isEqualTo(account.getMail());
		assertThat(user.getPassword()).isEqualTo(account.getPassword());
		
		
		verify(accountRepository).findByMail(eq(mail));
	}
	
	@Test(expected=UsernameNotFoundException.class)
	public void testLoadUserByUsernameNotFound() throws Exception {
		String mail = "test@mail.fr";
		
		when(accountRepository.findByMail(eq(mail))).thenReturn(Optional.empty());
		
		this.userDetailsServiceImpl.loadUserByUsername(mail);
		
		verify(accountRepository).findByMail(eq(mail));
	}

}
