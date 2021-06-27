package TeXCalc.config.sub;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import TeXCalc.config.SubConfig;
import TeXCalc.config.conf.ConfBoolean;
import TeXCalc.config.conf.ConfString;
import lombok.Data;

@JsonAutoDetect
@Data
public class MathematicaConfig extends SubConfig {
	ConfString mathematicaPATH  = new ConfString("math");
	ConfBoolean executeSubsequent = new ConfBoolean(false);
}
