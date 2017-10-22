package fr.mediarollRest.mediarollRest.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.mediarollRest.mediarollRest.model.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
	
	Optional<Account> findByMail(@Param(value = "mail") String mail);
	
	int deleteByMail(@Param(value = "mail") String mail);
	

}
