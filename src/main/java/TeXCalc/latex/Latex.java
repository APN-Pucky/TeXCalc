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

import TeXCalc.util.Task;

public class Latex {
	
	public static File toPdf(String latex) {
		String uuid = UUID.randomUUID().toString();
		String TEMP_DIRECTORY = ".tmp" + File.separator + uuid;
		String TEMP_TEX_FILE_NAME = uuid; // for New22.tex
		String ret = TEMP_DIRECTORY + File.separator + TEMP_TEX_FILE_NAME + ".png";
		File ret_file = new File(TEMP_DIRECTORY + File.separator + TEMP_TEX_FILE_NAME + ".pdf") ;

		// 1. Prepare the .tex file
		String newLineWithSeparation = System.getProperty("line.separator") + System.getProperty("line.separator");
		String math = latex;

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
			Task.startUntracked(fluxSortie);
			Task.startUntracked(fluxErreur);
			p.waitFor();
			long stopTime = System.nanoTime();
			System.out.println((stopTime - startTime) / 1.e9 + " s");
		} catch (IOException | InterruptedException ex) {
			ex.printStackTrace();
		}
		
		try {
			File tmp = new File(TEMP_DIRECTORY + File.separator + "tex" + File.separator + TEMP_TEX_FILE_NAME + ".pdf");
			if(tmp.exists()) {
			Files.move(tmp,new File(TEMP_DIRECTORY + File.separator + TEMP_TEX_FILE_NAME + ".pdf") );
			}
			else
			{
				ret_file = null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret_file = null;
		}

		cleanUp(TEMP_DIRECTORY, TEMP_TEX_FILE_NAME);

		return ret_file;
	}
	
	public static BufferedImage pdfToImage(File pdf_file) {
		File f = pdf_file;
		if(f==null) return null;
		ProcessBuilder pb = new ProcessBuilder("pdftoppm",  "-png",
				f.getAbsolutePath());
		BufferedImage bi = null;
		try {
			Process p = pb.start();
			bi = ImageIO.read(p.getInputStream());
			StreamPrinter fluxSortie = new StreamPrinter(p.getInputStream(), true);
			StreamPrinter fluxErreur = new StreamPrinter(p.getErrorStream(), true);
			Task.startUntracked(fluxSortie);
			Task.startUntracked(fluxErreur);
			p.waitFor();
			
		} catch (InterruptedException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return bi;
	}
	
	public static BufferedImage toImage(String latex) {

		String math = latex;
		File f = toPdf(math);
		BufferedImage b = pdfToImage(f);
		f.delete();
		return b;
	}
	

	public static BufferedImage snipImage(String latex) {
		String newLineWithSeparation = System.getProperty("line.separator") + System.getProperty("line.separator");

		String math = "";
		math += "\\documentclass[preview,crop,border=1pt,convert]{standalone}" + newLineWithSeparation;
		math += "\\usepackage{amsfonts}" + newLineWithSeparation;
		math += "\\usepackage{amsmath}" + newLineWithSeparation;
		math += "\\begin{document}" + newLineWithSeparation;
		math += latex + newLineWithSeparation;
		math += "\\end{document}";
		return toImage(math);
	}


	public static BufferedImage snipMathImage(String math) {
		return snipImage("$" + math + "$");
	}
	

	private static void cleanUp(String TEMP_DIRECTORY, String TEMP_TEX_FILE_NAME) {
		// 5. Delete files
		for (File file : (new File(TEMP_DIRECTORY + File.separator + "tex").listFiles())) {
			if (file.getName().startsWith(TEMP_TEX_FILE_NAME + ".")) {
				//file.delete();
			}
		}
	}
}
