package testsConnexion;

import java.sql.SQLException;

import classes.Employee;
import classes.SickLeave;

public class TestSickLeave {
	public static void main (String[]args) throws SQLException {
		int employeeid = Employee.getAllEmploye().get(0).getEmployeId();
		
		SickLeave sl=new SickLeave(employeeid,"10/05/2000", "Publié");
		sl.insertDatabase();
		
	}
}
