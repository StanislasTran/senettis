
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
	private Double prixTotal;
	private String date;
	private String status;
	private int livraisonId;

	
	//Constructeurs ---------------------------------------------------
	public Livraison(Integer idChantier, Double prixTotal, String date, String status) {
		this(idChantier, prixTotal, date);
		if (status != null) {
			if (!status.isEmpty()) {
				if (status.equals("Publié") || status.equals("publié")) {
					this.status = "Publié";
				} else if (status.equals("Archivé") || status.equals("archivé"))  {
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

	public Livraison(Integer livraisonId, Integer idChantier, Double prixTotal, String date, String status) {
		this(idChantier, prixTotal, date, status);
		this.livraisonId = livraisonId;
	}

	public Livraison(Integer idChantier, Double prixTotal, String date) {
		this(idChantier, date);
		this.prixTotal = prixTotal;
	}
	
	public Livraison(Integer idChantier, String date) {
		super();
		this.idChantier = idChantier;
		
		if (date != null) {
			if (!(date.isEmpty())) {
				//FR
				try {
					if ( date.charAt(2) =='/' || date.charAt(2) == '-' || date.charAt(2) == '_') {
						date = date.substring(0, 2) + "/" + date.substring(3, 5) + "/"+ date.substring(6, 10);
					}
				} catch (Throwable e) { 
					e.printStackTrace(); 
					throw new Error("La date indiquée est incorrecte, une date doit être indiqué selon un des formats suivant : 31-01-2000, 31/01/2000, 2000-01-31 ou 2000/01/31.");
				}
				//EN
				try {
					if ( date.charAt(7) =='/' || date.charAt(7) == '-' || date.charAt(7) == '_') {
						date = date.substring(8, 10) + "/" + date.substring(5, 7) + "/"+ date.substring(0, 4);
					}
				} catch (Throwable e) { 
					e.printStackTrace(); 
					throw new Error("La date indiquée est incorrecte, une date doit être indiqué selon un des formats suivant : 31-01-2000, 31/01/2000, 2000-01-31 ou 2000/01/31.");
				}
			} else {
				throw new Error("La date indiquée est incorrecte, une date doit être indiqué selon un des formats suivant : 31-01-2000, 31/01/2000, 2000-01-31 ou 2000/01/31.");
			}
		}
		this.date = date;		
	}




	//lien base de données ---------------------------------------------------------------
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO Livraison(chantier,date,prixTotal,status) VALUES (?,?,?,?)";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1,this.idChantier,Types.INTEGER);
		statement.setObject(2,this.date,Types.DATE);
		statement.setObject(3,this.prixTotal,Types.DECIMAL);
		statement.setObject(4,this.status,Types.VARCHAR);
		
		statement.executeUpdate();
		
		//on recuperer l'id
		reqSql = "SELECT LAST_INSERT_ID();";
		Statement statement2 = connection.createprStatement();
		statement2.executeQuery(reqSql);
		return statement2;
	}


	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE Livraison SET Chantier=?, prixTotal=?, date=?, status=? WHERE LivraisonId=?;";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1,this.idChantier,Types.INTEGER);
		statement.setObject(2,this.prixTotal,Types.DECIMAL);
		statement.setObject(3,this.date,Types.DATE);
		statement.setObject(4,this.status,Types.VARCHAR);
		statement.setObject(5,this.livraisonId,Types.INTEGER);
		
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
		while(result.next()) {
			int livraisonId=result.getInt("LivraisonId");
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
		   allLivraison.add(new Livraison(livraisonId, chantierId, prixTotal, date, status));
		  
			
		}
		
		return allLivraison;
	}
	
	public static void printAllLivraison() throws SQLException {
		
		List<Livraison> allLivraison=getAllLivraison();
	
		for (Livraison livraison : allLivraison)	
			System.out.println(livraison);
	}
	
	
	public static Livraison getLivraisonById(int livraisonId) throws SQLException {
		String reqSql = "SELECT LivraisonId,Chantier,date,prixTotal,Status FROM Livraison WHERE LivraisonId=?;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, livraisonId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			livraisonId = result.getInt("LivraisonId");
			int chantierId = result.getInt("chantier");
			String date = result.getString("date");
			double prix = result.getDouble("prixTotal");
	
			//Double remboursementTransport = 0.0;
			//if (result.getString("remboursement_transport") != null) {
			//	remboursementTransport = Double.parseDouble(result.getString("remboursement_transport"));
			//}
			
			String status = result.getString("status");

			return new Livraison(livraisonId, chantierId, prix, date, status);

		} else {
			throw new SQLException("Data not found");
		}
	}

	
	public static List<ProduitParLivraison> getProductByLivraisonById(int livraisonId) throws SQLException {
		String reqSql = "SELECT ProduitParLivraisonId,Livraison,Produit,quantite,Status FROM ProduitParLivraison WHERE Livraison=?;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, livraisonId, Types.INTEGER);
		statement.executeQuery();
		System.out.println("19631");
		ResultSet result = statement.getResultSet();
		List<ProduitParLivraison> results = new ArrayList<>();

		while (result.next()) {
			int produitParLivraisonId = result.getInt("produitParLivraisonId");
			livraisonId = result.getInt("Livraison");
			int produitId = result.getInt("Produit");
			int quantite = result.getInt("quantite");
			String status = result.getString("status");
			
			results.add(new ProduitParLivraison(produitParLivraisonId, livraisonId, produitId, quantite, status));

		} 
		
		if (results.isEmpty()){
			throw new SQLException("Data not found");
		}
		return results;
	}
	
	
	@Override
	public String toString() {
		
		return ""+this.livraisonId+"|"+this.idChantier+"|"+this.prixTotal+"|"+this.date+"|"+this.status;
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
		if (status != null) {
			if (status == "Publié" || status == "publié") {
				this.status = "Publié";
			} else if (status == "Archivé" || status == "archivé") {
				this.status = "Archivé";
			} else if (status == "Draft" || status == "draft") {
				this.status = "Draft";
			} else {
				throw new Error("Le status indiqué est incorrect, le status doit être publié, archivé ou draft.");
			}
		} else {
			throw new Error("Le status indiqué est vide.");
		}
	}


	public int getLivraisonId() {
		return livraisonId;
	}


	public void setLivraisonId(int livraisonId) {
		if ((Integer)livraisonId == null) {
			throw new Error("setLivraisonId : le livraisonId indique est vide");
		}
		this.livraisonId = livraisonId;
	}
	
	

}
