package senettis;

import java.sql.SQLException;

public class Test {
	public static void main (String[]args) throws SQLException {
		Produit p=new Produit("Dr Papper",3.5,"Coca à l'amande");
		p.insertDatabase();
	}
}
