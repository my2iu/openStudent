package ca.openstudent.model.db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.eclipse.persistence.config.PersistenceUnitProperties;

import ca.openstudent.Student;


/**
 * For debug purposes, it's often useful to have a different
 * in-memory database per session. This class manages that
 */
@ManagedBean
@SessionScoped
public class SessionEMF implements Serializable
{
	/**
	 * Singleton EntityManagerFactory for accessing the per-session
	 * in-memory database 
	 */
	EntityManagerFactory emf = null;
	
	/**
	 * Creates an in-memory database that can be used for testing and
	 * demo purposes
	 */
	private synchronized void createEMF()
	{
		String jdbcString = "jdbc:derby:memory:" + getUniqueDatabaseID() + ";create=true";
		String jdbcDriver = "org.apache.derby.jdbc.EmbeddedDriver";
		
		if (emf != null) return;

		// TODO For now, I'll just let JPA create the database automatically,
		//   but it might be better to create it manually in order to make sure
		//   that all the settings and indices are set correctly.
//		// Create a database to use for testing
//		try {
//			try { Class.forName(jdbcDriver); } catch (ClassNotFoundException e) {}
//			
//			Connection con = DriverManager.getConnection(jdbcString);
//			createDatabase(con);
//			con.close();
//		} catch (SQLException e)
//		{
//			// Wrap the SQLException in a PersistenceException so that the error
//			// occurs at the same level as a normal exception triggered by 
//			// the persistence layer
//			throw new PersistenceException(e);
//		}
		
		// Create a custom connection that connects to the temporary test database
		// for this session
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(PersistenceUnitProperties.JDBC_DRIVER, jdbcDriver);
		properties.put(PersistenceUnitProperties.JDBC_URL, jdbcString);
		properties.put(PersistenceUnitProperties.JDBC_USER, null);
		properties.put(PersistenceUnitProperties.JDBC_PASSWORD, null);
		emf = Persistence.createEntityManagerFactory("openStudent", properties);
		
		// Put some sample data in the database so that there's something available
		// to test with
		createDatabaseSampleData(emf.createEntityManager());
	}
	
	/** 
	 * Manually creates some sample data for a sample OpenStudent database
	 */
	private void createDatabaseSampleData(EntityManager em)  
	{
		Student s1 = new Student();
		s1.setLegalFirstName("John");
		s1.setLegalLastName("Clark");

		Student s2 = new Student();
		s2.setLegalFirstName("Alice");
		s2.setLegalLastName("Wong");
		
		em.getTransaction().begin();
		em.persist(s1);
		em.persist(s2);
		em.getTransaction().commit();
		em.close();
	}


	/**
	 * Returns an EntityManagerFactory that connects to an in-memory database
	 * used for this session only
	 */
	public EntityManagerFactory getEMF()
	{
		if (emf == null)
			createEMF();
		return emf;
	}
	
	/**
	 * @return the instance of this class specific to the 
	 *    current session (creating one if necessary)
	 */
	public static SessionEMF instance()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		SessionEMF toReturn = (SessionEMF)context.getExternalContext().getSessionMap().get("sessionEMF");
		if (toReturn == null)
			toReturn = context.getApplication().evaluateExpressionGet(context, "#{sessionEMF}", SessionEMF.class);
		return toReturn;
	}
	
	/**
	 * To have a different database per id, we need a different
	 * identifying string for each database. This method will 
	 * generate one.
	 */
	private synchronized static String getUniqueDatabaseID()
	{
		dbId++;
		return "db" + Integer.toString(dbId);
	}
	private static int dbId = 0;
}
