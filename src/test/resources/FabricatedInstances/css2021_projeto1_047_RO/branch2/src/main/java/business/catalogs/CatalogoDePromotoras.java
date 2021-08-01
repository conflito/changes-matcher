package business.catalogs;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import business.Promotora;
import facade.exceptions.PromotoraNaoExisteException;

public class CatalogoDePromotoras {

	private EntityManager em;
	
	public CatalogoDePromotoras(EntityManager em) {
		this.em = em;
	}

	/**
	 * Pesquisa por uma promotora com um id especifico.
	 * @param id - o id da promotora a pesquisar.
	 * @return Promotora que corresponde ao id especificado.
	 * @throws PromotoraNaoExisteException 
	 */
	public Promotora findPromotoraByID(int id) throws PromotoraNaoExisteException {
		try {
			TypedQuery<Promotora> query = em.createNamedQuery(Promotora.GET_PROMOTORA_BY_ID, Promotora.class);
			query.setParameter(Promotora.ID_PROMOTORA, id);
			return query.getSingleResult();
		} catch (Exception e) {
			throw new PromotoraNaoExisteException(id);
		}
	}

}
