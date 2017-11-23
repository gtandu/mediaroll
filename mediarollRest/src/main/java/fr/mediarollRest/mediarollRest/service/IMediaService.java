package fr.mediarollRest.mediarollRest.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import fr.mediarollRest.mediarollRest.model.Media;

public interface IMediaService {

	public Media saveMedia(Media media);
	public void deleteMedia(Media media);
}
