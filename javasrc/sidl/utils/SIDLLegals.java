package sidl.utils;

import java.util.HashMap;
import java.util.Map;
import org.jpl7.Term;

public class SIDLLegals {
	
	private Map<String[], String[][]> actions = null;
	
	private Map<String[], String[]> switch2player = null;
	
	private Map<String[], Term> switch2unlimited = null;

	/**
	 * @param actions
	 * @param switch2player
	 */
	public SIDLLegals(Map<String[], String[][]> actions, 
			          Map<String[], String[]> switch2player,
			          Map<String[], Term> switch2unlimited) {
		super();
		this.actions = actions;
		this.switch2player = switch2player;
		this.switch2unlimited = switch2unlimited;
	}

	
	/**
	 * @param actions
	 * @param switch2player
	 */
	public SIDLLegals(Map<String[], String[][]> actions, 
			          Map<String[], String[]> switch2player) {
		this(actions, switch2player, new HashMap<String[], Term>());
	}

	/**
	 * 
	 */
	public SIDLLegals() {
		this.actions = new HashMap<String[], String[][]>();
		this.switch2player = new HashMap<String[], String[]>();
		this.switch2unlimited = new HashMap<String[], Term>();
	}

	/**
	 * @return the actions
	 */
	public final Map<String[], String[][]> getActions() {
		return actions;
	}

	/**
	 * @return the switch2player
	 */
	public final Map<String[], String[]> getSwitch2player() {
		return switch2player;
	}

	/**
	 * @return the switch2player
	 */
	public final Map<String[], Term> getSwitch2unlimited() {
		return switch2unlimited;
	}
	
	/**
	 * @param actions the actions to set
	 */
	public final void setActions(Map<String[], String[][]> actions) {
		this.actions = actions;
	}

	/**
	 * @param switch2player the switch2player to set
	 */
	public final void setSwitch2player(Map<String[], String[]> switch2player) {
		this.switch2player = switch2player;
	}
	
	public final void setSwitch2unlimited(Map<String[], Term> switch2unlimited) {
		this.switch2unlimited = switch2unlimited;
	}
}
