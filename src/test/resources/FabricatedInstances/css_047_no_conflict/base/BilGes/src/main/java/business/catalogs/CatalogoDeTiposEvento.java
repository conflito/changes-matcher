package business.catalogs;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import business.TipoEvento;
import facade.exceptions.NaoHaTiposEventoException;
import facade.exceptions.TipoEventoInvalidoException;

public class CatalogoDeTiposEvento {
	
	private EntityManager em;
	
	public CatalogoDeTiposEvento(EntityManager em) {
		this.em = em;
	}

	/**
	 * Pesquisa por todos os tipos de evento existentes no sistema.
	 * @return uma lista com os tipo de evento encontrados.
	 * @throws NaoHaTiposEventoException 
	 */
	public List<TipoEvento> getTiposEvento() throws NaoHaTiposEventoException {
		try {
			TypedQuery<TipoEvento> query = em.createNamedQuery(TipoEvento.GET_TIPOSEVENTO, TipoEvento.class);
			return query.getResultList();
		} catch (Exception e) {
			throw new NaoHaTiposEventoException();
		}
	}

	/**
	 * Pesquisa por um Tipo de Evento com um nome especifico.
	 * @param nomeTipo - o nome do tipo de evento a pesquisar.
	 * @return TipoEvento com o nome especificado.
	 * @throws TipoEventoInvalidoException 
	 */
	public TipoEvento findTipoEventoByName(String nomeTipo) throws TipoEventoInvalidoException {
		try {
			TypedQuery<TipoEvento> query = em.createNamedQuery(TipoEvento.GET_TIPOEVENTO_BY_NAME, TipoEvento.class);
			query.setParameter(TipoEvento.NOME_TIPOEVENTO, nomeTipo);
			return query.getSingleResult();
		} catch (Exception e) {
			throw new TipoEventoInvalidoException(nomeTipo);
		}
	}

}
