package info.cyanac.plugin.bukkit;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class AntiFly implements Listener {
	
	public static String[] vls = {"","0"};
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		
		Player p = e.getPlayer();
		Location locUPlayer = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() - 1, p.getLocation().getZ());
		Block blockSOn = locUPlayer.getBlock();
		
		Boolean flying = false;
		
		if(blockSOn.isEmpty() && p.getFallDistance() == 0){
			
			flying = true;
			
		}else{
			
			flying = false;
			
		}
		
		if(flying && !p.getAllowFlight()){
			
			if(!vls[0].equals(p.getName())){
				
				vls[1] = "0";
				
			}
			
			vls[0] = p.getName();	
			vls[1] = String.valueOf(Integer.parseInt(vls[1]) + 1);
			
			if(Integer.parseInt(vls[1]) > 9){
				
				p.kickPlayer(ChatColor.DARK_AQUA + "CyanAC detected Fly/CreativeFly.");
				
			}
			
		}
		
	}

}
