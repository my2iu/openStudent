package ca.openstudent.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ca.openstudent.Student;
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

}
