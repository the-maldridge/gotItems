
package GotItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public final class GotItem extends JavaPlugin {
    List<String> solvedQuest = new ArrayList<String>();

    @Override
    public void onEnable() {
	getLogger().info("Enabling GotItem");
    }
 
    @Override
    public void onDisable() {
	getLogger().info("Disabling GotItem");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	if(cmd.getName().equalsIgnoreCase("completeChallenge")) {
	    if(sender instanceof Player) {
		Player player = (Player)sender;
		solvedQuest.add(player.getName());
		player.sendMessage("You have completed the quest!");
		return true;
	    } else {
		sender.sendMessage("You must be a player!");
		return false;
	    }
	} else if(cmd.getName().equalsIgnoreCase("listCompletions")) {
	    if(solvedQuest.size() != 0) {
		sender.sendMessage("The following people have completed the challenge:");
		for(int i=0; i<solvedQuest.size(); i++) {
		    sender.sendMessage(i+1 +": "+solvedQuest.get(i));
		}
	    } else {
		sender.sendMessage("No one has completed the challenge yet");
	    }
	    return true;
	} else if(cmd.getName().equalsIgnoreCase("removePlayer")) {
	    if((args != null) && (args.length == 1)) {
		Player target = this.getServer().getPlayer(args[0]);
		if(target == null) {
		    sender.sendMessage(args[0] + "is not currently online");
		    return false;
		} else {
		    Iterator<String> iter = solvedQuest.iterator();
		    while(iter.hasNext()) {
			if(iter.next().equalsIgnoreCase(target.getName())) {
			    iter.remove();
			    sender.sendMessage("Successfully removed " + args[0]);
			} else {
			    sender.sendMessage(args[0] + " has not yet completed the challenge.");
			}
		    }
		    return true; //checked and user was either removed or not present
		}
	    }
	    return false; //wrong number of commands or serious error
	} else if(cmd.getName().equalsIgnoreCase("forceCompletion")) {
	    if((args != null) && (args.length == 1)) {
		Player target = this.getServer().getPlayer(args[0]);
		if(target == null) {
		    sender.sendMessage(args[0] + "is not currently online");
		    return false;
		} else {
		    solvedQuest.add(args[0]);
		    sender.sendMessage("Force completion for " + args[0]);
		    return true;
		}
	    }
	    return false; // could not force completion
	}
	return false; //no command matched
    }
    
}