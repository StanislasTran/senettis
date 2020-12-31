
package classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import connexion.SQLDatabaseConnexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Month;
import java.time.Year;

/**
 * 
 * Represent the turnOver of a site on a given month and year
 *
 */
public class TurnOver {

	private Integer siteId;
	private Month month;
	private Year year;
	private Double cleaning;
	private Double glazing;
	private Double FS;
	private Double misesBlanc;
	private Double others;
	private Double turnOver;
	private String siteName;
	private Status status;

	/************************
	 * 
	 * Constructors
	 * 
	 ***********************/

	/**
	 * Constructor for TurnOver
	 * 
	 * @param <type>Integer</type>    chantierId
	 * @param <type>Month</type>month
	 * @param <type>Year</type>year
	 * @param<type>Status</type> status
	 */

	public TurnOver(Integer siteId, Month month, Year year, Status status) {
		super();

		this.siteId = siteId;
		this.month = month;
		this.year = year;
		this.status = status;
	}

	/**
	 * Constructor for TurnOver
	 * 
	 * @param <type>Integer</type>  siteId
	 * @param <type>Month</type>    Month
	 * @param <type>Year</type>year
	 * @param <type>Double          </type>cleaning
	 * @param <type>Double          </type>glazing
	 * @param <type>Double          </type>FS
	 * @param <type>Double          </type>misesBlanc
	 * @param <type>Double          </type> others
	 * @param <type>Double          </type> turnOver
	 * @param <type>Double</type>   status
	 */

	public TurnOver(Integer siteId, Month month, Year year, Double cleaning, Double glazing, Double FS,
			Double misesBlanc, Double others, Double turnOver, Status status) {
		this(siteId, month, year, status);

		this.siteId = siteId;
		this.month = month;
		this.year = year;
		this.cleaning = cleaning;
		this.glazing = glazing;
		this.FS = FS;
		this.misesBlanc = misesBlanc;
		this.others = others;
		this.turnOver = turnOver;

	}

	/**
	 * Constructor for TurnOver
	 * 
	 * @param <type> Integer </type> siteId
	 * @param <type> Month </type> month
	 * @param <type> Year </type> year
	 * @param <type> Double </type> cleaning
	 * @param <type> Double </type> glazing
	 * @param <type> Double </type> FS
	 * @param <type> Double </type> misesBlanc
	 * @param <type> Double </type> others
	 * @param <type> Double </type> turnOver
	 * @param <type> Status </type> status
	 */
	public TurnOver(Integer siteId, Month month, Year year, Double cleaning, Double glazing, Double FS,
			Double misesBlanc, Double others, Double turnOver, Status status, String siteName) {
		this(siteId, month, year, cleaning, glazing, FS, misesBlanc, others, turnOver, status);
		this.siteName = siteName;

	}

	/**
	 * Insert the current TurnOver into the Database
	 * 
	 * @return <type>int</type> either (1) the row count for SQL Data Manipulation
	 *         Language (DML) statementsor (2) 0 for SQL statements that return
	 *         nothing
	 * @throws SQLException
	 */
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO ChiffreAffaire(Mois, Annee,Chantier,   Menage,Vitrerie,Mise_a_blanc,Fournitures_sanitaires,Autres,CA,Status) VALUES (?,?,?,?,?,?,?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.month, Types.INTEGER);
		statement.setObject(2, this.year, Types.INTEGER);
		statement.setObject(3, this.siteId, Types.INTEGER);
		statement.setObject(8, this.cleaning, Types.DECIMAL);
		statement.setObject(9, this.glazing, Types.DECIMAL);
		statement.setObject(10, this.misesBlanc, Types.DECIMAL);
		statement.setObject(11, this.FS, Types.DECIMAL);
		statement.setObject(12, this.others, Types.DECIMAL);
		statement.setObject(13, this.status, Types.VARCHAR);

