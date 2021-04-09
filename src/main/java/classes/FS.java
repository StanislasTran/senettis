
package classes;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import connexion.SQLDatabaseConnexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.Month;
import java.time.Year;

/**
 * Represent a Sanitary Supply
 *
 */

public class FS {

	private Integer FSId;
	private Integer siteId;
	private Integer startMonth;
	private Integer startYear;
	private Integer endMonth;
	private Integer endYear;
	private String description;
	private Double amountByMonth;
	private String status;
	private String subContractor;

	
	
	public FS(Integer FSId, Integer siteId, Integer startMonth, Integer startYear, Integer endMonth, Integer endYear, String description,
			Double amountByMonth, String subContractor, String status) {
		this(siteId, startMonth, startYear, amountByMonth, description, subContractor, status);
		if ((Integer) FSId != null) {
			this.FSId = FSId;
		} else {
			throw new Error(
					"Le fournitureSanitaireId est vide, merci de sp�cifier un id ou d'utiliser un autre constructeur.");
		}
		this.endMonth = endMonth;
		this.endYear = endYear;
	}
	
	
	
	
	
	/**
	 * Constructor for FS
	 * 
	 * @param <type>Integer</type>FSId
	 * @param <type>Integer</type>             siteId
	 * @param <type>Integer</type>startMonth
	 * @param <type>Integer</type>startYear
	 * @param <type>String</type>description
	 * @param <type>Double</type>amountByMonth
	 * @param <type>String</type>subContractor
	 * @param <type>String</type>status
	 */
	public FS(Integer FSId, Integer siteId, Integer startMonth, Integer startYear, String description,
			Double amountByMonth, String subContractor, String status) {
		this(siteId, startMonth, startYear, amountByMonth, description, subContractor, status);
		if ((Integer) FSId != null) {
			this.FSId = FSId;
		} else {
			throw new Error(
					"Le fournitureSanitaireId est vide, merci de sp�cifier un id ou d'utiliser un autre constructeur.");
		}
	}

	/**
	 * Constructor for FS
	 * 
	 * @param <type>Integer</type> FSId
	 * @param <type>Integer</type> startMonth
	 * @param <type>Integer</type> startYear
	 * @param <type>Double</type>  amountByMonth
	 * @param <type>String</type>  description
	 * @param <type>String</type>  subContractor
	 * @param <type>String</type>  status
	 */
	public FS(Integer FSId, Integer startMonth, Integer startYear, Double amountByMonth, String description,
			String subContractor, String status) {
		this(FSId, startMonth, startYear, amountByMonth, subContractor, status);

		this.description = description;
	}
	
	public FS(Integer siteId, int startMonth, int startYear, int endMonth, int endYear, Double amountByMonth, String description,
			String subContractor, String status) {
		this(siteId, startMonth, startYear, amountByMonth, subContractor, status);

		this.description = description;
		this.endMonth = endMonth;
		this.endYear = endYear;
	}


