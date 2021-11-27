package TeXCalc.config.sub;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import TeXCalc.config.Config;
import TeXCalc.config.SubConfig;
import TeXCalc.config.conf.ConfBoolean;
import TeXCalc.config.conf.ConfInteger;
import TeXCalc.config.conf.ConfString;
import lombok.Data;

@JsonAutoDetect
@Data
public class LatexConfig extends SubConfig {
	ConfString defaultEngine = new ConfString("lualatex");
	ConfInteger maxThreads = new ConfInteger(-1);
	ConfString environment = new ConfString("auto");
	ConfBoolean autoLeftRightBracket =new ConfBoolean(true);
	ConfBoolean autoLeftRightAbs = new ConfBoolean(true);
	ConfBoolean autoExport = new ConfBoolean(true);
	
}
