package facade.dto;

import business.Lugar;

public class LugarDTO {

	private int id;
	private char fila;
	private int assento;

	public LugarDTO(int id, char fila, int assento) {
		this.id = id;
		this.fila = fila;
		this.assento = assento;
	}
	
	public LugarDTO(char fila, int assento) {
		this.fila = fila;
		this.assento = assento;
	}

	public char getFila() {
		return fila;
	}

	public int getAssento() {
		return assento;
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
