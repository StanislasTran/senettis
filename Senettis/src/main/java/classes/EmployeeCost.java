
package classes;

import java.util.ArrayList;

import java.util.List;


import connexion.SQLDatabaseConnexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;



//to do (comment )
/**
 * Represent an EmployeeCost for a given month and year
 *
 *
 */
public class EmployeeCost {

	private Integer employeeCostId;
	private Integer employeeId;
	private Integer month;
	private Integer year;
	private Double transportationRefund;
	private Double phoneRefund;
	private Double netSalary;
	private Double grossSalary;
	private Double employerCharge;
	private Double salaryMass;
	private Double insurance;
	private Double basket;
	private Double loan;
	private Double sickLeave;
	private Double nbHours;
	private String status;

	/**
	 * Constructor EmployeeCost
	 * @param coutEmployeId
	 * @param employeId
	 * @param month
	 * @param annee
	 * @param remboursementTransport
	 * @param remboursementTelephone
	 * @param salaireNet
	 * @param salaireBrut
	 * @param chargesP
	 * @param masseS
	 * @param mutuelle
	 * @param paniers
	 * @param prets
	 * @param saisieArret
	 * @param nombreHeures
	 * @param status
	 */
	public EmployeeCost(Integer coutEmployeId, Integer employeId, Integer month, Integer annee, Double remboursementTransport, Double remboursementTelephone, 
			Double salaireNet, Double salaireBrut, Double chargesP, Double masseS, Double mutuelle, Double paniers,
			Double prets, Double saisieArret, Double nombreHeures, String status) {
		this(employeId,month, annee,remboursementTransport,remboursementTelephone, salaireNet,salaireBrut,chargesP,masseS,mutuelle,paniers, 
				prets,saisieArret, nombreHeures, status);
		if ((Integer) coutEmployeId != null) {
			this.employeeCostId = coutEmployeId;
		} else {
			throw new Error("Le coutEmployeId est vide, merci de sp�cifier un id ou d'utiliser un autre constructeur.");
		}
		
	}
	
	public EmployeeCost(int employeId, Integer month, Integer annee, Double remboursementTransport, Double remboursementTelephone, 
			Double salaireNet, Double salaireBrut, Double chargesP, Double masseS, Double mutuelle, Double paniers,
			Double prets, Double saisieArret, Double nombreHeures, String status) {
		this(employeId, month, annee, status);
		
		this.transportationRefund = remboursementTransport;
		this.phoneRefund = remboursementTelephone;
		this.netSalary = salaireNet;
		this.grossSalary = salaireBrut;
		this.employerCharge = chargesP;
		this.salaryMass = masseS;
		this.insurance = mutuelle;
		this.basket = paniers;
		this.loan = prets;
		this.sickLeave = saisieArret;
		this.nbHours = nombreHeures;
		
	}
	
	
	public EmployeeCost(int employeId, Integer month, Integer annee,String status) {
		// id
		if ((Integer) employeId != null) {
			this.employeeId = employeId;
		} else {
			throw new Error("L'employeId est vide, merci de spécifier un id.");
		}
		
		if (month != null) {
			this.month = month;
		} else {
			throw new Error("Le mois n'est pas spécifié.");
		}
		
		if (annee != null) {
			this.year = annee;
		} else {
			throw new Error("L'annee n'est pas spécifiée.");
		}
		
		if (status != null) {
			if (!status.isEmpty()) {
				if (status.equals("Publié") || status.equals("Publié")) {
					this.status = "Publié";
				} else if (status.equals("Archivé") || status.equals("Archivé"))  {
					this.status = "Archivé";
				} else if (status.equals("Draft") || status.equals("draft")) {
					this.status = "Draft";
				} else {
					throw new Error("Le status indiqu� est incorrect, le status doit �tre Publié, Archivé ou draft.");
				}
			} else {
				this.status = null;
			}
		}
		
	}

	

