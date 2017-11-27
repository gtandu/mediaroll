package fr.mediarollRest.mediarollRest.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.mediarollRest.mediarollRest.exception.AlbumNotFoundException;
import fr.mediarollRest.mediarollRest.model.Album;
import fr.mediarollRest.mediarollRest.repository.AlbumRepository;
import fr.mediarollRest.mediarollRest.service.IAlbumService;

@Service
public class AlbumService implements IAlbumService {
	
	@Autowired
	private AlbumRepository albumRepository;

	@Override
	public Album saveAlbum(Album album) {		
		return albumRepository.save(album);
	}

	@Override
	public Album updateAlbum(Long albumId, Album album) throws AlbumNotFoundException {
		Optional<Album> optionalAlbum = albumRepository.findById(albumId);
		if(optionalAlbum.isPresent()){
			Album albumFromDb = optionalAlbum.get();
			albumFromDb.setName(album.getName());
			return albumFromDb;
		}
		else
		{
			throw new AlbumNotFoundException();
		}
	}

}
