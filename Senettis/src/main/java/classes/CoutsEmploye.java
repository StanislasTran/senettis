
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

public class CoutsEmploye {

	private Integer coutEmployeId;
	private Integer employeId;
	private Integer mois;
	private Integer annee;
	private Double remboursementTransport;
	private Double remboursementTelephone;
	private Double salaireNet;
	private Double salaireBrut;
	private Double chargesP;
	private Double masseS;
	private Double mutuelle;
	private Double paniers;
	private Double prets;
	private Double saisieArret;
	private Double menage;
	private Double vitrerie;
	private Double fournituresSanitaires;
	private Double misesBlanc;
	private Double autres;
	private Double nombreHeures;
	private String status;

	// Constructeurs----------------------------------------------
	public CoutsEmploye(int coutEmployeId, int employeId, Integer mois, Integer annee, Double remboursementTransport, Double remboursementTelephone, 
			Double salaireNet, Double salaireBrut, Double chargesP, Double masseS, Double mutuelle, Double paniers,
			Double prets, Double saisieArret, Double menage, Double vitrerie, Double fournituresSanitaires, Double misesBlanc, 
			Double autres, Double nombreHeures, String status) {
		this(employeId,mois, annee,remboursementTransport,remboursementTelephone, salaireNet,salaireBrut,chargesP,masseS,mutuelle,paniers, 
				prets,saisieArret,menage,vitrerie,fournituresSanitaires,misesBlanc, autres, nombreHeures, status);
		if ((Integer) coutEmployeId != null) {
			this.coutEmployeId = coutEmployeId;
		} else {
			throw new Error("Le coutEmployeId est vide, merci de spécifier un id ou d'utiliser un autre constructeur.");
		}
		
	}
	
	public CoutsEmploye(int employeId, Integer mois, Integer annee, Double remboursementTransport, Double remboursementTelephone, 
			Double salaireNet, Double salaireBrut, Double chargesP, Double masseS, Double mutuelle, Double paniers,
			Double prets, Double saisieArret, Double menage, Double vitrerie, Double fournituresSanitaires, Double misesBlanc, 
			Double autres, Double nombreHeures, String status) {
		this(employeId, mois, annee, status);
		
		this.remboursementTransport = remboursementTransport;
		this.remboursementTelephone = remboursementTelephone;
		this.salaireNet = salaireNet;
		this.salaireBrut = salaireBrut;
		this.chargesP = chargesP;
		this.masseS = masseS;
		this.mutuelle = mutuelle;
		this.paniers = paniers;
		this.prets = prets;
		this.saisieArret = saisieArret;
		this.menage = menage;
		this.vitrerie = vitrerie;
		this.fournituresSanitaires = fournituresSanitaires;
		this.misesBlanc = misesBlanc;
		this.nombreHeures = nombreHeures;
		this.autres = autres;
		
	}
	
	
	public CoutsEmploye(int employeId, Integer mois, Integer annee,String status) {
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
		
	}

	

	// Liens avec la BDD-----------------------------------------------
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO CoutEmploye(mois, annee,employe,mutuelle,indemnite_panier,salaire_brut,salaire_net,cout_transport,cout_telephone,charges_patronales,masse_salariale,menage,vitrerie,mises_a_blanc,fournitures_sanitaires,autres,remboursement_prets,saisie_arret,nb_heures,status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.mois, Types.INTEGER);
		statement.setObject(2, this.annee, Types.INTEGER);
		statement.setObject(3, this.employeId, Types.INTEGER);
		statement.setObject(4, this.mutuelle, Types.DECIMAL);
		statement.setObject(5, this.paniers, Types.DECIMAL);
		statement.setObject(6, this.salaireBrut, Types.DECIMAL);
		statement.setObject(7, this.salaireNet, Types.DECIMAL);
		statement.setObject(8, this.remboursementTransport, Types.DECIMAL);
		statement.setObject(9, this.remboursementTelephone, Types.DECIMAL);
		statement.setObject(10, this.chargesP, Types.DECIMAL);
		statement.setObject(11, this.masseS, Types.DECIMAL);
		statement.setObject(12, this.menage, Types.DECIMAL);
		statement.setObject(13, this.vitrerie, Types.DECIMAL);
		statement.setObject(14, this.misesBlanc, Types.DECIMAL);
		statement.setObject(15, this.fournituresSanitaires, Types.DECIMAL);
		statement.setObject(16, this.autres, Types.DECIMAL);
		statement.setObject(17, this.prets, Types.DECIMAL);
		statement.setObject(18, this.saisieArret, Types.DECIMAL);
		statement.setObject(19, this.nombreHeures, Types.DECIMAL);
		statement.setObject(20, this.status, Types.VARCHAR);

