package business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
	@NamedQuery(name=EventoDia.GET_DATES_WITH_AVAILABLE_TICKETS, 
			query="SELECT d.start FROM EventoDia d, Bilhete b , Evento e WHERE b.estado = 'AVAILABLE' AND e.nome = :"+Evento.EVENT_NAME ),
})
public class EventoDia implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String DATA_FIM = "end";

	public static final String DATA_INICIO = "start";

	public static final String FIND_BY_DATES = "EventoDia.findByDates";
	
	public static final String GET_DATES_WITH_AVAILABLE_TICKETS = "EventoDia.getDatesWithAvailableTickets";
	public static final String DATE = "start";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	/**
	 * The start date with time
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date start;
	
	/**
	 * The end date with time
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date end;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public List<Bilhete> getBilhetes() {
		return bilhetes;
	}

	public void setBilhetes(List<Bilhete> bilhetes) {
		this.bilhetes = bilhetes;
	}

	@OneToMany
	private List<Bilhete> bilhetes;
	
	@ManyToOne
	private Evento evento;
	
	public EventoDia(Date start, Date end, Evento evento) {
		this.start = start;
		this.end = end;
		this.evento = evento;
		this.bilhetes = new ArrayList<>();
	}
	
	protected EventoDia() {
	}

	public Date getStartDate() {
		return this.start;
	}

	public Date getEndDate() {
		return this.end;
	}

	public boolean hasBilhetesDisponiveis() {
		for (Bilhete b : bilhetes) {
			if(!b.isDisponivel()) {
				return true;
			}
		}
		return false;
	}

	public int numTicketsAvailable() {
		int num = 0;
		for (Bilhete b : bilhetes) {
			if(!b.isDisponivel()) {
				num += 1;
			}
		}
		return num;
	}

}
