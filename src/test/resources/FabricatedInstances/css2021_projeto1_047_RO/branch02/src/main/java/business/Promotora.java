package business;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Promotora
 *
 */
@Entity
@NamedQueries({ @NamedQuery(name = Promotora.GET_PROMOTORA_BY_ID, query = "SELECT p FROM Promotora p WHERE p.id = :"
							+ Promotora.ID_PROMOTORA)
			})
public class Promotora implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String GET_PROMOTORA_BY_ID = "Promotora.getPromotorByID";
	
	public static final String ID_PROMOTORA = "id";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;
	
	@Column(nullable = false)
	private String nome;
	
	@OneToMany
	private List<TipoEvento> tiposEvento;

	public Promotora() {
		super();
	}

	/**
	 * Verifica se esta promotora suporta um certo tipo de evento.
	 * @param nomeTipo - o nome do tipo de evento a suportar.
	 * @return true se suportar, false c.c.
	 */
	public boolean supportsTipoEvento(String nomeTipo) {
		for(TipoEvento tipo : this.tiposEvento) {
			if (tipo.getNome().equals(nomeTipo)) {
				return true;
			}
		}
		return false;
	}
	
}
