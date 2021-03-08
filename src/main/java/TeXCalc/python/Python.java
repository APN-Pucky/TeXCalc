package TeXCalc.python;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import TeXCalc.latex.Latex;
import TeXCalc.latex.StreamPrinter;
import TeXCalc.util.IO;
import TeXCalc.util.Task;

public class Python{
	public static boolean PRINT = false;
	public static boolean TIME = false;
 public static String toLatex(String py,Latex l)
 {
	 	String tj = "tmp_jupyter_";
		String uuid = tj + UUID.randomUUID().toString();

		FileWriter writer = null;
	 try {
			writer = new FileWriter(
					uuid + ".py", false);
			writer.write(py, 0, py.length());
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		ProcessBuilder pb = new ProcessBuilder("python3.7","-m","ipynb-py-convert",  uuid + ".py" , uuid + ".ipynb");
		try {
			long startTime = System.nanoTime();
			Process p = pb.start();
			if(PRINT) {
				StreamPrinter fluxSortie = new StreamPrinter(p.getInputStream(), true);
				StreamPrinter fluxErreur = new StreamPrinter(p.getErrorStream(), true);
				Task.startUntracked(fluxSortie);
				Task.startUntracked(fluxErreur);
			}
			p.waitFor();
			long stopTime = System.nanoTime();
			if(TIME)System.out.println((stopTime - startTime) / 1.e9 + " s");
		} catch (IOException | InterruptedException ex) {
			ex.printStackTrace();
		}
		
		
		pb = new ProcessBuilder("jupyter","nbconvert","--to","latex","--execute", uuid + ".ipynb");
		int exit = 0;
		try {
			long startTime = System.nanoTime();
			Process p = pb.start();
			StreamPrinter fluxErreur = null;
			//if(PRINT) {
				StreamPrinter fluxSortie = new StreamPrinter(p.getInputStream(), PRINT);
				fluxErreur = new StreamPrinter(p.getErrorStream(), PRINT);
				Task.startUntracked(fluxSortie);
				Task.startUntracked(fluxErreur);
			//}
			exit =p.waitFor();
			if(exit==1) return fluxErreur.text.split("------------------")[2]
					.replaceAll("\u001B\\[0;31m", " \\\\color{red}")
					.replaceAll("\u001B\\[0;36m", " \\\\color{green}")
					.replaceAll("\u001B\\[0;32m", " \\\\color{blue}")
					.replaceAll("\u001B\\[0m", " \\\\color{black}")
					.replaceAll("\\^", "\\\\^\\\\")
					.replaceAll("\n","\n\n")
					;
			long stopTime = System.nanoTime();
			if(TIME)System.out.println((stopTime - startTime) / 1.e9 + " s");
		} catch (IOException | InterruptedException ex) {
			ex.printStackTrace();
		}
		String s = IO.readFile(uuid + ".tex").split("maketitle")[1];
		s = s.split("\\\\end\\{tcolorbox\\}")[1];
		s = s.split("\\\\end\\{document\\}")[0];
		
		if(new File(uuid + "_files").exists()) {
			for(File f : new File(uuid + "_files").listFiles()) {
				l.cache(f.getPath());
			}
			new File(uuid+ "_files").deleteOnExit();
		}
		new File(uuid+ ".py").deleteOnExit();
		new File(uuid+ ".ipynb").deleteOnExit();
		
		return s;
 }
}
