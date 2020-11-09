package connexion;


public class SQLDatabaseConnection {
	
	private String connectionUrl = "jdbc:sqlserver://localhost:1433;" + "database=SenettisDB;" + "user=laetitia;"
			+ "password=laeti;" + "encrypt=false;" + "trustServerCertificate=false;" + "loginTimeout=30;";
	
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