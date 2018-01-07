package fr.mediarollRest.mediarollRest.web.controller;

import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIAS;
import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIAS_WITH_ID;
import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIA_ID;
import static fr.mediarollRest.mediarollRest.constant.Paths.PICTURES;
import static fr.mediarollRest.mediarollRest.constant.Paths.VIDEOS;
import static org.mockito.Matchers.any;
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
import java.util.Arrays;
import java.util.UUID;

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
import fr.mediarollRest.mediarollRest.exception.SpaceAvailableNotEnoughException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.model.Picture;
import fr.mediarollRest.mediarollRest.model.Video;
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
	public void testUploadMedias() throws Exception {

		Account account = new Account();
		account.setMediaList(new ArrayList<>());
		boolean isMedia = true;

		when(mediaService.isMedia(any(MockMultipartFile.class))).thenReturn(isMedia);
		when(accountService.findByMail(anyString())).thenReturn(account);
		when(mediaManagerService.saveMediaInFileSystem(any(Account.class), any(MockMultipartFile.class)))
				.thenReturn(new Picture());
		when(mediaService.saveMedia(any(Media.class))).thenReturn(new Picture());

		ResultActions result = mockMvc.perform(fileUpload((MEDIAS)).file(media));

		result.andExpect(status().isCreated());
		result.andDo(print());

		verify(mediaService).isMedia(any(MockMultipartFile.class));
		verify(accountService).findByMail(anyString());
		verify(mediaManagerService).saveMediaInFileSystem(any(Account.class), any(MockMultipartFile.class));
		verify(mediaService).saveMedia(any(Media.class));
	}

	@Test
	@WithMockUser
	public void testUploadMediasNotAllowed() throws Exception {

		Account account = new Account();
		account.setMediaList(new ArrayList<>());
		boolean isMEdia = false;

		when(mediaService.isMedia(any(MockMultipartFile.class))).thenReturn(isMEdia);

		ResultActions result = mockMvc.perform(fileUpload((MEDIAS)).file(media));

		result.andExpect(status().isUnsupportedMediaType());
		result.andDo(print());

		verify(mediaService).isMedia(any(MockMultipartFile.class));
	}

	@Test
	@WithMockUser
	public void testUploadMediasThrowFileUploadException() throws Exception {

		Account account = new Account();
		account.setMediaList(new ArrayList<>());

		boolean isMedia = true;
		when(mediaService.isMedia(any(MockMultipartFile.class))).thenReturn(isMedia);
		when(accountService.findByMail(anyString())).thenReturn(account);
		when(mediaManagerService.saveMediaInFileSystem(any(Account.class), any(MockMultipartFile.class)))
				.thenThrow(new FileUploadException());

		ResultActions result = mockMvc.perform(fileUpload((MEDIAS)).file(media));

		result.andExpect(status().isBadRequest());
		result.andDo(print());

		verify(mediaService).isMedia(any(MockMultipartFile.class));
		verify(accountService).findByMail(anyString());
		verify(mediaManagerService).saveMediaInFileSystem(any(Account.class), any(MockMultipartFile.class));
	}

	@Test
	@WithMockUser
	public void testUploadMediasThrowIOException() throws Exception {

		Account account = new Account();
		account.setMediaList(new ArrayList<>());

		boolean isMedia = true;
		when(mediaService.isMedia(any(MockMultipartFile.class))).thenReturn(isMedia);
		when(accountService.findByMail(anyString())).thenReturn(account);
		when(mediaManagerService.saveMediaInFileSystem(any(Account.class), any(MockMultipartFile.class)))
				.thenThrow(new IOException());

		ResultActions result = mockMvc.perform(fileUpload((MEDIAS)).file(media));

		result.andExpect(status().isInternalServerError());
		result.andDo(print());

		verify(mediaService).isMedia(any(MockMultipartFile.class));
		verify(accountService).findByMail(anyString());
		verify(mediaManagerService).saveMediaInFileSystem(any(Account.class), any(MockMultipartFile.class));
	}

	@Test
	@WithMockUser
	public void testUploadMediasThrowSpaceAvailableNotEnoughException() throws Exception {

		Account account = new Account();
		account.setMediaList(new ArrayList<>());

		boolean isMedia = true;
		when(mediaService.isMedia(any(MockMultipartFile.class))).thenReturn(isMedia);
		when(accountService.findByMail(anyString())).thenReturn(account);
		when(mediaManagerService.saveMediaInFileSystem(any(Account.class), any(MockMultipartFile.class)))
				.thenThrow(new SpaceAvailableNotEnoughException());

		ResultActions result = mockMvc.perform(fileUpload((MEDIAS)).file(media));

		result.andExpect(status().isInsufficientStorage());
		result.andDo(print());

		verify(mediaService).isMedia(any(MockMultipartFile.class));
		verify(accountService).findByMail(anyString());
		verify(mediaManagerService).saveMediaInFileSystem(any(Account.class), any(MockMultipartFile.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	public void testUploadMediasThrowMailNotFoundException() throws Exception {

		Account account = new Account();
		account.setMediaList(new ArrayList<>());

		boolean isMedia = true;
		when(mediaService.isMedia(any(MockMultipartFile.class))).thenReturn(isMedia);
		when(accountService.findByMail(anyString())).thenThrow(AccountNotFoundException.class);

		ResultActions result = mockMvc.perform(fileUpload((MEDIAS)).file(media));

		result.andExpect(status().isNotFound());
		result.andDo(print());

		verify(mediaService).isMedia(any(MockMultipartFile.class));
		verify(accountService).findByMail(anyString());
	}

	@Test
	@WithMockUser
	public void testDeleteMediaSuccess() throws Exception {
		String filePath = "src/test/resources/fileToDelete.txt";
		Picture picture = new Picture();
		picture.setFilePath(filePath);
		String id = UUID.randomUUID().toString();

		when(mediaService.findById(eq(id))).thenReturn(picture);
		when(accountService.findByMail(anyString())).thenReturn(new Account());
		when(mediaManagerService.deleteMediaInFileSystem(any(Account.class), anyString())).thenReturn(true);
		when(mediaService.deleteMediaById(eq(id))).thenReturn(true);

		ResultActions result = mockMvc.perform(delete(MEDIAS + MEDIA_ID, id));

		result.andExpect(status().isNoContent());
		result.andDo(print());

		verify(mediaService).findById(eq(id));
		verify(accountService).findByMail(anyString());
		verify(mediaManagerService).deleteMediaInFileSystem(any(Account.class), eq(picture.getFilePath()));
		verify(mediaService).deleteMediaById(eq(id));
	}

	@Test
	@WithMockUser
	public void testDeleteMediaThrowAccountNotFoundException() throws Exception {
		String filePath = "src/test/resources/fileToDelete.txt";
		Picture picture = new Picture();
		picture.setFilePath(filePath);
		String id = UUID.randomUUID().toString();

		when(mediaService.findById(eq(id))).thenReturn(picture);
		when(accountService.findByMail(anyString())).thenThrow(new AccountNotFoundException());

		ResultActions result = mockMvc.perform(delete(MEDIAS + MEDIA_ID, id));

		result.andExpect(status().isNotFound());
		result.andDo(print());

		verify(mediaService).findById(eq(id));
		verify(accountService).findByMail(anyString());
		verify(mediaManagerService, never()).deleteMediaInFileSystem(any(Account.class), eq(picture.getFilePath()));
		verify(mediaService, never()).deleteMediaById(eq(id));
	}

	@Test
	@WithMockUser
	public void testDeleteMediaNotFound() throws Exception {
		String filePath = "src/test/resources/fileToDelete.txt";
		Picture picture = new Picture();
		picture.setFilePath(filePath);
		String id = UUID.randomUUID().toString();

		when(mediaService.findById(eq(id))).thenThrow(new MediaNotFoundException());

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
		String id = UUID.randomUUID().toString();

		when(mediaService.findById(eq(id))).thenReturn(picture);
		when(accountService.findByMail(anyString())).thenReturn(new Account());
		when(mediaManagerService.deleteMediaInFileSystem(any(Account.class), anyString())).thenReturn(false);

		ResultActions result = mockMvc.perform(delete(MEDIAS + MEDIA_ID, id));

		result.andExpect(status().isInternalServerError());
		result.andDo(print());

		verify(mediaService).findById(eq(id));
		verify(accountService).findByMail(anyString());
		verify(mediaManagerService).deleteMediaInFileSystem(any(Account.class), eq(picture.getFilePath()));
	}

	@Test
	@WithMockUser
	public void testDeleteMediaIsNotDeleteInDb() throws Exception {
		String filePath = "src/test/resources/fileToDelete.txt";
		Picture picture = new Picture();
		picture.setFilePath(filePath);
		String id = UUID.randomUUID().toString();

		when(mediaService.findById(eq(id))).thenReturn(picture);
		when(accountService.findByMail(anyString())).thenReturn(new Account());
		when(mediaManagerService.deleteMediaInFileSystem(any(Account.class), anyString())).thenReturn(true);
		when(mediaService.deleteMediaById(eq(id))).thenReturn(false);

		ResultActions result = mockMvc.perform(delete(MEDIAS + MEDIA_ID, id));

		result.andExpect(status().isInternalServerError());
		result.andDo(print());

		verify(mediaService).findById(eq(id));
		verify(accountService).findByMail(anyString());
		verify(mediaManagerService).deleteMediaInFileSystem(any(Account.class), eq(picture.getFilePath()));
		verify(mediaService).deleteMediaById(eq(id));
	}

	@Test
	@WithMockUser
	public void testUpdateMediaInfo() throws Exception {
		String id = UUID.randomUUID().toString();
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
		String id = UUID.randomUUID().toString();
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

		Account account = new Account();
		ArrayList<Media> mediaList = new ArrayList<>();
		mediaList.add(new Picture());
		account.setMediaList(mediaList);
		when(accountService.findByMail(eq(mailAccount))).thenReturn(account);

		ResultActions result = mockMvc.perform(get(MEDIAS));

		result.andExpect(status().isOk());
		result.andDo(print());

		verify(accountService).findByMail(eq(mailAccount));
	}

	@Test
	@WithMockUser("test@mail.fr")
	public void testGetAllMediasThrowAccountNotFoundException() throws Exception {

		String mailAccount = "test@mail.fr";

		when(accountService.findByMail(eq(mailAccount))).thenThrow(new AccountNotFoundException());

		ResultActions result = mockMvc.perform(get(MEDIAS));

		result.andExpect(status().isNotFound());
		result.andDo(print());

		verify(accountService).findByMail(eq(mailAccount));
	}
	
	@Test
	@WithMockUser
	public void testGetMediaById() throws Exception {

		String mediaId = UUID.randomUUID().toString();
		when(mediaService.findById(eq(mediaId))).thenReturn(new Picture());

		ResultActions result = mockMvc.perform(get(MEDIAS_WITH_ID, mediaId));

		result.andExpect(status().isOk());
		result.andDo(print());

		verify(mediaService).findById(eq(mediaId));

	}

	@Test
	@WithMockUser
	public void testGetMediaByIdThrowMediaNotFoundException() throws Exception {

		String mediaId = UUID.randomUUID().toString();
		when(mediaService.findById(eq(mediaId))).thenThrow(new MediaNotFoundException());

		ResultActions result = mockMvc.perform(get(MEDIAS_WITH_ID, mediaId));

		result.andExpect(status().isNotFound());
		result.andDo(print());

		verify(mediaService).findById(eq(mediaId));

	}

	@Test
	@WithMockUser("test@mail.fr")
	public void testGetAllPictures() throws Exception {
		String mail = "test@mail.fr";
		Picture pic1 = new Picture();
		Picture pic2 = new Picture();
		
		when(mediaService.getAllPictures(eq(mail))).thenReturn(Arrays.asList(pic1,pic2));
		
		ResultActions result = mockMvc.perform(get(PICTURES));
		
		result.andExpect(status().isOk());
		result.andDo(print());
		
		verify(mediaService).getAllPictures(eq(mail));

	}
	
	@Test
	@WithMockUser("test@mail.fr")
	public void testGetAllVideos() throws Exception {
		String mail = "test@mail.fr";
		Video video1 = new Video();
		Video video2 = new Video();
		
		when(mediaService.getAllVideos(eq(mail))).thenReturn(Arrays.asList(video1,video2));
		
		ResultActions result = mockMvc.perform(get(VIDEOS));
		
		result.andExpect(status().isOk());
		result.andDo(print());
		
		verify(mediaService).getAllVideos(eq(mail));

	}
	
}
