package sidl.core;

import java.util.Random;

import org.jpl7.Query;

import sidl.utils.SIDLException;

public class Base {

	/**
	 * Own random generator
	 */
	protected Random R = null;

	public final static String SRC01 = "sidl.pl", SRC02 = "helpful.pl";

	public final static String PLAYER = "player", GAME = "game",
			OWNED = "owned", FACT = "fact", EQUAL = "equal", DOES = "does",
			NAME = "name";
			
	public final static String	INIT = "sidl_init", ACCOUNT = "sidl_account",
			TERMINAL = "sidl_terminal", NATURE = "sidl_nature",
			TOGGLE = "sidl_toggle", TOGGLEP = "sidl_toggleP",
			PREPARE = "sidl_prepare", COMPLETE = "sidl_complete",
			DONE = "sidl_done", SUBMIT = "sidl_submit", CLEAN = "sidl_clean",
			LEGALS = "sidl_legals", REVEAL = "sidl_reveal",
			CHRONON = "sidl_chronon";

	public final static String[] args = new String[] { "swipl", "-q", "-g", "true", "--nosignals" };

	protected synchronized void consult(String s) throws SIDLException {
		if (!(Query.hasSolution("consult('" + s + "')")))
			throw new SIDLException("Problems consulting " + s + "!");
	}

	/**
	 * @param r
	 *            the r to set
	 */
	public final void setR(Random r) {
		R = r;
	}

	/**
	 * @return
	 */
	public synchronized boolean pl(String s) {
		return (new Query(s)).hasSolution();
	}

	public final synchronized static void initJPL() {
		//JPL.init(args);
	}

	public final static void stopJPL() {
		Query.hasSolution("halt()");
	}

}
