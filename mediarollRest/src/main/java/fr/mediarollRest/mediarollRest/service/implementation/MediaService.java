package fr.mediarollRest.mediarollRest.service.implementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.net.MediaType;

import fr.mediarollRest.mediarollRest.constant.Constantes;
import fr.mediarollRest.mediarollRest.exception.MediaNotFoundException;
import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.repository.MediaRepository;
import fr.mediarollRest.mediarollRest.service.IMediaService;

@Service
public class MediaService implements IMediaService {

	@Autowired
	private MediaRepository mediaRepository;

	public Media saveMedia(Media media) {
		return mediaRepository.save(media);
	}

	@Transactional
	@Override
	public boolean deleteMediaById(Long id) {
		return mediaRepository.deleteById(id) != 0 ? true : false;
	}

	@Override
	public Media findById(Long id) throws MediaNotFoundException {
		Optional<Media> optionalMedia = mediaRepository.findById(id);

		if (optionalMedia.isPresent()) {
			return optionalMedia.get();
		} else {
			throw new MediaNotFoundException();
		}

	}

	@Override
	public Media updateMediaInfo(Long mediaId, Media media) throws MediaNotFoundException {

		Optional<Media> optionalMedia = mediaRepository.findById(mediaId);

		if (optionalMedia.isPresent()) {
			Media mediaFromDb = optionalMedia.get();
			mediaFromDb.setDescription(media.getDescription());
			return mediaRepository.save(mediaFromDb);
		} else {
			throw new MediaNotFoundException();
		}

	}

	@Override
	public String getMediaType(File file) throws IOException {
		Tika tika = new Tika();
		String detectedType = "";
		try {
			detectedType = tika.detect(file);
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		}
		return detectedType;

	}

	@Override
	public String encodeBase64(Media media) throws MediaNotFoundException, IOException{
		FileInputStream in;
		try {
			File file = new File(media.getFilePath());
			in = new FileInputStream(file);
			//in = mediaManagerService.getInputStreamFromMedia(media.getFilePath());
			byte[] mediaByte = IOUtils.toByteArray(in);
			String mediaBase64Encoded = Base64Utils.encodeToString(mediaByte);

			String mediaSrcUrl  = String.format(Constantes.URL_MEDIAS_SRC_BASE64, this.getMediaType(file), mediaBase64Encoded);
			
			media.setEncodedMedia(mediaSrcUrl);
			
			return media.getEncodedMedia();
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		}
		
	}

	@Override
	public String getMediaType(MultipartFile media) throws IOException {
		Tika tika = new Tika();
		String detectedType = "";
		try {
			detectedType = tika.detect(media.getBytes());
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		}
		return detectedType;

	}
	
	@Override
	public boolean isMedia(MultipartFile media) throws IOException {
		String detectedType;
		try {
			detectedType = getMediaType(media);
			return detectedType.contains(MediaType.ANY_IMAGE_TYPE.type())
					|| detectedType.contains(MediaType.ANY_VIDEO_TYPE.type()) ? true : false;
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		}

		

	}
}
