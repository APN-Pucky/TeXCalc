package TeXCalc.config;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import TeXCalc.config.conf.Displayable;
import TeXCalc.gui.GUI;
import lombok.Data;

@JsonAutoDetect
@Data
public class SubConfig extends Config implements Displayable{
	public String info ="";

	public Component display() {

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
				if(field.get(this) == null) {
					try {
						field.set(this, field.get(this.getClass().getDeclaredConstructor().newInstance()));
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(field.getType().getName().equals("java.lang.String"))
					j.add(link(GUI.area(field.get(this).toString()),field,this),c);
				if(field.getType().getName().equals("java.lang.Integer"))
					j.add(link(GUI.numericEdit((Integer)field.get(this)),field,this),c);
				if(field.getType().getName().equals("java.lang.Boolean"))
					j.add(link(GUI.check("",(Boolean)field.get(this)),field,this),c);
				if(field.get(this) instanceof Displayable)
					j.add(((Displayable)field.get(this)).display(),c);
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
		JButton resetButton = new JButton("Reset");
	    resetButton.addActionListener(new ActionListener() {

	        @Override
	        public void actionPerformed(ActionEvent arg0) {
	        	reset();
	        }
	    });	
		JButton defButton = new JButton("Save default");
	    defButton.addActionListener(new ActionListener() {

	        @Override
	        public void actionPerformed(ActionEvent arg0) {
	        	save_default();
	        }
	    });	
	    JPanel tmp = new JPanel();
	    tmp.add(resetButton);
	    tmp.add(defButton);
	    j.add(tmp,c);
		
		return new JScrollPane(j);
	}
}
