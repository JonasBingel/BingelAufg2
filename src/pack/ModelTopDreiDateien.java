package pack;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import javax.swing.SwingWorker;

//  

// 
/**
 * @author BingelJ ModelTopDreiDateien ist abgeleitet von SwingWorker und bildet
 *         das zweite Model des MVP-Entwurfmusters. Nach Aufruf durch .execute
 *         wird ein separater background-Thread geöffnet, der alle Dateien unter
 *         einem Pfad durchlaueft und die drei aktuell groessten Dateien
 *         published/auf der GUI darstellen laesst.
 * 
 *         Normalerweise sollte eine Trennung von Model und SwingWorker erfolgen
 *         bzw. SwingWorker im Presenter unterkommen, da Model nur die
 *         Datenstrukturen bildet, da es sich jedoch um eine rekursive Aufgabe
 *         handelt, die wiederholt ArrayLists/Zwischenergebnisse published ist
 *         eine Verheiratung in Ordnung und war darueber hinaus von der
 *         Aufgabenstellung gewuenscht.
 * 
 * @see https://stackoverflow.com/questions/15619019/where-should-i-invoke-my-swingworker-in-a-java-mvc-pattern
 * 
 *      Um eine loose Entkopplung von Model und Presenter (wie vom MVP
 *      gewuenscht) zu ermoeglichen, erfolgt die Benachrichtigung des Presenters
 *      durch einen Observer hier: TopDreiObserver.
 * 
 * @see https://stackoverflow.com/questions/5533497/mvc-progress-bar-threading
 */
public class ModelTopDreiDateien extends SwingWorker<Void, ArrayList<ModelDatei>> {
	private String path;
	private ArrayList<ModelDatei> topDrei;
	private TopDreiObserver observer;

	/**
	 * @author BingelJ Das Interface TopDreiObserver enthaelt zwei Methoden, analog
	 *         zum Swingworker vgl. process() und done(), um dem Observer von dem
	 *         Auftreten zu berichten.
	 *
	 */
	public interface TopDreiObserver {
		/**
		 * Uebergabe ist eine typsichere ArrayList, die gepublished werden soll. Der
		 * Observer wird also benachrichtigt, dass es etwas neues zu Publishen gibt.
		 * 
		 * @param arrayList typsichere ArrayList, die gepublished werden soll.
		 */
		public void neueTopDreiGefunden(ArrayList<ModelDatei> arrayList);

		/**
		 * Informiert Observer, dass die done()-Methode von SwingWorker aufgerufen
		 * wurde.
		 */
		public void swingWorkerBeendet();
	}

	/**
	 * Im Konstruktor wird die ArrayList instanziiert.
	 */
	public ModelTopDreiDateien() {
		topDrei = new ArrayList<>();

	}

	/**
	 * Prueft den uebergebenen Pfad und setzt ihn, wenn er existiert als Attribut
	 * und returned true, sonst false.
	 * 
	 * @param path Pfad, der geprueft und gesetzt werden soll.
	 * @return Boolean, ob der Pfad existiert.
	 */
	public boolean pruefeUndSetzePfad(String path) {
		if (Files.exists(Paths.get(path))) {
			this.path = path;
			return true;
		}
		return false;

	}

	/*
	 * (non-Javadoc) Ueberschrieben doInBackground() Methode. Zunachest wird die
	 * ArrayList geleert und mit dummy-Werten initalisiert. Dann wird ein
	 * FileVisitor gestartet als innere Klasse.
	 * 
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Void doInBackground() throws Exception {

		// Die Arrayliste wird geleert, sodass die Suche auch ohne Folgedehler mehrmals
		// verwendet werden kann.
		topDrei.clear();

		// Fuellen der ArrayList mit drei Dummy-Dateien für den Vergleich; eigentlich
		// auch mit Null möglich
		ModelDatei dummy = new ModelDatei();
		dummy.setGroesse(0);
		for (int i = 0; i < 3; i++) {
			topDrei.add(dummy);
		}

		Files.walkFileTree(Paths.get(this.path), new FileVisitor<Path>() {

			private int fortlaufendeNr = 1;
			private boolean braucheUpdate;

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.nio.file.FileVisitor#visitFile(java.lang.Object,
			 * java.nio.file.attribute.BasicFileAttributes)
			 */
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

