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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.JTextField;

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

	JComboBox<String> selectid = new JComboBox<String>(), 
			          selectaction = new JComboBox<String>();

	JLabel owner = new JLabel(OWNS + "none");
	
	JPanel unlimitedArgs = new JPanel();
	
	JTextField handInput = new JTextField();

	Map<String, String[][]> mainlegals = new HashMap<String, String[][]>();

	Map<String, String> mainidplayer = new HashMap<String, String>();
	
	Set<String> unlimitedInput = new HashSet<String>();

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
		unlimitedArgs.setLayout(new BorderLayout());
		unlimitedArgs.add(selectaction, BorderLayout.NORTH);
		unlimitedArgs.add(handInput, BorderLayout.SOUTH);
		handInput.setEnabled(false);
		this.add(unlimitedArgs, BorderLayout.SOUTH);
		selectid.addItemListener(this);
		submit.addActionListener(this);
		selectaction.addItemListener(this);
		this.setEnabled(false);
	}

	public void setEnabled(boolean a) {
		this.submit.setEnabled(a);
		this.selectaction.setEnabled(a);
		this.selectid.setEnabled(a);
		this.owner.setEnabled(a);
	}

	public void setID(String id) {
		this.selectaction.removeItemListener(this);
		this.selectaction.removeAllItems();
		for (String[] sw : mainlegals.get(id))
			selectaction.addItem(PP.pllike(sw));
		this.owner.setText(OWNS + this.mainidplayer.get(id));
		handInput.setEnabled(unlimitedInput.contains(id));
		this.selectaction.addItemListener(this);
	}

	public synchronized void itemStateChanged(ItemEvent it) {
		if (it.getItem() != null)
		{	
			submit.setForeground(Color.BLACK);
			if (this.selectid != null 
				&& this.selectid.getSelectedItem().equals(it.getItem()))
				this.setID((String) it.getItem());
			/*else if (it.getItem() != null && this.selectaction != null 
				&& this.selectaction.getSelectedItem()!=null	
				&& this.selectaction.getSelectedItem().equals(it.getItem()))
				System.out.println("here");*/
		}		
	}

	public void actionPerformed(ActionEvent arg0) {
		
		String player = owner.getText().replaceFirst(OWNS,"");
		String id = (String) selectid.getSelectedItem();
		String action;
		if (unlimitedInput.contains(id))
			action = handInput.getText();
		else	
			action = (String) selectaction.getSelectedItem();
		ref.switchesTextarea.append("player: " + player + "\n");
		ref.switchesTextarea.append("switch id: " + id + "\n");
		ref.switchesTextarea.append("action: " + action + "\n");
		
		if (ref.sidl.plSubmitAction(player, id, action))
		{	
			ref.switchesTextarea.append("=> success\n");
			submit.setForeground(Color.GREEN);
		}	
		else
		{	
			ref.switchesTextarea.append("=> failure\n");
			submit.setForeground(Color.RED);
		}	
	}

	public void updateGUI(SIDLLegals legals) {
		submit.setForeground(Color.BLACK);
		selectid.removeItemListener(this);
		selectid.removeAllItems();
		this.mainlegals.clear();
		this.mainidplayer.clear();
		this.unlimitedInput.clear();

		if (!legals.getActions().isEmpty()) {
			this.setEnabled(true);
			for (Entry<String[], String[][]> e : legals.getActions().entrySet()) {
				String s = PP.pllike(e.getKey());
				selectid.addItem(s);
				this.mainlegals.put(s, e.getValue());
				this.mainidplayer.put(s, PP.pllike(legals.getSwitch2player().get(e.getKey())));
				if (legals.getSwitch2unlimited().containsKey(e.getKey()))
					unlimitedInput.add(s);
			}
			setID((String) selectid.getSelectedItem());
		}

		selectid.addItemListener(this);
	}

}
