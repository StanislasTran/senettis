
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
	private Integer moisD;
	private Integer anneeD;
	private Integer moisF;
	private Integer anneeF;
	private String description;
	private Double montantParMois;
	private Integer duree;
	private Double valeur;
	private String status;
	private String type;

	// Constructeurs----------------------------------------------
	public AmmortissementEmploye(int ammortissementEmployeId, int employeId, Integer moisD, Integer anneeD,Integer moisF, Integer anneeF,String descritpion,Double montantParMois, Integer duree, Double valeur, String type, String status) {
		this(employeId,moisD, anneeD, moisF,anneeF,montantParMois, duree, valeur, type, status);
		if ((Integer) ammortissementEmployeId != null) {
			this.ammortissementEmployeId = ammortissementEmployeId;
		} else {
			throw new Error("Le ammortissementEmployeId est vide, merci de spécifier un id ou d'utiliser un autre constructeur.");
		}
		
	}
	
	public AmmortissementEmploye(int employeId, Integer moisD, Integer anneeD,Integer moisF, Integer anneeF,Double montantParMois,String description, Integer duree, Double valeur, String type, String status) {
		this(employeId,moisD, anneeD, moisF,anneeF,montantParMois, duree, valeur, type, status);
		
		this.description = description;		
	}
	
	
	public AmmortissementEmploye(int employeId, Integer moisD, Integer anneeD,Integer moisF, Integer anneeF,Double montantParMois, Integer duree, Double valeur, String type, String status) {
		// id
		if ((Integer) employeId != null) {
			this.employeId = employeId;
		} else {
			throw new Error("L'employeId est vide, merci de spécifier un id.");
		}
		
		if (moisD != null) {
			this.moisD = moisD;
		} else {
			throw new Error("Le mois de depart n'est pas spécifié.");
		}
		
		if (anneeD != null) {
			this.anneeD = anneeD;
		} else {
			throw new Error("L'annee de depart n'est pas spécifiée.");
		}
		
		if (moisF != null) {
			this.moisF = moisF;
		} else {
			throw new Error("Le mois de fin n'est pas spécifié.");
		}
		
		if (anneeF != null) {
			this.anneeF = anneeF;
		} else {
			throw new Error("L'annee de fin n'est pas spécifiée.");
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
		this.duree=duree;
		this.montantParMois = montantParMois;
		
	}

	

	// Liens avec la BDD-----------------------------------------------
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO AmmortissementEmploye(moisDepart, anneeDepart,employe,valeur,duree,type,status,moisFin,anneeFin,valeurParMois,description) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.moisD, Types.INTEGER);
		statement.setObject(2, this.anneeD, Types.INTEGER);
		statement.setObject(3, this.employeId, Types.INTEGER);
		statement.setObject(4, this.valeur, Types.DECIMAL);
		statement.setObject(5, this.duree, Types.INTEGER);
		statement.setObject(6, this.type, Types.VARCHAR);
		statement.setObject(7, this.status, Types.VARCHAR);
		statement.setObject(8, this.moisF, Types.INTEGER);
		statement.setObject(9, this.anneeF, Types.INTEGER);
		statement.setObject(10, this.montantParMois, Types.DECIMAL);
		statement.setObject(11, this.description, Types.VARCHAR);
		

		return statement.executeUpdate();
	}


	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE AmmortissementEmploye SET moisDepart=?, employe=?, duree=?, valeur=?, type=?, status=?, anneeDepart=?,moisFin=?,anneeFin=?,valeurParMois=?,description=? WHERE AmmortissementEmployeId=?";
				
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.moisD, Types.INTEGER);
		statement.setObject(2, this.employeId, Types.INTEGER);
		statement.setObject(3, this.duree, Types.INTEGER);
		statement.setObject(4, this.valeur, Types.DECIMAL);
		statement.setObject(5, this.type, Types.VARCHAR);
		statement.setObject(6, this.status, Types.VARCHAR);
		statement.setObject(7, this.anneeD, Types.INTEGER);
		statement.setObject(8, this.ammortissementEmployeId, Types.INTEGER);
		statement.setObject(9, this.moisF, Types.INTEGER);
		statement.setObject(10, this.anneeF, Types.INTEGER);
		statement.setObject(11, this.description, Types.VARCHAR);
		statement.setObject(12, this.montantParMois, Types.DECIMAL);

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
		String reqSql = "SELECT AmmortissementEmployeId,moisDepart,anneeDepart,employe,duree,valeur,type,status,moisFin,anneeFin,valeurParMois,description FROM AmmortissementEmploye WHERE AmmortissementEmployeId=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, ammortissementEmployeId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			ammortissementEmployeId = result.getInt("AmmortissementEmployeId");
			Integer moisD = result.getInt("moisDepart");
			Integer anneeD = result.getInt("anneeDepart");
			Integer moisF = result.getInt("moisFin");
			Integer anneeF = result.getInt("anneeFin");
			int employeId = result.getInt("Employe");

			Integer duree = result.getInt("duree");
			
			Double valeur = 0.0;
			if (result.getString("valeur") != null) {
				valeur = Double.parseDouble(result.getString("valeur"));
			}
			
			Double montantParMois = 0.0;
			if (result.getString("valeurParMois") != null) {
				montantParMois = Double.parseDouble(result.getString("valeurParMois"));
			}
			
			String type = result.getString("type");
			String status = result.getString("status");
			String description = result.getString("description");

			return new AmmortissementEmploye(ammortissementEmployeId, employeId,moisD, anneeD,moisF,anneeF,description,montantParMois,duree,valeur,type, status);

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
			Integer moisD = result.getInt("moisDepart");
			Integer anneeD = result.getInt("anneeDepart");
			Integer moisF = result.getInt("moisFin");
			Integer anneeF = result.getInt("anneeFin");
			int employeId = result.getInt("Employe");
			
			Integer duree = result.getInt("duree");
			
			Double valeur = 0.0;
			if (result.getString("valeur") != null) {
				valeur = Double.parseDouble(result.getString("valeur"));
			}
			
			Double montantParMois = 0.0;
			if (result.getString("valeurParMois") != null) {
				montantParMois = Double.parseDouble(result.getString("valeurParMois"));
			}
			
			String type = result.getString("type");
			String status = result.getString("status");
			String description = result.getString("description");

			allCoutEmploye.add(new AmmortissementEmploye(ammortissementEmployeId, employeId,moisD, anneeD,moisF,anneeF,description,montantParMois,duree,valeur,type, status));
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


	public Integer getDuree() {
		return duree;
	}

	public void setDuree(Integer duree) {
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

	public Integer getMoisD() {
		return moisD;
	}

	public void setMoisD(Integer moisD) {
		this.moisD = moisD;
	}

	public Integer getAnneeD() {
		return anneeD;
	}

	public void setAnneeD(Integer anneeD) {
		this.anneeD = anneeD;
	}

	public Integer getMoisF() {
		return moisF;
	}

	public void setMoisF(Integer moisF) {
		this.moisF = moisF;
	}

	public Integer getAnneeF() {
		return anneeF;
	}

	public void setAnneeF(Integer anneeF) {
		this.anneeF = anneeF;
	}

	public String getDescription() {
		if (description != null) {
			return description;
		}
		return "";
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getMontantParMois() {
		return montantParMois;
	}

	public void setMontantParMois(Double montantParMois) {
		this.montantParMois = montantParMois;
	}

	
	
	
	
}