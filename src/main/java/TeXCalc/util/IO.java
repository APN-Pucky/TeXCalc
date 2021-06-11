package TeXCalc.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class IO {
public static String loadFile(String name) {
	 InputStream in = IO.class.getResourceAsStream("/"+name);
	 try {
		return IOUtils.toString(in, StandardCharsets.UTF_8.name());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 return "";
}
public static String readFile(String path)
	{
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

public static String encodeFileToBase64Binary(String fileName) throws IOException {
    File file = new File(fileName);
    return Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file));
}
public static void writeFile(String path, String cont) {
FileWriter writer = null;
try {
		writer = new FileWriter(
				path, false);
		writer.write(cont, 0, cont.length());
		writer.close();
	} catch (IOException ex) {
		ex.printStackTrace();
	}
}
}
