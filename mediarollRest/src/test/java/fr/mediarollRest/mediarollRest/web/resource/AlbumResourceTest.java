package fr.mediarollRest.mediarollRest.web.resource;

import static fr.mediarollRest.mediarollRest.constant.Paths.ALBUMS;
import static fr.mediarollRest.mediarollRest.constant.Paths.ALBUM_WITH_ID;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import fr.mediarollRest.mediarollRest.exception.AlbumNotFoundException;
import fr.mediarollRest.mediarollRest.exception.MailNotFoundException;
import fr.mediarollRest.mediarollRest.model.Account;
import fr.mediarollRest.mediarollRest.model.Album;
import fr.mediarollRest.mediarollRest.service.implementation.AccountService;
import fr.mediarollRest.mediarollRest.service.implementation.AlbumService;
import fr.mediarollRest.mediarollRest.service.implementation.MediaService;

@RunWith(SpringRunner.class)
@WebMvcTest(AlbumResource.class)
public class AlbumResourceTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountService accountService;

	@MockBean
	private AlbumService albumService;

	@MockBean
	private MediaService mediaService;

	@InjectMocks
	private AlbumResource albumResource;

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	@WithMockUser
	public void testGetAlbum() throws Exception {
		Long albumId = 1L;

		when(albumService.findAlbumById(eq(albumId))).thenReturn(new Album());

		ResultActions result = mockMvc.perform(get(ALBUM_WITH_ID, albumId));

		result.andExpect(status().isOk());

		verify(albumService).findAlbumById(eq(albumId));

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

		ResultActions result = mockMvc.perform(
				post(ALBUMS).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(new Album())));

		result.andExpect(status().isOk());

		verify(accountService).findByMail(anyString());
		verify(albumService).saveAlbum(any(Album.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	public void testCreateAlbumThrowMailNotFoundException() throws Exception {

		when(accountService.findByMail(anyString())).thenThrow(MailNotFoundException.class);

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

		ResultActions result = mockMvc.perform(put(ALBUM_WITH_ID, albumId).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(new Album())));

		result.andExpect(status().isCreated());

		verify(albumService).updateAlbum(eq(albumId), any(Album.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	public void testUpdateAlbumThrowAlbumNotFoundException() throws Exception {

		Long albumId = 1L;

		when(albumService.updateAlbum(eq(albumId), any(Album.class))).thenThrow(AlbumNotFoundException.class);

		ResultActions result = mockMvc.perform(put(ALBUM_WITH_ID, albumId).contentType(MediaType.APPLICATION_JSON)
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
	public void testAddCoverToAlbum() throws Exception {
		throw new RuntimeException("not yet implemented");
	}
	
	@Test
	public void testAddCoverToAlbumThrowMediaNotFoundException() throws Exception {
		throw new RuntimeException("not yet implemented");
	}
	
	@Test
	public void testAddCoverToAlbumThrowAlbumNotFoundException() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

}
