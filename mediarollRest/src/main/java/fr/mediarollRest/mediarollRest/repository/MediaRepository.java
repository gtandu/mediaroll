package fr.mediarollRest.mediarollRest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.mediarollRest.mediarollRest.model.Media;

@Repository
public interface MediaRepository extends CrudRepository<Media, Long> {

	Optional<Media> findById(Long id);
	Integer deleteById(Long id);
	List<Media> findAll();
}
