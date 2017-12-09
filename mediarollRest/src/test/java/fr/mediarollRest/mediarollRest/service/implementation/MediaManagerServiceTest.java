package fr.mediarollRest.mediarollRest.service.implementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import com.google.common.net.MediaType;

import fr.mediarollRest.mediarollRest.exception.MediaNotFoundException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.model.Picture;

@RunWith(MockitoJUnitRunner.class)
public class MediaManagerServiceTest {

	@InjectMocks
	private MediaManagerService mediaManagerService;

	@Mock
	private AccountService accountService;

	private InputStream fileToUpload;

	private String fileName;

	private String mediasFolder;

	private MockMultipartFile media;

	private Account account;

	@Before
	public void init() throws IOException {
		this.account = new Account();
		account.setStorageSpace(100);
		mediasFolder = "uploadTest";
		System.setProperty("medias.folder", mediasFolder);
		this.fileName = "image.jpg";
		this.fileToUpload = new ClassPathResource(fileName).getInputStream();

	}

	@Test
	public void testSaveMediaInFileSystemPicture() throws Exception {
		media = new MockMultipartFile(fileName, fileName, MediaType.JPEG.type(), fileToUpload);
		when(accountService.decreaseStorageSpace(any(Account.class), anyLong())).thenReturn(100.0);
		Media mediaSaveInFileSystem = mediaManagerService.saveMediaInFileSystem(account, media);

		assertThat(mediaSaveInFileSystem).isNotNull();
		assertThat(mediaSaveInFileSystem.getName()).isEqualTo(fileName);
		assertThat(mediaSaveInFileSystem.getFilePath()).isNotEmpty();
		assertThat(mediaSaveInFileSystem.getFilePath()).contains(MediaManagerService.PICTURES_FOLDER);

		verify(accountService).decreaseStorageSpace(any(Account.class), anyLong());
	}

	@Test
	public void testSaveMediaInFileSystemVideo() throws Exception {
		this.fileName = "video.mp4";
		this.fileToUpload = new ClassPathResource(fileName).getInputStream();
		media = new MockMultipartFile(fileName, fileName, MediaType.MP4_VIDEO.type(), fileToUpload);
		Media mediaSaveInFileSystem = mediaManagerService.saveMediaInFileSystem(account, media);

		assertThat(mediaSaveInFileSystem).isNotNull();
		assertThat(mediaSaveInFileSystem.getName()).isEqualTo(fileName);
		assertThat(mediaSaveInFileSystem.getFilePath()).isNotEmpty();
		assertThat(mediaSaveInFileSystem.getFilePath()).contains(MediaManagerService.VIDEOS_FOLDER);
	}

	@Test
	public void testIsMedia() throws Exception {
		media = new MockMultipartFile(fileName, fileName, MediaType.JPEG.type(), fileToUpload);
		boolean isMedia = mediaManagerService.isMedia(media);

		assertThat(isMedia).isTrue();
	}

	@Test
	public void testIsNotMedia() throws Exception {

		this.fileName = "test.pdf";
		this.fileToUpload = new ClassPathResource(fileName).getInputStream();
		MockMultipartFile pdfFile = new MockMultipartFile(fileName, fileName, MediaType.PDF.type(), fileToUpload);

		boolean isMedia = mediaManagerService.isMedia(pdfFile);

		assertThat(isMedia).isFalse();
	}

	@Test
	public void testGetMediaType() throws Exception {
		media = new MockMultipartFile(fileName, fileName, MediaType.JPEG.type(), fileToUpload);
		String mediaType = mediaManagerService.getMediaType(media);

		assertThat(mediaType).contains((MediaType.JPEG.type()));
	}

	@Test
	public void testDeleteMediaInFileSystemSuccess() throws Exception {
		String filePath = "src/test/resources/fileToDelete.txt";
		Path newFilePath = Paths.get(filePath);
		Files.createFile(newFilePath);

		when(accountService.increaseStorageSpace(any(Account.class), anyLong())).thenReturn(100.0);

		boolean isDeleteInFileSystem = mediaManagerService.deleteMediaInFileSystem(account, filePath);

		assertThat(isDeleteInFileSystem).isTrue();

		verify(accountService).increaseStorageSpace(any(Account.class), anyLong());

	}

	@Test
	public void testDeleteMediaInFileSystemFailed() throws Exception {
		String filePath = "src/test/resources/fileToDelete.txt";
		when(accountService.increaseStorageSpace(any(Account.class), anyLong())).thenReturn(100.0);

		boolean isDeleteInFileSystem = mediaManagerService.deleteMediaInFileSystem(account, filePath);

		assertThat(isDeleteInFileSystem).isFalse();

		verify(accountService).increaseStorageSpace(any(Account.class), anyLong());
	}

	@Test
	public void testGetInputStreamFromMedia() throws Exception {
		Picture picture = new Picture();
		picture.setFilePath("src/test/resources/image.jpg");

		InputStream inputStreamFromMedia = mediaManagerService.getInputStreamFromMedia(picture);

		assertThat(inputStreamFromMedia).isNotNull();

	}

	@Test(expected = MediaNotFoundException.class)
	public void testGetInputStreamFromMediaThrowsFileNotFoundException() throws Exception {
		Picture picture = new Picture();
		picture.setFilePath("notFound");

		mediaManagerService.getInputStreamFromMedia(picture);
	}

}
