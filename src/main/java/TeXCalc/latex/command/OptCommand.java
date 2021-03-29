package TeXCalc.latex.command;

public class OptCommand extends Command {

	public OptCommand(String name) {
		super(name);
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
