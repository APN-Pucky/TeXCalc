package TeXCalc.config.conf;

import java.awt.Component;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import TeXCalc.gui.GUI;
@JsonAutoDetect
public class ConfString extends ConfElement<String>{
	JTextArea jt;
	public ConfString() {
	}
	public ConfString(String v) {
		this.defaultValue = v;
		this.value = this.defaultValue;
	}
	public void gui_to_value() {
		if(jt!=null)this.value = jt.getText();
	}
	public void value_to_gui() {
		if(jt!=null)jt.setText(value);
	}
	@Override
	public Component display() {
		if(jt == null) {
		jt = GUI.area(value);
		jt.getDocument().addDocumentListener(new DocumentListener() {
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
		return jt;
	}
}
