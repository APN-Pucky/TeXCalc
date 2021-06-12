package TeXCalc.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@JsonAutoDetect
public interface Test {
	@Getter @Setter
	String v="";
}
