package sidl.utils;

import java.util.Random;

/** Very deterministic random
 * @author tagiew
 *
 */
public class NonRandom extends Random {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3272363697352854814L;

	/* (non-Javadoc)
	 * @see java.util.Random#nextDouble()
	 */
	public double nextDouble() {
		return 0.0;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Random#nextInt(int)
	 */
	public int nextInt(int n) {
		return 0;
	}
}
