package facade.services;

import business.util.Triplet;

import business.handlers.BuyPassTicketsHandler;
import facade.exceptions.ApplicationException;

public class BuyPassTicketService {

	
	private BuyPassTicketsHandler buyPass;
	
	
	public BuyPassTicketService(BuyPassTicketsHandler buyPass) {
		this.buyPass = buyPass;
	}
	
	
	public int buyPass(String eventName) throws ApplicationException {
		return buyPass.buyPass(eventName);
	}
	
	public Triplet<String, String, Integer> numberOfPassesAndInsertMail(int numberPasses, String email) throws ApplicationException{
		return buyPass.numberOfPassesAndInsertMail(numberPasses, email);
		
	}
}
