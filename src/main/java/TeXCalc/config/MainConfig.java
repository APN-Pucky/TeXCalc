package TeXCalc.config;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import TeXCalc.config.conf.ConfElement;
import TeXCalc.config.sub.DebugConfig;
import TeXCalc.config.sub.GUIConfig;
import TeXCalc.config.sub.HotkeyConfig;
import TeXCalc.config.sub.LatexConfig;
import TeXCalc.config.sub.MathematicaConfig;
import TeXCalc.config.sub.PythonConfig;
import lombok.Data;

@JsonAutoDetect
@Data
public class MainConfig extends Config{
	//public static MainConfig current = new MainConfig();
	//public static final String configfile = "main_conf.json";
	
	GUIConfig gui = new GUIConfig();
	LatexConfig latex = new LatexConfig();
	PythonConfig python3 = new PythonConfig();
	MathematicaConfig math = new MathematicaConfig();
	DebugConfig debug = new DebugConfig();
	HotkeyConfig hotkey = new HotkeyConfig();
	
	@JsonIgnore
	private HashMap<String,Object> postponed = new HashMap<String,Object>(); 

	public void show() {
		JFrame f = new JFrame();
		JPanel j = new JPanel();
		j.setLayout(new BoxLayout(j, BoxLayout.Y_AXIS));
		JTabbedPane tab = new JTabbedPane();
		for (Field field : this.getClass().getDeclaredFields())
		{
			field.setAccessible(true);
			try {
				if(field.get(this) instanceof SubConfig)
				{
					try {
						tab.addTab(field.getName(),null,((SubConfig)field.get(this)).display());
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
					{
					//System.out.println("unknown " + field.getType().getName());
					}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JPanel jb = new JPanel();
		JButton saveButton = new JButton("Save permanent");
	    saveButton.addActionListener(new ActionListener() {

	        @Override
	        public void actionPerformed(ActionEvent arg0) {
	        	save();
	            //saveText(areaText);
	        	f.dispose();
	        }
	    });	
	    JButton resetButton = new JButton("Reset all");
	    resetButton.addActionListener(new ActionListener() {

	        @Override
	        public void actionPerformed(ActionEvent arg0) {
	        	reset();
	        	save();
	        	f.dispose();
	        }
	    });	
	    j.add(tab);
	    jb.add(resetButton);
	    jb.add(saveButton);
	    j.add(jb);
		f.add(j);
		f.pack();
		f.setVisible(true);
	}
	
	public void save() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			//ObjectNode r = objectMapper.createObjectNode();
			//r.set("config", objectMapper.valueToTree(Config.current));
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(System.getProperty("user.dir") +System.getProperty("file.separator")+ MainConfig.configfile),MainConfig.current);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	

	@JsonAnySetter
	public void setAnyValue(String key,Object value) {
		postponed.put(key, value);
	}
	public void setValue(String key,Object value) {
		for (Field field : this.getClass().getDeclaredFields())
		{
			field.setAccessible(true);
				try {
					if(field.get(this) instanceof SubConfig)
					{
						for(Field sf : field.get(this).getClass().getDeclaredFields())
						{
							sf.setAccessible(true);
							if(sf.getName().equals(key))
							{
								((ConfElement)sf.get(field.get(this))).setValue(value);
								System.out.println(sf + " " + value + " " + gui.getBackgroundColor().getValue());
							}
							System.out.println(sf.getName());
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	
	}
	
	public void load() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
        	Config.current = objectMapper.readValue(new File(System.getProperty("user.dir") +System.getProperty("file.separator")+ Config.configfile), MainConfig.class);
        }
		catch (IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        for(String s : current.postponed.keySet()) {
        	//System.out.println("Fixing: " + s);
        	Config.current.setValue(s, current.postponed.get(s));
        }
	}
}
