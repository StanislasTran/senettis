
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

public class FournitureSanitaire {

	private Integer fournitureSanitaireId;
	private Integer chantierId;
	private Integer moisD;
	private Integer anneeD;
	private String description;
	private Double montantParMois;
	private String status;
	private String sousTraitant;

	// Constructeurs----------------------------------------------
	public FournitureSanitaire(int fournitureSanitaireId, int chantierId, Integer moisD, Integer anneeD,String description,Double montantParMois,String sousTraitant, String status) {
		this(chantierId,moisD, anneeD,montantParMois, description,sousTraitant, status);
		if ((Integer) fournitureSanitaireId != null) {
			this.fournitureSanitaireId = fournitureSanitaireId;
		} else {
			throw new Error("Le fournitureSanitaireId est vide, merci de spécifier un id ou d'utiliser un autre constructeur.");
		}
	}
	
	public FournitureSanitaire(int chantierId, Integer moisD, Integer anneeD,Double montantParMois,String description, String sousTraitant, String status) {
		this(chantierId,moisD, anneeD, montantParMois, sousTraitant, status);
		
		this.description = description;		
	}
	
	
	public FournitureSanitaire(int chantierId, Integer moisD, Integer anneeD,Double montantParMois, String sousTraitant, String status) {
		// id
		if ((Integer) chantierId != null) {
			this.chantierId = chantierId;
		} else {
			throw new Error("L'chantierId est vide, merci de spécifier un id.");
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
		
		this.sousTraitant = sousTraitant;
		this.montantParMois = montantParMois;
		
	}

	

	// Liens avec la BDD-----------------------------------------------
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO FournitureSanitaire(moisDepart,anneeDepart,chantier,sousTraitant,status,valeurParMois,description) VALUES (?,?,?,?,?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.moisD, Types.INTEGER);
		statement.setObject(2, this.anneeD, Types.INTEGER);
		statement.setObject(3, this.chantierId, Types.INTEGER);
		statement.setObject(4, this.sousTraitant, Types.VARCHAR);
		statement.setObject(5, this.status, Types.VARCHAR);
		statement.setObject(6, this.montantParMois, Types.DECIMAL);
		statement.setObject(7, this.description, Types.VARCHAR);
		

		return statement.executeUpdate();
	}


	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE FournitureSanitaire SET moisDepart=?, chantier=?,sousTraitant=?, status=?, anneeDepart=?,valeurParMois=?,description=? WHERE FournitureSanitaireId=?";
				
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.moisD, Types.INTEGER);
		statement.setObject(2, this.chantierId, Types.INTEGER);
		statement.setObject(3, this.sousTraitant, Types.VARCHAR);
		statement.setObject(4, this.status, Types.VARCHAR);
		statement.setObject(5, this.anneeD, Types.INTEGER);
		statement.setObject(6, this.montantParMois, Types.DECIMAL);
		statement.setObject(7, this.description, Types.VARCHAR);
		statement.setObject(8, this.fournitureSanitaireId, Types.INTEGER);
		return statement.executeUpdate();
	}


	private static Statement selectAllFournitureSanitaire() throws SQLException {
		String reqSql = "SELECT * FROM FournitureSanitaire";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}

	public static FournitureSanitaire getFournitureSanitaireById(int fournitureSanitaireId) throws SQLException {
		String reqSql = "SELECT FournitureSanitaireId,moisDepart,anneeDepart,chantier,sousTraitant,status,valeurParMois,description FROM FournitureSanitaire WHERE FournitureSanitaireId=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, fournitureSanitaireId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			fournitureSanitaireId = result.getInt("FournitureSanitaireId");
			Integer moisD = result.getInt("moisDepart");
			Integer anneeD = result.getInt("anneeDepart");
			int chantierId = result.getInt("Chantier");

			Double montantParMois = 0.0;
			if (result.getString("valeurParMois") != null) {
				montantParMois = Double.parseDouble(result.getString("valeurParMois"));
			}
			
			String sousTraitant = result.getString("sousTraitant");
			String status = result.getString("status");
			String description = result.getString("description");

			return new FournitureSanitaire(fournitureSanitaireId, chantierId,moisD, anneeD,description,montantParMois,sousTraitant, status);

		} else {
			throw new SQLException("Data not found");
		}
	}

	// -----------------------------------------------
	public static List<FournitureSanitaire> getAllFournitureSanitaire() throws SQLException {

		ResultSet result = selectAllFournitureSanitaire().getResultSet();
		List<FournitureSanitaire> allCoutChantier = new ArrayList<FournitureSanitaire>();
		while (result.next()) {
			int fournitureSanitaireId = result.getInt("FournitureSanitaireId");
			Integer moisD = result.getInt("moisDepart");
			Integer anneeD = result.getInt("anneeDepart");
			int chantierId = result.getInt("Chantier");

			Double montantParMois = 0.0;
			if (result.getString("valeurParMois") != null) {
				montantParMois = Double.parseDouble(result.getString("valeurParMois"));
			}
			
			String sousTraitant = result.getString("sousTraitant");
			String status = result.getString("status");
			String description = result.getString("description");

			allCoutChantier.add(new FournitureSanitaire(fournitureSanitaireId, chantierId,moisD, anneeD,description,montantParMois,sousTraitant, status));
		}

		return allCoutChantier;
	}

	public static void printAllFournitureSanitaire() throws SQLException {

		List<FournitureSanitaire> allCoutChantier = getAllFournitureSanitaire();

		for (FournitureSanitaire coutChantier : allCoutChantier)
			System.out.println(coutChantier);
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

	public int getChantierId() {
		return chantierId;
	}
	
	public int getSiteId() {
		return chantierId;
	}

	public void setChantierId(Integer chantierId) {
		if (chantierId == null) {
			throw new Error("setChantierId : le chantierId indique est vide");
		}
		this.chantierId = chantierId;
	}

	public Integer getFournitureSanitaireId() {
		return fournitureSanitaireId;
	}

	public void setFournitureSanitaireId(Integer fournitureSanitaireId) {
		this.fournitureSanitaireId = fournitureSanitaireId;
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

	public String getSousTraitant() {
		return sousTraitant;
	}

	public void setSousTraitant(String sousTraitant) {
		this.sousTraitant = sousTraitant;
	}

	
	
	
	
}