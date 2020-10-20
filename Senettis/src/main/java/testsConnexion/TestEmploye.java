package testsConnexion;

import java.sql.SQLException;

import classes.Employe;

public class TestEmploye {
	public static void main (String[]args) throws SQLException {
		Employe e=new Employe("M.","T","Stan",3,"20/10/2020","Publié");
		e.insertDatabase();
	}
}
