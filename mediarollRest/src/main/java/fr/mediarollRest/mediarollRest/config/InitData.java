package fr.mediarollRest.mediarollRest.config;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.service.IAccountService;
import fr.mediarollRest.mediarollRest.model.Album;
import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.model.Movie;
import fr.mediarollRest.mediarollRest.model.Picture;
import fr.mediarollRest.mediarollRest.service.implementation.MediaService;

@Component
public class InitData implements ApplicationRunner {

	@Autowired
	private IAccountService accountService;

	@Autowired
	private MediaService mediaService;
	
	public void run(ApplicationArguments args) {
		
		Account account1 = new Account("g.tandu@hotmail.fr", "glodie", "Glodie", "Tandu");
		Account account2 = new Account("test@mail.fr","test","test","test");
		
		accountService.saveAccount(account1);
		accountService.saveAccount(account2);
		
		Picture p1 = new Picture(1, "beubeu01", "Beubeu à Evry", "25/10/2017", account1);
		Picture p2 = new Picture(2, "Beubeu02", "Beubeu en cours", "25/10/2017", account2);
		
		mediaService.saveMedia(p1);
		mediaService.saveMedia(p2);
		
		
		Movie m1 = new Movie(3, "Beubeu s'amuse", "Beubeu fait du breakDance", "25/10/2017", account1, "3:21");
		
		mediaService.saveMedia(m1);
		
		ArrayList<Media> medias = new ArrayList<Media>();
		medias.add(p1);
		medias.add(m1);
		
		Album a1 = new Album(4, "Les histoires de Beubeu", "Rien à dire", "25/10/2017", account1, p1, medias);
		
		mediaService.saveMedia(a1);
	}
	
	

}
