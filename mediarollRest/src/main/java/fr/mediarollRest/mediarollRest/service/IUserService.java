package fr.mediarollRest.mediarollRest.service;

import java.util.List;
import java.util.Optional;

import fr.mediarollRest.mediarollRest.model.User;

public interface IUserService {
	
	public Optional<User> findByMail(String mail);
	public User saveUser(User user);
	public User updateUser(User user);
	public boolean deleteByMail(String mail);
	public List<User> findAll();
	public boolean isUserExist(User user);
	

}
