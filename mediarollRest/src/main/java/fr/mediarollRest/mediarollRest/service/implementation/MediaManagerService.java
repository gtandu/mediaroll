package fr.mediarollRest.mediarollRest.service.implementation;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.net.MediaType;

import fr.mediarollRest.mediarollRest.constant.Properties;
import fr.mediarollRest.mediarollRest.exception.MediaNotFoundException;
import fr.mediarollRest.mediarollRest.exception.SpaceAvailableNotEnoughException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.model.Picture;
import fr.mediarollRest.mediarollRest.model.Video;
import fr.mediarollRest.mediarollRest.service.IMediaManagerService;

@Service
public class MediaManagerService implements IMediaManagerService {

	@Autowired
	private AccountService accountService;

	@Autowired
	private MediaService mediaService;

	public static final String VIDEOS_FOLDER = "videos";
	public static final String PICTURES_FOLDER = "pictures";

	@Override
	public Media saveMediaInFileSystem(Account account, MultipartFile media)
			throws FileUploadException, IOException, SpaceAvailableNotEnoughException {
		String randomUUIDFileName = "";
		Media mediaToSave = null;
		String folder = "";
		String mediaType = mediaService.getMediaType(media);
		String uploadDate = generateCurrentDate();

		if (!media.isEmpty()) {
			try {

				if (mediaType.contains(MediaType.ANY_IMAGE_TYPE.type())) {
					folder = PICTURES_FOLDER;
					mediaToSave = new Picture();
				} else {
					folder = VIDEOS_FOLDER;
					mediaToSave = new Video();
				}

				accountService.decreaseStorageSpace(account, media.getSize());

				byte[] bytes = media.getBytes();

				// Creating the directory to store file
				String rootPath = Properties.MEDIAS_FOLDER;
				File dir = new File(rootPath + File.separator + folder);
				if (!dir.exists()) {
					dir.mkdirs();
				}

				// Create the file on server
				UUID uuid = UUID.randomUUID();
				String mediaExtension = FilenameUtils.getExtension(media.getOriginalFilename());
				randomUUIDFileName = uuid.toString() + '.' + mediaExtension;
				File serverFile = new File(dir.getAbsolutePath() + File.separator + randomUUIDFileName);

				while (serverFile.exists()) {
					serverFile = new File(dir.getAbsolutePath() + File.separator + randomUUIDFileName);
				}

				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				System.out.println("Server File Location=" + serverFile.getAbsolutePath());

				System.out.println("You successfully uploaded file=" + randomUUIDFileName);

				mediaToSave.setFilePath(serverFile.getAbsolutePath());
				mediaToSave.setImportDate(uploadDate);
				mediaToSave.setName(media.getOriginalFilename());
				
				return mediaToSave;
			} catch (IOException e) {
				throw new IOException("You failed to upload " + randomUUIDFileName + " => " + e.getMessage());
			}
		} else {
			throw new FileUploadException("The file is empty");
		}

	}

	@Override
	public FileInputStream getInputStreamFromMedia(String filePath) throws MediaNotFoundException{
		try {
			File file = new File(filePath);
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new MediaNotFoundException(e.getMessage());
		}
	}

	@Override
	public boolean deleteMediaInFileSystem(Account account, String filePath) {
		File serverFile = new File(filePath);
		accountService.increaseStorageSpace(account, serverFile.length());
		return serverFile.delete();
	}

	private String generateCurrentDate() {
		Date dNow = new Date();

		SimpleDateFormat ft = new SimpleDateFormat("d M Y 'at' hh:mm:ss");
		return ft.format(dNow);
	}

}
