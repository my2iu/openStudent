package ca.openstudent.model;

import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.persistence.config.PersistenceUnitProperties;


/**
 * For debug purposes, it's often useful to have a different
 * in-memory database per session. This class manages that
 */
@SessionScoped
public class SessionEMF 
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
		String jdbcString = "derby:memory:" + getUniqueDatabaseID() + ";create=true";
		String jdbcDriver = "org.apache.derby.jdbc.EmbeddedDriver";
		
		if (emf != null) return;

		// Create a database to use for testing
		createDatabase(jdbcString, jdbcDriver);
		createDatabaseSampleData(jdbcString, jdbcDriver);
		
		// Create a custom connection that connects to the temporary test database
		// for this session
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(PersistenceUnitProperties.JDBC_DRIVER, jdbcDriver);
		properties.put(PersistenceUnitProperties.JDBC_URL, jdbcString);
		emf = Persistence.createEntityManagerFactory("openStudent", properties);
	}
	
	/** 
	 * Manually creates the tables for a sample OpenStudent database
	 */
	public void createDatabase(String jdbcString, String jdbcDriver) 
	{
		
		// TODO Auto-generated method stub
		
	}
	/** 
	 * Manually creates some sample data for a sample OpenStudent database
	 */
	private void createDatabaseSampleData(String jdbcString, String jdbcDriver) 
	{
		// TODO Auto-generated method stub
		
	}


	/**
	 * Returns an EntityManagerFactory that connects to an in-memory database
	 * used for this session only
	 */
	public EntityManagerFactory getEMF()
	{
		return emf;
	}
	
	/**
	 * @return the instance of this class specific to the 
	 *    current session (creating one if necessary)
	 */
	public static SessionEMF instance()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		SessionEMF toReturn = (SessionEMF)context.getExternalContext().getSessionMap()
				.get("sessionEMF");
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
	private static int dbId = 1;
}
