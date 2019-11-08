package sidl.utils;

import java.util.Random;

public class Decide {

	/** chooses randomly from distribution
	 * @param R Random Generator
	 * @param d distribution
	 * @return
	 */
	public final static int calcChoice(Random R, double[] d) {
		double r = R.nextDouble();
		int i = 0, a = -1;
		double entry;
		while (i < d.length){
			entry = d[i];
			if (entry > 0) {
				a = i;
				r = r - entry;
				if (r < 0.0) break;
			}
			i++;
		}
		return a;
	}

}
