package business.catalogs;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import business.Evento;
import business.EventoDia;
import business.Promotora;
import business.TipoEvento;

public class CatalogoDeEventos {
	
	private EntityManager em;
	
	public CatalogoDeEventos(EntityManager em) {
		this.em = em;
	}

	/**
	 * Procura por um evento que tenha sido atribu�do um certo nome.
	 * @param nome - o nome do evento a procurar.
	 * @return um Evento se existir um evento com o nome dado, c.c. null.
	 */
	public Evento findEventoByName(String nome) {
		try {
			TypedQuery<Evento> query = em.createNamedQuery(Evento.FIND_EVENTO_BY_NAME, Evento.class);
			query.setParameter(Evento.NOME_EVENTO, nome);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Criar e persiste um novo evento a adicionar neste catalogo.
	 * @param nome - nome do evento.
	 * @param tipoEvento - tipo do evento.
	 * @param pr - a promotora do evento.
	 * @param datasHoras - os pares de datas de acontecimento do evento.
	 */
	public void addEvento(String nome, TipoEvento tipoEvento, Promotora pr, List<Date> datasHoras) {
		Evento newEvento = new Evento(nome, tipoEvento, pr);
		
		for(int i = 0; i < datasHoras.size(); i+=2) {
			EventoDia newEventoDia = new EventoDia(datasHoras.get(i), datasHoras.get(i+1), newEvento);
			em.persist(newEventoDia);
			newEvento.addEventoDia(newEventoDia);
		}
		
		em.persist(newEvento);
	}

}
