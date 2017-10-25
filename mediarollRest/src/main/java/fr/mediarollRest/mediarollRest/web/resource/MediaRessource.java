package fr.mediarollRest.mediarollRest.web.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.mediarollRest.mediarollRest.model.Media;
import fr.mediarollRest.mediarollRest.service.implementation.MediaService;

@RestController
@RequestMapping("")
public class MediaRessource {

	@Autowired
	private MediaService mediaService;
	
	@RequestMapping(value = "/medias", 
				    produces={"application/json"},
				    method = RequestMethod.GET)
	@ResponseBody
	public List<Media> findAllMedia() {
		return mediaService.findAll();
	}
	
	@RequestMapping(value = "/medias/name={name}", 
					produces={"text/html"},
					method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteMediaByName(@PathVariable("name") String name) {
		
		return "Vous avez supprimé le media portant le nom de : "+name;
	}
}
