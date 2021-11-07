// src: https://tex.stackexchange.com/a/167133
package TeXCalc.latex;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.io.Files;

import TeXCalc.config.Config;
import TeXCalc.exec.Exec;
import TeXCalc.gui.GUI;
import TeXCalc.mathematica.Mathematica;
import TeXCalc.util.IO;
import TeXCalc.util.Task;
import lombok.Getter;
import lombok.Setter;


public class Latex {
	//public static Latex _default = new Latex();
	public static boolean PRINT = false;
	public static boolean TIME = false;
	//public static String TEXENGINE = "lualatex";
	public static String TYPE_STANDALONE = IO.loadFile("type_standalone.tex");
	public static String TYPE_DOCUMENT = IO.loadFile("type_document.tex");

	public static String FRAMETOP = IO.loadFile("frametop.tex");
	public static String FRAMEEND = IO.loadFile("frameend.tex");

	@Getter 
	@Setter
	public HashMap<String,String> filecache = new HashMap<String,String>();


	private JTextArea engine ;
	private JTextArea standaloneType ;
	private JTextArea documentType;
	private JTextArea top;
	private JTextArea end;

	public String getTop() { return top.getText();}
	public String getEnd() { return end.getText();}
	public String getStandaloneType() { return standaloneType.getText();}
	public String getDocumentType() { return documentType.getText();}
	public String getEngine() { return engine.getText();}
	
	public void setTop(String a) { top.setText(a);}
	public void setEnd(String a) { end.setText(a);}
	public void setStandaloneType(String a) { standaloneType.setText(a);}
	public void setDocumentType(String a) {  documentType.setText(a);}
	public void setEngine(String a) { engine.setText(a);}

	@JsonIgnore
	private JPanel panel;
	
	public Latex() {
		this(FRAMETOP, FRAMEEND);
	}
	

