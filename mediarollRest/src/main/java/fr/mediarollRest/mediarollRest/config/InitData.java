package fr.mediarollRest.mediarollRest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import fr.mediarollRest.mediarollRest.model.User;
import fr.mediarollRest.mediarollRest.repository.UserRepository;

@Component
public class InitData implements ApplicationRunner {

	@Autowired
	private UserRepository userRepository;

	public void run(ApplicationArguments args) {
		
		User user1 = new User("g.tandu@hotmail.fr", "glodie", "Glodie", "Tandu");
		User user2 = new User("test@mail.fr","test","test","test");
		
		userRepository.save(user1);
		userRepository.save(user2);
	}
	
	

}
