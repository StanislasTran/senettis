package classes;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import connexion.SQLDatabaseConnection;

public class Site {

	private String nom;
	private String adresse;
	private Double CA;
	private String status;
	private int chantierId;

	public Site(int chantierId, String nom, String adresse, Double CA,String status) {
		this(nom,adresse,CA,status);
		
		this.chantierId = chantierId;
	}
	
	public Site(String nom, String adresse, Double CA,String status) {	
		this(nom,status);

		this.adresse = adresse;
		this.CA = CA;
	}

	public Site(String nom,String status) {
		super();	
		this.nom = nom;
		this.status=status;
	}
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO Chantier(nom,adresse,CA,status) VALUES (?,?,?,?)";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1,this.nom,Types.VARCHAR);
		statement.setObject(2,this.adresse,Types.VARCHAR);
		statement.setObject(3,this.CA,Types.DECIMAL);
		statement.setObject(4,this.status,Types.VARCHAR);
		
		return statement.executeUpdate();
	}
	
	private static Statement selectAllChantier() throws SQLException {
		String reqSql = "SELECT * FROM Chantier";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}
	
	public static List<Site> getAllChantier() throws SQLException {

		ResultSet result=selectAllChantier().getResultSet();
		List<Site> allChantier=new ArrayList<Site>();
		//System.out.println("Id|Nom|Adresse|CA|Status");
		while(result.next()) {
			int chantierId=result.getInt("ChantierId");
			String nom=result.getString("Nom");
			
			String adresse;
			if (result.getString("Adresse") != null) {
				adresse = result.getString("Adresse");
			}
			else {
				adresse = "";
			}
			
			Double CA = 0.0;
			if (result.getString("CA") != null) {
				CA = Double.parseDouble(result.getString("CA"));
			}
			
		    String status=result.getString("Status");
		   allChantier.add(new Site(chantierId, nom, adresse, CA, status));
		}
		return allChantier;
	}
	
	public static void printAllChantier() throws SQLException {
		
		List<Site> allChantier=getAllChantier();
	
		for (Site chantier : allChantier)	
			System.out.println(chantier);
	}
	
	@Override
	public String toString() {
		
		return ""+this.chantierId+"|"+this.nom+"|"+this.adresse+"|"+this.CA+"|"+this.status;
	}

	
	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE Chantier SET nom=?, adresse=?, CA=?, status=? WHERE ChantierId=?;";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1,this.nom.toString(),Types.VARCHAR);
		statement.setObject(2,this.adresse,Types.VARCHAR);
		statement.setObject(3,this.CA,Types.DECIMAL);
		statement.setObject(4,this.status,Types.VARCHAR);
		statement.setObject(5,this.chantierId,Types.INTEGER);
		
		return statement.executeUpdate();
	}
	
	/**
	 * Retourne le nombre de chantier dans la base de données
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static int getCountChantier() throws SQLException {
		String reqSql = "SELECT count(*) as count FROM Chantier";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		ResultSet result = statement.getResultSet();
		if (result.next()) {
			if (result.getInt("count") > 0) {
				return result.getInt("count");
			}
		}
		return 0;
	}
	
	
	/***
	 * @param chantierId
	 * @return le chantier correspondant à l'id indique en argument
	 * @throws SQLException
	 */
	public static Site getChantierById(int chantierId) throws SQLException {
		String reqSql = "SELECT ChantierId,nom,adresse,CA,Status FROM Chantier WHERE ChantierId=?;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, chantierId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			chantierId = result.getInt("ChantierId");
			String nom = result.getString("Nom");
			
			String adresse;
			if (result.getString("Adresse") != null) {
				adresse = result.getString("Adresse");
			}
			else {
				adresse = "";
			}
			
			Double CA = 0.0;
			if (result.getString("CA") != null) {
				CA = Double.parseDouble(result.getString("CA"));
			}

			String status = result.getString("status");

			return new Site(chantierId, nom, adresse, CA, status);

		} else {
			throw new SQLException("Data not found");
		}
	}

	
	
	
	
	

	
	
	/**
	 * getter and setter
	 */
	


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if (status == null) {
			throw new Error("setStatus : le status indique est vide");
		}
		this.status = status;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		if (nom == null) {
			throw new Error("setNom : le nom indique est vide");
		}
		this.nom = nom;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		if (adresse == null) {
			throw new Error("setAdresse : le adresse indique est vide");
		}
		this.adresse = adresse;
	}

	public Double getCA() {
		return CA;
	}

	public void setCA(Double CA) {
		if (CA == null) {
			throw new Error("setCA : le CA indique est vide");
		}
		this.CA = CA;
	}
	
	public int getChantierId() {
		return chantierId;
	}

	public void setChantierId(int chantierId) {
		this.chantierId = chantierId;
	}

	
}
