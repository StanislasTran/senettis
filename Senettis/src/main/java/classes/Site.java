package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import connexion.SQLDatabaseConnection;

public class Site {

	private String name;
	private String adress;

	private Status status;
	private Integer siteId;

	/***************************
	 * 
	 * Constructors
	 * 
	 **************************/

	/**
	 * Constructor
	 * 
	 * @param <type>Integer</type>siteId
	 * @param <type>String</type>name    Not Null
	 * @param <type>String</type>adress
	 * @param <type>Status</type>status  Not Null
	 */
	public Site(Integer siteId, String name, String adress, Status status) {
		this(name, adress, status);

		this.siteId = siteId;
	}

	/**
	 * Constructor
	 * 
	 * @param <type>String</type>name   Not Null
	 * @param <type>String</type>adress
	 * @param <type>Status</type>status Not Null
	 */

	public Site(String name, String adress, Status status) {
		this(name, status);
		this.adress = adress;

	}

	/**
	 * Constructor
	 * 
	 * @param <type>String</type>name   Not Null
	 * @param <type>Status</type>status Not Null
	 */
	public Site(String name, Status status) {
		super();
		this.name = name;
		this.status = status;
	}

	/**
	 * Insert the Site in the database
	 * 
	 * @return <type> int </type> either (1) the row count for SQL Data Manipulation
	 *         Language (DML) statementsor (2) 0 for SQL statements that return
	 *         nothing
	 * @throws SQLException
	 */
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO Chantier(nom,adresse,status) VALUES (?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.name, Types.VARCHAR);
		statement.setObject(2, this.adress, Types.VARCHAR);

		statement.setObject(3, this.status.getValue(), Types.VARCHAR);

		return statement.executeUpdate();
	}

	/**
	 * execute and return the query "Select * FROM Chantier Where Status='Publié'"
	 * 
	 * @return <type>Statement </type> the statement returned from the query
	 * @throws SQLException
	 */
	private static Statement selectAllChantier() throws SQLException {
		String reqSql = "SELECT * FROM Chantier Where Status='Publié'";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}

	/**
	 * Execute an query to get All site in the database where the Status='Publié' in
	 * a <type>List</type>
	 * 
	 * @return <type>List<Site> </type> list which contain all Site in the database
	 * @throws SQLException
	 */
	public static List<Site> getAllChantier() throws SQLException {

		ResultSet result = selectAllChantier().getResultSet();
		List<Site> allChantier = new ArrayList<Site>();
		while (result.next()) {
			int chantierId = result.getInt("ChantierId");
			String nom = result.getString("Nom");

			String adress;
			if (result.getString("Adresse") != null) {
				adress = result.getString("Adresse");
			} else {
				adress = "";
			}

			Status status = Status.getStatus(result.getString("Status"));
			allChantier.add(new Site(chantierId, nom, adress, status));
		}
		return allChantier;
	}

	/**
	 * Print all site where status='Publié'
	 * 
	 * @throws SQLException
	 */
	public static void printAllSite() throws SQLException {

		List<Site> allSite = getAllChantier();

		for (Site site : allSite)
			System.out.println(site);
	}

	@Override
	public String toString() {

		return "" + this.siteId + "|" + this.name + "|" + this.adress + "|" + this.status;
	}

	/**
	 * Update the site in database
	 * 
	 * @return either (1) the row count for SQL Data Manipulation Language (DML)
	 *         statementsor (2) 0 for SQL statements that return nothing
	 * @throws SQLExceptionSQLException - if a database access error occurs;this
	 *                                  method is called on a closed
	 *                                  PreparedStatementor the SQL statement
	 *                                  returns a ResultSet
	 *                                  objectSQLTimeoutException - when the driver
	 *                                  has determined that thetimeout value that
	 *                                  was specified by the setQueryTimeoutmethod
	 *                                  has been exceeded and has at least attempted
	 *                                  to cancelthe currently running Statement
	 */
	public int updateDatabase() throws SQLException, SQLTimeoutException {
		String reqSql = "UPDATE Chantier SET nom=?, adresse=?, status=? WHERE ChantierId=?;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.name.toString(), Types.VARCHAR);
		statement.setObject(2, this.adress, Types.VARCHAR);
		statement.setObject(3, this.status.getValue(), Types.VARCHAR);
		statement.setObject(4, this.siteId, Types.INTEGER);

		return statement.executeUpdate();
	}

	/**
	 * Return the number of site in the database where status="Publié"
	 * 
	 * @return <type>int</type> the number of Site
	 * @throws SQLException
	 */
	public static int getCountChantier() throws SQLException {
		String reqSql = "SELECT count(*) as count FROM Chantier Where Status='Publié'";
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

	/***
	 * @param <type>Integer</type>chantierId
	 * @return <type>Site</type> the site from database
	 * @throws SQLException
	 */
	public static Site getSiteById(int chantierId) throws SQLException {
		String reqSql = "SELECT ChantierId,nom,adresse,Status FROM Chantier WHERE ChantierId=?;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, chantierId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			chantierId = result.getInt("ChantierId");
			String nom = result.getString("Nom");

			String adress;
			if (result.getString("Adresse") != null) {
				adress = result.getString("Adresse");
			} else {
				adress = "";
			}

			Status status = Status.getStatus(result.getString("status"));

			return new Site(chantierId, nom, adress, status);

		} else {
			throw new SQLException("Data not found");
		}
	}

	/***
	 * @param <type>String</type>name
	 * @return <
	 * @throws SQLException
	 */
	public static Integer getSiteIdByName(String name) throws SQLException {
		String reqSql = "SELECT ChantierId,nom,adresse,Status FROM Chantier WHERE Nom=?;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, name, Types.VARCHAR);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();
		Integer chantierId = 0;
		if (result.next())
			chantierId = result.getInt("ChantierId");

		return chantierId;

	}

	/**
	 * getter and setter
	 */

	/**
	 * getter for the attribute status
	 * 
	 * @return <type>Status </type> status
	 */
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		if (Objects.isNull(status)) {
			throw new Error("setStatus : le status indique est vide");
		}
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setNom(String name) {
		if (name == null) {
			throw new Error("setNom : le nom indique est vide");
		}
		this.name = name;
	}

	/**
	 * getter for the attribute adress
	 * 
	 * @return <type> String </type> adresse
	 */
	public String getAdresse() {
		return adress;
	}

	/**
	 * setter for the attribute adress
	 * 
	 * @param <type>String</type>adress
	 */
	public void setAdress(String adress) {
		if (adress == null) {
			throw new Error("setAdresse : le adresse indique est vide");
		}
		this.adress = adress;
	}

	public Integer getSiteId() {
		return siteId;
	}

	/**
	 * Setter for the attribute <type>Integer</type>siteId
	 * 
	 * @param siteId
	 */
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

}
