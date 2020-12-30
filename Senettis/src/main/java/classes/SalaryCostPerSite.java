package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Month;
import java.time.Year;

import java.util.Objects;

import connexion.SQLDatabaseConnexion;

/**
 * Salary cost by site computed from employees affectations for a given month
 * and year
 * 
 *
 */
public class SalaryCostPerSite {

	private Month month;
	private Year year;
	private Integer siteId;
	private Double AffectationCost;
	private Double totalCost;
	private Double MABCost;

	/**************************
	 * 
	 * Constructors
	 * 
	 **************************/

	/**************************
	 * Constructor for SalaryCostPerSite
	 * 
	 * @param <type>Integer</type>    siteId not null
	 * @param <type>Month</type>month not null
	 * @param <type>Year</type>       year not null
	 * @throws SQLException
	 */

	public SalaryCostPerSite(Integer siteId, Month month, Year year) throws SQLException {
		this.setMonth(month);
		this.setYear(year);
		this.setSiteId(siteId);
		this.AffectationCost = computeAffectationCost(siteId, month, year, "normal");
		this.MABCost = computeAffectationCost(siteId, month, year, "MAB");
		this.totalCost = AffectationCost + MABCost;

	}

	/**
	 * get the view ACjoinCe view filtered on
	 * <param>SiteId</param>,<param>month</param> and <param>year</param> in a
	 * <type>ResultSet</type>
	 * 
	 * @param <type>Integer</type> siteId not null
	 * @param <type>Month</type>   month not null
	 * @param <type>Year           </type> year
	 * @return <type>ResultSet</type> result from the query
	 * @throws SQLException
	 */

	private ResultSet getACjoinCE(Integer siteId, Month month, Year year) throws SQLException {
		String selection = "Chantier,Employe,AC_nb_heures,MoisDebut,AnneeDebut,mois,annee,mutuelle,indemnite_panier,masse_salariale,cout_transport,cout_telephone,remboursement_prets,saisie_arret,CE_nb_heures";
		String source = "ACjoinCE_View";
		String condition = "ACStatus='Publié' AND CEStatus='Publié' AND Chantier=? AND Mois=? AND Annee=?";
		String reqSql = "Select " + selection + " FROM " + source + " WHERE " + condition + " ;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, siteId, Types.INTEGER);

		statement.setObject(2, month.getValue(), Types.INTEGER);
		statement.setObject(3, year.getValue(), Types.INTEGER);
		statement.execute();
		return statement.getResultSet();
	}

	/**
	 * get the view ACMABjoinCE view filtered on
	 * <param>SiteId</param>,<param>month</param> and <param>year</param> in a
	 * <type>ResultSet</type>
	 * 
	 * @param <type>Integer</type> siteId not null
	 * @param <type>Month</type>   month not null
	 * @param <type>Year           </type> year
	 * @return <type>ResultSet</type> result from the query
	 * @throws SQLException
	 */
	private ResultSet getACMABjoinCE(int siteId, Month month, Year year) throws SQLException {
		String selection = "Chantier,Employe,AC_nb_heures,mois,annee,mutuelle,indemnite_panier,masse_salariale,cout_transport,cout_telephone,remboursement_prets,saisie_arret,CE_nb_heures";
		String source = "ACjoinCEMAB_View";
		String condition = "ACStatus='Publié' AND CEStatus='Publié' AND Chantier=? AND Mois=? AND Annee=?";
		String reqSql = "Select " + selection + " FROM " + source + " WHERE " + condition + " ;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, siteId, Types.INTEGER);
		statement.setObject(2, month.getValue(), Types.INTEGER);
		statement.setObject(3, year.getValue(), Types.INTEGER);
		statement.execute();
		return statement.getResultSet();
	}

	/**
	 * Compute the SalaryAffectationCost
	 * 
	 * @param <type>Double</type> ACNbHeures
	 * @param <type>Double</type> insurance
	 * @param <type>Double</type> indemnitePanier
	 * @param <type>Double</type> masseSalariale
	 * @param <type>Double</type> coutTransport
	 * @param <type>Double</type> coutTelephone
	 * @param <type>Double</type> remboursementPrets
	 * @param <type>Double</type> saisieArret
	 * @param <type>Double</type> CENbHeures
	 * @return <type>Double</type> the SalaryAffectationCost
	 */

