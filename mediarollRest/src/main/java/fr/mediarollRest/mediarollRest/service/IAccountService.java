package fr.mediarollRest.mediarollRest.service;

import java.util.List;

import fr.mediarollRest.mediarollRest.exception.AccountNotFoundException;
import fr.mediarollRest.mediarollRest.model.Account;

public interface IAccountService {
	
	public Account findByMail(String mail) throws AccountNotFoundException;
	public Account saveAccountAndEncodePassword(Account account);
	public Account saveAccount(Account account);
	public Account updateUser(Account account);
	public boolean deleteByMail(String mail);
	public List<Account> findAll();
	public boolean isAccountExist(Account account);
	

}
