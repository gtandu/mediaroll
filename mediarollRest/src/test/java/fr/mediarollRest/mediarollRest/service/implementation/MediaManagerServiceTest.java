package fr.mediarollRest.mediarollRest.service.implementation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import com.google.common.net.MediaType;

import fr.mediarollRest.mediarollRest.model.Media;

@RunWith(MockitoJUnitRunner.class)
public class MediaManagerServiceTest {
	
	
	@InjectMocks
	private MediaManagerService mediaManagerService;
	
	private InputStream fileToUpload;
	
	private String fileName;
	
	private String mediasFolder;
	
	private MockMultipartFile media;
	
	@Before
	public void init() throws IOException{
		
		mediasFolder = "uploadTest";
		System.setProperty("medias.folder", mediasFolder);
		this.fileName = "image.jpg";
		InputStream fileToUpload = new ClassPathResource(fileName).getInputStream();
		media = new MockMultipartFile(fileName,fileName, MediaType.JPEG.type(), fileToUpload);
	}
	
	@Test
	public void testSaveMediaInFileSystemPictures() throws Exception {
		Media mediaSaveInFileSystem = mediaManagerService.saveMediaInFileSystem(media);
		
		assertThat(mediaSaveInFileSystem).isNotNull();
		assertThat(mediaSaveInFileSystem.getName()).isEqualTo(fileName);
		assertThat(mediaSaveInFileSystem.getFilePath()).isNotEmpty();
	}
	
	//TODO Test with Video

	@Test
	public void testIsMedia() throws Exception {
		
		boolean isMedia = mediaManagerService.isMedia(media);
		
		assertThat(isMedia).isTrue();
	}
	
	@Test
	public void testIsNotMedia() throws Exception {
		
		this.fileName = "test.pdf";
		this.fileToUpload =   new ClassPathResource(fileName).getInputStream();
		MockMultipartFile pdfFile = new MockMultipartFile(fileName,fileName, MediaType.PDF.type(), fileToUpload);
		
		boolean isMedia = mediaManagerService.isMedia(pdfFile);
		
		assertThat(isMedia).isFalse();
	}

	@Test
	public void testGetMediaType() throws Exception {
		String mediaType = mediaManagerService.getMediaType(media);
		
		assertThat(mediaType).contains((MediaType.JPEG.type()));
	}
	
	
}
