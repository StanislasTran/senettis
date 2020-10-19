package connexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLDatabaseConnection {
	
	
	private String connectionUrl = "jdbc:sqlserver://localhost:1433;" + "database=senettis;" + "user=senettis;"
			+ "password=pswd;" + "encrypt=false;" + "trustServerCertificate=false;" + "loginTimeout=30;";
	
	
	/**
	 * getter for the variable connectionUrl
	 * @return connection Url in String
	 */
	public String getConnectionUrl () {
		return this.connectionUrl;
	}
	
	
	
	
	public void main(String [] args) {
		
	}
	
}