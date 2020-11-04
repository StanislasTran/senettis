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
		if (status != null) {
			if (!status.isEmpty()) {
				if (status.equals("Publi�") || status.equals("publi�")) {
					this.status = "Publi�";
				} else if (status.equals("Archiv�") || status.equals("archiv�"))  {
					this.status = "Archiv�";
				} else if (status.equals("Draft") || status.equals("draft")) {
					this.status = "Draft";
				} else {
					throw new Error("Le status indiqu� est incorrect, le status doit �tre publi�, archiv� ou draft.");
				}
			} else {
				this.status = null;
			}
		}
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
				//FR
				try {
					if ( date.charAt(2) =='/' || date.charAt(2) == '-' || date.charAt(2) == '_') {
						date = date.substring(0, 2) + "/" + date.substring(3, 5) + "/"+ date.substring(6, 10);
					}
				} catch (Throwable e) { 
					e.printStackTrace(); 
					throw new Error("La date indiqu�e est incorrecte, une date doit �tre indiqu� selon un des formats suivant : 31-01-2000, 31/01/2000, 2000-01-31 ou 2000/01/31.");
				}
				//EN
				try {
					if ( date.charAt(7) =='/' || date.charAt(7) == '-' || date.charAt(7) == '_') {
						date = date.substring(8, 10) + "/" + date.substring(5, 7) + "/"+ date.substring(0, 4);
					}
				} catch (Throwable e) { 
					e.printStackTrace(); 
					throw new Error("La date indiqu�e est incorrecte, une date doit �tre indiqu� selon un des formats suivant : 31-01-2000, 31/01/2000, 2000-01-31 ou 2000/01/31.");
				}
			} else {
				throw new Error("La date indiqu�e est incorrecte, une date doit �tre indiqu� selon un des formats suivant : 31-01-2000, 31/01/2000, 2000-01-31 ou 2000/01/31.");
			}
		}
		this.date = date;		
	}




	//lien base de donn�es ---------------------------------------------------------------
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
		String reqSql = "UPDATE Livraison SET Produit=?, Chantier=?, prixTotal=?, date=?, status=? WHERE LivraisonId=?;";
		
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
		//System.out.println("Id|ProduitId|ChantierId|PrixTotal|Date|Status");
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
		String reqSql = "SELECT LivraisonId,Chantier,Produit,date,prixTotal,Status FROM Livraison WHERE LivraisonId=?;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, livraisonId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			livraisonId = result.getInt("LivraisonId");
			int chantierId = result.getInt("chantier");
			int produtiId = result.getInt("Produit");
			String date = result.getString("date");
			double prix = result.getDouble("prixTotal");
	
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
			if (status == "Publi�" || status == "publi�") {
				this.status = "Publi�";
			} else if (status == "Archiv�" || status == "archiv�") {
				this.status = "Archiv�";
			} else if (status == "Draft" || status == "draft") {
				this.status = "Draft";
			} else {
				throw new Error("Le status indiqu� est incorrect, le status doit �tre publi�, archiv� ou draft.");
			}
		} else {
			throw new Error("Le status indiqu� est vide.");
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
