
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

/**
 * represent employee Amortization into the system
 */

public class EmployeeAmortization {

	private Integer EmployeeAmortizationId;
	private Integer employeeId;
	private Integer startMonth;
	private Integer startYear;
	private Integer endMonth;
	private Integer endYear;
	private String description;
	private Double costByMonth;
	private Integer duration;
	private Double value;
	private String status;
	private String type;

	/**
	 * Constructor
	 * 
	 * @param EmployeeAmortizationId
	 * @param employeeId
	 * @param startMonth
	 * @param startYear
	 * @param endMonth
	 * @param endYear
	 * @param description
	 * @param costByMonth
	 * @param duration
	 * @param value
	 * @param type
	 * @param status
	 */
	public EmployeeAmortization(Integer EmployeeAmortizationId, Integer employeeId, Integer startMonth,
			Integer startYear, Integer endMonth, Integer endYear, String description, Double costByMonth,
			Integer duration, Double value, String type, String status) {
		this(EmployeeAmortizationId, startMonth, startYear, endMonth, endYear, costByMonth, duration, value, type,
				status);
		if (EmployeeAmortizationId != null) {
			this.EmployeeAmortizationId = EmployeeAmortizationId;
		} else {
			throw new Error(
					"Le ammortissementEmployeId est vide, merci de spécifier un id ou d'utiliser un autre constructeur.");
		}
		this.description = description;
	}

	/**
	 * Constructor
	 * 
	 * @param employeId
	 * @param moisD
	 * @param anneeD
	 * @param moisF
	 * @param anneeF
	 * @param montantParMois
	 * @param description
	 * @param duree
	 * @param valeur
	 * @param type
	 * @param status
	 */
	public EmployeeAmortization(Integer employeeId, Integer startMonth, Integer startYear, Integer endMonth,
			Integer endYear, Double costByMonth, String description, Integer duration, Double value, String type,
			String status) {
		this(employeeId, startMonth, startYear, endMonth, endYear, costByMonth, duration, value, type, status);

		this.description = description;
	}

	/**
	 * 
	 * @param employeId
	 * @param moisD
	 * @param anneeD
	 * @param moisF
	 * @param anneeF
	 * @param montantParMois
	 * @param duree
	 * @param valeur
	 * @param type
	 * @param status
	 */
	public EmployeeAmortization(Integer employeeId, Integer startMonth, Integer startYear, Integer endMonth,
			Integer endYear, Double costByMonth, Integer duration, Double value, String type, String status) {
		// id
		if (employeeId != null) {
			this.employeeId = employeeId;
		} else {
			throw new Error("L'employeId est vide, merci de spécifier un id.");
		}

		if (startYear != null) {
			this.startMonth = startYear;
		} else {
			throw new Error("Le mois de depart n'est pas spécifié.");
		}

		if (startYear != null)
			this.startYear = startYear;

		if (endMonth != null) {
			this.endMonth = endMonth;
		} else {
			throw new Error("Le mois de fin n'est pas spécifié.");
		}

		if (endYear != null) {
			this.endYear = endYear;
		} else {
			throw new Error("L'annee de fin n'est pas spécifiée.");
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

		this.value = value;
		this.type = type;
		this.duration = duration;
		this.costByMonth = costByMonth;

	}

	/**
	 * Insert curent EmployeeAmortization into the database
	 * 
	 * @return either (1) the row count for SQL Data Manipulation Language (DML)
	 *         statements or (2) 0 for SQL statements that return nothing
	 * @throws SQLException - if a database access error occurs;this method is
	 *                      called on a closed PreparedStatementor the SQL statement
	 *                      returns a ResultSet objectSQLTimeoutException - when the
	 *                      driver has determined that the timeout value that was
	 *                      specified by the setQueryTimeoutmethod has been exceeded
	 *                      and has at least attempted to cancel the currently
	 *                      running Statement
	 */
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO AmmortissementEmploye(moisDepart, anneeDepart,employe,valeur,duree,type,status,moisFin,anneeFin,valeurParMois,description) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.startMonth, Types.INTEGER);
		statement.setObject(2, this.startYear, Types.INTEGER);
		statement.setObject(3, this.employeeId, Types.INTEGER);
		statement.setObject(4, this.value, Types.DECIMAL);
		statement.setObject(5, this.duration, Types.INTEGER);
		statement.setObject(6, this.type, Types.VARCHAR);
		statement.setObject(7, this.status, Types.VARCHAR);
		statement.setObject(8, this.endMonth, Types.INTEGER);
		statement.setObject(9, this.endYear, Types.INTEGER);
		statement.setObject(10, this.costByMonth, Types.DECIMAL);
		statement.setObject(11, this.description, Types.VARCHAR);

