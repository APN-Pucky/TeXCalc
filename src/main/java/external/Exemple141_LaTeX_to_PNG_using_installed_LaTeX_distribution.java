// src: https://tex.stackexchange.com/a/167133
package external;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Exemple141_LaTeX_to_PNG_using_installed_LaTeX_distribution {

    public static void main(String[] args) throws IOException {

        String TEMP_DIRECTORY = ".tmp";
        String TEMP_TEX_FILE_NAME = UUID.randomUUID().toString(); // for New22.tex

        // 1. Prepare the .tex file
        String newLineWithSeparation = System.getProperty("line.separator")+System.getProperty("line.separator");
        String math = "";
        math += "\\documentclass[preview,crop,convert]{standalone}" + newLineWithSeparation;
        math += "\\usepackage{amsfonts}" + newLineWithSeparation;
        math += "\\usepackage{amsmath}" + newLineWithSeparation;
        math += "\\begin{document}" + newLineWithSeparation;
        math += "$\\begin{array}{l}" + newLineWithSeparation;
        math += "\\forall\\varepsilon\\in\\mathbb{R}_+^*\\ \\exists\\eta>0\\ |x-x_0|\\leq\\eta\\Longrightarrow|f(x)-f(x_0)|\\leq\\varepsilon\\\\" + newLineWithSeparation;
        math += "\\det\\begin{bmatrix}a_{11}&a_{12}&\\cdots&a_{1n}\\\\a_{21}&\\ddots&&\\vdots\\\\\\vdots&&\\ddots&\\vdots\\\\a_{n1}&\\cdots&\\cdots&a_{nn}\\end{bmatrix}\\overset{\\mathrm{def}}{=}\\sum_{\\sigma\\in\\mathfrak{S}_n}\\varepsilon(\\sigma)\\prod_{k=1}^n a_{k\\sigma(k)}\\\\" + newLineWithSeparation;
        math += "{\\sideset{_\\alpha^\\beta}{_\\gamma^\\delta}{\\mathop{\\begin{pmatrix}a&b\\\\c&d\\end{pmatrix}}}}\\\\" + newLineWithSeparation;
        math += "\\int_0^\\infty{x^{2n} e^{-a x^2}\\,dx} = \\frac{2n-1}{2a} \\int_0^\\infty{x^{2(n-1)} e^{-a x^2}\\,dx} = \\frac{(2n-1)!!}{2^{n+1}} \\sqrt{\\frac{\\pi}{a^{2n+1}}}\\\\" + newLineWithSeparation;
        math += "\\int_a^b{f(x)\\,dx} = (b - a) \\sum\\limits_{n = 1}^\\infty  {\\sum\\limits_{m = 1}^{2^n  - 1} {\\left( { - 1} \\right)^{m + 1} } } 2^{ - n} f(a + m\\left( {b - a} \\right)2^{-n} )\\\\" + newLineWithSeparation;
        math += "\\int_{-\\pi}^{\\pi} \\sin(\\alpha x) \\sin^n(\\beta x) dx = \\textstyle{\\left \\{ \\begin{array}{cc} (-1)^{(n+1)/2} (-1)^m \\frac{2 \\pi}{2^n} \\binom{n}{m} & n \\mbox{ odd},\\ \\alpha = \\beta (2m-n) \\\\ 0 & \\mbox{otherwise} \\\\ \\end{array} \\right .}\\\\" + newLineWithSeparation;
        math += "L = \\int_a^b \\sqrt{ \\left|\\sum_{i,j=1}^ng_{ij}(\\gamma(t))\\left(\\frac{d}{dt}x^i\\circ\\gamma(t)\\right)\\left(\\frac{d}{dt}x^j\\circ\\gamma(t)\\right)\\right|}\\,dt\\\\" + newLineWithSeparation;
        math += "\\begin{array}{rl} s &= \\int_a^b\\left\\|\\frac{d}{dt}\\vec{r}\\,(u(t),v(t))\\right\\|\\,dt \\\\ &= \\int_a^b \\sqrt{u'(t)^2\\,\\vec{r}_u\\cdot\\vec{r}_u + 2u'(t)v'(t)\\, \\vec{r}_u\\cdot\\vec{r}_v+ v'(t)^2\\,\\vec{r}_v\\cdot\\vec{r}_v}\\,\\,\\, dt. \\end{array}\\\\" + newLineWithSeparation;
        math += "\\end{array}$" + newLineWithSeparation;
        math += "\\end{document}";

        System.out.println(" 2. Create the .tex file");
        FileWriter writer = null;
        try {
        	File dir = new File(TEMP_DIRECTORY);
            if (!dir.exists()) dir.mkdirs();
            writer = new FileWriter(TEMP_DIRECTORY + File.separator + TEMP_TEX_FILE_NAME + ".tex", false);
            writer.write(math, 0, math.length());
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("  3. Execute LaTeX from command line  to generate picture");
        ProcessBuilder pb = new ProcessBuilder("pdflatex", "-shell-escape", TEMP_TEX_FILE_NAME + ".tex");
        pb.directory(new File(TEMP_DIRECTORY));
        try {
        	long startTime = System.nanoTime();
            Process p = pb.start();
            StreamPrinter fluxSortie = new StreamPrinter(p.getInputStream(), false);
            StreamPrinter fluxErreur = new StreamPrinter(p.getErrorStream(), false);
            new Thread(fluxSortie).start();
            new Thread(fluxErreur).start();
            p.waitFor();
            long stopTime = System.nanoTime();
            System.out.println((stopTime - startTime)/1.e9);
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        
        
        System.out.println(" 4. Display picture");
        
        BufferedImage img=ImageIO.read(new File(TEMP_DIRECTORY + File.separator+ TEMP_TEX_FILE_NAME + ".png"));
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(200,300);
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        
        
        // 5. Delete files
        for (File file : (new File(TEMP_DIRECTORY).listFiles())) {
            if (file.getName().startsWith(TEMP_TEX_FILE_NAME + ".")) {
                file.delete();
            }
        }
    }
}
