Needs@"SyntaxAnnotations`"
Needs@"CellsToTeX`" 

NotebookPauseForEvaluation[nb_] := Module[{},
While[NotebookEvaluatingQ[nb],Pause[.25]]];

NotebookEvaluatingQ[nb_]:=Module[{},
SelectionMove[nb,All,Notebook];
Or@@Map["Evaluating"/.#&,Developer`CellInformation[nb]]
];

UsingFrontEnd[
nb = NotebookOpen["tmp.m"];
(* SetOptions[nb, Evaluator -> "Kernel2"]; *)
SelectionMove[nb, All, Notebook];
SelectionEvaluate[nb]
NotebookPauseForEvaluation[nb];
NotebookSave[nb];
(* SetOptions[CellToTeX, "CurrentCellIndex" -> Automatic]; *)
Print[ExportString[NotebookGet[nb]/.
  cell : Cell[_, __] :> Cell[CellToTeX[cell], "Final"], "TeX",
 "FullDocument" -> False, "ConversionRules" -> {"Final" -> Identity}]
]
];

Quit[];
