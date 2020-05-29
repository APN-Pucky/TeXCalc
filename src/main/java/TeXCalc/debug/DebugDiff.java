package TeXCalc.debug;

import java.util.List;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch.Diff;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch.Operation;

public class DebugDiff {
public static void main(String[] args) {
	List<Diff> diffs = new DiffMatchPatch().diffMain("a A = 6", "a B = 5");
	  for (Diff diff : diffs) {
	    if (diff.operation == Operation.EQUAL) {
	      System.out.println(diff.text); // Return only single diff, can also find multiple based on use case
	    }
	    if (diff.operation == Operation.DELETE) {
	      System.out.println("RM" + diff.text); // Return only single diff, can also find multiple based on use case
	    	
	    }
	    if (diff.operation == Operation.INSERT) {
	      System.out.println("ADD" + diff.text); // Return only single diff, can also find multiple based on use case
	    }
	  }
	}

}
