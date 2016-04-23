package com.project.bnb.db;

import java.util.List;

import org.hibernate.SessionFactory;

import com.project.bnb.core.VacationRooms;


public class VacationRoomsDAO extends GenericDAO<VacationRooms> {

    public VacationRoomsDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<VacationRooms> findAll() {
        return list(namedQuery("com.passerelle.admin.core.VacationRooms.findAll"));
    }
    
    public List<VacationRooms> findByRoom(int roomId) {
        return list(namedQuery("com.passerelle.admin.core.VacationRooms.findByRoom")
                .setParameter("room", roomId)
        );
    }
}
