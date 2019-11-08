package sidl.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jpl7.Query;
import org.jpl7.Term;
import sidl.core.Base;

/**
 * @author Rustam Tagiew
 *
 */
public class JHPL {
	
	public final static String fileString(String filename) throws SIDLException {
		String r = "", line;
		BufferedReader b;
		try {
			b = new BufferedReader(new FileReader(filename));
			while ((line = b.readLine()) != null)
			  r = r + line + "\n";
			b.close();
		} catch (Exception e) {
			throw new SIDLException("File unloadable!");
		}
		return r;		
	}

	public final static String[][] l2aa(Term o) {
		if (!o.isListPair())
			return new String[0][0];
		Term[] ts = o.toTermArray();
		String[][] r = new String[ts.length][];
		for (int i = 0; i < ts.length; i++)
			if (ts[i].isListPair())
				r[i] = l2a(ts[i]);
		return r;
	}

	public final static String[] l2a(Term o) {
		if (!o.isListPair())
			return new String[0];
		Term[] ts = o.toTermArray();
		String[] r = new String[ts.length];
		for (int i = 0; i < r.length; i++) {
			if (ts[i] instanceof org.jpl7.Atom) {
				r[i] = ts[i].name();
			} else if (ts[i] instanceof org.jpl7.Integer) {
				r[i] = ts[i].intValue() + "";
			}
		}
		return r;
	}

	public final static double[] l2da(Term o) {
		if (!o.isListPair())
			return new double[0];
		Term[] ts = o.toTermArray();
		double[] r = new double[ts.length];
		for (int i = 0; i < r.length; i++)
			r[i] = ts[i].doubleValue();
		return r;
	}

	public final static Term a2l(String[] a) {
		return org.jpl7.Util.stringArrayToList(a);
	}

	public final static String[][] qf(Query q, String X) {
		Map<String, Term>[] hta = q.allSolutions();
		if (hta == null)
			return new String[][] {};
		String[][] r = new String[hta.length][];
		for (int i = 0; i < hta.length; i++) {
			Term val = hta[i].get(X);
			r[i] = l2a(val);
		}
		return r;
	}

	public final static String[][] qf(Query q, String X, String C) {
		Map<String, Term>[] hta = q.allSolutions();
		if (hta == null)
			return new String[][] {};
		String[][] r = new String[hta.length][];
		for (int i = 0; i < hta.length; i++) {
			Term val = hta[i].get(X);
			String[] f = l2a(val);
			r[i] = new String[f.length + 1];
			int j = 0;
			for (String of : f)
				r[i][j++] = of;
			r[i][f.length] = hta[i].get(C).toString();
		}
		return r;
	}

	public final static String state2file(String[][] facts, Map<String[], Double> accounts) {
		String s = "";
		for (String[] f : facts)
			s = s + ":-assert(" + Base.FACT + "(" + PP.pllike(f) + ")).\n";
		for (Entry<String[], Double> e : accounts.entrySet())
			s = s + ":-assert(" + Base.ACCOUNT + "(" + PP.pllike(e.getKey()) + ","
					+ PP.d2hr(e.getValue()) + ")).\n";
		return s;
	}
	
	
	/** Accounts to String
	 * @param accounts
	 * @return
	 */
	public final static String acs2s(Map<String[], Double> accounts) {
		String s = "";
		for (Entry<String[], Double> e : accounts.entrySet())
			s = s + PP.pllike(e.getKey()) + "="
					+ PP.d2hr(e.getValue()) + ";";
		return s;
	}

	/** 
	 * @param ls
	 * @param b
	 * @return
	 */
	public final static String ls2s(HashMap<String[], String[][]> ls, int b) {
		String r = "";
		for (Entry<String[], String[][]> e : ls.entrySet())
			r = r + PP.pllike(e.getKey()) + ":\n"
					+ PP.pllike(e.getValue(), b) + "\n";
		return r;
	}

}
