package fr.mediarollRest.mediarollRest.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.repository.AccountRepository;

import static java.util.Collections.emptyList;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
	@Autowired
	private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByMail(mail);
        if (!accountOptional.isPresent()) {
            throw new UsernameNotFoundException(mail);
        }
        Account account = accountOptional.get();
		return new User(account.getUsername(), account.getPassword(), emptyList());
    }

}
