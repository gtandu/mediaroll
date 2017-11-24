package fr.mediarollRest.mediarollRest.web.resource;

import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIAS;
import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIA_ID;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import fr.mediarollRest.mediarollRest.exception.MediaNotFoundException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.model.Picture;
import fr.mediarollRest.mediarollRest.service.implementation.AccountService;
import fr.mediarollRest.mediarollRest.service.implementation.MediaManagerService;
import fr.mediarollRest.mediarollRest.service.implementation.MediaService;

@RunWith(SpringRunner.class)
@WebMvcTest(MediaResource.class)
public class MediaResourceTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AccountService accountService;

	@MockBean
	private MediaService mediaService;

	@MockBean
	private MediaManagerService mediaManagerService;

	private String fileName;
	
	private MockMultipartFile media;
	
	@Before
	public void init() throws IOException{
		
		this.fileName = "image.jpg";
		InputStream fileToUpload = new ClassPathResource(fileName).getInputStream();
		media = new MockMultipartFile("media",fileName, MediaType.IMAGE_JPEG.getType(), fileToUpload);
	}
	
	@Test
	@WithMockUser
	public void testUploadMedia() throws Exception {
		
		Account account = new Account();
		account.setMediaList(new ArrayList<>());
		boolean isMedia = true;
		
		when(mediaManagerService.isMedia(any(MockMultipartFile.class))).thenReturn(isMedia);
		when(accountService.findByMail(anyString())).thenReturn(Optional.of(account));
		when(mediaManagerService.saveMediaInFileSystem(any(MockMultipartFile.class))).thenReturn(new Picture());
		when(accountService.saveAccount(any(Account.class))).thenReturn(account);
		
		ResultActions result = mockMvc.perform(fileUpload((MEDIAS)).file(media));
		result.andExpect(status().isCreated());
		
		verify(mediaManagerService).isMedia(any(MockMultipartFile.class));
		verify(accountService).findByMail(anyString());
		verify(mediaManagerService).saveMediaInFileSystem(any(MockMultipartFile.class));
		verify(accountService).saveAccount(any(Account.class));
	}
	
	@Test
	@WithMockUser
	public void testUploadMediaNotAllowed() throws Exception {
		
		Account account = new Account();
		account.setMediaList(new ArrayList<>());
		boolean isMEdia = false;
		
		when(mediaManagerService.isMedia(any(MockMultipartFile.class))).thenReturn(isMEdia);
		
		ResultActions result = mockMvc.perform(fileUpload((MEDIAS)).file(media));
		result.andExpect(status().isBadRequest());
		
		verify(mediaManagerService).isMedia(any(MockMultipartFile.class));
	}
	
	@Test
	@WithMockUser
	public void testUploadMediaThrowFileUploadException() throws Exception {
		
		Account account = new Account();
		account.setMediaList(new ArrayList<>());
		
		boolean isMedia = true;
		when(mediaManagerService.isMedia(any(MockMultipartFile.class))).thenReturn(isMedia);
		when(accountService.findByMail(anyString())).thenReturn(Optional.of(account));
		when(mediaManagerService.saveMediaInFileSystem(any(MockMultipartFile.class))).thenThrow(new FileUploadException());
		
		ResultActions result = mockMvc.perform(fileUpload((MEDIAS)).file(media));
		result.andExpect(status().isBadRequest());
		
		verify(mediaManagerService).isMedia(any(MockMultipartFile.class));
		verify(accountService).findByMail(anyString());
		verify(mediaManagerService).saveMediaInFileSystem(any(MockMultipartFile.class));
	}
	
	@Test
	@WithMockUser
	public void testUploadMediaThrowIOException() throws Exception {
		
		Account account = new Account();
		account.setMediaList(new ArrayList<>());
		
		boolean isMedia = true;
		when(mediaManagerService.isMedia(any(MockMultipartFile.class))).thenReturn(isMedia);
		when(accountService.findByMail(anyString())).thenReturn(Optional.of(account));
		when(mediaManagerService.saveMediaInFileSystem(any(MockMultipartFile.class))).thenThrow(new IOException());
		
		ResultActions result = mockMvc.perform(fileUpload((MEDIAS)).file(media));
		result.andExpect(status().isInternalServerError());
		
		verify(mediaManagerService).isMedia(any(MockMultipartFile.class));
		verify(accountService).findByMail(anyString());
		verify(mediaManagerService).saveMediaInFileSystem(any(MockMultipartFile.class));
	}

	@Test
	@WithMockUser
	public void testDeleteMediaSuccess() throws Exception {
		String filePath = "src/test/resources/fileToDelete.txt";
	    Picture picture = new Picture();
	    picture.setFilePath(filePath);
	    Long id = 1L;
		
		when(mediaService.findById(anyLong())).thenReturn(picture);
		when(mediaManagerService.deleteMediaInFileSystem(anyString())).thenReturn(true);
		when(mediaService.deleteMediaById(anyLong())).thenReturn(true);
		
		ResultActions result = mockMvc.perform(delete(MEDIAS+MEDIA_ID, id));
		result.andExpect(status().isNoContent());
		
		verify(mediaService).findById(eq(id));
		verify(mediaManagerService).deleteMediaInFileSystem(eq(picture.getFilePath()));
		verify(mediaService).deleteMediaById(eq(id));
	}
	
	@Test
	@WithMockUser
	public void testDeleteMediaNotFound() throws Exception {
		String filePath = "src/test/resources/fileToDelete.txt";
	    Picture picture = new Picture();
	    picture.setFilePath(filePath);
	    Long id = 1L;
		
		when(mediaService.findById(anyLong())).thenThrow(new MediaNotFoundException());
		
		ResultActions result = mockMvc.perform(delete(MEDIAS+MEDIA_ID, id));
		result.andExpect(status().isNotFound());
		
		verify(mediaService).findById(eq(id));
	}
	
	@Test
	@WithMockUser
	public void testDeleteMediaIsNotDeleteInFileSystem() throws Exception {
		String filePath = "src/test/resources/fileToDelete.txt";
	    Picture picture = new Picture();
	    picture.setFilePath(filePath);
	    Long id = 1L;
		
		when(mediaService.findById(anyLong())).thenReturn(picture);
		when(mediaManagerService.deleteMediaInFileSystem(anyString())).thenReturn(false);
		
		ResultActions result = mockMvc.perform(delete(MEDIAS+MEDIA_ID, id));
		result.andExpect(status().isInternalServerError());
		
		verify(mediaService).findById(eq(id));
		verify(mediaManagerService).deleteMediaInFileSystem(eq(picture.getFilePath()));
	}
	
	@Test
	@WithMockUser
	public void testDeleteMediaIsNotDeleteInDb() throws Exception {
		String filePath = "src/test/resources/fileToDelete.txt";
	    Picture picture = new Picture();
	    picture.setFilePath(filePath);
	    Long id = 1L;
		
		when(mediaService.findById(anyLong())).thenReturn(picture);
		when(mediaManagerService.deleteMediaInFileSystem(anyString())).thenReturn(true);
		when(mediaService.deleteMediaById(anyLong())).thenReturn(false);
		
		ResultActions result = mockMvc.perform(delete(MEDIAS+MEDIA_ID, id));
		result.andExpect(status().isInternalServerError());
		
		verify(mediaService).findById(eq(id));
		verify(mediaManagerService).deleteMediaInFileSystem(eq(picture.getFilePath()));
		verify(mediaService).deleteMediaById(eq(id));
	}
	
	
	
	
}
