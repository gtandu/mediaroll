package fr.mediarollRest.mediarollRest.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import fr.mediarollRest.mediarollRest.model.Media;

public interface IMediaService {

	public void saveMedia(MultipartFile file) throws IOException;
	public void deleteMedia(Media media);
}
