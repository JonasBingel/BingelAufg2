package pack;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author BingelJ DataSizeRenderer ist abgeleitet vom DefaultTableCellRenderer
 *         und positioniert die gewaehlten Spaltendaten rechtsbuending und
 *         formatiert die DateiGroesse in KiloByte.
 */
public class DataSizeRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	/**
	 * Aufruf des Super-Konstruktors und rechtsbündige Darstellung.
	 */
	public DataSizeRenderer() {
		super();
		setHorizontalAlignment(SwingConstants.RIGHT);
	}

	/*
	 * (non-Javadoc) Ueberschriebene setValue-Methode, die den entsprechenden Wert
	 * der Spalte durch 1000 dividiert und Tausendertrennzeichen sowie kB einfuegt.
	 * Hinweis: Nach der neuen ISO-Definition ist ein KiloByte 1000 Byte, ein
	 * KibiByte 1024 Byte, folglich Division durch 1000.
	 * 
	 * @see javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object value) {

		String s1 = value.toString();

		try {
			Long fileSizeKB = Long.parseLong(s1) / 1000;
			super.setValue(String.format("%,d", fileSizeKB) + " kB");

		} catch (NumberFormatException e) {
			System.out.println("Mindestens eine Spalte enthaelt keine Zahl. Kennzeichnung mit 'ERR' erfolgt.");
			e.printStackTrace();
			super.setValue("ERR");
		}

	}

}
