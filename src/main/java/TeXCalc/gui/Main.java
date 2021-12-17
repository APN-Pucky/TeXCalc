package TeXCalc.gui;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;

import TeXCalc.compat.Compat;
import TeXCalc.config.Config;
import TeXCalc.util.HotKey;
import TeXCalc.util.Task;
import de.neuwirthinformatik.Alexander.GitJarUpdate.Info;
import de.neuwirthinformatik.Alexander.GitJarUpdate.Update;
import de.neuwirthinformatik.Alexander.GitJarUpdate.Version;
import lombok.Getter;
import lombok.Setter;

public class Main {
	
	@Getter
	CellList celllist= null;
	JTabbedPane tp= null;
	JScrollPane jsp = null;
	JTextField savename;
	@Getter @Setter
	String version = "DEV";
	JFrame jframe;
	String tmp_save = "tmp_save.json~";
	Thread cur;
	static { loadConfig(); }
	
	public Main() {
		//FlatLaf.install(new FlatLightLaf());
		if(Config.current.getGui().getTheme().getValue().equals("dark"))LafManager.install(new DarculaTheme());
				//GUI.setUIFont (new javax.swing.plaf.FontUIResource("Serif",Font.BOLD,15));

		Update.loadUpdate("TeXCalc-all.jar", "APN-Pucky", "TeXCalc");
		File tf = new File(".tmp");
		try {
			FileUtils.deleteDirectory(tf);
		} catch (IOException e) {
			// TODO Auto-generatd catch block
			e.printStackTrace();
		}
		if(!tf.exists() || !tf.isDirectory())tf.mkdir();
		version = Info.VERSION;
		jframe = new JFrame("TeXCalc-" + Info.VERSION);
		jframe.setLayout(new BorderLayout());

		JMenuBar menubar = new JMenuBar();
		JMenu menu = new JMenu("A menu");
		menubar.add(menu);
		
		JToolBar toolBar = new JToolBar("Still draggable");
		try {
			addButtons(toolBar);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}



		jframe.add(toolBar, BorderLayout.PAGE_START);
		celllist = new CellList(0);
		celllist.setMain(this);
		refreshTabs();
		
		jframe.setResizable(true);
		//jframe.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		//jframe.setUndecorated(true);
		jframe.setVisible(true);
		jframe.pack();
		jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		load(tmp_save);
		

		
		Task.startUntracked(() -> {
			while(true) {
				Task.sleep(5000);
				save(tmp_save);
			}
		});
	}
	
	private void refreshTabs() {
		if(tp!=null)jframe.remove(tp);
		tp= new JTabbedPane();
		tp.addTab("Notebook",null,jsp =new JScrollPane(celllist.getPanel()
				,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
	            ),"");
		jsp.getVerticalScrollBar().setUnitIncrement(20);
		tp.addTab("Settings",null,jsp =new JScrollPane(celllist.getLatex().getPanel()),"");
		jsp.getVerticalScrollBar().setUnitIncrement(20);
		jframe.add(tp);
	}

	protected void addButtons(JToolBar toolBar) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		JButton button = null;
		
		savename = GUI.text("save.json",true,100);
		
		toolBar.add(savename);
		
		button = GUI.buttonSync("Save", () -> save());
		toolBar.add(button);
		
		button = GUI.buttonAsync("Export", () -> export());
		toolBar.add(button);

		button = GUI.buttonAsync("Load", () -> load());
		toolBar.add(button);

		button = GUI.buttonAsync("Remove All", () -> celllist.rmAll());
		toolBar.add(button);

		button = GUI.buttonAsync("Refresh", () -> update());
		button.setMnemonic(KeyEvent.class.getField("VK_" + Config.current.getHotkey().getRefresh().getValue()).getInt(null));
		toolBar.add(button);

		button = GUI.buttonAsync("Add Cell", () -> addCell());
		toolBar.add(button);
		
