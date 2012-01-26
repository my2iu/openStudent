package ca.openstudent.model.db;

import javax.persistence.EntityManager;

/**
 * All database transactions flow through the methods of
 * this class so that transactions are properly handled
 */
public class Db 
{
	/**
	 * Call update to perform an update on the database within a transaction
	 */
	public static void update(Update tx)
	{
		EntityManager em = EMF.getEMF().createEntityManager();
		try {
			em.getTransaction().begin();
			tx.execute(em);
			em.getTransaction().commit();
		} catch (RuntimeException e) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			// TODO Log exceptions
			throw e;  // It might be better to wrap this exception, or maybe not
		} finally {
			em.close();
		}
	}

	/**
	 * Call query to perform an query on the database within a transaction
	 */
	public static <T> T query(Query tx)
	{
		T result = null;
		EntityManager em = EMF.getEMF().createEntityManager();
		try {
			em.getTransaction().begin();
			result = tx.execute(em);
			em.getTransaction().commit();
		} catch (RuntimeException e) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			// TODO Log exceptions
			throw e;  // It might be better to wrap this exception, or maybe not
		} finally {
			em.close();
		}
		return result;
	}

	/**
	 * Update transactions should be denoted with this interface
	 */
	public static interface Update
	{
		public void execute(EntityManager em);
	}

	/**
	 * Transactions that do queries should be denoted with this interface
	 */
	public static interface Query
	{
		public <T> T execute(EntityManager em);		
	}
}
