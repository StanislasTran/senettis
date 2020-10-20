package classes;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import connexion.SQLDatabaseConnection;

public class Chantier {

	private String nom;
	private String adresse;
	private Double CA;
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public Double getCA() {
		return CA;
	}

	public void setCA(Double CA) {
		this.CA = CA;
	}

	public Chantier(String nom, String adresse, Double CA,String status) {
		super();	
		this.nom = nom;
		this.adresse = adresse;
		this.CA = CA;
		this.status=status;
	}

	public Chantier(String nom,String status) {
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

}
