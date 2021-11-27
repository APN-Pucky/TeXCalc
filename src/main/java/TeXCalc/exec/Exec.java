package TeXCalc.exec;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.UUID;

import TeXCalc.config.Config;
import TeXCalc.gui.GUI;
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

	public Exec(String name) {
		this(name, true);
	}

	public Exec(String name, boolean forcestop) {
		this.name = name;
		this.forcestop = forcestop;
		uuid = UUID.randomUUID().toString();
		dirName = ".tmp" + File.separator + name + "_" + uuid + File.separator + name + File.separator;
		dir = new File(dirName);
		dir.mkdirs();
	}

	public String exec(String... args) {
		GUI.log.d("Execute " + name + " in " + dirName + "$ ./" + args[0]);
		ProcessBuilder pb = new ProcessBuilder(args);
		Process p = null;
		pb.directory(dir);
		try {
			GUI.log.d("Running " + name + " in " + dirName + "$ ./" + args[0]);
			long startTime = System.nanoTime();
			p = pb.start();
			StreamPrinter fluxSortie = new StreamPrinter(p.getInputStream(), Task.id() + "",
					Config.current.getDebug().getPrintOuput().getValue());
			StreamPrinter fluxErreur = new StreamPrinter(p.getErrorStream(), Task.id() + "",
					Config.current.getDebug().getPrintError().getValue());
			Task.startUntracked(fluxSortie);
			Task.startUntracked(fluxErreur);
			Thread et = Thread.currentThread();
			Task.startUntracked(() -> {
				try {
					Thread.sleep(1000 * Config.current.getGeneral().getThreadsTimeoutSeconds().getValue());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(et.isAlive())
				{
					et.interrupt();
					GUI.log.e("Thread froze -> killed: " + dir + " $ " +String.join(" ", args) , "Exec");
				}
			});
			GUI.log.d("w8 " + name + " in " + dirName + "$ ./" + args[0]);
			int exit = p.waitFor();
			GUI.log.d("w8d " + name + " in " + dirName + "$ ./" + args[0]);
			if (exit == 1)
				return fluxErreur.text;
			long stopTime = System.nanoTime();
			if (TIME)
				GUI.log.m((stopTime - startTime) / 1.e9 + " s");
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (InterruptedException ie) {
			GUI.log.m("stopped, due to outdated " + dir + "  " + String.join(" ", args));
			if (p != null) {
				if (forcestop)
					p.destroyForcibly();
				// else
				// p.destroy();
			}
		}
		return "";
	}

	public void writeFile(String filename, String cont) {
		IO.writeFile(getDirName() + filename, cont);
	}

	public String readFile(String filename) {
		return IO.readFile(getDirName() + filename);

	}
}
