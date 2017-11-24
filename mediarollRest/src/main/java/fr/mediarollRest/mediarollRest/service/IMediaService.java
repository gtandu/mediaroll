package fr.mediarollRest.mediarollRest.service;

import fr.mediarollRest.mediarollRest.model.Media;

public interface IMediaService {

	public Media saveMedia(Media media);
	public boolean deleteMediaById(Long id);
}
