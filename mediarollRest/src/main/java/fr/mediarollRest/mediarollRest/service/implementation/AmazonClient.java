package fr.mediarollRest.mediarollRest.service.implementation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.google.common.net.MediaType;

import fr.mediarollRest.mediarollRest.exception.MediaNotFoundException;
import fr.mediarollRest.mediarollRest.exception.SpaceAvailableNotEnoughException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.model.Picture;
import fr.mediarollRest.mediarollRest.model.Video;

@Service
public class AmazonClient {

	private AmazonS3 s3client;
	@Autowired
	private AccountService accountService;
	@Autowired
	private MediaService mediaService;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;
	@Value("${amazonProperties.bucketName}")
	private String bucketName;
	@Value("${amazonProperties.accessKey}")
	private String accessKey;
	@Value("${amazonProperties.secretKey}")
	private String secretKey;
	
	@Value("${amazonProperties.region}")
	private String region;
	
	

	public static final String VIDEOS_FOLDER = "videos";
	public static final String PICTURES_FOLDER = "pictures";

	@PostConstruct
	private void initializeAmazon() {

		BasicAWSCredentials creds = new BasicAWSCredentials(this.accessKey, this.secretKey);
		s3client = AmazonS3ClientBuilder.standard().withRegion(region).withCredentials(new AWSStaticCredentialsProvider(creds)).build();
	}

	public Media uploadFile(Account account, MultipartFile media)
			throws FileUploadException, IOException, SpaceAvailableNotEnoughException {
		String mediaType = mediaService.getMediaType(media);
		Media mediaToSave = null;
		String folder = "";
		String fileUrl = "";
		String uploadDate = generateCurrentDate();
		String fileName = "";
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

				File file = convertMultiPartToFile(media);
				fileName = generateFileName(media);
				fileUrl = uploadFileTos3bucket(fileName, file);
				
				

				mediaToSave.setFilePath(fileUrl);
				mediaToSave.setKeyS3(fileName);
				mediaToSave.setImportDate(uploadDate);
				mediaToSave.setName(media.getOriginalFilename());
				file.delete();
				
				System.out.println("Server File Location = " + fileUrl);
				System.out.println("You successfully uploaded file = " + media.getOriginalFilename());
				return mediaToSave;
			} catch (IOException e) {
				throw new IOException("You failed to upload " + fileName + " => " + e.getMessage());
			}
		} else {
			throw new FileUploadException("The file is empty");
		}
	}

	public boolean deleteFileFromS3Bucket(Account account, String fileName) {
		//TODO REFACTOR
		long contentLength = s3client.getObjectMetadata(bucketName, fileName).getContentLength();
		accountService.increaseStorageSpace(account, contentLength);
		s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
		return true;
	}

	public InputStream getInputStreamFromMedia(String fileName) throws MediaNotFoundException {
		S3Object object = s3client.getObject(new GetObjectRequest(bucketName, fileName));
		return object.getObjectContent().getDelegateStream();
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	private String generateFileName(MultipartFile media) {
		UUID uuid = UUID.randomUUID();
		String mediaExtension = FilenameUtils.getExtension(media.getOriginalFilename());
		return uuid.toString() + '.' + mediaExtension;
	}

	private String uploadFileTos3bucket(String fileName, File file) {
		s3client.putObject(
				new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
		s3client.getObject(new GetObjectRequest(bucketName, fileName));
		return endpointUrl + "/" + bucketName + "/" + fileName;

	}

	private String generateCurrentDate() {
		Date dNow = new Date();

		SimpleDateFormat ft = new SimpleDateFormat("d M Y 'at' hh:mm:ss");
		return ft.format(dNow);
	}

}