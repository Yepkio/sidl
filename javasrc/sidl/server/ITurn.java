package sidl.server;

public class ITurn {
	
	String number = null;
	
	String choice = null;

	/**
	 * 
	 */
	public ITurn() {
	}
	
	/**
	 * @param number
	 * @param choice
	 */
	public ITurn(String number, String choice) {
		this.number = number;
		this.choice = choice;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the choice
	 */
	public String getChoice() {
		return choice;
	}

	/**
	 * @param choice the choice to set
	 */
	public void setChoice(String choice) {
		this.choice = choice;
	}
}
