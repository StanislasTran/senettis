package testsConnexion;

import java.sql.SQLException;

import classes.Affectation;

public class TestAffectation {
	public static void main (String[]args) throws SQLException {
		Affectation a=new Affectation(1,4);
		a.insertDatabase();
	}
}
