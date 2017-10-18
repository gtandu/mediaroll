package fr.mediarollRest.mediarollRest.web.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.mediarollRest.mediarollRest.model.User;
import fr.mediarollRest.mediarollRest.service.IUserService;

@RestController
@RequestMapping("")
public class UserResource {

	@Autowired
	private IUserService userService;

	@RequestMapping(value = "/accounts", method = RequestMethod.GET)
	public ResponseEntity<List<User>> findAll() {
		List<User> listUser = userService.findAll();
		if (listUser.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(listUser, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/user/{mail:.+}", method = RequestMethod.GET)
	public ResponseEntity<User> getUserByMail(@PathVariable("mail") String mail) {
		Optional<User> userOptional = userService.findByMail(mail);

		if (userOptional.isPresent()) {
			return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public ResponseEntity<Void> createUser(@RequestBody User user) {
		if (userService.isUserExist(user)) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} else {
			userService.saveUser(user);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
	}

	@RequestMapping(value = "/user", method = RequestMethod.PUT)
	public ResponseEntity<User> updateUser(@RequestBody User user) {

		User userUpdated = userService.updateUser(user);

		if (userUpdated != null) {
			return new ResponseEntity<>(userUpdated, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(value = "/user/{mail:.+}", method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteUserByMail(@PathVariable("mail") String mail) {
		boolean deleteStatus = userService.deleteByMail(mail);
		if (deleteStatus) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

}
