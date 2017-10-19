package fr.mediarollRest.mediarollRest.web.resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.mediarollRest.mediarollRest.service.IUserService;

@RunWith(MockitoJUnitRunner.class)
public class UserResourceTest {
	@Mock
	private IUserService userService;
	@InjectMocks
	private UserResource userResource;

	@Test
	public void testFindAll() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public void testGetUserByMail() throws Exception {
		throw new RuntimeException("not yet implemented");
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
