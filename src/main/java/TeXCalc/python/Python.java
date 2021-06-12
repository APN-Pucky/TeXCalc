package TeXCalc.python;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

import org.json.JSONObject;

import TeXCalc.code.Code;
import TeXCalc.config.Config;
import TeXCalc.exec.Exec;
import TeXCalc.latex.Latex;
import TeXCalc.latex.StreamPrinter;
import TeXCalc.latex.TeXable;
import TeXCalc.latex.wrap.Wrapper;
import TeXCalc.util.IO;
import TeXCalc.util.Task;

public class Python implements Code{
	public static boolean PRINT = false;
	public static boolean FULL = false;
	public static boolean TIME = false;
	public static String ipynb_py_convert =  "import re\n" + 
			"import sys\n" + 
			"from ipynb_py_convert.__main__ import main\n" + 
			"if __name__ == '__main__':\n" + 
			"    sys.argv[0] = re.sub(r'(-script\\.pyw|\\.exe)?$', '', sys.argv[0])\n" + 
			"    sys.exit(main())\n";
	
	public static String ipynb_template(String py) { return  "{\n" + 
			"  \"cells\": [\n" + 
			"    {\n" + 
			"      \"cell_type\": \"code\",\n" + 
			"      \"metadata\": {},\n" + 
			"      \"source\": [\n" + 
					"\t" + String.join(",\n\t",Arrays.asList(py.split("\n")).stream().map((String a) -> JSONObject.quote(a+ "\n")).collect(Collectors.toList())) + "\n"+
			"      ],\n" + 
			"      \"outputs\": [],\n" + 
			"      \"execution_count\": null\n" + 
			"    }\n" + 
			"  ],\n" + 
			"  \"metadata\": {\n" + 
			"    \"anaconda-cloud\": {},\n" + 
			"    \"kernelspec\": {\n" + 
			"      \"display_name\": \"Python 3\",\n" + 
			"      \"language\": \"python\",\n" + 
			"      \"name\": \"python3\"\n" + 
			"    },\n" + 
			"    \"language_info\": {\n" + 
			"      \"codemirror_mode\": {\n" + 
			"        \"name\": \"ipython\",\n" + 
			"        \"version\": 3\n" + 
			"      },\n" + 
			"      \"file_extension\": \".py\",\n" + 
			"      \"mimetype\": \"text/x-python\",\n" + 
			"      \"name\": \"python\",\n" + 
			"      \"nbconvert_exporter\": \"python\",\n" + 
			"      \"pygments_lexer\": \"ipython3\",\n" + 
			"      \"version\": \"3.6.1\"\n" + 
			"    }\n" + 
			"  },\n" + 
			"  \"nbformat\": 4,\n" + 
			"  \"nbformat_minor\": 4\n" + 
			"}\n"; 
	}
	public static String toLatex(String s, Latex l) {return new Python().to(s,l);}
	public String to(String py, Latex l) {
		String filename = "tmp_jupyter_" + UUID.randomUUID().toString();
		Exec ex = new Exec("jupyter");
		//ex.writeFile(filename + ".py",py);
		//ex.writeFile("ipynb-nb-convert.py", ipynb_py_convert);
		//ex.exec(Config.current.getPython(),"ipynb-nb-convert.py", filename + ".py",filename + ".ipynb");
		ex.writeFile(filename + ".ipynb",ipynb_template(py));
		String ret = ex.exec("jupyter","nbconvert","--to","latex","--execute", filename + ".ipynb");
		if(!ret.equals(""))
			{
			System.out.println(ret);
			return  ret.split("------------------")[2]
					.replaceAll("\u001B\\[0;31m", " \\\\color{red}")
					.replaceAll("\u001B\\[0;36m", " \\\\color{green}")
					.replaceAll("\u001B\\[0;32m", " \\\\color{blue}")
					.replaceAll("\u001B\\[0m", " \\\\color{black}")
					.replaceAll("\\^", "\\\\^\\\\")
					.replaceAll("\n","\n\n")
					;
			}
		String s = ex.readFile(filename+ ".tex").split("maketitle")[1];
		if(!Config.current.getPython3().getShowCells().getValue())s = s.substring(s.indexOf("\\end{tcolorbox}")+15);
		s = s.split("\\\\end\\{document\\}")[0];
		
		File jpd = new File(ex.getDirName() + filename + "_files");
		if(jpd.exists()) {
			for(File f : jpd.listFiles()) {
				l.cache(filename + "_files" + File.separator + f.getName(), f);
			}
		}
		return s;
	}

 public static String toLatex2(String py,Latex l)
 {
	 	String tj = "tmp_jupyter_";
		String uuid = tj + UUID.randomUUID().toString();

		IO.writeFile(uuid+".py",py);

		if(!new File("ipynb-py-convert.py").exists())
		IO.writeFile("ipynb-py-convert"+".py",ipynb_py_convert);
		new File("ipynb-py-convert.py").deleteOnExit();
		ProcessBuilder pb = new ProcessBuilder("python3.7","ipynb-py-convert.py",  uuid + ".py" , uuid + ".ipynb");
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
			new File(uuid+ "_files").delete();
		}
		new File(uuid+ ".py").delete();
		new File(uuid+ ".ipynb").delete();
		new File(uuid+ ".tex").delete();
		
		return s;
 }
}
