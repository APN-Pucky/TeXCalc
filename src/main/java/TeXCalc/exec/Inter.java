package TeXCalc.exec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Inter {
	public static final boolean PRINT = true;
	ProcessBuilder pb;
	Process p;
	public Inter(String... command) throws IOException {
		pb = new ProcessBuilder(command);
			p = pb.start();
	}
	public String pr(String in) {
		System.out.println("PR: " + in);
		return in;
	}
	public String input(String in) {
		PrintWriter pr = new PrintWriter(p.getOutputStream());
		pr.println(in);
		pr.flush();
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String ligne = "";
        String text = "";
        boolean reset = false;
        char[] c = new char[1];
        while(true) {
        	try {
				br.read(c,0,1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	//System.out.print(c[0]);
        	text += c[0];
        	//System.out.println(text);
        	String[] ar;
        	if(text.contains("\n")) {
        		ar = text.split("\n");
        		if(ar.length == 0)
        			ar = new String[] {""};
        	}
        	else {
        		ar = new String[] {text};
        	}
        	if(ar[ar.length-1].matches(".*Out\\[\\d+\\]=.*")) {
        		//System.out.println("RESET");
        		text = "";
        		reset= true;
        	}
        	if(reset && ar[ar.length-1].matches(".*In\\[\\d+\\]:=.*")) {
        		//System.out.println("BREAK");
        		text ="";
        		for(int i =0; i < ar.length-2;++i) {
        			text += ar[i] + "\n";
        		}
        		break;
        	}
        }
     return text.replace("In\\[\\d+\\]:=","");
	}
}
