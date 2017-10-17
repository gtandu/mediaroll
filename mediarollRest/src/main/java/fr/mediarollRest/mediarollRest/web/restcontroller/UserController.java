package fr.mediarollRest.mediarollRest.web.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.mediarollRest.mediarollRest.service.IUserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private IUserService userService;
	
	@RequestMapping(value="/{mail}", method = RequestMethod.GET)
	public ResponseEntity<?> getUserInfoByMail(@PathVariable("mail") String mail){
		
		return userService.findByMail(mail);
	}

}
