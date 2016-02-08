package info.cyanac.plugin.bukkit;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class AntiWaterWalk implements Listener{
	
	public static String[] vls = {"","0"};
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		
		Player p = e.getPlayer();
		Location locUPlayer = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() - 1, p.getLocation().getZ());
		Block blockSOn = locUPlayer.getBlock();
		Block blockInPlayer = p.getLocation().getBlock();
		
		if(blockSOn.isLiquid() && !blockInPlayer.isLiquid()){
			
			if(!vls[0].equals(p.getName())){
				
				vls[1] = "0";
				
			}
			
			vls[0] = p.getName();	
			vls[1] = String.valueOf(Integer.parseInt(vls[1]) + 1);
			
			if(Integer.parseInt(vls[1]) > 29){
				
				p.kickPlayer(ChatColor.DARK_AQUA + "CyanAC detected Jesus/WaterWalker.");
				
			}
			
		}
		
	}

}
