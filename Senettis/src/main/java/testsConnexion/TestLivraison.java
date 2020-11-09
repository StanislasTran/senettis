package testsConnexion;

import java.sql.SQLException;

import classes.Delivery;

public class TestLivraison {
	public static void main (String[]args) throws SQLException {
		Delivery l=new Delivery(1,1,5.0,"19/10/2020","Publié");
		l.insertDatabase();
		
		Delivery.printAllLivraison();
	}
}
