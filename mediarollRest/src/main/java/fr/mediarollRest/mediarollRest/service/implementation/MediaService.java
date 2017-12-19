package fr.mediarollRest.mediarollRest.service.implementation;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.net.MediaType;

import fr.mediarollRest.mediarollRest.exception.MediaNotFoundException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.model.Picture;
import fr.mediarollRest.mediarollRest.model.Video;
import fr.mediarollRest.mediarollRest.repository.AccountRepository;
import fr.mediarollRest.mediarollRest.repository.MediaRepository;
import fr.mediarollRest.mediarollRest.service.IMediaService;

@Service
public class MediaService implements IMediaService {

	@Autowired
	private MediaRepository mediaRepository;

	@Autowired
	private AccountRepository accountRepository;

	public Media saveMedia(Media media) {
		return mediaRepository.save(media);
	}

	@Transactional
	@Override
	public boolean deleteMediaById(String id) {
		return mediaRepository.deleteById(id) != 0 ? true : false;
	}

	@Override
	public Media findById(String id) throws MediaNotFoundException {
		Optional<Media> optionalMedia = mediaRepository.findById(id);

		if (optionalMedia.isPresent()) {
			return optionalMedia.get();
		} else {
			throw new MediaNotFoundException();
		}

	}

	@Override
	public Media updateMediaInfo(String mediaId, Media media) throws MediaNotFoundException {

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

	@Override
	public List<Picture> getAllPictures(String owner) {
		Optional<Account> optionalAccount = accountRepository.findByMail(owner);

		if (optionalAccount.isPresent()) {
			Account account = optionalAccount.get();
			return mediaRepository.findPictures(account);
		}
		// TODO REFACTOR
		return null;
	}

	@Override
	public List<Video> getAllVideos(String owner) {
		Optional<Account> optionalAccount = accountRepository.findByMail(owner);

		if (optionalAccount.isPresent()) {
			Account account = optionalAccount.get();
			return mediaRepository.findVideos(account);
		}
		// TODO REFACTOR
		return null;
	}
}
