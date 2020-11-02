
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

public class Affectation {

	private Integer idChantier;
	private Integer idEmploye;
	private Double nombreHeures;
	private String status;
	private int affectationId;
	
	
	public Affectation(Integer idChantier, Integer idEmploye,String status) {
		super();
		this.idChantier = idChantier;
		this.idEmploye = idEmploye;
		this.status=status;
	}

	public Affectation(Integer idChantier, Integer idEmploye, Double nombreHeures,String status) {
	this(idChantier,idEmploye,status);
		this.nombreHeures = nombreHeures;
	}
	
	public Affectation(Integer affectationId, Integer idChantier, Integer idEmploye, Double nombreHeures,String status) {
		this(idChantier,idEmploye,nombreHeures, status);
			this.affectationId = affectationId;
		}

	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO Affectation(chantier,employe,nombre_heures,status) VALUES (?,?,?,?)";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1,this.idChantier,Types.INTEGER);
		statement.setObject(2,this.idEmploye,Types.INTEGER);
		statement.setObject(3,this.nombreHeures,Types.DECIMAL);
		statement.setObject(4,this.status,Types.VARCHAR);
		
		return statement.executeUpdate();
	}
	

	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE Affectation SET Employe=?, Chantier=?, nombre_heures=?, status=? WHERE AffectationId=?;";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1,this.idEmploye.toString(),Types.INTEGER);
		statement.setObject(2,this.idChantier,Types.INTEGER);
		statement.setObject(3,this.nombreHeures,Types.DECIMAL);
		statement.setObject(4,this.status,Types.VARCHAR);
		statement.setObject(5,this.affectationId,Types.INTEGER);
		
		return statement.executeUpdate();
	}
	
	
	private static Statement selectAllAffectation() throws SQLException {
		String reqSql = "SELECT * FROM Affectation";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}
	
	public static List<Affectation> getAllAffectation() throws SQLException {

		ResultSet result=selectAllAffectation().getResultSet();
		List<Affectation> allAffectation=new ArrayList<Affectation>();
		System.out.println("Id|EmployeId|ChantierId|NombreHeures|Status");
		while(result.next()) {
			int affectationId=result.getInt("AffectationId");
			int employeId=result.getInt("Employe");
			int chantierId=result.getInt("Chantier");
			Double nombreHeures=result.getDouble("Nombre_heures");
		    String status=result.getString("Status");
		   allAffectation.add(new Affectation(affectationId, employeId, chantierId, nombreHeures, status));
		  
			
		}
		
		return allAffectation;
	}
	
	public static void printAllAffectation() throws SQLException {
		
		List<Affectation> allAffectation=getAllAffectation();
	
		for (Affectation affectation : allAffectation)	
			System.out.println(affectation);
	}
	
	@Override
	public String toString() {
		
		return ""+this.affectationId+"|"+this.idEmploye+"|"+this.idChantier+"|"+this.nombreHeures+"|"+this.status;
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

	public Integer getIdChantier() {
		return idChantier;
	}

	public void setIdChantier(Integer idChantier) {
		if (idChantier == null) {
			throw new Error("setIdChantier : le idChantier indique est vide");
		}
		this.idChantier = idChantier;
	}

	public Integer getIdEmploye() {
		return idEmploye;
	}

	public void setIdEmploye(Integer idEmploye) {
		if (idEmploye == null) {
			throw new Error("setIdEmploye : le idEmploye indique est vide");
		}
		this.idEmploye = idEmploye;
	}

	public Double getNombreHeures() {
		return nombreHeures;
	}

	public void setNombreHeures(Double nombreHeures) {
		if (nombreHeures == null) {
			throw new Error("setNombreHeures : le nombreHeures indique est vide");
		}
		this.nombreHeures = nombreHeures;
	}

	public int getAffectationId() {
		return affectationId;
	}

	public void setAffectationId(Integer affectationId) {
		if (affectationId == null) {
			throw new Error("setAffectationId : le AffectationId indique est vide");
		}
		this.affectationId = affectationId;
	}

}