	/**
	 * Constructor for FS
	 * 
	 * @param <type>Integer</type> siteId
	 * @param <type>Integer</type> startMonth
	 * @param <type>Integer</type> startYear
	 * @param <type>Double</type>  amountByMonth
	 * @param <type>String</type>  subContractor
	 * @param <type>String</type>  status
	 */
	public FS(Integer siteId, Integer startMonth, Integer startYear, Double amountByMonth, String subContractor,
			String status) {
		// id
		if ((Integer) siteId != null) {
			this.siteId = siteId;
		} else {
			throw new Error("L'chantierId est vide, merci de sp�cifier un id.");
		}

		if (startMonth != null) {
			this.startMonth = startMonth;
		} else {
			throw new Error("Le mois de depart n'est pas sp�cifi�.");
		}

		if (startYear != null) {
			this.startYear = startYear;
		} else {
			throw new Error("L'annee de depart n'est pas sp�cifi�e.");
		}

		if (status != null) {
			if (!status.isEmpty()) {
				if (status.equals("Publié") || status.equals("Publié")) {
					this.status = "Publié";
				} else if (status.equals("Archivé") || status.equals("Archivé")) {
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

		this.subContractor = subContractor;
		this.amountByMonth = amountByMonth;

	}



	/**
	 * Insert the current FS into the database
	 * 
	 * @return <type>int</type> either (1) the row count for SQL Data Manipulation
	 *         Language (DML) statementsor (2) 0 for SQL statements that return
	 *         nothing
	 * 
	 * @throws SQLException - if a database access error occurs;this method is
	 *                      called on a closed PreparedStatementor the SQL statement
	 *                      returns a ResultSet objectSQLTimeoutException - when the
	 *                      driver has determined that thetimeout value that was
	 *                      specified by the setQueryTimeoutmethod has been exceeded
	 *                      and has at least attempted to cancelthe currently
	 *                      running Statement
	 */
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO FournitureSanitaire(moisDepart,anneeDepart,chantier,sousTraitant,status,valeurParMois,description, anneeFin, moisFin) VALUES (?,?,?,?,?,?,?,?,?)";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.startMonth, Types.INTEGER);
		statement.setObject(2, this.startYear, Types.INTEGER);
		statement.setObject(3, this.siteId, Types.INTEGER);
		statement.setObject(4, this.subContractor, Types.VARCHAR);
		statement.setObject(5, this.status, Types.VARCHAR);
		statement.setObject(6, this.amountByMonth, Types.DECIMAL);
		statement.setObject(7, this.description, Types.VARCHAR);
		statement.setObject(8, this.endYear, Types.INTEGER);
		statement.setObject(9, this.endMonth, Types.INTEGER);

		return statement.executeUpdate();
	}

	/**
	 * Update the current FS into the database
	 * 
	 * @return <type>int</type> either (1) the row count for SQL Data Manipulation
	 *         Language (DML) statements or (2) 0 for SQL statements that return
	 *         nothing
	 * @throws SQLException - if a database access error occurs;this method is
	 *                      called on a closed PreparedStatementor the SQL statement
	 *                      returns a ResultSet objectSQLTimeoutException - when the
	 *                      driver has determined that thetimeout value that was
	 *                      specified by the setQueryTimeoutmethod has been exceeded
	 *                      and has at least attempted to cancelthe currently
	 *                      running Statement
	 */

	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE FournitureSanitaire SET moisDepart=?, chantier=?,sousTraitant=?, status=?, anneeDepart=?,valeurParMois=?,description=?, anneeFin=?,moisFin=? WHERE FournitureSanitaireId=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.startMonth, Types.INTEGER);
		statement.setObject(2, this.siteId, Types.INTEGER);
		statement.setObject(3, this.subContractor, Types.VARCHAR);
		statement.setObject(4, this.status, Types.VARCHAR);
		statement.setObject(5, this.startYear, Types.INTEGER);
		statement.setObject(6, this.amountByMonth, Types.DECIMAL);
		statement.setObject(7, this.description, Types.VARCHAR);
		statement.setObject(8, this.endYear, Types.INTEGER);
		statement.setObject(9, this.endMonth, Types.INTEGER);
		statement.setObject(10, this.FSId, Types.INTEGER);
		return statement.executeUpdate();
	}

	/**
	 * Execute the query SELECT * FROM FournitureSanitaire and return the Statement
	 * 
	 * @return <type>Statement</type> statement
	 * @throws SQLException
	 */
	private static Statement selectAllFS() throws SQLException {
		String reqSql = "SELECT * FROM FournitureSanitaire";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}

