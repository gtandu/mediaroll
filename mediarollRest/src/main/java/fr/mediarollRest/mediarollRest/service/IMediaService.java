package fr.mediarollRest.mediarollRest.service;

import fr.mediarollRest.mediarollRest.exception.MediaNotFoundException;
import fr.mediarollRest.mediarollRest.model.Media;

public interface IMediaService {

	public Media saveMedia(Media media);
	public Media findById(Long id) throws MediaNotFoundException;
	public boolean deleteMediaById(Long id);
	public Media updateMediaInfo(Long mediaId, Media media) throws MediaNotFoundException;
}
