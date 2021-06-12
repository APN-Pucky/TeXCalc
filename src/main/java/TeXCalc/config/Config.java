package TeXCalc.config;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

import TeXCalc.gui.GUI;
import lombok.Data;

@JsonAutoDetect
@Data
public class Config {
	public static MainConfig current = new MainConfig();
	public static final String configfile = "conf.json";
	@JsonIgnore
	private JFrame f;
	//String python = "python3.7";
	//String defaultEngine = "lualatex";
	//String theme= "dark";
	//String environment = "aligned";
	//String backgroundColor = "#2f2f2f";
	//Integer numberOfLines = 6;
	//Integer widthOfLines = 100;
	//Boolean autoLeftRightBracket = true;
	//String mathematicaPATH  = "math";
	
	public void show() {
		f = new JFrame();
		JPanel j = new JPanel();
		j.setLayout(new GridBagLayout());
		int index = 0;
		for (Field field : this.getClass().getDeclaredFields())
		{		
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.NONE;
			c.weightx = 0.3;
			c.weighty = 0.1;
			c.gridx = 1;
			c.gridy = index;
			j.add(GUI.text(field.getName()),c);
		
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.7;
			c.weighty = 0.1;
			c.gridx = 2;
			c.gridy = index;

			field.setAccessible(true);
			try {
				if(field.getType().getName().equals("java.lang.String"))
					j.add(link(GUI.area(field.get(this).toString()),field,this),c);
				if(field.getType().getName().equals("java.lang.Integer"))
					j.add(link(GUI.numericEdit((Integer)field.get(this)),field,this),c);
				if(field.getType().getName().equals("java.lang.Boolean"))
					j.add(link(GUI.check("",(Boolean)field.get(this)),field,this),c);
			} catch (IllegalArgumentException  e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			index++;
		}
		GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.7;
			c.weighty = 0.1;
			c.gridx = 2;
			c.gridy = index++;
		JButton saveButton = new JButton("Save");
	    saveButton.addActionListener(new ActionListener() {

	        @Override
	        public void actionPerformed(ActionEvent arg0) {
	        	//save();
	            //saveText(areaText);
	        	f.dispose();
	        }
	    });	
	    j.add(saveButton,c);
		f.add(new JScrollPane(j));
		f.pack();
		f.setVisible(true);
	}
	
	public Component link(JCheckBox c,Field f,Object o) {
		c.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				update(c,f,o);
			}
			
		});
		return c;
	}
	public Component link(JTextComponent c,Field f,Object o)  {
		
		c.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
				    update(c,f,o);
				  }
				  public void removeUpdate(DocumentEvent e) {
				    update(c,f,o);
				  }
				  public void insertUpdate(DocumentEvent e) {
				    update(c,f,o);
				  }
		});
		return c;
	}
	public void update(JCheckBox c, Field f,Object o) {
		  if(f.getType().getName().equals("java.lang.Boolean"))
			try {
				f.set(o, c.isSelected());
			}   catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  }
	public void update(JTextComponent c, Field f,Object o) {
					  if(f.getType().getName().equals("java.lang.String"))
						try {
							f.set(o, c.getText());
						} catch (IllegalArgumentException | IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					  if(f.getType().getName().equals("java.lang.Integer"))
						try {
							f.set(o, Integer.parseInt(c.getText()));
						} catch(NumberFormatException e)
					   {
					   }
					  catch (IllegalArgumentException | IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				  }
	
	public void reset() {
		for (Field field : this.getClass().getDeclaredFields())
	        	{
				field.setAccessible(true);
						try {

							if(field.get(this) instanceof Defaultable)
								((Defaultable)field.get(this)).reset();
							else
								field.set(this, field.get(this.getClass().getDeclaredConstructor().newInstance()));
						} catch (IllegalArgumentException | IllegalAccessException | InstantiationException
								| InvocationTargetException | NoSuchMethodException | SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

	        	}
	}
	public void save_default() {
		for (Field field : this.getClass().getDeclaredFields())
	        	{
				field.setAccessible(true);
						try {

							if(field.get(this) instanceof Defaultable)
								((Defaultable)field.get(this)).setDefaultValue(((Valueable)field.get(this)).getValue());;
						} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

	        	}
	}
}
