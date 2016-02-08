package info.cyanac.plugin.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class AntiNoSlowdown implements Listener {
	
	public static String[] vls = {"","0"};
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		
		Player p = e.getPlayer();
		Block blockInPlayer = p.getLocation().getBlock();
		
		if(blockInPlayer.getType() == Material.WEB){
			
			if(p.getVelocity().equals(new Vector(0.0,0.0,0.0)) && !p.isSprinting()){
				
				if(!vls[0].equals(p.getName())){
					
					vls[1] = "0";
					
				}
				
				vls[0] = p.getName();	
				vls[1] = String.valueOf(Integer.parseInt(vls[1]) + 1);
				
				if(Integer.parseInt(vls[1]) > 99){
					
					p.kickPlayer(ChatColor.DARK_AQUA + "CyanAC detected NoSlowdown/AntiWeb.");
					
				}
				
				e.setCancelled(true);			
				
			}
			
		}
		
	}
	
}
