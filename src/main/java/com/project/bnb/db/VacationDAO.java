package com.project.bnb.db;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.hibernate.SessionFactory;

import com.project.bnb.core.Vacation;

public class VacationDAO extends GenericDAO<Vacation> {

    public VacationDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public List<Vacation> findAll() {
        return list(namedQuery("com.passerelle.admin.core.Vacation.findAll"));
    }
    
    public List<Vacation> findByDate(Date date) {
    	Calendar c = Calendar.getInstance(); 
    	c.setTime(date); 
    	c.add(Calendar.DATE, 30);
    	Date dateEnd = new Date(c.getTimeInMillis());
        return list(
                namedQuery("com.passerelle.admin.core.Vacation.findByDate")
                .setParameter("date", date)
                .setParameter("dateend", dateEnd)
        );
    }
    public List<Vacation> findByName(String name) {
        StringBuilder builder = new StringBuilder("%");
        builder.append(name).append("%");
        return list(
                namedQuery("com.passerelle.admin.core.Vacation.findByName")
                .setParameter("name", builder.toString())
        );
    }
    
    public List<Vacation> findAllByDatesOff(Date dateStart, Date dateEnd) {
    	return list(namedQuery("com.passerelle.admin.core.Vacation.findAllByDatesOff")
    			.setParameter("datestart", dateStart)
    			.setParameter("dateend", dateEnd)    			
    			);
    }

    public List<Vacation> findAllByDate(Date date) {
    	return list(namedQuery("com.passerelle.admin.core.Vacation.findAllByDate")
    			.setParameter("date", date)    	
    	);
    }
    
    public List<Vacation> findByRoom(int room) {
    	return list(namedQuery("com.passerelle.admin.core.Vacation.findAllByRoom")
    			.setParameter("idRoom", room)
    			);
    }
	
}
