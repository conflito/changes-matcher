package business;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: TipoEvento
 *
 */
@Entity
@NamedQueries({ @NamedQuery(name = TipoEvento.GET_TIPOSEVENTO, query = "SELECT te FROM TipoEvento te"),
				@NamedQuery(name = TipoEvento.GET_TIPOEVENTO_BY_NAME, query = "SELECT te FROM TipoEvento te WHERE te.nome = :"
							+ TipoEvento.NOME_TIPOEVENTO)
			})
public class TipoEvento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String GET_TIPOSEVENTO = "TipoEvento.getTiposEvento";
	public static final String GET_TIPOEVENTO_BY_NAME = "TipoEvento.getTipoEventoByName";

	public static final String NOME_TIPOEVENTO = "nome";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private int maxCapacidade;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoLugares tipoLugares;

	protected TipoEvento() {
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMaxCapacidade() {
		return maxCapacidade;
	}

	public void setMaxCapacidade(int maxCapacidade) {
		this.maxCapacidade = maxCapacidade;
	}

	public TipoLugares getTipoLugares() {
		return tipoLugares;
	}

	public void setTipoLugares(TipoLugares tipoLugares) {
		this.tipoLugares = tipoLugares;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
