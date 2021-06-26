package TeXCalc.mathematica;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

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
		super();
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
				inter = new MathematicaInter(Config.current.getMath().getMathematicaPATH().getValue());
			} catch (IOException e) {
				String math = JOptionPane.showInputDialog("Please enter the path to your mathematica installation ($ whereis math)");
				Config.current.getMath().getMathematicaPATH().setValue(math);
				Config.current.save();
				try {
					inter = new MathematicaInter(Config.current.getMath().getMathematicaPATH().getValue());
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
			return inter.input("NBEval[\"" + ex.getDirName() + "tmp.m\"" + "]").replaceAll("\\s*\\\\\\s*\n*\\s*\n*\\s*>\\s*", "");
		}
	}
	public static class MathematicaInter extends Inter {
		public boolean unlicensed = false;
	public MathematicaInter(String... command) throws IOException {
			super(command);
		}

	public String input(String in) {
		if(unlicensed)return "unlicensed";
		PrintWriter pr = new PrintWriter(p.getOutputStream());
		pr.println(in);
		pr.flush();
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String ligne = "";
        String text = "";
        boolean reset = false;
        char[] c = new char[1];
        while(true) {
        	try {
				br.read(c,0,1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	//System.out.print(c[0]);
        	text += c[0];
        	//System.out.println(text);
        	String[] ar;
        	if(text.contains("\n")) {
        		ar = text.split("\n");
        		if(ar.length == 0)
        			ar = new String[] {""};
        	}
        	else {
        		ar = new String[] {text};
        	}
        	if(ar[ar.length-1].matches(".*activation key.*")) {
        		unlicensed = true;
        		return "unlicensed";
        	}
        	if(ar[ar.length-1].matches(".*Out\\[\\d+\\]=.*")) {
        		//System.out.println("RESET");
        		text = "";
        		reset= true;
        	}
        	if(reset && ar[ar.length-1].matches(".*In\\[\\d+\\]:=.*")) {
        		//System.out.println("BREAK");
        		text ="";
        		for(int i =0; i < ar.length-2;++i) {
        			text += ar[i] + "\n";
        		}
        		break;
        	}
        }
     return text.replace("In\\[\\d+\\]:=","");
	}
	
	}


}