	private Double computeSalaryAffectationCost(Double ACNHours, Double insurance, Double basketAllowance,
			Double salaryMass, Double transportationCost, Double phoneCost, Double loanRefund,
			Double sickLeave, Double CEHours) {

		Double salaryCostCE = insurance + basketAllowance + salaryMass + transportationCost + phoneCost
				+ loanRefund + sickLeave;

		Double coef = ACNHours / CEHours;

		return salaryCostCE * coef;
	}

	/**
	 * Compute the Affectation cost for a
	 * </type>Integer</type>siteId,<type>Month</type> month,<type>year</type> and
	 * <type> String</type> given
	 * 
	 * @param <type> Integer </type> siteId not null
	 * @param <type> Month </type> month not null
	 * @param <type> Year </type> year not null
	 * @param <type> String </type>type not null, can take 2 values "normal" or
	 *               "MAB"
	 * @return <type> Double </type> affectationCost
	 * @throws SQLException
	 */

	private Double computeAffectationCost(Integer siteId, Month month, Year year, String type) throws SQLException {

		ResultSet result = null;
		if (Objects.isNull(type))
			throw new IllegalArgumentException("type can't be null must be equal to 'normal' Or 'MAB'");
		if (type.equals("normal")) {
			result = getACjoinCE(siteId, month, year);
		} else if (type.equals("MAB")) {
			result = getACMABjoinCE(siteId, month, year);
		} else
			throw new IllegalArgumentException("unknown type, type are 'normal' or 'MAB'");

		Double affectationCost = 0.00;

		while (result.next()) {

			Double ACNbHeures = 0.00;
			Double insurance = 0.00;
			Double basketAllowance = 0.00;
			Double salaryMass = 0.00;
			Double transportationCost = 0.00;
			Double phoneCost = 0.00;
			Double loeanRefund = 0.00;
			Double sickLeave = 0.00;
			Double CEHours = 0.00;

			if (!Objects.isNull(result.getDouble("AC_nb_heures")))
				ACNbHeures = result.getDouble("AC_nb_heures");
			if (!Objects.isNull(result.getDouble("mutuelle")))
				insurance = result.getDouble("mutuelle");
			if (!Objects.isNull(result.getDouble("indemnite_panier")))
				basketAllowance = result.getDouble("indemnite_panier");
			if (!Objects.isNull(result.getDouble("masse_salariale")))
				salaryMass = result.getDouble("masse_salariale");
			if (!Objects.isNull(result.getDouble("cout_transport")))
				transportationCost = result.getDouble("cout_transport");
			if (!Objects.isNull(result.getDouble("cout_telephone")))
				phoneCost = result.getDouble("cout_telephone");

			sickLeave = sickLeaveSum(result.getInt("Employe"), month, year);

			loeanRefund = refundLoanSum(result.getInt("Employe"), month, year);

			if (!Objects.isNull(result.getDouble("CE_nb_heures")))
				CEHours = result.getDouble("CE_nb_heures");

			affectationCost += computeSalaryAffectationCost(ACNbHeures, insurance, basketAllowance, salaryMass,
					transportationCost, phoneCost, loeanRefund, sickLeave, CEHours);

		}

		return affectationCost;
	}

	
	/**
	
	/**
	 * Compute the employee sick leave of an employee on a given month and
	 * year
	 * 
	 * @param <type> Integer </type>employeId not null
	 * @param <type> Month</type>month not null
	 * @param <type> Year</type> year not null
	 * @return <type> Double </type> sum fo the sick leave
	 * @throws SQLException
	 */
	private Double sickLeaveSum(Integer employeId, Month month, Year year) throws SQLException {

		String selection = "Employe ,sum(valeurParMois)  as sumV  ";
		String source = "AmmortissementEmploye ";

		String condition = "status='Publié'  AND Employe=? AND type='Saisie Arret' AND datefromparts(?,?,1) between datefromparts(AmmortissementEmploye.AnneeDepart,AmmortissementEmploye.MoisDepart,1) AND datefromparts(AmmortissementEmploye.AnneeFin,AmmortissementEmploye.MoisFin,1) ";
		String reqSql = "Select " + selection + " FROM " + source + " WHERE " + condition + "group by (Employe) ;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, employeId, Types.INTEGER);

		statement.setObject(2, year.getValue(), Types.INTEGER);
		statement.setObject(3, month.getValue(), Types.INTEGER);

		try {
			statement.execute();

			ResultSet result = statement.getResultSet();
			if (result.next()) {
				;
				return result.getDouble("sumV");
			}

		} catch (Exception e) {

		}
		return 0.00;

	}

