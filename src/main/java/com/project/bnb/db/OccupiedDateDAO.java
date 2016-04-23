package com.project.bnb.db;

import java.util.List;

import org.hibernate.SessionFactory;

import com.project.bnb.core.OccupiedDate;

public class OccupiedDateDAO extends GenericDAO<OccupiedDate> {

    /**
     * Constructor.
     *
     * @param sessionFactory Hibernate session factory.
     */
    public OccupiedDateDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<OccupiedDate> findAll() {
        return list(namedQuery("com.passerelle.admin.core.OccupiedDate.findAll"));
    }
    
    public List<OccupiedDate> findByRoom(int roomId) {
        return list(namedQuery("com.passerelle.admin.core.OccupiedDate.findByRoom")
                .setParameter("room", roomId)
        );
    }
    
    public List<OccupiedDate> findByReservation(long resId) {
        return list(namedQuery("com.passerelle.admin.core.OccupiedDate.findByReservation")
                .setParameter("reservation", resId)
        );
    }
}
