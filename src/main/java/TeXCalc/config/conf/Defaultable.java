package TeXCalc.config.conf;

import lombok.Getter;
import lombok.Setter;

public interface Defaultable<T> extends Valueable<T> {
	public T getDefaultValue();
	public void setDefaultValue(T d);
	public default void reset() {
		setValue(getDefaultValue());
	}
}
