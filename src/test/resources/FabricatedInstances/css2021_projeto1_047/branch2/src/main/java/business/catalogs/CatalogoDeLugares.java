package business.catalogs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import business.Evento;
import business.EventoDia;
import business.Instalacao;
import business.Lugar;
import facade.exceptions.AGApplicationException;

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
	
	public Lugar getLugarPorFilaEAssentoEInstalacao(char c, int i, Instalacao instalacao) throws AGApplicationException {
		try {
		TypedQuery<Lugar> query = em.createNamedQuery(Lugar.GET_SEATS_BY_ROW_SEAT_LOCATION, Lugar.class);
		query.setParameter(Lugar.FILA, c);
		query.setParameter(Lugar.ASSENTO, i);
		query.setParameter(Instalacao.INSTALACAO_NAME, instalacao);
			return query.getSingleResult();
		} catch (PersistenceException e) {
			throw new AGApplicationException ("Lugar não existe!", e);
		}
	}


	public Lugar refresh(int id) {
		return em.find(Lugar.class, id);
	}

}
