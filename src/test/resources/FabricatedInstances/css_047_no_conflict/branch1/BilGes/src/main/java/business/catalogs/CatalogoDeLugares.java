package business.catalogs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import business.Evento;
import business.EventoDia;
import business.Lugar;

public class CatalogoDeLugares {
	
	private EntityManager em;
	
	public CatalogoDeLugares(EntityManager em) {
		this.em = em;
	}

	public List<Lugar> getLugaresPorDataeEvento(Date data, Evento evento) {
		try {
		TypedQuery<Lugar> query = em.createNamedQuery(Lugar.GET_ORDERED_SEATS_BY_DATE_AND_EVENT, Lugar.class);
		query.setParameter(EventoDia.DATE, data);
		query.setParameter(Evento.EVENT_NAME, evento);
			return query.getResultList();
		} catch (PersistenceException e) {
			return new ArrayList<>();
		}
	}

	public Lugar refresh(Lugar l) {
		return em.find(Lugar.class, l.getId());
	}

}
