// src: https://tex.stackexchange.com/a/167133
package TeXCalc.latex;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.io.Files;

import TeXCalc.config.Config;
import TeXCalc.exec.Exec;
import TeXCalc.gui.GUI;
import TeXCalc.util.IO;
import TeXCalc.util.Task;
import lombok.Getter;
import lombok.Setter;


public class Latex {
	//public static Latex _default = new Latex();
	public static boolean PRINT = false;
	public static boolean TIME = false;
	//public static String TEXENGINE = "lualatex";
	public static String TYPE_STANDALONE =  "\\documentclass[preview,crop,border=1pt,convert,varwidth=\\maxdimen]{standalone}\n";
	public static String TYPE_DOCUMENT = "\\documentclass{article}\n";
	/*public static String FRAMETOP =
			"\\usepackage{amsfonts}\n"+
			"\\usepackage{amsmath}\n" +
			"\\usepackage{amsthm}\n"+
			"\\usepackage{slashed}"+
			"\\usepackage[compat=1.1.0]{tikz-feynman}\n" +
			"\\DeclareMathOperator{\\Tr}{Tr}"+
			"\\setlength\\parindent{0pt}"+
			"\\begin{document}\n";
			*/
	
	public static String FRAMETOP = "\\usepackage{amsfonts}\n" + 
			"\\usepackage{amsmath}\n" + 
			"\\usepackage{amsthm}\n" + 
			"\\usepackage{markdown}\n" + 
			"\\usepackage{slashed}\\usepackage[compat=1.1.0]{tikz-feynman}\n" + 
			"\\DeclareMathOperator{\\Tr}{Tr}\\setlength\\parindent{0pt}\n" + 
			"    \\usepackage[breakable]{tcolorbox}\n" + 
			"    \\usepackage{parskip} % Stop auto-indenting (to mimic markdown behaviour)\n" + 
			"    \n" + 
			"    \\usepackage{iftex}\n" + 
			"    \\ifPDFTeX\n" + 
			"    	\\usepackage[T1]{fontenc}\n" + 
			"    	\\usepackage{mathpazo}\n" + 
			"    \\else\n" + 
			"    	\\usepackage{fontspec}\n" + 
			"    \\fi\n" + 
			"\n" + 
			"    % Basic figure setup, for now with no caption control since it's done\n" + 
			"    % automatically by Pandoc (which extracts ![](path) syntax from Markdown).\n" + 
			"    \\usepackage{graphicx}\n" + 
			"    % Maintain compatibility with old templates. Remove in nbconvert 6.0\n" + 
			"    \\let\\Oldincludegraphics\\includegraphics\n" + 
			"    % Ensure that by default, figures have no caption (until we provide a\n" + 
			"    % proper Figure object with a Caption API and a way to capture that\n" + 
			"    % in the conversion process - todo).\n" + 
			"    \\usepackage{caption}\n" + 
			"    \\DeclareCaptionFormat{nocaption}{}\n" + 
			"    \\captionsetup{format=nocaption,aboveskip=0pt,belowskip=0pt}\n" + 
			"\n" + 
			"    \\usepackage{float}\n" + 
			"    \\floatplacement{figure}{H} % forces figures to be placed at the correct location\n" + 
			"    \\usepackage{xcolor} % Allow colors to be defined\n" + 
			"    \\usepackage{enumerate} % Needed for markdown enumerations to work\n" + 
			"    \\usepackage{geometry} % Used to adjust the document margins\n" + 
			"    \\usepackage{amsmath} % Equations\n" + 
			"    \\usepackage{amssymb} % Equations\n" + 
			"    \\usepackage{textcomp} % defines textquotesingle\n" + 
			"    % Hack from http://tex.stackexchange.com/a/47451/13684:\n" + 
			"    \\AtBeginDocument{%\n" + 
			"        \\def\\PYZsq{\\textquotesingle}% Upright quotes in Pygmentized code\n" + 
			"    }\n" + 
			"    \\usepackage{upquote} % Upright quotes for verbatim code\n" + 
			"    \\usepackage{eurosym} % defines \\euro\n" + 
			"    \\usepackage[mathletters]{ucs} % Extended unicode (utf-8) support\n" + 
			"    \\usepackage{fancyvrb} % verbatim replacement that allows latex\n" + 
			"    \\usepackage{grffile} % extends the file name processing of package graphics \n" + 
			"                         % to support a larger range\n" + 
			"    \\makeatletter % fix for old versions of grffile with XeLaTeX\n" + 
			"    \\@ifpackagelater{grffile}{2019/11/01}\n" + 
			"    {\n" + 
			"      % Do nothing on new versions\n" + 
			"    }\n" + 
			"    {\n" + 
			"      \\def\\Gread@@xetex#1{%\n" + 
			"        \\IfFileExists{\"\\Gin@base\".bb}%\n" + 
			"        {\\Gread@eps{\\Gin@base.bb}}%\n" + 
			"        {\\Gread@@xetex@aux#1}%\n" + 
			"      }\n" + 
			"    }\n" + 
			"    \\makeatother\n" + 
			"    \\usepackage[Export]{adjustbox} % Used to constrain images to a maximum size\n" + 
			"    \\adjustboxset{max size={0.9\\linewidth}{0.9\\paperheight}}\n" + 
			"\n" + 
			"    % The hyperref package gives us a pdf with properly built\n" + 
			"    % internal navigation ('pdf bookmarks' for the table of contents,\n" + 
			"    % internal cross-reference links, web links for URLs, etc.)\n" + 
			"    \\usepackage{hyperref}\n" + 
			"    % The default LaTeX title has an obnoxious amount of whitespace. By default,\n" + 
			"    % titling removes some of it. It also provides customization options.\n" + 
			"    \\usepackage{titling}\n" + 
			"    \\usepackage{longtable} % longtable support required by pandoc >1.10\n" + 
			"    \\usepackage{booktabs}  % table support for pandoc > 1.12.2\n" + 
			"    \\usepackage[inline]{enumitem} % IRkernel/repr support (it uses the enumerate* environment)\n" + 
			"    \\usepackage[normalem]{ulem} % ulem is needed to support strikethroughs (\\sout)\n" + 
			"                                % normalem makes italics be italics, not underlines\n" + 
			"    \\usepackage{mathrsfs}\n" + 
			"    \n" + 
			"\n" + 
			"    \n" + 
			"    % Colors for the hyperref package\n" + 
			"    \\definecolor{urlcolor}{rgb}{0,.145,.698}\n" + 
			"    \\definecolor{linkcolor}{rgb}{.71,0.21,0.01}\n" + 
			"    \\definecolor{citecolor}{rgb}{.12,.54,.11}\n" + 
			"\n" + 
			"    % ANSI colors\n" + 
			"    \\definecolor{ansi-black}{HTML}{3E424D}\n" + 
			"    \\definecolor{ansi-black-intense}{HTML}{282C36}\n" + 
			"    \\definecolor{ansi-red}{HTML}{E75C58}\n" + 
			"    \\definecolor{ansi-red-intense}{HTML}{B22B31}\n" + 
			"    \\definecolor{ansi-green}{HTML}{00A250}\n" + 
			"    \\definecolor{ansi-green-intense}{HTML}{007427}\n" + 
			"    \\definecolor{ansi-yellow}{HTML}{DDB62B}\n" + 
			"    \\definecolor{ansi-yellow-intense}{HTML}{B27D12}\n" + 
			"    \\definecolor{ansi-blue}{HTML}{208FFB}\n" + 
			"    \\definecolor{ansi-blue-intense}{HTML}{0065CA}\n" + 
			"    \\definecolor{ansi-magenta}{HTML}{D160C4}\n" + 
			"    \\definecolor{ansi-magenta-intense}{HTML}{A03196}\n" + 
			"    \\definecolor{ansi-cyan}{HTML}{60C6C8}\n" + 
			"    \\definecolor{ansi-cyan-intense}{HTML}{258F8F}\n" + 
			"    \\definecolor{ansi-white}{HTML}{C5C1B4}\n" + 
			"    \\definecolor{ansi-white-intense}{HTML}{A1A6B2}\n" + 
			"    \\definecolor{ansi-default-inverse-fg}{HTML}{FFFFFF}\n" + 
			"    \\definecolor{ansi-default-inverse-bg}{HTML}{000000}\n" + 
			"\n" + 
			"    % common color for the border for error outputs.\n" + 
			"    \\definecolor{outerrorbackground}{HTML}{FFDFDF}\n" + 
			"\n" + 
			"    % commands and environments needed by pandoc snippets\n" + 
			"    % extracted from the output of `pandoc -s`\n" + 
			"    \\providecommand{\\tightlist}{%\n" + 
			"      \\setlength{\\itemsep}{0pt}\\setlength{\\parskip}{0pt}}\n" + 
			"    \\DefineVerbatimEnvironment{Highlighting}{Verbatim}{commandchars=\\\\\\{\\}}\n" + 
			"    % Add ',fontsize=\\small' for more characters per line\n" + 
			"    \\newenvironment{Shaded}{}{}\n" + 
			"    \\newcommand{\\KeywordTok}[1]{\\textcolor[rgb]{0.00,0.44,0.13}{\\textbf{{#1}}}}\n" + 
			"    \\newcommand{\\DataTypeTok}[1]{\\textcolor[rgb]{0.56,0.13,0.00}{{#1}}}\n" + 
			"    \\newcommand{\\DecValTok}[1]{\\textcolor[rgb]{0.25,0.63,0.44}{{#1}}}\n" + 
			"    \\newcommand{\\BaseNTok}[1]{\\textcolor[rgb]{0.25,0.63,0.44}{{#1}}}\n" + 
			"    \\newcommand{\\FloatTok}[1]{\\textcolor[rgb]{0.25,0.63,0.44}{{#1}}}\n" + 
			"    \\newcommand{\\CharTok}[1]{\\textcolor[rgb]{0.25,0.44,0.63}{{#1}}}\n" + 
			"    \\newcommand{\\StringTok}[1]{\\textcolor[rgb]{0.25,0.44,0.63}{{#1}}}\n" + 
			"    \\newcommand{\\CommentTok}[1]{\\textcolor[rgb]{0.38,0.63,0.69}{\\textit{{#1}}}}\n" + 
			"    \\newcommand{\\OtherTok}[1]{\\textcolor[rgb]{0.00,0.44,0.13}{{#1}}}\n" + 
			"    \\newcommand{\\AlertTok}[1]{\\textcolor[rgb]{1.00,0.00,0.00}{\\textbf{{#1}}}}\n" + 
			"    \\newcommand{\\FunctionTok}[1]{\\textcolor[rgb]{0.02,0.16,0.49}{{#1}}}\n" + 
			"    \\newcommand{\\RegionMarkerTok}[1]{{#1}}\n" + 
			"    \\newcommand{\\ErrorTok}[1]{\\textcolor[rgb]{1.00,0.00,0.00}{\\textbf{{#1}}}}\n" + 
			"    \\newcommand{\\NormalTok}[1]{{#1}}\n" + 
			"    \n" + 
			"    % Additional commands for more recent versions of Pandoc\n" + 
			"    \\newcommand{\\ConstantTok}[1]{\\textcolor[rgb]{0.53,0.00,0.00}{{#1}}}\n" + 
			"    \\newcommand{\\SpecialCharTok}[1]{\\textcolor[rgb]{0.25,0.44,0.63}{{#1}}}\n" + 
			"    \\newcommand{\\VerbatimStringTok}[1]{\\textcolor[rgb]{0.25,0.44,0.63}{{#1}}}\n" + 
			"    \\newcommand{\\SpecialStringTok}[1]{\\textcolor[rgb]{0.73,0.40,0.53}{{#1}}}\n" + 
			"    \\newcommand{\\ImportTok}[1]{{#1}}\n" + 
			"    \\newcommand{\\DocumentationTok}[1]{\\textcolor[rgb]{0.73,0.13,0.13}{\\textit{{#1}}}}\n" + 
			"    \\newcommand{\\AnnotationTok}[1]{\\textcolor[rgb]{0.38,0.63,0.69}{\\textbf{\\textit{{#1}}}}}\n" + 
			"    \\newcommand{\\CommentVarTok}[1]{\\textcolor[rgb]{0.38,0.63,0.69}{\\textbf{\\textit{{#1}}}}}\n" + 
			"    \\newcommand{\\VariableTok}[1]{\\textcolor[rgb]{0.10,0.09,0.49}{{#1}}}\n" + 
			"    \\newcommand{\\ControlFlowTok}[1]{\\textcolor[rgb]{0.00,0.44,0.13}{\\textbf{{#1}}}}\n" + 
			"    \\newcommand{\\OperatorTok}[1]{\\textcolor[rgb]{0.40,0.40,0.40}{{#1}}}\n" + 
			"    \\newcommand{\\BuiltInTok}[1]{{#1}}\n" + 
			"    \\newcommand{\\ExtensionTok}[1]{{#1}}\n" + 
			"    \\newcommand{\\PreprocessorTok}[1]{\\textcolor[rgb]{0.74,0.48,0.00}{{#1}}}\n" + 
			"    \\newcommand{\\AttributeTok}[1]{\\textcolor[rgb]{0.49,0.56,0.16}{{#1}}}\n" + 
			"    \\newcommand{\\InformationTok}[1]{\\textcolor[rgb]{0.38,0.63,0.69}{\\textbf{\\textit{{#1}}}}}\n" + 
			"    \\newcommand{\\WarningTok}[1]{\\textcolor[rgb]{0.38,0.63,0.69}{\\textbf{\\textit{{#1}}}}}\n" + 
			"    \n" + 
			"    \n" + 
			"    % Define a nice break command that doesn't care if a line doesn't already\n" + 
			"    % exist.\n" + 
			"    \\def\\br{\\hspace*{\\fill} \\\\* }\n" + 
			"    % Math Jax compatibility definitions\n" + 
			"    \\def\\gt{>}\n" + 
			"    \\def\\lt{<}\n" + 
			"    \\let\\Oldtex\\TeX\n" + 
			"    \\let\\Oldlatex\\LaTeX\n" + 
			"    \\renewcommand{\\TeX}{\\textrm{\\Oldtex}}\n" + 
			"    \\renewcommand{\\LaTeX}{\\textrm{\\Oldlatex}}\n" + 
			"    % Document parameters\n" + 
			"    % Document title\n" + 
			"    \\title{tmp\\_jupyter}\n" + 
			"    \n" + 
			"    \n" + 
			"    \n" + 
			"    \n" + 
			"    \n" + 
			"% Pygments definitions\n" + 
			"\\makeatletter\n" + 
			"\\def\\PY@reset{\\let\\PY@it=\\relax \\let\\PY@bf=\\relax%\n" + 
			"    \\let\\PY@ul=\\relax \\let\\PY@tc=\\relax%\n" + 
			"    \\let\\PY@bc=\\relax \\let\\PY@ff=\\relax}\n" + 
			"\\def\\PY@tok#1{\\csname PY@tok@#1\\endcsname}\n" + 
			"\\def\\PY@toks#1+{\\ifx\\relax#1\\empty\\else%\n" + 
			"    \\PY@tok{#1}\\expandafter\\PY@toks\\fi}\n" + 
			"\\def\\PY@do#1{\\PY@bc{\\PY@tc{\\PY@ul{%\n" + 
			"    \\PY@it{\\PY@bf{\\PY@ff{#1}}}}}}}\n" + 
			"\\def\\PY#1#2{\\PY@reset\\PY@toks#1+\\relax+\\PY@do{#2}}\n" + 
			"\n" + 
			"\\expandafter\\def\\csname PY@tok@w\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.73,0.73,0.73}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@c\\endcsname{\\let\\PY@it=\\textit\\def\\PY@tc##1{\\textcolor[rgb]{0.25,0.50,0.50}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@cp\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.74,0.48,0.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@k\\endcsname{\\let\\PY@bf=\\textbf\\def\\PY@tc##1{\\textcolor[rgb]{0.00,0.50,0.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@kp\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.00,0.50,0.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@kt\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.69,0.00,0.25}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@o\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.40,0.40,0.40}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@ow\\endcsname{\\let\\PY@bf=\\textbf\\def\\PY@tc##1{\\textcolor[rgb]{0.67,0.13,1.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@nb\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.00,0.50,0.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@nf\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.00,0.00,1.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@nc\\endcsname{\\let\\PY@bf=\\textbf\\def\\PY@tc##1{\\textcolor[rgb]{0.00,0.00,1.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@nn\\endcsname{\\let\\PY@bf=\\textbf\\def\\PY@tc##1{\\textcolor[rgb]{0.00,0.00,1.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@ne\\endcsname{\\let\\PY@bf=\\textbf\\def\\PY@tc##1{\\textcolor[rgb]{0.82,0.25,0.23}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@nv\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.10,0.09,0.49}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@no\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.53,0.00,0.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@nl\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.63,0.63,0.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@ni\\endcsname{\\let\\PY@bf=\\textbf\\def\\PY@tc##1{\\textcolor[rgb]{0.60,0.60,0.60}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@na\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.49,0.56,0.16}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@nt\\endcsname{\\let\\PY@bf=\\textbf\\def\\PY@tc##1{\\textcolor[rgb]{0.00,0.50,0.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@nd\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.67,0.13,1.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@s\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.73,0.13,0.13}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@sd\\endcsname{\\let\\PY@it=\\textit\\def\\PY@tc##1{\\textcolor[rgb]{0.73,0.13,0.13}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@si\\endcsname{\\let\\PY@bf=\\textbf\\def\\PY@tc##1{\\textcolor[rgb]{0.73,0.40,0.53}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@se\\endcsname{\\let\\PY@bf=\\textbf\\def\\PY@tc##1{\\textcolor[rgb]{0.73,0.40,0.13}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@sr\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.73,0.40,0.53}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@ss\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.10,0.09,0.49}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@sx\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.00,0.50,0.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@m\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.40,0.40,0.40}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@gh\\endcsname{\\let\\PY@bf=\\textbf\\def\\PY@tc##1{\\textcolor[rgb]{0.00,0.00,0.50}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@gu\\endcsname{\\let\\PY@bf=\\textbf\\def\\PY@tc##1{\\textcolor[rgb]{0.50,0.00,0.50}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@gd\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.63,0.00,0.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@gi\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.00,0.63,0.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@gr\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{1.00,0.00,0.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@ge\\endcsname{\\let\\PY@it=\\textit}\n" + 
			"\\expandafter\\def\\csname PY@tok@gs\\endcsname{\\let\\PY@bf=\\textbf}\n" + 
			"\\expandafter\\def\\csname PY@tok@gp\\endcsname{\\let\\PY@bf=\\textbf\\def\\PY@tc##1{\\textcolor[rgb]{0.00,0.00,0.50}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@go\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.53,0.53,0.53}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@gt\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.00,0.27,0.87}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@err\\endcsname{\\def\\PY@bc##1{\\setlength{\\fboxsep}{0pt}\\fcolorbox[rgb]{1.00,0.00,0.00}{1,1,1}{\\strut ##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@kc\\endcsname{\\let\\PY@bf=\\textbf\\def\\PY@tc##1{\\textcolor[rgb]{0.00,0.50,0.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@kd\\endcsname{\\let\\PY@bf=\\textbf\\def\\PY@tc##1{\\textcolor[rgb]{0.00,0.50,0.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@kn\\endcsname{\\let\\PY@bf=\\textbf\\def\\PY@tc##1{\\textcolor[rgb]{0.00,0.50,0.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@kr\\endcsname{\\let\\PY@bf=\\textbf\\def\\PY@tc##1{\\textcolor[rgb]{0.00,0.50,0.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@bp\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.00,0.50,0.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@fm\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.00,0.00,1.00}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@vc\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.10,0.09,0.49}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@vg\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.10,0.09,0.49}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@vi\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.10,0.09,0.49}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@vm\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.10,0.09,0.49}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@sa\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.73,0.13,0.13}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@sb\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.73,0.13,0.13}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@sc\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.73,0.13,0.13}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@dl\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.73,0.13,0.13}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@s2\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.73,0.13,0.13}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@sh\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.73,0.13,0.13}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@s1\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.73,0.13,0.13}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@mb\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.40,0.40,0.40}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@mf\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.40,0.40,0.40}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@mh\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.40,0.40,0.40}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@mi\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.40,0.40,0.40}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@il\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.40,0.40,0.40}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@mo\\endcsname{\\def\\PY@tc##1{\\textcolor[rgb]{0.40,0.40,0.40}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@ch\\endcsname{\\let\\PY@it=\\textit\\def\\PY@tc##1{\\textcolor[rgb]{0.25,0.50,0.50}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@cm\\endcsname{\\let\\PY@it=\\textit\\def\\PY@tc##1{\\textcolor[rgb]{0.25,0.50,0.50}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@cpf\\endcsname{\\let\\PY@it=\\textit\\def\\PY@tc##1{\\textcolor[rgb]{0.25,0.50,0.50}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@c1\\endcsname{\\let\\PY@it=\\textit\\def\\PY@tc##1{\\textcolor[rgb]{0.25,0.50,0.50}{##1}}}\n" + 
			"\\expandafter\\def\\csname PY@tok@cs\\endcsname{\\let\\PY@it=\\textit\\def\\PY@tc##1{\\textcolor[rgb]{0.25,0.50,0.50}{##1}}}\n" + 
			"\n" + 
			"\\def\\PYZbs{\\char`\\\\}\n" + 
			"\\def\\PYZus{\\char`\\_}\n" + 
			"\\def\\PYZob{\\char`\\{}\n" + 
			"\\def\\PYZcb{\\char`\\}}\n" + 
			"\\def\\PYZca{\\char`\\^}\n" + 
			"\\def\\PYZam{\\char`\\&}\n" + 
			"\\def\\PYZlt{\\char`\\<}\n" + 
			"\\def\\PYZgt{\\char`\\>}\n" + 
			"\\def\\PYZsh{\\char`\\#}\n" + 
			"\\def\\PYZpc{\\char`\\%}\n" + 
			"\\def\\PYZdl{\\char`\\$}\n" + 
			"\\def\\PYZhy{\\char`\\-}\n" + 
			"\\def\\PYZsq{\\char`\\'}\n" + 
			"\\def\\PYZdq{\\char`\\\"}\n" + 
			"\\def\\PYZti{\\char`\\~}\n" + 
			"% for compatibility with earlier versions\n" + 
			"\\def\\PYZat{@}\n" + 
			"\\def\\PYZlb{[}\n" + 
			"\\def\\PYZrb{]}\n" + 
			"\\makeatother\n" + 
			"\n" + 
			"\n" + 
			"    % For linebreaks inside Verbatim environment from package fancyvrb. \n" + 
			"    \\makeatletter\n" + 
			"        \\newbox\\Wrappedcontinuationbox \n" + 
			"        \\newbox\\Wrappedvisiblespacebox \n" + 
			"        \\newcommand*\\Wrappedvisiblespace {\\textcolor{red}{\\textvisiblespace}} \n" + 
			"        \\newcommand*\\Wrappedcontinuationsymbol {\\textcolor{red}{\\llap{\\tiny$\\m@th\\hookrightarrow$}}} \n" + 
			"        \\newcommand*\\Wrappedcontinuationindent {3ex } \n" + 
			"        \\newcommand*\\Wrappedafterbreak {\\kern\\Wrappedcontinuationindent\\copy\\Wrappedcontinuationbox} \n" + 
			"        % Take advantage of the already applied Pygments mark-up to insert \n" + 
			"        % potential linebreaks for TeX processing. \n" + 
			"        %        {, <, #, %, $, ' and \": go to next line. \n" + 
			"        %        _, }, ^, &, >, - and ~: stay at end of broken line. \n" + 
			"        % Use of \\textquotesingle for straight quote. \n" + 
			"        \\newcommand*\\Wrappedbreaksatspecials {% \n" + 
			"            \\def\\PYGZus{\\discretionary{\\char`\\_}{\\Wrappedafterbreak}{\\char`\\_}}% \n" + 
			"            \\def\\PYGZob{\\discretionary{}{\\Wrappedafterbreak\\char`\\{}{\\char`\\{}}% \n" + 
			"            \\def\\PYGZcb{\\discretionary{\\char`\\}}{\\Wrappedafterbreak}{\\char`\\}}}% \n" + 
			"            \\def\\PYGZca{\\discretionary{\\char`\\^}{\\Wrappedafterbreak}{\\char`\\^}}% \n" + 
			"            \\def\\PYGZam{\\discretionary{\\char`\\&}{\\Wrappedafterbreak}{\\char`\\&}}% \n" + 
			"            \\def\\PYGZlt{\\discretionary{}{\\Wrappedafterbreak\\char`\\<}{\\char`\\<}}% \n" + 
			"            \\def\\PYGZgt{\\discretionary{\\char`\\>}{\\Wrappedafterbreak}{\\char`\\>}}% \n" + 
			"            \\def\\PYGZsh{\\discretionary{}{\\Wrappedafterbreak\\char`\\#}{\\char`\\#}}% \n" + 
			"            \\def\\PYGZpc{\\discretionary{}{\\Wrappedafterbreak\\char`\\%}{\\char`\\%}}% \n" + 
			"            \\def\\PYGZdl{\\discretionary{}{\\Wrappedafterbreak\\char`\\$}{\\char`\\$}}% \n" + 
			"            \\def\\PYGZhy{\\discretionary{\\char`\\-}{\\Wrappedafterbreak}{\\char`\\-}}% \n" + 
			"            \\def\\PYGZsq{\\discretionary{}{\\Wrappedafterbreak\\textquotesingle}{\\textquotesingle}}% \n" + 
			"            \\def\\PYGZdq{\\discretionary{}{\\Wrappedafterbreak\\char`\\\"}{\\char`\\\"}}% \n" + 
			"            \\def\\PYGZti{\\discretionary{\\char`\\~}{\\Wrappedafterbreak}{\\char`\\~}}% \n" + 
			"        } \n" + 
			"        % Some characters . , ; ? ! / are not pygmentized. \n" + 
			"        % This macro makes them \"active\" and they will insert potential linebreaks \n" + 
			"        \\newcommand*\\Wrappedbreaksatpunct {% \n" + 
			"            \\lccode`\\~`\\.\\lowercase{\\def~}{\\discretionary{\\hbox{\\char`\\.}}{\\Wrappedafterbreak}{\\hbox{\\char`\\.}}}% \n" + 
			"            \\lccode`\\~`\\,\\lowercase{\\def~}{\\discretionary{\\hbox{\\char`\\,}}{\\Wrappedafterbreak}{\\hbox{\\char`\\,}}}% \n" + 
			"            \\lccode`\\~`\\;\\lowercase{\\def~}{\\discretionary{\\hbox{\\char`\\;}}{\\Wrappedafterbreak}{\\hbox{\\char`\\;}}}% \n" + 
			"            \\lccode`\\~`\\:\\lowercase{\\def~}{\\discretionary{\\hbox{\\char`\\:}}{\\Wrappedafterbreak}{\\hbox{\\char`\\:}}}% \n" + 
			"            \\lccode`\\~`\\?\\lowercase{\\def~}{\\discretionary{\\hbox{\\char`\\?}}{\\Wrappedafterbreak}{\\hbox{\\char`\\?}}}% \n" + 
			"            \\lccode`\\~`\\!\\lowercase{\\def~}{\\discretionary{\\hbox{\\char`\\!}}{\\Wrappedafterbreak}{\\hbox{\\char`\\!}}}% \n" + 
			"            \\lccode`\\~`\\/\\lowercase{\\def~}{\\discretionary{\\hbox{\\char`\\/}}{\\Wrappedafterbreak}{\\hbox{\\char`\\/}}}% \n" + 
			"            \\catcode`\\.\\active\n" + 
			"            \\catcode`\\,\\active \n" + 
			"            \\catcode`\\;\\active\n" + 
			"            \\catcode`\\:\\active\n" + 
			"            \\catcode`\\?\\active\n" + 
			"            \\catcode`\\!\\active\n" + 
			"            \\catcode`\\/\\active \n" + 
			"            \\lccode`\\~`\\~ 	\n" + 
			"        }\n" + 
			"    \\makeatother\n" + 
			"\n" + 
			"    \\let\\OriginalVerbatim=\\Verbatim\n" + 
			"    \\makeatletter\n" + 
			"    \\renewcommand{\\Verbatim}[1][1]{%\n" + 
			"        %\\parskip\\z@skip\n" + 
			"        \\sbox\\Wrappedcontinuationbox {\\Wrappedcontinuationsymbol}%\n" + 
			"        \\sbox\\Wrappedvisiblespacebox {\\FV@SetupFont\\Wrappedvisiblespace}%\n" + 
			"        \\def\\FancyVerbFormatLine ##1{\\hsize\\linewidth\n" + 
			"            \\vtop{\\raggedright\\hyphenpenalty\\z@\\exhyphenpenalty\\z@\n" + 
			"                \\doublehyphendemerits\\z@\\finalhyphendemerits\\z@\n" + 
			"                \\strut ##1\\strut}%\n" + 
			"        }%\n" + 
			"        % If the linebreak is at a space, the latter will be displayed as visible\n" + 
			"        % space at end of first line, and a continuation symbol starts next line.\n" + 
			"        % Stretch/shrink are however usually zero for typewriter font.\n" + 
			"        \\def\\FV@Space {%\n" + 
			"            \\nobreak\\hskip\\z@ plus\\fontdimen3\\font minus\\fontdimen4\\font\n" + 
			"            \\discretionary{\\copy\\Wrappedvisiblespacebox}{\\Wrappedafterbreak}\n" + 
			"            {\\kern\\fontdimen2\\font}%\n" + 
			"        }%\n" + 
			"        \n" + 
			"        % Allow breaks at special characters using \\PYG... macros.\n" + 
			"        \\Wrappedbreaksatspecials\n" + 
			"        % Breaks at punctuation characters . , ; ? ! and / need catcode=\\active 	\n" + 
			"        \\OriginalVerbatim[#1,codes*=\\Wrappedbreaksatpunct]%\n" + 
			"    }\n" + 
			"    \\makeatother\n" + 
			"\n" + 
			"    % Exact colors from NB\n" + 
			"    \\definecolor{incolor}{HTML}{303F9F}\n" + 
			"    \\definecolor{outcolor}{HTML}{D84315}\n" + 
			"    \\definecolor{cellborder}{HTML}{CFCFCF}\n" + 
			"    \\definecolor{cellbackground}{HTML}{F7F7F7}\n" + 
			"    \n" + 
			"    % prompt\n" + 
			"    \\makeatletter\n" + 
			"    \\newcommand{\\boxspacing}{\\kern\\kvtcb@left@rule\\kern\\kvtcb@boxsep}\n" + 
			"    \\makeatother\n" + 
			"    \\newcommand{\\prompt}[4]{\n" + 
			"        {\\ttfamily\\llap{{\\color{#2}[#3]:\\hspace{3pt}#4}}\\vspace{-\\baselineskip}}\n" + 
			"    }\n" + 
			"    \n" + 
			"\n" + 
			"    \n" + 
			"    % Prevent overflowing lines due to hard-to-break entities\n" + 
			"    \\sloppy \n" + 
			"    % Setup hyperref package\n" + 
			"    \\hypersetup{\n" + 
			"      breaklinks=true,  % so long urls are correctly broken across lines\n" + 
			"      colorlinks=true,\n" + 
			"      urlcolor=urlcolor,\n" + 
			"      linkcolor=linkcolor,\n" + 
			"      citecolor=citecolor,\n" + 
			"      }\n" + 
			"    % Slightly bigger margins than the latex defaults\n" + 
			"    \n" + 
			"    \\geometry{verbose,tmargin=1in,bmargin=1in,lmargin=1in,rmargin=1in}\n" + 
			"    \n" + 
			"    \n" + 
			"\\begin{document}";
	public static String FRAMEEND = "\\end{document}\n";

