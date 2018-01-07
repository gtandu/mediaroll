# Bienvenue sur MediaRoll

![MediaRoll Logo](https://github.com/gtandu/mediaroll/blob/master/mediarollClient/src/assets/img/icon.png)

MediaRoll est une plateforme de sauvegarde et de partage de photos/vidéos à distance.
Comme Google Photos, il résulte d'un besoin de partager des photos entre amis.

##  Les technologies

### Back

* [Spring MVC](https://spring.io/)
* [Maven](https://maven.apache.org/)
* [MySQL](https://www.mysql.com/fr/)

### Front
* [Angular CLI](https://cli.angular.io/)
* [MaterializeCss](http://materializecss.com/)
* [JQuery](https://jquery.com/)
* [SweetAlert](http://t4t5.github.io/sweetalert/)

## Installation

### Configuration de l'API REST

* [Java JDK Version 1.8](http://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html)
* [Eclipse Java EE](http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/keplersr2)
	* Installer le plugin Spring Tools disponible sur le Markelplace d'eclipse.
* Clonez le projet dans votre entrepôt :

		git clone https://github.com/gtandu/mediaroll.git
* Dans votre workspace d'Eclipse : **Import -> Maven -> Existing Maven Projects**
* Cliquez droit sur votre projet : **Run as -> Maven Install** pour télécharger les libs.

### Test de l'API Rest

* Vous pouvez tester l'api sans deployer le client Angular via l'application [Postman](https://www.getpostman.com/)
* Vous pouvez importer les requetes :  [Requetes Postman](https://www.getpostman.com/collections/bb595da6078e2c677d69)
* **IMPORTANT TOUTES LES REQUETES NECESSITE UN TOKEN VALIDE**. Il faut donc le generer en premier et l'inclure dans le header de chaque requete. 

### Base de données

* Créer une base de données MySQL :
	* **Name** :  mediaroll
	* **Port** : 3306
	* **Username** : root
	* **Password** : root

* Vous pouvez adapter votre base de données (Adresse, port, nom, mot de passe) dans **src/main/resources/application-prod.properties**

		spring.datasource.url = jdbc:mysql://localhost:3306/mediaroll
		spring.datasource.username = root
		spring.datasource.password = root	
			
				
### Démarrer le serveur
* Sélectionnez votre projet
*  **Cliquez droit -> Run as -> Run Configurations**
*  Créer une nouvelle configuration
	* Main type : fr.mediarollRest.mediarollRest.MediarollRestApplication
	* Profile : dev (Si vous souhaitez utilisez la base de données mémoire)
	* Profile : prod (Pour utiliser votre propre base de données configuré au préalable.)
	* Dans l'onglets arguments, ajouter les VM arguments suivants :
		* **-Dmedias.folder=../medias**
		* **-Ddummy=-noverify**
	* -**Dmedias.folder=../medias** permet de spécifier le chemin de sauvegarde vos médias.
	* Terminer par **"Apply"** ensuite **"Run"**. 
*  Lorsque vous ouvrez la console, vous devriez voir  :

		2017-06-27 13:24:50 INFO  o.s.j.e.a.AnnotationMBeanExporter - Registering beans for JMX exposure on startup
		2017-06-27 13:24:50 INFO  o.s.b.c.e.t.TomcatEmbeddedServletContainer - Tomcat started on port(s): 8080 (http)
		2017-06-27 13:24:50 INFO  fr.diptrack.DiptrackApplication - Started DiptrackApplication in 14.631 seconds (JVM running for 16.048)
			
*  Votre serveur est lancé

### Configuration du Front

* [Installer Node.js](https://nodejs.org/en/)
* [Installer Angular CLI](https://cli.angular.io/)
* Lancez la commande **npm install** dans le répertoire **mediaRollClient**
* Puis saisissez la commande suivante pour demarrer le client :
**`ng serve`** 
* Vous devriez voir les informations suivantes :

	`webpack: Compiled successfully.`
      
* Votre serveur est lancé.
* Ouvrez votre navigateur et allez à l'adresse suivante : [http://localhost:4200/login](http://localhost:4200/login)
* Ouvrez le fichier suivant "**authentification.service.ts**" dans le dossier **mediaroll/mediarollClient/src/app/services/**
* Modifier l'adresse du serveur pour qu'elle pointe vers votre API REST

		constructor(private http: Http) {
			/../
        	this.server = 'http://localhost:8080';
        }

### Compte Utilisateur

**Compte n°1 (Démo)**

	User : demo@mediaroll.xyz
	Password : projet

**Compte n°2**

	User : glodie@mediaroll.xyz
	Password : projet

**Compte n°3**

	User : corentin@mediaroll.xyz
	Password : projet



