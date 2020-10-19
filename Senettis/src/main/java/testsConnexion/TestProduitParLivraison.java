package testsConnexion;

import java.sql.SQLException;

import classes.Livraison;
import classes.ProduitParLivraison;

public class TestProduitParLivraison {
	public static void main (String[]args) throws SQLException {
		ProduitParLivraison pl=new ProduitParLivraison(1,1);
		pl.insertDatabase();
	}
}
