package TeXCalc.latex.wrap;

import java.util.ArrayList;
import java.util.LinkedList;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch.Diff;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch.Operation;

public class SimpleDiffAlign extends Align {
	public String too(String s) {
		String ret = "";

		String[] lines = s.replaceAll("\n", "").replaceAll("\r", "").split("\\\\\\\\");
		ret += lines[0] + "\\\\";
		for(int i = 0 ; i  < lines.length-1;++i) {
			System.out.println("'" +lines[i] + "' vs '" + lines[i+1] + "'");
			DiffMatchPatch dmp = new DiffMatchPatch();
			LinkedList<Diff> diffs =dmp .diffMain(lines[i],lines[i+1]);
			dmp.diffCleanupSemantic(diffs);
			  for (Diff diff : diffs) {
			    if (diff.operation == Operation.EQUAL) {
			    	System.out.println("EQ" + diff.text);
		    		if(ret.endsWith("^") || ret.endsWith("_"))
		    		{
		    			String ts = ret.substring(ret.length()-1,ret.length());
	    				ret = ret.substring(0,ret.length()-1);
	    				ret += "\\color{black}" + ts+diff.text;
		    		}
		    		else {
			    			ret += "\\color{black}" + diff.text;
		    			
		    		}
			    }
			    if (diff.operation == Operation.DELETE) {
			    	if(!diff.text.trim().equals("")) {
			    		if(diff.text.startsWith("^") || diff.text.startsWith("_"))
			    		{
			    		//	ret += diff.text.substring(0, 1)+"\\DIFFrm{" + diff.text.substring(1) + "}";
			    		} else {
			    		//ret += "\\DIFFrm{" + diff.text + "}";
			    		}
			    	}
			    }
			    if (diff.operation == Operation.INSERT) {
			    	if(!diff.text.trim().equals("")) {
			    		if(ret.endsWith("^") || ret.endsWith("_"))
			    		{
			    			String ts = ret.substring(ret.length()-1,ret.length());
			    			ret = ret.substring(0,ret.length()-1);
			    			ret += "\\color{green}" + ts+diff.text;
			    		}
			    		else {
			    			ret += "\\color{green}" + diff.text;
			    		}
			    	}
			    }
			  }
			  ret += "\\\\"+System.lineSeparator();
			}
		return ret;
		}
	public String to(String s) {
		StringBuilder ret = new StringBuilder("");
		StringBuilder ret2 = new StringBuilder("");

		String[] lines = s.replaceAll("\n", "").replaceAll("\r", "").split("\\\\\\\\");
		ret.append( lines[0] + "\\\\");
		ArrayList<LinkedList<Diff>> diffss = new ArrayList<LinkedList<Diff>>(lines.length-1);
		DiffMatchPatch dmp = new DiffMatchPatch();
		for(int i = 0; i < lines.length-1;++i) {
			LinkedList<Diff> diffs =dmp .diffMain(lines[i],lines[i+1]);
			dmp.diffCleanupSemantic(diffs);
			diffss.add(diffs);
		}
		for(int i = 0 ; i  < lines.length-1;++i) {
			System.out.println("'" +lines[i] + "' vs '" + lines[i+1] + "'");
			 for (Diff diff : diffss.get(i)) {
			    if (diff.operation == Operation.EQUAL) {
			    	System.out.println("EQ" + diff.text);
		    		if(ret.length()>0 && (ret.charAt(ret.length()-1)=='^' || ret.charAt(ret.length()-1)=='_' ))
		    		{
		    			String ts = ret.substring(ret.length()-1,ret.length());
	    				ret = ret.deleteCharAt(ret.length()-1);
	    				ret.append( "😂" + ts+diff.text);
		    		}
		    		else {
			    		ret.append( "😂" + diff.text);
		    		}
			    }
			    if (diff.operation == Operation.INSERT) {
			    		if(ret.length()>0 && (ret.charAt(ret.length()-1)=='^' || ret.charAt(ret.length()-1)=='_' ))
			    		{
			    			String ts = ret.substring(ret.length()-1,ret.length());
			    			ret = ret.deleteCharAt(ret.length()-1);
			    			ret.append(	"😊" + ts+diff.text);
			    		}
			    		else {
			    			ret.append("😊" + diff.text);
			    		}
			    }
			  }
			  if(i!= lines.length-2)ret .append("\\\\"+System.lineSeparator());
		}
		return ret.toString().replaceAll("😂", "\\\\color{black}").replaceAll( 
				"😊", "\\\\color{green}");
		}

}
