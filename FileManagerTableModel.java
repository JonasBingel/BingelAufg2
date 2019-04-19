package pack;

import javax.swing.table.DefaultTableModel;

/**
 * @author BingelJ Diese Klasse ist abgeleitet vom DefaultTableModel und fuegt
 *         nur die Spalten hinzu, sodass sie der Aufgabenstellung entsprechen.
 *
 */
public class FileManagerTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	/**
	 * Ruft Super-Konstruktor auf und uebergibt Spaltennamen.
	 */
	public FileManagerTableModel() {
		super(new String[] { "Nr", "Dateiname", "Verzeichnis", "Groesse" }, 0);
	}

}
