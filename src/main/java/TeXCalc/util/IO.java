package TeXCalc.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
}
