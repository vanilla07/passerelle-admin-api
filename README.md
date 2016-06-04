# Introduction

A REST API to manage bookings for La Passerelle.

This API is used here : http://jchevalier.pro/projects/bnb

and linked to this angular project : http://github.com/vanilla07/passerelle-admin

* Backend : Java / Dropwizard / Hibernate

* Embedded H2 database

# Running The Application

To run the application run the following commands.

* To package the example run (Maven and Java 8 required).

        mvn package

* To setup the h2 database run.

		java -jar target/project-bnb-1.0.0-SNAPSHOT.jar db migrate config.yml

* To run the server run.

        java -jar target/project-bnb-1.0.0-SNAPSHOT.jar server config.yml

* To run in debug mode.
	
		java -Xdebug -agentlib:jdwp=transport=dt_socket,address=9999,server=y,suspend=n -jar target/project-bnb-1.0.0-SNAPSHOT.jar server config.yml

# Test The Application without Java 8 compilation :

* Download and unzip compiled-files.zip

* In dropwizard-logs folder, run the command (Java 8 required) :
 		
		java -jar target/project-bnb-1.0.0-SNAPSHOT.jar server config.yml

# Test the API

When the server is running, the API is available here : http://0.0.0.0:8091.
Use the following urls when testing the API.

		GET     /bookings (com.project.bnb.resources.ReservationsResource)
	    POST    /bookings (com.project.bnb.resources.ReservationsResource)
	    DELETE  /bookings/{id} (com.project.bnb.resources.ReservationsResource)
	    GET     /bookings/{id} (com.project.bnb.resources.ReservationsResource)
	    PUT     /bookings/{id} (com.project.bnb.resources.ReservationsResource)
	    GET     /calendar/{room_id} (com.project.bnb.resources.CalendarResource)
	    GET     /rooms (com.project.bnb.resources.RoomResource)
	    GET     /rooms/{id} (com.project.bnb.resources.RoomResource)
	    GET     /vacation (com.project.bnb.resources.VacationResource)
	    POST    /vacation (com.project.bnb.resources.VacationResource)
	    GET     /vacation/dates/all (com.project.bnb.resources.VacationResource)
	    GET     /vacation/room/{id} (com.project.bnb.resources.VacationResource)
	    DELETE  /vacation/{id} (com.project.bnb.resources.VacationResource)
	    GET     /vacation/{id} (com.project.bnb.resources.VacationResource)
	    PUT     /vacation/{id} (com.project.bnb.resources.VacationResource)
	    GET     /vacation/{id}/dates (com.project.bnb.resources.VacationResource)



