package com.project.bnb.core;
import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "vacation")
@NamedQueries({
    @NamedQuery(name = "com.passerelle.admin.core.Vacation.findAll",
            query = "select e from Vacation e"),
	//All vacations starting after the date or including the date
	@NamedQuery(name = "com.passerelle.admin.core.Vacation.findAllByDate",
        query = "select e from Vacation e "
        		+ "where e.dateStart >= :date "
        		+ "or (dateStart < :date and dateEnd > :date) "
        		+ "order by dateStart asc"
		),
		//All vacations starting after the date or including the date
		@NamedQuery(name = "com.passerelle.admin.core.Vacation.findAllByRoom",
		query = "select e from Vacation e "
				+ "JOIN e.rooms r "
				+ "where r.room = :idRoom "
				+ "order by e.dateStart asc"
				),
	// All vacations with the exact dates start and end
    @NamedQuery(name = "com.passerelle.admin.core.Vacation.findAllByDatesOff",
            query = "select e from Vacation e "
            		+ "where e.dateStart = :datestart "
            		+ "and e.dateEnd = :dateend "
    		),
    // All vacations containing this words in the name
    @NamedQuery(name = "com.passerelle.admin.core.Vacation.findByName",
            query = "select e from Vacation e "
            + "where e.name like :name "),
    // All vacations starting after the date or including the date sorted and limited to a dateEnd
 	@NamedQuery(name = "com.passerelle.admin.core.Vacation.findByDate",
 	        query = "select e from Vacation e "
 	        		+ "where (e.dateStart between :date and :dateend "
	        		+ "or e.dateStart < :date and e.dateEnd > :date) "
	        		+ "order by e.dateStart asc"
 			)
	})
public class Vacation implements AbstractModel {

    /**
     * Entity's unique identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    /**
     * Starting date.
     */
    @Column(name = "date_start")
    private Date dateStart;
    /**
     * Ending date.
     */
    @Column(name = "date_end")
    private Date dateEnd;
    /**
     * Notes.
     */
    private String notes;
    
    @OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL},orphanRemoval=true)
	@JoinColumn(name="id_vacation", nullable=false)
    private List<VacationRooms> rooms;

	/**
     * A no-argument constructor.
     */
    public Vacation() {
    }

	public Vacation(String name, Date dateStart, Date dateEnd, String notes) {
		this.name = name;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.notes = notes;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

    public List<VacationRooms> getRooms() {
		return rooms;
	}

	public void setRooms(List<VacationRooms> rooms) {
		this.rooms = rooms;
	}
	
	public void addRoom(VacationRooms room){
	    this.rooms.add(room);
	}
	
	public void clearRooms() {
		this.rooms.clear();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateEnd == null) ? 0 : dateEnd.hashCode());
		result = prime * result
				+ ((dateStart == null) ? 0 : dateStart.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((rooms == null) ? 0 : rooms.hashCode());
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
		Vacation other = (Vacation) obj;
		if (dateEnd == null) {
			if (other.dateEnd != null)
				return false;
		} else if (!dateEnd.equals(other.dateEnd))
			return false;
		if (dateStart == null) {
			if (other.dateStart != null)
				return false;
		} else if (!dateStart.equals(other.dateStart))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (rooms == null) {
			if (other.rooms != null)
				return false;
		} else if (!rooms.equals(other.rooms))
			return false;
		return true;
	}

}


