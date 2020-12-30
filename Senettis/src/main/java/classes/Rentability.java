package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Month;
import java.time.Year;

import connexion.SQLDatabaseConnexion;


/**
 * 
 * The rentability object represent the rentability of a Site for a given month and year
 *
 */
public class Rentability {
	private Integer site;
	private Month month;
	private Year year;
	private Double turnOver;
	private Double employeeCost;
	private Double delivery;
	private Double material;
	private Double comissions;
	private Double costPrice;
	private Double grossMargin;
	private Double FSCost;
	private Double percent;

	/**
	 * Constructor for Rentability
	 * 
	 * @param <type>Integer</type>            site
	 * @param <type>Month</type>month
	 * @param <type>Year</type>year
	 * @param <type>Double</type>turnOver
	 * @param <type>Double</type>employeeCost
	 * @param <type>Double</type>delivery
	 * @param <type>Double</type>material
	 * @param <type>Double</type>FSCost
	 * @param <type>Double</type>comissions
	 * @param <type>Double</type>coutRevient
	 * @param <type>Double</type>margeBrut
	 * @param <type>Double</type>percent
	 */

	public Rentability(Integer site, Month month, Year year, Double turnOver, Double employeeCost, Double delivery,
			Double material, Double FSCost, Double comissions, Double costPrice, Double grossMargin, Double percent) {
		super();
		this.site = site;
		this.month = month;
		this.year = year;
		this.turnOver = turnOver;
		this.employeeCost = employeeCost;
		this.delivery = delivery;
		this.material = material;
		this.comissions = comissions;
		this.costPrice = costPrice;
		this.grossMargin = grossMargin;
		this.setPercent(percent);
		this.FSCost = FSCost;
	}

	/**
	 * Insert the current rentability object in the database
	 * 
	 * @throws SQLException
	 */
	public void insert() throws SQLException {
		String reqSql = "Insert INTO Rentabilite (Chantier,Mois,Annee,Status,CoutsEmploye,CoutsLivraison,CoutMateriel,CoutFournituresSanitaires,Comission,CoutDeRevient,MargeBrut,ChiffreAffaire) VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.site, Types.INTEGER);
		statement.setObject(2, this.month.getValue(), Types.INTEGER);

		statement.setObject(3, this.year.getValue(), Types.INTEGER);
		statement.setObject(4, Status.PUBLISHED.getValue(), Types.VARCHAR);

		statement.setObject(5, this.employeeCost, Types.DECIMAL);
		statement.setObject(6, this.delivery, Types.DECIMAL);
		statement.setObject(7, this.material, Types.DECIMAL);
		statement.setObject(8, this.FSCost, Types.DECIMAL);

		statement.setObject(9, this.comissions, Types.DECIMAL);
		statement.setObject(10, this.costPrice, Types.DECIMAL);
		statement.setObject(11, this.grossMargin, Types.DECIMAL);
		statement.setObject(12, this.turnOver, Types.DECIMAL);

		statement.executeUpdate();

	}

	/**
	 * Update the current Rentability in the database
	 * 
	 * @throws SQLException
	 */
	public void update() throws SQLException {

		if (exist()) {
			String reqSql = "update Rentabilite SET  CoutsEmploye=?,CoutsLivraison=?,CoutMateriel=?,CoutFournituresSanitaires=?,Comission=?,CoutDeRevient=?,MargeBrut=?,ChiffreAffaire=? WHERE Chantier=? AND Mois=? AND Annee=? AND Status=?";
			Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
			PreparedStatement statement = connection.prepareStatement(reqSql);
			statement.setObject(9, this.site, Types.INTEGER);
			statement.setObject(10, this.month.getValue(), Types.INTEGER);

			statement.setObject(11, this.year.getValue(), Types.INTEGER);
			statement.setObject(12, Status.PUBLISHED.getValue(), Types.VARCHAR);

			statement.setObject(1, this.employeeCost, Types.DECIMAL);
			statement.setObject(2, this.delivery, Types.DECIMAL);
			statement.setObject(3, this.material, Types.DECIMAL);

			statement.setObject(4, this.FSCost, Types.DECIMAL);

			statement.setObject(5, this.comissions, Types.DECIMAL);
			statement.setObject(6, this.costPrice, Types.DECIMAL);
			statement.setObject(7, this.grossMargin, Types.DECIMAL);
			statement.setObject(8, this.turnOver, Types.DECIMAL);

			statement.executeUpdate();

		} else
			insert();
	}

	/**
	 * Check if the rentability already exist or not in the database
	 * 
	 * @return <type>boolean</type> true if the rentability exist else false
	 * @throws SQLException
	 */
	public boolean exist() throws SQLException {
		String reqSql = "Select count (*) as count from Rentabilite where Chantier=? AND Mois=? AND Annee=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.site, Types.INTEGER);
		statement.setObject(2, this.month.getValue(), Types.INTEGER);

		statement.setObject(3, this.year.getValue(), Types.INTEGER);

		statement.execute();

		ResultSet result = statement.getResultSet();
		result.next();

		return result.getInt("count") > 0;

	}

	/**
	 * Setter for percent attribute
	 * 
	 * @return <Double>percent</double>
	 */
	public Double getPercent() {
		return percent;
	}

	/**
	 * Setter for percent attribute
	 * 
	 * @param <type>Double</type> percent
	 */
	public void setPercent(Double percent) {
		this.percent = percent;
	}

}
