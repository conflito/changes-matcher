package business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Evento
 *
 */
@Entity
@NamedQueries({ @NamedQuery(name = Evento.FIND_EVENTO_BY_NAME, query = "SELECT e FROM Evento e WHERE e.nome = :"
							+ Evento.NOME_EVENTO),
				@NamedQuery(name=Evento.GET_DATE_BY_EVENTO_NAME, query="SELECT d.start FROM EventoDia d, Evento e WHERE e.nome = :"
							+ Evento.EVENT_NAME),
})
public class Evento implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String EVENT_NAME = "nome";

	public static final String GET_DATE_BY_EVENTO_NAME = "Evento.findByName";
	
	public static final String FIND_EVENTO_BY_NAME = "Evento.getEventoByName";

	public static final String NOME_EVENTO = "nome";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(nullable = false, unique = true)
	private String nome;
	
	@JoinColumn(nullable = false)
	private Promotora promotora;
	
	@JoinColumn(nullable = false)
	private TipoEvento tipoEvento;
	
	@ManyToOne
	private Instalacao instalacao;
	
	@Column(nullable = true)
	private double precoBilhetePasse;
	
	@Column(nullable = true)
	private double precoBilhete;
	
	@OneToMany
	private List<EventoDia> diasDoEvento;

	public Evento() {
	}
	
	public Evento(String nome, TipoEvento tipoEvento, Promotora promotora) {
		this.nome = nome;
		this.promotora = promotora;
		this.tipoEvento = tipoEvento;
		this.instalacao = null;
		this.diasDoEvento = new ArrayList<>();
	}
	
	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Promotora getPromotora() {
		return promotora;
	}

	public void setPromotora(Promotora promotora) {
		this.promotora = promotora;
	}

	public List<EventoDia> getDiasDoEvento() {
		return diasDoEvento;
	}

	public void setDiasDoEvento(List<EventoDia> diasDoEvento) {
		this.diasDoEvento = diasDoEvento;
	}
	
	public void setInstalacao(Instalacao instalacao) {
		this.instalacao = instalacao;		
	}
	
	public Instalacao getInstalacao() {
		return instalacao;
	}
	
	public TipoEvento getTipoEvento() {
		return this.tipoEvento;
	}

	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public boolean hasInstalacao() {
		return instalacao != null;
	}
	
	public void setPrecoBilhete(double preco) {
		this.precoBilhete = preco;
	}
	
	public void setPrecoBilhetePasse(double preco) {
		this.precoBilhetePasse = preco;
	}

	public double getPrecoBilhetePasse() {
		return precoBilhetePasse;
	}

	public double getPrecoBilhete() {
		return precoBilhete;
	}
	
	/**
	 * Adiciona um elemento EventoDia ao atributo diasDoEvento
	 * @param newEventoDia - o novo elemento a adicionar
	 */
	public void addEventoDia(EventoDia newEventoDia) {
		this.diasDoEvento.add(newEventoDia);
	}   

	public boolean bilhetePasseDisponivel() {
		for (EventoDia e : diasDoEvento) {
			if (!e.hasBilhetesDisponiveis()) {
				return false;
			}
		}
		return true;
	}

	public int numDiasEvento() {
		return diasDoEvento.size();
	}

	public int numBilhetesPasse() {
		int numTicketsAvailable = 0;
		for (EventoDia e : diasDoEvento) {
			numTicketsAvailable += e.numTicketsAvailable();
		}
		
		return numTicketsAvailable / numDiasEvento();
	}

}
