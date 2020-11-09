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
	private String comment;
	private String status;

	/**
	 * Constructor for Produit
	 * 
	 * @param <type>Double</type> price
	 * @param <type> String </type> name
	 * @param <type>String</type> comment
	 * @param <type>String</type> status
	 */
	public Produit(String name, Double price, String comment, String status) {
		this(name, price, status);
		this.comment = comment;

	}

	
	/*****************************************
	 * 
	 *            Constructors
	 * 
	 ****************************************/
	
	/***
	 * Constructor
	 * 
	 * @param <type> String </type> name </String>
	 * @param <type> int /<type> price 
	 * @param status
	 */
	public Produit(String name, Double price, String status) {
		this.nom = name;
		this.prix = price;
		this.status = status;
	}

	/***
	 * Constructor
	 * 
	 * @param nom
	 * @param prix
	 * @param status
	 */
	public Produit(int produitId, String nom, Double prix, String commentaires, String status) {
		this(nom, prix, commentaires, status);
		this.produitId = produitId;

	}

	/**
	 * Insert the product into the database
	 * 
	 * @return 0 if the request return no result
	 * 
	 * @throws SQLException
	 */
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO Produit(Nom,Prix,Commentaires,Status) VALUES (?,?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.nom, Types.VARCHAR);
		statement.setObject(2, this.prix, Types.DECIMAL);
		statement.setObject(3, this.comment, Types.VARCHAR);
		statement.setObject(4, this.status, Types.VARCHAR);

		return statement.executeUpdate();
	}

	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	private static Statement selectAllProduct() throws SQLException {
		String reqSql = "SELECT * FROM Produit";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}
	
	/**
	 * return the list of all product stored in the database
	 * @return <type> List<product> </type>
	 * @throws SQLException
	 */

	public static List<Produit> getAllProduct() throws SQLException {

		ResultSet result = selectAllProduct().getResultSet();
		List<Produit> allProduct = new ArrayList<Produit>();
		while (result.next()) {
			int produitId = result.getInt("ProduitId");
			String name = result.getString("Nom");
			Double price = result.getDouble("Prix");
			String comment = result.getString("Commentaires");
			String status = result.getString("Status");
			allProduct.add(new Produit(produitId, name, price, comment, status));

		}

		return allProduct;
	}

	/**
	 * print all product in the consol
	 * @throws SQLException
	 */
	public static void printAllProduct() throws SQLException {

		List<Produit> allProduct = getAllProduct();

		for (Produit produit : allProduct)
			System.out.println(produit);
	}

	/**
	 * Met à jour le produit dans la base de données en utlisant sont Identifiant
	 * 
	 * @return <type>int</type>
	 * @throws SQLException
	 */
	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE Produit SET nom=?, prix=?, commentaires=?, status=? WHERE ProduitId=?;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.nom.toString(), Types.VARCHAR);
		statement.setObject(2, this.prix, Types.DECIMAL);
		statement.setObject(3, this.comment, Types.VARCHAR);
		statement.setObject(4, this.status, Types.VARCHAR);
		statement.setObject(5, this.produitId, Types.INTEGER);
		// System.out.println(this.produitId);
		// System.out.println("MAJ");
		return statement.executeUpdate();

	}

	/**
	 * return a product object from a product store ine the database by using
	 * productId
	 * 
	 * @param productId
	 * @return
	 * @throws SQLException
	 */
	public static Produit getProductById(int productId) throws SQLException {
		String reqSql = "SELECT ProduitId,Nom,Prix,Commentaires,Status FROM Produit WHERE ProduitId=?;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, productId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			int produitId = result.getInt("ProduitId");
			String name = result.getString("Nom");
			Double price = result.getDouble("Prix");
			String comment = result.getString("Commentaires");
			String status = result.getString("Status");
			return new Produit(produitId, name, price, comment, status);

		} else {
			throw new SQLException("Data not found");
		}

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

	private void setStatus(String status) {
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
	public String getComment() {
		return comment;
	}

	/**
	 * @param commentaire the commentaire to set
	 */
	private void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {

		return "" + this.produitId + "|" + this.nom + "|" + this.prix + "|" + this.comment + "|" + this.status;
	}

}
