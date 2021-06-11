package TeXCalc.debug;

import java.io.IOException;

import TeXCalc.exec.Inter;
import TeXCalc.util.IO;

public class DebugInter {
public static void main(String... args) throws IOException {
	System.out.println("".matches("In\\[\\d+\\]:=.*"));
	Inter i = new Inter("/home/apn/.local/bin/math");
	System.out.println(i.input(IO.loadFile("run.m")));
	System.out.println(i.input("NBEval[\"/home/apn/git/TeXCalc/src/main/resources/tmp.m\"]"));
	System.out.println(i.input("a*b*c"));
	System.out.println(i.input("a*a*a"));
	System.out.println("done");
	System.out.println("Out[1]=".matches("Out\\[1\\]="));
}
}