	// Liens avec la BDD-----------------------------------------------
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO CoutEmploye(mois, annee,employe,mutuelle,indemnite_panier,salaire_brut,salaire_net,cout_transport,cout_telephone,charges_patronales,masse_salariale,remboursement_prets,saisie_arret,nb_heures,status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.month, Types.INTEGER);
		statement.setObject(2, this.year, Types.INTEGER);
		statement.setObject(3, this.employeeId, Types.INTEGER);
		statement.setObject(4, this.insurance, Types.DECIMAL);
		statement.setObject(5, this.basket, Types.DECIMAL);
		statement.setObject(6, this.grossSalary, Types.DECIMAL);
		statement.setObject(7, this.netSalary, Types.DECIMAL);
		statement.setObject(8, this.transportationRefund, Types.DECIMAL);
		statement.setObject(9, this.phoneRefund, Types.DECIMAL);
		statement.setObject(10, this.employerCharge, Types.DECIMAL);
		statement.setObject(11, this.salaryMass, Types.DECIMAL);
		statement.setObject(12, this.loan, Types.DECIMAL);
		statement.setObject(13, this.sickLeave, Types.DECIMAL);
		statement.setObject(14, this.nbHours, Types.DECIMAL);
		statement.setObject(15, this.status, Types.VARCHAR);

		
		return statement.executeUpdate();
	}


	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE CoutEmploye SET mois=?, employe=?, mutuelle=?, indemnite_panier=?, salaire_brut=?, salaire_net=?, cout_transport=?, cout_telephone=?, charges_patronales=?, masse_salariale=?, remboursement_prets=?, saisie_arret=?, nb_heures=?, status=?, annee=? WHERE CoutEmployeId=?";
				
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.month, Types.INTEGER);
		statement.setObject(2, this.employeeId, Types.INTEGER);
		statement.setObject(3, this.insurance, Types.DECIMAL);
		statement.setObject(4, this.basket, Types.DECIMAL);
		statement.setObject(5, this.grossSalary, Types.DECIMAL);
		statement.setObject(6, this.netSalary, Types.DECIMAL);
		statement.setObject(7, this.transportationRefund, Types.DECIMAL);
		statement.setObject(8, this.phoneRefund, Types.DECIMAL);
		statement.setObject(9, this.employerCharge, Types.DECIMAL);
		statement.setObject(10, this.salaryMass, Types.DECIMAL);
		statement.setObject(11, this.loan, Types.DECIMAL);
		statement.setObject(12, this.sickLeave, Types.DECIMAL);
		statement.setObject(13, this.nbHours, Types.DECIMAL);
		statement.setObject(14, this.status, Types.VARCHAR);
		statement.setObject(15, this.year, Types.INTEGER);
		statement.setObject(16, this.employeeCostId, Types.INTEGER);
		
	
		return statement.executeUpdate();
	}
	
	
	public int updateDatabaseFromEmployeId() throws SQLException {
		String reqSql = "UPDATE CoutEmploye SET mutuelle=?, indemnite_panier=?, salaire_brut=?, salaire_net=?, cout_transport=?, cout_telephone=?, charges_patronales=?, masse_salariale=?, remboursement_prets=?, saisie_arret=?, nb_heures=?, status=? WHERE Employe=? AND mois=? AND annee=?";
				
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.insurance, Types.DECIMAL);
		statement.setObject(2, this.basket, Types.DECIMAL);
		statement.setObject(3, this.grossSalary, Types.DECIMAL);
		statement.setObject(4, this.netSalary, Types.DECIMAL);
		statement.setObject(5, this.transportationRefund, Types.DECIMAL);
		statement.setObject(6, this.phoneRefund, Types.DECIMAL);
		statement.setObject(7, this.employerCharge, Types.DECIMAL);
		statement.setObject(8, this.salaryMass, Types.DECIMAL);
		statement.setObject(9, this.loan, Types.DECIMAL);
		statement.setObject(10, this.sickLeave, Types.DECIMAL);
		statement.setObject(11, this.nbHours, Types.DECIMAL);
		statement.setObject(12, this.status, Types.VARCHAR);
		statement.setObject(13, this.employeeId, Types.INTEGER);
		statement.setObject(14, this.month, Types.INTEGER);
		statement.setObject(15, this.year, Types.INTEGER);
		
		return statement.executeUpdate();
	}


	private static Statement selectAllCoutEmploye() throws SQLException {
		String reqSql = "SELECT * FROM CoutEmploye";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}


	public static int getCountCoutEmploye() throws SQLException {
		String reqSql = "SELECT count(*) as count FROM CoutEmploye";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
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

	public static EmployeeCost getCoutEmployeByEmployeId(int employeId, int month, int annee) throws SQLException {
		String reqSql = "SELECT CoutEmployeId,employe,mois,annee,mutuelle,indemnite_panier,salaire_brut,salaire_net,cout_transport,cout_telephone,charges_patronales,masse_salariale,remboursement_prets,saisie_arret,nb_heures,status FROM CoutEmploye WHERE Employe=? AND mois=? AND annee=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, employeId, Types.INTEGER);
		statement.setObject(2, month, Types.INTEGER);
		statement.setObject(3, annee, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			int coutEmployeId = result.getInt("CoutEmployeId");
			month = result.getInt("mois");
			annee = result.getInt("annee");
			employeId = result.getInt("Employe");
			
			
			
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

			return new EmployeeCost(coutEmployeId, employeId,month, annee,remboursementTransport,remboursementTelephone, salaireNet,salaireBrut,chargesP,masseS,mutuelle,paniers, 
					prets,saisieArret,nombreHeures, status);

		} else {
			throw new SQLException("Data not found");
		}
	}
	
	
	public static EmployeeCost getCoutEmployeById(int coutEmployeId) throws SQLException {
		String reqSql = "SELECT CoutEmployeId,mois,annee,employe,mutuelle,indemnite_panier,salaire_brut,salaire_net,cout_transport,cout_telephone,charges_patronales,masse_salariale,remboursement_prets,saisie_arret,nb_heures,status FROM CoutEmploye WHERE CoutEmployeId=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, coutEmployeId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			coutEmployeId = result.getInt("CoutEmployeId");
			Integer month = result.getInt("mois");
			Integer year = result.getInt("annee");
			Integer employeeId = result.getInt("Employe");
			
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
			
			Double salaryMass = 0.0;
			if (result.getString("masse_salariale") != null) {
				salaryMass = Double.parseDouble(result.getString("masse_salariale"));
			}
			
			Double loan = 0.0;
			if (result.getString("remboursement_prets") != null) {
				loan = Double.parseDouble(result.getString("remboursement_prets"));
			}
			
			Double sickLeave = 0.0;
			if (result.getString("saisie_arret") != null) {
				sickLeave = Double.parseDouble(result.getString("saisie_arret"));
			}
			
			Double nombreHeures = 0.0;
			if (result.getString("nb_heures") != null) {
				nombreHeures = Double.parseDouble(result.getString("nb_heures"));
			}

			String status = result.getString("status");

			return new EmployeeCost(coutEmployeId, employeeId,month, year,remboursementTransport,remboursementTelephone, salaireNet,salaireBrut,chargesP,salaryMass,mutuelle,paniers, 
					loan,sickLeave,nombreHeures, status);

		} else {
			throw new SQLException("Data not found");
		}
	}

	// -----------------------------------------------
	public static List<EmployeeCost> getAllCoutEmploye() throws SQLException {

		ResultSet result = selectAllCoutEmploye().getResultSet();
		List<EmployeeCost> allCoutEmploye = new ArrayList<EmployeeCost>();
		while (result.next()) {
			int coutEmployeId = result.getInt("CoutEmployeId");
			Integer month = result.getInt("mois");
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

			allCoutEmploye.add(new EmployeeCost(coutEmployeId, employeId,month, annee,remboursementTransport,remboursementTelephone, salaireNet,salaireBrut,
					chargesP,masseS,mutuelle,paniers, prets,saisieArret,nombreHeures, status));

		}

		return allCoutEmploye;
	}

	public static void printAllCoutEmploye() throws SQLException {

		List<EmployeeCost> allCoutEmploye = getAllCoutEmploye();

		for (EmployeeCost coutEmploye : allCoutEmploye)
			System.out.println(coutEmploye);
	}

	@Override
	public String toString() {

		return "" + this.employeeCostId + "|" + this.employeeId + "|" + this.month + "|" + this.year + "|" + this.transportationRefund + "|" + this.phoneRefund + "|"
				+ this.netSalary + "|" + this.grossSalary + "|" + this.employerCharge + "|" + this.salaryMass + "|"
				+ this.insurance + "|" + this.basket + "|" + this.loan + "|"
				+ this.sickLeave + "|" + this.nbHours
				+ "|" + this.status;
	}

	// Getter and setter-----------------------------------------------
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if (status != null) {
			if (status == "Publié" || status == "Publié") {
				this.status = "Publié";
			} else if (status == "Archivé" || status == "Archivé") {
				this.status = "Archivé";
			} else if (status == "Draft" || status == "draft") {
				this.status = "Draft";
			} else {
				throw new Error("Le status indiqu� est incorrect, le status doit �tre Publié, Archivé ou draft.");
			}
		} else {
			throw new Error("Le status indiqu� est vide.");
		}
	}

	public Double getNombreHeures() {
		if (nbHours == null) {
			return 0.0;
		}
		return nbHours;
	}

	public void setNombreHeures(Double nombreHeures) {
		if (nombreHeures == null) {
			throw new Error("setNombreHeures : le nombreHeures indique est vide");
		}
		this.nbHours = nombreHeures;
	}

	public Double getRemboursementTransport() {
		if (transportationRefund == null) {
			return 0.0;
		}
		return transportationRefund;
	}

	public void setRemboursementTransport(Double remboursementTransport) {
		if (remboursementTransport == null) {
			throw new Error("setRemboursementTransport : le remboursementTransport indique est vide");
		}
		this.transportationRefund = remboursementTransport;
	}

	public Double getRemboursementTelephone() {
		if (phoneRefund == null) {
			return 0.0;
		}
		return phoneRefund;
	}

	public void setRemboursementTelephone(Double remboursementTelephone) {
		if (remboursementTelephone == null) {
			throw new Error("setRemboursementTelephone : le remboursementTelephone indique est vide");
		}
		this.phoneRefund = remboursementTelephone;
	}

	public Double getSalaireNet() {
		if (netSalary == null) {
			return 0.0;
		}
		return netSalary;
	}

	public void setSalaireNet(Double salaireNet) {
		if (salaireNet == null) {
			throw new Error("setSalaireNet : le salaireNet indique est vide");
		}
		this.netSalary = salaireNet;
	}

	public int getEmployeId() {
		return employeeId;
	}

	public void setEmployeId(Integer employeId) {
		if (employeId == null) {
			throw new Error("setEmployeId : le employeId indique est vide");
		}
		this.employeeId = employeId;
	}
	
	
	/////////////////////////a verifier 

	public Integer getCoutEmployeId() {
		return employeeCostId;
	}

	public void setCoutEmployeId(Integer coutEmployeId) {
		this.employeeCostId = coutEmployeId;
	}

	public Integer getMois() {
		return month;
	}

	public void setMois(Integer mois) {
		this.month = mois;
	}
	
	public Integer getAnnee() {
		return year;
	}

	public void setAnnee(Integer annee) {
		this.year = annee;
	}

	public Double getSalaireBrut() {
		return grossSalary;
	}

	public void setSalaireBrut(Double salaireBrut) {
		this.grossSalary = salaireBrut;
	}

	public Double getChargesP() {
		return employerCharge;
	}

	public void setChargesP(Double chargesP) {
		this.employerCharge = chargesP;
	}

	public Double getMasseS() {
		return salaryMass;
	}

	public void setMasseS(Double masseS) {
		this.salaryMass = masseS;
	}

	public Double getMutuelle() {
		return insurance;
	}

	public void setMutuelle(Double mutuelle) {
		this.insurance = mutuelle;
	}

	public Double getPaniers() {
		return basket;
	}

	public void setPaniers(Double paniers) {
		this.basket = paniers;
	}

	public Double getPrets() {
		return loan;
	}

	public void setPrets(Double prets) {
		this.loan = prets;
	}

	public Double getSaisieArret() {
		return sickLeave;
	}

	public void setSaisieArret(Double saisieArret) {
		this.sickLeave = saisieArret;
	}


	
	
	

}
