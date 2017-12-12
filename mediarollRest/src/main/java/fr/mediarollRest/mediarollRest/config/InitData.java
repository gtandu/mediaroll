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
		
		Account account1 = new Account("glodie@mediaroll.xyz", "projet", "Glodie", "Tandu");
		Account account2 = new Account("corentin@mediaroll.xyz","projet","Corentin","Mapella");
		
		accountService.saveAccountAndEncodePassword(account1);
		accountService.saveAccountAndEncodePassword(account2);
		
		
	}
}