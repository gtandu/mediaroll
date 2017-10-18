package fr.mediarollRest.mediarollRest.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.mediarollRest.mediarollRest.model.User;
import fr.mediarollRest.mediarollRest.repository.UserRepository;
import fr.mediarollRest.mediarollRest.service.IUserService;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public Optional<User> findByMail(String mail) {
		
		return userRepository.findByMail(mail);
	}
	
	@Override
	@Transactional
	public boolean deleteByMail(String mail) {
		int deleteResult = userRepository.deleteByMail(mail);
		int trueInt = 1;
		return deleteResult == trueInt ? true : false;
	}

	@Override
	public List<User> findAll() {
		return (List<User>) userRepository.findAll();
	}

	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public boolean isUserExist(User user) {
		Optional<User> userFromDb = userRepository.findByMail(user.getMail());
		return userFromDb.isPresent();
	}

	@Override
	public User updateUser(User user) {
		Optional<User> userOptional = userRepository.findByMail(user.getMail());
		if(userOptional.isPresent()){
			User userFromDb = userOptional.get();
			userFromDb.setFirstName(user.getFirstName());
			userFromDb.setLastName(user.getLastName());
			return userRepository.save(userFromDb);
		}
		return null;
	}

}
