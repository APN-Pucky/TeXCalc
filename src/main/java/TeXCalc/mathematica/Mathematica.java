package TeXCalc.mathematica;

import java.io.File;

import TeXCalc.code.Code;
import TeXCalc.exec.Exec;
import TeXCalc.latex.Latex;
import TeXCalc.util.IO;

public class Mathematica implements Code {
	public static Object slow;
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
		ex.writeFile("run.m", run_m);
		ex.writeFile("tmp.m", math);
		ex.writeFile("mmacells.sty", mmacells);
		l.cache("mmacells.sty", new File(ex.getDirName() + "mmacells.sty"));
		// System.out.println("start");
		String ret = ex.exec("/bin/sh", "-c", "/home/apn/.local/bin/math -run < run.m > out.txt");
		// System.out.println("end: '"+ ex.readFile("out.txt") + "'");
		String o = ex.readFile("out.txt");
		System.out.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		if(o.indexOf("\\begin")==-1 || o.lastIndexOf("}") == -1)
		{
			return "Err";
		}
		else
		{
			System.out.println("ind: "+ o.indexOf("\\begin"));
			return o.substring(o.indexOf("\\begin"), o.lastIndexOf("}") + 1);
		}
		}
	}

}
