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
public class GeneralConfig extends SubConfig {
	ConfInteger maxThreads = new ConfInteger(-1);
	ConfInteger threadsTimeoutSeconds = new ConfInteger(30);
	
}
