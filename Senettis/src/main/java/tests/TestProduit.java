package tests;

import java.sql.SQLException;

import classes.Produit;

public class TestProduit {
	public static void main (String[]args) throws SQLException {
		Produit p=new Produit("Dr Papper",3.5,"Coca à l'amande");
		//p.insertDatabase();
		//p.deleteDatabase(1);
	}
}