		return statement.executeUpdate();
	}

	/**
	 * return a resultSet which contains CA Data for each site (left joint) When
	 * data don't exist return null filtered on a given month and year
	 * 
	 * @param <type>Month</type> month
	 * @param <type>Year<type>   year
	 * @return <type>ResultSet</type> all CA for each Site with Status="Publié"
	 * @throws SQLException
	 */

	public static ResultSet getCAForAllSite(Month month, Year year) throws SQLException {
		String selection = " Chantier.nom,Chantier.chantierId, CA.Mois, CA.Annee,CA.Chantier,  CA.Menage,CA.Vitrerie,CA.Mise_a_blanc,CA.Fournitures_sanitaires,CA.Autres,CA.CA,CA.Status";
		String sources = "Chantier Left join (Select * FROM ChiffreAffaire WHERE Mois=? AND Annee = ?) AS CA ON CA.Chantier=Chantier.ChantierId WHERE Chantier.Status='Publié'";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		String reqSql = "Select " + selection + " FROM " + sources;
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, month.getValue(), Types.INTEGER);
		statement.setObject(2, year.getValue(), Types.INTEGER);

		statement.execute();

		return statement.getResultSet();

	}

	/**
	 * Return a list of all CA for a given month and year
	 * 
	 * @param <type>Month</type> month
	 * @param <type>Year<type>   year
	 * @return <type>List<TurnOver></type> all TurnOver
	 * @throws SQLException
	 */
	public static List<TurnOver> getListCAForAllSite(Month month, Year year) throws SQLException {
		ResultSet result = getCAForAllSite(month, year);
		List<TurnOver> allCA = new ArrayList<TurnOver>();

		while (result.next()) {
			Integer chantierId = result.getInt("chantierId");
			Double cleaning = result.getDouble("Menage");
			Double glazing = result.getDouble("Vitrerie");
			Double FS = result.getDouble("Fournitures_sanitaires");
			Double misesBlanc = result.getDouble("Mise_a_blanc");
			Double others = result.getDouble("autres");

			Double turnOver = result.getDouble("CA");

			Status status = Status.DRAFT;
			if (!Objects.isNull(result.getString("status")))

				status = Status.getStatus(result.getString("status"));

			String siteName = result.getString("nom");
			allCA.add(new TurnOver(chantierId, month, year, cleaning, glazing, FS, misesBlanc, others, turnOver, status,
					siteName));

		}

		return allCA;
	}

	/**
	 * Return a TurnOver from a siteId ,month and year
	 * 
	 * @param <type>Integer</type>siteId
	 * @param <type>Month</type>month
	 * @param <type>Year</type>          year
	 * @return <type> TurnOver </type> the turnerOver searched
	 * @throws SQLException - if a database access error occurs;this method is
	 *                      called on a closed PreparedStatement or the SQLstatement
	 *                      does not return a ResultSet objectSQLTimeoutException -
	 *                      when the driver has determined that thetimeout value
	 *                      that was specified by the setQueryTimeoutmethod has been
	 *                      exceeded and has at least attempted to cancelthe
	 *                      currently running Statement
	 */
	public static TurnOver getTurnOverByDateAndSite(Integer siteId, int month, int year) throws SQLException {
		String reqSql = "SELECT Mois, Annee,Chantier,Menage,Vitrerie,Mise_a_blanc,Fournitures_sanitaires,Autres,CA,Status FROM ChiffreAffaire WHERE chantier=? and mois=? and annee=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, siteId, Types.INTEGER);
		statement.setObject(2, month, Types.INTEGER);
		statement.setObject(3, year, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			Double cleaning = result.getDouble("Menage");
			Double glazing = result.getDouble("Vitrerie");
			Double FS = result.getDouble("Fournitures_sanitaires");
			Double misesBlanc = result.getDouble("Mise_a_blanc");
			Double others = result.getDouble("autres");

			Double turnOver = result.getDouble("CA");

			Status status = Status.DRAFT;
			if (!Objects.isNull(result.getString("status"))) {
				status = Status.getStatus(result.getString("status"));
			}

			return new TurnOver(siteId, Month.of(month), Year.of(year), cleaning, glazing, FS, misesBlanc, others,
					turnOver, status);
		} else {
			throw new SQLException("Data not found");
		}
	}

	/**
	 * Return a TurnOver from a siteId ,month and year
	 * 
	 * 
	 * @param <type>Month</type>month
	 * @param <type>Year</type>       year
	 * @return <type> TurnOver </type> the turnerOver searched
	 * @throws SQLException - if a database access error occurs;this method is
	 *                      called on a closed PreparedStatement or the SQLstatement
	 *                      does not return a ResultSet objectSQLTimeoutException -
	 *                      when the driver has determined that thetimeout value
	 *                      that was specified by the setQueryTimeoutmethod has been
	 *                      exceeded and has at least attempted to cancelthe
	 *                      currently running Statement
	 */
	public static Map<Integer, Double> getTurnOverByDateAndSite(Month  month, Year year) throws SQLException {

		Map<Integer, Double> turnOversMap = new HashMap<Integer, Double>();
		String reqSql = "SELECT Mois, Annee,Chantier,Menage,Vitrerie,Mise_a_blanc,Fournitures_sanitaires,Autres,CA,Status FROM ChiffreAffaire WHERE   mois=? and annee=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, month.getValue(), Types.INTEGER);
		statement.setObject(2, year.getValue(), Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		while (result.next()) {
			Integer siteId = result.getInt("Chantier");

			Double turnOver = result.getDouble("CA");

			turnOversMap.put(siteId, turnOver);

		}
		return turnOversMap;
	}

	/**
	 * Check if the turnOver exist in the database
	 * 
	 * @return <type>boolean</type> true if the row exist in the database else false
	 * @throws SQLException
	 */
	public boolean exist() throws SQLException {
		String reqSql = "Select count(*) AS count FROM ChiffreAffaire WHERE mois=? AND annee=? AND chantier=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.month.getValue(), Types.INTEGER);
		statement.setObject(2, this.year.getValue(), Types.INTEGER);
		statement.setObject(3, this.siteId, Types.INTEGER);

		statement.execute();
		ResultSet result = statement.getResultSet();

		if (result.next()) {

			return result.getInt("count") > 0;
		} else
			return false;

	}

	/**
	 * Insert the data into the database if not exist else update
	 * 
	 * @throws SQLException
	 */
	public void inserOrUpdateRow() throws SQLException {

		if (!exist()) {
			insert();

		} else {
			update();

		}

	}

	/**
	 * Insert the current TurnOver into the database
	 * 
	 * @throws SQLException - if a database access error occurs;this method is
	 *                      called on a closed PreparedStatementor an argument is
	 *                      supplied to this methodSQLTimeoutException - when the
	 *                      driver has determined that thetimeout value that was
	 *                      specified by the setQueryTimeoutmethod has been exceeded
	 *                      and has at least attempted to cancel the currently
	 *                      running Statement
	 */
	public void insert() throws SQLException {
		String insert = "ChiffreAffaire (mois,annee,chantier,CA,menage,vitrerie,fournitures_sanitaires,mise_a_blanc,autres,Status)";
		String values = " (?,?,?,?,?,?,?,?,?,?) ";
		String reqSql = "insert INTO " + insert + " VALUES " + values;
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);

		statement.setObject(1, this.month.getValue(), Types.INTEGER);
		statement.setObject(2, this.year.getValue(), Types.INTEGER);
		statement.setObject(3, this.siteId, Types.INTEGER);
		statement.setObject(4, this.turnOver, Types.DECIMAL);
		statement.setObject(5, this.cleaning, Types.DECIMAL);
		statement.setObject(6, this.glazing, Types.DECIMAL);
		statement.setObject(7, this.FS, Types.DECIMAL);
		statement.setObject(8, this.misesBlanc, Types.DECIMAL);
		statement.setObject(9, this.others, Types.DECIMAL);
		statement.setObject(10, "Publié", Types.VARCHAR);

		statement.execute();
	}

	/*
	 * Update the current turnOver into the database
	 */
	public void update() throws SQLException {

		String set = " CA=?,menage=?,vitrerie=?,fournitures_sanitaires=?,mise_a_blanc=?,autres=?";
		String reqSql = "UPDATE ChiffreAffaire SET " + set + " Where mois=? AND annee=? AND chantier=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);

		statement.setObject(1, this.turnOver, Types.DECIMAL);
		statement.setObject(2, this.cleaning, Types.DECIMAL);
		statement.setObject(3, this.glazing, Types.DECIMAL);
		statement.setObject(4, this.FS, Types.DECIMAL);
		statement.setObject(5, this.misesBlanc, Types.DECIMAL);
		statement.setObject(6, this.others, Types.DECIMAL);
		statement.setObject(7, this.month.getValue(), Types.INTEGER);
		statement.setObject(8, this.year.getValue(), Types.INTEGER);
		statement.setObject(9, this.siteId, Types.INTEGER);
		statement.execute();
	}

	/**
	 * Getter for the attribute siteName
	 * 
	 * @return <type>String</type>
	 */
	public String getSiteName() {
		return this.siteName;
	}

	/**
	 * Setter for the attribute siteName
	 * 
	 * @param <type>String</type> siteName
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	/**
	 * Getter for the attribute month
	 * 
	 * @return <type>Month</type>month
	 */
	public Month getMonth() {
		return month;
	}

	/**
	 * Setter for the attribute month
	 * 
	 * @param <type>Month</type>month
	 */
	public void setMonth(Month month) {
		this.month = month;
	}

	/**
	 * Getter for the attribute year
	 * 
	 * @return <type>year</type>
	 */
	public Year getYear() {
		return year;
	}

	/**
	 * Getter for the attribute turnOver
	 * 
	 * @return <type>double</type> turnOver
	 */
	public double getTurnOver() {
		return this.turnOver;
	}

	/**
	 * Getter for the attribute year
	 * 
	 * @param <type>Year </type>year
	 */
	public void setAnnee(Year year) {
		this.year = year;
	}

	/**
	 * Getter for the attribute cleaning
	 * 
	 * @return <type>Double</type> cleaning
	 */
	public Double getCleaning() {
		return cleaning;
	}

	/**
	 * Setter for the attribute cleaning
	 * 
	 * @param <type>Double</type> cleaning
	 */
	public void setCleaning(Double cleaning) {
		this.cleaning = cleaning;
	}

	/**
	 * Getter for the attribute glazing
	 * 
	 * @return </type>Double</type> glazing
	 */
	public Double getGlazing() {
		return glazing;
	}

	/**
	 * Setter for the attribute glazing
	 * 
	 * @param <type>Double</type> glazing
	 */
	public void setGlazing(Double glazing) {
		this.glazing = glazing;
	}

	/**
	 * Getter for the attribute FS
	 * 
	 * @return
	 */
	public Double getFS() {
		return FS;
	}

	/**
	 * Setter for the attribute FS
	 * 
	 * @param <type>Double </type>FS
	 */
	public void setFS(Double FS) {
		this.FS = FS;
	}

	/**
	 * Getter for the attribute misesBlanc
	 * 
	 * @return <type> Double </type> misesBlanc
	 */
	public Double getMisesBlanc() {
		return misesBlanc;
	}

	/**
	 * Setter for the attribute misesBlanc
	 * 
	 * @param <type> Double </type> misesBlanc
	 */
	public void setMisesBlanc(Double misesBlanc) {
		this.misesBlanc = misesBlanc;
	}

	/**
	 * Getter for the attribute others
	 * 
	 * @return <type> Double </type> others
	 */
	public Double getOthers() {
		return others;
	}

	/**
	 * Setter for the attribute others
	 * 
	 * @param <type> Double </type> others
	 */
	public void setAutres(Double others) {
		this.others = others;
	}

	/**
	 * Getter for the attribute siteId
	 * 
	 * @return <type> Integer </type>
	 */
	public Integer getSiteId() {
		return this.siteId;
	}

	/**
	 * Setter for the attribute turnOver
	 * 
	 * @param <type>Double</type> turnOver
	 */
	public void setTurnOver(Double turnOver) {
		this.turnOver = turnOver;

	}

}
