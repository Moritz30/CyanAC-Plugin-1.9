package info.cyanac.plugin.bukkit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CyanACTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		
		if(command.getName().equalsIgnoreCase("cyanac")){
			
			List<String> tabCompletionList = new ArrayList();
			
			tabCompletionList.add("help");
			tabCompletionList.add("resync");
			tabCompletionList.add("license");
			
			return tabCompletionList;
			
		}
		
		return null;
		
	}

	
	
}
