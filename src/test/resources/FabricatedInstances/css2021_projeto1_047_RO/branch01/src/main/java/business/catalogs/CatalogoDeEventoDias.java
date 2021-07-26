package business.catalogs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import business.Evento;
import business.EventoDia;

public class CatalogoDeEventoDias {
private EntityManager em;
	
	public CatalogoDeEventoDias(EntityManager em) {
		this.em = em;
	}
	

	public List<Date> getDatasComBilhetesDisponiveis(String nomeEvento) {
		try {
			TypedQuery<Date> query = em.createNamedQuery(EventoDia.GET_DATES_WITH_AVAILABLE_TICKETS, Date.class);
			query.setParameter(Evento.EVENT_NAME, nomeEvento);	
			return query.getResultList();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

}
