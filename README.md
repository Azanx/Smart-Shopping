# Smart-Shopping
## About
Simple Shopping List app to help with planning shopping and managing resulting shopping list. Supports separate lists per user<br/>
Developed in Java8 and Spring Framework 4 (without Boot) with JPA / Hibernate DB support. Web frontend built with JSP and Bootstrap. Backing database used: MariaDB during deployment, H2 for testing.

## Deployment
Below instructions will help you get a copy of the project and deploy it on the server

### Prerequisites
For building:
* Java SDK 8
* Maven

For deployment:
* JRE 8 
* Tomcat 9
* MariaDB / MySQL database (I use MariaDB, but they're compatible)

### Configure
Configuration files can be found in src/main/resources<br/>
You can configure database server and hibernate settings in the db.properties file.<br/>
Remember to change db.username and db.password. Another important thing to remember is to set correct address in db.url (in dev settings inside the repository it's set to localhost)<br/>
You might need also to change serverTimezone parameter in db.url to be the same as your server timezone (it was set to fix bug related to european summer time, if there is no summer time in your server timezone you can remove it altoghether)<br/>
<br>
Logger settings can be changed in log4j.properties file.

### Building
1.Clone the repository:<br/>
```git clone git@github.com:Azanx/Smart-Shopping.git```<br/>
2.Go inside the main directory of cloned repository:<br/>
```cd Smart-Shopping```<br/>
3. Build and package the application<br/>
```mvn package```<br/>
Maven will compile the project and run automatic tests of main application components. You may see some log output, mainly INFO and DEBUG messages. Don't get scared by WARN logs regarding 'admin' account - they're normal during testing phase. Yo don't have to have database configured on the machine yet, as during test phase app is using H2 in-memory database.
4. Go into the "target" directory inside the main directory. You will find in there file named like: "shopping_list-x-x-x-SNAPSHOT.war". You will have to copy this file into the webapp folder of your tomcat instalation.

### Deploying
#### Configure database
Firstly you need to configure 'shopping_list' database and create user for database connection.
1. To create new database you have to start mariaDB/mySQL command line. If you haven't configured any administrative user account in the db, you can use root account without password (but it's recommended to set it for security reasons)<br/>
```mysql -u root```
2. Create 'shopping_list' database<br/>
```CREATE DATABASE shopping_list default character set utf8 default collate utf8_bin;```<br/>
3. Create database user for use by the application. User name and password can be configured in: `shopping_list/src/main/resources/db.properties` file.<br/>
```GRANT ALL PRIVILEGES ON shopping_list.* to devuser@'localhost' IDENTIFIED BY 'devuser'```<br/>
4. You don't have to create the database schema, as it will be created automatically during app deployment. WARNING - with default settings present in the repository all data in the database will be dropped during every application startup! If you don't want this behaviour you have to change value of `hibernate.hbm2ddl.auto` property inside the `db.properties` file!<br/>
#### Start the app
1. From inside the project directory, copy the "shopping_list-x-x-x-SNAPSHOT.war" into the machine running tomcat. If you are on linux and tomcat is on remote machine, you might use scp:<br/>
```scp shopping_list-x-x-x-SNAPSHOT.war server_ip:```<br/>
2. Log into the machine with tomcat installed, and copy the .war file into `TOMCAT_DIR/webapps` directory<br/>

After a short while you're war file should be extracted and you should be able to access the application from your webbrowser under: `server_ip:8080/shopping_list-x-x-x-SNAPSHOT`

## Author
* Kamil Piwowarski

## License
Licensed under the MIT License - see the [LICENSE file](https://github.com/Azanx/Smart-Shopping/blob/master/LICENSE)
