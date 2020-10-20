package classes;

import java.sql.Connection;
import java.util.Date;

import connexion.SQLDatabaseConnection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ProduitParLivraison {

	private Integer idLivraison;
	private Integer idProduit;
	private int quantite;
	private String status;


	
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO ProduitParLivraison(livraison,produit,quantite,status) VALUES (?,?,?,?)";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1,this.idLivraison,Types.INTEGER);
		statement.setObject(2,this.idProduit,Types.INTEGER);
		statement.setObject(3,this.quantite,Types.DECIMAL);
		statement.setObject(4,this.status,Types.VARCHAR);
		return statement.executeUpdate();
	}



	public Integer getIdLivraison() {
		return idLivraison;
	}



	public void setIdLivraison(Integer idLivraison) {
		this.idLivraison = idLivraison;
	}



	public Integer getIdProduit() {
		return idProduit;
	}



	public void setIdProduit(Integer idProduit) {
		this.idProduit = idProduit;
	}



	public Integer getQuantite() {
		return quantite;
	}



	public void setQuantite(Integer quantite) {
		this.quantite = quantite;
	}



	public ProduitParLivraison(Integer idLivraison, Integer idProduit, Integer quantite,String status) {
	
		this.quantite = quantite;
		
	}



	public ProduitParLivraison(Integer idLivraison, Integer idProduit,String status) {
		super();
		this.idLivraison = idLivraison;
		this.idProduit = idProduit;
		this.status=status;
		this.quantite = 1;
	}


	
}
