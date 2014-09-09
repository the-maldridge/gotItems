package GotItem.GotItem;
     
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public final class GotItem extends JavaPlugin {
    @Override
	public void onEnable() {
	getLogger().info("Enabling GotItem");
    }
 
    @Override
	public void onDisable() {
	getLogger().info("Disabling GotItem");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	if(cmd.getName().equalsIgnoreCase("completeChallenge")) {
	    if(sender instanceof Player) {
		Player player = (Player)sender;
		// do something
	    } else {
		sender.sendMessage("You must be a player!");
		return false;
	    }
	    // do something
	    return false;
	}
	return false;
    }
}