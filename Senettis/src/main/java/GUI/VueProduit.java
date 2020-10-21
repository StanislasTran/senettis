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

	private Display display;;
	private Shell shell;
	private Label label;

	public VueProduit() {
		this.display = new Display();
		this.shell = new Shell(display);
		this.shell.setText("Senettis App");

		Composite compositeMain = new Composite(shell, SWT.BACKGROUND);
		Color couleur = new Color(display, 131, 133, 131);
		compositeMain.setBackground(couleur);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		compositeMain.setLayout(fillLayout);

		Composite compositeNom = new Composite(compositeMain, SWT.NONE);
		compositeNom.setBackground(couleur);

		Label labelNom = new Label(compositeNom, SWT.NONE);
		labelNom.setBackground(couleur);
		labelNom.setText("Nom");
		labelNom.setBounds(10, 10, 100, 25);

		final Text textNom = new Text(compositeNom, SWT.BORDER);
		textNom.setText("");
		textNom.setBounds(10, 30, 100, 25);

		Composite compositePrix = new Composite(compositeMain, SWT.BACKGROUND);
		compositePrix.setBackground(couleur);

		Label labelPrix = new Label(compositePrix, SWT.NONE);
		labelPrix.setBackground(couleur);
		labelPrix.setText("Prix");
		labelPrix.setBounds(10, 10, 20, 25);

		final Text textPrix = new Text(compositePrix, SWT.BORDER);
		textPrix.setText("");
		textPrix.setBounds(10, 30, 30, 25);

		Composite compositeCommentaire = new Composite(compositeMain, SWT.BACKGROUND);
		compositeCommentaire.setBackground(couleur);

		Label labelCommentaire = new Label(compositeCommentaire, SWT.NONE);
		labelCommentaire.setBackground(couleur);
		labelCommentaire.setText("Commentaires");
		labelCommentaire.setBounds(10, 10, 100, 25);

		final Text textCommentaire = new Text(compositeCommentaire, SWT.CENTER);

		textCommentaire.setText("");
		textCommentaire.setBounds(10, 30, 100, 25);

		Composite compositeValidation = new Composite(compositeMain, SWT.CENTER);
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

		compositeMain.setSize(500, 500);
		labelNom.pack();
		labelPrix.pack();
		labelCommentaire.pack();

		this.shell.pack();
		this.shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
		labelNom.dispose();
	}
}
