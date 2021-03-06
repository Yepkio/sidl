package sidl.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.jpl7.Atom;
import org.jpl7.Compound;
import org.jpl7.JPL;
import org.jpl7.fli.Prolog;
import org.jpl7.Query;
import org.jpl7.Term;
import org.jpl7.Variable;

import sidl.utils.Decide;
import sidl.utils.JHPL;
import sidl.utils.PP;
import sidl.utils.SIDLException;
import sidl.utils.SIDLLegals;

/**
 * @author Rustam Tagiew
 * 
 */
public class SIDL extends Base {

	/**
	 * 
	 */
	private String name = "";
	
	/**
	 * 
	 */
	private String rules = null;

	/**
	 * 
	 */
	private String[] players = null;

	/**
	 * @return the players
	 */
	public final String[] getPlayers() {
		return players;
	}
	
	/**
	 * @param sidl
	 * @throws IOException
	 */
	public SIDL(Random r) throws SIDLException {
		consult(SIDL.class.getResource(SRC01).getPath());
		consult(JHPL.class.getResource(SRC02).getPath());
		setR(r);
	}
	
	/**
	 * @throws SIDLException
	 */
	public SIDL() throws SIDLException {
		this(new Random(System.currentTimeMillis()));
	}

	public SIDL(String s, Random rand) throws SIDLException {
		this(rand);
		this.loadGame(s);
	}
	
	public SIDL(String s) throws SIDLException {
		this(new Random(System.currentTimeMillis()));
		this.loadGame(s);
	}
	
	/**
	 * @return 
	 * @return
	 */
	private void plCheckplayers() throws SIDLException {
		Variable R = new Variable("R");
		Query query = new Query(PLAYER, R);
		if (!query.hasSolution())
			throw new SIDLException("There are no players!");
		Map<String, Term>[] hta = query.allSolutions();
		this.players = new String[hta.length];
		for (int i = 0; i < players.length; i++)
			this.players[i] = PP.pllike(JHPL.l2a(hta[i].get("R")));
	}
	
	/**
	 * @param game
	 */
	public void loadGame(String game) throws SIDLException {
		this.rules = JHPL.fileString(game);
		consult(game);
		pl(Base.INIT);
		plCheckplayers();
		this.name = this.plName();
	}

	/**
	 * @return
	 */
	public synchronized String plName() {
		Variable X = new Variable("X");
		Query query = new Query(NAME, X);
		if (!query.hasSolution())
			return "";
		return query.oneSolution().get("X").toString();
	}
	
	/**
	 * @return
	 */
	public synchronized String[][] plFacts() {
		Variable X = new Variable("X");
		Query query = new Query(FACT, X);
		if (!query.hasSolution())
			return new String[0][0];
		return JHPL.qf(query, "X");
	}

	/**
	 * Get facts visible for a player
	 * @param player 
	 * @return
	 */
	public synchronized String[][] plFacts(String player) {
		Variable X = new Variable("X");
		Query query = new Query(REVEAL, new Term[]{X, JHPL.mixedArrayToList(PP.likepl(player))});
		if (!query.hasSolution())
			return new String[0][0];
		return JHPL.qf(query, "X");
	}

	/**
	 * Get changes visible for a player
	 * @param player
	 * @return
	 */
	public synchronized String[][] plChanges(String player) {
		Variable X = new Variable("X");
		Variable C = new Variable("C");
		Query query = new Query(REVEAL, new Term[]{X, JHPL.mixedArrayToList(PP.likepl(player)), C});
		if (!query.hasSolution())
			return new String[0][0];
		return JHPL.qf(query, "X", "C");
	}

	/**
	 * get players' accounts
	 * @return
	 */
	public synchronized Map<String[], Double> plAccounts() {
		Map<String[], Double> r = new HashMap<String[], Double>();
		Variable A = new Variable("A");
		Variable M = new Variable("M");
		Query query = new Query(ACCOUNT, new Term[]{A, M});
		if (!query.hasSolution())
			return r;
		Map<String, Term>[] hta = query.allSolutions();
		for (Map<String, Term> ht : hta)
			r.put(JHPL.l2a(ht.get("A")), ht.get("M").doubleValue());
		return r;
	}
	
