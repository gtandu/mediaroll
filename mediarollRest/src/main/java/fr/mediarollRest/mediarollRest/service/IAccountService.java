package fr.mediarollRest.mediarollRest.service;

import java.util.List;
import java.util.Optional;

import fr.mediarollRest.mediarollRest.model.Account;

public interface IAccountService {
	
	public Optional<Account> findByMail(String mail);
	public Account saveAccountAndEncodePassword(Account account);
	public Account saveAccount(Account account);
	public Account updateUser(Account account);
	public boolean deleteByMail(String mail);
	public List<Account> findAll();
	public boolean isAccountExist(Account account);
	

}
