package fr.mediarollRest.mediarollRest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.model.Picture;
import fr.mediarollRest.mediarollRest.model.Video;

@Repository
public interface MediaRepository extends CrudRepository<Media, String> {

	Optional<Media> findById(String id);
	Integer deleteById(String id);
	List<Media> findAll();
	@Query("SELECT e From Media e WHERE TYPE(e) IN (Picture) AND e.owner = :owner")
	List<Picture> findPictures(@Param("owner") Account owner);
	@Query("SELECT e From Media e WHERE TYPE(e) IN (Video) AND e.owner = :owner")
	List<Video> findVideos(@Param("owner") Account account);
}
