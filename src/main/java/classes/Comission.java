package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import connexion.SQLDatabaseConnexion;

/**
 * represent comission into the system
 *
 */

public class Comission {
	private Integer comissionId;
	private Double comission;
	private Integer site;
	private Month startMonth;
	private Year startYear;
	private Status status;

	/**
	 * Constructor
	 * 
	 * @param comission
	 * @param site
	 * @param startMonth
	 * @param startYear
	 * @param status
	 */
	public Comission(Double comission, Integer site, Month startMonth, Year startYear, Status status) {
		super();
		this.comission = comission;
		this.site = site;
		this.startMonth = startMonth;
		this.startYear = startYear;
		this.status = status;
	}

	/**
	 * constructor
	 * 
	 * @param comissionId
	 * @param comission
	 * @param site
	 * @param startMonth
	 * @param startYear
	 * @param status
	 */

	public Comission(Integer comissionId, Double comission, Integer site, Month startMonth, Year startYear,
			Status status) {
		this(comission, site, startMonth, startYear, status);
		this.comissionId = comissionId;
	}

	/**
	 * 
	 * @param site
	 * @param startMonth
	 * @param startYear
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet getComissionsResultSet(Integer site, Month startMonth, Year startYear) throws SQLException {
		String reqSql = "Select * FROM Comission Where Status ='Publié' AND chantier=? AND MoisDebut=? AND AnneeDebut=?";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, site, Types.INTEGER);
		statement.setObject(2, startMonth.getValue(), Types.INTEGER);
		statement.setObject(3, startYear, Types.INTEGER);

		statement.executeUpdate();
		return statement.getResultSet();

	}

	/**
	 * getAllComissionActive
	 * 
	 * @param site
	 * @param startMonth
	 * @param startYear
	 * @return <type>ResultSet</type>
	 * @throws SQLException - if a database access error occurs or this method is
	 *                      called on a closed Statement
	 */

	public static ResultSet getAllComissionsActive(Month month, Year year) throws SQLException {
		String reqSql = "select chantier,Sum(comission) as SUM from Comission WHERE status ='Publié' AND AnneeDebut<? OR (AnneeDebut=? AND MoisDebut<=?) GROUP BY chantier;";

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
	 * @param site
	 * @param month
	 * @param year
	 * @return
	 * @throws SQLException
	 */

	public static Map<Integer, Double> getAllComissionsAfterMap(Month month, Year year) throws SQLException {
		ResultSet result = getAllComissionsActive(month, year);
		HashMap<Integer, Double> comissions = new HashMap<Integer, Double>();
		while (result.next()) {

			comissions.put(result.getInt("chantier"), result.getDouble("SUM"));

		}

		return comissions;
	}

	/**
	 * Getter which return the list of active comissions after on a given date
	 * 
	 * @param <type>Integer</type>site
	 * @param <type>Month</type>startMonth
	 * @param <type>Year</type>startYear
	 * @return <type>List<Comission></type> list of active comissions
	 * @throws SQLException - if a database access error occurs or this method is
	 *                      called on a closed result set
	 */

	public static List<Comission> getComissionsList(Integer site, Month startMonth, Year startYear)
			throws SQLException {
		ResultSet result = getComissionsResultSet(site, startMonth, startYear);
		List<Comission> comissions = new ArrayList<Comission>();
		while (result.next()) {

			comissions.add(new Comission(result.getDouble("Comission"), result.getInt("chantier"),
					Month.of(result.getInt("MoisDebut")), Year.of(result.getInt("AnneeDebut")),
					Status.valueOf(result.getString("Status"))));

		}

		return comissions;
	}

	/**
	 * Getter which return all Comissions stored into the database
	 * 
	 * @return <type>ResultSet</type> all the comissions into the database into a
	 *         ResultSet
	 * @throws SQLException - if a database access error occursor this method is
	 *                      called on a closed connection
	 */
	public static ResultSet getComissionsResultSet() throws SQLException {
		String reqSql = "Select Comission.comissionId,Comission.comission,Comission.MoisDebut,Comission.AnneeDebut,Chantier.ChantierId,Chantier.Nom FROM Comission JOIN Chantier ON Chantier.ChantierId=Comission.Chantier Where Chantier.Status ='Publié' AND Comission.Status='Publié' ORDER BY Chantier.Nom;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);

		statement.execute();
		return statement.getResultSet();

	}

	/**
	 * Insert the current Comission into the database
	 * 
	 * @throws SQLException - if a database access error occurs;this method is
	 *                      called on a closed PreparedStatementor the SQL statement
	 *                      returns a ResultSet object
	 */
	public void insert() throws SQLException {
		String reqSql = "INSERT INTO Comission(Comission,Chantier,MoisDebut,AnneeDebut,Status) values  (?,?,?,?,?);";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);

		statement.setObject(1, this.comission, Types.DECIMAL);
		statement.setObject(2, this.site, Types.INTEGER);
		statement.setObject(3, this.startMonth.getValue(), Types.INTEGER);
		statement.setObject(4, this.startYear.getValue(), Types.INTEGER);
		statement.setObject(5, this.status.getValue(), Types.VARCHAR);
		statement.executeUpdate();

	}

