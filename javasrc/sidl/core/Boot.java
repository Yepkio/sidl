package sidl.core;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import sidl.utils.JHPL;
import sidl.utils.NonRandom;
import sidl.utils.PP;

/**
 * @author Rustam Tagiew
 * 
 */
public class Boot {

	public static void printHelp() {
		System.out.println("You running on " + System.getProperty("os.name"));
		System.out.println("-sidl <filename>.yml  SIDL2.0 file");
		System.out.println("-random <type>		  sec - secure random");
		System.out.println("            		  non - deterministic");
		System.out.println("-run                  Run till the game is dead");
		System.out.println("                      default 0 chronons");
		System.out
				.println("-stop <iteration>     stop after <iterations> iterations");
		System.out.println("-dump                 Dump last full state");
		System.out.println("-gui                  GUI for the game");
		System.out
				.println("-dumpall              Dump operations in every chronon");
	}


	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Add \"-help\" as argument and see the stuff!");
			System.exit(0);
		}
		
		SIDL sidl = null;
		long steps = 10;
		boolean dumpall = true;
		List<String> l = Arrays.asList(args);
		
		if (l.contains("-help")) {
			printHelp();
		} else if (l.contains("-sidl")) {
			try {
				Base.initJPL();
				sidl = new SIDL(l.get(l.indexOf("-sidl") + 1), new Random(
						System.currentTimeMillis()));
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.exit(0);
			}
		}
		
		if (l.contains("-run")) {
			if (l.contains("-random")) {
				Random sr = (l.get(l.indexOf("-random") + 1).equals("non")) ? new NonRandom()
						: new SecureRandom();
				sr.setSeed(System.currentTimeMillis());
				sidl.setR(sr);
			}

			if (l.contains("-stop"))
				steps = Long.parseLong(l.get(l.indexOf("-stop") + 1));

			if (!l.contains("-dumpall"))
				dumpall = false;

			long time = System.currentTimeMillis();
			int i = 0;
			while (i < steps && !sidl.pl(Base.TERMINAL)) {
				i++;

				if (dumpall) {
					System.out.println(" ");
					System.out.println("»»»»»»»»»»»»»»»»»»»»»»»»»»» ITERATION #" + i);
					System.out.println("Legal switches: \n"
							+ JHPL.ls2s(sidl.plLegals(), 5));
				}

				sidl.plNatureActions();

				if (dumpall)
					System.out.println("Does: " + sidl.plDo(Base.DOES).toString());

				sidl.pl(Base.PREPARE);

				if (dumpall)
					System.out.println("Done: " + sidl.plDo(Base.DONE).toString());

				if (dumpall) {
					System.out.println("Changes: " + PP.pllike(sidl.plChanges("[]")));
					for (String a : sidl.getPlayers()) 
						System.out.println("Changes for " + a + ": " +
								PP.pllike(sidl.plChanges(a)));
					
					System.out.println("Accounts: " + JHPL.acs2s(sidl.plAccounts()));
				}
					
				sidl.pl(Base.COMPLETE);

				if (dumpall) {
					System.out.print("THE WHOLE GAME STATE:\n");
					System.out.println(PP.pllike(sidl.plFacts(), 5));
				}
			}

			time = System.currentTimeMillis() - time;
			System.out.println(" ");
			System.out.println(i + " steps in " + time + " ms");
			System.out.println("It means " + (i * 1.0) / (time * 1.0)
					+ " steps pro 1 ms");
		}

		if (l.contains("-dump")) {
			System.out.println(" ");
			System.out
					.println(JHPL.state2file(sidl.plFacts(), sidl.plAccounts()));
		}

		// sidl2.submitAction("[red]", "[red]", "[blue,blue]");
	}

}
