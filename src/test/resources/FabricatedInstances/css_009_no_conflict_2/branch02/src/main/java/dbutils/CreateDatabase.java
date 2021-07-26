package dbutils;

import java.util.HashMap;

import javax.persistence.Persistence;

public class CreateDatabase {

	/**
	 * An utility class should not have public constructors
	 */
	private CreateDatabase () {
	}
	
	public static void main(String[] args) {
		HashMap<String, String> properties = new HashMap<>();
		properties.put("javax.persistence.schema-generation.database.action", "drop-and-create");
		properties.put("javax.persistence.schema-generation.create-source", "metadata");
		properties.put("javax.persistence.schema-generation.drop-source", "metadata");
		properties.put("javax.persistence.sql-load-script-source", "META-INF/load-script.sql"); 

   	 	Persistence.generateSchema("domain-model-jpa", properties);
	}
}
