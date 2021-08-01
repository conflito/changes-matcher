package business.entities;

import java.lang.String;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class Manager {

	   
	@Id @GeneratedValue
	private int id;
	
	private String name;
	
	public Manager() {
		super();
	}
	
	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
   
	
}
