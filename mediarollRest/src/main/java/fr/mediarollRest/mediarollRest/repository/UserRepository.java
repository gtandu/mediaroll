package fr.mediarollRest.mediarollRest.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.mediarollRest.mediarollRest.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	
	Optional<User> findByMail(String mail);
	
	int deleteByMail(String mail);
	

}
