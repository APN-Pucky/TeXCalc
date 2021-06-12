package TeXCalc.config.conf;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import TeXCalc.gui.GUI;

public class ConfBoolean extends ConfElement<Boolean> {
	JCheckBox j;
	public ConfBoolean() {
		
	}
	public ConfBoolean(boolean b) {
		this.value = b;
		this.defaultValue = b;
	}
	@Override
	public Component display() {
		if(j ==null) {
			j = GUI.check("",value);
			j.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					gui_to_value();
				}
				
			});
		}
		return j;
	}

	@Override
	public void value_to_gui() {
		// TODO Auto-generated method stub
		if(j!=null)j.setSelected(value);
	}

	@Override
	public void gui_to_value() {
		// TODO Auto-generated method stub
		if(j!=null)value = j.isSelected();
	}

}
