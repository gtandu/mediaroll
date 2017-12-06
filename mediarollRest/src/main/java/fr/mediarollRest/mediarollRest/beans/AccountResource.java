package fr.mediarollRest.mediarollRest.beans;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import fr.mediarollRest.mediarollRest.model.Account;

public class AccountResource extends ResourceSupport {

	@JsonUnwrapped
	private Account account;

	public AccountResource(Account account) {
		this.account = account;
	}

	public Account getAccount() {
		return account;
	}

}
