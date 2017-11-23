package fr.mediarollRest.mediarollRest.service;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface IMediaManagerService {
	
	public String saveMediaInFileSystem(MultipartFile media, String folder) throws FileUploadException;
	
	public boolean isMedia(MultipartFile media);

	String getMediaType(MultipartFile media);
}
