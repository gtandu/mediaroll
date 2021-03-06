package fr.mediarollRest.mediarollRest.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.mediarollRest.mediarollRest.exception.AlbumNotFoundException;
import fr.mediarollRest.mediarollRest.model.Account;
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

	@Transactional
	@Override
	public boolean deleteAlbum(Long albumId) throws AlbumNotFoundException {
		Optional<Album> optionalAlbum = albumRepository.findById(albumId);
		if(optionalAlbum.isPresent()) {
			return albumRepository.deleteById(albumId) != 0 ? true : false ;
		}
		else
		{
			throw new AlbumNotFoundException();
		}
		
	}

	@Override
	public Album findAlbumById(Long albumId) throws AlbumNotFoundException {
		Optional<Album> optionalAlbum = albumRepository.findById(albumId);
		if(optionalAlbum.isPresent()){
			return optionalAlbum.get();
		}
		else
		{
			throw new AlbumNotFoundException();
		}
	}

	@Override
	public List<Album> findAlbumByOwner(Account account) {
		return albumRepository.findByOwner(account);
	}
	
	

}
