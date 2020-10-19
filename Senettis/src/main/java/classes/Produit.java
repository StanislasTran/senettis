package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import connexion.SQLDatabaseConnection;

public class Produit {

	private String nom;
	private Double prix;
	private String commentaires;
	private String status;
	
	public String getStatus() {
		return status;
	}

	private  void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Constructor for Produit
	 * 
	 * @param prix
	 * @param nom
	 * @param commentaire
	 */
	public Produit(String nom, Double prix, String commentaires,String status) {
		this.nom = nom;
		this.prix = prix;
		this.commentaires = commentaires;
		this.status=status;
	}

	public Produit(String nom, Double prix) {
		this.nom = nom;
		this.prix = prix;
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
	
	/*
	 * public int deleteDatabase(int id) throws SQLException { String reqSql =
	 * "UPDATE Produit(nom,prix,commentaires) VALUES (?,?,?)";
	 * 
	 * Connection connection = DriverManager.getConnection(new
	 * SQLDatabaseConnection().getConnectionUrl()); PreparedStatement statement =
	 * connection.prepareStatement(reqSql); statement.setObject(1,id,Types.DECIMAL);
	 * 
	 * return statement.executeUpdate(); }
	 */

}
