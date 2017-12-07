package fr.mediarollRest.mediarollRest.web.controller;

import static fr.mediarollRest.mediarollRest.constant.Paths.ACCOUNT_WITH_MAIL;
import static fr.mediarollRest.mediarollRest.constant.Paths.ALBUMS;
import static fr.mediarollRest.mediarollRest.constant.Paths.ALBUM_WITH_ID;
import static fr.mediarollRest.mediarollRest.constant.Paths.COVER;
import static fr.mediarollRest.mediarollRest.constant.Paths.MEDIAS_WITH_ID;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.mediarollRest.mediarollRest.exception.AccountNotFoundException;
import fr.mediarollRest.mediarollRest.exception.AlbumNotFoundException;
import fr.mediarollRest.mediarollRest.exception.MediaNotFoundException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.model.Album;
import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.model.Picture;
import fr.mediarollRest.mediarollRest.resource.AlbumResource;
import fr.mediarollRest.mediarollRest.service.implementation.AccountService;
import fr.mediarollRest.mediarollRest.service.implementation.AlbumService;
import fr.mediarollRest.mediarollRest.service.implementation.MediaService;
import fr.mediarollRest.mediarollRest.web.controller.AlbumController;
import fr.mediarollRest.mediarollRest.web.resource.assembler.AlbumAssembler;

