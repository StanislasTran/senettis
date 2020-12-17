
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

import connexion.SQLDatabaseConnection;

public class AffectationMiseABlanc {

	private Integer affectationId;
	private Integer idChantier;
	private Integer idEmploye;
	private Double nbHours;

	private Month month;
	private Year year;
	private Status status;

	/**********************
	 * 
	 * Constructors
	 * 
	 *********************/

	/**
	 * Constructor for Affectation
	 * 
	 * @param idChantier
	 * @param idEmploye
	 * @param status
	 */
	public AffectationMiseABlanc(Integer idChantier, Integer idEmploye, Month month, Year year, Status status) {
		super();
		this.idChantier = idChantier;
		this.idEmploye = idEmploye;
		this.status = status;
		this.month = month;
		this.year = year;
	}

	/**
	 * Constructor for Affectation
	 * 
	 * @param idChantier
	 * @param idEmploye
	 * @param month
	 * @param year
	 * @param nombreHeures
	 * @param status
	 */
	public AffectationMiseABlanc(Integer idChantier, Integer idEmploye, Double nombreHeures, Month month, Year year,
			Status status) {
		this(idChantier, idEmploye, month, year, status);
		this.nbHours = nombreHeures;
	}

	/**
	 * Constructor for Affectation
	 * 
	 * @param affectationId
	 * @param idChantier
	 * @param idEmploye
	 * @param nombreHeures
	 * @param status
	 */
	public AffectationMiseABlanc(Integer affectationId, Integer idChantier, Integer idEmploye, Double nombreHeures,
			Month month, Year year, Status status) {
		this(idChantier, idEmploye, nombreHeures, month, year, status);
		this.affectationId = affectationId;
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
		String reqSql = "INSERT INTO AffectationMAB(chantier,employe,nombre_heures,Mois,Annee,status) VALUES (?,?,?,?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.idChantier, Types.INTEGER);
		statement.setObject(2, this.idEmploye, Types.INTEGER);
		statement.setObject(3, this.nbHours, Types.DECIMAL);
		statement.setObject(4, this.month.getValue(), Types.INTEGER);
		statement.setObject(5, this.year.getValue(), Types.INTEGER);
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
		String reqSql = "UPDATE AffectationMAB SET Employe=?, Chantier=?, nombre_heures=?,Mois=?,Annee=?, status=? WHERE AffectationId=?;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.idEmploye.toString(), Types.INTEGER);
		statement.setObject(2, this.idChantier, Types.INTEGER);
		statement.setObject(3, this.nbHours, Types.DECIMAL);
		statement.setObject(3, this.month.getValue(), Types.INTEGER);
		statement.setObject(4, this.year.getValue(), Types.INTEGER);
		statement.setObject(5, this.status.getValue(), Types.VARCHAR);
		statement.setObject(6, this.affectationId, Types.INTEGER);

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
		String reqSql = "SELECT * FROM AffectationMAB";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
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
	public static List<AffectationMiseABlanc> getAllAffectation() throws SQLException {

		ResultSet result = selectAllAffectation().getResultSet();
		List<AffectationMiseABlanc> allAffectation = new ArrayList<AffectationMiseABlanc>();

		while (result.next()) {
			Integer affectationId = result.getInt("AffectationId");
			Integer employeId = result.getInt("Employe");
			Integer chantierId = result.getInt("Chantier");
			Double nombreHeures = result.getDouble("Nombre_heures");
			Month month = Month.of(result.getInt("Mois"));
			Year year = Year.of(result.getInt("Annee"));
			Status status = Status.getStatus(result.getString("Status"));
			allAffectation.add(
					new AffectationMiseABlanc(affectationId, employeId, chantierId, nombreHeures, month, year, status));

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
		String selection = "emplData.EmployeId AS 'EmployeId',emplData.Nom,EmplData.prenom,count(DISTINCT AffectationMAB.chantier) as 'nb_chantier',SUM(AffectationMAB.Nombre_heures) as 'nb_heure'";
		String source = "(Select * FROM AffectationMAB WHERE Status='Published' as AffectationMAB  RIGHT JOIN (Select distinct EmployeId,Nom,Prenom FROM Employe WHERE Status='Published') AS emplData ON emplData.EmployeId=AffectationMAB.Employe";
		String group = "emplData.EmployeId,emplData.Nom,EmplData.prenom";

		String reqSql = "SELECT " + selection + " FROM " + source + " GROUP BY " + group;
		System.out.println(reqSql);

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
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
	public static ResultSet getEmployeStats(Month month, Year year) throws SQLException {
		String selection = "emplData.EmployeId AS 'EmployeId',emplData.Nom,EmplData.prenom,count(DISTINCT AffectationMAB.chantier) as 'nb_chantier',SUM(AffectationMAB.Nombre_heures) as 'nb_heure'";
		String source = "(select * FROM AffectationMAB WHERE Mois='" + month.getValue() + "' AND Annee='"
				+ year.getValue()
				+ "' ) AS AffectationMAB  RIGHT JOIN (Select distinct EmployeId,Nom,Prenom FROM Employe) AS emplData ON emplData.EmployeId=AffectationMAB.Employe";
		String group = "emplData.EmployeId,emplData.Nom,EmplData.prenom";

		String reqSql = "SELECT " + selection + " FROM " + source + " GROUP BY " + group;
		System.out.println(reqSql);

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.execute(reqSql);
		return statement.getResultSet();

	}

	/**
	 * Return a list of stats for each site in a <type> ResultSet</type> Column 1 :
	 * siteId Column 2 : name Column 4 : Number of Employee affected Column 5 : Sum
	 * of all hours affected
	 * 
	 * @return <type> ResultSet </type> statement ResultSet
	 * @throws SQLException
	 */
	public static ResultSet getChantierStats() throws SQLException {
		String selection = "chantData.ChantierId AS 'ChantierId',ChantData.Nom,ChantData.CA,count(DISTINCT AffectationMAB.Employe) as 'nb_Employe',SUM(AffectationMAB.Nombre_heures) as 'nb_heure' ";
		String source = "AffectationMAB RIGHT JOIN (Select DISTINCT ChantierId,Nom FROM Chantier) AS chantData ON chantData.ChantierId=AffectationMAB.Chantier";
		String group = "chantData.ChantierId,chantData.Nom,chantData.CA";

		String reqSql = "SELECT " + selection + " FROM " + source + " GROUP BY " + group;

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		Statement statement = connection.createStatement();

		statement.execute(reqSql);

		return statement.getResultSet();

	}

	/**
	 * Return a list of stats for each site in a <type> ResultSet</type> Column 1 :
	 * siteId Column 2 : name column 3: Number of Employee affected Column 5 : Sum
	 * of all hours affected
	 * 
	 * @return <type> ResultSet </type> statement ResultSet
	 * @throws SQLException
	 */
	public static ResultSet getChantierStats(Month month, Year year) throws SQLException {
		String selection = "chantData.ChantierId AS 'ChantierId',ChantData.Nom,count(DISTINCT AffectationMAB.Employe) as 'nb_Employe',SUM(AffectationMAB.Nombre_heures) as 'nb_heure' ";
		String source = "( Select * FROM AffectationMAB WHERE Mois=? AND Annee=?) AS AffectationMAB RIGHT JOIN (Select DISTINCT ChantierId , Nom FROM Chantier) AS chantData ON chantData.ChantierId=AffectationMAB.Chantier";
		String group = "chantData.ChantierId,chantData.Nom"; 

		String reqSql = "SELECT " + selection + " FROM " + source + " GROUP BY " + group;

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, month.getValue(), Types.INTEGER);
		statement.setObject(2, year.getValue(), Types.INTEGER);
		statement.execute();

		return statement.getResultSet();

	}

	/**
	 * Execute a query and return the list of all employee affected to the siteId
	 * entered in parameter
	 * 
	 * @param employeId
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet getSiteAffectationPublished(int siteId, Month month, Year year) throws SQLException {
		String selection = "AffectationId,Nom,Prenom,AffectationMAB.Nombre_heures,AffectationMAB.AffectationId,Numero_matricule";
		String source = "(Select * From Employe WHERE Employe.Status='Publié') as Employe INNER JOIN (Select * FROM AffectationMAB WHERE AffectationMAB.Status='Publié') as AffectationMAB ON Employe.EmployeId=AffectationMAB.Employe ";
		String condition = "AffectationMAB.Chantier=? AND Mois=? AND Annee=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		String reqSql = "SELECT " + selection + " FROM " + source + " WHERE " + condition;

		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, siteId, Types.INTEGER);
		statement.setObject(2, month.getValue(), Types.INTEGER);
		statement.setObject(3, year.getValue(), Types.INTEGER);
		statement.execute();
		return statement.getResultSet();
	}

	/**
	 * Execute a query and return the list of all employee affected to the siteId
	 * entered in parameter Only where Status ='Publié'
	 * 
	 * @param employeId
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet getSiteAffectationPublished(int siteId) throws SQLException {
		String selection = "AffectationId,Nom,Prenom,AffectationMAB.Nombre_heures,AffectationMAB.AffectationId,Numero_matricule";
		String source = "(Select * from Employe WHERE Employe.Status='Publié' as Employe INNER JOIN (Select * from AffectationMAB WHERE AffectationMAB.Status='Publié') as AffectationMAB ON Employe.EmployeId=AffectationMAB.Employe  ";
		String condition = "AffectationMAB.Chantier=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		String reqSql = "SELECT " + selection + " FROM " + source + " WHERE " + condition;

		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, siteId, Types.INTEGER);
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
		String selection = "ChantierId,nom,adresse,Nombre_heures,AffectationMAB.AffectationId";
		String source = " (Select * from Chantier where Chantier.Status='Publié') as Chantier INNER JOIN (Select * FROM AffectationMAB WHERE AffectationMAB.Status='Publié') as AffectationMAB ON Chantier.ChantierId=AffectationMAB.Chantier ";
		String condition = "AffectationMAB.Employe=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
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
	public static ResultSet getEmployeAffectationPublished(int employeId, Month month, Year year) throws SQLException {
		String selection = "ChantierId,nom,adresse,Nombre_heures,AffectationMAB.AffectationId";
		String source = "chantier INNER JOIN AffectationMAB ON Chantier.ChantierId=AffectationMAB.Chantier ";
		String condition = "AffectationMAB.Employe=? AND Mois=? AND Annee=? AND AffectationMAB.Status='Publié' AND Chantier.Status='Publié'";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		String reqSql = "SELECT " + selection + " FROM " + source + " WHERE " + condition;

		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, employeId, Types.INTEGER);
		statement.setObject(2, month.getValue(), Types.INTEGER);
		statement.setObject(3, year.getValue(), Types.INTEGER);
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

		String selection = "AffectationId,Chantier,Employe,Nombre_heures,Mois,Annee,Status";
		String source = "AffectationMAB";

		String condition = "AffectationMAB.AffectationId=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
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
	public static AffectationMiseABlanc getAffectation(int affectationId) throws SQLException {
		ResultSet result = getAffectationResultSet(affectationId);
		System.out.println(affectationId + "affectationId");
		if (result.next()) {
			System.out.println(result.toString());
			int chantier = result.getInt("Chantier");
			int employe = result.getInt("Employe");
			Double nombre_heures;
			if (!Objects.isNull(result.getDouble("Nombre_heures")))
				nombre_heures = result.getDouble("Nombre_heures");
			else
				nombre_heures = 0.0;
			Month month = Month.of(result.getInt("Mois"));
			Year year = Year.of(result.getInt("Annee"));
			Status status = Status.getStatus(result.getString("status"));

			return new AffectationMiseABlanc(affectationId, chantier, employe, nombre_heures, month, year, status);
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
		String reqSql = "UPDATE AffectationMAB SET Chantier=? , Employe=?,Nombre_heures=?, Mois=?,Annee=? WHERE affectationId=?";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.idChantier, Types.INTEGER);
		statement.setObject(2, this.idEmploye, Types.INTEGER);
		statement.setObject(3, this.nbHours, Types.DECIMAL);
		statement.setObject(4, this.month.getValue(), Types.INTEGER);
		statement.setObject(5, this.year.getValue(), Types.INTEGER);
		statement.setObject(6, this.affectationId, Types.INTEGER);

		return statement.executeUpdate();

	}

	/**
	 * update an Affectation in the table Affectation by using attribute of the
	 * product
	 * 
	 * @throws SQLException
	 */
	public int remove() throws SQLException {
		String reqSql = "UPDATE AffectationMAB SET Status='Archivé' WHERE affectationId=?";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);

		statement.setObject(1, this.affectationId, Types.INTEGER);

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
	 * getter for the attribute idChantier
	 * 
	 * @return <type> Integer</type> idChantier
	 */
	public Integer getIdChantier() {
		return idChantier;
	}

	/**
	 * setter for the attribute idChantier
	 * 
	 * @param <type> Integer </type> idChantier
	 */
	public void setIdChantier(Integer idChantier) {
		if (idChantier == null) {
			throw new Error("setIdChantier : le idChantier indique est vide");
		}
		this.idChantier = idChantier;
	}

	/**
	 * getter for the attribute idEmploye
	 * 
	 * @return <type> Integer</type> idEmploye
	 */
	public Integer getIdEmploye() {
		return idEmploye;
	}

	/**
	 * setter for the attribute idEmploye
	 * 
	 * @param <type> Integer </type>idEmploye
	 */
	public void setIdEmploye(Integer idEmploye) {
		if (idEmploye == null) {
			throw new Error("setIdEmploye : le idEmploye indique est vide");
		}
		this.idEmploye = idEmploye;
	}

	/**
	 * getter for the attribute nbHours
	 * 
	 * @return <type> Double </type> nbHours
	 */
	public Double getNombreHeures() {
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
		return affectationId;
	}

	/**
	 * setter for the attribute affectationId
	 * 
	 * @param <type> Integer </type> affectationId
	 */
	public void setAffectationId(Integer affectationId) {
		if (affectationId == null) {
			throw new Error("setAffectationId : le AffectationId indique est vide");
		}
		this.affectationId = affectationId;
	}

	/**
	 * getter for the attribute month
	 * 
	 * @return <type> Month </type> month
	 */
	public Month getMonth() {
		return this.month;

	}

	public void setMonth(Month month) {
		if (Objects.isNull(month))
			throw new IllegalArgumentException("month can't be null");
		this.month = month;
	}

	/**
	 * getter for the attribute Year
	 * 
	 * @return <type> Integer </type> year
	 */
	public Year getYear() {
		return this.year;
	}

	/**
	 * Setter for the attribute year
	 * 
	 * @param <type> Integer </type>year
	 */
	public void setYear(Year year) {
		if (Objects.isNull(year))
			throw new IllegalArgumentException("year can't be null");
		this.year = year;

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

		List<AffectationMiseABlanc> allAffectation = getAllAffectation();

		for (AffectationMiseABlanc affectation : allAffectation)
			System.out.println(affectation);
	}

	@Override
	public String toString() {

		return "" + this.affectationId + "|" + this.idEmploye + "|" + this.idChantier + "|" + this.nbHours + "|"
				+ this.status;
	}

}
