package facade.dto;

import business.TipoLugares;

public class InstalacaoDTO {
	
	private String nome;
	private TipoLugares tipoLugares;
	
	public InstalacaoDTO(String nome, TipoLugares tipoLugares) {
		this.nome = nome;
		this.tipoLugares = tipoLugares;
		
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TipoLugares getTipoLugares() {
		return tipoLugares;
	}

	public void setTipoLugares(TipoLugares tipoLugares) {
		this.tipoLugares = tipoLugares;
	}
	
	public String toString() {
		return "Nome Instalacao: " + this.nome + " \n Tipo de Lugares: " + this.tipoLugares;
		
	}

}
