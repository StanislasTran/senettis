package GUI;

import java.sql.SQLException;

import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import classes.Produit;

public class VueProduit {
	private Composite vueProduit;
	public VueProduit(Composite composite) {
		this.vueProduit=new Composite(composite,SWT.NONE);
		Color couleur = new Color(composite.getDisplay(), 131, 133, 131);
		vueProduit.setBackground(couleur);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		vueProduit.setLayout(fillLayout);

		Composite compositeNom = new Composite(vueProduit, SWT.NONE);
		compositeNom.setBackground(couleur);

		Label labelNom = new Label(compositeNom, SWT.NONE);
		labelNom.setBackground(couleur);
		labelNom.setText("Nom");
		labelNom.setBounds(10, 10, 100, 25);

		final Text textNom = new Text(compositeNom, SWT.BORDER);
		textNom.setText("");
		textNom.setBounds(10, 30, 100, 25);

		Composite compositePrix = new Composite(vueProduit, SWT.BACKGROUND);
		compositePrix.setBackground(couleur);

		Label labelPrix = new Label(compositePrix, SWT.NONE);
		labelPrix.setBackground(couleur);
		labelPrix.setText("Prix");
		labelPrix.setBounds(10, 10, 20, 25);

		final Text textPrix = new Text(compositePrix, SWT.BORDER);
		textPrix.setText("");
		textPrix.setBounds(10, 30, 30, 25);

		Composite compositeCommentaire = new Composite(vueProduit, SWT.BACKGROUND);
		compositeCommentaire.setBackground(couleur);

		Label labelCommentaire = new Label(compositeCommentaire, SWT.NONE);
		labelCommentaire.setBackground(couleur);
		labelCommentaire.setText("Commentaires");
		labelCommentaire.setBounds(10, 10, 100, 25);

		final Text textCommentaire = new Text(compositeCommentaire, SWT.CENTER);

		textCommentaire.setText("");
		textCommentaire.setBounds(10, 30, 100, 25);

		Composite compositeValidation = new Composite(vueProduit, SWT.CENTER);
		compositeValidation.setBackground(couleur);
		Button button = new Button(compositeValidation, SWT.BACKGROUND);
		button.setText("Valider");
		button.setBounds(10, 60, 100, 25);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				Produit produit = new Produit(textNom.getText(), Double.parseDouble(textPrix.getText()),
						textCommentaire.getText(), "Publié");

				try {
					produit.insertDatabase();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("done");

			}

		});

		vueProduit.setSize(500, 500);
		labelNom.pack();
		labelPrix.pack();
		labelCommentaire.pack();

		

	
	}
	
	public Composite getComposite() {
		return this.vueProduit;
	}
}