		button = GUI.buttonAsync("Config", () -> config());
		toolBar.add(button);
		//button = GUI.buttonAsync("Settings", () -> celllist.getLatex().settings()); // todo top und end, changes
		//toolBar.add(button);
		// TODO Settings
		//toolBar.add(GUI.text("lualatex",true,100));
		
	}
	
	public void export() {
		Task.stop(cur);
		cur = Task.append(() -> {
		File ef =  new File(System.getProperty("user.dir") + File.separator + "export");
		if( ef.exists() )
		{
		//deleteDirectory(System.getProperty("user.dir") + File.separator + "export");
		try {
			//FileUtils.deleteDirectory(ef);
			FileUtils.cleanDirectory(ef);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}
		String tex = celllist.toLaTeX();
		celllist.getLatex().cleanCache(tex);
		File f = celllist.getLatex().toPdf(tex);
		try {
			if(f != null && f.exists())
			{
				FileUtils.copyDirectory(f.getParentFile(), ef);
			
			}
		} catch (IOException e) {
			// TODO Auto-generated #catch block
			e.printStackTrace();
		}

		});
	}
	public static void deleteDirectory(String dir) {
	    try {
			Files.walk(Paths.get(dir))
				      .forEach(source -> {
				    	 try {
							FileUtils.deleteDirectory(source.toFile());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				      });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation) 
			  throws IOException {
			    Files.walk(Paths.get(sourceDirectoryLocation))
			      .forEach(source -> {
			          Path destination = Paths.get(destinationDirectoryLocation, source.toString()
			            .substring(sourceDirectoryLocation.length()));
			          try {
			              Files.copy(source, destination);
			          } catch (IOException e) {
			              e.printStackTrace();
			          }
			      });
			}
	
	public void addCell() {
		celllist.increase();
		//jframe.pack();
		//jframe.repaint();
	}

	public void save() {
		if(!(new File(savename.getText())).exists() || GUI.confirm(jframe,"overwrite " + savename.getText() + "?","overwrite " + savename.getText() + "?" ) ) {
			save(savename.getText());
		}
	}
	
	public void save(String filename) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			ObjectNode r = objectMapper.createObjectNode();
			r.put("version", version);
			r.set("celllist", objectMapper.valueToTree(celllist));
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename),r);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void config() {
		Config.current.show();
		
	}
	
	public static void loadConfig() {
		Config.current.load();
	}

	public void load() {
		final JFileChooser fc = new JFileChooser(new File(System.getProperty("user.dir")));
		 int returnVal = fc.showOpenDialog(jframe);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            load(file);
	            savename.setText(file.getAbsolutePath());
	        } else {
	            GUI.log.m("Open command cancelled by user.");
	        }
	}
	
	public void load(File file) {
		ObjectMapper objectMapper = new ObjectMapper();
        try {
        	JsonNode n = objectMapper.readTree(Compat.fix(file));
        	if(new Version(n.get("version").asText()).compareTo( new Version(Info.VERSION)) > 0)
        	{
        		int r =JOptionPane.showConfirmDialog(jframe, "File version " + n.get("version").asText() +" > TeXCalc version "+Info.VERSION + "\nContinue?" , "Warning",
        		        JOptionPane.YES_NO_OPTION);
        		if(r == JOptionPane.NO_OPTION) return;
        	}
			CellList cl = objectMapper.readValue(n.get("celllist").toString(), CellList.class);
			cl.setMain(this);
			cl.linkAll();
			setCelllist(cl);
			refreshTabs();
			//jframe.remove(jsp);
			//jframe.add(jsp = new JScrollPane(celllist.getPanel()));
			jframe.pack();
			jframe.repaint();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //This is where a real application would open the file.
        GUI.log.m("Opening: " + file.getAbsolutePath() + ".");
	
	}
	public void load(String filename) {
			load(new File(filename));
	            
	}
	
	public void setCelllist(CellList cl) {
		celllist = cl;
		update();
	}

	public void update() {
		celllist.update();
		jframe.validate();
		jframe.pack();
		jframe.repaint();
	}
}
