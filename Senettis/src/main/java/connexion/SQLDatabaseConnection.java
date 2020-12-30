package connexion;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLDatabaseConnection {
	
	private String connectionUrl = "jdbc:sqlserver://localhost:1433;" + "database=SenettisDB;" + "user=laetitia;"
			+ "password=laeti;" + "encrypt=false;" + "trustServerCertificate=true;" + "loginTimeout=30;";
	
	/**
	 * getter for the variable connectionUrl
	 * @return connection Url in String
	 * @throws SQLException 
	 */
	public String getConnectionUrl () throws SQLException {
	
		return this.connectionUrl;
	}
	
	
	
	
	public void main(String [] args) {
		
	}
	
}