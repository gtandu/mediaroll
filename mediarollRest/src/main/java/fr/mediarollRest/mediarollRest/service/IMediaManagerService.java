package fr.mediarollRest.mediarollRest.service;

import java.io.IOException;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import fr.mediarollRest.mediarollRest.model.Media;

public interface IMediaManagerService {
	
	public Media saveMediaInFileSystem(MultipartFile media) throws FileUploadException, IOException;
	
	public boolean isMedia(MultipartFile media);

	String getMediaType(MultipartFile media);
}