		return statement.executeUpdate();
	}

	/**
	 * update the current EmployeeAmortization into the database
	 * 
	 * @return either (1) the row count for SQL Data Manipulation Language (DML)
	 *         statementsor (2) 0 for SQL statements that return nothing
	 * @throws SQLException
	 */
	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE AmmortissementEmploye SET moisDepart=?, employe=?, duree=?, valeur=?, type=?, status=?, anneeDepart=?,moisFin=?,anneeFin=?,valeurParMois=?,description=? WHERE AmmortissementEmployeId=?";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.startMonth, Types.INTEGER);
		statement.setObject(2, this.employeeId, Types.INTEGER);
		statement.setObject(3, this.duration, Types.INTEGER);
		statement.setObject(4, this.value, Types.DECIMAL);
		statement.setObject(5, this.type, Types.VARCHAR);
		statement.setObject(6, this.status, Types.VARCHAR);
		statement.setObject(7, this.startYear, Types.INTEGER);
		statement.setObject(8, this.endMonth, Types.INTEGER);
		statement.setObject(9, this.endYear, Types.INTEGER);
		statement.setObject(10, this.costByMonth, Types.DECIMAL);
		statement.setObject(11, this.description, Types.VARCHAR);
		statement.setObject(12, this.EmployeeAmortizationId, Types.INTEGER);
		System.out.println(this.description);
		System.out.println(this.type);
		System.out.println(this.EmployeeAmortizationId);
		return statement.executeUpdate();
	}

	/**
	 * return all Statment frome the database
	 * 
	 * @return <type>Statment</type>
	 * @throws SQLException
	 */
	private static Statement selectAllAmmortissementEmploye() throws SQLException {
		String reqSql = "SELECT * FROM AmmortissementEmploye";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}

	/**
	 * Return the number of employeeCost
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static int getEmployeeCost() throws SQLException {
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

	/**
	 * Return the list of all Amortization for a given employee
	 * 
	 * @param employeId
	 * @return
	 * @throws SQLException
	 */
	public static List<EmployeeAmortization> getAmortizationEmployee(int employeId) throws SQLException {
		String reqSql = "SELECT AmmortissementEmployeId,moisDepart,anneeDepart,employe,duree,valeur,type,status,moisFin,anneeFin,valeurParMois,description FROM AmmortissementEmploye WHERE Employe=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, employeId, Types.INTEGER);
		statement.executeQuery();
		List<EmployeeAmortization> allCoutEmploye = new ArrayList<EmployeeAmortization>();

		ResultSet result = statement.getResultSet();

		while (result.next()) {
			int ammortissementEmployeId = result.getInt("AmmortissementEmployeId");
			Integer startMonth = result.getInt("moisDepart");
			Integer startYear = result.getInt("anneeDepart");
			Integer endMonth = result.getInt("moisFin");
			Integer endYear = result.getInt("anneeFin");
			employeId = result.getInt("Employe");

			Integer duration = result.getInt("duree");

			Double value = 0.0;
			if (result.getString("valeur") != null) {
				value = Double.parseDouble(result.getString("valeur"));
			}

			Double costByMonth = 0.0;
			if (result.getString("valeurParMois") != null) {
				costByMonth = Double.parseDouble(result.getString("valeurParMois"));
			}

			String type = result.getString("type");
			String status = result.getString("status");
			String description = result.getString("description");

			allCoutEmploye.add(new EmployeeAmortization(ammortissementEmployeId, employeId, startMonth, startYear,
					endMonth, endYear, description, costByMonth, duration, value, type, status));
		}

		return allCoutEmploye;
	}

	/**
	 * Getter for amortizationEmployee for a given AmortizationEmployeeId
	 * 
	 * @param AmortizationEmployeeId
	 * @return
	 * @throws SQLException
	 */
	public static EmployeeAmortization getAmortizationById(Integer AmortizationEmployeeId) throws SQLException {
		String reqSql = "SELECT AmmortissementEmployeId,moisDepart,anneeDepart,employe,duree,valeur,type,status,moisFin,anneeFin,valeurParMois,description FROM AmmortissementEmploye WHERE AmmortissementEmployeId=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, AmortizationEmployeeId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			AmortizationEmployeeId = result.getInt("AmmortissementEmployeId");
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

			return new EmployeeAmortization(AmortizationEmployeeId, employeId, moisD, anneeD, moisF, anneeF,
					description, montantParMois, duree, valeur, type, status);

		} else {
			throw new SQLException("Data not found");
		}
	}

	/**
	 * get the list of all EmployeeAmortization into the database
	 * @return <type>List<EmployeeAmortization> </type>
	 * @throws SQLException
	 */
	public static List<EmployeeAmortization> getAllAmmortissementEmploye() throws SQLException {

		ResultSet result = selectAllAmmortissementEmploye().getResultSet();
		List<EmployeeAmortization> allCoutEmploye = new ArrayList<EmployeeAmortization>();
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

			allCoutEmploye.add(new EmployeeAmortization(ammortissementEmployeId, employeId, moisD, anneeD, moisF,
					anneeF, description, montantParMois, duree, valeur, type, status));
		}

		return allCoutEmploye;
	}

	
	/**
	 * print All Amortization from the database
	 * @throws SQLException
	 */
	public static void printAllEmployeeAmortization() throws SQLException {

		List<EmployeeAmortization> allCoutEmploye = getAllAmmortissementEmploye();

		for (EmployeeAmortization coutEmploye : allCoutEmploye)
			System.out.println(coutEmploye);
	}

	// Getter and setter-----------------------------------------------
	public String getStatus() {
		return status;
	}

	/**
	 * Setter for Status
	 * @param status
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
				throw new Error("Le status indiqué est incorrect, le status doit être Publié, archivé ou draft.");
			}
		} else {
			throw new Error("Le status indiqu� est vide.");
		}
	}

	
	/**
	 * Getter for EmployeeId
	 * 
	 * @return <type>Integer</type>
	 */
	public Integer getEmployeId() {
		return employeeId;
	}

	/**
	 * Setter for employeeId
	 * @param employeeId
	 */
	public void setEmployeeId(Integer employeeId) {
		if (employeeId == null) {
			throw new Error("setEmployeId : le employeId indique est vide");
		}
		this.employeeId = employeeId;
	}

	/**
	 * Getter for EmployeeAmortizationId
	 * @return <type>Integer</type>
	 */
	public Integer getEmployeeAmortizationId() {
		return EmployeeAmortizationId;
	}

	/***
	 * Setter for EmployeeAmortizationId
	 * @param ammortissementEmployeId
	 */
	public void setEmployeeAmortizationId(Integer EmployeeAmortizationId) {
		this.EmployeeAmortizationId = EmployeeAmortizationId;
	}

	/**
	 * 
	 * @return <type>Integer</type> duration
	 */
	public Integer getDuration() {
		return duration;
	}

	
	/**
	 * Setter for duration
	 * @param duration
	 */
	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	/**
	 * Getter for value
	 * @return <type>Double</type>
	 */
	public Double getValue() {
		return value;
	}

	
	/**
	 * Setter for value
	 * @param valeur
	 */
	public void setValeur(Double valeur) {
		this.value = valeur;
	}

	
	/**
	 * Getter for type
	 * @return <type>String</type>
	 */
	public String getType() {
		return type;
	}

	
	/**
	 * Setter for type
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the startMonth
	 */
	public Integer getStartMonth() {
		return startMonth;
	}

	/**
	 * @param startMonth the startMonth to set
	 */
	public void setStartMonth(Integer startMonth) {
		this.startMonth = startMonth;
	}

	/**
	 * @return the startYear
	 */
	public Integer getStartYear() {
		return startYear;
	}

	/**
	 * @param startYear the startYear to set
	 */
	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}

	/**
	 * @return the endMonth
	 */
	public Integer getEndMonth() {
		return endMonth;
	}

	/**
	 * @param endMonth the endMonth to set
	 */
	public void setEndMonth(Integer endMonth) {
		this.endMonth = endMonth;
	}

	/**
	 * @return the endYear
	 */
	public Integer getEndYear() {
		return endYear;
	}

	/**
	 * @param endYear the endYear to set
	 */
	public void setEndYear(Integer endYear) {
		this.endYear = endYear;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the costByMonth
	 */
	public Double getCostByMonth() {
		return costByMonth;
	}

	/**
	 * @param costByMonth the costByMonth to set
	 */
	public void setCostByMonth(Double costByMonth) {
		this.costByMonth = costByMonth;
	}

	/**
	 * @return the employeeId
	 */
	public Integer getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Double value) {
		this.value = value;
	}

	
}