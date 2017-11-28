package fr.mediarollRest.mediarollRest.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.mediarollRest.mediarollRest.model.Album;

@Repository
public interface AlbumRepository extends CrudRepository<Album, Long> {
	
	Optional<Album> findById(Long id);
	
	Integer deleteById(Long id);

}
