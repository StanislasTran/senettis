
package classes;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import connexion.SQLDatabaseConnexion;

/**
 * 
 * SiteAssignment represent the number of hours affected to an employee to a
 * site A SiteAssinment has a startDate to describe when the system has to
 * consider the assignments into computation
 *
 */
public class SiteAssignment {

	private Integer assignementId;
	private Integer site;
	private Integer employee;
	private Double nbHours;

	private Month startMonth;
	private Year startYear;
	private Status status;

	/**********************
	 * 
	 * Constructors
	 * 
	 *********************/

	/**
	 * Constructor for Affectation
	 * 
	 * @param site
	 * @param employee
	 * @param status
	 */
	public SiteAssignment(Integer site, Integer employee, Month startMonth, Year startYear, Status status) {
		super();
		this.site = site;
		this.employee = employee;
		this.status = status;
		this.startMonth = startMonth;
		this.startYear = startYear;
	}

	/**
	 * Constructor for Affectation
	 * 
	 * @param site
	 * @param idEmploye
	 * @param month
	 * @param year
	 * @param nombreHeures
	 * @param status
	 */
	public SiteAssignment(Integer site, Integer idEmploye, Double nombreHeures, Month startMonth, Year startYear,
			Status status) {
		this(site, idEmploye, startMonth, startYear, status);
		this.nbHours = nombreHeures;
	}

	/**
	 * Constructor for Affectation
	 * 
	 * @param affectationId
	 * @param site
	 * @param idEmploye
	 * @param nombreHeures
	 * @param status
	 */
	public SiteAssignment(Integer affectationId, Integer site, Integer idEmploye, Double nombreHeures, Month startMonth,
			Year startYear, Status status) {
		this(site, idEmploye, nombreHeures, startMonth, startYear, status);
		this.assignementId = affectationId;
	}

	/*******************************
	 * 
	 * Database Queries
	 * 
	 ******************************/

	/**
	 * 
	 * Insert a new row in the table "Produit" in the database by using the
	 * attributes of the product
	 * 
	 * @return <type> int </type> number of row returned by the query
	 * @throws SQLException
	 */

	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO AffectationChantier(chantier,employe,nombre_heures,MoisDebut,AnneeDebut,status) VALUES (?,?,?,?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.site, Types.INTEGER);
		statement.setObject(2, this.employee, Types.INTEGER);
		statement.setObject(3, this.nbHours, Types.DECIMAL);
		statement.setObject(4, this.startMonth.getValue(), Types.INTEGER);
		statement.setObject(5, this.startYear.getValue(), Types.INTEGER);
		statement.setObject(6, this.status.getValue(), Types.VARCHAR);

