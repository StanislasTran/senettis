
package classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.microsoft.sqlserver.jdbc.StringUtils;

import connexion.SQLDatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AmmortissementEmploye {

	private Integer ammortissementEmployeId;
	private Integer employeId;
	private Integer mois;
	private Integer annee;
	private Double duree;
	private Double valeur;
	private String status;
	private String type;

	// Constructeurs----------------------------------------------
	public AmmortissementEmploye(int ammortissementEmployeId, int employeId, Integer mois, Integer annee, Double duree, Double valeur, String type, String status) {
		this(employeId,mois, annee, duree, valeur, type, status);
		if ((Integer) ammortissementEmployeId != null) {
			this.ammortissementEmployeId = ammortissementEmployeId;
		} else {
			throw new Error("Le ammortissementEmployeId est vide, merci de spécifier un id ou d'utiliser un autre constructeur.");
		}
		
	}
	
	public AmmortissementEmploye(int employeId, Integer mois, Integer annee, Double duree, Double valeur, String type, String status) {
		this(employeId, mois, annee, valeur, type, status);
		
		this.duree = duree;		
	}
	
	
	public AmmortissementEmploye(int employeId, Integer mois, Integer annee, Double valeur, String type, String status) {
		// id
		if ((Integer) employeId != null) {
			this.employeId = employeId;
		} else {
			throw new Error("L'employeId est vide, merci de spécifier un id.");
		}
		
		if (mois != null) {
			this.mois = mois;
		} else {
			throw new Error("Le mois n'est pas spécifié.");
		}
		
		if (annee != null) {
			this.annee = annee;
		} else {
			throw new Error("L'annee n'est pas spécifiée.");
		}
		
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
		
		this.valeur = valeur;
		this.type = type;
		
	}

	

	// Liens avec la BDD-----------------------------------------------
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO AmmortissementEmploye(mois, annee,employe,valeur,duree,type,status) VALUES (?,?,?,?,?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.mois, Types.INTEGER);
		statement.setObject(2, this.annee, Types.INTEGER);
		statement.setObject(3, this.employeId, Types.INTEGER);
		statement.setObject(4, this.valeur, Types.DECIMAL);
		statement.setObject(5, this.duree, Types.DECIMAL);
		statement.setObject(6, this.type, Types.VARCHAR);
		statement.setObject(7, this.status, Types.VARCHAR);

		return statement.executeUpdate();
	}


	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE AmmortissementEmploye SET mois=?, employe=?, duree=?, valeur=?, type=?, status=?, annee=? WHERE AmmortissementEmployeId=?";
				
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.mois, Types.INTEGER);
		statement.setObject(2, this.employeId, Types.INTEGER);
		statement.setObject(3, this.duree, Types.DECIMAL);
		statement.setObject(4, this.valeur, Types.DECIMAL);
		statement.setObject(5, this.type, Types.VARCHAR);
		statement.setObject(6, this.status, Types.VARCHAR);
		statement.setObject(7, this.annee, Types.INTEGER);
		statement.setObject(8, this.ammortissementEmployeId, Types.INTEGER);

		return statement.executeUpdate();
	}


	private static Statement selectAllAmmortissementEmploye() throws SQLException {
		String reqSql = "SELECT * FROM AmmortissementEmploye";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}


	public static int getCountCoutEmploye() throws SQLException {
		String reqSql = "SELECT count(*) as count FROM CoutEmploye";
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


	public static AmmortissementEmploye getAmmortissementEmployeById(int ammortissementEmployeId) throws SQLException {
		String reqSql = "SELECT AmmortissementEmployeId,mois,annee,employe,duree,valeur,type,status FROM AmmortissementEmploye WHERE AmmortissementEmployeId=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, ammortissementEmployeId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			ammortissementEmployeId = result.getInt("AmmortissementEmployeId");
			Integer mois = result.getInt("mois");
			Integer annee = result.getInt("annee");
			int employeId = result.getInt("Employe");
			
			Double duree = 0.0;
			if (result.getString("duree") != null) {
				duree = Double.parseDouble(result.getString("duree"));
			}
			
			Double valeur = 0.0;
			if (result.getString("valeur") != null) {
				valeur = Double.parseDouble(result.getString("valeur"));
			}
			
			String type = result.getString("type");
			String status = result.getString("status");

			return new AmmortissementEmploye(ammortissementEmployeId, employeId,mois, annee,duree,valeur,type, status);

		} else {
			throw new SQLException("Data not found");
		}
	}

	// -----------------------------------------------
	public static List<AmmortissementEmploye> getAllAmmortissementEmploye() throws SQLException {

		ResultSet result = selectAllAmmortissementEmploye().getResultSet();
		List<AmmortissementEmploye> allCoutEmploye = new ArrayList<AmmortissementEmploye>();
		while (result.next()) {
			int ammortissementEmployeId = result.getInt("AmmortissementEmployeId");
			Integer mois = result.getInt("mois");
			Integer annee = result.getInt("annee");
			int employeId = result.getInt("Employe");
			
			Double duree = 0.0;
			if (result.getString("duree") != null) {
				duree = Double.parseDouble(result.getString("duree"));
			}
			
			Double valeur = 0.0;
			if (result.getString("valeur") != null) {
				valeur = Double.parseDouble(result.getString("valeur"));
			}
			
			String type = result.getString("type");
			String status = result.getString("status");

			allCoutEmploye.add(new AmmortissementEmploye(ammortissementEmployeId, employeId,mois, annee,duree, valeur, type, status));

		}

		return allCoutEmploye;
	}

	public static void printAllAmmortissementEmploye() throws SQLException {

		List<AmmortissementEmploye> allCoutEmploye = getAllAmmortissementEmploye();

		for (AmmortissementEmploye coutEmploye : allCoutEmploye)
			System.out.println(coutEmploye);
	}


	// Getter and setter-----------------------------------------------
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if (status != null) {
			if (status == "Publié" || status == "publié") {
				this.status = "Publié";
			} else if (status == "Archivé" || status == "archivé") {
				this.status = "Archivé";
			} else if (status == "Draft" || status == "draft") {
				this.status = "Draft";
			} else {
				throw new Error("Le status indiqué est incorrect, le status doit être publié, archivé ou draft.");
			}
		} else {
			throw new Error("Le status indiqué est vide.");
		}
	}

	public int getEmployeId() {
		return employeId;
	}

	public void setEmployeId(Integer employeId) {
		if (employeId == null) {
			throw new Error("setEmployeId : le employeId indique est vide");
		}
		this.employeId = employeId;
	}

	public Integer getAmmortissementEmployeId() {
		return ammortissementEmployeId;
	}

	public void setAmmortissementEmployeId(Integer ammortissementEmployeId) {
		this.ammortissementEmployeId = ammortissementEmployeId;
	}

	public Integer getMois() {
		return mois;
	}

	public void setMois(Integer mois) {
		this.mois = mois;
	}

	public Integer getAnnee() {
		return annee;
	}

	public void setAnnee(Integer annee) {
		this.annee = annee;
	}

	public Double getDuree() {
		return duree;
	}

	public void setDuree(Double duree) {
		this.duree = duree;
	}

	public Double getValeur() {
		return valeur;
	}

	public void setValeur(Double valeur) {
		this.valeur = valeur;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	
	
	
}