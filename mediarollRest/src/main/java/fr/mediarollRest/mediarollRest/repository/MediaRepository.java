package fr.mediarollRest.mediarollRest.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.mediarollRest.mediarollRest.model.Media;

@Repository
public interface MediaRepository extends CrudRepository<Media, Long> {

	Media findByName(String name);
	void deleteByName(String name);
	List<Media> findAll();
}
