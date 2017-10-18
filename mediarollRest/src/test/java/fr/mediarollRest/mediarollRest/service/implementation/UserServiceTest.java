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

import fr.mediarollRest.mediarollRest.model.User;
import fr.mediarollRest.mediarollRest.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;
	@InjectMocks
	private UserService userService;

	private String mail;
	private User user1;
	private User user2;

	@Before
	public void init() {
		mail = "user1@mail.fr";
		user1 = new User(mail, "usr1", "Joe", "Biceps");
		user2 = new User("user2@mail.fr", "usr2", "Bibi", "Aumic");
	}

	@Test
	public void testFindByMail() throws Exception {
		// WHEN

		when(userRepository.findByMail(eq(mail))).thenReturn(Optional.of(user1));

		// GIVEN
		userService.findByMail(mail);

		// THEN
		verify(userRepository).findByMail(eq(mail));

	}

	@Test
	public void testDeleteByMailSuccess() throws Exception {
		// WHEN
		int deleteOperationSuccess = 1;
		when(userRepository.deleteByMail(eq(mail))).thenReturn(deleteOperationSuccess);

		// GIVEN
		userService.deleteByMail(mail);

		// THEN
		verify(userRepository).deleteByMail(eq(mail));
	}

	@Test
	public void testDeleteByMailFailed() throws Exception {
		// WHEN
		int deleteOperationFailed = 0;
		when(userRepository.deleteByMail(eq(mail))).thenReturn(deleteOperationFailed);

		// GIVEN
		userService.deleteByMail(mail);

		// THEN
		verify(userRepository).deleteByMail(eq(mail));
	}

	@Test
	public void testFindAll() throws Exception {
		// WHEN
		when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

		// GIVEN
		userService.findAll();

		// THEN
		verify(userRepository).findAll();

	}

	@Test
	public void testSaveUser() throws Exception {
		// WHEN
		when(userRepository.save(eq(user1))).thenReturn(user1);

		// GIVEN
		userService.saveUser(user1);

		// THEN
		verify(userRepository).save(eq(user1));
	}

	@Test
	public void testIsUserExistTrue() throws Exception {
		// WHEN
		when(userRepository.findByMail(eq(mail))).thenReturn(Optional.of(user1));

		// GIVEN
		userService.isUserExist(user1);

		// THEN
		verify(userRepository).findByMail(eq(mail));

	}

	@Test
	public void testIsUserExistFalse() throws Exception {
		// WHEN
		when(userRepository.findByMail(eq(mail))).thenReturn(Optional.empty());

		// GIVEN
		userService.isUserExist(user1);

		// THEN
		verify(userRepository).findByMail(eq(mail));
	}

	@Test
	public void testUpdateUserWithUserExist() throws Exception {
		// WHEN
		when(userRepository.findByMail(eq(mail))).thenReturn(Optional.of(user1));
		when(userRepository.save(eq(user1))).thenReturn(user1);

		// GIVEN
		userService.updateUser(user1);

		// THEN
		verify(userRepository).findByMail(eq(mail));
		verify(userRepository).save(eq(user1));
	}

	@Test
	public void testUpdateUserWithUserNotExist() throws Exception {
		// WHEN
		when(userRepository.findByMail(eq(mail))).thenReturn(Optional.empty());

		// GIVEN
		userService.updateUser(user1);

		// THEN
		verify(userRepository).findByMail(eq(mail));
	}

}
