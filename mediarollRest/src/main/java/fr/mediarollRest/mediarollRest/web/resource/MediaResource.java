package fr.mediarollRest.mediarollRest.web.resource;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.service.implementation.AccountService;
import fr.mediarollRest.mediarollRest.service.implementation.MediaManagerService;
import fr.mediarollRest.mediarollRest.service.implementation.MediaService;

@RestController
@RequestMapping("")
public class MediaResource {

	@Autowired
	private AccountService accountService;

	@Autowired
	private MediaService mediaService;

	@Autowired
	private MediaManagerService mediaManagerService;

	@RequestMapping(value = "/medias", produces = { "application/json" }, method = RequestMethod.GET)
	@ResponseBody
	public List<Media> findAllMedia() {
		return mediaService.findAll();
	}

	@RequestMapping(value = "/medias/name={name}", produces = { "text/html" }, method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteMediaByName(@PathVariable("name") String name) {

		return "Vous avez supprimé le media portant le nom de : " + name;
	}

	@PostMapping("/medias")
	@ResponseBody
	public ResponseEntity<Void> handleFileUpload(@RequestParam("media") MultipartFile media, Principal principal)
			throws IOException {

		Media mediaToSave = null;

		if (mediaManagerService.isMedia(media)) {
			Optional<Account> userOptional = accountService.findByMail(principal.getName());
			if (userOptional.isPresent()) {
				Account account = userOptional.get();

				try {
					mediaToSave = mediaManagerService.saveMediaInFileSystem(media);

					mediaToSave.setOwner(account);
					account.getMediaList().add(mediaToSave);
					accountService.saveAccount(account);

					return new ResponseEntity<>(HttpStatus.CREATED);
				} catch (FileUploadException e) {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
			}

			else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}
}
