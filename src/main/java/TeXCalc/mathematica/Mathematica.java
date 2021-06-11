package TeXCalc.mathematica;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import TeXCalc.code.Code;
import TeXCalc.config.Config;
import TeXCalc.exec.Exec;
import TeXCalc.exec.Inter;
import TeXCalc.latex.Latex;
import TeXCalc.util.IO;

public class Mathematica implements Code {
	public static Object slow = new Object();
	public static Inter inter = null;

	public Mathematica() {
		requirePackage("mmacells");
	}

	public static String run_m = IO.loadFile("run.m");
	public static String mmacells = IO.loadFile("mmacells.sty");

	public static String toLatex(String math, Latex l) {
		return new Mathematica().to(math, l);
	}

	public void init() {
		if (inter == null) {

			try {
				inter = new Inter(Config.current.getMathematicaPATH());
			} catch (IOException e) {
				String math = JOptionPane.showInputDialog("Please enter the path to your mathematica installation ($ whereis math)");
				Config.current.setMathematicaPATH(math);
				try {
					inter = new Inter(Config.current.getMathematicaPATH());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			inter.input(IO.loadFile("run.m"));
		}

	}

	public String to(String math, Latex l) {
		synchronized (slow) {
			init();
			Exec ex = new Exec("mathematica", false);
			ex.writeFile("tmp.m", math);
			ex.writeFile("mmacells.sty", mmacells);
			l.cache("mmacells.sty", new File(ex.getDirName() + "mmacells.sty"));
			return inter.input("NBEval[\"" + ex.getDirName() + "tmp.m\"" + "]");
		}
	}

}
