package fr.mediarollRest.mediarollRest.web.controller;

import static fr.mediarollRest.mediarollRest.constant.Paths.ACCOUNTS;
import static fr.mediarollRest.mediarollRest.constant.Paths.MAIL;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.mediarollRest.mediarollRest.exception.AccountNotFoundException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.resource.AccountResource;
import fr.mediarollRest.mediarollRest.service.IAccountService;
import fr.mediarollRest.mediarollRest.web.resource.assembler.AccountAssembler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value="accounts", description="Operations pertaining to accounts in MediaRoll")
public class AccountController {

	@Autowired
	private IAccountService accountService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private AccountAssembler accountAssembler;
	
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	

	@ApiOperation(value = "View a list of accounts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 204, message = "No users in db"),
    })
	@RequestMapping(value = ACCOUNTS, method = RequestMethod.GET)
	public ResponseEntity<List<AccountResource>> findAll() {
		List<AccountResource> accountsResourceList = new ArrayList<AccountResource>();
		
		List<Account> accountsList = accountService.findAll();
		if (accountsList.isEmpty()) {
			logger.info("Account list is empty. Add an account");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			for (Account account : accountsList) {
				accountsResourceList.add(accountAssembler.toResource(account));
			}
			return new ResponseEntity<>(accountsResourceList, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "View an user account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user"),
            @ApiResponse(code = 404, message = "No users in db"),
    })
	@RequestMapping(value = ACCOUNTS+MAIL, method = RequestMethod.GET)
	public ResponseEntity<AccountResource> getUserByMail(@PathVariable("mail") String mail) {
		try {
			Account account = accountService.findByMail(mail);
			AccountResource accountResource = accountAssembler.toResource(account);
			return new ResponseEntity<>(accountResource, HttpStatus.OK);
		} catch (AccountNotFoundException e) {
			logger.error(messageSource.getMessage("error.account.not.found",null, Locale.FRANCE), mail);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		}
	}

	

	@ApiOperation(value = "Create a user account")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created"),
            @ApiResponse(code = 409, message = "User already exist in database"),
    })
	@RequestMapping(value = ACCOUNTS, method = RequestMethod.POST)
	public ResponseEntity<AccountResource> createUser(@RequestBody Account account) {
		if (accountService.isAccountExist(account)) {
			logger.error(messageSource.getMessage("error.account.not.found",null, Locale.FRANCE), account.getMail());
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} else {
			Account accountSaved = accountService.saveAccountAndEncodePassword(account);
			AccountResource accountResource = accountAssembler.toResource(accountSaved);
			return new ResponseEntity<>(accountResource, HttpStatus.CREATED);
		}
	}

	@ApiOperation(value = "Update an user account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User updated"),
            @ApiResponse(code = 404, message = "User not found in database"),
    })
	@RequestMapping(value = ACCOUNTS, method = RequestMethod.PUT)
	public ResponseEntity<AccountResource> updateUser(@RequestBody Account account) {

		Account accountUpdated = accountService.updateUser(account);

		if (accountUpdated != null) {
			AccountResource accountResource = accountAssembler.toResource(accountUpdated);
			return new ResponseEntity<>(accountResource, HttpStatus.OK);
		} else {
			logger.error(messageSource.getMessage("error.account.not.found",null, Locale.FRANCE), account.getMail());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@ApiOperation(value = "Delete an user account")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "User deleted"),
            @ApiResponse(code = 404, message = "User not found in database"),
    })
	@RequestMapping(value = ACCOUNTS+MAIL, method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteUserByMail(@PathVariable("mail") String mail) {
		boolean deleteStatus = accountService.deleteByMail(mail);
		if (deleteStatus) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			logger.error(messageSource.getMessage("error.account.not.found",null, Locale.FRANCE), mail);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}


}
