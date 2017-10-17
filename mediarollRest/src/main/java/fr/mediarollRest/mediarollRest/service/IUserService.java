package fr.mediarollRest.mediarollRest.service;

import org.springframework.http.ResponseEntity;

import fr.mediarollRest.mediarollRest.model.User;

public interface IUserService {
	
	public ResponseEntity<User> findByMail(String mail);
	

}
