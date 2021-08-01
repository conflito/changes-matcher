package dbutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

public class RunSQLScript {

	public void runScript (EntityManagerFactory emf, String scriptFilename) throws IOException, SQLException {
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream(scriptFilename)))) {
		    String command;
		    int i = 1;
		    EntityManager em = emf.createEntityManager();
		    while ((command = br.readLine()) != null) {
		        System.out.println(i + ": " + command);
		        i++;
			    em.getTransaction().begin();
		        Query q = em.createNativeQuery(command);
		        q.executeUpdate();
		        em.getTransaction().commit();
		    }
		    em.close();
		}
	}
	
}
