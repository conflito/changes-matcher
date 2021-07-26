package facade.dto;

import business.Instalacao;

public class LugarDTO {
	
	private int id;
	private char fila;
	private int assento;
	private Instalacao instalacao;
	
	
	public LugarDTO(char fila, int assento, Instalacao instalacao) {
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
