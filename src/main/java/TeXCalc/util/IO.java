package TeXCalc.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import org.apache.commons.io.FileUtils;

public class IO {
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
