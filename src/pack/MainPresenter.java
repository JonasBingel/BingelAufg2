package pack;

import java.awt.*;
import java.util.*;

import pack.ModelTopDreiDateien.TopDreiObserver;
import pack.View.ViewObserver;

/**
 * @author BingelJ Abgabeaufgabe SS2019 Programmieren 2 Herr Mehler
 *         MainPresenter ist Teil der Model-View-Presenter-Entwurfmusters und
 *         implementiert die Interfaces TopDreiObsever, ViewObserver. Nach dem
 *         MVP besteht keine Kommunikation zwischen View und Model, da Presenter
 *         den Vermittler bildet. Die gewaehlte Implementierung ist eine passive
 *         View, d.h. sie bekommt nur Table-Model ueberreicht und passt keine
 *         Komponenten an.
 * 
 *         Alle Anforderungen der Abgabeaufgabe wurden vollstaending erfuellt.
 *         Darüber hinaus wurde das MVP-Pattern strengst moeglich eingehalten
 *         (lose Kopplung durch Observer) und weitere Zusatzfunktionen
 *         implementiert. Das TextField prueft mit Enter den Pfad und kann
 *         dadurch erneut ModelAlleDateien aufrufen. Der TopDrei-Button kann
 *         mehrmals betaetigt werden und die GUI wurde robuster gestaltet. *
 *
 */
public class MainPresenter implements TopDreiObserver, ViewObserver {
	private View view;
	private ModelAlleDateien modelAlleDateien;
	private ModelTopDreiDateien modelDreiDateien;
	FileManagerTableModel tableModel = new FileManagerTableModel();

	/**
	 * @param view                View des MVP Entwurfmusters. Passive View, die
	 *                            zuerst erstellt wird und im Konstruktor mit einem
	 *                            entsprechenden Observer (this) versehen wird.
	 * @param modelAlleDateien    Ein Model des MVP Entwurfmusters. Es verwaltet
	 *                            eine HashMap aller Dateien unter einem gegebenen
	 *                            Pfad.
	 * @param modelTopDreiDateien Zweites Model des MVP Entwurfmusters. Es verwaltet
	 *                            eine typsichere ArrayList der drei groessten
	 *                            Dateien unter einem gegebenen Pfad.
	 */
	public MainPresenter(View view, ModelAlleDateien modelAlleDateien, ModelTopDreiDateien modelTopDreiDateien) {

		this.view = view;
		this.modelAlleDateien = modelAlleDateien;
		this.modelDreiDateien = modelTopDreiDateien;
		this.tableModel = view.getTableModel();

		// Setzen der Observer
		this.view.setObserver(this);
		this.modelDreiDateien.setObserver(this);

		// Initiales Fuellen des TableModels
		if (this.modelAlleDateien.pruefeUndSetzePfad(view.getTf().getText())) {
			// this.modelAlleDateien.walkFiles();
			this.fillTableModel(this.modelAlleDateien.walkFiles());
		} else {
			// do nothing, d.h. zunaechst leere Tabelle falls Pfad nicht gegeben sein
			// sollte.
		}

	}

	// ########################
	// View-MainPresenter Kommunikation
	// Da eine passive View gewählt wurde, liegt die Kommunikation/Befuellen &
	// Leeren des TableModels im Main-Presenter. Fuer aktive View, muss eine
	// Verlagerung in View.java erfolgen.
	// ########################

