package fr.mediarollRest.mediarollRest.service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import fr.mediarollRest.mediarollRest.exception.MediaNotFoundException;
import fr.mediarollRest.mediarollRest.exception.SpaceAvailableNotEnoughException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.model.Media;

public interface IMediaManagerService {
	
	public Media saveMediaInFileSystem(Account account, MultipartFile media) throws FileUploadException, IOException, SpaceAvailableNotEnoughException;
	
	public boolean deleteMediaInFileSystem(Account account, String filePath);
	
	public InputStream getInputStreamFromMedia(String filePath) throws MediaNotFoundException;
}
