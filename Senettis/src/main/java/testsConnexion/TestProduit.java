package testsConnexion;

import java.sql.ResultSet;
import java.sql.SQLException;

import classes.Product;
import classes.Status;

public class TestProduit {
	public static void main(String[] args) throws SQLException {
		Product p = new Product("Dr Papper", 3.5, "Coca à l'amande", Status.PUBLISHED);
		p.insertDatabase();
		// p.deleteDatabase(1);

	
		Product.printAllProduct();
	}
}
