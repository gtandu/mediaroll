package fr.mediarollRest.mediarollRest.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.mediarollRest.mediarollRest.exception.MediaNotFoundException;
import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.repository.MediaRepository;
import fr.mediarollRest.mediarollRest.service.IMediaService;

@Service
public class MediaService implements IMediaService {

	@Autowired
	private MediaRepository mediaRepository;

	public Media saveMedia(Media media){
		return mediaRepository.save(media);
	}
	
	@Transactional
	public boolean deleteMediaById(Long id) {
		return mediaRepository.deleteById(id) != 0 ? true: false;
	}
	
	public Media findById(Long id) throws MediaNotFoundException {
		Optional<Media> optionalMedia = mediaRepository.findById(id);
		
		if(optionalMedia.isPresent()) {
			return optionalMedia.get();
		}
		else
		{
			throw new MediaNotFoundException();
		}
		
	}

	public List<Media> findAll() {
		return mediaRepository.findAll();
	}
}
