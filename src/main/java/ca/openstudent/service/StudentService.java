package ca.openstudent.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import ca.openstudent.dao.impl.StudentDaoImpl;
import ca.openstudent.model.Student;
import ca.openstudent.model.StudentRegistry;

public class StudentService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	StudentDaoImpl studentDAO = StudentDaoImpl.Factory.getInstance();//.getInstance();
		
    public Long create(Student Student)
    {
        if(Student == null)
        {
            throw new RuntimeException("Unable to create Student. Student object is null.");
        }
        StudentRegistry.persist(Student);
       
        return Student.getId();
    }

    public void delete(Student student)
    {
        if(student == null)
        {
            throw new RuntimeException("Unable to delete Student. Student object is null.");
        }
        StudentRegistry.delete(student);
    }

    public Collection<Student> getAllStudents()
    {
        return null;//studentDAO.getAll();
    }

    public Student getStudent(Long StudentId)
    {
    	return StudentRegistry.find(StudentId);
    }

    public Collection<Student> searchStudents(String Studentname)
    {
        String searchCriteria = (Studentname == null)? "":Studentname.toLowerCase().trim();
      //  Collection<Student> Students = studentDAO.values();
        Collection<Student> searchResults = new ArrayList<Student>();
       // for (Student Student : Students)
        {
           // if(Student.getStudentname() != null && Student.getStudentname().toLowerCase().trim().startsWith(searchCriteria))
            {
             //   searchResults.add(Student);
            }
        }
        return searchResults;
    }

    public void update(Student Student)
    {
      //  if(Student == null || !studentDAO.containsKey(Student.getId()))
        {
            throw new RuntimeException("Unable to update Student. Student object is null or Student Id ["+Student.getId()+"] is invalid." );
        }
      //  StudentS_TABLE.put(Student.getId(), Student);
    }
 
    protected Long getMaxStudentId()
    {
        Set<Long> keys = null;//StudentS_TABLE.keySet();
        Long maxId = 1L;
        for (Long key : keys)
        {
            if(key > maxId)
            {
                maxId = key;
            }
        }
        return maxId;
    }

	public Student find(String Studentname, String password) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Find Students with the name.
	 * Returns a List of names found.
	 * 
	 * @param name
	 * @return
	 */
	public Collection<String> findByName(String name) {
		return StudentRegistry.findNamesByName(name);
		
	}
	
	public List<Student> findStudentsByName(String name) {
		return this.searchStudents(name, null);
		
	}

	
	public List<Student> searchStudents(String name, String gender) {
		if (gender == null || "".equals(gender))
			return 	StudentRegistry.findByName(name);
		else
			return 	StudentRegistry.findByNameAndGender(name, gender);
	}
    
    
    
}
