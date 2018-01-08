package fr.mediarollRest.mediarollRest.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import fr.mediarollRest.mediarollRest.exception.AccountExistInSharedListOfMediaException;
import fr.mediarollRest.mediarollRest.exception.AccountNotExistInSharedListOfMediaException;
import fr.mediarollRest.mediarollRest.exception.AccountNotFoundException;
import fr.mediarollRest.mediarollRest.exception.MediaNotFoundException;
import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.model.Picture;
import fr.mediarollRest.mediarollRest.model.Video;

public interface IMediaService {

	public Media saveMedia(Media media);

	public Media findById(String id) throws MediaNotFoundException;

	public boolean deleteMediaById(String id);

	public Media updateMediaInfo(String mediaId, Media media) throws MediaNotFoundException;

	public String getMediaType(File file) throws IOException;

	public String getMediaType(MultipartFile media) throws IOException;

	public boolean isMedia(MultipartFile media) throws IOException;

	public List<Picture> getAllPictures(String owner) throws AccountNotFoundException;

	public List<Video> getAllVideos(String owner) throws AccountNotFoundException;

	public Media addUserToSharedList(String userMail, String mediaId)
			throws AccountExistInSharedListOfMediaException, AccountNotFoundException, MediaNotFoundException;

	public Media removeUserFromSharedList(String userMail, String mediaId)
			throws AccountNotFoundException, MediaNotFoundException, AccountNotExistInSharedListOfMediaException;
}
