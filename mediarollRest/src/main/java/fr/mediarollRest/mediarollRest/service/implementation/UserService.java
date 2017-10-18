package fr.mediarollRest.mediarollRest.service.implementation;

import java.util.List;

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
	public User findByMail(String mail) {
		return userRepository.findByMail(mail).get();
	}
	
	@Override
	@Transactional
	public boolean deleteByMail(String mail) {
		int deleteResult = userRepository.deleteByMail(mail);
		if(deleteResult == 1) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public List<User> findAll() {
		return (List<User>) userRepository.findAll();
	}

}
