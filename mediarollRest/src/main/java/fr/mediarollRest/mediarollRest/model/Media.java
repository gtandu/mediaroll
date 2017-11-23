package fr.mediarollRest.mediarollRest.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@Inheritance
public abstract class Media {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String name;
	private String description;
	private String importDate;
	private String filePath;
	private boolean privateMedia;

	@ManyToOne(cascade= {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	private Account owner;

	@OneToMany(cascade= {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	private List<Account> sharedPeople;

	public Media() {
		this.name ="";
		this.description = "";
		this.importDate = "";
		this.filePath = "";
		this.privateMedia = true;
		this.sharedPeople = new ArrayList<Account>();
	}
	
	public Media(String mediaName, String mediaPath, String uploadDate) {
		this.name = mediaName;
		this.description="";
		this.filePath = mediaPath;
		this.importDate = uploadDate;
		this.privateMedia = true;
		this.sharedPeople = new ArrayList<Account>();
	}

	public Media(int id, String name, String description, String importDate, String filePath, boolean privateMedia,
			Account owner, List<Account> sharedPeople) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.importDate = importDate;
		this.filePath = filePath;
		this.privateMedia = privateMedia;
		this.owner = owner;
		this.sharedPeople = sharedPeople;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public String getFilePath() {
		return filePath;
	}

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

	
}
