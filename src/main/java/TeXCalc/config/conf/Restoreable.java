package TeXCalc.config.conf;

import java.util.ArrayList;

public interface Restoreable<T> extends Valueable<T>{
	public ArrayList<T> getOldValues();
	public void setOldValues(ArrayList<T> d);
	public default void restore() {
		setValue(getOldValues().get(getOldValues().size()-1));
	}
}
