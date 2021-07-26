package facade.dto;

import java.util.ArrayList;
import java.util.List;
import business.EventoDia;

public class InstalacaoDTO {
	
	private int id;
	private String nome;
	private List<EventoDia> diasComEventos;
	
	public InstalacaoDTO(int id, String nome) {
		
	}


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

}
