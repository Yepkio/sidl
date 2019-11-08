/**
 * 
 */
package sidl.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sidl.utils.PP;
import sidl.utils.SIDLLegals;

/**
 * @author Rustam Tagiew
 * 
 */
public class SIDLSubmitPanel extends JPanel implements ItemListener,
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3015065534150299545L;

	public static final String OWNS = "owner: ";

	SIDLGUI ref = null;

	JButton submit = new JButton("Submit action");

	JComboBox<String> selectid = new JComboBox<String>(), selectaction = new JComboBox<String>();

	JLabel owner = new JLabel(OWNS + "none");

	HashMap<String, String[][]> mainlegals = new HashMap<String, String[][]>();

	HashMap<String, String> mainidplayer = new HashMap<String, String>();

	/**
	 * @param selectid
	 * @param selectaction
	 * @param selectplayer
	 */
	public SIDLSubmitPanel(SIDLGUI g) {
		super();
		ref = g;
		this.setLayout(new BorderLayout());
		this.add(submit, BorderLayout.WEST);
		this.add(owner, BorderLayout.EAST);
		this.add(selectid, BorderLayout.CENTER);
		this.add(selectaction, BorderLayout.SOUTH);
		selectid.addItemListener(this);
		submit.addActionListener(this);
		this.setEnabled(false);
	}

	public void setEnabled(boolean a) {
		this.submit.setEnabled(a);
		this.selectaction.setEnabled(a);
		this.selectid.setEnabled(a);
		this.owner.setEnabled(a);
	}

	public void setID(String id) {
		this.selectaction.removeAllItems();
		for (String[] sw : mainlegals.get(id))
			selectaction.addItem(PP.pllike(sw));
		this.owner.setText(OWNS + this.mainidplayer.get(id));
	}

	public synchronized void itemStateChanged(ItemEvent it) {
		if (this.selectid != null && it.getItem() != null
				&& this.selectid.getSelectedItem().equals(it.getItem()))
			this.setID((String) it.getItem());
	}

	public void actionPerformed(ActionEvent arg0) {
		boolean t = ref.sidl2.plSubmitAction(owner.getText().replaceFirst(OWNS,
				""), (String) selectid.getSelectedItem(), (String) selectaction
				.getSelectedItem());
		if (t)
			submit.setForeground(Color.BLACK);
		else
			submit.setForeground(Color.RED);
	}

	public void updateGUI(SIDLLegals legals) {
		selectid.removeItemListener(this);
		selectid.removeAllItems();
		this.mainlegals.clear();
		this.mainidplayer.clear();

		if (!legals.getActions().isEmpty()) {
			this.setEnabled(true);
			for (Entry<String[], String[][]> e : legals.getActions().entrySet()) {
				String s = PP.pllike(e.getKey());
				selectid.addItem(s);
				this.mainlegals.put(s, e.getValue());
				this.mainidplayer.put(s, PP.pllike(legals.getSwitch2player()
						.get(e.getKey())));
			}
			setID((String) selectid.getSelectedItem());
		}


		selectid.addItemListener(this);
	}

}
