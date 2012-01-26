package ca.openstudent.bean;

import java.util.Collections;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import ca.openstudent.model.Student;
import ca.openstudent.model.StudentRegistry;

@ManagedBean
@RequestScoped
public class Debug_ListAllStudentsBacking 
{
	public List<Student> getStudentList()
	{
		return StudentRegistry.getStudentList();
	}

}
