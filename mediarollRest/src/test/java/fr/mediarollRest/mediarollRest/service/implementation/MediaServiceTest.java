package fr.mediarollRest.mediarollRest.service.implementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.mediarollRest.mediarollRest.exception.MediaNotFoundException;
import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.model.Picture;
import fr.mediarollRest.mediarollRest.repository.MediaRepository;

@RunWith(MockitoJUnitRunner.class)
public class MediaServiceTest {

	@Mock
	private MediaRepository mediaRepository;

	@InjectMocks
	private MediaService mediaService;

	@Test
	public void testDeleteMediaByIdSuccess() throws Exception {
		// WHEN
		Long id = 1L;
		when(mediaRepository.deleteById(eq(id))).thenReturn(1);

		// GIVEN
		mediaService.deleteMediaById(id);

		// THEN
		verify(mediaRepository).deleteById(eq(id));

	}

	@Test
	public void testDeleteMediaByIdFailed() throws Exception {
		// WHEN
		Long id = 1L;
		when(mediaRepository.deleteById(eq(id))).thenReturn(0);

		// GIVEN
		mediaService.deleteMediaById(id);

		// THEN
		verify(mediaRepository).deleteById(eq(id));

	}

	@Test
	public void testFindByIdFound() throws Exception {
		Long id = 1L;
		when(mediaRepository.findById(anyLong())).thenReturn(Optional.of(new Picture()));

		Media media = mediaService.findById(id);

		assertThat(media).isNotNull();

		verify(mediaRepository).findById(eq(id));
	}

	@Test(expected = MediaNotFoundException.class)
	public void testFindByIdNotFound() throws Exception {
		Long id = 1L;
		when(mediaRepository.findById(anyLong())).thenReturn(Optional.empty());

		mediaService.findById(id);

		verify(mediaRepository).findById(eq(id));
	}

	@Test
	public void testUpdateMediaInfo() throws Exception {
		Long mediaId = 1L;
		Media mediaWithNewInfo = new Picture();
		Picture mediaToUpdate =new Picture();
		mediaWithNewInfo.setDescription("test description");

		when(mediaRepository.findById(anyLong())).thenReturn(Optional.of(mediaToUpdate));
		when(mediaRepository.save(any(Media.class))).thenReturn(mediaToUpdate);
		
		Media mediaUpdated = mediaService.updateMediaInfo(mediaId, mediaWithNewInfo);
		
		assertThat(mediaUpdated).isNotNull();
		assertThat(mediaUpdated.getDescription()).isEqualTo(mediaWithNewInfo.getDescription());
		
		verify(mediaRepository).findById(eq(mediaId));
		verify(mediaRepository).save(any(Media.class));
	}
	
	@Test(expected=MediaNotFoundException.class)
	public void testUpdateMediaInfoThrowMediaNotFoundException() throws Exception {
		Long mediaId = 1L;
		Media media = new Picture();
		media.setDescription("test description");

		when(mediaRepository.findById(anyLong())).thenReturn(Optional.empty());
		
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
}
