

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


Create table AffectationChantier (
 AffectationId INT PRIMARY KEY IDENTITY (1, 1),
    Chantier int NOT NULL,
    Employe int NOT NULL,
    FOREIGN KEY (Chantier) REFERENCES Chantier(ChantierId), 
    FOREIGN KEY (Employe) REFERENCES Employe(EmployeId),
    Nombre_heures decimal,
	"Status" VARCHAR (50) NOT NULL,
	MoisDebut int NOT NULL,
	AnneeDebut int NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_affectationChantier CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
	
	CONSTRAINT check_moonthAffectationChantier CHECK (("MoisDebut")>=0 and ("MoisDebut")<=12),
	 
);
GO

Create TRIGGER AffectationChantier_Update
ON AffectationChantier
AFTER UPDATE
AS
UPDATE
AffectationChantier
SET Date_de_modification = GETDATE()
 WHERE AffectationId IN (SELECT DISTINCT AffectationId FROM Inserted);
 GO


Create table AffectationMAB (
 AffectationId INT  IDENTITY (1, 1),
    Chantier int NOT NULL,
    Employe int NOT NULL,
    FOREIGN KEY (Chantier) REFERENCES Chantier(ChantierId), 
    FOREIGN KEY (Employe) REFERENCES Employe(EmployeId),
	Mois int,
	Annee int,
    Nombre_heures decimal,
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_affectationMAB CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
	CONSTRAINT check_moonthMAB CHECK (("Mois")>=0 and ("Mois")<=12),
	PRIMARY KEY(Chantier,Employe,Mois,Annee)
);

GO


Create TRIGGER AffectationMAB_Update
ON AffectationMAB
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
    ChiffreAffaireId  INT IDENTITY  (1, 1),
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

Create TRIGGER ChiffreAffaire_Update
ON ChiffreAffaire
AFTER UPDATE
AS
UPDATE
ChiffreAffaire
SET Date_de_modification = GETDATE()
WHERE ChiffreAffaireId IN (SELECT DISTINCT ChiffreAffaireId FROM Inserted);

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

Create view  
ACjoinCE_View 
as 
SELECT 
AffectationChantier.Chantier,AffectationChantier.Employe,
AffectationChantier.Nombre_heures as AC_nb_heures,
AffectationChantier.MoisDebut,
AffectationChantier.AnneeDebut,
AffectationChantier.status as ACStatus,
CoutEmploye.mois,CoutEmploye.annee,
CoutEmploye.mutuelle,CoutEmploye.indemnite_panier,
CoutEmploye.masse_salariale,CoutEmploye.cout_transport,
CoutEmploye.cout_telephone,CoutEmploye.remboursement_prets,
CoutEmploye.saisie_arret,CoutEmploye.nb_heures as CE_nb_heures,
CoutEmploye.status as CEStatus


FROM 
AffectationChantier 
JOIN CoutEmploye
ON
AffectationChantier.Employe=CoutEmploye.Employe




GO

Create view  
ACjoinCEMAB_View 
as 
SELECT 
AffectationMAB.Chantier,AffectationMAB.Employe,
AffectationMAB.Nombre_heures as AC_nb_heures,
AffectationMAB.status as ACStatus,
CoutEmploye.mois,CoutEmploye.annee,
CoutEmploye.mutuelle,CoutEmploye.indemnite_panier,
CoutEmploye.masse_salariale,CoutEmploye.cout_transport,
CoutEmploye.cout_telephone,CoutEmploye.remboursement_prets,
CoutEmploye.saisie_arret,CoutEmploye.nb_heures as CE_nb_heures,
CoutEmploye.status as CEStatus


FROM 
AffectationMAB 
JOIN CoutEmploye
ON
AffectationMAB.Employe=CoutEmploye.Employe
AND
AffectationMAB.Mois=CoutEmploye.Mois
AND
AffectationMAB.Annee=CoutEmploye.Annee

GO
Create table Comission (
 comissionId INT PRIMARY KEY IDENTITY (1, 1),
 comission decimal NOT NULL,
Chantier int NOT NULL,
    FOREIGN KEY (Chantier) REFERENCES Chantier(ChantierId), 
   	"Status" VARCHAR (50) NOT NULL,
	MoisDebut int NOT NULL,
	AnneeDebut int NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_Comission CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
	
	CONSTRAINT check_moonth_Comission CHECK (("MoisDebut")>=0 and ("MoisDebut")<=12),
	 
);
GO





Create table Rentabilite (
 RentabiliteId INT PRIMARY KEY IDENTITY (1, 1),
  	Chantier int NOT NULL,
	Mois int NOT NULL,
	Annee int NOT NULL,
	"Status" VARCHAR (50) NOT NULL,

    FOREIGN KEY (Chantier) REFERENCES Chantier(ChantierId), 
   	CoutsEmploye decimal,
	CoutsLivraison decimal,
	CoutMateriel decimal,
	CoutFournituresSanitaires decimal,
	Comission decimal,
	CoutDeRevient decimal,
	MargeBrut decimal,
	ChiffreAffaire decimal,
	
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 

	CONSTRAINT check_status_Rentabilite CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
	
	CONSTRAINT check_moonth_Rentabilite CHECK (("Mois")>=0 and ("Mois")<=12))
	 
;


GO


Create view
ChantierSalaireCost_view
as
Select ChantierId,v1.employe,v1.mois,v1.annee,Cost,costMAB,(ISNULL(cost, 0 )+ISNULL(costMAB, 0 )) as totalCost from Chantier
left join
(select Chantier,employe,mois,annee,(mutuelle+indemnite_panier+masse_salariale+cout_transport+cout_telephone+remboursement_prets+saisie_arret)*(AC_nb_heures/CE_nb_heures) as Cost from ACjoinCE_View) as v1
On v1.chantier=ChantierId

left join
(select Chantier,employe,mois,annee,(mutuelle+indemnite_panier+masse_salariale+cout_transport+cout_telephone+remboursement_prets+saisie_arret)*(AC_nb_heures/CE_nb_heures) as CostMAB from ACjoinCEMAB_View )as v2
on v1.Chantier=v2.Chantier AND v1.annee=v2.annee AND v1.mois=v2.mois AND v1.employe=v2.employe;