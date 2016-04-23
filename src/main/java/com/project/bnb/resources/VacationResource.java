package com.project.bnb.resources;

import java.sql.Date;
import java.util.List;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import io.dropwizard.jersey.params.IntParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.common.base.Optional;
import com.project.bnb.PasserelleUtils;
import com.project.bnb.core.VacationRooms;
import com.project.bnb.core.Vacation;
import com.project.bnb.core.VacationDate;
import com.project.bnb.db.VacationRoomsDAO;
import com.project.bnb.db.VacationDAO;
import com.project.bnb.db.VacationDateDAO;

@Path("/vacation")
@Produces(MediaType.APPLICATION_JSON)
public class VacationResource {

	private VacationRoomsDAO vacationRoomsDAO;
	private VacationDateDAO vacationDateDAO;
	private VacationDAO vacationDAO;

	public VacationRoomsDAO getVacationDAO() {
		return vacationRoomsDAO;
	}

	public void setVacationDAO(VacationRoomsDAO vacationRoomsDAO) {
		this.vacationRoomsDAO = vacationRoomsDAO;
	}

	public VacationDateDAO getVacationDateDAO() {
		return vacationDateDAO;
	}

	public void setVacationDateDAO(VacationDateDAO vacationDateDAO) {
		this.vacationDateDAO = vacationDateDAO;
	}

	public VacationDAO getVacationDataDAO() {
		return vacationDAO;
	}

	public void setVacationDataDAO(VacationDAO vacationDAO) {
		this.vacationDAO = vacationDAO;
	}

	public VacationResource(VacationRoomsDAO vacationRoomsDAO, VacationDateDAO vacationDateDAO, VacationDAO vacationDAO) {
		this.vacationRoomsDAO = vacationRoomsDAO;
		this.vacationDateDAO = vacationDateDAO;
		this.vacationDAO = vacationDAO;
	}

	@GET
	@UnitOfWork
	public List<Vacation> findByName(@QueryParam("name") Optional<String> name) {
		if (name.isPresent()) {
			return vacationDAO.findByName(name.get());
		} 
		else{
			return vacationDAO.findAll();
		}
	}
	
	@GET
	@UnitOfWork
	@Path("/room/{id}")
	public List<Vacation> findByRoom(@PathParam("id") IntParam id) {
		return vacationDAO.findByRoom(id.get().intValue());
	}
	
	@GET
	@UnitOfWork
	@Path("/{id}/dates")
	public List<VacationDate> findDatesById(@PathParam("id") LongParam id) {
		return vacationDateDAO.findByVacation(id.get().longValue());
	}
	
	@GET
	@UnitOfWork
	@Path("/dates/all")
	public List<VacationDate> findAllDates() {
		return vacationDateDAO.findAll();
	}

	@GET
	@Path("/{id}")
	@UnitOfWork
	public Optional<Vacation> findById(@PathParam("id") LongParam id) {
		return vacationDAO.findById(id.get());
	}
	
	@DELETE
	@Path("/{id}")
	@UnitOfWork
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") LongParam id) {
		Optional<Vacation> vac = vacationDAO.findById(id.get());
        if (!vac.isPresent()) {
            //throw new ResourceNotFoundException(new Throwable("Impossible to remove the vacation "+ id.get() + ": Resource not found."));
            return Response.status(Status.NOT_FOUND).build();
        }
        vacationDAO.delete(vac.get());
		return Response.ok().build();
	} 
	
	@POST
	@UnitOfWork
	@Consumes(MediaType.APPLICATION_JSON)
	public void addVacation(Vacation request) throws Exception {
		// TODO : tester la validité des dates
		// TODO : tester la présence des champs obligatoires	
		// tester VacationRooms null
		
		addOrUpdate(request);
	}
	
	@PUT
	@Path("/{id}")
	@UnitOfWork
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateVacation(Vacation request) throws Exception {
		
		// TODO : tester la validité des dates
		// TODO : tester la présence des champs obligatoires		
		
		addOrUpdate(request);
	}
	
	private void addOrUpdate(Vacation vacation) {
		List<Date> dates = PasserelleUtils.getVacationDates(vacation);
		
		Optional<Vacation> findById = vacationDAO.findById(vacation.getId());
		
		if (findById.isPresent()) {
			// Update tables of vacation dates and vacation rooms
			// Remove old vacation dates
			List<VacationDate> datesToRemove = vacationDateDAO.findByVacation(vacation.getId());
			if (null != datesToRemove && !datesToRemove.isEmpty()){
				vacationDateDAO.delete(datesToRemove);
			}
			Vacation vac = findById.get();
			vac.clearRooms();
			vacation.getRooms().forEach(o -> vac.addRoom(o));
			// update vacation
			vacationDAO.saveOrUpdate(vac);
		}
		else {
			// save new vacation
			vacationDAO.saveOrUpdate(vacation);
		}
		for (VacationRooms vacRoom : vacation.getRooms()) {
			int room = vacRoom.getRoom();
			// Add new vacation dates
			for (Date date : dates){
				vacationDateDAO.saveOrUpdate(new VacationDate(vacation.getId(), room, date));
			}
		}
	}
}