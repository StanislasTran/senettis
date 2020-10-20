package testsConnexion;

import java.sql.ResultSet;
import java.sql.SQLException;

import classes.Produit;

public class TestProduit {
	public static void main(String[] args) throws SQLException {
		Produit p = new Produit("Dr Papper", 3.5, "Coca à l'amande", "Publié");
		p.insertDatabase();
		// p.deleteDatabase(1);

	
		Produit.printAllProduct();
	}
}
