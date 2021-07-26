package business;

import java.io.Serializable;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Bilhete
 *
 */
@Entity
@NamedQueries({ @NamedQuery(name=Bilhete.FIND_BY_DATE_AND_EVENT, 
							query="SELECT b FROM Bilhete b, EventoDia d, Evento e WHERE b.estado = 'AVAILABLE' AND d.start = :"
									+ EventoDia.DATE + " AND e.nome = :" +Evento.EVENT_NAME ),
				@NamedQuery(name=Bilhete.FIND_BY_SEAT_DATE_EVENT,
							query="SELECT b FROM Bilhete b, EventoDia d, Evento e, Lugar l WHERE b.estado = 'AVAILABLE' AND d.start = :"
									+ EventoDia.DATE +" AND e.nome = :"+Evento.EVENT_NAME+" AND l.id = :"+Lugar.LUGAR_ID)
})
public class Bilhete implements Serializable {

	public static final long serialVersionUID = 1L;

	public static final String FIND_BY_DATE_AND_EVENT = "Bilhete.FindByDateAndEvent";
	public static final String FIND_BY_SEAT_DATE_EVENT = "Bilhete.FindBySeatandDateandEvent";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@ManyToOne
	private Lugar lugar;
	
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Estado estado;
	
	Bilhete() {
		super();
	}
	
	public Bilhete(Lugar lugar, Estado estado) {
		this.lugar = lugar;
		this.estado = estado;
	}
	
	public Bilhete(Estado estado) {
		this.estado = estado;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Constroi um bilhete 
	 * @param preco O preco do bilhete
	 * @param lugar O lugar do bilhete
	 * @param eventoDia a data do evento do bilhete
	 */
	public Bilhete(double preco, Lugar lugar, EventoDia eventoDia) {
		this.lugar = lugar;
	}

	/**
	 * Retorna o lugar do bilhete
	 * @return lugar do bilhete
	 */
	public Lugar getLugar() {
		return lugar;
	}

	public void setLugar(Lugar lugar) {
		this.lugar = lugar;
	}
		
	public boolean isDisponivel() {
		return estado == Estado.AVAILABLE;
	}

	public void setEstado(Estado novoEstado) {
		this.estado = novoEstado;
	}

	public Estado getEstado() {
		return estado;
	}
   
}
