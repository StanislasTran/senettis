package testsConnexion;

import java.sql.SQLException;

import classes.Employe;
import classes.Product;

public class TestEmploye {
	public static void main (String[]args) throws SQLException {
		//Employe e=new Employe("M.","T","Stan",488,"20/10/2020","Publi�");
		//e.insertDatabase();
		
		Employe.printAllEmploye(); 
	}
}
