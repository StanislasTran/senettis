package testsConnexion;

import java.sql.SQLException;

import classes.Chantier;

public class TestChantier {
	public static void main (String[]args) throws SQLException {
		Chantier c=new Chantier("Chantier1","Publi�");
		c.insertDatabase();
		
		Chantier.printAllChantier();
	}
}
