package testsConnexion;

import java.sql.ResultSet;
import java.sql.SQLException;

import classes.Product;

public class TestProduit {
	public static void main(String[] args) throws SQLException {
		Product p = new Product("Dr Papper", 3.5, "Coca à l'amande", "Publié");
		p.insertDatabase();
		// p.deleteDatabase(1);

	
		Product.printAllProduct();
	}
}
