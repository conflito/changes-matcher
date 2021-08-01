package business.handlers;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import business.Promotora;
import business.TipoEvento;
import business.catalogs.CatalogoDeEventos;
import business.catalogs.CatalogoDePromotoras;
import business.catalogs.CatalogoDeTiposEvento;
import facade.exceptions.AGApplicationException;
import facade.exceptions.DatasNaoValidasException;
import facade.exceptions.NomeEventoNaoUnicoException;
import facade.exceptions.PromotoraNaoCertificadaException;
import facade.exceptions.PromotoraNaoExisteException;
import facade.exceptions.TipoEventoInvalidoException;

public class CriarEventoHandler {

	private EntityManagerFactory emf;

	public CriarEventoHandler(EntityManagerFactory emf) {
		this.emf = emf;
	}
	
	/**
	 * Encontra os tipos de evento registados no sistema.
	 * @return uma lista com os tipos de eventos existentes.
	 * @throws AGApplicationException
	 */
	public List<TipoEvento> pedirTipoEvento() throws AGApplicationException {
		EntityManager em = emf.createEntityManager();
		CatalogoDeTiposEvento catTipoEvento = new CatalogoDeTiposEvento(em);

		try {
			em.getTransaction().begin();

			List<TipoEvento> tiposEvento = catTipoEvento.getTiposEvento();

			em.getTransaction().commit();
			return tiposEvento;
		} catch(Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();		
			}
			throw new AGApplicationException("Erro ao pedir os tipos de evento em CriarEventoHandler", e);
		}
	}
	
	/**
	 * Criar um novo evento de acordo com a informacao especificada.
	 * @param nomeTipo - o nome do tipo de evento
	 * @param nome - o nome do evento
	 * @param datas_horas - as datas e horas quando o evento acontece
	 * @param promotora - a entidade promotora do evento
	 * @throws AGApplicationException TipoEventoInvalidoException, NomeEventoNaoUnicoException,
	 * 			DatasNaoValidasException, PromotoraNaoExisteException, PromotoraNaoCertificadaException 
	 */
	public void criarEvento(String nomeTipo, String nome, List<Date> datasHoras, int promotora) throws AGApplicationException,
					TipoEventoInvalidoException, NomeEventoNaoUnicoException, DatasNaoValidasException,
					PromotoraNaoExisteException, PromotoraNaoCertificadaException {
		EntityManager em = emf.createEntityManager();
		CatalogoDeTiposEvento catTiposEvento = new CatalogoDeTiposEvento(em);
		CatalogoDeEventos catEventos = new CatalogoDeEventos(em);
		CatalogoDePromotoras catPromotoras = new CatalogoDePromotoras(em);

		try {
			em.getTransaction().begin();

			TipoEvento tipoEvento = catTiposEvento.findTipoEventoByName(nomeTipo);
			
			if(catEventos.findEventoByName(nome) != null) {
				throw new NomeEventoNaoUnicoException(nome);
			}
			
			if(!verifyDates(datasHoras)) {
				throw new DatasNaoValidasException();
			}
			
			Promotora pr = catPromotoras.findPromotoraByID(promotora);
			if(!pr.supportsTipoEvento(nomeTipo)) {
				throw new PromotoraNaoCertificadaException(nomeTipo, promotora);
			}
			
			catEventos.addEvento(nome, tipoEvento, pr, datasHoras);
			
			em.getTransaction().commit();			
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new AGApplicationException("Erro ao persistir novo evento em CriarEventoHandler", e);
		} finally {
			em.close();
		}
	}

	/**
	 * Confirma que os pares de datas de uma lista sucedem-se temporalmente.
	 * @param datas_horas - a lista com os pares de datas.
	 * @return true se todos os pares se sucedem, false c.c.
	 */
	private boolean verifyDates(List<Date> datasHoras) {
		for(int i = 0; i < datasHoras.size(); i+=2) {
			if (datasHoras.get(i).after(datasHoras.get(i+1))) {
				return false;
			}
		}
		return true;
	}

}