package fr.mediarollRest.mediarollRest.service;

import java.util.List;

import fr.mediarollRest.mediarollRest.exception.AlbumNotFoundException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.model.Album;

public interface IAlbumService {
	
	public Album saveAlbum(Album album);
	
	public Album updateAlbum(Long albumId, Album album) throws AlbumNotFoundException;
	
	public boolean deleteAlbum(Long albumId) throws AlbumNotFoundException;
	
	public Album findAlbumById(Long albumId) throws AlbumNotFoundException;
	
	public List<Album> findAlbumByOwner(Account account);
	
	
	

}
