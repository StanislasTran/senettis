package classes;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.microsoft.sqlserver.jdbc.StringUtils;

import connexion.SQLDatabaseConnection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Livraison {

	private Integer idChantier;
	private Integer idProduit;
	private Double prixTotal;
	private String date;
	private String status;
	private int livraisonId;

	
	//Constructeurs ---------------------------------------------------
	public Livraison(Integer idChantier, Integer idProduit, Double prixTotal, String date, String status) {
		this(idChantier, idProduit, prixTotal, date);
		this.status=status;
	}

	public Livraison(Integer livraisonId, Integer idChantier, Integer idProduit, Double prixTotal, String date, String status) {
		this(idChantier, idProduit, prixTotal, date, status);
		this.livraisonId = livraisonId;
	}

	public Livraison(Integer idChantier, Integer idProduit, Double prixTotal, String date) {
		this(idChantier, idProduit, date);
		this.prixTotal = prixTotal;
	}
	
	public Livraison(Integer idChantier, Integer idProduit, String date) {
		super();
		this.idChantier = idChantier;
		this.idProduit = idProduit;
		
		if (date != null) {
			if (!(date.isEmpty())) {
				if ( date.charAt(2) =='/' || date.charAt(2) == '-' || date.charAt(2) == '_') {
					//System.out.println("fr");
					// date francaise
					// on reecrit en format francais juste pour s'assurer que toutes les dates
					// seront ecrites avec le meme format jj/mm/aaaa
					date = date.substring(0, 2) + "/" + date.substring(3, 5) + "/"
							+ date.substring(6, 10);
				}
				else if ( date.charAt(7) =='/' || date.charAt(7) == '-' || date.charAt(7) == '_') {
					//System.out.println("en");
					// date anglaise
					date = date.substring(8, 10) + "/" + date.substring(5, 7) + "/"
							+ date.substring(0, 4);
				} else {
					throw new Error(
							"La date indiquée est incorrecte, une date doit être indiqué selon un des formats suivant : 31-01-2000, 31/01/2000, 2000-01-31 ou 2000/01/31.");
				}
			}
			else {
				date = null;
			}
		}
		this.date = date;		
	}




	//lien base de données ---------------------------------------------------------------
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO Livraison(chantier,produit,date,prixTotal,status) VALUES (?,?,?,?,?)";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1,this.idChantier,Types.INTEGER);
		statement.setObject(2,this.idProduit,Types.INTEGER);
		statement.setObject(3,this.date,Types.DATE);
		statement.setObject(4,this.prixTotal,Types.DECIMAL);
		statement.setObject(5,this.status,Types.VARCHAR);
		
		return statement.executeUpdate();
	}


	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE Livraison SET Produit=?, Chantier=?, prix_total=?, date=?, status=? WHERE LivraisonId=?;";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1,this.idProduit.toString(),Types.INTEGER);
		statement.setObject(2,this.idChantier,Types.INTEGER);
		statement.setObject(3,this.prixTotal,Types.DECIMAL);
		statement.setObject(4,this.date,Types.DATE);
		statement.setObject(5,this.status,Types.VARCHAR);
		statement.setObject(6,this.livraisonId,Types.INTEGER);
		
		return statement.executeUpdate();
	}
	
	
	private static Statement selectAllLivraison() throws SQLException {
		String reqSql = "SELECT * FROM Livraison";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}
	
	public static List<Livraison> getAllLivraison() throws SQLException {

		ResultSet result=selectAllLivraison().getResultSet();
		List<Livraison> allLivraison=new ArrayList<Livraison>();
		System.out.println("Id|ProduitId|ChantierId|PrixTotal|Date|Status");
		while(result.next()) {
			int livraisonId=result.getInt("LivraisonId");
			int produitId=result.getInt("Produit");
			int chantierId=result.getInt("Chantier");
			Double prixTotal=result.getDouble("PrixTotal");
			String date;
			if (result.getString("date") == null) {
				date = null;
			} else {
				String usDate = result.getString("date");
				// on transforme en date au format francais
				date = usDate.substring(8, 10) + "/" + usDate.substring(5, 7) + "/" + usDate.substring(0, 4);
			}
		    String status=result.getString("Status");
		   allLivraison.add(new Livraison(livraisonId, chantierId, produitId, prixTotal, date, status));
		  
			
		}
		
		return allLivraison;
	}
	
	public static void printAllLivraison() throws SQLException {
		
		List<Livraison> allLivraison=getAllLivraison();
	
		for (Livraison livraison : allLivraison)	
			System.out.println(livraison);
	}
	
	
	public static Livraison getLivraisonById(int livraisonId) throws SQLException {
		String reqSql = "SELECT LivraisonId,ChantierId,ProduitId,date,prix_total,Status FROM Livraison WHERE LivraisonId=?;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, livraisonId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			livraisonId = result.getInt("EmployeId");
			int chantierId = result.getInt("chantierId");
			int produtiId = result.getInt("ProduitId");
			String date = result.getString("date");
			double prix = result.getDouble("prix_total");
	
			//Double remboursementTransport = 0.0;
			//if (result.getString("remboursement_transport") != null) {
			//	remboursementTransport = Double.parseDouble(result.getString("remboursement_transport"));
			//}
			
			String status = result.getString("status");

			return new Livraison(livraisonId, chantierId, produtiId, prix, date, status);

		} else {
			throw new SQLException("Data not found");
		}
	}

	
	
	
	
	@Override
	public String toString() {
		
		return ""+this.livraisonId+"|"+this.idProduit+"|"+this.idChantier+"|"+this.prixTotal+"|"+this.date+"|"+this.status;
	}

	
	//getter and setter -----------------------------------------------------
	public Integer getIdChantier() {
		return idChantier;
	}


	public void setIdChantier(Integer idChantier) {
		if (idChantier == null) {
			throw new Error("setIdChantier : le idChantier indique est vide");
		}
		this.idChantier = idChantier;
	}

	public Integer getIdProduit() {
		return idProduit;
	}

	public void setIdProduit(Integer idProduit) {
		if (idProduit == null) {
			throw new Error("setIdProduit : le idProduit indique est vide");
		}
		this.idProduit = idProduit;
	}

	public Double getPrixTotal() {
		return prixTotal;
	}

	public void setPrixTotal(Double prixTotal) {
		if (prixTotal == null) {
			throw new Error("setPrixTotal : le prixTotal indique est vide");
		}
		this.prixTotal = prixTotal;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		// date
		if (date != null) {
			if (StringUtils.isNumeric(date.substring(0, 4))
					&& StringUtils.isNumeric(date.substring(8, 10))
					&& StringUtils.isNumeric(date.substring(5, 7))) {
				// date anglaise
				// on reecrit en format francais
				date = date.substring(8, 10) + "/" + date.substring(5, 7) + "/"
						+ date.substring(0, 4);
			} else if (StringUtils.isNumeric(date.substring(6, 10))
					&& StringUtils.isNumeric(date.substring(3, 5))
					&& StringUtils.isNumeric(date.substring(0, 2))) {
				// date francaise
				// on reecrit en format francais juste pour s'assurer que toutes les dates
				// seront ecrites avec le meme format jj/mm/aaaa
				date = date.substring(0, 2) + "/" + date.substring(3, 5) + "/"
						+ date.substring(6, 10);
			} else {
				throw new Error(
						"La date indiquée est incorrecte, une date doit être indiqué selon un des formats suivant : 31-01-2000, 31/01/2000, 2000-01-31 ou 2000/01/31.");
			}
		} else {
			throw new Error("La date indiquée est vide.");
		}
		this.date = date;

	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public int getLivraisonId() {
		return livraisonId;
	}


	public void setLivraisonId(int livraisonId) {
		this.livraisonId = livraisonId;
	}
	
	

}
