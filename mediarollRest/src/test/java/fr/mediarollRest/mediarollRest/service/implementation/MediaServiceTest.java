package fr.mediarollRest.mediarollRest.service.implementation;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.model.Picture;
import fr.mediarollRest.mediarollRest.model.User;
import fr.mediarollRest.mediarollRest.repository.MediaRepository;

@RunWith(MockitoJUnitRunner.class)
public class MediaServiceTest {

	@Mock
	private MediaRepository mediaRepository;
	
	@InjectMocks
	private MediaService mediaService;
	
	private User u1;
	private Picture p1;
	
	//private Movie m1;
	//private Album a1;
	
	@Before
	public void init() {
		u1 = new User("corentin@mediaroll.xyz","cor","Corentin","Mapella");
		p1 = new Picture(1,"Pic01","PicTest","25/10/2017",u1);
		
		//m1 = new Movie(1,"Mov1","MovieTest","25/10/2017",u1,"3:15");
		//a1 = new Album(1,"Alb1","AlbTest","25/10/2017",u1);
	}
	
	@Test
	public void testSaveMedia()  throws Exception {
		//WHEN
		when(mediaRepository.save(any(Picture.class))).thenReturn(p1);
		
		//when(mediaRepository.save(any(Movie.class))).thenReturn(m1);
		//when(mediaRepository.save(any(Album.class))).thenReturn(a1);
		
		//GIVEN
		mediaService.saveMedia(any(Picture.class));
		
		//mediaService.saveMedia(any(Movie.class));
		//mediaService.saveMedia(any(Album.class));
		
		//THEN
		verify(mediaRepository).save(any(Picture.class));
		
		//verify(mediaRepository).save(any(Movie.class));
		//verify(mediaRepository).save(any(Album.class));
	}

	@Test
	public void testDeleteMedia()  throws Exception {
		//WHEN
		doNothing().when(mediaRepository).delete(any(Picture.class));
		
		//doNothing().when(mediaRepository).delete(any(Movie.class));
		//doNothing().when(mediaRepository).delete(any(Album.class));
		
		//GIVEN
		mediaService.deleteMedia(any(Picture.class));
		
		//mediaService.deleteMedia(any(Movie.class));
		//mediaService.deleteMedia(any(Album.class));
				
		//THEN
		verify(mediaRepository).delete(any(Picture.class));
		
		//verify(mediaRepository).delete(any(Movie.class));
		//verify(mediaRepository).delete(any(Album.class));
	}
	
	@Test
	public void testFindByName()  throws Exception {
		// WHEN
		when(mediaRepository.findByName("Pic01")).thenReturn(p1);

		// GIVEN
		mediaService.findByName("Pic01");

		// THEN
		verify(mediaRepository).findByName("Pic01");
	}

	@Test
	public void testFindAll()  throws Exception {
		ArrayList<Media> mediaList = new ArrayList<Media>();
		
		// WHEN
		when(mediaRepository.findAll()).thenReturn(mediaList);

		// GIVEN
		mediaService.findAll();

		// THEN
		verify(mediaRepository).findAll();
	}
}
