package testsConnexion;

import java.sql.SQLException;

import classes.Site;

public class TestChantier {
	public static void main (String[]args) throws SQLException {
		Site c=new Site("Chantier1","Publié");
		c.insertDatabase();
		
		Site.printAllChantier();
	}
}
