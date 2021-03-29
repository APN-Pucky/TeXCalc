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
			 w = "{" + s + "}";
		return "\\" + name + w + "\n";
	}

}
