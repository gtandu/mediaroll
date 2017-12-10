package fr.mediarollRest.mediarollRest.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import fr.mediarollRest.mediarollRest.exception.MediaNotFoundException;
import fr.mediarollRest.mediarollRest.model.Media;

public interface IMediaService {

	public Media saveMedia(Media media);
	public Media findById(Long id) throws MediaNotFoundException;
	public boolean deleteMediaById(Long id);
	public Media updateMediaInfo(Long mediaId, Media media) throws MediaNotFoundException;
	public String encodeBase64(Media media) throws MediaNotFoundException, IOException;
	public String getMediaType(InputStream in) throws IOException;
	public String getMediaType(MultipartFile media) throws IOException;
	public boolean isMedia(MultipartFile media) throws IOException;
}
