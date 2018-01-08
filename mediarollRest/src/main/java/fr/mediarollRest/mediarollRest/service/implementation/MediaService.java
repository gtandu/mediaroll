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

import fr.mediarollRest.mediarollRest.exception.AccountExistInSharedListOfMediaException;
import fr.mediarollRest.mediarollRest.exception.AccountNotExistInSharedListOfMediaException;
import fr.mediarollRest.mediarollRest.exception.AccountNotFoundException;
import fr.mediarollRest.mediarollRest.exception.MediaNotFoundException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.model.Picture;
import fr.mediarollRest.mediarollRest.model.Video;
import fr.mediarollRest.mediarollRest.repository.MediaRepository;
import fr.mediarollRest.mediarollRest.service.IMediaService;

@Service
public class MediaService implements IMediaService {

	@Autowired
	private MediaRepository mediaRepository;

	@Autowired
	private AccountService accountService;

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
	public List<Picture> getAllPictures(String owner) throws AccountNotFoundException {
		Account account = accountService.findByMail(owner);
		return mediaRepository.findPictures(account);
	}

	@Override
	public List<Video> getAllVideos(String owner) throws AccountNotFoundException {
		Account account = accountService.findByMail(owner);
		return mediaRepository.findVideos(account);
	}

	@Override
	public Media addUserToSharedList(String userMail, String mediaId)
			throws AccountExistInSharedListOfMediaException, AccountNotFoundException, MediaNotFoundException {

		Account account = accountService.findByMail(userMail);
		Media media = this.findById(mediaId);

		if (media.getSharedPeople().contains(account)) {
			throw new AccountExistInSharedListOfMediaException();
		} else {
			account.getSharedMedias().add(media);
			media.getSharedPeople().add(account);
			return this.saveMedia(media);
		}

	}

	@Override
	public Media removeUserFromSharedList(String userMail, String mediaId)
			throws AccountNotFoundException, MediaNotFoundException, AccountNotExistInSharedListOfMediaException {

		Account account = accountService.findByMail(userMail);
		Media media = this.findById(mediaId);

		if (media.getSharedPeople().contains(account)) {
			account.getSharedMedias().remove(media);
			media.getSharedPeople().remove(account);
			return this.saveMedia(media);
		} else {
			throw new AccountNotExistInSharedListOfMediaException();
		}

	}
}
