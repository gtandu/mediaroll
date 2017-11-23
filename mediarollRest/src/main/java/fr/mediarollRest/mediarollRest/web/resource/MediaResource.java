package fr.mediarollRest.mediarollRest.web.resource;

import java.io.IOException;
import java.util.List;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.net.MediaType;

import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.service.implementation.MediaManagerService;
import fr.mediarollRest.mediarollRest.service.implementation.MediaService;

@RestController
@RequestMapping("")
public class MediaResource {

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
	public ResponseEntity<Void> handleFileUpload(@RequestParam("media") MultipartFile media,
			RedirectAttributes redirectAttributes) throws IOException {

		String folder = "";
		String mediaPath = "";

		if (mediaManagerService.isMedia(media)) {

			String mediaType = mediaManagerService.getMediaType(media);

			if (mediaType.contains(MediaType.ANY_IMAGE_TYPE.type())) {
				folder = "pictures";
				try {
					mediaPath = mediaManagerService.saveMediaInFileSystem(media, folder);
				} catch (FileUploadException e) {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
			}

			else {
				folder = "videos";
				try {
					mediaPath = mediaManagerService.saveMediaInFileSystem(media, folder);
				} catch (FileUploadException e) {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
			}
			
			return new ResponseEntity<>(HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}
}
