package fr.mediarollRest.mediarollRest.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import fr.mediarollRest.mediarollRest.model.User;
import fr.mediarollRest.mediarollRest.repository.UserRepository;
import fr.mediarollRest.mediarollRest.service.IUserService;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public ResponseEntity<User> findByMail(String mail) {
		Optional<User> userOptional = userRepository.findByMail(mail);
		try {
			User user = userOptional.get();
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
	}

}
