package fr.mediarollRest.mediarollRest.service.implementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.mediarollRest.mediarollRest.exception.AlbumNotFoundException;
import fr.mediarollRest.mediarollRest.model.Album;
import fr.mediarollRest.mediarollRest.repository.AlbumRepository;

@RunWith(MockitoJUnitRunner.class)
public class AlbumServiceTest {
	@Mock
	private AlbumRepository albumRepository;
	@InjectMocks
	private AlbumService albumService;

	@Test
	public void testSaveAlbum() throws Exception {
		
		Album album = new Album();
		when(albumRepository.save(any(Album.class))).thenReturn(new Album());
		
		albumService.saveAlbum(album);
		
		verify(albumRepository).save(any(Album.class));
	}

	@Test
	public void testUpdateAlbum() throws Exception {
		Album albumWithNewInfo = new Album();
		albumWithNewInfo.setName("My album");
		Long albumId = 1L;
		
		Album albumToUpdate = new Album();
		
		when(albumRepository.findById(eq(albumId))).thenReturn(Optional.of(albumToUpdate));
		
		Album albumUpdated = albumService.updateAlbum(albumId, albumWithNewInfo);
		
		assertThat(albumUpdated).isNotNull();
		assertThat(albumUpdated.getName()).isEqualTo(albumWithNewInfo.getName());
		
		verify(albumRepository).findById(eq(albumId));
	}
	
	@Test(expected=AlbumNotFoundException.class)
	public void testUpdateAlbumThrow() throws Exception {
		Album album = new Album();
		Long albumId = 1L;
		
		when(albumRepository.findById(eq(albumId))).thenReturn(Optional.empty());
		
		albumService.updateAlbum(albumId, album);
		
		verify(albumRepository).findById(eq(albumId));
		
		
	}

	@Test
	public void testDeleteAlbumReturnTrue() throws Exception {
		
		Long albumId = 1L;
		
		when(albumRepository.findById(eq(albumId))).thenReturn(Optional.of(new Album()));
		when(albumRepository.deleteById(eq(albumId))).thenReturn(new Integer(1));
		
		boolean albumIsDeleted = albumService.deleteAlbum(albumId);
		
		assertThat(albumIsDeleted).isTrue();
		
		verify(albumRepository).findById(eq(albumId));
		verify(albumRepository).deleteById(eq(albumId));
	}
	
	@Test
	public void testDeleteAlbumReturnFalse() throws Exception {
		
		Long albumId = 1L;
		
		when(albumRepository.findById(eq(albumId))).thenReturn(Optional.of(new Album()));
		when(albumRepository.deleteById(eq(albumId))).thenReturn(new Integer(0));
		
		boolean albumIsDeleted = albumService.deleteAlbum(albumId);
		
		assertThat(albumIsDeleted).isFalse();
		
		verify(albumRepository).findById(eq(albumId));
		verify(albumRepository).deleteById(eq(albumId));
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=AlbumNotFoundException.class)
	public void testDeleteAlbumThrowAlbumNotFoundException() throws Exception {
		
		Long albumId = 1L;
		
		when(albumRepository.findById(eq(albumId))).thenReturn(Optional.empty());
		
		albumService.deleteAlbum(albumId);
		
		verify(albumRepository).findById(eq(albumId));
	}

	@Test
	public void testFindAlbumByMail() throws Exception {
		// WHEN

		Long albumId = 1L;
		
		when(albumRepository.findById(eq(albumId))).thenReturn(Optional.of(new Album()));
		

		// GIVEN
		albumService.findAlbumById(albumId);

		// THEN
		verify(albumRepository).findById(eq(albumId));

	}
	
	@Test(expected=AlbumNotFoundException.class)
	public void testFindAlbumByMailThrowAccountNotFoundException() throws Exception {
		// WHEN

		Long albumId = 1L;
		
		when(albumRepository.findById(eq(albumId))).thenReturn(Optional.empty());

		// GIVEN
		albumService.findAlbumById(albumId);

		// THEN
		verify(albumRepository).findById(eq(albumId));

	}

}
