package fr.mediarollRest.mediarollRest.web.resource;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.service.implementation.MediaService;

@RunWith(MockitoJUnitRunner.class)
public class MediaResourceTest {

	@Mock
	private MediaResource mediaRessource;
	
	@InjectMocks
	private MediaService mediaService;
	
	/**
	@Test
	public void testFindAllMedia() throws Exception {
		List<Media> mediaList = new ArrayList<Media>();
		
		// WHEN
		when(mediaService.findAll()).thenReturn(mediaList);

		// GIVEN
		mediaRessource.findAllMedia();

		// THEN
		verify(mediaService).findAll();
	}
	*/
}
