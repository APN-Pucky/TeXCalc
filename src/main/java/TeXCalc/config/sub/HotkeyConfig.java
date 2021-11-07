package TeXCalc.config.sub;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import TeXCalc.config.SubConfig;
import TeXCalc.config.conf.ConfString;
import lombok.Data;

@JsonAutoDetect
@Data
public class HotkeyConfig extends SubConfig {
	String info = "HotKeys are used in combination with mostly the Alt key";
	ConfString refresh = new ConfString("F5");

}
