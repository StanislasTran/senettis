
package classes;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
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

	// Constructeurs ---------------------------------------------------
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

	public Delivery(Integer deliveryId, Integer siteId, Double totalPrice, String date, String status) {
		this(siteId, totalPrice, date, status);
		this.deliveryId = deliveryId;
	}

	public Delivery(Integer siteId, Double totalPrice, String date) {
		this(siteId, date);
		this.totalPrice = totalPrice;
	}

	public Delivery(Integer siteId, String date) {
		super();
		this.siteId = siteId;

		if (date != null) {
			if (!(date.isEmpty())) {
				// FR
				try {
					if (date.charAt(2) == '/' || date.charAt(2) == '-' || date.charAt(2) == '_') {
						date = date.substring(0, 2) + "/" + date.substring(3, 5) + "/" + date.substring(6, 10);
					}
				} catch (Throwable e) {
					e.printStackTrace();
					throw new Error(
							"La date indiquée est incorrecte, une date doit �tre indiqu� selon un des formats suivant : 31-01-2000, 31/01/2000, 2000-01-31 ou 2000/01/31.");
				}
				// EN
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
						"La date indiqu�e est incorrecte, une date doit être indiquée selon un des formats suivant : 31-01-2000, 31/01/2000, 2000-01-31 ou 2000/01/31.");
			}
		}
		this.date = date;
	}

	public Delivery(Integer siteId) {
		super();
		this.siteId = siteId;
	}

	// lien base de donn�es
	// ---------------------------------------------------------------
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO Livraison(chantier,date,prixTotal,status) VALUES (?,?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.siteId, Types.INTEGER);
		statement.setObject(2, this.date, Types.DATE);
		statement.setObject(3, this.totalPrice, Types.DECIMAL);
		statement.setObject(4, this.status, Types.VARCHAR);

		statement.executeUpdate();

		// si il y a une erreur on devrait supprimer la livraison creee ??????

		// on recuperer l'id
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

	private static Statement selectAllLivraison() throws SQLException {
		String reqSql = "SELECT * FROM Livraison";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}

	public static List<Delivery> getAllLivraison() throws SQLException {

		ResultSet result = selectAllLivraison().getResultSet();
		List<Delivery> allLivraison = new ArrayList<Delivery>();
		while (result.next()) {
			int livraisonId = result.getInt("LivraisonId");
			int chantierId = result.getInt("Chantier");
			Double prixTotal = result.getDouble("PrixTotal");
			String date;
			if (result.getString("date") == null) {
				date = null;
			} else {
				String usDate = result.getString("date");
				// on transforme en date au format francais
				date = usDate.substring(8, 10) + "/" + usDate.substring(5, 7) + "/" + usDate.substring(0, 4);
			}
			String status = result.getString("Status");
			allLivraison.add(new Delivery(livraisonId, chantierId, prixTotal, date, status));

		}

		return allLivraison;
	}

	public static void printAllLivraison() throws SQLException {

		List<Delivery> allLivraison = getAllLivraison();

		for (Delivery livraison : allLivraison)
			System.out.println(livraison);
	}

	public static Delivery getLivraisonById(int livraisonId) throws SQLException {
		String reqSql = "SELECT LivraisonId,Chantier,date,prixTotal,Status FROM Livraison WHERE LivraisonId=?;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, livraisonId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			livraisonId = result.getInt("LivraisonId");
			int chantierId = result.getInt("chantier");
			String date = result.getString("date");
			double prix = result.getDouble("prixTotal");
			String status = result.getString("status");
			return new Delivery(livraisonId, chantierId, prix, date, status);

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

	public void setIdChantier(Integer idChantier) {
		if (idChantier == null) {
			throw new Error("setIdChantier : le idChantier indique est vide");
		}
		this.siteId = idChantier;
	}

	public Double getPrixTotal() {
		return totalPrice;
	}

	public void setPrixTotal(Double prixTotal) {
		if (prixTotal == null) {
			throw new Error("setPrixTotal : le prixTotal indique est vide");
		}
		this.totalPrice = prixTotal;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		// date
		if (date != null) {
			try {
				if (StringUtils.isNumeric(date.substring(0, 4)) && StringUtils.isNumeric(date.substring(8, 10))
						&& StringUtils.isNumeric(date.substring(5, 7))) {
					// date anglaise
					// on reecrit en format francais
					date = date.substring(8, 10) + "/" + date.substring(5, 7) + "/" + date.substring(0, 4);
				} else if (StringUtils.isNumeric(date.substring(6, 10)) && StringUtils.isNumeric(date.substring(3, 5))
						&& StringUtils.isNumeric(date.substring(0, 2))) {
					// date francaise
					// on reecrit en format francais juste pour s'assurer que toutes les dates
					// seront ecrites avec le meme format jj/mm/aaaa
					date = date.substring(0, 2) + "/" + date.substring(3, 5) + "/" + date.substring(6, 10);

				} else {
					throw new Error(
							"La date indiqu�e est incorrecte, une date doit �tre indiqu� selon un des formats suivant : 31-01-2000, 31/01/2000, 2000-01-31 ou 2000/01/31.");
				}

			} catch (Exception e) {
				throw new Error(
						"La date indiqu�e est incorrecte, une date doit �tre indiqu� selon un des formats suivant : 31-01-2000, 31/01/2000, 2000-01-31 ou 2000/01/31.");
			}
		} else {
			throw new Error("La date indiqu�e est vide.");
		}
		this.date = date;

	}

	public String getStatus() {
		return status;
	}

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

	public int getLivraisonId() {
		return deliveryId;
	}

	public void setLivraisonId(int livraisonId) {
		if ((Integer) livraisonId == null) {
			throw new Error("setLivraisonId : le livraisonId indique est vide");
		}
		this.deliveryId = livraisonId;
	}

	public static Map<Integer, Double> getAllLivraisonFilteredMap(Month month, Year year) throws SQLException {
		ResultSet result = getAllLivraisonsAfterResultSet(month, year);
		HashMap<Integer, Double> liv = new HashMap<Integer, Double>();
		while (result.next()) {

			liv.put(result.getInt("chantier"), result.getDouble("SUM"));

		}
		return liv;
	}

	public static ResultSet getAllLivraisonsAfterResultSet(Month month, Year year) throws SQLException {
		String debut = "01/" + month.getValue() + "/" + year.getValue();
		String fin;

		if (month.getValue() == 2) {
			fin = "29/" + month.getValue() + "/" + year.getValue();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			simpleDateFormat.setLenient(false);
			try {
				simpleDateFormat.parse(fin);

			} catch (ParseException e) {
				fin = "28/" + month.getValue() + "/" + year.getValue();
			}

		} else if (month.getValue() == 1 || month.getValue() == 3 || month.getValue() == 5 || month.getValue() == 7
				|| month.getValue() == 8 || month.getValue() == 10 || month.getValue() == 12) {
			fin = "31/" + month.getValue() + "/" + year.getValue();
		} else {
			fin = "30/" + month.getValue() + "/" + year.getValue();
		}
		String reqSql = "select chantier,Sum(PrixTotal) as SUM from Livraison WHERE status ='Publié' AND Date>=? AND Date<=? GROUP BY chantier;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, debut, Types.DATE);
		statement.setObject(2, fin, Types.DATE);
		
		statement.execute();
		return statement.getResultSet();
	}

}