	/**
	 * Compute the employee refund for a loan of an employee on a given month and
	 * year
	 * 
	 * @param <type> Integer </type>employeId not null
	 * @param <type> Month</type>month not null
	 * @param <type> Year</type> year not null
	 * @return <type> Double </type> sum of the loan
	 * @throws SQLException
	 */
	private Double refundLoanSum(Integer employeId, Month month, Year year) throws SQLException {

		String selection = "Employe ,sum(valeurParMois)  as sumV ";
		String source = "AmmortissementEmploye";

		String condition = "status='Publié'  AND Employe=? AND type='Saisie Arret' AND datefromparts(?,?,1) between datefromparts(AmmortissementEmploye.AnneeDepart,AmmortissementEmploye.MoisDepart,1) AND datefromparts(AmmortissementEmploye.AnneeFin,AmmortissementEmploye.MoisFin,1)";
		String reqSql = "Select " + selection + " FROM " + source + " WHERE " + condition + "group by (Employe) ;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, employeId, Types.INTEGER);

		statement.setObject(2, year.getValue(), Types.INTEGER);
		statement.setObject(3, month.getValue(), Types.INTEGER);
		statement.execute();

		ResultSet result = statement.getResultSet();
		try {
			statement.execute();

			if (result.next()) {

				return result.getDouble("sumV");
			}

		} catch (Exception e) {

		}
		return 0.00;

	}

	/**
	 * Getter for the attribute AffectationCost
	 * 
	 * @return <type>Double</type> AffectationCost
	 */
	public Double getAffectationCost() {
		return AffectationCost;
	}

	/**
	 * Getter for the attribute totalCost
	 * 
	 * @return <type>Double</type> totalCost
	 */

	public Double getTotalCost() {
		return totalCost;
	}

	/**
	 * Getter for the attribute MABCost
	 * 
	 * @return <type>Double</type>MABCost
	 */
	public Double getMABCost() {
		return MABCost;
	}

	/**
	 * Getter for the attribute Month
	 * 
	 * @return <type>Month</type> month
	 */
	public Month getMonth() {
		return month;
	}

	/**
	 * Getter for the attribute year
	 * 
	 * @return <type>Year</type> year
	 */
	public Year getYear() {
		return year;
	}

	/**
	 * Getter for the attribute siteId
	 * 
	 * @return </type>Integer</type> siteId
	 */
	public Integer getSiteId() {
		return siteId;
	}

	/**
	 * Setter for the attribute month
	 * 
	 * @param </type>Month</type>month not null
	 */
	public void setMonth(Month month) {
		if (!Objects.isNull(month))
			this.month = month;
		else
			throw new IllegalArgumentException("month can't be null");
	}

	/**
	 * Setter for the attribute year
	 * 
	 * @param year
	 */

	public void setYear(Year year) {
		if (!Objects.isNull(month))
			this.year = year;
		else
			throw new IllegalArgumentException("year can't be null");
	}

	/**
	 * Setter for the attrinute siteId
	 * 
	 * @param <type>Integer</type> siteId
	 */
	public void setSiteId(Integer siteId) {
		if (!Objects.isNull(siteId))
			this.siteId = siteId;
		else
			throw new IllegalArgumentException("siteId can't be null");
	}

}
