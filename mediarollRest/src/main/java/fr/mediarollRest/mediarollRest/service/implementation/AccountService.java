package fr.mediarollRest.mediarollRest.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.mediarollRest.mediarollRest.exception.MailNotFoundException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.repository.AccountRepository;
import fr.mediarollRest.mediarollRest.service.IAccountService;

@Service
public class AccountService implements IAccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public Account findByMail(String mail) throws MailNotFoundException {
		
		
		Optional<Account> optionalAccount = accountRepository.findByMail(mail);
		if(optionalAccount.isPresent()){
			return optionalAccount.get();
		}
		else
		{
			throw new MailNotFoundException();
		}
	}

	@Override
	@Transactional
	public boolean deleteByMail(String mail) {
		int deleteResult = accountRepository.deleteByMail(mail);
		return deleteResult != 0 ? true : false;
	}

	@Override
	public List<Account> findAll() {
		return (List<Account>) accountRepository.findAll();
	}

	@Override
	public Account saveAccountAndEncodePassword(Account account) {
		account.setPassword(passwordEncoder.encode(account.getPassword()));
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
		if (accountOptional.isPresent()) {
			Account accountFromDb = accountOptional.get();
			accountFromDb.setFirstname(account.getFirstname());
			accountFromDb.setLastname(account.getLastname());
			return accountRepository.save(accountFromDb);
		}
		return null;
	}

	@Override
	public Account saveAccount(Account account) {
		return accountRepository.save(account);
	}

}
