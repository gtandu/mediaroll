package fr.mediarollRest.mediarollRest.service.implementation;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.apache.tika.Tika;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.net.MediaType;

import fr.mediarollRest.mediarollRest.service.IMediaManagerService;

@Service
public class MediaManagerService implements IMediaManagerService {

	@Override
	public String saveMediaInFileSystem(MultipartFile media, String folder) throws FileUploadException {
		String randomUUIDFileName = "";
		
		if (!media.isEmpty()) {
			try {
				byte[] bytes = media.getBytes();

				// Creating the directory to store file
				String rootPath = System.getProperty("medias.folder");
				File dir = new File(rootPath + File.separator + folder);
				if (!dir.exists()) {
					dir.mkdirs();
				}

				// Create the file on server
				UUID uuid = UUID.randomUUID();
				randomUUIDFileName = uuid.toString();
				File serverFile = new File(dir.getAbsolutePath() + File.separator + randomUUIDFileName);

				while (serverFile.exists()) {
					serverFile = new File(dir.getAbsolutePath() + File.separator + randomUUIDFileName);
				}

				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				System.out.println("Server File Location=" + serverFile.getAbsolutePath());

				System.out.println("You successfully uploaded file=" + randomUUIDFileName);
				
				return serverFile.getAbsolutePath();
			} catch (Exception e) {
				throw new FileUploadException("You failed to upload " + randomUUIDFileName + " => " + e.getMessage());
			}
		} else {
			throw new FileUploadException("You failed to upload " + media.getOriginalFilename() + " because the file was empty.");
		}

	}

	@Override
	public boolean isMedia(MultipartFile media) {
		Tika tika = new Tika();
		String detectedType = "";
		try {
			detectedType = tika.detect(media.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return detectedType.contains(MediaType.ANY_IMAGE_TYPE.type())
				|| detectedType.contains(MediaType.ANY_VIDEO_TYPE.type()) ? true : false;

	}
	
	@Override
	public String getMediaType(MultipartFile media) {
		Tika tika = new Tika();
		String detectedType = "";
		try {
			detectedType = tika.detect(media.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return detectedType;

	}

}
