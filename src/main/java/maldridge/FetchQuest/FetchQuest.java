
package maldridge.FetchQuest;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;

public final class FetchQuest extends JavaPlugin {
    List<String> solvedQuest = new ArrayList<String>();
    List<String> questItems = new ArrayList<String>();

    @Override
    public void onEnable() {
	getLogger().info("Enabling FetchQuest");
	questItems = this.getConfig().getStringList("thingsToGet");
        getLogger().info("Must get a " + questItems);
    }
 
    @Override
    public void onDisable() {
	getLogger().info("Disabling FetchQuest");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	if(cmd.getName().equalsIgnoreCase("completeQuest")) {
	    if(sender instanceof Player) {
		Player player = (Player)sender;
		boolean hasItems = true;
		boolean alreadySolved = false;

		for(int i=0; i<questItems.size(); i++) {
		    if(!player.getInventory().contains(Material.getMaterial(questItems.get(i)))) {
			//player did not have the specified item, set the flag and break
			hasItems = false;
			player.sendMessage(ChatColor.YELLOW + "It looks like you don't have any " + questItems.get(i) + " with you.");
			break;
		    } // otherwise keep checking
		}
		
		//see if they already solved the quest
		for(int i=0; i<solvedQuest.size(); i++) {
		    if(solvedQuest.get(i) == player.getName()) {
			sender.sendMessage("You've already solved the quest!");
			alreadySolved = true;
		    }
		}

		//if the two conditions have been met, then score them for the challenge
		if(hasItems && !alreadySolved) {
		    if(solvedQuest.size() == 0) {
			this.getServer().broadcastMessage(ChatColor.GREEN + "Player " + player.getName() + " has completed the quest and won the prize!");
		    }
		    solvedQuest.add(player.getName());
		    player.sendMessage("You have completed the quest!");
		}

		//we've made it this far and so all the checks have run without error
		//go ahead and signal command success
		return true;

	    } else {
		//only players can solve the quest
		sender.sendMessage("You must be a player!");
		return false;
	    }
	    //======================================================================================
	} else if(cmd.getName().equalsIgnoreCase("listCompletions")) {
	    if(solvedQuest.size() != 0) {
		sender.sendMessage("The following people have completed the challenge:");

		//show completions in order
		for(int i=0; i<solvedQuest.size(); i++) {
		    sender.sendMessage(i+1 + ": " + ChatColor.BOLD + solvedQuest.get(i));
		}
	    } else {
		//don't show the list if no one has solved it yet
		sender.sendMessage("No one has completed the challenge yet");
	    }
	    return true;
	    //======================================================================================
	} else if(cmd.getName().equalsIgnoreCase("removePlayer")) {
	    if((args != null) && (args.length == 1)) {
		Player target = this.getServer().getPlayer(args[0]);
		if(target.hasPermission("fetchquest.removePlayer")) {
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
		} else {
		    sender.sendMessage(ChatColor.RED + "You don't have that permission!");
		    return true;
		}
	    } else {
		return false; //wrong number of operands
	    }
	    //======================================================================================
	} else if(cmd.getName().equalsIgnoreCase("forceCompletion")) {
	    if((args != null) && (args.length == 1)) {
		Player target = this.getServer().getPlayer(args[0]);
		if(target.hasPermission("fetchquest.forceCompletion")) {
		    if(target == null) {
			sender.sendMessage(args[0] + "is not currently online");
			return false;
		    } else {
			solvedQuest.add(args[0]);
			sender.sendMessage("Force completion for " + args[0]);
			return true;
		    }
		} else {
		    sender.sendMessage(ChatColor.RED + "You don't have that permission!");
		    return true;
		}
	    } else {
		return false; //could not force completion
	    }
	} else {
	    return false; //no command matched
	}
    }    
}