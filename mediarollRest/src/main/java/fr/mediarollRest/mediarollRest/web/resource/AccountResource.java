package fr.mediarollRest.mediarollRest.web.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.service.IAccountService;
import static fr.mediarollRest.mediarollRest.constant.Paths.*;

@RestController
public class AccountResource {

	@Autowired
	private IAccountService accountService;

	@RequestMapping(value = ACCOUNTS, method = RequestMethod.GET)
	public ResponseEntity<List<Account>> findAll() {
		List<Account> accountsList = accountService.findAll();
		if (accountsList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(accountsList, HttpStatus.OK);
		}
	}

	@RequestMapping(value = ACCOUNT+MAIL, method = RequestMethod.GET)
	public ResponseEntity<Account> getUserByMail(@PathVariable("mail") String mail) {
		Optional<Account> accountOptional = accountService.findByMail(mail);

		if (accountOptional.isPresent()) {
			return new ResponseEntity<>(accountOptional.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = ACCOUNT, method = RequestMethod.POST)
	public ResponseEntity<Void> createUser(@RequestBody Account account) {
		if (accountService.isAccountExist(account)) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} else {
			accountService.saveAccount(account);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
	}

	@RequestMapping(value = ACCOUNT, method = RequestMethod.PUT)
	public ResponseEntity<Account> updateUser(@RequestBody Account account) {

		Account userUpdated = accountService.updateUser(account);

		if (userUpdated != null) {
			return new ResponseEntity<>(userUpdated, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(value = ACCOUNT+MAIL, method = RequestMethod.DELETE)
	public ResponseEntity<Account> deleteUserByMail(@PathVariable("mail") String mail) {
		boolean deleteStatus = accountService.deleteByMail(mail);
		if (deleteStatus) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

}
