package testsConnexion;

import java.sql.SQLException;

import classes.Employe;

public class TestEmploye {
	public static void main (String[]args) throws SQLException {
		Employe e=new Employe("Mme","Alyssa","Pepper",1234);
		e.insertDatabase();
	}
}
