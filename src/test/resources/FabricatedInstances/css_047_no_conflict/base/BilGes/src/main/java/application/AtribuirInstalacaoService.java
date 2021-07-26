package application;

import java.util.Date;
import java.util.OptionalDouble;

import business.handlers.AtribuirInstalacaoHandler;
import facade.exceptions.ApplicationException;

public class AtribuirInstalacaoService {
	
	private AtribuirInstalacaoHandler atribuirInstalacaoHandler;


	public AtribuirInstalacaoService(AtribuirInstalacaoHandler atribuirInstalacaoHandler) {
		this.atribuirInstalacaoHandler = atribuirInstalacaoHandler;
	}
	
	public Iterable<String> atribuirInstalacao() throws ApplicationException {
		return atribuirInstalacaoHandler.atribuirInstalacao();
	}
	

	public void darInfoAtribuicao(String nomeEvento, String nomeInstalacao, Date dataInicioVenda, double precoIndividual, OptionalDouble precoPasse) throws ApplicationException {
		atribuirInstalacaoHandler.darInfoAtribuicao(nomeEvento, nomeInstalacao, dataInicioVenda, precoIndividual, precoPasse);
	}

}
