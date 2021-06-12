package TeXCalc.config;

import java.awt.Component;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;

@JsonAutoDetect
@Data
public abstract class ConfElement<T> implements Displayable,Defaultable<T>
{
	T defaultValue;
	//T oldValue;
	T value;
	
	public abstract Component display();
	public abstract void value_to_gui();
	public abstract void gui_to_value();
	
	public void setValue(T v) {
		this.value = v;
		value_to_gui();
	}

}
