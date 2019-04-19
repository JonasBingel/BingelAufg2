package pack;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.util.HashMap;
import java.nio.file.attribute.*;

/**
 * @author BingelJ ModellAlleDateien ist abgeleitet vom typsicheren
 *         SimpleFileVisitor und bildet das erste Model des MVP-Entwurfmusters.
 *         Alle Dateien unter einem bestimmten Pfad werden in einer HashMap
 *         verwaltet.
 *
 */
public class ModelAlleDateien extends SimpleFileVisitor<Path> {
	private int fortlaufendeNr;
	private String path;
	private HashMap<Integer, ModelDatei> alleDateien;

	/**
	 * Konstruktor instanziiert die HashMap<>, sodass sie befuellt werden kann.
	 */
	public ModelAlleDateien() {
		alleDateien = new HashMap<>();
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
		} else {
			return false;
		}
	}

	/**
	 * Aufruf der Files.WalkFileTree-Methode. Zuvor wird die HashMap noch geleert
	 * und die fortlaufendeNr auf 1 gesetzt, sodass ein mehrmaliges Aufrufen keine
	 * FolgeFehler oder vorige Elemente enthaelt.
	 * 
	 * @return HashMap<Integer, ModelDatei> die alle ModelDateien enthaelt
	 */
	public HashMap<Integer, ModelDatei> walkFiles() {
		this.alleDateien.clear();
		try {
			this.fortlaufendeNr = 1;
			Files.walkFileTree(Paths.get(this.path), this);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return alleDateien;
	}

	/**
	 * (non-Javadoc) Ueberschriebene vistiFile-methode, die die
	 * Informationen/Eigenschaften jeder einzelnen Datei in eine ModelDatei
	 * schreibt, die der ArrayList hinzugefuegt wird.
	 * 
	 * @see java.nio.file.SimpleFileVisitor#visitFile(java.lang.Object,
	 *      java.nio.file.attribute.BasicFileAttributes)
	 */
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

		ModelDatei neueDatei = new ModelDatei();
		neueDatei.setNr(this.fortlaufendeNr++);
		neueDatei.setName(file.getFileName().toString());
		neueDatei.setPfad(file.getParent().toString());
		neueDatei.setGroesse(attrs.size());

		alleDateien.put(neueDatei.getNr(), neueDatei);
		return FileVisitResult.CONTINUE;

	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path arg0, IOException arg1) throws IOException {
		return FileVisitResult.SKIP_SUBTREE;
	}

}
