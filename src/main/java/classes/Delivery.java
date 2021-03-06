
package classes;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.microsoft.sqlserver.jdbc.StringUtils;
import connexion.SQLDatabaseConnexion;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.Year;



/**
 * Represent a Delivery into the System
 *
 */
public class Delivery {

	private Integer siteId;
	private Double totalPrice;
	private String date;
	private String status;
	private Integer deliveryId;

	/**
	 * Constructor
	 * 
	 * @param <type>Integer</type>siteId
	 * @param <type>Double</type>totalPrice
	 * @param <type>String</type>date
	 * @param <type>Status</type>status
	 */
	public Delivery(Integer siteId, Double totalPrice, String date, String status) {
		this(siteId, totalPrice, date);
		if (status != null) {
			if (!status.isEmpty()) {
				if (status.equals("Publié") || status.equals("Publié")) {
					this.status = "Publié";
				} else if (status.equals("Archivé") || status.equals("Archivé")) {
					this.status = "Archivé";
				} else if (status.equals("Draft") || status.equals("draft")) {
					this.status = "Draft";
				} else {
					throw new Error("Le status indiqué est incorrect, le status doit être publié, archivé ou draft.");
				}
			} else {
				this.status = null;
			}
		}
	}

	/**
	 * Constructor
	 * 
	 * @param <type>Integer</type>deliveryId
	 * @param <type>Integer</type>siteId
	 * @param <type>Double</type>totalPrice
	 * @param <type>String</type>date
	 * @param <type>Status</type>status
	 */
	public Delivery(Integer deliveryId, Integer siteId, Double totalPrice, String date, String status) {
		this(siteId, totalPrice, date, status);
		this.deliveryId = deliveryId;
	}

	/**
	 * Constructor
	 * 
	 * @param <type>Integer</type>siteId
	 * @param <type>Double</type>totalPrice
	 * @param <type>String</type>date
	 * 
	 */
	public Delivery(Integer siteId, Double totalPrice, String date) {
		this(siteId, date);
		this.totalPrice = totalPrice;
	}

	/**
	 * Constructor
	 * 
	 * @param <type>Integer</type>siteId
	 * 
	 * @param <type>String</type>date
	 * 
	 */
	public Delivery(Integer siteId, String date) {
		super();
		this.siteId = siteId;

		if (date != null) {
			if (!(date.isEmpty())) {

				try {
					if (date.charAt(2) == '/' || date.charAt(2) == '-' || date.charAt(2) == '_') {
						date = date.substring(0, 2) + "/" + date.substring(3, 5) + "/" + date.substring(6, 10);
					}
				} catch (Throwable e) {
					e.printStackTrace();
					throw new Error(
							"La date indiquée est incorrecte, une date doit être indiquée selon un des formats suivant : 31-01-2000, 31/01/2000, 2000-01-31 ou 2000/01/31.");
				}

				try {
					if (date.charAt(7) == '/' || date.charAt(7) == '-' || date.charAt(7) == '_') {
						date = date.substring(8, 10) + "/" + date.substring(5, 7) + "/" + date.substring(0, 4);
					}
				} catch (Throwable e) {
					e.printStackTrace();
					throw new Error(
							"La date indiquée est incorrecte, une date doit être indiquée selon un des formats suivant : 31-01-2000, 31/01/2000, 2000-01-31 ou 2000/01/31.");
				}
			} else {
				throw new Error(
						"La date indiquée est incorrecte, une date doit être indiquée selon un des formats suivant : 31-01-2000, 31/01/2000, 2000-01-31 ou 2000/01/31.");
			}
		}
		this.date = date;
	}

	/**
	 * Constructor
	 * 
	 * @param <type>Integer</type> siteId
	 */
	public Delivery(Integer siteId) {
		super();
		this.siteId = siteId;
	}

