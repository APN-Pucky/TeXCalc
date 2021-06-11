package TeXCalc.debug;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

public class DebugResources {
 public static void main(String[] args) {
	 InputStream in = DebugResources.class.getResourceAsStream("/mmacells.sty");
	 try {
		System.out.println(IOUtils.toString(in, StandardCharsets.UTF_8.name()));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 }
}
