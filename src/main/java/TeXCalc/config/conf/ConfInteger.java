package TeXCalc.config.conf;

import java.awt.Component;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import TeXCalc.gui.GUI;
import TeXCalc.gui.GUI.IntegerField;

public class ConfInteger extends ConfElement<Integer> {
	IntegerField f;

	public ConfInteger() {}
	public ConfInteger(Integer v) {
		this.value = v;
		this.defaultValue = v;
	}

	@Override
	public Component display() {
		// TODO Auto-generated method stub
		if (f == null) {
			f = GUI.numericEdit(value);
			f.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
				    gui_to_value();
				  }
				  public void removeUpdate(DocumentEvent e) {
				    gui_to_value();
				  }
				  public void insertUpdate(DocumentEvent e) {
				    gui_to_value();
				  }
				}
				);
		}
		return f;
	}

	@Override
	public void value_to_gui() {
		if(f!=null)f.setNumber(value);
	}

	@Override
	public void gui_to_value() {
		// TODO Auto-generated method stub
		if(f!=null)value = (f.getNumber());

	}

}
