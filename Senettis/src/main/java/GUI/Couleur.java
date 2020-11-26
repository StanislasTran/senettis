package GUI;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class Couleur {

	private static  Display displ;
	public final static Color bleuClair = new Color(displ, 213, 234, 253);
	public final static Color lightCyan = new Color(displ, 204, 255, 255);
	public final static Color bleuFonce = new Color(displ, 1, 88, 144);
	public final static Color gris = new Color(displ, 240, 240, 240);
	public final static Color PeterRiver=new Color(displ,52,152,219);
	public final static Color blanc = new Color(displ, 254, 254, 254);
	public final static Color rouge = new Color(displ, 254, 0, 0);
	public final static Color noir = new Color(displ, 0, 0, 0);
	
	public static void setDisplay(Display display) {
		displ=display;
	}

}
