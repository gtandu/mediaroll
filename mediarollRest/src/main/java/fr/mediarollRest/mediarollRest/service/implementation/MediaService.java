package fr.mediarollRest.mediarollRest.service.implementation;

import java.io.IOException;
import java.util.List;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.net.MediaType;

import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.repository.MediaRepository;
import fr.mediarollRest.mediarollRest.service.IMediaService;

@Service
public class MediaService implements IMediaService {

	@Autowired
	MediaRepository mediaRepository;

	public void saveMedia(MultipartFile media) throws IOException {
		Tika tika = new Tika();
		String detectedType = tika.detect(media.getBytes());
		if(detectedType.contains(MediaType.ANY_IMAGE_TYPE.type()) || detectedType.contains(MediaType.ANY_VIDEO_TYPE.type())){
			System.out.println("FORMAT ACCEPTE");			
		}
		else
		{
			System.out.println("FORMAT REFUSE");
		}

		// String extension =
		// FilenameUtils.getExtension(file.getOriginalFilename());
		// mediaRepository.save(media);
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
