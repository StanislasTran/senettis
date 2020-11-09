
package classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.microsoft.sqlserver.jdbc.StringUtils;

import connexion.SQLDatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Employee {

	private Integer employeId;
	private Title titre;
	private String nom;
	private String prenom;
	private String mail;
	private String telephone;
	private String numeroMatricule;
	private String pointure;
	private String taille;
	private String dateArrivee;
	private Double nombreHeures;
	private Double remboursementTransport;
	private Double remboursementTelephone;
	private Double salaire;
	private String status;

	// Constructeurs-----------------------------------------------
	/***
	 * 
	 * cree un employe a partir des champs employeId, titre, nom, prenom,
	 * numeroMatricule, status, dateArrivee, mail, telephone, pointure, taille,
	 * nombreHeures, renboursementTransport, remboursementTelephone, salaire
	 * 
	 * @param employeId              : int
	 * @param t                      : verification faite
	 * @param nom
	 * @param prenom
	 * @param mail                   : verification faite
	 * @param telephone
	 * @param numeroMatricule        : int
	 * @param pointure
	 * @param taille
	 * @param dateArrivee            : string, verification faite
	 * @param nombreHeures           : double
	 * @param remboursementTransport :double
	 * @param remboursementTelephone : double
	 * @param salaire                :double
	 * @param status                 : verification faite
	 */
	public Employee(int employeId, String t, String nom, String prenom, String mail, String telephone,
			String numeroMatricule, String pointure, String taille, String dateArrivee, Double nombreHeures,
			Double remboursementTransport, Double remboursementTelephone, Double salaire, String status) {
		this(t, nom, prenom, mail, telephone, numeroMatricule, pointure, taille, dateArrivee, nombreHeures,
				remboursementTransport, remboursementTelephone, salaire, status);

		// id
		if ((Integer) employeId != null) {
			this.employeId = employeId;
		} else {
			throw new Error("L'employeId est vide, merci de spécifier un id ou d'utiliser un autre constructeur.");
		}
	}

	/***
	 * cree un employe a partir des champs titre, nom, prenom, numeroMatricule,
	 * status, dateArrivee, mail, telephone, pointure, taille, nombreHeures,
	 * renboursementTransport, remboursementTelephone, salaire
	 * 
	 * @param t                      : verification faite
	 * @param nom
	 * @param prenom
	 * @param mail                   : verification faite
	 * @param telephone
	 * @param numeroMatricule        : int
	 * @param pointure
	 * @param taille
	 * @param dateArrivee            : string, verification faite
	 * @param nombreHeures           : double
	 * @param remboursementTransport :double
	 * @param remboursementTelephone : double
	 * @param salaire                :double
	 * @param status                 : verification faite
	 */
	public Employee(String t, String nom, String prenom, String mail, String telephone, String numeroMatricule,
			String pointure, String taille, String dateArrivee, Double nombreHeures, Double remboursementTransport,
			Double remboursementTelephone, Double salaire, String status) {
		this(t, nom, prenom, numeroMatricule, dateArrivee, status);

		// je verifie que l adresse mail est correcte
		if (mail != null ) {
			if (mail.isEmpty()) {
				mail = null;
			}
			else {
				String regex = "^(.+)@(.+)$";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(mail);
				if (!matcher.matches()) {
					throw new Error("L'adresse mail est incorrecte.");
				}
			}
		}
		this.mail = mail;

		// telephone
		if (telephone != null) {
			telephone = telephone.replace(".", "");
		} 
		this.telephone = telephone;
		
		// pointure
		this.pointure = pointure;
		
		// taille
		this.taille = taille;
		
		// nombreHeures
		this.nombreHeures = nombreHeures;
		
		// remboursement transport
		this.remboursementTransport = remboursementTransport;
		
		// remboursement telephone
		this.remboursementTelephone = remboursementTelephone;
		
		// salaire
		this.salaire = salaire;
	}

	/***
	 * cree un employe a partir des champs titre, nom, prenom, numeroMatricule,
	 * status et dateArrivee
	 * 
	 * @param t               : verification faite
	 * @param nom
	 * @param prenom
	 * @param numeroMatricule : int
	 * @param dateArrivee     : string, verification faite
	 * @param status          : verification faite
	 */
	public Employee(String t, String nom, String prenom, String numeroMatricule, String dateArrivee, String status) {
		this(t, nom, prenom, numeroMatricule);

		// status
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
		
		// date arrivee
		if (dateArrivee != null) {
			if (!(dateArrivee.isEmpty())) {
				if ( dateArrivee.charAt(2) =='/' || dateArrivee.charAt(2) == '-' || dateArrivee.charAt(2) == '_') {
					//System.out.println("fr");
					// date francaise
					// on reecrit en format francais juste pour s'assurer que toutes les dates
					// seront ecrites avec le meme format jj/mm/aaaa
					dateArrivee = dateArrivee.substring(0, 2) + "/" + dateArrivee.substring(3, 5) + "/"
							+ dateArrivee.substring(6, 10);
				}
				else if ( dateArrivee.charAt(7) =='/' || dateArrivee.charAt(7) == '-' || dateArrivee.charAt(7) == '_') {
					//System.out.println("en");
					// date anglaise
					dateArrivee = dateArrivee.substring(8, 10) + "/" + dateArrivee.substring(5, 7) + "/"
							+ dateArrivee.substring(0, 4);
				} else {
					throw new Error(
							"La date d'arrivée indiquée est incorrecte, une date doit être indiqué selon un des formats suivant : 31-01-2000, 31/01/2000, 2000-01-31 ou 2000/01/31.");
				}
			}
			else {
				dateArrivee = null;
			}
		}
		this.dateArrivee = dateArrivee;		
	}

	/***
	 * cree un employe a partir des champs titre, nom, prenom et numeroMatricule
	 * 
	 * @param t               : verification faite
	 * @param nom
	 * @param prenom
	 * @param numeroMatricule : int
	 */
	public Employee(String t, String nom, String prenom, String numeroMatricule) {
		super();

		// je verifie le titre
		if (t != null) {
			if ((t == "M") || (t == "M.")) {
				this.titre = titre.M;
			} else if ((t == "Mme") || (t == "MME") || (t == "Mme.") || (t == "MME.")) {
				this.titre = titre.Mme;
			} else if (t.contains("me") || t.contains("ME")) {
				this.titre = titre.Mme;
			} else {
				this.titre = titre.M;
			}
		} else {
			this.titre = titre.M;
		}

		// nom
		if (nom != null) {
			this.nom = nom;
		} else {
			throw new Error("Le nom indiqué est vide.");
		}

		// prenom
		if (prenom != null) {
			this.prenom = prenom;
		} else {
			throw new Error("Le prénom indiqué est vide.");
		}

		// numero matricule
		if (numeroMatricule != null) {
			this.numeroMatricule = numeroMatricule;
		} else {
			throw new Error("Le numéro de matricule indiqué est vide.");
		}
	}

	// Liens avec la BDD-----------------------------------------------
	/***
	 * permet d'ajouter un employe dans la base de données
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO Employe(titre,nom,prenom,mail,telephone,numero_matricule,pointure,taille,date_arrivee,nombre_heures,remboursement_transport,remboursement_telephone,salaire,status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.titre.toString(), Types.VARCHAR);
		statement.setObject(2, this.nom, Types.VARCHAR);
		statement.setObject(3, this.prenom, Types.VARCHAR);
		statement.setObject(4, this.mail, Types.VARCHAR);
		statement.setObject(5, this.telephone, Types.VARCHAR);
		statement.setObject(6, this.numeroMatricule, Types.DECIMAL);
		statement.setObject(7, this.pointure, Types.VARCHAR);
		statement.setObject(8, this.taille, Types.VARCHAR);
		statement.setObject(9, this.dateArrivee, Types.DATE);
		statement.setObject(10, this.nombreHeures, Types.DECIMAL);
		statement.setObject(11, this.remboursementTransport, Types.VARCHAR);
		statement.setObject(12, this.remboursementTelephone, Types.VARCHAR);
		statement.setObject(13, this.salaire, Types.DECIMAL);
		statement.setObject(14, this.status, Types.VARCHAR);

		return statement.executeUpdate();
	}

	/***
	 * Permet de modifier un employe dans la base de données, pour que la requete
	 * fonctionne il faut que l'employe ai son champs employeId completé car
	 * l'update se fait a partir de l'id la requete modifiera tous les champs de
	 * l'employe en les remplacant par la valeur actuelle associee a cet employe
	 * pour chaque champs
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE Employe SET titre=?, nom=?, prenom=?, mail=?, telephone=?, numero_matricule=?, pointure=?, taille=?, date_arrivee=?, nombre_heures=?, remboursement_transport=?, remboursement_telephone=?, salaire=?, status=? WHERE EmployeId=?;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.titre.toString(), Types.VARCHAR);
		statement.setObject(2, this.nom, Types.VARCHAR);
		statement.setObject(3, this.prenom, Types.VARCHAR);
		statement.setObject(4, this.mail, Types.VARCHAR);
		statement.setObject(5, this.telephone, Types.VARCHAR);
		statement.setObject(6, this.numeroMatricule, Types.DECIMAL);
		statement.setObject(7, this.pointure, Types.VARCHAR);
		statement.setObject(8, this.taille, Types.VARCHAR);
		statement.setObject(9, this.dateArrivee, Types.DATE);
		statement.setObject(10, this.nombreHeures, Types.DECIMAL);
		statement.setObject(11, this.remboursementTransport, Types.VARCHAR);
		statement.setObject(12, this.remboursementTelephone, Types.VARCHAR);
		statement.setObject(13, this.salaire, Types.DECIMAL);
		statement.setObject(14, this.status, Types.VARCHAR);
		statement.setObject(15, this.employeId, Types.INTEGER);

		return statement.executeUpdate();
	}

	/***
	 * requete la base de données afin de recuperer tous les elements de la table
	 * employe
	 * 
	 * @return
	 * @throws SQLException
	 */
	private static Statement selectAllEmploye() throws SQLException {
		String reqSql = "SELECT * FROM Employe";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}

	/**
	 * Retourne le nombre d'employé dans la base de données
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static int getCountEmploye() throws SQLException {
		String reqSql = "SELECT count(*) as count FROM Employe";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		ResultSet result = statement.getResultSet();
		if (result.next()) {
			if (result.getInt("count") > 0) {
				return result.getInt("count");
			}
		}
		return 0;
	}

	/***
	 * 
	 * @param employeId
	 * @return l'employe correspondant à l'id indique en argument
	 * @throws SQLException
	 */
	public static Employee getEmployeById(int employeId) throws SQLException {
		String reqSql = "SELECT EmployeId,titre,nom,prenom,mail,telephone,numero_matricule,pointure,taille,date_arrivee,nombre_heures,remboursement_transport,remboursement_telephone,Salaire,Status FROM Employe WHERE EmployeId=?;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, employeId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			employeId = result.getInt("EmployeId");
			String titre = result.getString("Titre");
			String nom = result.getString("Nom");
			String prenom = result.getString("Prenom");
			String mail = result.getString("mail");
			String telephone = result.getString("Telephone");
			String numeroMatricule = result.getString("Numero_matricule");
			String pointure = result.getString("Pointure");
			String taille = result.getString("Taille");
			String dateArrivee = result.getString("Date_arrivee");
			
			Double nombreHeures = 0.0;
			if (result.getString("Nombre_heures") != null) {
				nombreHeures = Double.parseDouble(result.getString("Nombre_heures"));
			}
			
			Double remboursementTransport = 0.0;
			if (result.getString("remboursement_transport") != null) {
				remboursementTransport = Double.parseDouble(result.getString("remboursement_transport"));
			}
			
			Double remboursementTelephone = 0.0;
			if (result.getString("remboursement_telephone") != null) {
				remboursementTelephone = Double.parseDouble(result.getString("remboursement_telephone"));
			}
			
			Double salaire = 0.0;
			if (result.getString("salaire") != null) {
				salaire = Double.parseDouble(result.getString("salaire"));
			}

			String status = result.getString("status");

			return new Employee(employeId, titre, nom, prenom, mail, telephone, numeroMatricule, pointure, taille,
					dateArrivee, nombreHeures, remboursementTransport, remboursementTelephone, salaire, status);

		} else {
			throw new SQLException("Data not found");
		}
	}

	// -----------------------------------------------
	/***
	 * 
	 * @return une liste contenant tous les employes renvoyes par selectAllEmploye()
	 * @throws SQLException
	 */
	public static List<Employee> getAllEmploye() throws SQLException {

		ResultSet result = selectAllEmploye().getResultSet();
		List<Employee> allEmploye = new ArrayList<Employee>();
		while (result.next()) {
			int employeId = result.getInt("EmployeId");
			String titre = result.getString("Titre");
			String nom = result.getString("Nom");
			String prenom = result.getString("Prenom");
			String mail = result.getString("Mail");
			String telephone = result.getString("Telephone");
			String numeroMatricule = result.getString("Numero_matricule");
			String pointure = result.getString("Pointure");
			String taille = result.getString("Taille");

			String dateArrivee;
			if (result.getString("Date_arrivee") == null) {
				dateArrivee = null;
			} else {
				String usDate = result.getString("Date_arrivee");
				// on transforme en date au format francais
				dateArrivee = usDate.substring(8, 10) + "/" + usDate.substring(5, 7) + "/" + usDate.substring(0, 4);
			}

			Double nombreHeures = result.getDouble("Nombre_heures");
			Double remboursementTransport = result.getDouble("Remboursement_transport");
			Double remboursementTelephone = result.getDouble("Remboursement_telephone");
			Double salaire = result.getDouble("Salaire");
			String status = result.getString("Status");

			allEmploye.add(new Employee(employeId, titre, nom, prenom, mail, telephone, numeroMatricule, pointure,
					taille, dateArrivee, nombreHeures, remboursementTransport, remboursementTelephone, salaire,
					status));

		}

		return allEmploye;
	}

	public static void printAllEmploye() throws SQLException {

		List<Employee> allEmploye = getAllEmploye();

		for (Employee employe : allEmploye)
			System.out.println(employe);
	}

	@Override
	public String toString() {

		return "" + this.employeId + "|" + this.titre + "|" + this.nom + "|" + this.prenom + "|" + this.mail + "|"
				+ this.telephone + "|" + this.numeroMatricule + "|" + this.pointure + "|" + this.taille + "|"
				+ this.dateArrivee + "|" + this.nombreHeures + "|" + this.remboursementTransport + "|"
				+ this.remboursementTelephone + "|" + this.salaire + "|" + this.status;
	}

	// Getter and setter-----------------------------------------------
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

	public void setTitre(Title titre) {
		if (titre == null) {
			throw new Error("setTitre : le titre indique est vide");
		}
		this.titre = titre;
	}

	public String getTitre() {
		if (titre == null) {
			return "";
		}
		return titre.toString();
	}

	public void setTitre(String t) {
		if (titre == null) {
			throw new Error("setTitre : le titre indique est vide");
		}
		if (t != null) {
			if ((t == "M") || (t == "M.")) {
				this.titre = titre.M;
			} else if ((t == "Mme") || (t == "MME") || (t == "Mme.") || (t == "MME.")) {
				this.titre = titre.Mme;
			} else if (t.contains("me") || t.contains("ME")) {
				this.titre = titre.Mme;
			} else {
				this.titre = titre.M;
			}
		}
	}

	public String getNom() {
		if (nom == null) {
			return "";
		}
		return nom;
	}

	public void setNom(String nom) {
		if (nom == null) {
			throw new Error("setNom : le nom indique est vide");
		}
		this.nom = nom;
	}

	public String getPrenom() {
		if (prenom == null) {
			return "";
		}
		return prenom;
	}

	public void setPrenom(String prenom) {
		if (prenom == null) {
			throw new Error("setPrenom : le prenom indique est vide");
		}
		this.prenom = prenom;
	}

	public String getMail() {
		if (mail == null) {
			return "";
		}
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
			throw new Error("L'adresse mail est incorrecte.");
		}
		this.mail = mail;
	}

	public String getTelephone() {
		if (telephone == null) {
			return "";
		}
		return telephone;
	}

	public void setTelephone(String telephone) {
		if (telephone == null) {
			throw new Error("setTelephone : le telephone indique est vide");
		}
		telephone = telephone.replace(".", "");
		this.telephone = telephone;
	}

	public String getNumeroMatricule() {
		if (numeroMatricule == null) {
			return "";
		}
		return numeroMatricule;
	}

	public void setNumeroMatricule(String numeroMatricule) {
		if (numeroMatricule == null) {
			throw new Error("setNumeroMatricule : le numeroMatricule indique est vide");
		}
		this.numeroMatricule = numeroMatricule;
	}

	public String getPointure() {
		if (pointure == null) {
			return "";
		}
		return pointure;
	}

	public void setPointure(String pointure) {
		if (pointure == null) {
			throw new Error("setPointure : le pointure indique est vide"); 
		}
		this.pointure = pointure;
	}

	public String getTaille() {
		if (taille == null) {
			return "";
		}
		return taille;
	}

	public void setTaille(String taille) {
		if (taille == null) {
			throw new Error("setTaille : le taille indique est vide");
		}
		this.taille = taille;
	}

	public String getDateArrivee() {

		return dateArrivee;
	}

	public void setDateArrivee(String dateArrivee) {
		// date arrivee
		if (dateArrivee != null) {
			if (StringUtils.isNumeric(dateArrivee.substring(0, 4))
					&& StringUtils.isNumeric(dateArrivee.substring(8, 10))
					&& StringUtils.isNumeric(dateArrivee.substring(5, 7))) {
				// date anglaise
				// on reecrit en format francais
				dateArrivee = dateArrivee.substring(8, 10) + "/" + dateArrivee.substring(5, 7) + "/"
						+ dateArrivee.substring(0, 4);
			} else if (StringUtils.isNumeric(dateArrivee.substring(6, 10))
					&& StringUtils.isNumeric(dateArrivee.substring(3, 5))
					&& StringUtils.isNumeric(dateArrivee.substring(0, 2))) {
				// date francaise
				// on reecrit en format francais juste pour s'assurer que toutes les dates
				// seront ecrites avec le meme format jj/mm/aaaa
				dateArrivee = dateArrivee.substring(0, 2) + "/" + dateArrivee.substring(3, 5) + "/"
						+ dateArrivee.substring(6, 10);
			} else {
				throw new Error(
						"La date d'arrivée indiquée est incorrecte, une date doit être indiqué selon un des formats suivant : 31-01-2000, 31/01/2000, 2000-01-31 ou 2000/01/31.");
			}
		} else {
			throw new Error("La date d'arrivée indiquée est vide.");
		}
		this.dateArrivee = dateArrivee;

	}

	public Double getNombreHeures() {
		if (nombreHeures == null) {
			return 0.0;
		}
		return nombreHeures;
	}

	public void setNombreHeures(Double nombreHeures) {
		if (nombreHeures == null) {
			throw new Error("setNombreHeures : le nombreHeures indique est vide");
		}
		this.nombreHeures = nombreHeures;
	}

	public Double getRemboursementTransport() {
		if (remboursementTransport == null) {
			return 0.0;
		}
		return remboursementTransport;
	}

	public void setRemboursementTransport(Double remboursementTransport) {
		if (remboursementTransport == null) {
			throw new Error("setRemboursementTransport : le remboursementTransport indique est vide");
		}
		this.remboursementTransport = remboursementTransport;
	}

	public Double getRemboursementTelephone() {
		if (remboursementTelephone == null) {
			return 0.0;
		}
		return remboursementTelephone;
	}

	public void setRemboursementTelephone(Double remboursementTelephone) {
		if (remboursementTelephone == null) {
			throw new Error("setRemboursementTelephone : le remboursementTelephone indique est vide");
		}
		this.remboursementTelephone = remboursementTelephone;
	}

	public Double getSalaire() {
		if (salaire == null) {
			return 0.0;
		}
		return salaire;
	}

	public void setSalaire(Double salaire) {
		if (salaire == null) {
			throw new Error("setSalaire : le salaire indique est vide");
		}
		this.salaire = salaire;
	}

	public int getEmployeId() {
		return employeId;
	}

	public void setEmployeId(Integer employeId) {
		if (employeId == null) {
			throw new Error("setEmployeId : le employeId indique est vide");
		}
		this.employeId = employeId;
	}
	
	

}
