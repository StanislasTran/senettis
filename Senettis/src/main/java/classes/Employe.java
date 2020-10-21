package classes;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connexion.SQLDatabaseConnection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Employe {

	private int employeId;
	private Titre titre;
	private String nom;
	private String prenom;
	private String mail;
	private String telephone;
	private Integer numeroMatricule;
	private String pointure;
	private String taille;
	private Date dateArrivee;
	private Double nombreHeures;
	private Double remboursementTransport;
	private Double remboursementTelephone;
	private Double salaire;
	private String status;
	
	public Employe(int EmployeId, String t, String nom, String prenom, String mail, String telephone, Integer numeroMatricule,
			String pointure, String taille, String dateArrivee, Double nombreHeures, Double remboursementTransport,
			Double remboursementTelephone, Double salaire,String status) {
		this(t,nom,prenom,numeroMatricule,dateArrivee,status);
		
		this.employeId = employeId;
		
		//je verifie que l adresse mail est correcte 
		String regex = "^(.+)@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(mail);
		if (!matcher.matches()) {
			throw new Error("l adresse mail est incorrecte");
		}
		
	
		this.mail = mail;
		telephone = telephone.replace(".", "");
		this.telephone = telephone;
		this.pointure = pointure;
		this.taille = taille;
	
		
		//on transforme la date qui est en string vers un type date
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    Date dateFinale = null;
		
		this.dateArrivee = dateFinale;
		
		this.nombreHeures = nombreHeures;
		this.remboursementTransport = remboursementTransport;
		this.remboursementTelephone = remboursementTelephone;
		this.salaire = salaire;
	}

	public Employe(String t, String nom, String prenom,Integer numeroMatricule, String dateArrivee,String status) {
		this(t,nom,prenom,numeroMatricule);
		this.status=status;

		
		//on transforme la date qui est en string vers un type date
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    Date dateFinale = null;
		try {
			dateFinale = simpleDateFormat.parse(dateArrivee);
		} catch (ParseException e) {
			simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
			try {
				dateFinale = simpleDateFormat.parse(dateArrivee);
			} catch (ParseException e2) {
				e2.printStackTrace();
			}
		}
		this.dateArrivee = dateFinale;
	}

	public Employe(String t, String nom, String prenom, Integer numeroMatricule) {
		super();
		
		//je verifie le titre
		if (t != null) {
			if ((t == "M") || (t == "M.")) {
				this.titre = titre.M;
			}
			else if ((t == "Mme") ||  (t == "MME")||  (t == "Mme.")||  (t == "MME.")) {
				this.titre = titre.Mme;
			}		
			else if (t.contains("me")||t.contains("ME")) {
				this.titre = titre.Mme;
			}
			else {
				this.titre = titre.M;
			}
		}
		
		this.nom = nom;
		this.prenom = prenom;
		this.numeroMatricule = numeroMatricule;
	}

	
	public Employe(int EmployeId, String t, String nom, String prenom, String mail, String telephone, Integer numeroMatricule,
			String pointure, String taille, Date dateArrivee, Double nombreHeures, Double remboursementTransport,
			Double remboursementTelephone, Double salaire,String status) {
		this(t,nom,prenom,numeroMatricule);
		
		this.status = status;
		this.dateArrivee = dateArrivee;
		this.employeId = employeId;
		
		//je verifie que l adresse mail est correcte 
		if (mail != null) {
			String regex = "^(.+)@(.+)$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(mail);
			if (!matcher.matches()) {
				throw new Error("l adresse mail est incorrecte");
			}
		}
	
		this.mail = mail;
		
		if (telephone != null) {
			telephone = telephone.replace(".", "");
		}
		this.telephone = telephone;
		this.pointure = pointure;
		this.taille = taille;
	
		
		//on transforme la date qui est en string vers un type date
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    Date dateFinale = null;
		
		this.dateArrivee = dateFinale;
		
		this.nombreHeures = nombreHeures;
		this.remboursementTransport = remboursementTransport;
		this.remboursementTelephone = remboursementTelephone;
		this.salaire = salaire;
	}

	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO Employe(titre,nom,prenom,mail,telephone,numero_matricule,pointure,taille,date_arrivee,nombre_heures,remboursement_transport,remboursement_telephone,salaire,status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1,this.titre.toString(),Types.VARCHAR);
		statement.setObject(2,this.nom,Types.VARCHAR);
		statement.setObject(3,this.prenom,Types.VARCHAR);
		statement.setObject(4,this.mail,Types.VARCHAR);
		statement.setObject(5,this.telephone,Types.VARCHAR);
		statement.setObject(6,this.numeroMatricule,Types.DECIMAL);
		statement.setObject(7,this.pointure,Types.VARCHAR);
		statement.setObject(8,this.taille,Types.VARCHAR);
		statement.setObject(9,this.dateArrivee,Types.DATE);
		statement.setObject(10,this.nombreHeures,Types.DECIMAL);
		statement.setObject(11,this.remboursementTransport,Types.VARCHAR);
		statement.setObject(12,this.remboursementTelephone,Types.VARCHAR);
		statement.setObject(13,this.salaire,Types.DECIMAL);
		statement.setObject(14,this.status,Types.VARCHAR);
		
		return statement.executeUpdate();
	}
	
	
	
	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE Employe SET titre=?, nom=?, prenom=?, mail=?, telephone=?, numero_matricule=?, pointure=?, taille=?, date_arrivee=?, nombre_heures=?, remboursement_transport=?, remboursement_telephone=?, salaire=?, status=? WHERE EmployeId=?;";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1,this.titre.toString(),Types.VARCHAR);
		statement.setObject(2,this.nom,Types.VARCHAR);
		statement.setObject(3,this.prenom,Types.VARCHAR);
		statement.setObject(4,this.mail,Types.VARCHAR);
		statement.setObject(5,this.telephone,Types.VARCHAR);
		statement.setObject(6,this.numeroMatricule,Types.DECIMAL);
		statement.setObject(7,this.pointure,Types.VARCHAR);
		statement.setObject(8,this.taille,Types.VARCHAR);
		statement.setObject(9,this.dateArrivee,Types.DATE);
		statement.setObject(10,this.nombreHeures,Types.DECIMAL);
		statement.setObject(11,this.remboursementTransport,Types.VARCHAR);
		statement.setObject(12,this.remboursementTelephone,Types.VARCHAR);
		statement.setObject(13,this.salaire,Types.DECIMAL);
		statement.setObject(14,this.status,Types.VARCHAR);
		statement.setObject(15,this.employeId,Types.INTEGER);
		
		return statement.executeUpdate();
	}
	
	
	private static Statement selectAllEmploye() throws SQLException {
		String reqSql = "SELECT * FROM Employe";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}
	
	public static List<Employe> getAllEmploye() throws SQLException {

		ResultSet result=selectAllEmploye().getResultSet();
		List<Employe> allEmploye=new ArrayList<Employe>();
		System.out.println("Id|Titre|Nom|Prenom|Mail|Telephone|numeroMatricule|Pointure|Taille|DateArrivée|NombreHeures|RemboursementTransport|RemboursementTelephone|Salaire|Status");
		while(result.next()) {
			int produitId=result.getInt("EmployeId");
			String titre=result.getString("Titre");
			String nom=result.getString("Nom");
			String prenom=result.getString("Prenom");
			String mail=result.getString("Mail");
			String telephone=result.getString("Telephone");
			int numeroMatricule=result.getInt("Numero_matricule");
			String pointure=result.getString("Pointure");
			String taille=result.getString("Taille");
			Date dateArrivee=result.getDate("Date_arrivee");
			Double nombreHeures=result.getDouble("Nombre_heures");
			Double remboursementTransport=result.getDouble("Remboursement_transport");
			Double remboursementTelephone=result.getDouble("Remboursement_telephone");
			Double salaire=result.getDouble("Salaire");
		    String status=result.getString("Status");
		   allEmploye.add(new Employe(produitId, titre, nom, prenom, mail, telephone, numeroMatricule, pointure, taille, dateArrivee, nombreHeures, remboursementTransport, remboursementTelephone, salaire, status));
		  
			
		}
		
		
		
		return allEmploye;
	}
	
	public static void printAllEmploye() throws SQLException {
		
		List<Employe> allEmploye=getAllEmploye();
	
		for (Employe employe : allEmploye)	
			System.out.println(employe);
	}
	
	@Override
	public String toString() {
		
		return ""+this.employeId+"|"+this.titre+"|"+this.nom+"|"+this.prenom+"|"+this.mail+"|"+this.telephone+"|"+this.numeroMatricule+"|"+this.pointure+"|"+this.taille+"|"+this.dateArrivee+"|"+this.nombreHeures+"|"+this.remboursementTransport+"|"+this.remboursementTelephone+"|"+this.salaire+"|"+this.status;
	}

	
	/**
	 * 
	 * getter and setter
	 */
	
	
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		if (status == null) {
			throw new Error("setStatus : le status indique est vide");
		}
		this.status = status;
	}


	public void setTitre(Titre titre) {
		if (titre == null) {
			throw new Error("setTitre : le titre indique est vide");
		}
		this.titre = titre;
	}


	public String getTitre() {
		return titre.toString();
	}


	public void setTitre(String t) {
		if (titre == null) {
			throw new Error("setTitre : le titre indique est vide");
		}
		if (t != null) {
			if ((t == "M") || (t == "M.")) {
				this.titre = titre.M;
			}
			else if ((t == "Mme") ||  (t == "MME")||  (t == "Mme.")||  (t == "MME.")) {
				this.titre = titre.Mme;
			}		
			else if (t.contains("me")||t.contains("ME")) {
				this.titre = titre.Mme;
			}
			else {
				this.titre = titre.M;
			}
		}
	}


	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		if (nom == null) {
			throw new Error("setNom : le nom indique est vide");
		}
		this.nom = nom;
	}


	public String getPrenom() {
		return prenom;
	}


	public void setPrenom(String prenom) {
		if (prenom == null) {
			throw new Error("setPrenom : le prenom indique est vide");
		}
		this.prenom = prenom;
	}


	public String getMail() {
		return mail;
	}


	public void setMail(String mail) {
		if (mail == null) {
			throw new Error("setMail : le mail indique est vide");
		}
		String regex = "^(.+)@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(mail);
		if (!matcher.matches()) {
			throw new Error("l adresse mail est incorrecte");
		}
		this.mail = mail;
	}


	public String getTelephone() {
		return telephone;
	}


	public void setTelephone(String telephone) {
		if (telephone == null) {
			throw new Error("setTelephone : le telephone indique est vide");
		}
		telephone = telephone.replace(".", "");
		this.telephone = telephone;
	}


	public Integer getNumeroMatricule() {
		return numeroMatricule;
	}


	public void setNumeroMatricule(Integer numeroMatricule) {
		if (numeroMatricule == null) {
			throw new Error("setNumeroMatricule : le numeroMatricule indique est vide");
		}
		this.numeroMatricule = numeroMatricule;
	}


	public String getPointure() {
		return pointure;
	}


	public void setPointure(String pointure) {
		if (pointure == null) {
			throw new Error("setPointure : le pointure indique est vide");
		}
		this.pointure = pointure;
	}


	public String getTaille() {
		return taille;
	}


	public void setTaille(String taille) {
		if (taille == null) {
			throw new Error("setTaille : le taille indique est vide");
		}
		this.taille = taille;
	}


	public Date getDateArrivee() {
		return dateArrivee;
	}


	public void setDateArrivee(Date dateArrivee) {
		if (dateArrivee == null) {
			throw new Error("setDateArrivee : le dateArrivee indique est vide");
		}
		this.dateArrivee = dateArrivee;
	}


	public Double getNombreHeures() {
		return nombreHeures;
	}


	public void setNombreHeures(Double nombreHeures) {
		if (nombreHeures == null) {
			throw new Error("setNombreHeures : le nombreHeures indique est vide");
		}
		this.nombreHeures = nombreHeures;
	}


	public Double getRemboursementTransport() {
		return remboursementTransport;
	}


	public void setRemboursementTransport(Double remboursementTransport) {
		if (remboursementTransport == null) {
			throw new Error("setRemboursementTransport : le remboursementTransport indique est vide");
		}
		this.remboursementTransport = remboursementTransport;
	}


	public Double getRemboursementTelephone() {
		return remboursementTelephone;
	}


	public void setRemboursementTelephone(Double remboursementTelephone) {
		if (remboursementTelephone == null) {
			throw new Error("setRemboursementTelephone : le remboursementTelephone indique est vide");
		}
		this.remboursementTelephone = remboursementTelephone;
	}


	public Double getSalaire() {
		return salaire;
	}


	public void setSalaire(Double salaire) {
		if (salaire == null) {
			throw new Error("setSalaire : le salaire indique est vide");
		}
		this.salaire = salaire;
	}

	
}
