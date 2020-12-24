
package classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import java.time.Month;
import java.time.Year;

public class AmmortissementChantier {

	private Integer ammortissementChantierId;
	private Integer chantierId;
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
	public AmmortissementChantier(int ammortissementChantierId, int chantierId, Integer moisD, Integer anneeD,Integer moisF, Integer anneeF,String description,Double montantParMois, Integer duree, Double valeur, String type, String status) {
		this(chantierId,moisD, anneeD, moisF,anneeF,montantParMois, duree, valeur, type, status);
		if ((Integer) ammortissementChantierId != null) {
			this.ammortissementChantierId = ammortissementChantierId;
		} else {
			throw new Error("Le ammortissementChantierId est vide, merci de spécifier un id ou d'utiliser un autre constructeur.");
		}
		this.description = description;	
	}
	
	public AmmortissementChantier(int chantierId, Integer moisD, Integer anneeD,Integer moisF, Integer anneeF,Double montantParMois,String description, Integer duree, Double valeur, String type, String status) {
		this(chantierId,moisD, anneeD, moisF,anneeF,montantParMois, duree, valeur, type, status);
		
		this.description = description;		
	}
	
	
	public AmmortissementChantier(int chantierId, Integer moisD, Integer anneeD,Integer moisF, Integer anneeF,Double montantParMois, Integer duree, Double valeur, String type, String status) {
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
		String reqSql = "INSERT INTO AmmortissementChantier(moisDepart, anneeDepart,chantier,valeur,duree,type,status,moisFin,anneeFin,valeurParMois,description) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.moisD, Types.INTEGER);
		statement.setObject(2, this.anneeD, Types.INTEGER);
		statement.setObject(3, this.chantierId, Types.INTEGER);
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
		String reqSql = "UPDATE AmmortissementChantier SET moisDepart=?, chantier=?, duree=?, valeur=?, type=?, status=?, anneeDepart=?,moisFin=?,anneeFin=?,valeurParMois=?,description=? WHERE AmmortissementChantierId=?";
				
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.moisD, Types.INTEGER);
		statement.setObject(2, this.chantierId, Types.INTEGER);
		statement.setObject(3, this.duree, Types.INTEGER);
		statement.setObject(4, this.valeur, Types.DECIMAL);
		statement.setObject(5, this.type, Types.VARCHAR);
		statement.setObject(6, this.status, Types.VARCHAR);
		statement.setObject(7, this.anneeD, Types.INTEGER);
		statement.setObject(8, this.moisF, Types.INTEGER);
		statement.setObject(9, this.anneeF, Types.INTEGER);
		statement.setObject(10, this.montantParMois, Types.DECIMAL);
		statement.setObject(11, this.description, Types.VARCHAR);
		statement.setObject(12, this.ammortissementChantierId, Types.INTEGER);
		System.out.println(this.description);
		System.out.println(this.type);
		System.out.println(this.ammortissementChantierId);
		return statement.executeUpdate();
	}


	private static Statement selectAllAmmortissementChantier() throws SQLException {
		String reqSql = "SELECT * FROM AmmortissementChantier";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}


	public static int getCountCoutChantier() throws SQLException {
		String reqSql = "SELECT count(*) as count FROM CoutChantier";
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


	public static AmmortissementChantier getAmmortissementChantierById(int ammortissementChantierId) throws SQLException {
		String reqSql = "SELECT AmmortissementChantierId,moisDepart,anneeDepart,chantier,duree,valeur,type,status,moisFin,anneeFin,valeurParMois,description FROM AmmortissementChantier WHERE AmmortissementChantierId=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, ammortissementChantierId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			ammortissementChantierId = result.getInt("AmmortissementChantierId");
			Integer moisD = result.getInt("moisDepart");
			Integer anneeD = result.getInt("anneeDepart");
			Integer moisF = result.getInt("moisFin");
			Integer anneeF = result.getInt("anneeFin");
			int chantierId = result.getInt("Chantier");

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

			return new AmmortissementChantier(ammortissementChantierId, chantierId,moisD, anneeD,moisF,anneeF,description,montantParMois,duree,valeur,type, status);

		} else {
			throw new SQLException("Data not found");
		}
	}

	// -----------------------------------------------
	public static List<AmmortissementChantier> getAllAmmortissementChantier() throws SQLException {

		ResultSet result = selectAllAmmortissementChantier().getResultSet();
		List<AmmortissementChantier> allCoutChantier = new ArrayList<AmmortissementChantier>();
		while (result.next()) {
			int ammortissementChantierId = result.getInt("AmmortissementChantierId");
			Integer moisD = result.getInt("moisDepart");
			Integer anneeD = result.getInt("anneeDepart");
			Integer moisF = result.getInt("moisFin");
			Integer anneeF = result.getInt("anneeFin");
			int chantierId = result.getInt("Chantier");
			
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

			allCoutChantier.add(new AmmortissementChantier(ammortissementChantierId, chantierId,moisD, anneeD,moisF,anneeF,description,montantParMois,duree,valeur,type, status));
		}

		return allCoutChantier;
	}

	
	
	public static void printAllAmmortissementChantier() throws SQLException {

		List<AmmortissementChantier> allCoutChantier = getAllAmmortissementChantier();

		for (AmmortissementChantier coutChantier : allCoutChantier)
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

	public Integer getAmmortissementChantierId() {
		return ammortissementChantierId;
	}

	public void setAmmortissementChantierId(Integer ammortissementChantierId) {
		this.ammortissementChantierId = ammortissementChantierId;
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

	
	public static Map<Integer, Double> getAllACFilteredMap( Month month, Year year)
			throws SQLException {
		ResultSet result = getAllACsAfterResultSet( month, year);
		HashMap<Integer,Double> ac = new HashMap<Integer, Double>();
		while (result.next()) {

			ac.put(result.getInt("chantier"), result.getDouble("SUM"));

		}
		return ac;
	}
	
	public static ResultSet getAllACsAfterResultSet(Month month, Year year)
			throws SQLException {
		
		String reqSql = "select chantier,Sum(valeurParMois) as SUM from AmmortissementChantier WHERE status ='Publié' AND (anneeDepart<=? AND anneeFin>=?) OR (anneeDepart=? AND moisDepart<=? AND moisFin>=?) GROUP BY chantier;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, year.getValue(), Types.INTEGER);
		statement.setObject(2, year.getValue(), Types.INTEGER);
		statement.setObject(3, year.getValue(), Types.INTEGER);
		statement.setObject(4, month.getValue(), Types.INTEGER);
		statement.setObject(5, month.getValue(), Types.INTEGER);
		statement.execute();
		return statement.getResultSet();
	}
	
	
}