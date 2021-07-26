package business;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Lugar
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name=Lugar.GET_ORDERED_SEATS_BY_DATE_AND_EVENT,
			query="SELECT l FROM Lugar l, Bilhete b, EventoDia ed, Evento e WHERE e.nome = :" + Evento.EVENT_NAME + " AND b.estado = 'AVAILABLE' AND ed.start = :" + EventoDia.DATE+ " ORDER BY l.fila, l.assento"),
	@NamedQuery(name=Lugar.GET_SEATS_BY_ROW_SEAT_LOCATION,
	query="SELECT l FROM Lugar l, Instalacao i WHERE l.fila = :"+Lugar.FILA+" AND l.assento = :"+Lugar.ASSENTO+" AND i.nome = :"+Instalacao.INSTALACAO_NAME),


})
public class Lugar implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String GET_ORDERED_SEATS_BY_DATE_AND_EVENT = "Lugar.getOrderedSeatsByDateAndEvent";
	
	public static final String LUGAR_ID = "id";
	
	public static final String GET_SEATS_BY_ROW_SEAT_LOCATION = "Lugar.getSeatsByRowSeatLocation";

	public static final String FILA = "fila";

	public static final String ASSENTO = "assento";


	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(nullable = false)
	private char fila;
	
	@Column(nullable = false)
	private int assento;
	
	@ManyToOne
	private Instalacao instalacao;
	
	Lugar() {
		super();
	}
	
	public Lugar(char fila, int assento, Instalacao instalacao) {
		this.fila = fila;
		this.assento = assento;
		this.instalacao = instalacao;
	}

	public char getFila() {
		return fila;
	}

	public int getAssento() {
		return assento;
	}
	
	public Instalacao getInstalacao() {
		return instalacao;
	}

	public int getId() {
		return id;
	}
   
}
