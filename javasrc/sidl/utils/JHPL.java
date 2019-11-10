package sidl.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jpl7.fli.Prolog;
import org.jpl7.Query;
import org.jpl7.Term;
import org.jpl7.Atom;
import org.jpl7.Compound;
import org.jpl7.JPL;

import sidl.core.Base;


/**
 * @author Rustam Tagiew
 *
 */
public class JHPL {
	
	public static String fileString(String filename) throws SIDLException {
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

	/**
	 * 
	 */
	public static String[][] l2aa(Term o) {
		if (!o.isListPair())
			return new String[0][0];
		Term[] ts = o.toTermArray();
		String[][] r = new String[ts.length][];
		for (int i = 0; i < ts.length; i++)
			if (ts[i].isListPair())
				r[i] = l2a(ts[i]);
		return r;
	}
	
	/**
	 * Turn a list of atoms, strings, doubles and ints into a string array
	 */
	public static String[] l2a(Term o) {
		if (!o.isListPair())
			return new String[0];
		Term[] ts = o.toTermArray();
		String[] r = new String[ts.length];
		for (int i = 0; i < r.length; i++) {
			switch(ts[i].type())
			{
				case Prolog.INTEGER: 
					r[i] = Integer.toString(ts[i].intValue());
					break;
				case Prolog.FLOAT:
					r[i] = Double.toString(ts[i].doubleValue());
					break;
				case Prolog.STRING:
				case Prolog.ATOM:	
					r[i] = ts[i].name();
					break;
				case Prolog.COMPOUND: 
					r[i] = compound2string((Compound) ts[i]);
					break;	
			}
		}
		return r;
	}
	
	/**
	 * Turn a list of atoms, strings, doubles and ints into a string array
	 */
	public static String compound2string(Compound o) {
		Term[] ts = o.args();
		String r = "";
		for (int i = 0; i < ts.length; i++) 
			r = r + ":" + ts[i].name();
		return r;
	}

	public static double[] l2da(Term o) {
		if (!o.isListPair())
			return new double[0];
		Term[] ts = o.toTermArray();
		double[] r = new double[ts.length];
		for (int i = 0; i < r.length; i++)
			r[i] = ts[i].doubleValue();
		return r;
	}

	public static String[][] qf(Query q, String X) {
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

	public static String[][] qf(Query q, String X, String C) {
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

	public static String state2file(String[][] facts, Map<String[], Double> accounts) {
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
	public static String acs2s(Map<String[], Double> accounts) {
		String s = "";
		for (Entry<String[], Double> e : accounts.entrySet())
			s = s + PP.pllike(e.getKey()) + "="+ PP.d2hr(e.getValue()) + ";";
		return s;
	}

	/** 
	 * @param ls
	 * @param b
	 * @return
	 */
	public static String ls2s(Map<String[], String[][]> ls, int b) {
		String r = "";
		for (Entry<String[], String[][]> e : ls.entrySet())
			r = r + PP.pllike(e.getKey()) + ":\n" + PP.pllike(e.getValue(), b) + "\n";
		return r;
	}

	public static Term mixedArrayToList(String[] a) {
		Term list = JPL.LIST_NIL; 
		for (int i = a.length - 1; i >= 0; i--) {
			try
			{
				list = new Compound(JPL.LIST_PAIR, 
						new Term[] { new org.jpl7.Integer(Integer.parseInt(a[i])), list });
			}
			catch(NumberFormatException ignored0)
			{
			    try
			    {
			    	list = new Compound(JPL.LIST_PAIR, 
							new Term[] { new org.jpl7.Float(Double.parseDouble(a[i])), list });
			    }
				catch(NumberFormatException ignored1)
				{
					list = new Compound(JPL.LIST_PAIR, new Term[] { new Atom(a[i]), list });
				}
			}
		}
		return list;
	}
	
}
