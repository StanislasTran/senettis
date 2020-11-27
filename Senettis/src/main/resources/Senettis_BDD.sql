

Create table Produit (
    "ProduitId"  INT PRIMARY KEY IDENTITY (1, 1),
    "Nom" VARCHAR (100) NOT NULL,
	"Marque" VARCHAR (100),
    "Prix" decimal (4,2) NOT NULL,
    "Commentaires" VARCHAR (500),
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_produit CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
	
);

GO

Create TRIGGER Product_Update
ON Produit
AFTER UPDATE
AS
UPDATE
Produit
SET Date_de_modification = GETDATE()
 WHERE ProduitId IN (SELECT DISTINCT ProduitId FROM Inserted);
 GO

Create table Chantier (
    ChantierId INT PRIMARY KEY IDENTITY (1, 1),
    Nom VARCHAR (100) NOT NULL,
    CA decimal,
    adresse VARCHAR (500),
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()),
	CONSTRAINT check_status_chantier CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
	 
);

GO

Create TRIGGER Chantier_Update
ON Chantier
AFTER UPDATE
AS
UPDATE
Chantier
SET Date_de_modification = GETDATE()
 WHERE ChantierId IN (SELECT DISTINCT ChantierId FROM Inserted);
 GO



Create table Employe (
    EmployeId INT PRIMARY KEY IDENTITY (1, 1),
    Titre VARCHAR (10),
    Nom VARCHAR (100) NOT NULL,
    Prenom VARCHAR (100) NOT NULL,
    Mail VARCHAR(50),
	Qualifications VARCHAR(50),
    Telephone VARCHAR(15),
    Numero_matricule VARCHAR(40) NOT NULL,
    Pointure VARCHAR(10),
    Taille VARCHAR(10),
    Date_arrivee DATE,
	anciennete INT,
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_employe CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
    CONSTRAINT check_titre CHECK ((Titre) IN ('M','Mme')),
    constraint check_mail check (Mail like '%_@__%.__%'),
    CONSTRAINT matricule_unique UNIQUE (Numero_matricule),   
);

GO

Create TRIGGER Employe_Update
ON Employe
AFTER UPDATE
AS
UPDATE
Employe
SET Date_de_modification = GETDATE()
 WHERE EmployeId IN (SELECT DISTINCT EmployeId FROM Inserted);
 GO


Create table Livraison (
    LivraisonId INT PRIMARY KEY IDENTITY (1, 1),
    Chantier int NOT NULL,
    Date DATE , 
    FOREIGN KEY (Chantier) REFERENCES Chantier(ChantierId), 
    PrixTotal decimal,
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_livraison CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
	 
);

GO

Create TRIGGER Livraison_Update
ON Livraison
AFTER UPDATE
AS
UPDATE
Livraison
SET Date_de_modification = GETDATE()
 WHERE LivraisonId IN (SELECT DISTINCT LivraisonId FROM Inserted);
 GO


Create table Affectation (
 AffectationId INT PRIMARY KEY IDENTITY (1, 1),
    Chantier int NOT NULL,
    Employe int NOT NULL,
    FOREIGN KEY (Chantier) REFERENCES Chantier(ChantierId), 
    FOREIGN KEY (Employe) REFERENCES Employe(EmployeId),
    Nombre_heures decimal,
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_affectation CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
	 
);

GO

Create TRIGGER Affectation_Update
ON Affectation
AFTER UPDATE
AS
UPDATE
Affectation
SET Date_de_modification = GETDATE()
 WHERE AffectationId IN (SELECT DISTINCT AffectationId FROM Inserted);
 GO

Create table ProduitParLivraison(
 ProduitParLivraisonId INT PRIMARY KEY IDENTITY (1, 1),
    Produit int NOT NULL,
    Livraison int NOT NULL,
    FOREIGN KEY (Livraison) REFERENCES Livraison(LivraisonId),
    FOREIGN KEY (Produit) REFERENCES Produit(ProduitId),
    Quantite int DEFAULT 1,
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_produit_par_livraison CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
	 
);


GO

Create TRIGGER ProduitParLivraison_Update
ON ProduitParLivraison
AFTER UPDATE
AS
UPDATE
ProduitParLivraison
SET Date_de_modification = GETDATE()
 WHERE ProduitParLivraisonId IN (SELECT DISTINCT ProduitParLivraisonId FROM Inserted);
 GO

 
Create table ChiffreAffaire (
    "ChiffreAffajreId"  INT IDENTITY  (1, 1),
    mois int NOT NULL,
	annee int NOT NULL,
	Chantier int NOT NULL,
    FOREIGN KEY (Chantier) REFERENCES Chantier(ChantierId), 
	CA decimal, 
	menage decimal,
	vitrerie decimal,
	fournitures_sanitaires decimal, 
	mise_a_blanc decimal,
	autres decimal,
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_ChiffreAffaire CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
	Primary Key (mois,annee,chantier)
	
);


