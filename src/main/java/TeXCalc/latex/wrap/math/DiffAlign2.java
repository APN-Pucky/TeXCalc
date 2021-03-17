package TeXCalc.latex.wrap.math;

import java.util.ArrayList;
import java.util.LinkedList;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch.Diff;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch.Operation;

public class DiffAlign2 extends Align
{
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
			//dmp.diffCleanupEfficiency(diffs);
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
		String[] lines2 = ret.toString().replaceAll("\n", "").replaceAll("\r", "").split("\\\\\\\\");
		for(int i = 0 ; i  < lines2.length-1 && lines.length>= i+1;++i) {
			LinkedList<Diff> diffs = dmp.diffMain(lines2[i],lines[i+1]);
			//dmp.diffCleanupEfficiency(diffs);
			dmp.diffCleanupSemantic(diffs);
			dmp.diffCleanupMerge(diffs);
			System.out.println("V2: "+ lines2[i] + " vs " + lines[i+1]);
		    //ret2.append("\\foreach{\\col}{}{");
		    for (Diff diff : diffs) {
			    if (diff.operation == Operation.EQUAL) {
			    	ret2.append(diff.text );
			    }
			    
			    if(diff.operation == Operation.DELETE) {
			    	System.out.println("RM2 _"  + diff.text);
			    	if(diff.text.equals("😊")|| diff.text.equals("😂")) {
			    		ret2.append(diff.text);
			    	}
			    	else {
			    	if(ret2.length()>0 && (ret2.charAt(ret2.length()-1)=='^' || ret2.charAt(ret2.length()-1)=='_' ))
			    	{
			    		String ts = ret2.substring(ret2.length()-1,ret2.length());
			    		ret2 = ret2.deleteCharAt(ret2.length()-1);
			    		ret2.append( "\\colorlet{red1}{red}\\colorlet{cur}{blackgreen1!50!red1}\\color{cur}" + ts+diff.text + "\\colorlet{red1}{black}\\colorlet{cur}{blackgreen1!50!red1}\\color{cur}");
			    	}
			    	else {
			    		ret2.append( "\\colorlet{red1}{red}\\colorlet{cur}{blackgreen1!50!red1}\\color{cur}" + diff.text + "\\colorlet{red1}{black}\\colorlet{cur}{blackgreen1!50!red1}\\color{cur}");
			    	}
			    	}
			    }
		    }
		    //ret2.append("}");
			ret2.append("\\\\"+System.lineSeparator());
		}
		ret2.append(lines2[lines2.length-1]);
		//return ret.toString();
		//\\colorlet{save}{.}\\colorlet{cur}{save!50!red!90!}\\color{cur}
		return ret2.toString().replaceAll("😂", "\\\\colorlet{blackgreen1}{black}\\\\colorlet{cur}{blackgreen1!50!red1}\\\\color{cur}")
				.replaceAll( "😊",  "\\\\colorlet{blackgreen1}{green}\\\\colorlet{cur}{blackgreen1!50!red1}\\\\color{cur}")
				.replaceAll("&", "\\\\xglobal\\\\colorlet{tmpand}{.}&\\\\color{tmpand}")
				;

						}
	}
