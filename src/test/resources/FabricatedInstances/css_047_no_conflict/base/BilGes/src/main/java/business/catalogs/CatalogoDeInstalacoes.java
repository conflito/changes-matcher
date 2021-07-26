package business.catalogs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import business.EventoDia;
import business.Instalacao;
import facade.exceptions.InstalacaoNaoExisteDatasException;
import facade.exceptions.InstalacaoNaoExisteException;

public class CatalogoDeInstalacoes {
	private EntityManager em;

	/**
	 * Constructs a discount's catalog giving a entity manager factory
	 */
	public CatalogoDeInstalacoes(EntityManager em) {
		this.em = em;
	}
	
	public Iterable<Instalacao> getAllInstalacoes(){
		try {
			TypedQuery<Instalacao> query = em.createNamedQuery(Instalacao.GET_ALL_NAMES, Instalacao.class);
			return query.getResultList();
		} catch (Exception e) {
			return new ArrayList<>(); 
		}	
		
	}

	public Instalacao getInstalacao(String nomeInstalacao) throws InstalacaoNaoExisteException {
		try {
			TypedQuery<Instalacao> query = em.createNamedQuery(Instalacao.FIND_BY_NAME, Instalacao.class);
			query.setParameter(Instalacao.INSTALACAO_NAME, nomeInstalacao);
			return query.getSingleResult();
		} catch (Exception e) {
			throw new InstalacaoNaoExisteException (nomeInstalacao);
		}
	}


	public List<Instalacao> getInstalacoesDisponiveisDatas(Date startDate, Date endDate) throws InstalacaoNaoExisteDatasException {
		try {
			TypedQuery<Instalacao> query = em.createNamedQuery(Instalacao.FIND_BY_DATES, Instalacao.class);
			query.setParameter(EventoDia.DATA_INICIO, startDate);
			query.setParameter(EventoDia.DATA_FIM, endDate);
			return query.getResultList();
		} catch (Exception e) {
			throw new InstalacaoNaoExisteDatasException ();
		}
	}

}
