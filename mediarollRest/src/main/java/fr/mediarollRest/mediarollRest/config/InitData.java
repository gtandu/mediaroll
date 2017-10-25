package fr.mediarollRest.mediarollRest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.service.IAccountService;

@Component
public class InitData implements ApplicationRunner {

	@Autowired
	private IAccountService accountService;

	public void run(ApplicationArguments args) {
		
		Account account1 = new Account("g.tandu@hotmail.fr", "glodie", "Glodie", "Tandu");
		Account account2 = new Account("test@mail.fr","test","test","test");
		
		accountService.saveAccount(account1);
		accountService.saveAccount(account2);
	}
	
	

}
