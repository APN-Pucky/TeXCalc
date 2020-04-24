// src: https://tex.stackexchange.com/a/167133
package TeXCalc.latex;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.google.common.io.Files;

public class Latex {

	public static BufferedImage toImage(String latex) {
		String uuid = UUID.randomUUID().toString();
		String TEMP_DIRECTORY = ".tmp" + File.separator + uuid;
		String TEMP_TEX_FILE_NAME = uuid; // for New22.tex
		String ret = TEMP_DIRECTORY + File.separator + TEMP_TEX_FILE_NAME + ".png";

		// 1. Prepare the .tex file
		String newLineWithSeparation = System.getProperty("line.separator") + System.getProperty("line.separator");
		String math = "";
		math += "\\documentclass[preview,crop,border=1pt,convert]{standalone}" + newLineWithSeparation;
		math += "\\usepackage{amsfonts}" + newLineWithSeparation;
		math += "\\usepackage{amsmath}" + newLineWithSeparation;
		math += "\\begin{document}" + newLineWithSeparation;
		math += latex + newLineWithSeparation;
		math += "\\end{document}";

		System.out.println(" 2. Create the .tex file");
		FileWriter writer = null;
		try {
			File dir = new File(TEMP_DIRECTORY + File.separator + "tex");
			if (!dir.exists())
				dir.mkdirs();
			writer = new FileWriter(
					TEMP_DIRECTORY + File.separator + "tex" + File.separator + TEMP_TEX_FILE_NAME + ".tex", false);
			writer.write(math, 0, math.length());
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		System.out.print("  3. Execute LaTeX {" + uuid + "} from command line  to generate picture = ");
		ProcessBuilder pb = new ProcessBuilder("pdflatex",  "-halt-on-error",
				TEMP_TEX_FILE_NAME + ".tex");
		pb.directory(new File(TEMP_DIRECTORY + File.separator + "tex"));
		try {
			long startTime = System.nanoTime();
			Process p = pb.start();
			StreamPrinter fluxSortie = new StreamPrinter(p.getInputStream(), true);
			StreamPrinter fluxErreur = new StreamPrinter(p.getErrorStream(), true);
			new Thread(fluxSortie).start();
			new Thread(fluxErreur).start();
			p.waitFor();
			long stopTime = System.nanoTime();
			System.out.println((stopTime - startTime) / 1.e9 + " s");
		} catch (IOException | InterruptedException ex) {
			ex.printStackTrace();
		}
		
		pb = new ProcessBuilder("pdftoppm",  "-png",
				TEMP_TEX_FILE_NAME + ".pdf");
		pb.directory(new File(TEMP_DIRECTORY + File.separator + "tex"));
		BufferedImage bi = null;
		try {
			Process p = pb.start();
			bi = ImageIO.read(p.getInputStream());
			StreamPrinter fluxSortie = new StreamPrinter(p.getInputStream(), true);
			StreamPrinter fluxErreur = new StreamPrinter(p.getErrorStream(), true);
			new Thread(fluxSortie).start();
			new Thread(fluxErreur).start();
					p.waitFor();
			
		} catch (InterruptedException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		cleanUp(TEMP_DIRECTORY, TEMP_TEX_FILE_NAME);

		return bi;
	}

	private static void cleanUp(String TEMP_DIRECTORY, String TEMP_TEX_FILE_NAME) {
		// 5. Delete files
		for (File file : (new File(TEMP_DIRECTORY + File.separator + "tex").listFiles())) {
			if (file.getName().startsWith(TEMP_TEX_FILE_NAME + ".")) {
				//file.delete();
			}
		}
	}

	public static BufferedImage toMathImage(String math) {
		return toImage("$" + math + "$");
	}
}
