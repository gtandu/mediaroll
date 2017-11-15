package fr.mediarollRest.mediarollRest.web.resource;

import static fr.mediarollRest.mediarollRest.constant.Paths.ACCOUNTS;
import static fr.mediarollRest.mediarollRest.constant.Paths.MAIL;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value="accounts", description="Operations pertaining to accounts in MediaRoll")
public class AccountResource {

	@Autowired
	private IAccountService accountService;

	@ApiOperation(value = "View a list of accounts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 204, message = "No users in db"),
    })
	@RequestMapping(value = ACCOUNTS, method = RequestMethod.GET)
	public ResponseEntity<List<Account>> findAll() {
		List<Account> accountsList = accountService.findAll();
		if (accountsList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			for (Account account : accountsList) {
				account.add(linkTo(methodOn(AccountResource.class).getUserByMail(account.getMail())).withSelfRel());

			}
			return new ResponseEntity<>(accountsList, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "View an user account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user"),
            @ApiResponse(code = 404, message = "No users in db"),
    })
	@RequestMapping(value = ACCOUNTS+MAIL, method = RequestMethod.GET)
	public ResponseEntity<Account> getUserByMail(@PathVariable("mail") String mail) {
		Optional<Account> accountOptional = accountService.findByMail(mail);

		if (accountOptional.isPresent()) {
			
			Account account = accountOptional.get();
			buildLink(mail, account);
			return new ResponseEntity<>(account, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	private void buildLink(String mail, Account account) {
		account.add(linkTo(methodOn(AccountResource.class).getUserByMail(mail)).withSelfRel());
		account.add(linkTo(methodOn(AccountResource.class).findAll()).withRel("list accounts"));
	}

	@ApiOperation(value = "Create a user account")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created"),
            @ApiResponse(code = 409, message = "User already exist in database"),
    })
	@RequestMapping(value = ACCOUNTS, method = RequestMethod.POST)
	public ResponseEntity<Void> createUser(@RequestBody Account account) {
		if (accountService.isAccountExist(account)) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} else {
			accountService.saveAccount(account);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
	}

	@ApiOperation(value = "Update an user account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User updated"),
            @ApiResponse(code = 404, message = "User not found in database"),
    })
	@RequestMapping(value = ACCOUNTS, method = RequestMethod.PUT)
	public ResponseEntity<Account> updateUser(@RequestBody Account account) {

		Account userUpdated = accountService.updateUser(account);

		if (userUpdated != null) {
			return new ResponseEntity<>(userUpdated, HttpStatus.OK);
		} else {
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
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

}
