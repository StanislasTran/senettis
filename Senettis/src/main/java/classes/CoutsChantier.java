
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

public class CoutsChantier {

	private Integer coutChantierId;
	private Integer chantierId;
	private Integer mois;
	private Integer annee;
	private Double menage;
	private Double vitrerie;
	private Double fournituresSanitaires;
	private Double misesBlanc;
	private Double autres;
	private Double commission;
	private Double materiel;
	private Double livraison;
	private Double ponctuel;
	private String status;

	// Constructeurs----------------------------------------------
	public CoutsChantier(int coutChantierId, int chantierId, Integer mois, Integer annee, Double menage, Double vitrerie, Double fournituresSanitaires, Double misesBlanc, 
			Double autres, Double commission, Double materiel, Double livraison, Double ponctuel, String status) {
		this(chantierId,mois, annee, menage,vitrerie,fournituresSanitaires,misesBlanc, autres, commission, materiel, livraison, ponctuel, status);
		if ((Integer) coutChantierId != null) {
			this.coutChantierId = coutChantierId;
		} else {
			throw new Error("Le coutChantierId est vide, merci de spécifier un id ou d'utiliser un autre constructeur.");
		}
		
	}
	
	public CoutsChantier(int chantierId, Integer mois, Integer annee, Double menage, Double vitrerie, Double fournituresSanitaires, Double misesBlanc, 
			Double autres, Double commission, Double materiel, Double livraison, Double ponctuel, String status) {
		this(chantierId, mois, annee, status);
		
		this.menage = menage;
		this.vitrerie = vitrerie;
		this.fournituresSanitaires = fournituresSanitaires;
		this.misesBlanc = misesBlanc;
		this.commission = commission;
		this.materiel = materiel;
		this.livraison = livraison;
		this.ponctuel = ponctuel;
		this.autres = autres;
		
	}
	
	
	public CoutsChantier(int chantierId, Integer mois, Integer annee,String status) {
		// id
		if ((Integer) chantierId != null) {
			this.chantierId = chantierId;
		} else {
			throw new Error("L'chantierId est vide, merci de spécifier un id.");
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
		
	}

	

	// Liens avec la BDD-----------------------------------------------
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO CoutChantier(mois, annee,chantier,commission, materiel, livraison, ponctuel,menage,vitrerie,mises_a_blanc,fournitures_sanitaires,autres,status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.mois, Types.INTEGER);
		statement.setObject(2, this.annee, Types.INTEGER);
		statement.setObject(3, this.chantierId, Types.INTEGER);
		statement.setObject(4, this.commission, Types.DECIMAL);
		statement.setObject(5, this.materiel, Types.DECIMAL);
		statement.setObject(6, this.livraison, Types.DECIMAL);
		statement.setObject(7, this.ponctuel, Types.DECIMAL);
		statement.setObject(8, this.menage, Types.DECIMAL);
		statement.setObject(9, this.vitrerie, Types.DECIMAL);
		statement.setObject(10, this.misesBlanc, Types.DECIMAL);
		statement.setObject(11, this.fournituresSanitaires, Types.DECIMAL);
		statement.setObject(12, this.autres, Types.DECIMAL);
		statement.setObject(13, this.status, Types.VARCHAR);

		return statement.executeUpdate();
	}


	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE CoutChantier SET mois=?, chantier=?, commission=?, materiel=?, livraison=?, ponctuel=?, menage=?, vitrerie=?, mises_a_blanc=?, fournitures_sanitaires=?, autres=?, status=?, annee=? WHERE CoutChantierId=?";
				
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.mois, Types.INTEGER);
		statement.setObject(2, this.annee, Types.INTEGER);
		statement.setObject(3, this.chantierId, Types.INTEGER);
		statement.setObject(4, this.commission, Types.DECIMAL);
		statement.setObject(5, this.materiel, Types.DECIMAL);
		statement.setObject(6, this.livraison, Types.DECIMAL);
		statement.setObject(7, this.ponctuel, Types.DECIMAL);
		statement.setObject(8, this.menage, Types.DECIMAL);
		statement.setObject(9, this.vitrerie, Types.DECIMAL);
		statement.setObject(10, this.misesBlanc, Types.DECIMAL);
		statement.setObject(11, this.fournituresSanitaires, Types.DECIMAL);
		statement.setObject(12, this.autres, Types.DECIMAL);
		statement.setObject(13, this.status, Types.VARCHAR);
		statement.setObject(14, this.coutChantierId, Types.INTEGER);

		return statement.executeUpdate();
	}


	private static Statement selectAllCoutChantier() throws SQLException {
		String reqSql = "SELECT * FROM CoutChantier";

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


	public static CoutsChantier getCoutChantierById(int coutChantierId) throws SQLException {
		String reqSql = "SELECT CoutChantierId,mois, annee,chantier,commission, materiel, livraison, ponctuel,menage,vitrerie,mises_a_blanc,fournitures_sanitaires,autres,status FROM CoutChantier WHERE CoutChantierId=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, coutChantierId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			coutChantierId = result.getInt("CoutChantierId");
			Integer mois = result.getInt("mois");
			Integer annee = result.getInt("annee");
			int chantierId = result.getInt("Chantier");
			
			Double commission = 0.0;
			if (result.getString("commission") != null) {
				commission = Double.parseDouble(result.getString("commission"));
			}
			
			Double materiel = 0.0;
			if (result.getString("materiel") != null) {
				materiel = Double.parseDouble(result.getString("materiel"));
			}
			
			Double livraison = 0.0;
			if (result.getString("livraison") != null) {
				livraison = Double.parseDouble(result.getString("livraison"));
			}
			
			Double ponctuel = 0.0;
			if (result.getString("ponctuel") != null) {
				ponctuel = Double.parseDouble(result.getString("ponctuel"));
			}
			
			Double menage = 0.0;
			if (result.getString("menage") != null) {
				menage = Double.parseDouble(result.getString("menage"));
			}
			
			Double vitrerie = 0.0;
			if (result.getString("vitrerie") != null) {
				vitrerie = Double.parseDouble(result.getString("vitrerie"));
			}
			
			Double misesBlanc = 0.0;
			if (result.getString("mises_a_blanc") != null) {
				misesBlanc = Double.parseDouble(result.getString("mises_a_blanc"));
			}
			
			Double fournituresSanitaires = 0.0;
			if (result.getString("fournitures_sanitaires") != null) {
				fournituresSanitaires = Double.parseDouble(result.getString("fournitures_sanitaires"));
			}

			Double autres = 0.0;
			if (result.getString("autres") != null) {
				autres = Double.parseDouble(result.getString("autres"));
			}
			
			String status = result.getString("status");

			return new CoutsChantier(coutChantierId, chantierId,mois, annee,commission,materiel,livraison,ponctuel,menage,vitrerie,fournituresSanitaires,misesBlanc, autres, status);

		} else {
			throw new SQLException("Data not found");
		}
	}

	// -----------------------------------------------
	public static List<CoutsChantier> getAllCoutChantier() throws SQLException {

		ResultSet result = selectAllCoutChantier().getResultSet();
		List<CoutsChantier> allCoutChantier = new ArrayList<CoutsChantier>();
		while (result.next()) {
			int coutChantierId = result.getInt("CoutChantierId");
			Integer mois = result.getInt("mois");
			Integer annee = result.getInt("annee");
			int chantierId = result.getInt("Chantier");
			
			Double commission = 0.0;
			if (result.getString("commission") != null) {
				commission = Double.parseDouble(result.getString("commission"));
			}
			
			Double materiel = 0.0;
			if (result.getString("materiel") != null) {
				materiel = Double.parseDouble(result.getString("materiel"));
			}
			
			Double livraison = 0.0;
			if (result.getString("livraison") != null) {
				livraison = Double.parseDouble(result.getString("livraison"));
			}
			
			Double ponctuel = 0.0;
			if (result.getString("ponctuel") != null) {
				ponctuel = Double.parseDouble(result.getString("ponctuel"));
			}
			
			Double menage = 0.0;
			if (result.getString("menage") != null) {
				menage = Double.parseDouble(result.getString("menage"));
			}
			
			Double vitrerie = 0.0;
			if (result.getString("vitrerie") != null) {
				vitrerie = Double.parseDouble(result.getString("vitrerie"));
			}
			
			Double misesBlanc = 0.0;
			if (result.getString("mises_a_blanc") != null) {
				misesBlanc = Double.parseDouble(result.getString("mises_a_blanc"));
			}
			
			Double fournituresSanitaires = 0.0;
			if (result.getString("fournitures_sanitaires") != null) {
				fournituresSanitaires = Double.parseDouble(result.getString("fournitures_sanitaires"));
			}

			Double autres = 0.0;
			if (result.getString("autres") != null) {
				autres = Double.parseDouble(result.getString("autres"));
			}

			String status = result.getString("status");

			allCoutChantier.add(new CoutsChantier(coutChantierId, chantierId,mois, annee,commission,materiel,livraison,ponctuel,menage,vitrerie,fournituresSanitaires,misesBlanc, autres, status));

		}

		return allCoutChantier;
	}

	public static void printAllCoutChantier() throws SQLException {

		List<CoutsChantier> allCoutChantier = getAllCoutChantier();

		for (CoutsChantier coutChantier : allCoutChantier)
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

	public void setChantierId(Integer chantierId) {
		if (chantierId == null) {
			throw new Error("setChantierId : le chantierId indique est vide");
		}
		this.chantierId = chantierId;
	}
	
	
	/////////////////////////a verifier 

	public Integer getCoutChantierId() {
		return coutChantierId;
	}

	public void setCoutChantierId(Integer coutChantierId) {
		this.coutChantierId = coutChantierId;
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

	public Double getMenage() {
		return menage;
	}

	public void setMenage(Double menage) {
		this.menage = menage;
	}

	public Double getVitrerie() {
		return vitrerie;
	}

	public void setVitrerie(Double vitrerie) {
		this.vitrerie = vitrerie;
	}

	public Double getFournituresSanitaires() {
		return fournituresSanitaires;
	}

	public void setFournituresSanitaires(Double fournituresSanitaires) {
		this.fournituresSanitaires = fournituresSanitaires;
	}

	public Double getMisesBlanc() {
		return misesBlanc;
	}

	public void setMisesBlanc(Double misesBlanc) {
		this.misesBlanc = misesBlanc;
	}

	public Double getAutres() {
		return autres;
	}

	public void setAutres(Double autres) {
		this.autres = autres;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}

	public Double getMateriel() {
		return materiel;
	}

	public void setMateriel(Double materiel) {
		this.materiel = materiel;
	}

	public Double getLivraison() {
		return livraison;
	}

	public void setLivraison(Double livraison) {
		this.livraison = livraison;
	}

	public Double getPonctuel() {
		return ponctuel;
	}

	public void setPonctuel(Double ponctuel) {
		this.ponctuel = ponctuel;
	}
	
	
	

}
