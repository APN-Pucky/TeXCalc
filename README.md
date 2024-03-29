# TeXCalc
![all](https://github.com/APN-Pucky/TeXCalc/actions/workflows/all.yml/badge.svg)
![downloads](https://img.shields.io/github/downloads/APN-Pucky/TeXCalc/total)

Live rendered latex

 ![](https://raw.githubusercontent.com/APN-Pucky/TeXCalc/master/image/preview.png)

## Requirements

* java
* ![tex-live](https://www.tug.org/texlive/acquire-netinstall.html) comes with needed:
  * lualatex (or latexmk, pdflatex, ...)
  * pdftoppm
* (python3/Anaconda)
* (math/Mathematica)
* 
## Features

### LaTeX
Is run in preview standalone mode for displaying cells.

### Python
Each cell is separate from others, so no variables are saved inbetween them.

### Mathematica
Results/variables/definitions can be reused across cells.
Standalone documentclass must be without "crop" and "varwidth".
Also needs "\usepackage{mmacells}".


## Troubleshooting
* missing output on simple test latex input
  * remove \usepackage{markdown} or install/fix it
  * remove \usepackage{mmacells} (needed for Mathematica)
  * remove everything except for the needed usepackages in the Settings
  * ImageMagick might need permission to edit/write pdfs

## Development

Import the project as a git project into eclipse. 
Then run

```sh
$ ./gradlew cleanEclipse eclipse
```

to fix the setup.
