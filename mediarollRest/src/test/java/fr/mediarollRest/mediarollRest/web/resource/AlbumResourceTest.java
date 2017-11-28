package fr.mediarollRest.mediarollRest.web.resource;

import static fr.mediarollRest.mediarollRest.constant.Paths.ALBUMS;
import static fr.mediarollRest.mediarollRest.constant.Paths.MAIL;
import static fr.mediarollRest.mediarollRest.constant.Paths.ALBUM_WITH_ID;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

@RunWith(SpringRunner.class)
@WebMvcTest(AlbumResource.class)
public class AlbumResourceTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountService accountService;

	@MockBean
	private AlbumService albumService;

	@InjectMocks
	private AlbumResource albumResource;

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	@WithMockUser
	public void testCreateAlbum() throws Exception {
		String mail = "test@hotmail.fr";
		
		when(accountService.findByMail(eq(mail))).thenReturn(new Account());
		when(albumService.saveAlbum(any(Album.class))).thenReturn(new Album());

		ResultActions result = mockMvc.perform(post(ALBUMS + MAIL, mail).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(new Album())));

		result.andExpect(status().isOk());
		
		verify(accountService).findByMail(eq(mail));
		verify(albumService).saveAlbum(any(Album.class));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@WithMockUser
	public void testCreateAlbumThrowMailNotFoundException() throws Exception {
		String mail = "test@hotmail.fr";
		
		when(accountService.findByMail(eq(mail))).thenThrow(MailNotFoundException.class);

		ResultActions result = mockMvc.perform(post(ALBUMS + MAIL, mail).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(new Album())));

		result.andExpect(status().isNotFound());
		
		verify(accountService).findByMail(eq(mail));
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

}
