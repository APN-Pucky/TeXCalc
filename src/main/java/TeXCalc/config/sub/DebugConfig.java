package TeXCalc.config.sub;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import TeXCalc.config.SubConfig;
import TeXCalc.config.conf.ConfBoolean;
import TeXCalc.config.conf.ConfString;
import lombok.Data;
@JsonAutoDetect
@Data
public class DebugConfig extends SubConfig {
	ConfBoolean printAll= new ConfBoolean(false);
	ConfString logFile = new ConfString("texcalc.log");
	//String ok = "";



}
