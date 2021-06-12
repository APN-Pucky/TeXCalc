package TeXCalc.config.sub;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import TeXCalc.config.SubConfig;
import lombok.Data;

@JsonAutoDetect
@Data
public class PythonConfig extends SubConfig{
	String python = "python3.7";
}
