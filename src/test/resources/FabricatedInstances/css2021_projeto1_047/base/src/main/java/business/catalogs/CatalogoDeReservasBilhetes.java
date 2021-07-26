package business.catalogs;

import java.util.List;

import javax.persistence.EntityManager;

import business.Bilhete;
import business.ReservaBilhete;

public class CatalogoDeReservasBilhetes {

	private EntityManager em;
	
	public CatalogoDeReservasBilhetes(EntityManager em) {
		this.em = em;
	}

	public String addReservaBilhete(String email, List<Bilhete> bilhetes, double valorTotal) {
		ReservaBilhete reserva = new ReservaBilhete(email, bilhetes, valorTotal);
		reserva.setReferencia();
		reserva.setEntidade();
		reserva.setValorTotal(valorTotal);
		em.persist(reserva);
		return reserva.getDadosPagamento();
	}

}
