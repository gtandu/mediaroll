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

### Configuration du Back

* [Java JDK Version 1.8](http://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html)
* [Eclipse Java EE](http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/keplersr2)
* Clonez le projet dans votre entrepôt :

		git clone https://github.com/gtandu/mediaroll.git
* Dans votre workspace d'Eclipse : **Import -> Maven -> Existing Maven Projects**
* Cliquez droit sur votre projet : **Run as -> Maven Install** pour télécharger les libs.

### Base de données

* Créer une base de donnée :
	* **Name** :  mediaroll
	* **Port** : 3306
	* **Username** : root
	* **Password** : root

* Vous pouvez adapter votre base de données (Adresse, port, nom, mot de passe) dans **src/main/resources/Application.properties**

		spring.datasource.url = jdbc:mysql://localhost:3306/mediaroll
		spring.datasource.username = root
		spring.datasource.password = root		
				
### Démarrer le serveur
* Sélectionnez votre projet
*  **Cliquez droit -> Run as -> Java Application**
*  Lorsque vous ouvrez la console, vous devriez voir  :

		2017-06-27 13:24:50 INFO  o.s.j.e.a.AnnotationMBeanExporter - Registering beans for JMX exposure on startup
		2017-06-27 13:24:50 INFO  o.s.b.c.e.t.TomcatEmbeddedServletContainer - Tomcat started on port(s): 8080 (http)
		2017-06-27 13:24:50 INFO  fr.diptrack.DiptrackApplication - Started DiptrackApplication in 14.631 seconds (JVM running for 16.048)
			
*  Votre serveur est lancé

### Configuration du Front

* Lancez la commande **npm install** dans le répertoire **mediaRollClient**
* Puis lancez **ng serve** 
* Vous devriez voir les informations suivantes :

      chunk {inline} inline.bundle.js (inline) 5.79 kB [entry] [rendered]
      chunk {main} main.bundle.js (main) 203 kB [initial] [rendered]
      chunk {polyfills} polyfills.bundle.js (polyfills) 562 kB [initial] [rendered]
      chunk {styles} styles.bundle.js (styles) 576 kB [initial] [rendered]
      chunk {vendor} vendor.bundle.js (vendor) 12.5 MB [initial] [rendered]

      webpack: Compiled successfully.
      
* Votre serveur est lancé
* Ouvrez votre navigateur et allez à l'adresse suivante : **http://localhost:4200/login**

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



