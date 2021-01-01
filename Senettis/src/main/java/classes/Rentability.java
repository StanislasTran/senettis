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
import java.util.List;
import java.util.Objects;

import connexion.SQLDatabaseConnexion;

/**
 * 
 * The rentability object represent the rentability of a Site for a given month
 * and year
 *
 */
public class Rentability {
	private Integer site;
	private Month month;
	private Year year;
	private double turnOver;
	private double employeeCost;
	private double delivery;
	private double material;
	private double comission;
	private double costPrice;

	private double grossMargin;
	private double FSCost;
	private double percent;
	private String name;

	/**
	 * Constructor for Rentability
	 * 
	 * @param <type>Integer</type> site
	 * @param <type>Month</type>   month
	 * @param <type>Year</type>    year
	 * @param <type>Double</type>  turnOver
	 * @param <type>Double</type>  employeeCost
	 * @param <type>Double</type>  delivery
	 * @param <type>Double</type>  material
	 * @param <type>Double</type>  FSCost
	 * @param <type>Double</type>  comission
	 * @param <type>Double</type>  coutRevient
	 * @param <type>Double</type>  margeBrut
	 * @param <type>Double</type>  percent
	 */

	public Rentability(Integer site, String name, Month month, Year year, Double turnOver, Double employeeCost,
			Double delivery, Double material, Double FSCost, Double comission, Double costPrice, Double grossMargin,
			Double percent) {
		super();
		this.site = site;
		this.month = month;
		this.year = year;
		this.turnOver = turnOver;
		this.employeeCost = employeeCost;
		this.delivery = delivery;
		this.material = material;
		this.comission = comission;
		this.costPrice = costPrice;
		this.grossMargin = grossMargin;
		this.percent=percent;
		this.FSCost = FSCost;
		this.name = name;
	}

	/**
	 * Getter for the attribute site
	 * 
	 * @return <type>Integer</type>site
	 */
	public Integer getSite() {
		return site;
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
	 * Getter for the attribute year
	 * 
	 * @return <type>Year</type>year
	 */

	public Year getYear() {
		return year;
	}

	/**
	 * Getter for the attribute turnOver
	 * 
	 * @return <type>double</type>turnOver
	 */

	public double getTurnOver() {
		return turnOver;
	}

	/**
	 * Getter for the attribute employeeCost
	 * 
	 * @return <type>double</type>employeeCost
	 */
	public double getEmployeeCost() {
		return employeeCost;
	}

	/**
	 * Getter for the attribute delivery
	 * 
	 * @return <type>double</type>delivery
	 */
	public double getDelivery() {
		return delivery;
	}

	/**
	 * Getter for the attribute material
	 * 
	 * @return <type>double</type>material
	 */

	public double getMaterial() {
		return material;
	}

	/**
	 * Getter for the attribute comission
	 * 
	 * @return <type>double</type>comission
	 */

	public double getComission() {
		return comission;
	}

	/**
	 * Getter for the attribute costPrice
	 * 
	 * @return <type>double</type>costPrice
	 */

	public double getCostPrice() {
		return costPrice;
	}

	/**
	 * Getter for the attribute grossMargin
	 * 
	 * @return <type>double</type>grossMargin
	 */
	public double getGrossMargin() {
		return grossMargin;
	}

	/**
	 * Getter for the attribute FSCost
	 * 
	 * @return <type>double</type>FSCost
	 */
	public double getFSCost() {
		return FSCost;
	}

	/**
	 * Getter for the attribute percent
	 * 
	 * @return <Double>percent</double>
	 */
	public Double getPercent() {
		return percent;
	}



	/**
	 * Getter for the attribute name
	 * 
	 * @return <type>String</type>the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Select all Rentability for a given month and year
	 * 
	 * @param <type>Month</type> month
	 * @param <type>Year</type>  year
	 * @return <type>ResultSet</type>result
	 * @throws SQLException - if a database access error occurs;this method is
	 *                      called on a closed PreparedStatementor an argument is
	 *                      supplied to this methodSQLTimeoutException - when the
	 *                      driver has determined that the timeout value that was
	 *                      specified by the setQueryTimeoutmethod has been exceeded
	 *                      and has at least attempted to cancel the currently
	 *                      running Statement
	 */
	public static ResultSet getRentabilityDB(Month month, Year year) throws SQLException {
		String reqSql = "Select * from rentabilite_view where  (mois =? and annee=?) ";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);

		statement.setObject(1, month.getValue(), Types.INTEGER);
		statement.setObject(2, year.getValue(), Types.INTEGER);
		statement.execute();
		ResultSet result = statement.getResultSet();
		return result;
	}

	/**
	 * return the list of rentability for a given month and year
	 * 
	 * @param <type>Month</type>    month
	 * @param <type>Year</type>year
	 * @return <type>List<Rentability></type> rentabilityList
	 * @throws SQLException
	 */
	public static List<Rentability> getRentabilityByDate(Month month, Year year) throws SQLException {
		List<Rentability> rentabilityList = new ArrayList<Rentability>();
		ResultSet result = getRentabilityDB(month, year);

		while (result.next()) {
			Integer site = -1;
			double turnOver = 0;
			double employeeCost = 0;
			double delivery = 0;
			double material = 0;
			double comission = 0;
			double costPrice = 0;
			double grossMargin = 0;
			double FSCost = 0;
			double percent = 0;
			String name = "";

			if (!Objects.isNull(result.getString("nom")))
				name = result.getString("nom");
			System.out.println(name);
			if (!Objects.isNull(result.getInt("chantierId")))
				site = result.getInt("chantierId");
			if (!Objects.isNull(result.getDouble("CA")))
				turnOver = result.getDouble("CA");
			if (!Objects.isNull(result.getDouble("salaryCost")))
				employeeCost = result.getDouble("salaryCost");
			if (!Objects.isNull(result.getDouble("LivraisonCost")))
				delivery = result.getDouble("LivraisonCost");
			if (!Objects.isNull(result.getDouble("Amortissement")))
				material = result.getDouble("Amortissement");
			if (!Objects.isNull(result.getDouble("comissionValue")))
				comission = result.getDouble("comissionValue");
			if (!Objects.isNull(result.getDouble("FSCost")))
				FSCost = result.getDouble("FSCost");
			if (!Objects.isNull(result.getDouble("revient")))
				costPrice = result.getDouble("revient");
			if (!Objects.isNull(result.getDouble("MargeBrut")))
				grossMargin = result.getDouble("MargeBrut");
			percent = (turnOver * 100) / grossMargin;

			rentabilityList.add(new Rentability(site, name, month, year, turnOver, employeeCost, delivery, material,
					FSCost, comission, costPrice, grossMargin, percent));
		}
		System.out.println(rentabilityList.size());
		return rentabilityList;
	}
}
