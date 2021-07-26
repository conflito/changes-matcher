package business.producer;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import business.producer.exceptions.NoProducerException;
import business.producer.exceptions.ProducerWithoutCertification;
import facade.exceptions.ApplicationException;

/**
 *
 */
public class ProducerCatalog {
	
	private EntityManager em;
	
	/**
	 * Constructs a Producer's catalog given a entity manager factory
	 */
	public ProducerCatalog(EntityManager em) {
		this.em = em;
	}

	
	/**
	 * Gets the producer from the database
	 * @param registrationNumber of the producer 
	 * @return the producer with that registration number
	 * @throws NoProducerException if the producer does not exist
	 */
	public Producer getProducer (int registrationNumber) throws ApplicationException {
		TypedQuery<Producer> query = em.createNamedQuery(Producer.FIND_BY_REGNUM, Producer.class);
		query.setParameter(Producer.INT_REGNUM, registrationNumber);
		try {
			return query.getSingleResult();
		} catch (PersistenceException e) {
			throw new NoProducerException("Producer not found.", e);
		}
	}
	
	
	/**
	 * Checks if the producer has certification for that type of Event
	 * @param producerNumber the number of the producer
	 * @param typeOfEvent the type of Event
	 * @throws NoProducerException if the producer does not have that type of certification 
	 */
	public void checkcertification(int producerNumber, String typeOfEvent) throws ApplicationException {
		TypedQuery<String> query = em.createNamedQuery(Producer.GET_THIS_PRODUCER_CERTIFICATION, String.class);
		query.setParameter(Producer.INT_REGNUM, producerNumber);
		try {
			
			List<String> listCertification = query.getResultList();

			boolean check = false;
			for(String e : listCertification) {
				if(e.equals(typeOfEvent)) {
					check = true;
					break;
				}
			}
			if(!check) {
				throw new ProducerWithoutCertification("Error no certification of the type: " + typeOfEvent);
			}
		} catch (PersistenceException e) {
			throw new ProducerWithoutCertification("Error checking certification", e);
		}
		
	}

}
