package TeXCalc.gui;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;

import TeXCalc.latex.Latex;

public class Main extends JFrame {
	CellList cl = null;
	JScrollPane jsp = null;
	JTextField savename;
	public Main() {
		
		this.setLayout(new BorderLayout());
		

		JMenuBar menubar = new JMenuBar();
		JMenu menu = new JMenu("A menu");
		menubar.add(menu);

		this.setJMenuBar(menubar);
		
		JToolBar toolBar = new JToolBar("Still draggable");
		addButtons(toolBar);
		
		this.add(toolBar, BorderLayout.PAGE_START);
		
		this.add(jsp =new JScrollPane(cl = new CellList(11)));
		this.setVisible(true);
		this.pack();
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
	}

	protected void addButtons(JToolBar toolBar) {
		JButton button = null;
		
		savename = GUI.textEdit("save.json");
		
		toolBar.add(savename);
		
		button = GUI.buttonSync("Save", () -> save());
		toolBar.add(button);
		
		button = GUI.buttonAsync("Export", () -> export());
		toolBar.add(button);

		button = GUI.buttonAsync("Load", () -> load());
		toolBar.add(button);
		

		button = GUI.buttonAsync("Refresh", () -> update());
		toolBar.add(button);

		button = GUI.buttonAsync("Add Cell", () -> addCell());
		toolBar.add(button);
		// TODO Settings
	}
	
	public void export() {
		String tex = cl.toLaTeX();
		PrintWriter writer;
		try {
			writer = new PrintWriter("export.tex", "UTF-8");
			writer.print(tex);
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File f = Latex.toPdf(tex);
		try {
			if(f != null && f.exists())
				Files.move(f  ,  new File(System.getProperty("user.dir") + File.separator + "export.pdf"));
		} catch (IOException e) {
			// TODO Auto-generated #catch block
			e.printStackTrace();
		}
		
		try {
			ImageIO.write(Latex.pdfToImage(new File(System.getProperty("user.dir") + File.separator + "export.pdf")), "png", new File(System.getProperty("user.dir") + File.separator + "export.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void addCell() {
		cl.increase();
		this.pack();
		this.repaint();
	}

	public void save() {

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new File(savename.getText()),cl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void load() {
		final JFileChooser fc = new JFileChooser(new File(System.getProperty("user.dir")));
		 int returnVal = fc.showOpenDialog(this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            ObjectMapper objectMapper = new ObjectMapper();
	            try {
					Cell[] cells = objectMapper.readValue(file, Cell[].class);
					cl = new CellList(cells);
					this.remove(jsp);
					this.add(jsp = new JScrollPane(cl));
					this.pack();
					this.repaint();
	            } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            //This is where a real application would open the file.
	            GUI.log.m("Opening: " + file.getName() + ".");
	        } else {
	            GUI.log.m("Open command cancelled by user.");
	        }
	}

	public void update() {
		cl.update();
	}
}
