package fr.mediarollRest.mediarollRest.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.mediarollRest.mediarollRest.model.User;
import fr.mediarollRest.mediarollRest.repository.UserRepository;
import fr.mediarollRest.mediarollRest.service.IUserService;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public User findByMail(String mail) {
		return userRepository.findByMail(mail).get();
	}
	
	@Override
	public boolean deleteByMail(String mail) {
		return userRepository.deleteByMail(mail);
	}

}
