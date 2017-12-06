package fr.mediarollRest.mediarollRest.web.resource;

import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIAS;
import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIAS_WITH_ID;
import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIA_ID;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.mediarollRest.mediarollRest.exception.AccountNotFoundException;
import fr.mediarollRest.mediarollRest.exception.MediaNotFoundException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.model.Picture;
import fr.mediarollRest.mediarollRest.service.implementation.AccountService;
import fr.mediarollRest.mediarollRest.service.implementation.MediaManagerService;
import fr.mediarollRest.mediarollRest.service.implementation.MediaService;

@RunWith(SpringRunner.class)
@WebMvcTest(MediaController.class)
public class MediaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountService accountService;

	@MockBean
	private MediaService mediaService;

	@MockBean
	private MediaManagerService mediaManagerService;

	private String fileName = "image.jpg";

	private MockMultipartFile media;

	@Before
	public void init() throws IOException {

		InputStream fileToUpload = new ClassPathResource(fileName).getInputStream();
		media = new MockMultipartFile("file", fileName, MediaType.IMAGE_JPEG.getType(), fileToUpload);
	}

	@Test
	@WithMockUser
	public void testUploadMedia() throws Exception {

		Account account = new Account();
		account.setMediaList(new ArrayList<>());
		boolean isMedia = true;

		when(mediaManagerService.isMedia(any(MockMultipartFile.class))).thenReturn(isMedia);
		when(accountService.findByMail(anyString())).thenReturn(account);
		when(mediaManagerService.saveMediaInFileSystem(any(MockMultipartFile.class))).thenReturn(new Picture());
		when(mediaService.saveMedia(any(Media.class))).thenReturn(new Picture());

		ResultActions result = mockMvc.perform(fileUpload((MEDIAS)).file(media));
		
		result.andExpect(status().isCreated());
		result.andDo(print());

		verify(mediaManagerService).isMedia(any(MockMultipartFile.class));
		verify(accountService).findByMail(anyString());
		verify(mediaManagerService).saveMediaInFileSystem(any(MockMultipartFile.class));
		verify(mediaService).saveMedia(any(Media.class));
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
		result.andDo(print());

		verify(mediaManagerService).isMedia(any(MockMultipartFile.class));
	}

	@Test
	@WithMockUser
	public void testUploadMediaThrowFileUploadException() throws Exception {

		Account account = new Account();
		account.setMediaList(new ArrayList<>());

		boolean isMedia = true;
		when(mediaManagerService.isMedia(any(MockMultipartFile.class))).thenReturn(isMedia);
		when(accountService.findByMail(anyString())).thenReturn(account);
		when(mediaManagerService.saveMediaInFileSystem(any(MockMultipartFile.class)))
				.thenThrow(new FileUploadException());

		ResultActions result = mockMvc.perform(fileUpload((MEDIAS)).file(media));
		
		result.andExpect(status().isBadRequest());
		result.andDo(print());

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
		when(accountService.findByMail(anyString())).thenReturn(account);
		when(mediaManagerService.saveMediaInFileSystem(any(MockMultipartFile.class))).thenThrow(new IOException());

		ResultActions result = mockMvc.perform(fileUpload((MEDIAS)).file(media));
		
		result.andExpect(status().isInternalServerError());
		result.andDo(print());

		verify(mediaManagerService).isMedia(any(MockMultipartFile.class));
		verify(accountService).findByMail(anyString());
		verify(mediaManagerService).saveMediaInFileSystem(any(MockMultipartFile.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	public void testUploadMediaThrowMailNotFoundException() throws Exception {

		Account account = new Account();
		account.setMediaList(new ArrayList<>());

		boolean isMedia = true;
		when(mediaManagerService.isMedia(any(MockMultipartFile.class))).thenReturn(isMedia);
		when(accountService.findByMail(anyString())).thenThrow(AccountNotFoundException.class);

		ResultActions result = mockMvc.perform(fileUpload((MEDIAS)).file(media));
		
		result.andExpect(status().isNotFound());
		result.andDo(print());

		verify(mediaManagerService).isMedia(any(MockMultipartFile.class));
		verify(accountService).findByMail(anyString());
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

		ResultActions result = mockMvc.perform(delete(MEDIAS + MEDIA_ID, id));
		
		result.andExpect(status().isNoContent());
		result.andDo(print());

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

		ResultActions result = mockMvc.perform(delete(MEDIAS + MEDIA_ID, id));
		
		result.andExpect(status().isNotFound());
		result.andDo(print());

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

		ResultActions result = mockMvc.perform(delete(MEDIAS + MEDIA_ID, id));
		
		result.andExpect(status().isInternalServerError());
		result.andDo(print());

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

		ResultActions result = mockMvc.perform(delete(MEDIAS + MEDIA_ID, id));
		
		result.andExpect(status().isInternalServerError());
		result.andDo(print());

		verify(mediaService).findById(eq(id));
		verify(mediaManagerService).deleteMediaInFileSystem(eq(picture.getFilePath()));
		verify(mediaService).deleteMediaById(eq(id));
	}

	@Test
	@WithMockUser
	public void testUpdateMediaInfo() throws Exception {
		Long id = 1L;

		Picture pictureToUpdate = new Picture();
		Picture pictureUpdated = new Picture();
		ObjectMapper mapper = new ObjectMapper();

		when(mediaService.updateMediaInfo(eq(id), any(Picture.class))).thenReturn(pictureUpdated);

		ResultActions result = mockMvc.perform(put(MEDIAS + MEDIA_ID, id).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(pictureToUpdate)));
		
		result.andExpect(status().isOk());
		result.andDo(print());

		verify(mediaService).updateMediaInfo(eq(id), any(Picture.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	public void testUpdateMediaInfoMediaNotFoundException() throws Exception {
		Long id = 1L;

		Picture pictureToUpdate = new Picture();
		ObjectMapper mapper = new ObjectMapper();

		when(mediaService.updateMediaInfo(eq(id), any(Picture.class))).thenThrow(MediaNotFoundException.class);

		ResultActions result = mockMvc.perform(put(MEDIAS + MEDIA_ID, id).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(pictureToUpdate)));
		
		result.andExpect(status().isNotFound());
		result.andDo(print());

		verify(mediaService).updateMediaInfo(eq(id), any(Picture.class));
	}

	@Test
	@WithMockUser("test@mail.fr")
	public void testGetAllMedias() throws Exception {

		String mailAccount = "test@mail.fr";

		when(accountService.findByMail(eq(mailAccount))).thenReturn(new Account());

		ResultActions result = mockMvc.perform(get(MEDIAS));

		result.andExpect(status().isOk());
		result.andDo(print());

		verify(accountService).findByMail(eq(mailAccount));
	}

	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser("test@mail.fr")
	public void testGetAllMediasUserNotFound() throws Exception {

		String mailAccount = "test@mail.fr";

		when(accountService.findByMail(eq(mailAccount))).thenThrow(AccountNotFoundException.class);

		ResultActions result = mockMvc.perform(get(MEDIAS));

		result.andExpect(status().isNotFound());
		result.andDo(print());

		verify(accountService).findByMail(eq(mailAccount));
	}

	@Test
	@WithMockUser
	public void testGetImageAsResponseEntity() throws Exception {
		Long mediaId = 1L;
		InputStream pictureStream = new ClassPathResource(fileName).getInputStream();
		
		when(mediaService.findById(eq(mediaId))).thenReturn(new Picture());
		when(mediaManagerService.getInputStreamFromMedia(any(Media.class))).thenReturn(pictureStream);

		ResultActions result = mockMvc.perform(get(MEDIAS_WITH_ID, mediaId));

		result.andExpect(status().isOk());
		result.andDo(print());
		
		verify(mediaService).findById(eq(mediaId));
		verify(mediaManagerService).getInputStreamFromMedia(any(Media.class));
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=MediaNotFoundException.class)
	@WithMockUser
	public void testGetImageAsResponseEntityThrowsMediaNotFoundException() throws Exception {
		Long mediaId = 1L;
		
		when(mediaService.findById(eq(mediaId))).thenThrow(MediaNotFoundException.class);

		ResultActions result = mockMvc.perform(get(MEDIAS_WITH_ID, mediaId));

		result.andExpect(status().isOk());
		result.andDo(print());
		
		verify(mediaService).findById(eq(mediaId));
		verify(mediaManagerService, never()).getInputStreamFromMedia(any(Media.class));
	}

}