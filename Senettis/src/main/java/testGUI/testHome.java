package testGUI;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import GUI.Home;


public class testHome {

	
	public static void main(String[] args) {
		
		try {
			DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
			Home home=new Home();
			
		} catch (SQLException | IOException e) {
			System.out.println("erreur au lancement de l'application");
			e.printStackTrace();
			
			Display display = new Display();
			Shell shell = new Shell(display);
			shell.setText("");
			shell.open();
			
			MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. "+'\n'+e.getMessage());
			dialog.open();
		}

	}
	
	
}
