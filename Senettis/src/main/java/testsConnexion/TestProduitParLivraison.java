package testsConnexion;

import java.sql.SQLException;

import classes.Delivery;
import classes.ProductByDelivery;

public class TestProduitParLivraison {
	public static void main (String[]args) throws SQLException {
		ProductByDelivery pl=new ProductByDelivery(2,1,"Publié");
		pl.insertDatabase();
		
		ProductByDelivery.printAllProduitParLivraison();
	}
}
