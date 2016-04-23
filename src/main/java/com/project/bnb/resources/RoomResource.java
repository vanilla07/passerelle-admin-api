package com.project.bnb.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.IntParam;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Optional;
import com.project.bnb.core.Reservation;
import com.project.bnb.core.Room;
import com.project.bnb.core.RoomEnum;
import com.project.bnb.core.VacationRooms;
import com.project.bnb.core.Vacation;
import com.project.bnb.db.ReservationDAO;
import com.project.bnb.db.VacationRoomsDAO;
import com.project.bnb.db.VacationDAO;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
public class RoomResource {

	private List<Room> rooms;
	private ReservationDAO reservationDAO;
	private VacationRoomsDAO vacationRoomsDAO;
	private VacationDAO vacationDAO;

	public RoomResource(ReservationDAO reservationDAO, VacationRoomsDAO vacationRoomsDAO, VacationDAO vacationDAO) {
		super();
		initRooms();
		this.reservationDAO = reservationDAO;
		this.vacationRoomsDAO = vacationRoomsDAO;
		this.vacationDAO = vacationDAO;
	}

	@GET
	@UnitOfWork
	public List<Room> getJsonRooms(@QueryParam("date") Optional<Date> date) {
		if (date.isPresent()) {
			for (Room r : rooms) {
				List<Vacation> vacDatas = getVacationDatas(r);
				fillRooms(r, reservationDAO.findByRoomByDate(r.getId(), date.get()), 
						vacDatas);
			}
		}
		return this.rooms;
	}

	@GET
	@Path("/{id}")
	@UnitOfWork
	public Room getJsonRoomsById(
			@PathParam("id") IntParam roomId,
			@QueryParam("date") Optional<Date> date
			) {
		if (date.isPresent()) {
			for (Room r : rooms) {
				if ( r.getId() == roomId.get()) {
					List<Vacation> vacDatas = getVacationDatas(r);
					fillRooms(r, reservationDAO.findByRoomByDate(r.getId(), date.get()), 
								vacDatas);
					return r;   
				}
			}
		}
		// TODO : la chambre n'existe pas : remvoyer un message d'erreur
		return null;
	}
	
	private List<Vacation> getVacationDatas(Room r) {
		List<Vacation> vacDatas = new ArrayList<Vacation>();
		List<Vacation> vacs = vacationDAO.findAll();
		for (Vacation vac : vacs) {
			for (VacationRooms v : vac.getRooms() ) {
				if (v.getRoom() == r.getId()) {
					vacDatas.add(vac);
					break;
				}
			}
		}
		return vacDatas;
	}
	
	private void initRooms() {
		rooms = new ArrayList<Room>();
		for (RoomEnum r : RoomEnum.values()) {
			rooms.add(new Room(r));
		}
	}
	
	private void fillRooms(Room room, List<Reservation> reservations, List<Vacation> vacations) {
		// empty the lists
		room.cleanDates();
		// add reservations
		for (Reservation res : reservations) {
			room.addBooking(res);					
		}
		//add vacations	
		for (Vacation vac : vacations) {
			room.addVacation(vac);
		}
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public ReservationDAO getReservationDAO() {
		return reservationDAO;
	}

	public void setReservationDAO(ReservationDAO reservationDAO) {
		this.reservationDAO = reservationDAO;
	}

	public VacationRoomsDAO getVacationDAO() {
		return vacationRoomsDAO;
	}

	public void setVacationDAO(VacationRoomsDAO vacationRoomsDAO) {
		this.vacationRoomsDAO = vacationRoomsDAO;
	}

	public VacationDAO getVacationDataDAO() {
		return vacationDAO;
	}

	public void setVacationDataDAO(VacationDAO vacationDAO) {
		this.vacationDAO = vacationDAO;
	}

}
