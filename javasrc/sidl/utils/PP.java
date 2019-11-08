package sidl.utils;



public class PP {

	final public static int SL = 12;
	
	final public static double rd = 1000;
	
	/**
	 * Round human readably
	 * 
	 * @param d
	 *            double value
	 * @return
	 */
	public static double d2hr(double d) {
		return Math.round(d * PP.rd) / PP.rd;
	}
	
	public static String d2hr(double[] d) {
		int l = d.length - 1;
		String s = "[";
		for (int i = 0; i < l; i++)
			s = s + d2hr(d[i]) + ",";
		return s + d2hr(d[l]) + "]";
	}

	public final static String[] likepl(String a) {
		return a.replace("[", " ").replace("]", " ").trim().split(",");
	}

	/**
	 * @param a
	 * @return
	 */
	public final static String pllike(String[] a) {
		if (a.length==0)
			return "[]";
		String r = "[";
		for (int i = 0; i < (a.length-1); i++)
			r = r + a[i] + ",";
		return r + a[a.length-1] + "]";		
	}

	public final static String pllike(String[][] a) {
		if (a.length==0)
			return "[[]]";
		String r = "[";
		for (int i = 0; i < (a.length-1); i++)
			r = r + pllike(a[i]) + ",";
		r = r + pllike(a[a.length-1]) + "]";
		return r;
	}

	public static String pllike(String[][] fs, int b) {
		String r = "";
		for (int i = 0; i < fs.length; i++)
			r = r + pllike(fs[i]) + ((i % b == b - 1) ? "\n" : "");
		return r;
	}
}
