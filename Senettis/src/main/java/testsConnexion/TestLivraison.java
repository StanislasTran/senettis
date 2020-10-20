package testsConnexion;

import java.sql.SQLException;

import classes.Livraison;

public class TestLivraison {
	public static void main (String[]args) throws SQLException {
		Livraison l=new Livraison(1,1,"19/10/2020","Publié");
		l.insertDatabase();
	}
}
