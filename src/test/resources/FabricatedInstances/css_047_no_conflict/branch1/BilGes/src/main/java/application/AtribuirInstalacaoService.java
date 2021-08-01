package application;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalDouble;

import business.Instalacao;
import business.handlers.AtribuirInstalacaoHandler;
import facade.dto.InstalacaoDTO;
import facade.exceptions.AGApplicationException;

public class AtribuirInstalacaoService {
	
	private AtribuirInstalacaoHandler atribuirInstalacaoHandler;


	public AtribuirInstalacaoService(AtribuirInstalacaoHandler atribuirInstalacaoHandler) {
		this.atribuirInstalacaoHandler = atribuirInstalacaoHandler;
	}
	
	public Iterable<InstalacaoDTO> atribuirInstalacao() throws AGApplicationException {
		List<InstalacaoDTO> result = new LinkedList<>(); 
		for (Instalacao instalacao : atribuirInstalacaoHandler.atribuirInstalacao()) {
			result.add(new InstalacaoDTO(instalacao.getId(), instalacao.getNome(), instalacao.getType()));
		}
		return result;
	}
	

	public void darInfoAtribuicao(String nomeEvento, String nomeInstalacao, Date dataInicioVenda, double precoIndividual, OptionalDouble precoPasse) throws AGApplicationException {
		atribuirInstalacaoHandler.darInfoAtribuicao(nomeEvento, nomeInstalacao, dataInicioVenda, precoIndividual, precoPasse);
	}

}
