package fr.mediarollRest.mediarollRest.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.repository.AccountRepository;
import fr.mediarollRest.mediarollRest.service.IAccountService;

@Service
public class AccountService implements IAccountService {
	
	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Optional<Account> findByMail(String mail) {
		
		return accountRepository.findByMail(mail);
	}
	
	@Override
	@Transactional
	public boolean deleteByMail(String mail) {
		int deleteResult = accountRepository.deleteByMail(mail);
		int trueInt = 1;
		return deleteResult == trueInt ? true : false;
	}

	@Override
	public List<Account> findAll() {
		return (List<Account>) accountRepository.findAll();
	}

	@Override
	public Account saveAccount(Account account) {
		return accountRepository.save(account);
	}

	@Override
	public boolean isAccountExist(Account account) {
		Optional<Account> accountFromDb = accountRepository.findByMail(account.getMail());
		return accountFromDb.isPresent();
	}

	@Override
	public Account updateUser(Account account) {
		Optional<Account> accountOptional = accountRepository.findByMail(account.getMail());
		if(accountOptional.isPresent()){
			Account accountFromDb = accountOptional.get();
			accountFromDb.setFirstName(account.getFirstName());
			accountFromDb.setLastName(account.getLastName());
			return accountRepository.save(accountFromDb);
		}
		return null;
	}

}
