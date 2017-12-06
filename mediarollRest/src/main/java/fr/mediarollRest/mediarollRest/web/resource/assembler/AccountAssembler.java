package fr.mediarollRest.mediarollRest.web.resource.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import fr.mediarollRest.mediarollRest.beans.AccountResource;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.web.resource.AccountController;

@Service
public class AccountAssembler extends ResourceAssemblerSupport<Account, AccountResource> {

	public AccountAssembler() {
		super(Account.class, AccountResource.class);
	}

	@Override
	public AccountResource toResource(Account account) {
		AccountResource accountResource = new AccountResource(account);
	    Link selfLink = linkTo(
		        methodOn(AccountController.class).getUserByMail(account.getMail()))
		        .withSelfRel();
	    Link accountsListLink = linkTo(methodOn(AccountController.class).findAll()).withRel("list accounts");
		
	    accountResource.add(selfLink);		
		accountResource.add(accountsListLink);
	    
		return accountResource;
	}

}
