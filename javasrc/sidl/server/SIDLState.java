package sidl.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import sidl.core.SIDL;
import sidl.utils.PP;

/**
 * @author Rustam Tagiew
 *
 */
public class SIDLState {
	
	String[][] facts;
	
	HashMap<String, Double> accounts;

	/**
	 * @return the facts
	 */
	public final String[][] getFacts() {
		return facts;
	}

	/**
	 * @return the accounts
	 */
	public final HashMap<String, Double> getAccounts() {
		return accounts;
	}

	/**
	 * @param facts the facts to set
	 */
	public final void setFacts(String[][] facts) {
		this.facts = facts;
	}

	/**
	 * @param accounts the accounts to set
	 */
	public final void setAccounts(HashMap<String, Double> accounts) {
		this.accounts = accounts;
	}

	/**
	 * @param facts
	 * @param accounts
	 */
	public SIDLState(String[][] facts, HashMap<String, Double> accounts) {
		super();
		this.facts = facts;
		this.accounts = accounts;
	}

	/**
	 * 
	 */
	public SIDLState() {
		super();
	}
	
	private void makeAccounts(SIDL sidl2) {
		Map<String[], Double> accs = sidl2.plAccounts();
		this.accounts = new HashMap<String, Double>();
		for (Entry<String[], Double> e : accs.entrySet())
			this.accounts.put(PP.pllike(e.getKey()), e.getValue());
	}

	public SIDLState(SIDL sidl2, boolean whole) {
		makeAccounts(sidl2);
		this.facts = (whole) ? sidl2.plFacts() : sidl2.plChanges("[]");
	}
	
	public SIDLState(SIDL sidl2, String agent, boolean whole) {
		makeAccounts(sidl2);
		this.facts = (whole) ? sidl2.plFacts() : sidl2.plChanges(agent);
	}
	
}