	/**
	 * Getter of active comission for a given Site and date
	 * 
	 * @param <type>Integer</type>         siteId
	 * @param <type>Month</type>startMonth
	 * @param <type>Year</type>            startYear
	 * @return <type>Double</type>
	 * @throws SQLException - if a database access error occurs;this method is
	 *                      called on a closed PreparedStatementor an argument is
	 *                      supplied to this method
	 */
	public static Double getComissionSum(Integer siteId, Month startMonth, Year startYear) throws SQLException {
		Double totalCommision = 0.00;

		String reqSql = "Select Sum(comission) as Sum FROM Comission where chantier=? AND (AnneeDebut<? OR (AnneeDebut=? AND MoisDebut<=?))";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, siteId, Types.INTEGER);
		statement.setObject(2, startYear.getValue(), Types.INTEGER);
		statement.setObject(3, startYear.getValue(), Types.INTEGER);
		statement.setObject(4, startMonth.getValue(), Types.INTEGER);
		statement.execute();
		ResultSet result = statement.getResultSet();

		while (result.next()) {
			totalCommision += result.getDouble("Sum");
		}

		return totalCommision;
	}

	/**
	 * Change the status of the current Comission into the database to "Archivé"
	 * 
	 * @throws SQLException - if a database access error occurs;this method is
	 *                      called on a closed PreparedStatementor the SQL statement
	 *                      returns a ResultSet object
	 */
	public void Delete() throws SQLException {
		String reqSql = "Update  Comission SET Status='Archivé' WHERE ComissionId= .;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);

		statement.setObject(1, this.comissionId, Types.INTEGER);

		statement.executeUpdate();

	}

	/**
	 * Change a comission status from it's Id
	 * 
	 * @param <type> Integer</type>comissionId
	 * @throws SQLException - if a database access error occurs;this method is
	 *                      called on a closed PreparedStatementor the SQL statement
	 *                      returns a ResultSet objectSQLTimeoutException - when the
	 *                      driver has determined that thetimeout value that was
	 *                      specified by the setQueryTimeoutmethod has been exceeded
	 *                      and has at least attempted to cancelthe currently
	 *                      running Statement
	 */
	public static void removeById(Integer comissionId) throws SQLException {
		String reqSql = "Update  Comission SET Status='Archivé' WHERE ComissionId= ?;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);

		statement.setObject(1, comissionId, Types.INTEGER);

		statement.executeUpdate();

	}

	
	
	public void setStatus(Status status) {
		if (status == null) {
			throw new Error("setStatus : le status indique est vide");
		}
		this.status = status;
	}

}