	/**
	 * Loescht alle Eintraege im TableModel durch wiederholtes aufrufen der
	 * removeRow-Methode.
	 */
	public void deleteTableEntries() {
		if (tableModel.getRowCount() > 0) {
			for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
				tableModel.removeRow(i);
			}
		}
	}

	/**
	 * Eine moegliche Implementierung von fillTableModel nimmt eine typsichere
	 * HashMap entgegen und befuellt das TableModel mit Vektoren. Vor dem Befuellen
	 * wird das TableModel zunaechst geleert.
	 * 
	 * @param dateien typsichere HashMap.
	 */
	public void fillTableModel(HashMap<Integer, ModelDatei> dateien) {
		this.deleteTableEntries();
		for (int key : dateien.keySet()) {
			Vector<String> vector = new Vector<>();
			vector.add(Integer.toString(dateien.get(key).getNr()));
			vector.add(dateien.get(key).getName());
			vector.add(dateien.get(key).getPfad());
			vector.add(Long.toString(dateien.get(key).getGroesse()));
			tableModel.addRow(vector);
		}
	}

	/**
	 * Implementierung und Funktion aehnlich wie {@link #fillTableModel(HashMap)}
	 * aber nimmt als Uebergabeparameter eine typsichere ArrayList.
	 * 
	 * @param topDrei typsichere ArrayList.
	 */
	public void fillTableModel(ArrayList<ModelDatei> topDrei) {
		this.deleteTableEntries();
		try {
			for (int i = 0; i < topDrei.size(); i++) {
				Vector<String> vector = new Vector<>();
				vector.add(Integer.toString(topDrei.get(i).getNr()));
				vector.add(topDrei.get(i).getName());
				vector.add(topDrei.get(i).getPfad());
				vector.add(Long.toString(topDrei.get(i).getGroesse()));
				tableModel.addRow(vector);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ########################
	// Main-Presenter Implementierung
	// ########################

	public void setModelAlleDateien(ModelAlleDateien modelAlleDateien) {
		this.modelAlleDateien = modelAlleDateien;
	}

	public ModelAlleDateien getModelAlleDateien() {
		return this.modelAlleDateien;
	}

	public void setModelDreiDateien(ModelTopDreiDateien modelDreiDateien) {
		this.modelDreiDateien = modelDreiDateien;
	}

	public void setView(View view) {
		this.view = view;
	}

	// ########################
	// ViewObserver-Interface
	// ########################

	/**
	 * Implementierung der TopDreiEvent-Methode des Interfaces
	 * {@link ViewObserver#TopDreiEvent()} Da eine passive View gewaehlt wurde,
	 * liegt die eigentliche Implementierung hier und nicht in der View. Sie ruft
	 * den SwingWorker auf und veraendert die GUI-Elemente entsprechend, um sie
	 * moeglichst robust zu gestalten.
	 */
	@Override
	public void TopDreiEvent() {

		// Bemerkung: Da Swing-Worker nur einmal verwendet werden kann bevor er
		// "verbraucht" ist, muss man neue SwingWorker Objekte erzeugen, wenn ein neuer
		// Durchlauf möglich sein soll.
		// Zur Vollständigkeit wird dies hier implementiert anhand einer
		// Fallunterscheidung des Swingworker-States

		if (this.modelDreiDateien.istSwingWorkerVerbraucht()) {
			// Neuer SwingWoker wird gesetzt, da alter verbraucht ist
			this.modelDreiDateien = new ModelTopDreiDateien();
			this.modelDreiDateien.setObserver(this);

		} else {
			// do nothing, da SwingWorker noch nicht verwendet wurde
		}

		/**
		 * Zunaechst wird der eingetragene Pfad geprueft und gesetzt. Bei Gueltigkeit
		 * wird der SwingWorker gestartet und die Progressbar angezeigt sowie andere GUI
		 * Elemente deaktiviert. Bei Misserfolg wird die Farbe des Textfelds auf Rot
		 * geaendert.
		 */
		if (this.modelDreiDateien.pruefeUndSetzePfad(this.view.getTf().getText())) {
			this.view.getTf().setEnabled(false);
			this.view.getTf().setBackground(Color.WHITE);
			this.view.getTopDreiButton().setEnabled(false);
			this.view.zeigeCard(ProgressBarCards.PROGRESSBAR);
			this.modelDreiDateien.execute();
			this.deleteTableEntries();
		} else {
			this.view.getTf().setBackground(Color.RED);
		}

	}

	/**
	 * Implementierung der Interface-Methode {@link ViewObserver#TextFieldEvent()}
	 * Bei Bestaetigung des TF Eintrags mittels Enter wird der Pfad geprueft und
	 * gesetzt. Anschließend die Hintergrundfarbe auf Weiss oder Rot gesetzt und bei
	 * existentem Pfad alle Dateien unter dem gegebenen Pfad gesammelt und ins
	 * TableModel gefuellt.
	 * 
	 */
	@Override
	public void TextFieldEvent() {

		if (this.modelAlleDateien.pruefeUndSetzePfad(view.getTf().getText())) {
			view.getTf().setBackground(Color.WHITE);
			this.fillTableModel(this.modelAlleDateien.walkFiles());
		} else {
			view.getTf().setBackground(Color.RED);
		}

	}

	// ########################
	// TopDreiObserver-Interface Implementierung
	// ########################

	/**
	 * Implementierung der Inferface-Methode
	 * {@link TopDreiObserver#neueTopDreiGefunden(ArrayList)}. Diese Methode wird
	 * aus der Process-Methode des SwingWorkers aufgerufen und sorgt fuer eine
	 * Aktualisierung des TableModels.
	 * 
	 * @param arrayList typsichere ArrayList der drei aktuell groessten Dateien, die
	 *                  nun in das TableModel gefuellt werden sollen.
	 * 
	 */
	@Override
	public void neueTopDreiGefunden(ArrayList<ModelDatei> arrayList) {
		this.fillTableModel(arrayList);
	}

	/**
	 * Implementierung der Interface-Methode
	 * {@link TopDreiObserver#swingWorkerBeendet()}. Diese Methode wird aus der
	 * done-Methode des SwingWorkers aufgerufen. Die GUI-Elemente werden wieder
	 * aktiviert und die ProgressBar wird mittels CardLayout versteckt.
	 */
	@Override
	public void swingWorkerBeendet() {
		this.view.zeigeCard(ProgressBarCards.EMPTY);
		this.view.getTopDreiButton().setEnabled(true);
		this.view.getTf().setEnabled(true);
	}

	/**
	 * Erzeugt zunaechst die entsprechenden View und Model(alle sowie TopDrei)
	 * Komponenten. Diese werden dem MainPresenter-Konstruktor uebergeben.
	 * 
	 * @param args Wird nicht weiter verwendet.
	 */
	public static void main(String[] args) {

		View view = new View();
		ModelAlleDateien modelAlleDateien = new ModelAlleDateien();
		ModelTopDreiDateien modelTopDrei = new ModelTopDreiDateien();
		@SuppressWarnings("unused")
		MainPresenter presenter = new MainPresenter(view, modelAlleDateien, modelTopDrei);
	}
}