GO

Create TRIGGER CoutChantier_Update
ON CoutChantier
AFTER UPDATE
AS
UPDATE
CoutChantier
SET Date_de_modification = GETDATE()
 WHERE CoutChantierId IN (SELECT DISTINCT CoutChantierId FROM Inserted);
 GO

Create table CoutEmploye (
    "CoutEmployeId"  INT PRIMARY KEY IDENTITY (1, 1),
	mois int NOT NULL,
	annee int NOT NULL,
	Employe int NOT NULL,
    FOREIGN KEY (Employe) REFERENCES Employe(EmployeId), 
	mutuelle decimal,
	indemnite_panier decimal,
	salaire_brut decimal, 
	salaire_net decimal, 
	cout_transport decimal, 
	cout_telephone decimal, 
	charges_patronales decimal,
	masse_salariale decimal, 
	menage decimal, 
	vitrerie decimal, 
	mises_a_blanc decimal,
	fournitures_sanitaires decimal, 
	autres decimal, 
	remboursement_prets decimal,
	saisie_arret decimal,
	nb_heures decimal,
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_coutEmploye CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
	CONSTRAINT coutPeriode_unique UNIQUE (employe,mois,annee), 
	
);

GO

Create TRIGGER CoutEmploye_Update
ON CoutEmploye
AFTER UPDATE
AS
UPDATE
CoutEmploye
SET Date_de_modification = GETDATE()
 WHERE CoutEmployeId IN (SELECT DISTINCT CoutEmployeId FROM Inserted);
 GO

 Create table AmmortissementEmploye (
    "AmmortissementEmployeId"  INT PRIMARY KEY IDENTITY (1, 1),
	moisDepart int NOT NULL,
	anneeDepart int NOT NULL,
	moisFin int NOT NULL,
	anneeFin int NOT NULL,
	Employe int NOT NULL,
    FOREIGN KEY (Employe) REFERENCES Employe(EmployeId), 
	duree int NOT NULL,
	valeur decimal NOT NULL,
	valeurParMois decimal,
	type VARCHAR(100) NOT NULL,
	description VARCHAR(200),
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_ammortissementEmploye CHECK (("Status") IN ('Publié','Brouillon','Archivé'))
);

GO

Create TRIGGER AmmortissementEmploye_Update
ON AmmortissementEmploye
AFTER UPDATE
AS
UPDATE
AmmortissementEmploye
SET Date_de_modification = GETDATE()
 WHERE AmmortissementEmployeId IN (SELECT DISTINCT AmmortissementEmployeId FROM Inserted);
 GO


  Create table AmmortissementChantier (
    AmmortissementChantierId  INT PRIMARY KEY IDENTITY (1, 1),
	moisDepart int NOT NULL,
	anneeDepart int NOT NULL,
	moisFin int NOT NULL,
	anneeFin int NOT NULL,
	Chantier int NOT NULL,
    FOREIGN KEY (Chantier) REFERENCES Chantier(ChantierId), 
	duree int NOT NULL,
	valeur decimal NOT NULL,
	valeurParMois decimal,
	type VARCHAR(100) NOT NULL,
	description VARCHAR(200),
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_ammortissementChantier CHECK (("Status") IN ('Publié','Brouillon','Archivé'))
);

GO

Create TRIGGER AmmortissementChantier_Update
ON AmmortissementChantier
AFTER UPDATE
AS
UPDATE
AmmortissementChantier
SET Date_de_modification = GETDATE()
 WHERE AmmortissementChantierId IN (SELECT DISTINCT AmmortissementChantierId FROM Inserted);
 GO


  Create table FournitureSanitaire (
    FournitureSanitaireId  INT PRIMARY KEY IDENTITY (1, 1),
	moisDepart int,
	anneeDepart int,
	Chantier int NOT NULL,
    FOREIGN KEY (Chantier) REFERENCES Chantier(ChantierId), 
	valeurParMois decimal NOT NULL,
	sousTraitant VARCHAR(100) NOT NULL,
	description VARCHAR(200),
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_fournitureSanitaire CHECK (("Status") IN ('Publié','Brouillon','Archivé'))
);

GO

Create TRIGGER FournitureSanitaire_Update
ON FournitureSanitaire
AFTER UPDATE
AS
UPDATE
FournitureSanitaire
SET Date_de_modification = GETDATE()
 WHERE FournitureSanitaireId IN (SELECT DISTINCT FournitureSanitaireId FROM Inserted);
 GO