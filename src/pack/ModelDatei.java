package pack;

/**
 * @author BingelJ Implementiert die grundlegende Datenstruktur, die in
 *         ModelAlleDateien und ModleTopDreiDateien verwendet wird. Sie
 *         implementiert typsicher das Comparable-Interface.
 *
 */
public class ModelDatei implements Comparable<ModelDatei> {
	private int nr;
	private String name;
	private String pfad;
	private long groesse;

	/**
	 * Leere Implementierung des Konstruktors, da alles mit Settern hinzugefuegt
	 * wird. Das Hinzufuegen per Parameter sorgt fuer zu lange Zeilen.
	 */
	public ModelDatei() {

	}

	/**
	 * Überschriebene compareTo-Methode, die typsicher zwei ModelDateien anhand
	 * ihrer Groesse in eine Rangfolge ordnet.
	 */
	@Override
	public int compareTo(ModelDatei andereDatei) {
		if (this.groesse < andereDatei.groesse)
			return 1;
		else if (this.groesse == andereDatei.groesse)
			return 0;
		else
			return -1;
	}

	// ########################
	// Getter/ Setter
	// ########################

	public int getNr() {
		return nr;
	}

	public void setNr(int nr) {
		this.nr = nr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPfad() {
		return pfad;
	}

	public void setPfad(String pfad) {
		this.pfad = pfad;
	}

	public long getGroesse() {
		return groesse;
	}

	public void setGroesse(long groesse) {
		this.groesse = groesse;
	}

}
