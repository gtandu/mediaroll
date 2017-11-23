package fr.mediarollRest.mediarollRest.web.resource;

import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIAS;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value="medias", description="Operations pertaining to medias in MediaRoll")
public class MediaResource {

	@Autowired
	private AccountService accountService;

	@Autowired
	private MediaManagerService mediaManagerService;

	
	@ApiOperation(value = "Upload a media")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully upload"),
            @ApiResponse(code = 400, message = "The file is not a media"),
            @ApiResponse(code = 500, message = "Attempt to save file in file system failed"),
    })
	@RequestMapping(value = MEDIAS, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> uploadMedia(@RequestParam("media") MultipartFile media, Principal principal)
			throws IOException {

		Media mediaToSave = null;

		if (mediaManagerService.isMedia(media)) {
			Optional<Account> userOptional = accountService.findByMail(principal.getName());
			Account account = userOptional.get();

			try {
				mediaToSave = mediaManagerService.saveMediaInFileSystem(media);

				mediaToSave.setOwner(account);
				account.getMediaList().add(mediaToSave);
				accountService.saveAccount(account);

				return new ResponseEntity<>(HttpStatus.CREATED);
			}
			catch (IOException e) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			} 
			catch (FileUploadException e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}

		else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}
}
