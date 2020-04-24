package external;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class StreamPrinter implements Runnable {

    // Source: http://labs.excilys.com/2012/06/26/runtime-exec-pour-les-nuls-et-processbuilder/
    private final InputStream inputStream;

    private boolean print;

    StreamPrinter(InputStream inputStream, boolean print) {
        this.inputStream = inputStream;
        this.print = print;
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
                    System.out.println(ligne);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