	/**
	 * Insert the current delivery into the database
	 * 
	 * @return <type>int</type> either (1) the row count for SQL Data Manipulation
	 *         Language (DML) statementsor (2) 0 for SQL statements that return
	 *         nothing
	 * @throws SQLException - if a database access error occurs;this method is
	 *                      called on a closed PreparedStatementor the SQL statement
	 *                      returns a ResultSet objectSQLTimeoutException - when the
	 *                      driver has determined that thetimeout value that was
	 *                      specified by the setQueryTimeoutmethod has been exceeded
	 *                      and has at least attempted to cancelthe currently
	 *                      running Statement
	 */
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO Livraison(chantier,date,prixTotal,status) VALUES (?,?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.siteId, Types.INTEGER);
		statement.setObject(2, this.date, Types.DATE);
		statement.setObject(3, this.totalPrice, Types.DECIMAL);
		statement.setObject(4, this.status, Types.VARCHAR);
		statement.executeUpdate();

		reqSql = "SELECT LivraisonId AS LastID FROM Livraison WHERE LivraisonId = @@Identity;";
		Statement statement2 = connection.createStatement();
		statement2.executeQuery(reqSql);

		ResultSet result = statement2.getResultSet();
		if (result.next()) {

			return result.getInt("LastID");
		} else {
			throw new SQLException("Data not found");
		}
	}

	/**
	 * Update the current delivery into the database
	 * 
	 * @returneither (1) the row count for SQL Data Manipulation Language (DML)
	 *               statementsor (2) 0 for SQL statements that return nothing
	 * @throwsSQLException - if a database access error occurs;this method is called
	 *                     on a closed PreparedStatementor the SQL statement returns
	 *                     a ResultSet objectSQLTimeoutException - when the driver
	 *                     has determined that thetimeout value that was specified
	 *                     by the setQueryTimeoutmethod has been exceeded and has at
	 *                     least attempted to cancelthe currently running Statement
	 */
	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE Livraison SET Chantier=?, prixTotal=?, date=?, status=? WHERE LivraisonId=?;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.siteId, Types.INTEGER);
		statement.setObject(2, this.totalPrice, Types.DECIMAL);
		statement.setObject(3, this.date, Types.DATE);
		statement.setObject(4, this.status, Types.VARCHAR);
		statement.setObject(5, this.deliveryId, Types.INTEGER);

		return statement.executeUpdate();
	}

	/**
	 * Select all Livraison into the database
	 * 
	 * @return<type>Statement</type>s
	 * @throws SQLException - if a database access error occurs,this method is
	 *                      called on a closed Statement, the givenSQL statement
	 *                      produces anything other than a single ResultSet object,
	 *                      the method is called on a PreparedStatement or
	 *                      CallableStatementSQLTimeoutException - when the driver
	 *                      has determined that thetimeout value that was specified
	 *                      by the setQueryTimeoutmethod has been exceeded and has
	 *                      at least attempted to cancelthe currently running
	 *                      Statement
	 */
	private static Statement selectAllDelivery() throws SQLException {
		String reqSql = "SELECT LivraisonId, Chantier, PrixTotal, date, Livraison.Status FROM Livraison LEFT JOIN Chantier ON Chantier.ChantierId=Livraison.Chantier ORDER BY Chantier.Nom;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}
	
	
	
	/**
	 * 
	 * Getter which return the list of all delivrery into the database
	 * @return <type>List<Delivery> </type>
	 * @throws SQLException
	 */

	public static List<Delivery> getAllDelivery() throws SQLException {

		ResultSet result = selectAllDelivery().getResultSet();
		List<Delivery> allLivraison = new ArrayList<Delivery>();
		while (result.next()) {
			Integer deliveryId = result.getInt("LivraisonId");
			Integer siteId = result.getInt("Chantier");
			Double totalPrice = result.getDouble("PrixTotal");
			String date;
			if (result.getString("date") == null) {
				date = null;
			} else {
				String usDate = result.getString("date");
				// convert into french format
				date = usDate.substring(8, 10) + "/" + usDate.substring(5, 7) + "/" + usDate.substring(0, 4);
			}
			String status = result.getString("Status");
			allLivraison.add(new Delivery(deliveryId, siteId, totalPrice, date, status));

		}

		return allLivraison;
	}

	
	/**
	 * pint all delivery store into the database
	 * @throws SQLException
	 */
	public static void printAllDelivery() throws SQLException {

		List<Delivery> allDelivery = getAllDelivery();

		for (Delivery delivery : allDelivery)
			System.out.println(delivery);
	}
	
	/**
	 * Getter which return the delivery associated with the deliveryId given in parameter
	 * @param livraisonId
	 * @return <type>Delivery</type>
	 * @throws SQLException
	 */

	public static Delivery getLivraisonById(int livraisonId) throws SQLException {
		String reqSql = "SELECT LivraisonId,Chantier,date,prixTotal,Status FROM Livraison WHERE LivraisonId=?;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, livraisonId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			livraisonId = result.getInt("LivraisonId");
			Integer siteId = result.getInt("chantier");
			String date = result.getString("date");
			double price = result.getDouble("prixTotal");
			String status = result.getString("status");
			return new Delivery(livraisonId, siteId, price, date, status);

		} else {
			throw new SQLException("Data not found");
		}
	}

	@Override
	public String toString() {

		return "" + this.deliveryId + "|" + this.siteId + "|" + this.totalPrice + "|" + this.date + "|" + this.status;
	}

	// getter and setter -----------------------------------------------------
	public Integer getIdChantier() {
		return siteId;
	}

	
	/**
	 * Setter for the attribute siteid
	 * @param siteId
	 */
	public void setSiteId(Integer siteId) {
		if (siteId == null) {
			throw new Error("setIdChantier : le idChantier indique est vide");
		}
		this.siteId = siteId;
	}

	/**
	 * Getter for totalPrice
	 * @return <type>Double</type>
	 */
	public Double getTotalPrice() {
		return totalPrice;
	}

	
	/**
	 * Setter for total Price
	 * @param totalPrice
	 */
	public void setTotalPrice(Double totalPrice) {
		if (totalPrice == null) {
			throw new Error("setPrixTotal : le prixTotal indique est vide");
		}
		this.totalPrice = totalPrice;
	}

	public String getDate() {
		return date;
	}

	/**
	 * Setter for date
	 * @param date
	 */
	public void setDate(String date) {
		
		if (date != null) {
			try {
				if (StringUtils.isNumeric(date.substring(0, 4)) && StringUtils.isNumeric(date.substring(8, 10))
						&& StringUtils.isNumeric(date.substring(5, 7))) {
					
					date = date.substring(8, 10) + "/" + date.substring(5, 7) + "/" + date.substring(0, 4);
				} else if (StringUtils.isNumeric(date.substring(6, 10)) && StringUtils.isNumeric(date.substring(3, 5))
						&& StringUtils.isNumeric(date.substring(0, 2))) {
					
					date = date.substring(0, 2) + "/" + date.substring(3, 5) + "/" + date.substring(6, 10);

				} else {
					throw new Error(
							"La date indiqu�e est incorrecte, une date doit être indiquée selon un des formats suivant : 01-01-2000, 01/01/2000, 2000-01-31 ou 2000/01/31.");
				}

			} catch (Exception e) {
				throw new Error(
						"La date indiquée est incorrecte, une date doit être indiqué selon un des formats suivant : 31-01-2000, 31/01/2000, 2000-01-31 ou 2000/01/31.");
			}
		} else {
			throw new Error("La date indiqu�e est vide.");
		}
		this.date = date;

	}

	
	/**
	 * Getter for Status
	 * @return <type>String</type>
	 */
	public String getStatus() {
		return status;
	}

	
	/**
	 * Stter for Status
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
				throw new Error("Le status indiqu� est incorrect, le status doit �tre Publié, Archivé ou draft.");
			}
		} else {
			throw new Error("Le status indiqu� est vide.");
		}
	}

	/*
	 * Getter for deliveryId
	 */
	public int getDeliveryId() {
		return deliveryId;
	}

	
	/**
	 * Setter for deliveryId
	 * @param deliveryId
	 */
	public void setDeliveryId(Integer deliveryId) {
		if ((Integer) deliveryId == null) {
			throw new Error("setLivraisonId : le livraisonId indique est vide");
		}
		this.deliveryId = deliveryId;
	}

	public static Map<Integer, Double> getAllLivraisonFilteredMap(Month month, Year year) throws SQLException {
		ResultSet result = getAllDeliveryOnDate(month, year);
		HashMap<Integer, Double> liv = new HashMap<Integer, Double>();
		while (result.next()) {

			liv.put(result.getInt("chantier"), result.getDouble("SUM"));

		}
		return liv;
	}

	/**
	 * Getter which return a resultSet which contains all Delivery on a given date
	 * @param month
	 * @param year
	 * @return <type>ResultSet</type>
	 * @throws SQLException
	 */
	public static ResultSet getAllDeliveryOnDate(Month month, Year year) throws SQLException {
		String start = "01/" + month.getValue() + "/" + year.getValue();
		String end;

		if (month.getValue() == 2) {
			end = "29/" + month.getValue() + "/" + year.getValue();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			simpleDateFormat.setLenient(false);
			try {
				simpleDateFormat.parse(end);

			} catch (ParseException e) {
				end = "28/" + month.getValue() + "/" + year.getValue();
			}

		} else if (month.getValue() == 1 || month.getValue() == 3 || month.getValue() == 5 || month.getValue() == 7
				|| month.getValue() == 8 || month.getValue() == 10 || month.getValue() == 12) {
			end = "31/" + month.getValue() + "/" + year.getValue();
		} else {
			end = "30/" + month.getValue() + "/" + year.getValue();
		}
		String reqSql = "select chantier,Sum(PrixTotal) as SUM from Livraison WHERE status ='Publié' AND Date>=? AND Date<=? GROUP BY chantier;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, start, Types.DATE);
		statement.setObject(2, end, Types.DATE);

		statement.execute();
		return statement.getResultSet();
	}

	public static Delivery getTotalDelivery(Integer month, Integer annee) throws SQLException {
		String debut, fin;
		if (month==1 || month == 3 || month == 5 || month == 7 || month == 8 ) {
			debut = "01/0"+Integer.toString(month)+"/"+Integer.toString(annee);
			fin = "31/0"+Integer.toString(month)+"/"+Integer.toString(annee);
		}
		else if (month == 10 || month == 12) {
			debut = "01/"+Integer.toString(month)+"/"+Integer.toString(annee);
			fin = "31/"+Integer.toString(month)+"/"+Integer.toString(annee);
		}
		else if (month == 4 || month == 6 || month == 9) {
			debut = "01/0"+Integer.toString(month)+"/"+Integer.toString(annee);
			fin = "30/0"+Integer.toString(month)+"/"+Integer.toString(annee);
		}
		else if (month == 11) {
			debut = "01/"+Integer.toString(month)+"/"+Integer.toString(annee);
			fin = "30/"+Integer.toString(month)+"/"+Integer.toString(annee);
		}
		else if (Year.isLeap(Integer.toUnsignedLong(annee))){
			debut = "01/0"+Integer.toString(month)+"/"+Integer.toString(annee);
			fin = "29/0"+Integer.toString(month)+"/"+Integer.toString(annee);
		}
		else {
			debut = "01/0"+Integer.toString(month)+"/"+Integer.toString(annee);
			fin = "28/0"+Integer.toString(month)+"/"+Integer.toString(annee);
		}
		
		String reqSql = "SELECT sum(prixTotal) as prixTotal FROM Livraison WHERE Status='Publié' AND Date<? AND Date>?;";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		System.out.println(debut+" "+fin);
		statement.setObject(1, fin, Types.DATE);
		statement.setObject(2, debut, Types.DATE);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			Double price = 0.0;
			if (result.getString("prixTotal") != null) {
				price = Double.parseDouble(result.getString("prixTotal"));
			}
			
			return new Delivery(-1,price,"01/01/2020", "Publié");

		} else {
			throw new SQLException("Data not found");
		}
	}

}
