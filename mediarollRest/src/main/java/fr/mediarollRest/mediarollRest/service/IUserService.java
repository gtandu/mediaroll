package fr.mediarollRest.mediarollRest.service;

import java.util.List;

import fr.mediarollRest.mediarollRest.model.User;

public interface IUserService {
	
	public User findByMail(String mail);
	public boolean deleteByMail(String mail);
	public List<User> findAll();
	

}
