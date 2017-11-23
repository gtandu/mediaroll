package fr.mediarollRest.mediarollRest.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.repository.MediaRepository;
import fr.mediarollRest.mediarollRest.service.IMediaService;

@Service
public class MediaService implements IMediaService {

	@Autowired
	MediaRepository mediaRepository;

	public Media saveMedia(Media media){
		return mediaRepository.save(media);
	}

	public void deleteMedia(Media media) {
		mediaRepository.delete(media);
	}

	public Media findByName(String name) {
		return mediaRepository.findByName(name);
	}

	public List<Media> findAll() {
		return mediaRepository.findAll();
	}
}
