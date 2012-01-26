package ca.openstudent.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ca.openstudent.model.db.Db;

public class StudentRegistry 
{
	/**
	 * Warning: This method exists for debugging and testing purposes only
	 */
	public static List<Student> getStudentList() 
	{
		return Db.query(new Db.Query() {
			public List<Student> execute(EntityManager em) {
				Query query = em.createNamedQuery("student.getAll");
				return query.getResultList();
			}
		});
	}

	/**
	 * Find a student with the given id
	 */
	public static Student find(final long id) 
	{
		return Db.query(new Db.Query() {
			public Student execute(EntityManager em) {
				return (Student) em.find(Student.class, Long.valueOf(id));
			}
		});
	}
	
	/**
	 * Merge student changes back into the database
	 */
	public static void merge(final Student student)
	{
		Db.update(new Db.Update() {
			public void execute(EntityManager em) {
				em.merge(student);
			}
		});
	}

	public static List<Student> findByName(final String name) 
	{
		return Db.query(new Db.Query() {
			public List<Student> execute(EntityManager em) {
				Query query = em.createNamedQuery("student.findByName");
				query.setParameter("name", "%" + name);
				return query.getResultList();
			}
		});
	}
	
	public static List<Student> findByGender(final String gender) 
	{
		return Db.query(new Db.Query() {
			public List<Student> execute(EntityManager em) {
				Query query = em.createNamedQuery("student.findByGender");
				query.setParameter("gender", gender);
				return query.getResultList();
			}
		});
	}
	
	public static Collection<String> findNamesByName(final String name) 
	{
		// WARNING: This seems like it could be a really slow query and hard on the database
		//   if this query is used a lot. This should probably be cached somehow
		return Db.query(new Db.Query() {
			public Collection<String> execute(EntityManager em) {
				Query query = em.createNamedQuery("student.findByGender");
				query.setParameter("name", name + "%");
				List<Student> students = query.getResultList();
				Set<String> nameList = new HashSet<String>();
				for (Student s: students)
				{
					String tempName = null;
					if( s.getLegalFirstName().toLowerCase().startsWith(name.toLowerCase())) {
						tempName = s.getLegalFirstName();
					}
					else if( s.getLegalLastName().toLowerCase().startsWith(name.toLowerCase())) {
						tempName = s.getLegalLastName();
					}
					else if( s.getLegalMiddleName().toLowerCase().startsWith(name.toLowerCase())) {
						tempName = s.getLegalMiddleName();
					}
					else if( s.getUsualLastName().toLowerCase().startsWith(name.toLowerCase())) {
						tempName = s.getUsualLastName();
					}
					else if( s.getUsualFirstName().toLowerCase().startsWith(name.toLowerCase())) {
						tempName = s.getUsualFirstName();
					}
					
					if (tempName == null) continue;
					if( !nameList.contains(tempName)) {
						//Only add if not already in list or if not empty
						nameList.add(tempName);
					}
				}
				return nameList;
			}
		});
	}
}