	/**
	 * @return
	 */
	public synchronized SIDLLegals plOwnedLegals() {
		Map<String[], String[][]> acts = new HashMap<String[], String[][]>();
		Map<String[], String[]> owns = new HashMap<String[], String[]>();
		Map<String[], Term> unlimitedStructures = new HashMap<String[], Term>();
		Variable P  = new Variable("P");
		Variable ID = new Variable("ID");
		Variable As = new Variable("As");
		Query query = new Query(LEGALS, new Term[]{P, ID, As});
		if (query.hasSolution())
		{
			Map<String,Term>[] hta = query.allSolutions();
			for (Map<String,Term> ht : hta) {
				String[] id = JHPL.l2a(ht.get("ID"));
				acts.put(id, JHPL.l2aa(ht.get("As")));
				owns.put(id, JHPL.l2a(ht.get("P")));
			}
		}
		query = new Query(LEGALSTRUCTURES, new Term[]{P, ID, As});
		if (query.hasSolution())
		{	
			Map<String,Term>[] hta = query.allSolutions();
			for (Map<String,Term> ht : hta) {
				String[] id = JHPL.l2a(ht.get("ID"));
				acts.put(id, JHPL.l2aa(ht.get("As")));
				owns.put(id, JHPL.l2a(ht.get("P")));
				unlimitedStructures.put(id,ht.get("As"));
			}
		}
		return new SIDLLegals(acts, owns, unlimitedStructures);
	}

	/**
	 * @return
	 */
	public synchronized Map<String[], String[][]> plLegals() {
		Map<String[], String[][]> r = new HashMap<String[], String[][]>();
		Variable ID = new Variable("ID");
		Variable As = new Variable("As");
		Query query = new Query(LEGALS, new Term[]{ID, As});
		if (!query.hasSolution())
			return r;
		Map<String,Term>[] hta = query.allSolutions();
		for (Map<String,Term> ht : hta)
			r.put(JHPL.l2a(ht.get("ID")), JHPL.l2aa(ht.get("As")));
		return r;
	}
	
	public synchronized Map<String, String> plDo(String dop) {
		Map<String, String> r = new HashMap<String, String>();
		Variable X = new Variable("X"), A = new Variable("A");
		Query query = new Query(dop, new Term[]{X, A});
		if (!query.hasSolution())
			return r;
		Map<String,Term>[] hta = query.allSolutions();
		for (Map<String,Term> ht : hta)
			r.put(PP.pllike(JHPL.l2a(ht.get("X"))), PP
					.pllike(JHPL.l2a(ht.get("A"))));
		return r;
	}

	public synchronized boolean plNatureActions() {
		Variable X = new Variable("X"), D = new Variable("D"), T = new Variable("T");
		Query query = new Query(NATURE, new Term[]{X, D, T});
		if (!query.hasSolution())
			return false;
		Map<String,Term>[] hta = query.allSolutions();
		for (Map<String,Term> ht : hta) {
			int c = (EQUAL.equals(ht.get("T").name())) ? 
					R.nextInt(ht.get("D").args()[0].intValue())
					: Decide.calcChoice(R, JHPL.l2da(ht.get("D")));
			Query toggle = new Query(TOGGLEP, new Term[]{ht.get("X"), new org.jpl7.Integer(c)});
			toggle.hasSolution();
		}
		return true;
	}

	public synchronized boolean plSubmitAction(String player, String id, String action) {	
		Term[] args = 
			{
				JHPL.mixedArrayToList(PP.likepl(player)), 
				JHPL.mixedArrayToList(PP.likepl(id)), 
				JHPL.mixedArrayToList(PP.likepl(action))
			};
	    
		return new Query(SUBMIT, args).hasSolution();
	}
	
	public void makeOneChronon() {
		plNatureActions();
		pl(Base.CHRONON);
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @return the rules
	 */
	public final String getRules() {
		return rules;
	}

}
