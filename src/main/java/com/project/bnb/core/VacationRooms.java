package com.project.bnb.core;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "vacation_rooms")
@NamedQueries({
	// All vacations
    @NamedQuery(name = "com.passerelle.admin.core.VacationRooms.findAll",
            query = "select e from VacationRooms e"),
    // All vacations for this room
    @NamedQuery(name = "com.passerelle.admin.core.VacationRooms.findByRoom",
            query = "select e from VacationRooms e "
            + "where e.room = :room ")
})
public class VacationRooms implements AbstractModel {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
 
    private int room;

	public VacationRooms() {
    }
	
	public int getRoom() {
		return room;
	}

	public void setRoom(int room) {
		this.room = room;
	}
	
	public long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + room;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VacationRooms other = (VacationRooms) obj;
		if (id != other.id)
			return false;
		if (room != other.room)
			return false;
		return true;
	}
}
