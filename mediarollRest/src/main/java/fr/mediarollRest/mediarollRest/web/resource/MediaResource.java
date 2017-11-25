package fr.mediarollRest.mediarollRest.web.resource;

import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIAS;
import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIA_ID;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.mediarollRest.mediarollRest.exception.MediaNotFoundException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.service.implementation.AccountService;
import fr.mediarollRest.mediarollRest.service.implementation.MediaManagerService;
import fr.mediarollRest.mediarollRest.service.implementation.MediaService;
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
	private MediaService mediaService;

	@Autowired
	private MediaManagerService mediaManagerService;
	
	@ApiOperation(value = "Update media info")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 400, message = "The file is not a media"),
    })
	@RequestMapping(value = MEDIAS+MEDIA_ID, method = RequestMethod.PUT)
	public ResponseEntity<Media> updateMediaInfo(@PathVariable("mediaId") Long mediaId, @RequestBody Media media){
		
		try {
			Media mediaUpdated = mediaService.updateMediaInfo(mediaId, media);
			return new ResponseEntity<Media>(mediaUpdated, HttpStatus.OK);
		} catch (MediaNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		
	}

	
	@ApiOperation(value = "Upload a media")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully upload"),
            @ApiResponse(code = 400, message = "The file is not a media"),
            @ApiResponse(code = 500, message = "Attempt to save file in file system failed"),
    })
	@RequestMapping(value = MEDIAS, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Media> uploadMedia(@RequestParam("media") MultipartFile media, Principal principal)
			throws IOException {

		Media mediaToSave = null;

		if (mediaManagerService.isMedia(media)) {
			Optional<Account> userOptional = accountService.findByMail(principal.getName());
			Account account = userOptional.get();

			try {
				mediaToSave = mediaManagerService.saveMediaInFileSystem(media);

				mediaToSave.setOwner(account);
				account.getMediaList().add(mediaToSave);
				Media mediaSaved = mediaService.saveMedia(mediaToSave);

				return new ResponseEntity<>(mediaSaved,HttpStatus.CREATED);
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
	
	@ApiOperation(value = "Delete a media")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Media successfully deleted"),
            @ApiResponse(code = 404, message = "Media not found in db. Please to check media ID"),
            @ApiResponse(code = 500, message = "An error occured during the process to delete Media"),
    })
	@RequestMapping(value=MEDIAS+MEDIA_ID,method=RequestMethod.DELETE)
	public ResponseEntity<Void> deleteMedia(@PathVariable("mediaId") Long mediaId){
		try {
			Media mediaInDb = mediaService.findById(mediaId);
			boolean isDeleteFromFileSystem = mediaManagerService.deleteMediaInFileSystem(mediaInDb.getFilePath());
			
			if(isDeleteFromFileSystem) {
				boolean isDeleteFromDb = mediaService.deleteMediaById(mediaId);
				
				if(isDeleteFromDb) {
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}
				else
				{
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			else
			{
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}
		catch(MediaNotFoundException e) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		
		

	}
}