	/**
	 * Select a FS from the database where fournitureSanitaireId=FSId AND
	 * Status=Published
	 * 
	 * @param <type>int</type>FSId
	 * @return <type>FS</type> the FS
	 * @throws SQLException throw the exception if the DATA is not found
	 */
	public static FS getFSById(int FSId) throws SQLException {
		String reqSql = "SELECT FournitureSanitaireId,moisDepart,anneeDepart,chantier,sousTraitant,status,valeurParMois,description, moisFin, anneeFin FROM FournitureSanitaire WHERE FournitureSanitaireId=? AND Status='Publié'";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, FSId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			FSId = result.getInt("FournitureSanitaireId");
			Integer moisD = result.getInt("moisDepart");
			Integer anneeD = result.getInt("anneeDepart");
			int chantierId = result.getInt("Chantier");

			Double montantParMois = 0.0;
			if (result.getString("valeurParMois") != null) {
				montantParMois = Double.parseDouble(result.getString("valeurParMois"));
			}

			String subContractor = result.getString("sousTraitant");
			String status = result.getString("status");
			String description = result.getString("description");
			Integer moisF = result.getInt("moisFin");
			Integer anneeF = result.getInt("anneeFin");

			return new FS(FSId, chantierId, moisD, anneeD, moisF, anneeF, description, montantParMois, subContractor, status);

		} else {
			throw new SQLException("Data not found");
		}
	}

	/**
	 * Return the list of all FS (Published)
	 * 
	 * @return <type>List<FS></type> allSiteCost
	 * @throws SQLException
	 */
	public static List<FS> getAllFS() throws SQLException {

		ResultSet result = selectAllFS().getResultSet();
		List<FS> allSiteCost = new ArrayList<FS>();
		while (result.next()) {
			int fournitureSanitaireId = result.getInt("FournitureSanitaireId");
			Integer startMonth = result.getInt("moisDepart");
			Integer startYear = result.getInt("anneeDepart");
			Integer endMonth = result.getInt("moisFin");
			Integer endYear = result.getInt("anneeFin");
			int chantierId = result.getInt("Chantier");

			Double amountByMonth = 0.0;
			if (result.getString("valeurParMois") != null) {
				amountByMonth = Double.parseDouble(result.getString("valeurParMois"));
			}

			String subContractor = result.getString("sousTraitant");
			String status = result.getString("status");
			String description = result.getString("description");

			allSiteCost.add(new FS(fournitureSanitaireId, chantierId, startMonth, startYear, endMonth, endYear,description, amountByMonth,
					subContractor, status));
		}

		return allSiteCost;
	}

	/**
	 * Displays all FS in a simple print
	 * 
	 * @throws SQLException
	 */

	public static void printAllFournitureSanitaire() throws SQLException {

		List<FS> allCoutChantier = getAllFS();

		for (FS coutChantier : allCoutChantier)
			System.out.println(coutChantier);
	}

	/**
	 * Return the active FS cost for a given month and year
	 * 
	 * @param <type>Month</type> month
	 * @param <type>Year         </type> year
	 * @return <type>ResultSet</type> return the result into ResultSet
	 * @throws SQLException
	 */

	public static ResultSet getAllCFSAfterResultSet(Month month, Year year) throws SQLException {
		String reqSql = "select chantier,Sum(valeurParMois) as SUM from FournitureSanitaire WHERE status ='Publié' AND (anneeDepart<? OR (anneeDepart=? AND moisDepart<=?) OR (AnneeDepart is Null AND MoisDepart is Null)) GROUP BY chantier;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, year.getValue(), Types.INTEGER);
		statement.setObject(2, year.getValue(), Types.INTEGER);
		statement.setObject(3, month.getValue(), Types.INTEGER);
		statement.execute();
		return statement.getResultSet();

	}

	/**
	 * return a map with key=siteId and Double the comission if the comission is
	 * applied on the date month,year
	 * 
	 * @param <type>Month</type>    month
	 * @param <type>Year</type>year
	 * @return <type>Map<Integer, Double></type> comissions
	 * @throws SQLException
	 */

	public static Map<Integer, Double> getAllFSterMap(Month month, Year year) throws SQLException {
		ResultSet result = getAllCFSAfterResultSet(month, year);
		HashMap<Integer, Double> comissions = new HashMap<Integer, Double>();
		while (result.next()) {

			comissions.put(result.getInt("chantier"), result.getDouble("SUM"));

		}

		return comissions;
	}

	/******************
	 * 
	 * Getters And Setters
	 * 
	 ******************/

	/**
	 * Getter for the attribute status
	 * 
	 * @return <type>String</type> status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Setter for the attribute Status
	 * 
	 * @param <type>String</type>status
	 */
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

	/**
	 * Getter for the attribute siteId
	 * 
	 * @return <type>Integer</type> siteId
	 */
	public Integer getSiteId() {
		return siteId;
	}

	/**
	 * Setter for the attribute siteId
	 * 
	 * @param <type>Integer</type> siteId
	 */
	public void setSiteId(Integer siteId) {
		if (siteId == null) {
			throw new Error("setChantierId : le chantierId indique est vide");
		}
		this.siteId = siteId;
	}

	/**
	 * Getter for the attribute FSId
	 * 
	 * @return <type>FSId</type>
	 */
	public Integer getFSId() {
		return FSId;
	}

	/**
	 * Setter for the attribute FSId
	 * 
	 * @param <type>Integer</type> FSId
	 */
	public void setFSId(Integer FSId) {
		this.FSId = FSId;
	}

	/**
	 * Getter for the attribute startMonth
	 * 
	 * @return <type>Integer</type> startMonth
	 */
	public Integer getStartMonth() {
		return startMonth;
	}

	/**
	 * Setter for the attribute startMonth
	 * 
	 * @param <type>Integer</type> startMonth
	 */
	public void setStartMonth(Integer startMonth) {
		this.startMonth = startMonth;
	}

	/**
	 * Setter for the attribute startYear
	 * 
	 * @param <type>Integer</type> startYear
	 */
	public Integer getStartYear() {
		return startYear;
	}

	/**
	 * Setter for the attribute startYear
	 * 
	 * @param startYear
	 */
	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}

	/**
	 * Getter for the attribute description
	 * 
	 * @return <type>String</type> description
	 */
	public String getDescription() {
		if (description != null) {
			return description;
		}
		return "";
	}

	/**
	 * Setter for the attribute description
	 * 
	 * @param <type> String</type> description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter for the attribute amountByMonth
	 * 
	 * @return <type>Double</type> amountByMonth
	 */
	public Double getAmountByMonth() {
		return amountByMonth;
	}

	/**
	 * Setter for the attribute amountByMonth
	 * 
	 * @param <type>Double</type> amountByMonth
	 */

	public void setMontantParMois(Double amountByMonth) {
		this.amountByMonth = amountByMonth;
	}

	/**
	 * Getter for the attribute subContractor
	 * 
	 * @return <type>String</type>subContractor
	 */
	public String getSousTraitant() {
		return subContractor;
	}

	/**
	 * Setter for the attribute subContractor
	 * 
	 * @param <type>String</type>subContractor
	 */
	public void setSubContractor(String subContractor) {
		this.subContractor = subContractor;
	}

	public Integer getEndMonth() {
		return endMonth;
	}
	
	public Integer getEndYear() {
		return endYear;
	}

}