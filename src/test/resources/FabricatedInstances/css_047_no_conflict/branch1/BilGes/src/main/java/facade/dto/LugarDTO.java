package facade.dto;

import java.util.ArrayList;
import java.util.List;

import business.Instalacao;
import business.Lugar;

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

	public String toString(Lugar l) {
		//List<String> lugaresOrdenados = new ArrayList<>();
		//for (Lugar l : lugares) {
		StringBuilder lugar = new StringBuilder();
		lugar.append(l.getFila() + "-" + l.getAssento());
		//lugaresOrdenados.add(lugar.toString());
		//}
		return lugar.toString();

	}

}
