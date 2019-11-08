package sidl.utils;

import java.util.HashMap;
import java.util.Map;

public class SIDLLegals {
	
	private Map<String[], String[][]> actions = null;
	
	private Map<String[], String[]> switch2player = null;

	/**
	 * @param actions
	 * @param switch2player
	 */
	public SIDLLegals(Map<String[], String[][]> actions, Map<String[], String[]> switch2player) {
		super();
		this.actions = actions;
		this.switch2player = switch2player;
	}

	/**
	 * 
	 */
	public SIDLLegals() {
		this.actions = new HashMap<String[], String[][]>();
		this.switch2player = new HashMap<String[], String[]>();
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
	
}