	@Getter 
	@Setter
	public HashMap<String,String> filecache = new HashMap<String,String>();


	private JTextArea engine ;
	private JTextArea standaloneType ;
	private JTextArea documentType;
	private JTextArea top;
	private JTextArea end;

	public String getTop() { return top.getText();}
	public String getEnd() { return end.getText();}
	public String getStandaloneType() { return standaloneType.getText();}
	public String getDocumentType() { return documentType.getText();}
	public String getEngine() { return engine.getText();}
	
	public void setTop(String a) { top.setText(a);}
	public void setEnd(String a) { end.setText(a);}
	public void setStandaloneType(String a) { standaloneType.setText(a);}
	public void setDocumentType(String a) {  documentType.setText(a);}
	public void setEngine(String a) { engine.setText(a);}

	@JsonIgnore
	private JPanel panel;
	
	public Latex() {
		this(FRAMETOP, FRAMEEND);
	}
	

	@JsonCreator
	public Latex(@JsonProperty("top") String top,@JsonProperty("end") String end) {
		panel = new JPanel();
		
		this.top = GUI.areaLatex(top);
		this.end = GUI.areaLatex(end);
		engine = GUI.area(Config.current.getLatex().getDefaultEngine());
		standaloneType = GUI.areaLatex(TYPE_STANDALONE);
		documentType = GUI.areaLatex(TYPE_DOCUMENT);

		panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
		panel.add(engine);
		panel.add(new JSeparator());
		panel.add(standaloneType);
		panel.add(new JSeparator());
		panel.add(documentType);
		panel.add(new JSeparator());
		panel.add(this.top);
		panel.add(new JSeparator());
		panel.add(this.end);
	}
	public void cache(String key, String f) {
		//System.out.println("Cached " + f);
		if(new File(f).exists()) {
		try {
					filecache.put(key, IO.encodeFileToBase64Binary(f));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	public void cache(String key, File f) {
		//System.out.println("Cached " + f);
		if(f.exists()) {
		try {
					filecache.put(key, IO.encodeFileToBase64Binary(f.getPath()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	public void cache(File f) {
		//System.out.println("Cached " + f);
		if(f.exists()) {
		try {
					filecache.put(f.getPath(), IO.encodeFileToBase64Binary(f.getPath()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	public void cache(String f) {
		//System.out.println("Cached " + f);
		if(new File(f).exists()) {
		try {
					filecache.put(f, IO.encodeFileToBase64Binary(f));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public void cleanCache(String cont) {
		Iterator<String> it =filecache.keySet().iterator();
		while(it.hasNext()) {
			String f = it.next();
			if(!cont.contains(f.split("\\.")[0]))it.remove();
		}
	}
	
	public void pasteCache(String dir) {
		if(!new File(dir).isDirectory()) throw new RuntimeException("Can only paste Cache in directory");
		for(String f : filecache.keySet()) {
			File ff = new File(f);
			if(ff.exists()) {
				try {
					//System.out.println("copied");
					File fi = (new File(dir+ File.separator+ f));
					fi.getParentFile().mkdirs();
					Files.copy(ff,fi);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cache(f);
				
			}
			else {
			byte[] decodedImg = Base64.getDecoder()
                    .decode(filecache.get(f).getBytes(StandardCharsets.UTF_8));
			try {
				File fn = new File (dir+ File.separator + f);
				fn.getParentFile().mkdirs();
				fn.createNewFile();
				FileOutputStream fos = new FileOutputStream(fn);
				fos.write(decodedImg);
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
	}
	
	public File toPdf(String latex) {
		String filename = "export";
		Exec ex = new Exec("tex");
		IO.writeFile(ex.getDirName() + filename + ".tex", latex);
		pasteCache(ex.getDirName());
		ex.exec(getEngine(),  "-halt-on-error","--shell-escape",filename+ ".tex");

		File ret_file = new File(ex.getDirName() + File.separator +".."+ File.separator + filename+ ".pdf");
		
		try {
			File tmp = new File(ex.getDirName() + File.separator + filename + ".pdf");
			if(tmp.exists()) {
				Files.move(tmp,ret_file);
			}
			else
			{
				ret_file = null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret_file = null;
		}

		//cleanUp(, TEMP_TEX_FILE_NAME);

		return ret_file;
	}
	
	public File toPdf2(String latex) {
		String uuid = UUID.randomUUID().toString();
		String TEMP_DIRECTORY = ".tmp" + File.separator + uuid;
		String TEMP_TEX_FILE_NAME = "export"; // for New22.tex
		String ret = TEMP_DIRECTORY + File.separator + TEMP_TEX_FILE_NAME + ".png";
		File ret_file = new File(TEMP_DIRECTORY + File.separator + TEMP_TEX_FILE_NAME + ".pdf") ;

		// 1. Prepare the .tex file
		String newLineWithSeparation = System.getProperty("line.separator") + System.getProperty("line.separator");
		String math = latex;

		System.out.println(" 2. Create the .tex file");
		FileWriter writer = null;
		try {
			File dir = new File(TEMP_DIRECTORY + File.separator + "tex");
			if (!dir.exists())
				dir.mkdirs();
			writer = new FileWriter(
					TEMP_DIRECTORY + File.separator + "tex" + File.separator + TEMP_TEX_FILE_NAME + ".tex", false);
			writer.write(math, 0, math.length());
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		for(String f : filecache.keySet()) {
			File ff = new File(f);
			if(ff.exists()) {
				try {
					System.out.println("copied");
					File fi = (new File(TEMP_DIRECTORY + File.separator  + "tex" + File.separator+ f));
					fi.getParentFile().mkdirs();
					Files.copy(ff,fi);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cache(f);
				
			}
			else {
			byte[] decodedImg = Base64.getDecoder()
                    .decode(filecache.get(f).getBytes(StandardCharsets.UTF_8));
			try {
				File fn = new File (TEMP_DIRECTORY + File.separator + "tex" + File.separator + f);
				fn.getParentFile().mkdirs();
				fn.createNewFile();
				FileOutputStream fos = new FileOutputStream(fn);
				fos.write(decodedImg);
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
		
		System.out.print("  3. Execute LaTeX {" + uuid + "} from command line  to generate picture = ");
		ProcessBuilder pb = new ProcessBuilder(getEngine(),  "-halt-on-error",
				TEMP_TEX_FILE_NAME + ".tex");
		pb.directory(new File(TEMP_DIRECTORY + File.separator + "tex"));
		try {
			long startTime = System.nanoTime();
			Process p = pb.start();
			if(PRINT) {
				StreamPrinter fluxSortie = new StreamPrinter(p.getInputStream(), true);
				StreamPrinter fluxErreur = new StreamPrinter(p.getErrorStream(), true);
				Task.startUntracked(fluxSortie);
				Task.startUntracked(fluxErreur);
			}
			p.waitFor();
			long stopTime = System.nanoTime();
			if(TIME)System.out.println((stopTime - startTime) / 1.e9 + " s");
		} catch (IOException | InterruptedException ex) {
			ex.printStackTrace();
		}
		
		try {
			File tmp = new File(TEMP_DIRECTORY + File.separator + "tex" + File.separator + TEMP_TEX_FILE_NAME + ".pdf");
			if(tmp.exists()) {
			Files.move(tmp,new File(TEMP_DIRECTORY + File.separator + TEMP_TEX_FILE_NAME + ".pdf") );
			}
			else
			{
				ret_file = null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret_file = null;
		}

		cleanUp(TEMP_DIRECTORY, TEMP_TEX_FILE_NAME);

		return ret_file;
	}
	
	public BufferedImage pdfToImage(File pdf_file) {
		File f = pdf_file;
		if(f==null) return null;
		ProcessBuilder pb = new ProcessBuilder("pdftoppm",  "-png",
				f.getAbsolutePath());
		BufferedImage bi = null;
		try {
			Process p = pb.start();
			bi = ImageIO.read(p.getInputStream());
			if(PRINT) {
			StreamPrinter fluxSortie = new StreamPrinter(p.getInputStream(), true);
			StreamPrinter fluxErreur = new StreamPrinter(p.getErrorStream(), true);
			Task.startUntracked(fluxSortie);
			Task.startUntracked(fluxErreur);
			}
			p.waitFor();
			
		} catch (InterruptedException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return bi;
	}
	
	public BufferedImage toImage(String latex) {

		String math = latex;
		File f = toPdf(math);
		BufferedImage b = pdfToImage(f);
		if(f!= null)
			f.delete();
		return b;
	}
	

	public BufferedImage snipImage(String latex) {
		String newLineWithSeparation = System.getProperty("line.separator") + System.getProperty("line.separator");
		String math = getStandaloneType();
		math += getTop();
		math += latex + newLineWithSeparation;
		math += getEnd();
		return toImage(math);
	}


	public BufferedImage snipMathImage(String math) {
		return snipImage("$" + math + "$");
	}
	

	private void cleanUp(String TEMP_DIRECTORY, String TEMP_TEX_FILE_NAME) {
		// 5. Delete files
		new File(TEMP_DIRECTORY).deleteOnExit();
		/*
		for (File file : (new File(TEMP_DIRECTORY + File.separator + "tex").listFiles())) {
			if (file.getName().startsWith(TEMP_TEX_FILE_NAME + ".")) {
				file.deleteOnExit();
			}
		}
		*/
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
	
	public static String begin(String env) {
		return "\\begin{" + env + "}";
	}
	public static String end(String env) {
		return "\\end{" + env + "}";
	}
	public void checkRequirements(String[] requirements) {
		for(String r : requirements) {
			if(!getTop().contains(r)) {
				System.out.println("Required: " + r);
				//TODO popup?
			}
		}
		
	}
}
