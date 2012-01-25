package ca.openstudent.model;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Controls access to the EntityManagerFactory
 *
 */

public class EMF 
{
	/**
	 * When giving demos, it's often useful to have an in-memory database
	 * that doesn't require a lot of configuration and that wipes itself
	 * after each use so that testers can freely play with the data 
	 * without worrying they'll affect someone else. This flag enables
	 * that feature.
	 */
	static boolean isUseSessionDemoDB = false;
	
	/**
	 * Singleton EntityManagerFactory for accessing the main database 
	 */
	static EntityManagerFactory emf = null;
	
	/**
	 * Creates the default EntityManagerFactory for connecting to databases
	 */
	private static synchronized void createEMF()
	{
		if (emf != null) return;
		emf = Persistence.createEntityManagerFactory("openStudent");
	}
	
	/**
	 * Returns the EntityManagerFactory that should be used for getting
	 * entities from the database
	 */
	public static EntityManagerFactory getEMF()
	{
		if (isUseSessionDemoDB)
		{
			return SessionEMF.instance().getEMF();
		}
		else
		{
			if (emf == null)
				createEMF();
			return emf;
		}
	}
}
