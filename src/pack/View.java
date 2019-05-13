package pack;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.*;

/**
 * @author BingelJ View ist abgeleitet von JFrame und bildet die View des
 *         MVP-Entwurfmusters. Wie in MainPresenter beschrieben wurde eine
 *         passive View-Implementierung gewaehlt - d.h. View veraendert sich
 *         nicht eigenstaendig. Um diese moeglichste loose Kopplung zu
 *         ermoeglichen besitzt View einen Observer, der ueber die Aktionen
 *         informiert wird. Die Implementierung erfolgt dann in in den
 *         entsprechend aufgerufen Methoden - siehe MainPresenter.
 *
 */
public class View extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField tf;
	private JButton topDreiButton;
	private JPanel cards;
	private FileManagerTableModel tableModel;
	private ViewObserver observer;

	/**
	 * @author BingelJ ViewObserver informiert den Observer (hier Presenter) ueber
	 *         die entsprechenden Aktionen anhand von zwei Methoden.
	 */
	public interface ViewObserver {
		/**
		 * TopDreiEvent informiert den Observer, dass der TopDreiButton betaetigt wurde.
		 */
		public void TopDreiEvent();

		/**
		 * TextFieldEvent informiert den Observer, dass eine Eingabebestaetigung im
		 * Textfield erfolgt ist.
		 */
		public void TextFieldEvent();
	}

	public View() {

		// ########################
		// View Implementierung
		// ########################

		super("Dateimanager");
		tableModel = new FileManagerTableModel();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container cont = getContentPane();
		cont.setLayout(new BorderLayout());

		JPanel upperWindow = new JPanel();
		JPanel lowerWindow = new JPanel();

		// Upper Window Components

		/**
		 * Setzen von GridBagLayout und erstellen der notwendigen Constraints
		 * 
		 * @see https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html
		 */
		upperWindow.setLayout(new GridBagLayout());
		GridBagConstraints tfConstraint = new GridBagConstraints();
		GridBagConstraints btConstraint = new GridBagConstraints();
		GridBagConstraints lbConstraint = new GridBagConstraints();
		GridBagConstraints pbConstraint = new GridBagConstraints();

		// JLabel
		JLabel label = new JLabel("Such-Verzeichnis fuer Dateien: ");
		label.setHorizontalAlignment(SwingConstants.RIGHT);

		lbConstraint.gridx = 0;
		lbConstraint.gridy = 0;
		lbConstraint.gridwidth = 1;
		lbConstraint.gridheight = 1;
		lbConstraint.fill = GridBagConstraints.BOTH;
		lbConstraint.insets = new Insets(15, 0, 0, 10);
		lbConstraint.weightx = 0.25;

		// Textfield

		this.tf = new JTextField("C:/Java");
		tfConstraint.gridx = 1;
		tfConstraint.gridy = 0;
		tfConstraint.gridwidth = 1;
		tfConstraint.gridheight = 1;
		tfConstraint.fill = GridBagConstraints.BOTH;
		tfConstraint.insets = new Insets(25, 0, 0, 15);
		tfConstraint.weightx = 0.75;

		// JButton
		topDreiButton = new JButton("Top 3");
		topDreiButton.setSize(cont.getWidth() / 2, 15);
		btConstraint.gridx = 1;
		btConstraint.gridy = 1;
		btConstraint.gridwidth = 1;
		btConstraint.gridheight = 1;
		btConstraint.fill = GridBagConstraints.BOTH;
		btConstraint.weightx = 0.75;
		btConstraint.insets = new Insets(5, 0, 5, 15);

		// Progress Bar
		JProgressBar progressBar = new JProgressBar(0, 20);
		progressBar.setIndeterminate(true);
		progressBar.setString("läuft und läuft");
		progressBar.setStringPainted(true);

		pbConstraint.gridx = 1;
		pbConstraint.gridy = 2;
		pbConstraint.gridwidth = 1;
		pbConstraint.gridheight = 1;
		pbConstraint.fill = GridBagConstraints.BOTH;
		pbConstraint.weightx = 0.75;
		pbConstraint.insets = new Insets(0, 0, 15, 15);
		pbConstraint.anchor = GridBagConstraints.PAGE_END;

		/**
		 * Um die Progressbar zu verstecken ohne, dass das GridBagLayout collapsed, muss
		 * ein CardLayout verwendet werden, das zwischen einem leeren Platzhalter-Label
		 * und der Progress-Bar wechseln kann.
		 * 
		 * @see https://docs.oracle.com/javase/tutorial/uiswing/layout/card.html
		 */

		// Hinzufuegen der ProgressBar-Panel
		this.cards = new JPanel(new CardLayout());
		JPanel progressBarPane = new JPanel(new GridLayout(1, 1));
		progressBarPane.add(progressBar);
		cards.add(progressBarPane, ProgressBarCards.PROGRESSBAR.toString());

		// Hinzufuegen der Place-Holder Panel
		JPanel emptyPane = new JPanel(new GridLayout(1, 1));
		emptyPane.add(new JLabel(""));
		cards.add(emptyPane, ProgressBarCards.EMPTY.toString());

		// Hinzufuegen der Komponenten zum oberen Fenster
		upperWindow.add(label, lbConstraint);
		upperWindow.add(tf, tfConstraint);
		upperWindow.add(topDreiButton, btConstraint);
		upperWindow.add(cards, pbConstraint);
		this.zeigeCard(ProgressBarCards.EMPTY);

		cont.add(upperWindow, BorderLayout.NORTH);

		// ############################
		// Action-Listener
		// ############################

		this.tf.addActionListener(new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				observer.TextFieldEvent();
			}

		});

		this.topDreiButton.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				observer.TopDreiEvent();
			}

		});

		// ############################
		// Lower Window/ Table
		// ############################

		JTable table = new JTable(tableModel);

		// TABLE ROW SORTER

		// TableSorter implementiert die einzelnen Comparator, die in den entsprechenden
		// Spalten verwendet werden sollen.

		final TableRowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>(tableModel);

		rowSorter.setComparator(0, new Comparator<String>() {

			@Override
			public int compare(String s1, String s2) {
				int int1 = Integer.parseInt(s1);
				int int2 = Integer.parseInt(s2);
				if (int1 < int2)
					return 1;
				else if (int1 > int2)
					return -1;
				return 0;
			}
		});

		rowSorter.setComparator(3, new Comparator<String>() {

			@Override
			public int compare(String s1, String s2) {
				int int1 = Integer.parseInt(s1);
				int int2 = Integer.parseInt(s2);
				if (int1 < int2)
					return 1;
				else if (int1 > int2)
					return -1;
				return 0;
			}
		});

		table.setRowSorter(rowSorter);
		lowerWindow.add(new JScrollPane(table));
		cont.add(lowerWindow, BorderLayout.CENTER);

		// Breite der Spalten wird hier festgelegt. Die Spalten sind im TableModel
		// hardcodiert, da dies jedoch erweitert werden kann, werden die Spalten
		// dynamisch anhand von findcolumn ermittelt.
		int spaltenIndexGroesse = tableModel.findColumn("Groesse");
		int spaltenIndexNr = tableModel.findColumn("Nr");

		table.getColumnModel().getColumn(spaltenIndexNr).setPreferredWidth(10);
		table.getColumnModel().getColumn(spaltenIndexNr).setResizable(false);
		table.getColumnModel().getColumn(spaltenIndexNr).setPreferredWidth(75);
		table.getColumnModel().getColumn(tableModel.findColumn("Dateiname")).setPreferredWidth(200);
		table.getColumnModel().getColumn(tableModel.findColumn("Verzeichnis")).setPreferredWidth(200);
		table.getColumnModel().getColumn(spaltenIndexGroesse).setPreferredWidth(100);

		table.getColumnModel().getColumn(spaltenIndexGroesse).setCellRenderer(new DataSizeRenderer());

		this.pack();
		this.setVisible(true);

	}

	/**
	 * Sorgt fuer einen Wechsel der Card im CardLayout, d.h. ob ein Leeres Label
	 * oder die Progressbar angezeigt wird. Ein CardLayout muss hierfuer verwendet
	 * werden, da durch ein einfaches Veraendern durch setInvisible das Layout zum
	 * Collapsen bringt.
	 * 
	 * @param wantedCard Wert des Enum ProgressBarCards, das einer bestimmten Card
	 *                   entspricht.
	 */
	public void zeigeCard(ProgressBarCards wantedCard) {
		CardLayout cl = (CardLayout) (this.cards.getLayout());
		cl.show(cards, wantedCard.toString());
	}

	// ############################
	// Getter/Setter
	// ############################

	public JTextField getTf() {
		return this.tf;
	}

	public JButton getTopDreiButton() {
		return this.topDreiButton;
	}

	public void setTableModel(FileManagerTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public FileManagerTableModel getTableModel() {
		return tableModel;
	}

	public void setObserver(ViewObserver observer) {
		this.observer = observer;
	}

}
