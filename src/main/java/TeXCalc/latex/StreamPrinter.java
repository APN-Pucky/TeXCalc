package TeXCalc.latex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import TeXCalc.gui.GUI;

public class StreamPrinter implements Runnable {

    // Source: http://labs.excilys.com/2012/06/26/runtime-exec-pour-les-nuls-et-processbuilder/
    private final InputStream inputStream;
    public String text = "";
    public String addl = "";
    private boolean print;

    public StreamPrinter(InputStream inputStream, String addl, boolean print) {
        this.inputStream = inputStream;
        this.print = print;
        this.addl = addl;
    }

    private BufferedReader getBufferedReader(InputStream is) {
        return new BufferedReader(new InputStreamReader(is));
    }

    @Override
    public void run() {
        BufferedReader br = getBufferedReader(inputStream);
        String ligne = "";
        try {
            while ((ligne = br.readLine()) != null) {
                if (print) {
                    GUI.log.m(addl + " " +ligne);
                    text += ligne + "\n";
                }
                else {
                	text += ligne + "\n";
                }
            }
        } catch (IOException e) {
        	if(!e.getMessage().equals("Stream closed"))
        		e.printStackTrace();
        }
    }
}
