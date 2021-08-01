package business;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.persistence.DiscriminatorValue;

/**
 * Entity implementation class for Entity: InstalacaoAssentos
 *
 */
@Entity
@DiscriminatorValue(value = "Assentos")
public class InstalacaoAssentos extends Instalacao implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@OneToMany
	private List<Lugar> lugares;

	public InstalacaoAssentos() {
		super();
	}
	
	@Override
	public List<Lugar> getLugares() {
		return lugares;
	}

	public void setLugares(List<Lugar> lugares) {
		this.lugares = lugares;
	}

	@Override
	public String getType() {
		return "TipoLugares.SENTADO";
	}
	
	@Override
	public int getOcupacao() {
		return lugares.size();
	}

	public String helpString(){
		StringBuilder result = new StringBuilder();
		result.append(super.toString());
		if(lugares != null)
			result.append(" | Lugares: " + lugares.size());
		else
			result.append(" | Lugares: null");
		return result.toString();
	}

	public int hashString(){
		return helpString().hashCode();
	}
   
}
