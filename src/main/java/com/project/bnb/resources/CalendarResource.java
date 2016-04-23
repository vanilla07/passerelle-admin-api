package com.project.bnb.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.IntParam;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Optional;
import com.project.bnb.core.CalendarDate;
import com.project.bnb.core.OccupiedDate;
import com.project.bnb.core.Reservation;
import com.project.bnb.core.Vacation;
import com.project.bnb.core.VacationDate;
import com.project.bnb.db.OccupiedDateDAO;
import com.project.bnb.db.ReservationDAO;
import com.project.bnb.db.VacationDAO;
import com.project.bnb.db.VacationDateDAO;

@Path("/calendar")
@Produces(MediaType.APPLICATION_JSON)
public class CalendarResource {

	private OccupiedDateDAO occupiedDateDAO;
	private VacationDateDAO vacationDateDAO;
	private VacationDAO vacationDAO;
	private ReservationDAO reservationDAO;

	public CalendarResource(OccupiedDateDAO occupiedDateDAO,
			VacationDateDAO vacationDateDAO, ReservationDAO reservationDAO,
			VacationDAO vacationDAO) {
		this.occupiedDateDAO = occupiedDateDAO;
		this.vacationDateDAO = vacationDateDAO;
		this.reservationDAO = reservationDAO;
		this.vacationDAO = vacationDAO;
	}

	public CalendarResource() {
		super();
	}

	public List<CalendarDate> generateCalendar(List<OccupiedDate> reservationDates, List<VacationDate> vacationDates){
		List<CalendarDate> calendar = new ArrayList<CalendarDate>();
		for (OccupiedDate date : reservationDates){
			CalendarDate cal = new CalendarDate(date.getDate().toString(), true);
			Optional<Reservation> findById = reservationDAO.findById(date.getIdReservation());
			if (findById.isPresent()) {
				cal.setReservation(findById.get());
				calendar.add(cal);
			}
		}
		for (VacationDate date : vacationDates){
			CalendarDate cal = new CalendarDate(date.getDate().toString(), false);
			Optional<Vacation> findVacById = vacationDAO.findById(date.getIdVacation());
			if (findVacById.isPresent()) {
				Vacation vacData = findVacById.get();
				cal.setVacation(vacData);
				calendar.add(cal);
			}
		}
		return calendar;
	}
	
	@GET
	@Path("/{room_id}")
	@UnitOfWork
	public List<CalendarDate> getJsonCalendar(@PathParam("room_id") IntParam roomId) {
        return generateCalendar(occupiedDateDAO.findByRoom(roomId.get()), vacationDateDAO.findByRoom(roomId.get()));		
	}

	public OccupiedDateDAO getOccupiedDateDAO() {
		return occupiedDateDAO;
	}

	public void setOccupiedDateDAO(OccupiedDateDAO occupiedDateDAO) {
		this.occupiedDateDAO = occupiedDateDAO;
	}

	public VacationDateDAO getVacationDateDAO() {
		return vacationDateDAO;
	}

	public void setVacationDateDAO(VacationDateDAO vacationDateDAO) {
		this.vacationDateDAO = vacationDateDAO;
	}

	public VacationDAO getVacationDAO() {
		return vacationDAO;
	}

	public void setVacationDAO(VacationDAO vacationDAO) {
		this.vacationDAO = vacationDAO;
	}

	public ReservationDAO getReservationDAO() {
		return reservationDAO;
	}

	public void setReservationDAO(ReservationDAO reservationDAO) {
		this.reservationDAO = reservationDAO;
	}
	
}
