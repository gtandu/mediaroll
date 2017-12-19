package fr.mediarollRest.mediarollRest.service.implementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import fr.mediarollRest.mediarollRest.model.Video;
import fr.mediarollRest.mediarollRest.repository.AccountRepository;
import fr.mediarollRest.mediarollRest.repository.MediaRepository;

@RunWith(MockitoJUnitRunner.class)
public class MediaServiceTest {

	@Mock
	private MediaRepository mediaRepository;
	
	@Mock
	private AccountRepository accountRepository;
	
	@Mock
	private MediaManagerService mediaManagerService;

	@InjectMocks
	private MediaService mediaService;
	
	private InputStream fileToUpload;

	private String fileName;

	private MockMultipartFile media;

	@Before
	public void init() throws IOException {
		this.fileName = "image.jpg";
		this.fileToUpload = new ClassPathResource(fileName).getInputStream();

	}

	@Test
	public void testDeleteMediaByIdSuccess() throws Exception {
		// WHEN
		String id = UUID.randomUUID().toString();
		when(mediaRepository.deleteById(eq(id))).thenReturn(1);

		// GIVEN
		mediaService.deleteMediaById(id);

		// THEN
		verify(mediaRepository).deleteById(eq(id));

	}

	@Test
	public void testDeleteMediaByIdFailed() throws Exception {
		// WHEN
		String id = UUID.randomUUID().toString();;
		when(mediaRepository.deleteById(eq(id))).thenReturn(0);

		// GIVEN
		mediaService.deleteMediaById(id);

		// THEN
		verify(mediaRepository).deleteById(eq(id));

	}

	@Test
	public void testFindByIdFound() throws Exception {
		String id = UUID.randomUUID().toString();;
		when(mediaRepository.findById(anyString())).thenReturn(Optional.of(new Picture()));

		Media media = mediaService.findById(id);

		assertThat(media).isNotNull();

		verify(mediaRepository).findById(eq(id));
	}

	@Test(expected = MediaNotFoundException.class)
	public void testFindByIdNotFound() throws Exception {
		String id = UUID.randomUUID().toString();;
		when(mediaRepository.findById(anyString())).thenReturn(Optional.empty());

		mediaService.findById(id);

		verify(mediaRepository).findById(eq(id));
	}

	@Test
	public void testUpdateMediaInfo() throws Exception {
		String mediaId = UUID.randomUUID().toString();;
		Media mediaWithNewInfo = new Picture();
		Picture mediaToUpdate =new Picture();
		mediaWithNewInfo.setDescription("test description");

		when(mediaRepository.findById(anyString())).thenReturn(Optional.of(mediaToUpdate));
		when(mediaRepository.save(any(Media.class))).thenReturn(mediaToUpdate);
		
		Media mediaUpdated = mediaService.updateMediaInfo(mediaId, mediaWithNewInfo);
		
		assertThat(mediaUpdated).isNotNull();
		assertThat(mediaUpdated.getDescription()).isEqualTo(mediaWithNewInfo.getDescription());
		
		verify(mediaRepository).findById(eq(mediaId));
		verify(mediaRepository).save(any(Media.class));
	}
	
	@Test(expected=MediaNotFoundException.class)
	public void testUpdateMediaInfoThrowMediaNotFoundException() throws Exception {
		String mediaId = UUID.randomUUID().toString();;
		Media media = new Picture();
		media.setDescription("test description");

		when(mediaRepository.findById(eq(mediaId))).thenReturn(Optional.empty());
		
		mediaService.updateMediaInfo(mediaId, media);
		
		verify(mediaRepository).findById(eq(mediaId));
	}

	@Test
	public void testSaveMedia() throws Exception {
		Picture picture = new Picture();
		
		when(mediaRepository.save(any(Picture.class))).thenReturn(new Picture());
		
		mediaService.saveMedia(picture);
		
		verify(mediaRepository).save(any(Picture.class));
	}
	
	@Test
	public void testIsMedia() throws Exception {
		media = new MockMultipartFile(fileName, fileName, MediaType.JPEG.type(), fileToUpload);
		boolean isMedia = mediaService.isMedia(media);

		assertThat(isMedia).isTrue();
	}

	@Test
	public void testIsNotMedia() throws Exception {

		this.fileName = "test.pdf";
		this.fileToUpload = new ClassPathResource(fileName).getInputStream();
		MockMultipartFile pdfFile = new MockMultipartFile(fileName, fileName, MediaType.PDF.type(), fileToUpload);

		boolean isMedia = mediaService.isMedia(pdfFile);

		assertThat(isMedia).isFalse();
	}
	
	@Test
	public void testGetMediaTypeMultipartFile() throws Exception {
		media = new MockMultipartFile(fileName, fileName, MediaType.JPEG.type(), fileToUpload);
		String mediaType = mediaService.getMediaType(media);

		assertThat(mediaType).contains((MediaType.JPEG.type()));
	}

	@Test
	public void testGetMediaTypeFile() throws Exception {
		File fileToUpload = new ClassPathResource(fileName).getFile();
		String mediaType = mediaService.getMediaType(fileToUpload);

		assertThat(mediaType).contains((MediaType.JPEG.type()));
	}

	@Test
	public void testGetAllVideos() throws Exception {
		
		String owner = "babar@hotmail.fr";
		when(accountRepository.findByMail(eq(owner))).thenReturn(Optional.of(new Account()));
		when(mediaRepository.findVideos(any(Account.class))).thenReturn(Arrays.asList(new Video()));
		
		List<Video> videosList = mediaService.getAllVideos(owner);
		
		assertThat(videosList).isNotNull();
		assertThat(videosList).isNotEmpty();
		
		verify(accountRepository).findByMail(eq(owner));
		verify(mediaRepository).findVideos(any(Account.class));
	}

	@Test
	public void testGetAllPictures() throws Exception {
		String owner = "babar@hotmail.fr";
		when(accountRepository.findByMail(eq(owner))).thenReturn(Optional.of(new Account()));
		when(mediaRepository.findPictures(any(Account.class))).thenReturn(Arrays.asList(new Picture()));
		
		List<Picture> picturesList = mediaService.getAllPictures(owner);
		
		assertThat(picturesList).isNotNull();
		assertThat(picturesList).isNotEmpty();
		
		verify(accountRepository).findByMail(eq(owner));
		verify(mediaRepository).findPictures(any(Account.class));
	}

}
