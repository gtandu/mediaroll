package fr.mediarollRest.mediarollRest.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.mediarollRest.mediarollRest.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}