	@JsonCreator
	public Latex(@JsonProperty("top") String top,@JsonProperty("end") String end) {
		panel = new JPanel();
		
		this.top = GUI.areaLatex(top);
		this.end = GUI.areaLatex(end);
		engine = GUI.area(Config.current.getLatex().getDefaultEngine().getValue());
		standaloneType = GUI.areaLatex(TYPE_STANDALONE);
		documentType = GUI.areaLatex(TYPE_DOCUMENT);

		panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
		panel.add(engine);
		panel.add(new JSeparator());
		panel.add(standaloneType);
		panel.add(new JSeparator());
		panel.add(documentType);
		panel.add(new JSeparator());
		panel.add(this.top);
		panel.add(new JSeparator());
		panel.add(this.end);
	}
	public void cache(String key, String f) {
		//System.out.println("Cached " + f);
		if(new File(f).exists()) {
		try {
					filecache.put(key, IO.encodeFileToBase64Binary(f));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	public void cache(String key, File f) {
		//System.out.println("Cached " + f);
		if(f.exists()) {
		try {
					filecache.put(key, IO.encodeFileToBase64Binary(f.getPath()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	public void cache(File f) {
		//System.out.println("Cached " + f);
		if(f.exists()) {
		try {
					filecache.put(f.getPath(), IO.encodeFileToBase64Binary(f.getPath()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	public void cache(String f) {
		//System.out.println("Cached " + f);
		if(new File(f).exists()) {
		try {
					filecache.put(f, IO.encodeFileToBase64Binary(f));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public void cleanCache(String cont) {
		Iterator<String> it =filecache.keySet().iterator();
		while(it.hasNext()) {
			String f = it.next();
			if(!cont.contains(f.split("\\.")[0]))it.remove();
		}
	}
	
	public void pasteCache(String dir) {
		if(!new File(dir).isDirectory()) throw new RuntimeException("Can only paste Cache in directory");
		for(String f : filecache.keySet()) {
			File ff = new File(f);
			if(ff.exists()) {
				try {
					//System.out.println("copied");
					File fi = (new File(dir+ File.separator+ f));
					fi.getParentFile().mkdirs();
					Files.copy(ff,fi);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cache(f);
				
			}
			else {
			byte[] decodedImg = Base64.getDecoder()
                    .decode(filecache.get(f).getBytes(StandardCharsets.UTF_8));
			try {
				File fn = new File (dir+ File.separator + f);
				fn.getParentFile().mkdirs();
				fn.createNewFile();
				FileOutputStream fos = new FileOutputStream(fn);
				fos.write(decodedImg);
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
	}
	
	public File toPdf(String latex) {
		String filename = "export";
		Exec ex = new Exec("tex");
		IO.writeFile(ex.getDirName() + filename + ".tex", latex);
		pasteCache(ex.getDirName());
		ex.exec(getEngine(),  "-halt-on-error","--shell-escape",filename+ ".tex");

		File ret_file = new File(ex.getDirName() + File.separator +".."+ File.separator + filename+ ".pdf");
		
		try {
			File tmp = new File(ex.getDirName() + File.separator + filename + ".pdf");
			if(tmp.exists()) {
				Files.move(tmp,ret_file);
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

		//cleanUp(, TEMP_TEX_FILE_NAME);

		return ret_file;
	}
	
	public File toPdf2(String latex) {
		String uuid = UUID.randomUUID().toString();
		String TEMP_DIRECTORY = ".tmp" + File.separator + uuid;
		String TEMP_TEX_FILE_NAME = "export"; // for New22.tex
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
		
		for(String f : filecache.keySet()) {
			File ff = new File(f);
			if(ff.exists()) {
				try {
					System.out.println("copied");
					File fi = (new File(TEMP_DIRECTORY + File.separator  + "tex" + File.separator+ f));
					fi.getParentFile().mkdirs();
					Files.copy(ff,fi);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cache(f);
				
			}
			else {
			byte[] decodedImg = Base64.getDecoder()
                    .decode(filecache.get(f).getBytes(StandardCharsets.UTF_8));
			try {
				File fn = new File (TEMP_DIRECTORY + File.separator + "tex" + File.separator + f);
				fn.getParentFile().mkdirs();
				fn.createNewFile();
				FileOutputStream fos = new FileOutputStream(fn);
				fos.write(decodedImg);
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
		
		System.out.print("  3. Execute LaTeX {" + uuid + "} from command line  to generate picture = ");
		ProcessBuilder pb = new ProcessBuilder(getEngine(),  "-halt-on-error",
				TEMP_TEX_FILE_NAME + ".tex");
		pb.directory(new File(TEMP_DIRECTORY + File.separator + "tex"));
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
	
	public BufferedImage pdfToImage(File pdf_file) {
		File f = pdf_file;
		if(f==null) return null;
		ProcessBuilder pb = new ProcessBuilder("pdftoppm",  "-png",
				f.getAbsolutePath());
		BufferedImage bi = null;
		try {
			Process p = pb.start();
			bi = ImageIO.read(p.getInputStream());
			if(PRINT) {
			StreamPrinter fluxSortie = new StreamPrinter(p.getInputStream(), true);
			StreamPrinter fluxErreur = new StreamPrinter(p.getErrorStream(), true);
			Task.startUntracked(fluxSortie);
			Task.startUntracked(fluxErreur);
			}
			p.waitFor();
			
		} catch (InterruptedException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return bi;
	}
	
	public BufferedImage toImage(String latex) {

		String math = latex;
		File f = toPdf(math);
		BufferedImage b = pdfToImage(f);
		if(f!= null)
			f.delete();
		return b;
	}
	

	public BufferedImage snipImage(String latex) {
		String newLineWithSeparation = System.getProperty("line.separator") + System.getProperty("line.separator");
		String math = getStandaloneType();
		math += getTop();
		math += latex + newLineWithSeparation;
		math += getEnd();
		return toImage(math);
	}


	public BufferedImage snipMathImage(String math) {
		return snipImage("$" + math + "$");
	}
	

	private void cleanUp(String TEMP_DIRECTORY, String TEMP_TEX_FILE_NAME) {
		// 5. Delete files
		new File(TEMP_DIRECTORY).deleteOnExit();
		/*
		for (File file : (new File(TEMP_DIRECTORY + File.separator + "tex").listFiles())) {
			if (file.getName().startsWith(TEMP_TEX_FILE_NAME + ".")) {
				file.deleteOnExit();
			}
		}
		*/
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
	
	public static String begin(String env) {
		return "\\begin{" + env + "}";
	}
	public static String end(String env) {
		return "\\end{" + env + "}";
	}
	public void checkRequirements(String[] requirements) {
		for(String r : requirements) {
			if(!getTop().contains(r)) {
				System.out.println("Required: " + r);
				//TODO popup?
			}
		}
		// TODO move this somewhere else?
		if(getTop().contains("mmacells") && !filecache.containsKey("mmacells.sty")) {
			Exec ex = new Exec("mathematica", false);
			ex.writeFile("mmacells.sty", Mathematica.mmacells);
			cache("mmacells.sty", new File(ex.getDirName() + "mmacells.sty"));
		}
		
	}
}