@RunWith(SpringRunner.class)
@WebMvcTest(AlbumController.class)
public class AlbumControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountService accountService;

	@MockBean
	private AlbumService albumService;

	@MockBean
	private MediaService mediaService;
	
	@MockBean
	private AlbumAssembler albumAssembler;

	@InjectMocks
	private AlbumController albumResource;

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	@WithMockUser
	public void testGetAlbum() throws Exception {
		Long albumId = 1L;

		when(albumService.findAlbumById(eq(albumId))).thenReturn(new Album());
		when(albumAssembler.toResource(any(Album.class))).thenReturn(new AlbumResource(new Album()));

		ResultActions result = mockMvc.perform(get(ALBUM_WITH_ID, albumId));

		result.andExpect(status().isOk());

		verify(albumService).findAlbumById(eq(albumId));
		verify(albumAssembler).toResource(any(Album.class));

	}

	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	public void testGetAlbumThrowAlbumNotFoundException() throws Exception {

		Long albumId = 1L;

		when(albumService.findAlbumById(eq(albumId))).thenThrow(AlbumNotFoundException.class);

		ResultActions result = mockMvc.perform(get(ALBUM_WITH_ID, albumId));

		result.andExpect(status().isNotFound());

		verify(albumService).findAlbumById(eq(albumId));
	}

	@Test
	@WithMockUser
	public void testCreateAlbum() throws Exception {

		when(accountService.findByMail(anyString())).thenReturn(new Account());
		when(albumService.saveAlbum(any(Album.class))).thenReturn(new Album());
		when(albumAssembler.toResource(any(Album.class))).thenReturn(new AlbumResource(new Album()));

		ResultActions result = mockMvc.perform(
				post(ALBUMS).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(new Album())));

		result.andExpect(status().isOk());

		verify(accountService).findByMail(anyString());
		verify(albumService).saveAlbum(any(Album.class));
		verify(albumAssembler).toResource(any(Album.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	public void testCreateAlbumThrowMailNotFoundException() throws Exception {

		when(accountService.findByMail(anyString())).thenThrow(AccountNotFoundException.class);

		ResultActions result = mockMvc.perform(
				post(ALBUMS).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(new Album())));

		result.andExpect(status().isNotFound());

		verify(accountService).findByMail(anyString());
		verify(albumService, never()).saveAlbum(any(Album.class));
	}

	@Test
	@WithMockUser
	public void testUpdateAlbum() throws Exception {

		Long albumId = 1L;

		when(albumService.updateAlbum(eq(albumId), any(Album.class))).thenReturn(new Album());
		when(albumAssembler.toResource(any(Album.class))).thenReturn(new AlbumResource(new Album()));
		
		ResultActions result = mockMvc.perform(patch(ALBUM_WITH_ID, albumId).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(new Album())));

		result.andExpect(status().isCreated());

		verify(albumService).updateAlbum(eq(albumId), any(Album.class));
		verify(albumAssembler).toResource(any(Album.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	public void testUpdateAlbumThrowAlbumNotFoundException() throws Exception {

		Long albumId = 1L;

		when(albumService.updateAlbum(eq(albumId), any(Album.class))).thenThrow(AlbumNotFoundException.class);

		ResultActions result = mockMvc.perform(patch(ALBUM_WITH_ID, albumId).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(new Album())));

		result.andExpect(status().isNotFound());

		verify(albumService).updateAlbum(eq(albumId), any(Album.class));
	}

	@Test
	@WithMockUser
	public void testDeleteAlbum() throws Exception {
		Long albumId = 1L;
		when(albumService.deleteAlbum(eq(albumId))).thenReturn(true);

		ResultActions result = mockMvc.perform(delete(ALBUM_WITH_ID, albumId));

		result.andExpect(status().isNoContent());

		verify(albumService).deleteAlbum(eq(albumId));
	}

	@Test
	@WithMockUser
	public void testDeleteAlbumInternalServerError() throws Exception {
		Long albumId = 1L;
		when(albumService.deleteAlbum(eq(albumId))).thenReturn(false);

		ResultActions result = mockMvc.perform(delete(ALBUM_WITH_ID, albumId));

		result.andExpect(status().isInternalServerError());

		verify(albumService).deleteAlbum(eq(albumId));
	}

	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	public void testDeleteAlbumThrowAlbumNotFoundException() throws Exception {
		Long albumId = 1L;
		when(albumService.deleteAlbum(eq(albumId))).thenThrow(AlbumNotFoundException.class);

		ResultActions result = mockMvc.perform(delete(ALBUM_WITH_ID, albumId));

		result.andExpect(status().isNotFound());

		verify(albumService).deleteAlbum(eq(albumId));
	}

	@Test
	@WithMockUser
	public void testAddCoverToAlbum() throws Exception {
		Long pictureId = 1L;
		Long albumId = 1L;

		when(mediaService.findById(eq(pictureId))).thenReturn(new Picture());
		when(albumService.findAlbumById(eq(albumId))).thenReturn(new Album());
		when(albumService.saveAlbum(any(Album.class))).thenReturn(new Album());
		when(albumAssembler.toResource(any(Album.class))).thenReturn(new AlbumResource(new Album()));

		ResultActions result = mockMvc.perform(put(ALBUM_WITH_ID + COVER + MEDIAS_WITH_ID, albumId, pictureId));

		result.andExpect(status().isCreated());

		verify(mediaService).findById(eq(pictureId));
		verify(albumService).findAlbumById(eq(albumId));
		verify(albumService).saveAlbum(any(Album.class));
		verify(albumAssembler).toResource(any(Album.class));


	}

	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	public void testAddCoverToAlbumThrowMediaNotFoundException() throws Exception {
		Long pictureId = 1L;
		Long albumId = 1L;

		when(mediaService.findById(eq(pictureId))).thenThrow(MediaNotFoundException.class);

		ResultActions result = mockMvc.perform(put(ALBUM_WITH_ID + COVER + MEDIAS_WITH_ID, albumId, pictureId));

		result.andExpect(status().isNotFound());

		verify(mediaService).findById(eq(pictureId));
		verify(albumService, never()).findAlbumById(eq(albumId));
		verify(albumService, never()).saveAlbum(any(Album.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	public void testAddCoverToAlbumThrowAlbumNotFoundException() throws Exception {
		Long pictureId = 1L;
		Long albumId = 1L;

		when(mediaService.findById(eq(pictureId))).thenReturn(new Picture());
		when(albumService.findAlbumById(eq(albumId))).thenThrow(AlbumNotFoundException.class);

		ResultActions result = mockMvc.perform(put(ALBUM_WITH_ID + COVER + MEDIAS_WITH_ID, albumId, pictureId));

		result.andExpect(status().isNotFound());

		verify(mediaService).findById(eq(pictureId));
		verify(albumService).findAlbumById(eq(albumId));
		verify(albumService, never()).saveAlbum(any(Album.class));
	}

	@Test
	@WithMockUser
	public void testAddMediaToAlbum() throws Exception {
		Long pictureId = 1L;
		Long albumId = 1L;

		Album album = new Album();
		album.setMedias(new ArrayList<>());

		when(albumService.findAlbumById(eq(albumId))).thenReturn(album);
		when(mediaService.findById(eq(pictureId))).thenReturn(new Picture());
		when(albumService.saveAlbum(any(Album.class))).thenReturn(new Album());
		when(albumAssembler.toResource(any(Album.class))).thenReturn(new AlbumResource(new Album()));


		ResultActions result = mockMvc.perform(put(ALBUM_WITH_ID + MEDIAS_WITH_ID, albumId, pictureId));

		result.andExpect(status().isCreated());

		verify(albumService).findAlbumById(eq(albumId));
		verify(mediaService).findById(eq(pictureId));
		verify(albumService).saveAlbum(any(Album.class));
		verify(albumAssembler).toResource(any(Album.class));

	}

	@Test
	@WithMockUser
	public void testAddMediaToAlbumAlreadyExist() throws Exception {
		Long pictureId = 1L;
		Long albumId = 1L;
		Picture picture = new Picture();
		picture.setId(pictureId);

		Album album = new Album();
		album.setMedias(Arrays.asList(picture));

		when(albumService.findAlbumById(eq(albumId))).thenReturn(album);
		when(mediaService.findById(eq(pictureId))).thenReturn(picture);

		ResultActions result = mockMvc.perform(put(ALBUM_WITH_ID + MEDIAS_WITH_ID, albumId, pictureId));

		result.andExpect(status().isConflict());

		verify(albumService).findAlbumById(eq(albumId));
		verify(mediaService).findById(eq(pictureId));
		verify(albumService, never()).saveAlbum(any(Album.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	public void testAddMediaToAlbumThrowAlbumNotFoundException() throws Exception {
		Long pictureId = 1L;
		Long albumId = 1L;

		Album album = new Album();
		album.setMedias(new ArrayList<>());

		when(albumService.findAlbumById(eq(albumId))).thenThrow(AlbumNotFoundException.class);

		ResultActions result = mockMvc.perform(put(ALBUM_WITH_ID + MEDIAS_WITH_ID, albumId, pictureId));

		result.andExpect(status().isNotFound());

		verify(albumService).findAlbumById(eq(albumId));
		verify(mediaService, never()).findById(eq(pictureId));
		verify(albumService, never()).saveAlbum(any(Album.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	public void testAddMediaToAlbumThrowMediaNotFoundException() throws Exception {
		Long pictureId = 1L;
		Long albumId = 1L;

		Album album = new Album();
		album.setMedias(new ArrayList<>());

		when(albumService.findAlbumById(eq(albumId))).thenReturn(album);
		when(mediaService.findById(eq(pictureId))).thenThrow(MediaNotFoundException.class);

		ResultActions result = mockMvc.perform(put(ALBUM_WITH_ID + MEDIAS_WITH_ID, albumId, pictureId));

		result.andExpect(status().isNotFound());

		verify(albumService).findAlbumById(eq(albumId));
		verify(mediaService).findById(eq(pictureId));
		verify(albumService, never()).saveAlbum(any(Album.class));
	}

	@Test
	@WithMockUser
	public void testDeleteMediaFromAlbum() throws Exception {
		Long pictureId = 1L;
		Long albumId = 1L;
		Picture picture = new Picture();
		picture.setId(pictureId);

		Album album = new Album();
		ArrayList<Media> mediasList = new ArrayList<>();
		mediasList.add(picture);
		album.setMedias(mediasList);

		when(albumService.findAlbumById(eq(albumId))).thenReturn(album);
		when(mediaService.findById(eq(pictureId))).thenReturn(picture);
		when(albumService.saveAlbum(any(Album.class))).thenReturn(new Album());

		ResultActions result = mockMvc.perform(delete(ALBUM_WITH_ID + MEDIAS_WITH_ID, albumId, pictureId));

		result.andExpect(status().isNoContent());

		verify(albumService).findAlbumById(eq(albumId));
		verify(mediaService).findById(eq(pictureId));
		verify(albumService).saveAlbum(any(Album.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	public void testDeleteMediaFromAlbumThrowMediaNotFoundException() throws Exception {
		Long pictureId = 1L;
		Long albumId = 1L;
		Picture picture = new Picture();
		picture.setId(pictureId);

		Album album = new Album();
		ArrayList<Media> mediasList = new ArrayList<>();
		mediasList.add(picture);
		album.setMedias(mediasList);

		when(albumService.findAlbumById(eq(albumId))).thenReturn(album);
		when(mediaService.findById(eq(pictureId))).thenThrow(MediaNotFoundException.class);

		ResultActions result = mockMvc.perform(delete(ALBUM_WITH_ID + MEDIAS_WITH_ID, albumId, pictureId));

		result.andExpect(status().isNotFound());

		verify(albumService).findAlbumById(eq(albumId));
		verify(mediaService).findById(eq(pictureId));
		verify(albumService, never()).saveAlbum(any(Album.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	public void testDeleteMediaFromAlbumThrowAlbumNotFoundException() throws Exception {
		Long pictureId = 1L;
		Long albumId = 1L;
		Picture picture = new Picture();
		picture.setId(pictureId);

		Album album = new Album();
		ArrayList<Media> mediasList = new ArrayList<>();
		mediasList.add(picture);
		album.setMedias(mediasList);

		when(albumService.findAlbumById(eq(albumId))).thenThrow(AlbumNotFoundException.class);

		ResultActions result = mockMvc.perform(delete(ALBUM_WITH_ID + MEDIAS_WITH_ID, albumId, pictureId));

		result.andExpect(status().isNotFound());

		verify(albumService).findAlbumById(eq(albumId));
		verify(mediaService, never()).findById(eq(pictureId));
		verify(albumService, never()).saveAlbum(any(Album.class));
	}

	@Test
	@WithMockUser
	public void testDeleteMediaFromAlbumNotInAlbumList() throws Exception {
		Long pictureId = 1L;
		Long albumId = 1L;
		Picture picture = new Picture();

		Album album = new Album();
		ArrayList<Media> mediasList = new ArrayList<>();
		album.setMedias(mediasList);

		when(albumService.findAlbumById(eq(albumId))).thenReturn(album);
		when(mediaService.findById(eq(pictureId))).thenReturn(picture);

		ResultActions result = mockMvc.perform(delete(ALBUM_WITH_ID + MEDIAS_WITH_ID, albumId, pictureId));

		result.andExpect(status().isNotFound());

		verify(albumService).findAlbumById(eq(albumId));
		verify(mediaService).findById(eq(pictureId));
		verify(albumService, never()).saveAlbum(any(Album.class));
	}

	@Test
	@WithMockUser(username = "owner@mail.fr")
	public void testAddUserToSharedList() throws Exception {
		Long albumId = 1L;
		String mail = "test@mail.fr";
		Album album = new Album();
		album.setSharedPeople(new ArrayList<>());

		when(albumService.findAlbumById(eq(albumId))).thenReturn(album);
		when(accountService.findByMail(eq(mail))).thenReturn(new Account());
		when(albumService.saveAlbum(any(Album.class))).thenReturn(new Album());
		when(albumAssembler.toResource(any(Album.class))).thenReturn(new AlbumResource(new Album()));


		ResultActions result = mockMvc.perform(put(ALBUM_WITH_ID + ACCOUNT_WITH_MAIL, albumId, mail));

		result.andExpect(status().isCreated());

		verify(albumService).findAlbumById(eq(albumId));
		verify(accountService).findByMail(eq(mail));
		verify(albumService).saveAlbum(any(Album.class));
		verify(albumAssembler).toResource(any(Album.class));

	}

	@Test
	@WithMockUser(username = "owner@mail.fr")
	public void testAddUserToSharedListTryToShareToHimself() throws Exception {
		Long albumId = 1L;
		String mail = "owner@mail.fr";
		
		ResultActions result = mockMvc.perform(put(ALBUM_WITH_ID + ACCOUNT_WITH_MAIL, albumId, mail));

		result.andExpect(status().isConflict());
		
		verify(albumService, never()).findAlbumById(eq(albumId));
		verify(accountService, never()).findByMail(eq(mail));
		verify(albumService, never()).saveAlbum(any(Album.class));
	}
	
	@Test
	@WithMockUser(username = "owner@mail.fr")
	public void testAddUserToSharedListAlreadyInList() throws Exception {
		Long albumId = 1L;
		String mail = "test@mail.fr";
		Album album = new Album();
		ArrayList<Account> sharedPeopleList = new ArrayList<>();
		Account account = new Account();
		account.setMail(mail);
		sharedPeopleList.add(account);
		album.setSharedPeople(sharedPeopleList);

		when(albumService.findAlbumById(eq(albumId))).thenReturn(album);
		when(accountService.findByMail(eq(mail))).thenReturn(account);

		ResultActions result = mockMvc.perform(put(ALBUM_WITH_ID + ACCOUNT_WITH_MAIL, albumId, mail));

		result.andExpect(status().isConflict());

		verify(albumService).findAlbumById(eq(albumId));
		verify(accountService).findByMail(eq(mail));
		verify(albumService, never()).saveAlbum(any(Album.class));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser(username = "owner@mail.fr")
	public void testAddUserToSharedLisThrowAlbumNotFoundExceptiont() throws Exception {
		Long albumId = 1L;
		String mail = "test@mail.fr";
		Album album = new Album();
		album.setSharedPeople(new ArrayList<>());

		when(albumService.findAlbumById(eq(albumId))).thenThrow(AlbumNotFoundException.class);

		ResultActions result = mockMvc.perform(put(ALBUM_WITH_ID + ACCOUNT_WITH_MAIL, albumId, mail));

		result.andExpect(status().isNotFound());

		verify(albumService).findAlbumById(eq(albumId));
		verify(accountService, never()).findByMail(eq(mail));
		verify(albumService, never()).saveAlbum(any(Album.class));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser(username = "owner@mail.fr")
	public void testAddUserToSharedListThrowAccountNotFoundException() throws Exception {
		Long albumId = 1L;
		String mail = "test@mail.fr";
		Album album = new Album();
		album.setSharedPeople(new ArrayList<>());

		when(albumService.findAlbumById(eq(albumId))).thenReturn(album);
		when(accountService.findByMail(eq(mail))).thenThrow(AccountNotFoundException.class);

		ResultActions result = mockMvc.perform(put(ALBUM_WITH_ID + ACCOUNT_WITH_MAIL, albumId, mail));

		result.andExpect(status().isNotFound());

		verify(albumService).findAlbumById(eq(albumId));
		verify(accountService).findByMail(eq(mail));
		verify(albumService, never()).saveAlbum(any(Album.class));
	}

	@Test
	@WithMockUser
	public void testDeleteUserFromSharedList() throws Exception {
		Long albumId = 1L;
		String mail = "test@mail.fr";
		Album album = new Album();
		ArrayList<Account> sharedPeopleList = new ArrayList<>();
		Account account = new Account();
		account.setMail(mail);
		sharedPeopleList.add(account);
		album.setSharedPeople(sharedPeopleList);


		when(albumService.findAlbumById(eq(albumId))).thenReturn(album);
		when(accountService.findByMail(eq(mail))).thenReturn(account);
		when(albumService.saveAlbum(any(Album.class))).thenReturn(new Album());
		
		ResultActions result = mockMvc.perform(delete(ALBUM_WITH_ID + ACCOUNT_WITH_MAIL, albumId, mail));
		
		result.andExpect(status().isNoContent());
		
		verify(albumService).findAlbumById(eq(albumId));
		verify(accountService).findByMail(eq(mail));
		verify(albumService).saveAlbum(any(Album.class));
	}
	
	@Test
	@WithMockUser
	public void testDeleteUserFromSharedListAccountNotInList() throws Exception {
		Long albumId = 1L;
		String mail = "test@mail.fr";
		Album album = new Album();
		ArrayList<Account> sharedPeopleList = new ArrayList<>();
		Account account = new Account();
		account.setMail(mail);
		album.setSharedPeople(sharedPeopleList);


		when(albumService.findAlbumById(eq(albumId))).thenReturn(album);
		when(accountService.findByMail(eq(mail))).thenReturn(account);
		when(albumService.saveAlbum(any(Album.class))).thenReturn(new Album());
		
		ResultActions result = mockMvc.perform(delete(ALBUM_WITH_ID + ACCOUNT_WITH_MAIL, albumId, mail));
		
		result.andExpect(status().isNotFound());
		
		verify(albumService).findAlbumById(eq(albumId));
		verify(accountService).findByMail(eq(mail));
		verify(albumService, never()).saveAlbum(any(Album.class));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	public void testDeleteUserFromSharedListThrowAlbumNotFoundException() throws Exception {
		Long albumId = 1L;
		String mail = "test@mail.fr";
		Album album = new Album();
		ArrayList<Account> sharedPeopleList = new ArrayList<>();
		Account account = new Account();
		account.setMail(mail);
		sharedPeopleList.add(account);
		album.setSharedPeople(sharedPeopleList);


		when(albumService.findAlbumById(eq(albumId))).thenThrow(AlbumNotFoundException.class);
		
		ResultActions result = mockMvc.perform(delete(ALBUM_WITH_ID + ACCOUNT_WITH_MAIL, albumId, mail));
		
		result.andExpect(status().isNotFound());
		
		verify(albumService).findAlbumById(eq(albumId));
		verify(accountService, never()).findByMail(eq(mail));
		verify(albumService, never()).saveAlbum(any(Album.class));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	public void testDeleteUserFromSharedListThrowAccountNotFoundException() throws Exception {
		Long albumId = 1L;
		String mail = "test@mail.fr";
		Album album = new Album();
		ArrayList<Account> sharedPeopleList = new ArrayList<>();
		Account account = new Account();
		account.setMail(mail);
		sharedPeopleList.add(account);
		album.setSharedPeople(sharedPeopleList);


		when(albumService.findAlbumById(eq(albumId))).thenReturn(album);
		when(accountService.findByMail(eq(mail))).thenThrow(AccountNotFoundException.class);
		when(albumService.saveAlbum(any(Album.class))).thenReturn(new Album());
		
		ResultActions result = mockMvc.perform(delete(ALBUM_WITH_ID + ACCOUNT_WITH_MAIL, albumId, mail));
		
		result.andExpect(status().isNotFound());
		
		verify(albumService).findAlbumById(eq(albumId));
		verify(accountService).findByMail(eq(mail));
		verify(albumService, never()).saveAlbum(any(Album.class));
	}

	@Test
	@WithMockUser("test@mail.fr")
	public void testGetAllAlbums() throws Exception {
		String mail = "test@mail.fr";
		ArrayList<Album> albumList = new ArrayList<>();
		albumList.add(new Album());

		Account account = new Account();
		when(accountService.findByMail(eq(mail))).thenReturn(account);
		when(albumService.findAlbumByOwner(any(Account.class))).thenReturn(albumList);
		when(albumAssembler.toResource(any(Album.class))).thenReturn(new AlbumResource(new Album()));

		ResultActions result = mockMvc.perform(get(ALBUMS));
		
		result.andExpect(status().isOk());
		
		verify(accountService).findByMail(eq(mail));
		verify(albumService).findAlbumByOwner(any(Account.class));
		verify(albumAssembler).toResource(any(Album.class));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser("test@mail.fr")
	public void testGetAllAlbumsThrowsAccountNotFoundException() throws Exception {
		String mail = "test@mail.fr";
		ArrayList<Album> albumList = new ArrayList<>();
		albumList.add(new Album());

		when(accountService.findByMail(eq(mail))).thenThrow(AccountNotFoundException.class);

		ResultActions result = mockMvc.perform(get(ALBUMS));
		
		result.andExpect(status().isNotFound());
		
		verify(accountService).findByMail(eq(mail));
		verify(albumService, never()).findAlbumByOwner(any(Account.class));
		verify(albumAssembler, never()).toResource(any(Album.class));
	}

}
