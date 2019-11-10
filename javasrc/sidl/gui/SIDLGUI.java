package sidl.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import sidl.core.Base;
import sidl.core.SIDL;
import sidl.utils.JHPL;
import sidl.utils.PP;
import sidl.utils.SIDLLegals;

public class SIDLGUI extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1499776158346596886L;

	String filename = "";

	JButton load = new JButton("Load File"), next = new JButton("Next State");

	JTextArea stateTextarea = new JTextArea(0, 0),
			switchesTextarea = new JTextArea(0, 0), 
			accounts = new JTextArea(0,0);

	/** can be added to the window */
	JScrollPane statescroller = new JScrollPane(),
			switchesscroller = new JScrollPane(),
			accountsscroller = new JScrollPane();

	SIDLSubmitPanel sbp = null;

	SIDL sidl = null;

	public static void main(String[] args) {
		List<String> l = Arrays.asList(args);
		SIDLGUI gui = new SIDLGUI(l.get(l.indexOf("-sidl") + 1));
		gui.setVisible(true);
	}

	void doJTextArea(JTextArea jt) {
		jt.setFont(new Font("Tahoma", Font.PLAIN, 9));
		jt.setEditable(false);
		jt.setLineWrap(true);
		jt.setDoubleBuffered(true);
	}

	public SIDLGUI(String s) {
		filename = s;
		this.setSize(1000, 500);

		doJTextArea(this.stateTextarea);
		doJTextArea(this.switchesTextarea);
		doJTextArea(this.accounts);

		statescroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		statescroller.getViewport().add(stateTextarea);

		switchesscroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		switchesscroller.getViewport().add(switchesTextarea);

		accountsscroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		accountsscroller.getViewport().add(accounts);

		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,	statescroller, switchesscroller);

		sp.setDividerLocation(260);
		sp.setContinuousLayout(true);
		sp.setOneTouchExpandable(true);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(sp, BorderLayout.CENTER);

		getContentPane().add(accountsscroller, BorderLayout.EAST);

		JPanel southpane = new JPanel();
		getContentPane().add(southpane, BorderLayout.SOUTH);
		southpane.setLayout(new BorderLayout());
		southpane.add(load, BorderLayout.WEST);
		southpane.add(next, BorderLayout.EAST);
		load.addActionListener(this);
		next.addActionListener(this);
		next.setEnabled(false);
		sbp = new SIDLSubmitPanel(this);
		getContentPane().add(sbp, BorderLayout.NORTH);

		this.addWindowListener(new SIDLGUICloser(this));
	}

	public synchronized void updateGUI() {
		if (sidl.pl(Base.TERMINAL)) {
			next.setEnabled(false);
			sbp.setEnabled(false);
		} else {
			SIDLLegals legals = sidl.plOwnedLegals();
			sbp.updateGUI(legals);
		}

		stateTextarea.setText("Global Facts:\n" + PP.pllike(sidl.plFacts(), 2));
		for (String a : sidl.getPlayers())
			stateTextarea.append("\n\nFacts for " + a + ":\n" + PP.pllike(sidl.plFacts(a), 2));

		Map<String[], Double> acs = sidl.plAccounts();
		
		this.accounts.setText("");
		for (Entry<String[], Double> e : acs.entrySet())
			this.accounts.append(PP.pllike(e.getKey()) + " => "
					+ PP.d2hr(e.getValue().doubleValue()) + "\n");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public synchronized void actionPerformed(ActionEvent e) {
		if (e.getSource() == load) {
			try {
				Base.initJPL();
				JFileChooser chooser = new JFileChooser();
				chooser.setSelectedFile(new File(this.filename)
						.getCanonicalFile());
				chooser.showOpenDialog(this);
				this.filename = chooser.getSelectedFile().getCanonicalPath();
				sidl = new SIDL(filename, new Random(System.currentTimeMillis()));
				this.setTitle(sidl.plName());
				load.setEnabled(false);
				next.setEnabled(true);
				sbp.setEnabled(true);
				this.updateGUI();
			} catch (Exception e1) {
				System.out.println(e1);
				System.exit(0);
			}
		} else if (e.getSource() == next) {
			if (!sidl.pl(Base.TERMINAL)) {
				sidl.makeOneChronon();
				this.updateGUI();
			} else {
				next.setEnabled(false);
				sbp.setEnabled(false);
			}
		}

	}

}
