package classes;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import connexion.SQLDatabaseConnection;

public class Affectation {

	private Integer idChantier;
	private Integer idEmploye;
	private Double nombreHeures;


	
	public Integer getIdChantier() {
		return idChantier;
	}



	public void setIdChantier(Integer idChantier) {
		this.idChantier = idChantier;
	}



	public Integer getIdEmploye() {
		return idEmploye;
	}



	public void setIdEmploye(Integer idEmploye) {
		this.idEmploye = idEmploye;
	}



	public Double getNombreHeures() {
		return nombreHeures;
	}



	public void setNombreHeures(Double nombreHeures) {
		this.nombreHeures = nombreHeures;
	}

	

	public Affectation(Integer idChantier, Integer idEmploye) {
		super();
		this.idChantier = idChantier;
		this.idEmploye = idEmploye;
	}



	public Affectation(Integer idChantier, Integer idEmploye, Double nombreHeures) {
		super();
		this.idChantier = idChantier;
		this.idEmploye = idEmploye;
		this.nombreHeures = nombreHeures;
	}



	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO Affectation(chantier,employe,nombre_heures) VALUES (?,?,?)";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1,this.idChantier,Types.INTEGER);
		statement.setObject(2,this.idEmploye,Types.INTEGER);
		statement.setObject(3,this.nombreHeures,Types.DECIMAL);
		
		return statement.executeUpdate();
	}

}