				// kleine Verzoegerung wird eingebaut, wie gefordert von Aufgabenstellung
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				this.braucheUpdate = true;
				ModelDatei neueDatei = new ModelDatei();
				neueDatei.setNr(this.fortlaufendeNr++);
				neueDatei.setName(file.getFileName().toString());
				neueDatei.setPfad(file.getParent().toString());
				neueDatei.setGroesse(attrs.size());

				long neueDateiGroesse = neueDatei.getGroesse();

				// Vergleicht aktuelle Datei mit ModelDateien in ArrayList und fuegt sie
				// entsprechend ein.
				if (neueDatei.getGroesse() > topDrei.get(0).getGroesse()) {
					topDrei.set(2, topDrei.get(1));
					topDrei.set(1, topDrei.get(0));
					topDrei.set(0, neueDatei);
				} else if (neueDateiGroesse > topDrei.get(1).getGroesse()) {
					topDrei.set(2, topDrei.get(1));
					topDrei.set(1, neueDatei);
				} else if (neueDateiGroesse > topDrei.get(2).getGroesse()) {
					topDrei.set(2, neueDatei);
				} else {
					braucheUpdate = false;
				}

				/**
				 * Typcast ist hier unproblematisch, da wir topDrei.clone verwenden und topDrei
				 * typsicher ist. Eine Kopie wird verwenet, da der Zustand beim Tauschen
				 * eventuell nicht konsistent bleibt.
				 */
				if (braucheUpdate) {
					System.out.println("Neues Publish!");
					publish((ArrayList<ModelDatei>) topDrei.clone());
				}

				return FileVisitResult.CONTINUE;

			}

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				return FileVisitResult.SKIP_SUBTREE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {

				return FileVisitResult.CONTINUE;
			}

		});

		return null;

	}

	/*
	 * (non-Javadoc) Informiert Observer und uebergibt ihm die aktuellsten drei
	 * groessten Dateien.
	 * 
	 * @see javax.swing.SwingWorker#process(java.util.List)
	 */
	@Override
	protected void process(List<ArrayList<ModelDatei>> kopieTopDreiListe) {
		System.out.println("Process aufgerufen!");
		observer.neueTopDreiGefunden(kopieTopDreiListe.get(kopieTopDreiListe.size() - 1));
	}

	/*
	 * (non-Javadoc) Informiert Observer, dass DOne aufgerufen wurde. und printet
	 * eine Information auf der Konsole auf - fuer die leichtere Nachverfolgung bei
	 * der Bewertung.
	 * 
	 * @see javax.swing.SwingWorker#done()
	 */
	@Override
	protected void done() {
		observer.swingWorkerBeendet();
		System.out.println("Swing Worker done!");
	}

	/**
	 * Prueft, ob der Zustand von Swingworker Done ist, d.h. SwingWorker wurde
	 * verbraucht.
	 * 
	 * @return
	 */
	public boolean istSwingWorkerVerbraucht() {
		return this.getState().equals(SwingWorker.StateValue.DONE);
	}

	public void setObserver(TopDreiObserver observer) {
		this.observer = observer;
	}

	// BEMERKUNG:
	// noch kuerzer waere vermutlich ein Sortieren in der process-Methode;
	// hier aus didaktischen Gründen und aufgrund der Aufgabenstellung anders
	// implementiert.
	// Beispielhaft hier dargestellt

	// im FileVisitor nur folgendes prüfen:
//	if (neueDateiGroesse > topDrei.get(2).getGroesse()) {
//		publish(neueDatei)
//	}

	// In der process-Methode
//	kopieTopDreiListe.sort(null);
//	while (topDrei.size() > 3)
//		topDrei.remove(3)
//
}
