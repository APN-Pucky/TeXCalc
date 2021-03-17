package TeXCalc.latex.command;

import com.github.weisj.darklaf.task.DefaultsAdjustmentTask;

import TeXCalc.latex.DefaultTeXable;

public class Command implements DefaultTeXable {

	String name;
	public Command(String name)
	{
		this.name = name;
	}

	@Override
	public String to(String s) {
		// TODO Auto-generated method stub
		String w = "";
		if(s.contains(" ")) {
			String[] args = s.split(" ");
			if(!args[args.length-1].trim().equals(""))
				w += "["+ args[args.length-1] + "]"; 
			for(int i =0; i  < args.length-1;++i) {
				//if(!args[i].trim().equals(""))
					w += "{" + args[i] + "}";
			}
		}
		else {
			 w = "{" + s + "}";
		}
		return "\\" + name + w + "\n";
	}

}
