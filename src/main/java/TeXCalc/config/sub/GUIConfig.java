package TeXCalc.config.sub;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import TeXCalc.config.SubConfig;
import TeXCalc.config.conf.ConfInteger;
import TeXCalc.config.conf.ConfString;
import lombok.Data;

@JsonAutoDetect
@Data
public class GUIConfig extends SubConfig{
	ConfString theme= new ConfString("dark");
	ConfString backgroundColor = new ConfString("#2f2f2f");
	ConfInteger numberOfLines = new ConfInteger(6);
	ConfInteger widthOfLines = new ConfInteger(100);
}
