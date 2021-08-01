package business.handlers;

import java.util.Date;
import java.util.List;
import java.util.OptionalDouble;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import business.Evento;
import business.EventoDia;
import business.Instalacao;
import business.Lugar;
import business.MockData;
import business.TipoEvento;
import business.TipoLugares;
import business.catalogs.CatalogoDeBilhetes;
import business.catalogs.CatalogoDeEventos;
import business.catalogs.CatalogoDeInstalacoes;
import facade.exceptions.AGApplicationException;
import facade.exceptions.DataInvalidaException;
import facade.exceptions.EventoComInstalacaoException;
import facade.exceptions.EventoIncompativelComInstalacaoException;
import facade.exceptions.EventoNaoExisteException;
import facade.exceptions.InstalacaoNaoDisponivelDatas;
import facade.exceptions.MaisLugaresQueCapacidadeException;
import facade.exceptions.PrecoInvalidoException;

public class AtribuirInstalacaoHandler {

	private EntityManagerFactory emf;

	public AtribuirInstalacaoHandler(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public Iterable<Instalacao> atribuirInstalacao() throws AGApplicationException {
		EntityManager em = emf.createEntityManager();
		CatalogoDeInstalacoes catalogoInstalacao = new CatalogoDeInstalacoes(em);
		try {
			em.getTransaction().begin();
			Iterable<Instalacao> instalacoes = catalogoInstalacao.getAllInstalacoes();
			em.getTransaction().commit();
			return instalacoes;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new AGApplicationException("Error fetching installations.", e);
		} finally {
			em.close();
		}

	}

	public void darInfoAtribuicao(String nomeEvento, String nomeInstalacao, Date dataInicioVenda, 
									double precoIndividual, OptionalDouble precoPasse) throws AGApplicationException {
		EntityManager em = emf.createEntityManager();
		CatalogoDeInstalacoes catalogoInstalacao = new CatalogoDeInstalacoes(em);
		CatalogoDeEventos catalogoEvento = new CatalogoDeEventos(em);
		CatalogoDeBilhetes catalogoBilhete = new CatalogoDeBilhetes(em);

		try {

			em.getTransaction().begin();

			//nome do evento eh valido
			Evento evento = catalogoEvento.findEventoByName(nomeEvento);
			if(evento == null) {
				throw new EventoNaoExisteException(nomeEvento);
			}
			
			//evento ainda nao tem instalacao atribuida
			if (evento.hasInstalacao()) {
				throw new EventoComInstalacaoException(nomeEvento);
			}

			//nome da instalacao eh valida
			Instalacao instalacao = catalogoInstalacao.getInstalacao(nomeInstalacao);

			//tipo de evento
			TipoEvento tipoEvento = evento.getTipoEvento();

			//tipo de instalacao
			TipoLugares tipoInstalacao = instalacao.getType();

			//se nao forem compativeis
			if(!tipoEvento.getTipoLugares().equals(tipoInstalacao)){
				throw new EventoIncompativelComInstalacaoException(nomeEvento);	
			}

			//se a instalacao tiver mais lugares do que a capacidade do evento
			if(tipoInstalacao.equals(TipoLugares.SENTADO) && instalacao.getOcupacao() > tipoEvento.getMaxCapacidade() ) {
				throw new MaisLugaresQueCapacidadeException();
			}

			//verificar se a instalacao esta livre durante o periodo em que o evento se realiza
			List<EventoDia> eventoDias = evento.getListaEventoDias();

			for(EventoDia ed : eventoDias) {
				List<Instalacao> instalacoesDisponiveis = catalogoInstalacao.getInstalacoesDisponiveisDatas(ed.getStartDate(), ed.getEndDate());
				if(!instalacoesDisponiveis.contains(instalacao)) {
					throw new InstalacaoNaoDisponivelDatas(instalacao.getNome(), ed.getStartDate(), ed.getEndDate());
				}
			}

			//verificar se a data de venda de blhetes nao eh no passado
			if(dataInicioVenda.before(MockData.getDataAtual())) {
				throw new DataInvalidaException("A data de inicio de venda dos bilhetes nao pode ser no passado");
			}

			//verificar se a data de venda de bilhetes eh anterior ah primeira data do evento
			if(dataInicioVenda.after(eventoDias.get(0).getStartDate())) {
				throw new DataInvalidaException("A data de inicio de venda dos bilhetes nao pode ser superior ah data do primeiro dia de evento");			
			}

			//se o preco invidivual for menor que 0
			if(precoIndividual < 0) {
				throw new PrecoInvalidoException("" + precoIndividual);	
			}
			//set do preco unico
			evento.setPrecoUnico(precoIndividual);

			//set do preco do passe se estiver presente
			if(precoPasse.isPresent()) {
				evento.setPrecoPasse(precoPasse.getAsDouble());
			}

			//set da instalacao do evento
			evento.setInstalacao(instalacao);
			//set do evento na instalacao
			instalacao.setDiasComEventos(eventoDias);
			
			em.merge(instalacao);

			//bilhetes para localizacao sem lugares == em pe
			if(instalacao.getType().equals(TipoLugares.EM_PE)){
				int numBilhetes = Math.min(instalacao.getOcupacao(), tipoEvento.getMaxCapacidade());
				for(EventoDia ed : eventoDias) {
					catalogoBilhete.addBilhetesSemLugares(ed, numBilhetes);

				}
			} else {
				
				//bilhetes para localizacao com lugares == assentos
				List<Lugar> listaLugares = instalacao.getLugares();
				for(EventoDia ed : eventoDias) {
					catalogoBilhete.addBilhetesComLugares(ed, listaLugares);
				}
				
				
			}
			em.merge(eventoDias);
			evento.setDiasDoEvento(eventoDias);
			em.merge(evento);

			// end transaction
			em.getTransaction().commit();

		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new AGApplicationException("Error choosing request.", e);
		}	
	}

}
