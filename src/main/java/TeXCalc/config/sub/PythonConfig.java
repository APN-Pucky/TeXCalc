package TeXCalc.config.sub;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import TeXCalc.config.SubConfig;
import TeXCalc.config.conf.ConfBoolean;
import TeXCalc.config.conf.ConfString;
import lombok.Data;

@JsonAutoDetect
@Data
public class PythonConfig extends SubConfig{
	ConfString python = new ConfString("python3.7");
	ConfBoolean showCells = new ConfBoolean(true);
}