		return statement.executeUpdate();
	}

	/**
	 * update the row with the actual attributes of the product where the
	 * AffectationId in database is equal to the AffectationId of the product
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE AffectationChantier SET Employe=?, Chantier=?, nombre_heures=?,MoisDebut=?,AnneeDebut=?, status=? WHERE AffectationId=?;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.employee.toString(), Types.INTEGER);
		statement.setObject(2, this.site, Types.INTEGER);
		statement.setObject(3, this.nbHours, Types.DECIMAL);
		statement.setObject(3, this.startMonth.getValue(), Types.INTEGER);
		statement.setObject(4, this.startYear.getValue(), Types.INTEGER);
		statement.setObject(5, this.status.getValue(), Types.VARCHAR);
		statement.setObject(6, this.assignementId, Types.INTEGER);

		return statement.executeUpdate();
	}

	/**
	 * Select all data from Affectation table
	 * 
	 * @return <type> Statement</type> statement contain all data from the table
	 *         Affectation
	 * @throws SQLException
	 */
	private static Statement selectAllAffectation() throws SQLException {
		String reqSql = "SELECT * FROM AffectationChantier";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}

	/**
	 * Select all data from Affectation table and return the list of
	 * <type>Affectation</type> which contain all affectation
	 * 
	 * @return <type> List<Affectation> </type> list of all affectation store in the
	 *         database
	 * @throws SQLException
	 */
	public static List<SiteAssignment> getAllAffectation() throws SQLException {

		ResultSet result = selectAllAffectation().getResultSet();
		List<SiteAssignment> allAffectation = new ArrayList<SiteAssignment>();

		while (result.next()) {
			Integer affectationId = result.getInt("AffectationId");
			Integer employeId = result.getInt("Employe");
			Integer chantierId = result.getInt("Chantier");
			Double nombreHeures = result.getDouble("Nombre_heures");
			Month startMonth = Month.of(result.getInt("MoisDebut"));
			Year startYear = Year.of(result.getInt("AnneeDebut"));
			Status status = Status.getStatus(result.getString("Status"));
			allAffectation.add(new SiteAssignment(affectationId, employeId, chantierId, nombreHeures, startMonth,
					startYear, status));

		}

		return allAffectation;
	}

	/**
	 * Return a list of stats for each employee in a <type> ResultSet</type> Column
	 * 1 : EmployeId Column 2 : Name Column 3 : Prenom Column 4 : Number of site
	 * affected Column 5 : Sum of all hours affected
	 * 
	 * @return <type> ResultSet </type> statement ResultSet
	 * @throws SQLException
	 */
	public static ResultSet getEmployeStatsPublished() throws SQLException {
		String selection = "emplData.EmployeId AS 'EmployeId',emplData.Nom,EmplData.prenom,count(DISTINCT AffectationChantier.chantier) as 'nb_chantier',SUM(AffectationChantier.Nombre_heures) as 'nb_heure'";
		String source = "(Select * FROM AffectationChantier WHERE Status='Publié') as AffectationChantier  RIGHT JOIN (Select distinct EmployeId,Nom,Prenom FROM Employe WHERE Status='Publié') AS emplData ON emplData.EmployeId=AffectationChantier.Employe";
		String group = "emplData.EmployeId,emplData.Nom,EmplData.prenom";

		String reqSql = "SELECT " + selection + " FROM " + source + " GROUP BY " + group  + " ORDER BY emplData.Nom";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.execute(reqSql);

		return statement.getResultSet();

	}

	/**
	 * Return a list of stats for each employee in a <type> ResultSet</type> Column
	 * for a period 1 : EmployeId Column 2 : Name Column 3 : Prenom Column 4 :
	 * Number of site affected Column 5 : Sum of all hours affected
	 * 
	 * @return <type> ResultSet </type> statement ResultSet
	 * @throws SQLException
	 */
	public static ResultSet getEmployeStats(Month startMonth, Year startYear) throws SQLException {
		String selection = "emplData.EmployeId AS 'EmployeId',emplData.Nom,EmplData.prenom,count(DISTINCT AffectationChantier.chantier) as 'nb_chantier',SUM(AffectationChantier.Nombre_heures) as 'nb_heure'";
		String source = "(select * FROM AffectationChantier WHERE MoisDebut='" + startMonth.getValue()
				+ "' AND AnneeDebut='" + startYear.getValue() + " AND Status='Publié' ) AS AffectationChantier RIGHT JOIN (Select distinct EmployeId,Nom,Prenom FROM Employe WHERE Status='Publié') AS emplData ON emplData.EmployeId=AffectationChantier.Employe";
		String group = "emplData.EmployeId,emplData.Nom,EmplData.prenom";

		String reqSql = "SELECT " + selection + " FROM " + source + " GROUP BY " + group  + " ORDER BY emplData.Nom";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.execute(reqSql);
		return statement.getResultSet();

	}

	/**
	 * Return a list of stats for each employee in a <type> ResultSet</type> Column
	 * for a period 1 : EmployeId Column 2 : Name Column 3 : Prenom Column 4 :
	 * Number of site affected Column 5 : Sum of all hours affected
	 * 
	 * @return <type> ResultSet </type> statement ResultSet
	 * @throws SQLException
	 */
	public static ResultSet getEmployeStats() throws SQLException {
		String selection = "emplData.EmployeId AS 'EmployeId',emplData.Nom,EmplData.prenom,count(DISTINCT AffectationChantier.chantier) as 'nb_chantier',SUM(AffectationChantier.Nombre_heures) as 'nb_heure'";
		String source = " (SELECT * FROM AffectationChantier WHERE Status='Publié') AS AffectationChantier RIGHT JOIN (Select distinct EmployeId,Nom,Prenom FROM Employe WHERE Status='Publié') AS emplData ON emplData.EmployeId=AffectationChantier.Employe";
		String group = "emplData.EmployeId,emplData.Nom,EmplData.prenom";

		String reqSql = "SELECT " + selection + " FROM " + source + " GROUP BY " + group + " ORDER BY emplData.Nom";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.execute(reqSql);
		return statement.getResultSet();

	}

	/**
	 * Return a list of stats for each site in a <type> ResultSet</type> Column 1 :
	 * site Column 2 : name Column 3 : CA Column 4 : Number of Employee affected
	 * Column 5 : Sum of all hours affected
	 * 
	 * @return <type> ResultSet </type> statement ResultSet
	 * @throws SQLException
	 */
	public static ResultSet getChantierStats() throws SQLException {
		String selection = "chantData.ChantierId AS 'ChantierId',ChantData.Nom,count(DISTINCT AffectationChantier.Employe) as 'nb_Employe',SUM(AffectationChantier.Nombre_heures) as 'nb_heure' ";
		String source = "(SELECT * FROM AffectationChantier WHERE Status='Publié') AS AffectationChantier RIGHT JOIN (Select DISTINCT ChantierId,Nom FROM Chantier WHERE Status='Publié') AS chantData ON chantData.ChantierId=AffectationChantier.Chantier";
		String group = "chantData.ChantierId,chantData.Nom";

		String reqSql = "SELECT " + selection + " FROM " + source + " GROUP BY " + group + " ORDER BY ChantData.Nom";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		Statement statement = connection.createStatement();

		statement.execute(reqSql);

		return statement.getResultSet();

	}

	/**
	 * Return a list of stats for each site in a <type> ResultSet</type> Column 1 :
	 * site Column 2 : name Column 3 : CA Column 4 : Number of Employee affected
	 * Column 5 : Sum of all hours affected
	 * 
	 * @return <type> ResultSet </type> statement ResultSet
	 * @throws SQLException
	 */
	public static ResultSet getChantierStats(Month startMonth, Year startYear) throws SQLException {
		String selection = "chantData.ChantierId AS 'ChantierId',ChantData.Nom,count(DISTINCT AffectationChantier.Employe) as 'nb_Employe',SUM(AffectationChantier.Nombre_heures) as 'nb_heure' ";
		String source = "( Select * FROM AffectationChantier WHERE MoisDebut=? AND AnneeDebut=? AND Status='Publié') AS AffectationChantier RIGHT JOIN (Select DISTINCT ChantierId,Nom FROM Chantier WHERE Status='Publié') AS chantData ON chantData.ChantierId=AffectationChantier.Chantier";
		String group = "chantData.ChantierId,chantData.Nom";

		String reqSql = "SELECT " + selection + " FROM " + source + " GROUP BY " + group + " ORDER BY ChantData.Nom";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, startMonth.getValue(), Types.INTEGER);
		statement.setObject(2, startYear.getValue(), Types.INTEGER);
		statement.execute();
		return statement.getResultSet();

	}

	/**
	 * Execute a query and return the list of all employee affected to the site
	 * entered in parameter
	 * 
	 * @param employeId
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet getSiteAffectationPublished(int site, Month startMonth, Year startYear)
			throws SQLException {
		String selection = "AffectationId,Nom,Prenom,AffectationChantier.Nombre_heures,AffectationChantier.AffectationId,Numero_matricule";
		String source = "(Select * From Employe WHERE Employe.Status='Publié') as Employe INNER JOIN (Select * FROM AffectationChantier WHERE AffectationChantier.Status='Publié') as AffectationChantier ON Employe.EmployeId=AffectationChantier.Employe ";
		String condition = "AffectationChantier.Chantier=? AND MoisDebut=? AND AnneeDebut=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		String reqSql = "SELECT " + selection + " FROM " + source + " WHERE " + condition;

		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, site, Types.INTEGER);
		statement.setObject(2, startMonth.getValue(), Types.INTEGER);
		statement.setObject(3, startYear.getValue(), Types.INTEGER);
		statement.execute();
		return statement.getResultSet();
	}

	/**
	 * Execute a query and return the list of all employee affected to the site
	 * entered in parameter Only where Status ='Publié'
	 * 
	 * @param employeId
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet getSiteAffectationPublished(int site) throws SQLException {
		String selection = "AffectationId,Nom,Prenom,AffectationChantier.Nombre_heures,AffectationChantier.AffectationId,Numero_matricule,MoisDebut, AnneeDebut";
		String source = "(Select * from Employe WHERE Employe.Status='Publié' ) as Employe INNER JOIN (Select * from AffectationChantier WHERE AffectationChantier.Status='Publié') as AffectationChantier ON Employe.EmployeId=AffectationChantier.Employe  ";
		String condition = "AffectationChantier.Chantier=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		String reqSql = "SELECT " + selection + " FROM " + source + " WHERE " + condition;

		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, site, Types.INTEGER);
		statement.execute();

		return statement.getResultSet();
	}

	/**
	 * Execute a query and return the list of all site affected to the employeId
	 * entered in parameter
	 * 
	 * @param employeId
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet getEmployeAffectationPubished(int employeId) throws SQLException {
		String selection = "ChantierId,nom,adresse,Nombre_heures,AffectationChantier.AffectationId";
		String source = " (Select * from Chantier where Chantier.Status='Publié') as Chantier INNER JOIN (Select * FROM AffectationChantier WHERE AffectationChantier.Status='Publié') as AffectationChantier ON Chantier.ChantierId=AffectationChantier.Chantier ";
		String condition = "AffectationChantier.Employe=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		String reqSql = "SELECT " + selection + " FROM " + source + " WHERE " + condition;

		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, employeId, Types.INTEGER);
		statement.execute();
		return statement.getResultSet();
	}

	/**
	 * Execute a query and return the list of all site affected to the employeId
	 * entered in parameter where the affectation is Published
	 * 
	 * @param employeId
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet getEmployeAffectationPublished(int employeId) throws SQLException {
		String selection = "ChantierId,nom,MoisDebut,AnneeDebut,Nombre_heures,AffectationChantier.AffectationId";
		String source = "chantier INNER JOIN AffectationChantier ON Chantier.ChantierId=AffectationChantier.Chantier ";
		String condition = "AffectationChantier.Employe=?  AND AffectationChantier.Status='Publié' AND Chantier.Status='Publié'";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		String reqSql = "SELECT " + selection + " FROM " + source + " WHERE " + condition;

		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, employeId, Types.INTEGER);

		statement.execute();
		return statement.getResultSet();
	}

	/**
	 * Execute a query to obtain an affectation from an <param>
	 * affectationId</param> return the result of the query
	 * 
	 * 
	 * @param <type> Integer</type>affectationId
	 * @return <type>ResultSet</type>
	 * @throws SQLException
	 */
	public static ResultSet getAffectationResultSet(Integer affectationId) throws SQLException {

		String selection = "AffectationId,Chantier,Employe,Nombre_heures,MoisDebut,AnneeDebut,Status";
		String source = "AffectationChantier";

		String condition = "AffectationChantier.AffectationId=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		String reqSql = "SELECT " + selection + " FROM " + source + " WHERE " + condition;

		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, affectationId, Types.INTEGER);
		statement.execute();
		return statement.getResultSet();

	}

	/**
	 * Search and return an <type>Affectation</type> affectation by using <param>
	 * affectationid</param>
	 * 
	 * 
	 * @param affectationId
	 * @return <type> Affectation</type>
	 * @throws SQLException
	 */
	public static SiteAssignment getAffectation(int affectationId) throws SQLException {
		ResultSet result = getAffectationResultSet(affectationId);

		if (result.next()) {

			int chantier = result.getInt("Chantier");
			int employe = result.getInt("Employe");
			Double nombre_heures;
			if (!Objects.isNull(result.getDouble("Nombre_heures")))
				nombre_heures = result.getDouble("Nombre_heures");
			else
				nombre_heures = 0.0;
			Month startMonth = Month.of(result.getInt("MoisDebut"));
			Year startYear = Year.of(result.getInt("AnneeDebut"));
			Status status = Status.getStatus(result.getString("status"));

			return new SiteAssignment(affectationId, chantier, employe, nombre_heures, startMonth, startYear, status);
		}

		throw new SQLException("data not found");
	}

	/**
	 * update an Affectation in the table Affectation by using attribute of the
	 * product
	 * 
	 * @throws SQLException
	 */
	public int update() throws SQLException {
		String reqSql = "UPDATE AffectationChantier SET Chantier=? , Employe=?,Nombre_heures=?, MoisDebut=?,AnneeDebut=? WHERE affectationId=?";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.site, Types.INTEGER);
		statement.setObject(2, this.employee, Types.INTEGER);
		statement.setObject(3, this.nbHours, Types.DECIMAL);
		statement.setObject(4, this.startMonth.getValue(), Types.INTEGER);
		statement.setObject(5, this.startYear.getValue(), Types.INTEGER);
		statement.setObject(6, this.assignementId, Types.INTEGER);

		return statement.executeUpdate();

	}

	/**
	 * update an Affectation in the table Affectation by using attribute of the
	 * product
	 * 
	 * @throws SQLException
	 */
	public int remove() throws SQLException {
		String reqSql = "UPDATE AffectationChantier SET Status='Archivé' WHERE affectationId=?";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);

		statement.setObject(1, this.assignementId, Types.INTEGER);

		return statement.executeUpdate();

	}

	/***************************
	 * 
	 * getter and setter
	 * 
	 **************************/

	/**
	 * getter for the attribute status
	 * 
	 * @return <type> Status </type>
	 */

	public Status getStatus() {
		return status;
	}

	/**
	 * setter for the attribute status
	 * 
	 * @param status
	 */
	public void setStatus(Status status) {
		if (status == null) {
			throw new Error("setStatus : le status indique est vide");
		}
		this.status = status;
	}

	/**
	 * getter for the attribute site
	 * 
	 * @return <type> Integer</type> site
	 */
	public Integer getSite() {
		return site;
	}

	/**
	 * setter for the attribute site
	 * 
	 * @param <type> Integer </type> site
	 */
	public void setSite(Integer site) {
		if (site == null) {
			throw new Error("setsite : le site indique est vide");
		}
		this.site = site;
	}

	/**
	 * getter for the attribute idEmploye
	 * 
	 * @return <type> Integer</type> idEmploye
	 */
	public Integer getEmployee() {
		return employee;
	}

	/**
	 * setter for the attribute idEmploye
	 * 
	 * @param <type> Integer </type>idEmploye
	 */
	public void setEmployee(Integer employee) {
		if (employee == null) {
			throw new Error("setIdEmploye : le idEmploye indique est vide");
		}
		this.employee = employee;
	}

	/**
	 * getter for the attribute nbHours
	 * 
	 * @return <type> Double </type> nbHours
	 */
	public Double getNbHours() {
		return nbHours;
	}

	/**
	 * setter for the attribute nbHours
	 * 
	 * @param <type> Double </type> nbHours
	 */
	public void setNombreHeures(Double nbHours) {
		if (nbHours == null) {
			throw new Error("setNombreHeures : le nombreHeures indique est vide");
		}
		this.nbHours = nbHours;
	}

	/**
	 * getter for the attribute AffectationId
	 * 
	 * @return <type> Integer </type> affectationId
	 */
	public Integer getAffectationId() {
		return assignementId;
	}

	/**
	 * setter for the attribute affectationId
	 * 
	 * @param <type> Integer </type> affectationId
	 */
	public void setAssignment(Integer affectationId) {
		if (affectationId == null) {
			throw new Error("setAffectationId : le AffectationId indique est vide");
		}
		this.assignementId = affectationId;
	}

	/**
	 * getter for the attribute month
	 * 
	 * @return <type> Month </type> month
	 */
	public Month getStartMonth() {
		return this.startMonth;

	}

	public void setStartMonth(Month startMonth) {
		if (Objects.isNull(startMonth))
			throw new IllegalArgumentException("month can't be null");
		this.startMonth = startMonth;
	}

	/**
	 * getter for the attribute Year
	 * 
	 * @return <type> Integer </type> year
	 */
	public Year getStartYear() {
		return this.startYear;
	}

	/**
	 * Setter for the attribute year
	 * 
	 * @param <type> Integer </type>year
	 */
	public void setStartYear(Year startYear) {
		if (Objects.isNull(startYear))
			throw new IllegalArgumentException("year can't be null");
		this.startYear = startYear;

	}

	/****************************
	 * 
	 * Usefull function
	 * 
	 ***************************/

	/**
	 * print all affectation stored in the database
	 * 
	 * @throws SQLException
	 */

	public static void printAllAffectation() throws SQLException {

		List<SiteAssignment> allAffectation = getAllAffectation();

		for (SiteAssignment affectation : allAffectation)
			System.out.println(affectation);
	}

	@Override
	public String toString() {

		return "" + this.assignementId + "|" + this.employee + "|" + this.site + "|" + this.nbHours + "|" + this.status;
	}

	public int getIdEmploye() {
		// TODO Auto-generated method stub
		return employee;
	}

	public int getIdChantier() {
		// TODO Auto-generated method stub
		return site;
	}

	public Double getNombreHeures() {
		// TODO Auto-generated method stub
		return nbHours;
	}


}
