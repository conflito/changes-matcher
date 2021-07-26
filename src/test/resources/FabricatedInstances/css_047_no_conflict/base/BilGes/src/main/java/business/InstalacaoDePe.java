package business;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.persistence.*;
import javax.persistence.DiscriminatorValue;

/**
 * Entity implementation class for Entity: InstalacaoDePe
 *
 */
@Entity
@DiscriminatorValue(value = "DePe")
public class InstalacaoDePe extends Instalacao implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false)
	private int capacidade;

	public InstalacaoDePe() {
		super();
	}
	
	@Override
	public TipoLugares getType() {
		return TipoLugares.EM_PE;
	}
	
	@Override
	public int getOcupacao() {
		return this.capacidade;
	}

	@Override
	public List<Lugar> getLugares() {
		return Collections.<Lugar>emptyList();
	}
   
   
}
