package business;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: ReservaBilhete
 *
 */
@Entity

public class ReservaBilhete implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private int entidade;
	
	@Column(nullable = false, unique = true)
	private int referencia;
	
	@Column(nullable = false)
	private double valorTotal;
	
	@OneToMany
	private List<Bilhete> bilhetes;
	
	private Random rd;

	public ReservaBilhete() {
		super();
	}
	
	public ReservaBilhete(String email, List<Bilhete> bilhetes, double valorTotal) {
		this.email = email;
		this.bilhetes = bilhetes;
		this.rd = new Random();
	}

	public void setReferencia() {
		int num = rd.nextInt(1000000000);
		String formatted = String.format("%09d", num);
		this.referencia = Integer.parseInt(formatted);
		
	}

	public void setEntidade() {
		int num = rd.nextInt(100000);
		String formatted = String.format("%05d", num);
		this.entidade = Integer.parseInt(formatted);
		
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
		
	}

	public String getDadosPagamento() {
		StringBuilder dados = new StringBuilder();
		dados.append("Entidade: "+this.entidade + "\nReferencia: "+ this.referencia +"\nValor: " + this.valorTotal);
		return dados.toString();
	}
   
}
