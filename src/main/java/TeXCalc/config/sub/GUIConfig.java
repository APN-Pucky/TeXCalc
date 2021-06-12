package TeXCalc.config.sub;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import TeXCalc.config.SubConfig;
import lombok.Data;

@JsonAutoDetect
@Data
public class GUIConfig extends SubConfig{
	String theme= "dark";
	String backgroundColor = "#2f2f2f";
	Integer numberOfLines = 6;
	Integer widthOfLines = 100;
}
