package com.project.bnb;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import com.project.bnb.core.OccupiedDate;
import com.project.bnb.core.Reservation;
import com.project.bnb.core.VacationRooms;
import com.project.bnb.core.Vacation;
import com.project.bnb.core.VacationDate;
import com.project.bnb.db.OccupiedDateDAO;
import com.project.bnb.db.ReservationDAO;
import com.project.bnb.db.VacationRoomsDAO;
import com.project.bnb.db.VacationDAO;
import com.project.bnb.db.VacationDateDAO;
import com.project.bnb.resources.CalendarResource;
import com.project.bnb.resources.ReservationsResource;
import com.project.bnb.resources.RoomResource;
import com.project.bnb.resources.VacationResource;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ProjectBnbApplication extends Application<ProjectBnbConfiguration> {
    
	public static void main(String[] args) throws Exception {
        new ProjectBnbApplication().run(args);
    }

    private final HibernateBundle<ProjectBnbConfiguration> hibernateBundle =
            new HibernateBundle<ProjectBnbConfiguration>(Reservation.class, OccupiedDate.class, VacationRooms.class, VacationDate.class, Vacation.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(ProjectBnbConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public String getName() {
        return "Project BnB";
    }

    @Override
    public void initialize(Bootstrap<ProjectBnbConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        bootstrap.addBundle(new MigrationsBundle<ProjectBnbConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(ProjectBnbConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(ProjectBnbConfiguration configuration, Environment environment) {
    	// reservations
    	final ReservationDAO reservationDAO = new ReservationDAO(hibernateBundle.getSessionFactory());
    	final OccupiedDateDAO occupiedDateDAO = new OccupiedDateDAO(hibernateBundle.getSessionFactory());
    	
    	// closing dates
    	final VacationRoomsDAO vacationRoomsDAO = new VacationRoomsDAO(hibernateBundle.getSessionFactory());
    	final VacationDateDAO vacationDateDAO = new VacationDateDAO(hibernateBundle.getSessionFactory());
    	final VacationDAO vacationDAO = new VacationDAO(hibernateBundle.getSessionFactory());
    	
    	environment.jersey().register(new CalendarResource(occupiedDateDAO, vacationDateDAO, reservationDAO, vacationDAO));
        environment.jersey().register(new ReservationsResource(reservationDAO, occupiedDateDAO));
        environment.jersey().register(new VacationResource(vacationRoomsDAO, vacationDateDAO, vacationDAO));
        environment.jersey().register(new RoomResource(reservationDAO, vacationRoomsDAO, vacationDAO));
        
        // Enable CORS headers
        final FilterRegistration.Dynamic cors =
            environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }
}
