package business.catalogs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import business.Bilhete;
import business.Estado;
import business.Evento;
import business.EventoDia;
import business.Lugar;
import facade.exceptions.AGApplicationException;

public class CatalogoDeBilhetes {
	
	private EntityManager em;
	
	public CatalogoDeBilhetes(EntityManager em) {
		this.em = em;
	}

	public Bilhete getBilhetePorDataENomeEvento(String nomeEvento, Date data) throws AGApplicationException {
		TypedQuery<Bilhete> query = em.createNamedQuery(Bilhete.FIND_BY_DATE_AND_EVENT, Bilhete.class);
		query.setParameter(Evento.EVENT_NAME, nomeEvento);
		query.setParameter(EventoDia.DATE, data);
		
		try {
			return query.getSingleResult();
		} catch (PersistenceException e) {
			throw new AGApplicationException ("Bilhete nao encontrado.", e);
		}
		
	}

	public Bilhete findTicketBySeatAndEventAndDate(String nomeEvento, Date data, int l) throws AGApplicationException {
		TypedQuery<Bilhete> query = em.createNamedQuery(Bilhete.FIND_BY_SEAT_DATE_EVENT, Bilhete.class);
		query.setParameter(Evento.EVENT_NAME, nomeEvento);
		query.setParameter(EventoDia.DATE, data);
		query.setParameter(Lugar.LUGAR_ID, l);
		
		try {
			return query.getSingleResult();
		} catch (PersistenceException e) {
			throw new AGApplicationException ("Bilhete nao encontrado.", e);
		}
	}
	
	public void addBilhetesSemLugares(EventoDia ed, int numBilhetes) {
		
		List<Bilhete> listaBilhetes = new ArrayList<>(numBilhetes);

		for(int i = 0; i<numBilhetes; i++) {
			Bilhete newBilhete = new Bilhete(Estado.AVAILABLE);
			em.persist(newBilhete);
			listaBilhetes.add(newBilhete);
		}
		
		ed.setBilhetes(listaBilhetes);

		em.persist(ed);


	}

	public void addBilhetesComLugares(EventoDia ed, List<Lugar> listaLugares) {
		List<Bilhete> listaBilhetes = new ArrayList<>(listaLugares.size());

		for(int i = 0; i<listaLugares.size(); i++) {
			Bilhete newBilhete = new Bilhete(listaLugares.get(i), Estado.AVAILABLE);
			em.persist(newBilhete);
			listaBilhetes.add(newBilhete);
		}
		
		ed.setBilhetes(listaBilhetes);

		em.persist(ed);
	}

}
