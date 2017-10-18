package fr.mediarollRest.mediarollRest.web.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.mediarollRest.mediarollRest.model.User;
import fr.mediarollRest.mediarollRest.service.IUserService;

@RestController
@RequestMapping("/user")
public class UserResource {

	@Autowired
	private IUserService userService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<User>> findAll() {
		try {
			List<User> listUser = userService.findAll();
			return new ResponseEntity<List<User>>(listUser, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/{mail:.+}", method = RequestMethod.GET)
	public ResponseEntity<User> getUserInfoByMail(@PathVariable("mail") String mail) {
		try {
			User user = userService.findByMail(mail);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/{mail:.+}", method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteUserByMail(@PathVariable("mail") String mail) {
		boolean deleteStatus = userService.deleteByMail(mail);
		if (deleteStatus) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

}
