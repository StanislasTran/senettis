
package classes;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import connexion.SQLDatabaseConnection;

public class SickLeave {

	private Integer idSickLeave;
	private Integer idEmploye;
	private String dateDebut;
	private String motive;
	private String duration;
	private double cost;
	private String status;
	
	
	public SickLeave(Integer idEmploye, String dateDebut, String status) {
		super();
		this.idEmploye = idEmploye;
		
		if (status != null) {
			if (!status.isEmpty()) {
				if (status.equals("Publié") || status.equals("publié")) {
					this.status = "Publié";
				} else if (status.equals("Archivé") || status.equals("archivé"))  {
					this.status = "Archivé";
				} else if (status.equals("Draft") || status.equals("draft")) {
					this.status = "Draft";
				} else {
					throw new Error("Le status indiqué est incorrect, le status doit être publié, archivé ou draft.");
				}
			} else {
				this.status = null;
			}
		}
		
		// date debut
		if (dateDebut != null) {
			if (!(dateDebut.isEmpty())) {
				if ( dateDebut.charAt(2) =='/' || dateDebut.charAt(2) == '-' || dateDebut.charAt(2) == '_') {
					//System.out.println("fr");
					// date francaise
					// on reecrit en format francais juste pour s'assurer que toutes les dates
					// seront ecrites avec le meme format jj/mm/aaaa
					dateDebut = dateDebut.substring(0, 2) + "/" + dateDebut.substring(3, 5) + "/"
							+ dateDebut.substring(6, 10);
				}
				else if ( dateDebut.charAt(7) =='/' || dateDebut.charAt(7) == '-' || dateDebut.charAt(7) == '_') {
					//System.out.println("en");
					// date anglaise
					dateDebut = dateDebut.substring(8, 10) + "/" + dateDebut.substring(5, 7) + "/"
							+ dateDebut.substring(0, 4);
				} else {
					throw new Error(
							"La date d'arrivée indiquée est incorrecte, une date doit être indiqué selon un des formats suivant : 31-01-2000, 31/01/2000, 2000-01-31 ou 2000/01/31.");
				}
			}
			else {
				dateDebut = null;
			}
		}
		this.dateDebut = dateDebut;		
		
		
	}

	public SickLeave(Integer idSickLeave, Integer idEmploye, String dateDebut, String status) {
		this(idEmploye, dateDebut,status);
		this.idSickLeave = idSickLeave;
	}

	public SickLeave(Integer idSickLeave, Integer idEmploye, String dateDebut, String motive, String duree, Double cost, String status) {
		this(idSickLeave, idEmploye, dateDebut, status);
		this.motive = motive;
		this.duration = duree;
		this.cost = cost;		
	}


	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO ArretMaladie(date_de_debut,employe,duree,motif,cout,status) VALUES (?,?,?,?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.dateDebut, Types.DATE);
		statement.setObject(2, this.idEmploye, Types.INTEGER);
		statement.setObject(3, this.duration, Types.VARCHAR);
		statement.setObject(4, this.motive, Types.VARCHAR);
		statement.setObject(5, this.cost, Types.DECIMAL);
		statement.setObject(6, this.status, Types.VARCHAR);

		return statement.executeUpdate();
	}

	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE ArretMaladie SET Employe=?, date_de_debut=?, duree=?, motive=?, cost=?,status=? WHERE AMId=?;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.idEmploye, Types.INTEGER);
		statement.setObject(2, this.dateDebut, Types.VARCHAR);
		statement.setObject(3, this.duration, Types.VARCHAR);
		statement.setObject(4, this.motive, Types.VARCHAR);
		statement.setObject(5, this.cost, Types.DECIMAL);
		statement.setObject(6, this.status, Types.VARCHAR);
		statement.setObject(7, this.idSickLeave, Types.INTEGER);

		return statement.executeUpdate();
	}

	private static Statement selectAllSickLeave() throws SQLException {
		String reqSql = "SELECT * FROM ArretMaladie";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}

	public static List<SickLeave> getAllSickLeave() throws SQLException {


		ResultSet result = selectAllSickLeave().getResultSet();
		List<SickLeave> allSickLeave = new ArrayList<SickLeave>();
		
		while (result.next()) {
			int idSickLeave = result.getInt("AMId");
			int employeId = result.getInt("Employe");
			String dateDebut = result.getString("date_de_debut");
			String duree = result.getString("duree");
			String motive = result.getString("motif");
			Double cost = result.getDouble("cout");
			String status = result.getString("Status");
			allSickLeave.add(new SickLeave(idSickLeave, employeId, dateDebut, motive, duree, cost, status));


		}

		return allSickLeave;
	}
	
	
	public static SickLeave getSickLeaveById(int sickLeaveId) throws SQLException {
		String reqSql = "SELECT AMId,Employe,date_de_debut,duree,motif,cout,Status FROM ArretMaladie WHERE AMId=?;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, sickLeaveId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			sickLeaveId = result.getInt("AMId");
			Integer employeId = result.getInt("Employe");
			String date = result.getString("date_de_debut");
			String duration = result.getString("duree");
			String motive = result.getString("motif");
			
			Double cost = 0.0;
			if (result.getString("cout") != null) {
				cost = Double.parseDouble(result.getString("cout"));
			}

			String status = result.getString("status");

			return new SickLeave(sickLeaveId, employeId, date, duration, motive, cost, status);

		} else {
			throw new SQLException("Data not found");
		}
	}

	public static void printAllSickLeave() throws SQLException {

		List<SickLeave> allSickLeave = getAllSickLeave();

		for (SickLeave sl : allSickLeave)
			System.out.println(sl);
	}

	@Override
	public String toString() {

		return "" + this.idSickLeave + "|" + this.idEmploye + "|" + this.duration + "|" + this.motive + "|"+ this.duration + "|"+ this.cost + "|"
				+ this.status;
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

	public Integer getIdSickLeave() {
		return idSickLeave;
	}

	public void setIdSickLeave(Integer idSickLeave) {
		if (idSickLeave == null) {
			throw new Error("setIdSickLeave : le idSickLeave indique est vide");
		}
		this.idSickLeave = idSickLeave;
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

	public String getDateDebut() {
		if (dateDebut == null) {
			return "";
		}
		return dateDebut;
	}

	public void setDateDebut(String dateDebut) {
		if (dateDebut == null) {
			throw new Error("setDateDebut : le dateDebut indique est vide");
		}
		this.dateDebut = dateDebut;
	}

	public String getMotive() {
		if (motive == null) {
			return "";
		}
		return motive;
	}

	public void setMotive(String motive) {
		if (motive == null) {
			throw new Error("setMotive : le motive indique est vide");
		}
		this.motive = motive;
	}

	public String getDuree() {
		if (duration == null) {
			return "";
		}
		return duration;
	}

	public void setDuration(String duration) {
		if (duration == null) {
			throw new Error("setDuration : le duration indique est vide");
		}
		this.duration = duration;
	}

	public Double getCost() {
		if ((Double)cost == null) {
			return 0.0;
		}
		return cost;
	}

	public void setCost(Double cost) {
		if (cost == null) {
			throw new Error("setCost : le cost indique est vide");
		}
		this.cost = cost;
	}

	
}
