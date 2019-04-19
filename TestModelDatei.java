package pack;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class TestModelDatei {

	private String path;
	private ArrayList<ModelDatei> topDrei; // TODO da list muss sort funktionieren!

	public TestModelDatei(String path) {
		this.path = path;
		topDrei = new ArrayList<>();
		ModelDatei dummy = new ModelDatei();
		dummy.setGroesse(0);
		dummy.setName("dummy");
		topDrei.add(dummy);
		for (int i = 0; i < 3; i++) {
			topDrei.add(dummy);
		}
	}

	public void testeFileWalk() throws Exception {
		Files.walkFileTree(Paths.get(this.path), new FileVisitor<Path>() {

			private int fortlaufendeNr = 1;
			private boolean braucheUpdate = false;

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				// noch kuerzer jedoch muss man immer aktualisieren, selbst wenn es keine
				// Änderung gibt
//				topDrei.add(3, neueDatei);
//				topDrei.sort(null);

				this.braucheUpdate = true;
				ModelDatei neueDatei = new ModelDatei();
				neueDatei.setNr(this.fortlaufendeNr++);
				neueDatei.setName(file.getFileName().toString());
				neueDatei.setPfad(file.getParent().toString());
				neueDatei.setGroesse(attrs.size());

				if (neueDatei.getGroesse() > topDrei.get(0).getGroesse()) {
					topDrei.add(2, topDrei.get(1));
					topDrei.add(1, topDrei.get(0));
					topDrei.add(0, neueDatei);
				} else if (neueDatei.getGroesse() > topDrei.get(1).getGroesse()) {
					topDrei.add(2, topDrei.get(1));
					topDrei.add(1, neueDatei);
				} else if (neueDatei.getGroesse() > topDrei.get(2).getGroesse()) {
					topDrei.add(2, neueDatei);
				} else {
					braucheUpdate = false;
				}

				if (braucheUpdate == true) {
					while (topDrei.size() > 3) {
						topDrei.remove(3);
					}
					//publish(topDrei);
				}

				// durchlaufen mit for each und dann dreieckstausch und liste anpassen
				//

				// if update == true -> dann publish arraylist

				return FileVisitResult.CONTINUE;

				// check if file should be inside of list
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
				// don't know if necessary
			}

		});

	}

	public ArrayList<ModelDatei> getTopDrei() {
		return topDrei;
	}

}
