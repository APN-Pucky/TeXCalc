package TeXCalc.mathematica;

import java.io.File;

import TeXCalc.code.Code;
import TeXCalc.exec.Exec;
import TeXCalc.exec.Inter;
import TeXCalc.latex.Latex;
import TeXCalc.util.IO;

public class Mathematica implements Code {
	public static Object slow = new Object();
	public static Inter inter = new Inter("/home/apn/.local/bin/math");
	static {
		inter.input(IO.loadFile("run.m"));
	}

	public Mathematica() {
		requirePackage("mmacells");
	}

	public static String run_m = IO.loadFile("run.m");
	public static String mmacells = IO.loadFile("mmacells.sty");

	public static String toLatex(String math, Latex l) {
		return new Mathematica().to(math, l);
	}

	public String to(String math, Latex l) {
		synchronized(slow) {
		Exec ex = new Exec("mathematica",false);
		ex.writeFile("tmp.m", math);
		ex.writeFile("mmacells.sty", mmacells);
		l.cache("mmacells.sty", new File(ex.getDirName() + "mmacells.sty"));
		return inter.input("NBEval[\"" + ex.getDirName() + "tmp.m\"" + "]");
		}
	}

}
