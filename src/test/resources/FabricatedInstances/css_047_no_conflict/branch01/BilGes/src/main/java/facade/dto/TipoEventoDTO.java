package facade.dto;
import business.TipoLugares;

public class TipoEventoDTO {
	
	private int id;
	private String nome;
	private int maxCapacidade;
	private TipoLugares tipoLugares;
	
	public TipoEventoDTO(int id, String nome, int maxCapacidade, TipoLugares tipoLugares) {
		this.id = id;
		this.nome = nome;
		this.maxCapacidade = maxCapacidade;
		this.tipoLugares = tipoLugares;
		
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
