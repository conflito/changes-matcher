package business;

import static javax.persistence.InheritanceType.SINGLE_TABLE;
import static javax.persistence.DiscriminatorType.STRING;	

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Instalacao
 *
 */
@Entity
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn(name = "INSTALACAOTYPE", discriminatorType = STRING)
@NamedQueries({
	@NamedQuery(name=Instalacao.GET_ALL_NAMES, query="SELECT i FROM Instalacao i"),
	@NamedQuery(name=Instalacao.FIND_BY_NAME, query="SELECT i FROM Instalacao i WHERE i.nome = :" + Instalacao.INSTALACAO_NAME),
	@NamedQuery(name=Instalacao.FIND_BY_DATES, query="SELECT i FROM Instalacao i, EventoDia e WHERE e in (i.diasComEventos) and e.start NOT BETWEEN :" + EventoDia.DATA_INICIO + " AND :" + EventoDia.DATA_FIM)

})
public abstract class Instalacao implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String GET_ALL_NAMES = "Instalacao.getAllNames";
	
	public static final String INSTALACAO_NAME = "nome";

	public static final String FIND_BY_NAME = "Instalacao.findByName";
	
	public static final String FIND_BY_DATES = "Instalacao.findByDates";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable = false, unique = true)
	private String nome;
	
	@OneToMany
	private List<EventoDia> diasComEventos;

	protected Instalacao() {
		super();
	}
	
	public abstract String getType();

	public abstract int getOcupacao();

	public List<EventoDia> getListaDiasComEventos() {
		return new ArrayList<>(diasComEventos);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public List<EventoDia> getDiasComEventos() {
		return diasComEventos;
	}

	public void setDiasComEventos(List<EventoDia> diasComEventos) {
		this.diasComEventos = diasComEventos;
	}

	public abstract List<Lugar> getLugares();

	public String helpString(){
		StringBuilder result = new StringBuilder();
		result.append("ID: " + id + " | Name: " + nome);
		if(diasComEventos != null)
			result.append(" | Dias: " + diasComEventos.size());
		else
			result.append(" | Dias: null");
		return result.toString();
	}
}
