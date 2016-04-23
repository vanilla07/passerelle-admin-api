package com.project.bnb.db;

import java.util.List;

import org.hibernate.SessionFactory;

import com.project.bnb.core.VacationDate;

public class VacationDateDAO extends GenericDAO<VacationDate> {

    public VacationDateDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<VacationDate> findAll() {
        return list(namedQuery("com.passerelle.admin.core.VacationDate.findAll"));
    }
    
    public List<VacationDate> findByRoom(int roomId) {
        return list(namedQuery("com.passerelle.admin.core.VacationDate.findByRoom")
                .setParameter("room", roomId)
        );
    }
    
    public List<VacationDate> findByVacation(long vacId) {
        return list(namedQuery("com.passerelle.admin.core.VacationDate.findByVacation")
                .setParameter("vacation", vacId)
        );
    }
}
