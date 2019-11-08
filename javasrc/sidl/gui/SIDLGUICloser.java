/**
 * 
 */
package sidl.gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author Rustam Tagiew
 *
 */
public class SIDLGUICloser implements WindowListener {
	
	/**
	 * @param ref
	 */
	public SIDLGUICloser(SIDLGUI ref) {
		super();
		this.ref = ref;
	}

	SIDLGUI ref = null;

	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		ref.dispose();
		System.exit(0);
	}

	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		ref.dispose();
		System.exit(0);
	}

	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}
}
