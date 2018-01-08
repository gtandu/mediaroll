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
		Account account3 = new Account("demo@mediaroll.xyz","projet","Demonstration","Projet");
		Account account4 = new Account("prod@mediaroll.xyz","prod2018","Production","Projet");
		
		accountService.saveAccountAndEncodePassword(account1);
		accountService.saveAccountAndEncodePassword(account2);
		accountService.saveAccountAndEncodePassword(account3);
		accountService.saveAccountAndEncodePassword(account4);
		
	}
}