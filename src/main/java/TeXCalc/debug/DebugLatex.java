package TeXCalc.debug;

import TeXCalc.latex.Latex;

public class DebugLatex {
	public static void main(String[] args) {
		DebugFrame.showImage(Latex.toMathImage("a^2+b^2=c^2"));
	}
}
