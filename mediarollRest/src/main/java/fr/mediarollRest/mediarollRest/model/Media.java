package fr.mediarollRest.mediarollRest.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.hateoas.Link;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(name = "picture", value = Picture.class), @Type(name = "video", value = Video.class), })
@Entity
@Inheritance
public abstract class Media {

	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy = "uuid")
	private String id;
	private String name;
	private String description;
	private String importDate;
	private String filePath;
	private String keyS3;
	private boolean privateMedia;

	@JsonInclude
	@Transient
	private List<Link> links;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	private Account owner;

	@OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private List<Account> sharedPeople;

	public Media() {
		this.name = "";
		this.description = "";
		this.importDate = "";
		this.filePath = "";
		this.privateMedia = true;
		this.sharedPeople = new ArrayList<Account>();
		this.links = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImportDate() {
		return importDate;
	}

	public void setImportDate(String importDate) {
		this.importDate = importDate;
	}
	
	public String getKeyS3() {
		return keyS3;
	}

	public void setKeyS3(String keyS3) {
		this.keyS3 = keyS3;
	}

	@JsonIgnore
	public String getFilePath() {
		return filePath;
	}

	@JsonProperty
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public boolean isPrivateMedia() {
		return privateMedia;
	}

	public void setPrivateMedia(boolean privateMedia) {
		this.privateMedia = privateMedia;
	}

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
	}

	public List<Account> getSharedPeople() {
		return sharedPeople;
	}

	public void setSharedPeople(List<Account> sharedPeople) {
		this.sharedPeople = sharedPeople;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void add(Link link) {
		Assert.notNull(link, "Link must not be null!");
		this.links.add(link);
	}

}
