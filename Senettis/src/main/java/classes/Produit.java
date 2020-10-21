package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import connexion.SQLDatabaseConnection;

public class Produit {
	
	
	private int produitId;
	private String nom;
	private Double prix;
	private String commentaires;
	private String status;
	
	

	/**
	 * Constructor for Produit
	 * 
	 * @param prix
	 * @param nom
	 * @param commentaire
	 * @param status
	 */
	public Produit(String nom, Double prix, String commentaires,String status) {
		this(nom,prix,status);
		this.commentaires = commentaires;
	
	}

	/***
	 * Constructor 
	 * @param nom
	 * @param prix
	 * @param status
	 */
	public Produit(String nom, Double prix,String status) {
		this.nom = nom;
		this.prix = prix;
		this.status=status;
	}
	

	/***
	 * Constructor 
	 * @param nom
	 * @param prix
	 * @param status
	 */
	public Produit(int produitId,String nom, Double prix,String commentaires,String status) {
		this(nom,prix,commentaires,status);
		this.produitId=produitId;
		
	}
	
	

	/**
	 * Insert le produit dans la base de donnée
	 * @return 0 si la requête renvoie rien
     *         
	 * @throws SQLException
	 */
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO Produit(Nom,Prix,Commentaires,Status) VALUES (?,?,?,?)";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1,this.nom,Types.VARCHAR);
		statement.setObject(2,this.prix,Types.DECIMAL);
		statement.setObject(3,this.commentaires,Types.VARCHAR);
		statement.setObject(4,this.status,Types.VARCHAR);

		
		return statement.executeUpdate();
	}
	
	private static Statement selectAllProduct() throws SQLException {
		String reqSql = "SELECT * FROM Produit";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}
	
	public static List<Produit> getAllProduct() throws SQLException {

		ResultSet result=selectAllProduct().getResultSet();
		List<Produit> allProduct=new ArrayList<Produit>();
		System.out.println("Id|Nom|prix|Commentaires|Status");
		while(result.next()) {
			int produitId=result.getInt("ProduitId");
			String nom=result.getString("Nom");
			Double prix=result.getDouble("Prix");
			String commentaires=result.getString("Commentaires");
		    String status=result.getString("Status");
		   allProduct.add(new Produit(produitId,nom, prix,commentaires, status));
		  
			
		}
		
		
		
		return allProduct;
	}
	
	public static void printAllProduct() throws SQLException {
		
		List<Produit> allProduct=getAllProduct();
	
		for (Produit produit : allProduct)	
			System.out.println(produit);
	}
	
	
	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE Produit SET nom=?, prix=?, commentaires=?, status=? WHERE ProduitId=?;";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1,this.nom.toString(),Types.VARCHAR);
		statement.setObject(2,this.prix,Types.VARCHAR);
		statement.setObject(3,this.commentaires,Types.DECIMAL);
		statement.setObject(4,this.status,Types.VARCHAR);
		statement.setObject(5,this.produitId,Types.INTEGER);
		
		return statement.executeUpdate();
	}	
	
	
	/***
	 * 
	 * Getter and setter
	 * 
	 */
	
	
	public int getProduitId() {
		return produitId;
	}

	public void setProduitId(int produitId) {
		this.produitId = produitId;
	}
	
	public String getStatus() {
		return status;
	}

	private  void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * @return the prix
	 */
	public Double getPrix() {
		return prix;
	}

	/**
	 * @param prix the prix to set
	 */
	private void setPrix(Double prix) {
		this.prix = prix;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom the nom to set
	 */
	private void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return the commentaire
	 */
	public String getCommentaires() {
		return commentaires;
	}

	/**
	 * @param commentaire the commentaire to set
	 */
	private void setCommentaires(String commentaire) {
		this.commentaires = commentaires;
	}

	@Override
	public String toString() {
		
		return ""+this.produitId+"|"+this.nom+"|"+this.prix+"|"+this.commentaires+"|"+this.status;
	}
	
	
	
}
