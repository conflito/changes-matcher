package business.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import business.Bilhete;
import business.Estado;
import business.Evento;
import business.EventoDia;
import business.Lugar;
import business.TipoLugares;
import business.catalogs.CatalogoDeBilhetes;
import business.catalogs.CatalogoDeEventoDias;
import business.catalogs.CatalogoDeEventos;
import business.catalogs.CatalogoDeLugares;
import business.catalogs.CatalogoDeReservasBilhetes;
import facade.exceptions.AGApplicationException;
import facade.exceptions.BilhetePasseNaoDisponivelException;
import facade.exceptions.DatasNaoDisponiveisException;
import facade.exceptions.InvalidEventTypeException;
import facade.exceptions.LugarReservadoException;
import facade.exceptions.LugaresNaoDisponiveisException;
import facade.exceptions.QuantidadeBilhetesPasseIndisponivelException;
import facade.exceptions.QuantidadeInferioraUmException;

public class ComprarBilhetesHandler {

	private EntityManagerFactory emf;

	private Evento evento;
	private Date data;


	public ComprarBilhetesHandler(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public List<Date> comprarBilhetesLugares(String nomeEvento) throws AGApplicationException {

		EntityManager em = emf.createEntityManager();
		CatalogoDeEventos catalogoDeEventos = new CatalogoDeEventos(em);
		CatalogoDeEventoDias catalogoDeEventoDias = new CatalogoDeEventoDias(em);

		try {
			em.getTransaction().begin();
			//valida o nome do evento
			evento = catalogoDeEventos.findEventoByName(nomeEvento);

			if(evento.getTipo().getTipoLugares().equals(TipoLugares.EM_PE)) {
				throw new InvalidEventTypeException();
			}
	
			List<Date> datas = catalogoDeEventoDias.getDatasComBilhetesDisponiveis(nomeEvento);

			if(datas.isEmpty()) {
				throw new DatasNaoDisponiveisException();
			}
			em.getTransaction().commit();
			return datas;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new AGApplicationException("Erro ao validar evento.", e);
		} finally {
			em.close();
		}

	}

	public List<String> escolherData(Date data) throws AGApplicationException {
		EntityManager em = emf.createEntityManager();
		CatalogoDeLugares catalogoDeLugares = new CatalogoDeLugares(em);

		try {
			em.getTransaction().begin();
			this.data = data;
			List<Lugar> lugares = catalogoDeLugares.getLugaresPorDataeEvento(data, evento);
			if(lugares.isEmpty()) {
				throw new LugaresNaoDisponiveisException();
			}

			List<String> lugaresOrdenados = new ArrayList<>();
			for (Lugar l : lugares) {
				StringBuilder lugar = new StringBuilder();
				lugar.append(l.getFila() + "-" + l.getAssento());
				lugaresOrdenados.add(lugar.toString());
			}
			em.getTransaction().commit();
			return lugaresOrdenados;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new AGApplicationException("Erro ao procurar lugares.", e);
		} finally {
			em.close();
		}
	}

	public String escolhaLugares(List<Lugar> lugares, String email) throws AGApplicationException {
		EntityManager em = emf.createEntityManager();
		CatalogoDeLugares catalogoDeLugares = new CatalogoDeLugares(em);
		CatalogoDeBilhetes catalogoDeBilhetes = new CatalogoDeBilhetes(em);
		CatalogoDeReservasBilhetes catalogoDeReservasBilhetes = new CatalogoDeReservasBilhetes(em);

		try {
			em.getTransaction().begin();
			
			List<Bilhete> bilhetes = new ArrayList<>();
			for (Lugar l : lugares) {
				l = catalogoDeLugares.refresh(l);
				Bilhete bilhete = catalogoDeBilhetes.findTicketBySeatAndEventAndDate(evento.getNome(), data, l.getId());
				
				
				if (bilhete.getEstado().equals(Estado.UNAVAILABLE)) {
					throw new LugarReservadoException("" + l.getFila() + l.getAssento());
				}
				bilhete.setEstado(Estado.UNAVAILABLE);
				em.merge(bilhete);
				bilhetes.add(bilhete);
			}

			
			double valorTotal = evento.getPrecoBilhete()*bilhetes.size();

			String dadosPagamento = catalogoDeReservasBilhetes.addReservaBilhete(email, bilhetes, valorTotal);

			em.getTransaction().commit();
			return dadosPagamento;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new AGApplicationException("Erro ao escolher lugares.", e);
		}
	}

	public int comprarBilhetesPasse(String nomeEvento) throws AGApplicationException {
		EntityManager em = emf.createEntityManager();
		CatalogoDeEventos catalogoDeEventos = new CatalogoDeEventos(em);

		try {
			em.getTransaction().begin();
			evento = catalogoDeEventos.findEventoByName(nomeEvento);
			if (!evento.bilhetePasseDisponivel()) {
				throw new BilhetePasseNaoDisponivelException();
			}

			int numBilhetesPasse = evento.numBilhetesPasse();
			em.getTransaction().commit();
			return numBilhetesPasse;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new AGApplicationException("Erro ao verificar bilhetes-passe.", e);
		}	
	}

	public String escolhaNumBilhetes(int quantidade, String email) throws AGApplicationException {
		EntityManager em = emf.createEntityManager();
		CatalogoDeBilhetes catalogoDeBilhetes = new CatalogoDeBilhetes(em);
		CatalogoDeReservasBilhetes catalogoDeReservasBilhetes = new CatalogoDeReservasBilhetes(em);

		try {
			em.getTransaction().begin();
			evento = em.merge(evento);
			
			if (quantidade <= 0) {
				throw new QuantidadeInferioraUmException();
			}
			
			if (evento.numBilhetesPasse() < quantidade) {
				throw new QuantidadeBilhetesPasseIndisponivelException();
			}

			List<Bilhete> bilhetes = new ArrayList<>();
			while (quantidade > 0) {
				for (EventoDia ed : evento.getListaEventoDias()) {
					bilhetes.add(catalogoDeBilhetes.getBilhetePorDataENomeEvento(evento.getNome(), ed.getStart()));
				}
				quantidade--;
			}
			
			for(Bilhete b : bilhetes) {
				b.setEstado(Estado.UNAVAILABLE);
				em.merge(b);
			}
			
			double valorTotal = evento.getPrecoBilhetePasse()*quantidade;
			
			String dadosPagamento = catalogoDeReservasBilhetes.addReservaBilhete(email, bilhetes, valorTotal);

			em.getTransaction().commit();
			return dadosPagamento;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new AGApplicationException("Erro ao reservar bilhetes-passe.", e);
		}

	}
}
