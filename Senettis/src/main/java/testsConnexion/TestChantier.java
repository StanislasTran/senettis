package testsConnexion;

import java.sql.SQLException;

import classes.Site;
import classes.Status;

public class TestChantier {
	public static void main (String[]args) throws SQLException {
		Site c=new Site("Chantier1",Status.PUBLISHED);
		c.insertDatabase();
		
		Site.printAllSite();
	}
}
