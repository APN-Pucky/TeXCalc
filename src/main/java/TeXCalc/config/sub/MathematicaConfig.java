package TeXCalc.config.sub;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import TeXCalc.config.SubConfig;
import lombok.Data;

@JsonAutoDetect
@Data
public class MathematicaConfig extends SubConfig {
	String mathematicaPATH  = "math";
}
