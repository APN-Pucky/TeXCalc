package TeXCalc.config.sub;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import TeXCalc.config.SubConfig;
import lombok.Data;

@JsonAutoDetect
@Data
public class LatexConfig extends SubConfig {
	String defaultEngine = "lualatex";
	String environment = "aligned";
	Boolean autoLeftRightBracket = true;
	
}