		return statement.executeUpdate();
	}


	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE CoutEmploye SET mois=?, employe=?, mutuelle=?, indemnite_panier=?, salaire_brut=?, salaire_net=?, cout_transport=?, cout_telephone=?, charges_patronales=?, masse_salariale=?, menage=?, vitrerie=?, mises_a_blanc=?, fournitures_sanitaires=?, autres=?, remboursement_prets=?, saisie_arret=?, nb_heures=?, status=?, annee=? WHERE CoutEmployeId=?";
				
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.mois, Types.INTEGER);
		statement.setObject(2, this.employeId, Types.INTEGER);
		statement.setObject(3, this.mutuelle, Types.DECIMAL);
		statement.setObject(4, this.paniers, Types.DECIMAL);
		statement.setObject(5, this.salaireBrut, Types.DECIMAL);
		statement.setObject(6, this.salaireNet, Types.DECIMAL);
		statement.setObject(7, this.remboursementTransport, Types.DECIMAL);
		statement.setObject(8, this.remboursementTelephone, Types.DECIMAL);
		statement.setObject(9, this.chargesP, Types.DECIMAL);
		statement.setObject(10, this.masseS, Types.DECIMAL);
		statement.setObject(11, this.menage, Types.DECIMAL);
		statement.setObject(12, this.vitrerie, Types.DECIMAL);
		statement.setObject(13, this.misesBlanc, Types.DECIMAL);
		statement.setObject(14, this.fournituresSanitaires, Types.DECIMAL);
		statement.setObject(15, this.autres, Types.DECIMAL);
		statement.setObject(16, this.prets, Types.DECIMAL);
		statement.setObject(17, this.saisieArret, Types.DECIMAL);
		statement.setObject(18, this.nombreHeures, Types.DECIMAL);
		statement.setObject(19, this.status, Types.VARCHAR);
		statement.setObject(20, this.annee, Types.INTEGER);
		statement.setObject(21, this.coutEmployeId, Types.INTEGER);

		return statement.executeUpdate();
	}


	private static Statement selectAllCoutEmploye() throws SQLException {
		String reqSql = "SELECT * FROM CoutEmploye";

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


	public static CoutsEmploye getCoutEmployeById(int coutEmployeId) throws SQLException {
		String reqSql = "SELECT CoutEmployeId,mois,annee,employe,mutuelle,indemnite_panier,salaire_brut,salaire_net,cout_transport,cout_telephone,charges_patronales,masse_salariale,menage,vitrerie,mises_a_blanc,fournitures_sanitaires,autres,remboursement_prets,saisie_arret,nb_heures,status FROM CoutEmploye WHERE CoutEmployeId=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, coutEmployeId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			coutEmployeId = result.getInt("CoutEmployeId");
			Integer mois = result.getInt("mois");
			Integer annee = result.getInt("annee");
			int employeId = result.getInt("Employe");
			
			Double mutuelle = 0.0;
			if (result.getString("mutuelle") != null) {
				mutuelle = Double.parseDouble(result.getString("mutuelle"));
			}
			
			Double paniers = 0.0;
			if (result.getString("indemnite_panier") != null) {
				paniers = Double.parseDouble(result.getString("indemnite_panier"));
			}
			
			Double salaireBrut = 0.0;
			if (result.getString("salaire_brut") != null) {
				salaireBrut = Double.parseDouble(result.getString("salaire_brut"));
			}
			
			Double salaireNet = 0.0;
			if (result.getString("salaire_net") != null) {
				salaireNet = Double.parseDouble(result.getString("salaire_net"));
			}
			
			
			Double remboursementTransport = 0.0;
			if (result.getString("cout_transport") != null) {
				remboursementTransport = Double.parseDouble(result.getString("cout_transport"));
			}
			
			Double remboursementTelephone = 0.0;
			if (result.getString("cout_telephone") != null) {
				remboursementTelephone = Double.parseDouble(result.getString("cout_telephone"));
			}
			
			Double chargesP = 0.0;
			if (result.getString("charges_patronales") != null) {
				chargesP = Double.parseDouble(result.getString("charges_patronales"));
			}
			
			Double masseS = 0.0;
			if (result.getString("masse_salariale") != null) {
				masseS = Double.parseDouble(result.getString("masse_salariale"));
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
			
			Double prets = 0.0;
			if (result.getString("remboursement_prets") != null) {
				prets = Double.parseDouble(result.getString("remboursement_prets"));
			}
			
			Double saisieArret = 0.0;
			if (result.getString("saisie_arret") != null) {
				saisieArret = Double.parseDouble(result.getString("saisie_arret"));
			}
			
			Double nombreHeures = 0.0;
			if (result.getString("nb_heures") != null) {
				nombreHeures = Double.parseDouble(result.getString("nb_heures"));
			}

			String status = result.getString("status");

			return new CoutsEmploye(coutEmployeId, employeId,mois, annee,remboursementTransport,remboursementTelephone, salaireNet,salaireBrut,chargesP,masseS,mutuelle,paniers, 
					prets,saisieArret,menage,vitrerie,fournituresSanitaires,misesBlanc, autres,nombreHeures, status);

		} else {
			throw new SQLException("Data not found");
		}
	}

	// -----------------------------------------------
	public static List<CoutsEmploye> getAllCoutEmploye() throws SQLException {

		ResultSet result = selectAllCoutEmploye().getResultSet();
		List<CoutsEmploye> allCoutEmploye = new ArrayList<CoutsEmploye>();
		while (result.next()) {
			int coutEmployeId = result.getInt("CoutEmployeId");
			Integer mois = result.getInt("mois");
			Integer annee = result.getInt("annee");
			int employeId = result.getInt("Employe");
			
			Double mutuelle = 0.0;
			if (result.getString("mutuelle") != null) {
				mutuelle = Double.parseDouble(result.getString("mutuelle"));
			}
			
			Double paniers = 0.0;
			if (result.getString("indemnite_panier") != null) {
				paniers = Double.parseDouble(result.getString("indemnite_panier"));
			}
			
			Double salaireBrut = 0.0;
			if (result.getString("salaire_brut") != null) {
				salaireBrut = Double.parseDouble(result.getString("salaire_brut"));
			}
			
			Double salaireNet = 0.0;
			if (result.getString("salaire_net") != null) {
				salaireNet = Double.parseDouble(result.getString("salaire_net"));
			}
			
			
			Double remboursementTransport = 0.0;
			if (result.getString("cout_transport") != null) {
				remboursementTransport = Double.parseDouble(result.getString("cout_transport"));
			}
			
			Double remboursementTelephone = 0.0;
			if (result.getString("cout_telephone") != null) {
				remboursementTelephone = Double.parseDouble(result.getString("cout_telephone"));
			}
			
			Double chargesP = 0.0;
			if (result.getString("charges_patronales") != null) {
				chargesP = Double.parseDouble(result.getString("charges_patronales"));
			}
			
			Double masseS = 0.0;
			if (result.getString("masse_salariale") != null) {
				masseS = Double.parseDouble(result.getString("masse_salariale"));
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
			
			Double prets = 0.0;
			if (result.getString("remboursement_prets") != null) {
				prets = Double.parseDouble(result.getString("remboursement_prets"));
			}
			
			Double saisieArret = 0.0;
			if (result.getString("saisie_arret") != null) {
				saisieArret = Double.parseDouble(result.getString("saisie_arret"));
			}
			
			Double nombreHeures = 0.0;
			if (result.getString("nb_heures") != null) {
				nombreHeures = Double.parseDouble(result.getString("nb_heures"));
			}

			String status = result.getString("status");

			allCoutEmploye.add(new CoutsEmploye(coutEmployeId, employeId,mois, annee,remboursementTransport,remboursementTelephone, salaireNet,salaireBrut,
					chargesP,masseS,mutuelle,paniers, prets,saisieArret,menage,vitrerie,fournituresSanitaires,misesBlanc, autres,nombreHeures, status));

		}

		return allCoutEmploye;
	}

	public static void printAllCoutEmploye() throws SQLException {

		List<CoutsEmploye> allCoutEmploye = getAllCoutEmploye();

		for (CoutsEmploye coutEmploye : allCoutEmploye)
			System.out.println(coutEmploye);
	}

	@Override
	public String toString() {

		return "" + this.coutEmployeId + "|" + this.employeId + "|" + this.mois + "|" + this.annee + "|" + this.remboursementTransport + "|" + this.remboursementTelephone + "|"
				+ this.salaireNet + "|" + this.salaireBrut + "|" + this.chargesP + "|" + this.masseS + "|"
				+ this.mutuelle + "|" + this.paniers + "|" + this.prets + "|"
				+ this.saisieArret + "|" + this.menage + "|" + this.vitrerie + "|" + this.fournituresSanitaires + "|" + this.misesBlanc + "|" + this.autres  + "|" + this.nombreHeures
				+ "|" + this.status;
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

	public Double getNombreHeures() {
		if (nombreHeures == null) {
			return 0.0;
		}
		return nombreHeures;
	}

	public void setNombreHeures(Double nombreHeures) {
		if (nombreHeures == null) {
			throw new Error("setNombreHeures : le nombreHeures indique est vide");
		}
		this.nombreHeures = nombreHeures;
	}

	public Double getRemboursementTransport() {
		if (remboursementTransport == null) {
			return 0.0;
		}
		return remboursementTransport;
	}

	public void setRemboursementTransport(Double remboursementTransport) {
		if (remboursementTransport == null) {
			throw new Error("setRemboursementTransport : le remboursementTransport indique est vide");
		}
		this.remboursementTransport = remboursementTransport;
	}

	public Double getRemboursementTelephone() {
		if (remboursementTelephone == null) {
			return 0.0;
		}
		return remboursementTelephone;
	}

	public void setRemboursementTelephone(Double remboursementTelephone) {
		if (remboursementTelephone == null) {
			throw new Error("setRemboursementTelephone : le remboursementTelephone indique est vide");
		}
		this.remboursementTelephone = remboursementTelephone;
	}

	public Double getSalaireNet() {
		if (salaireNet == null) {
			return 0.0;
		}
		return salaireNet;
	}

	public void setSalaireNet(Double salaireNet) {
		if (salaireNet == null) {
			throw new Error("setSalaireNet : le salaireNet indique est vide");
		}
		this.salaireNet = salaireNet;
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
	
	
	/////////////////////////a verifier 

	public Integer getCoutEmployeId() {
		return coutEmployeId;
	}

	public void setCoutEmployeId(Integer coutEmployeId) {
		this.coutEmployeId = coutEmployeId;
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

	public Double getSalaireBrut() {
		return salaireBrut;
	}

	public void setSalaireBrut(Double salaireBrut) {
		this.salaireBrut = salaireBrut;
	}

	public Double getChargesP() {
		return chargesP;
	}

	public void setChargesP(Double chargesP) {
		this.chargesP = chargesP;
	}

	public Double getMasseS() {
		return masseS;
	}

	public void setMasseS(Double masseS) {
		this.masseS = masseS;
	}

	public Double getMutuelle() {
		return mutuelle;
	}

	public void setMutuelle(Double mutuelle) {
		this.mutuelle = mutuelle;
	}

	public Double getPaniers() {
		return paniers;
	}

	public void setPaniers(Double paniers) {
		this.paniers = paniers;
	}

	public Double getPrets() {
		return prets;
	}

	public void setPrets(Double prets) {
		this.prets = prets;
	}

	public Double getSaisieArret() {
		return saisieArret;
	}

	public void setSaisieArret(Double saisieArret) {
		this.saisieArret = saisieArret;
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
	
	
	

}
