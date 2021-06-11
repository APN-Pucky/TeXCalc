package TeXCalc.exec;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.UUID;

import TeXCalc.latex.StreamPrinter;
import TeXCalc.util.IO;
import TeXCalc.util.Task;
import lombok.Data;

@Data
public class Exec {
	public static boolean PRINT = false;
	public static boolean TIME = false;
	String uuid;
	String dirName;
	File dir;
	String name;
	boolean forcestop;
	public Exec(String name) {this(name,true);}
	public Exec(String name,boolean forcestop) {
		this.name = name;
		this.forcestop = forcestop;
		uuid = UUID.randomUUID().toString();
		dirName = ".tmp" + File.separator + name + "_" + uuid + File.separator + name + File.separator;
		dir = new File(dirName);
		dir.mkdirs();
	}
	
	public String exec(String... args) {
		System.out.println("Execute " + name +  " in " + dirName + "$ ./" + args[0]); 
		ProcessBuilder pb = new ProcessBuilder(args);
		Process p = null;
		pb.directory(dir);
		try {
			long startTime = System.nanoTime();
			p = pb.start();
			StreamPrinter fluxErreur=null;
				StreamPrinter fluxSortie = new StreamPrinter(p.getInputStream(), PRINT);
				fluxErreur = new StreamPrinter(p.getErrorStream(), PRINT);
				Task.startUntracked(fluxSortie);
				Task.startUntracked(fluxErreur);
			int exit = p.waitFor();
			if(exit==1) return fluxErreur.text;
			long stopTime = System.nanoTime();
			if(TIME)System.out.println((stopTime - startTime) / 1.e9 + " s");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		catch(InterruptedException ie) {
			System.out.println("stopped, due to outdated");
			if(p!=null) {
				if(forcestop)
					p.destroyForcibly();
				//else
					//p.destroy();
			}
		}
		return "";
	}
	public void writeFile(String filename ,String cont) {
		IO.writeFile(getDirName() + filename ,cont);
	}
	public String readFile(String filename) {
		return IO.readFile(getDirName() + filename); 
		
	}
}
