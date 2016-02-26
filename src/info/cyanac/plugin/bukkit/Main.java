package info.cyanac.plugin.bukkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.persistence.GeneratedValue;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;
import org.omg.PortableServer.ServantRetentionPolicyValue;

public class Main extends JavaPlugin implements Listener {
	
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {

	    try {
	        Metrics metrics = new Metrics(this);
	        
	        metrics.start();
	        
	    } catch (IOException e) {
	    	
	    	
	    	
	    }
		
		getServer().broadcastMessage(ChatColor.DARK_AQUA + "CyanAC was enabled successfull");
		updateData();
		
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new AntiNoSlowdown(), this);
		getServer().getPluginManager().registerEvents(new AntiWaterWalk(), this);
		getServer().getPluginManager().registerEvents(new AntiFly(), this);
		
		getCommand("cyanac").setTabCompleter(new CyanACTabCompleter());
		
		this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {

			public void run() {
				
				AntiNoSlowdown.vls[0] = "";
				AntiNoSlowdown.vls[1] = "0";
				
				AntiWaterWalk.vls[0] = "";
				AntiWaterWalk.vls[1] = "0";
				
				AntiFly.vls[0] = "";
				AntiFly.vls[1] = "0";
				
			}
			}, 3L, 3L);

		
	}
	
	@Override
	public void onDisable() {

		getServer().broadcastMessage(ChatColor.DARK_AQUA + "CyanAC was disabled successfull");
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player p = (Player)sender;
		
		if(command.getName().equalsIgnoreCase("cyanac")){
			
			if(args.length == 0){
				
				p.sendMessage(ChatColor.DARK_AQUA + "CyanAC: You must enter a subcommand. Type /cyanac help for help");
				
			}else if(args[0].equalsIgnoreCase("help")){
				
				p.sendMessage(ChatColor.DARK_AQUA + "CYANAC-HELP:");
				p.sendMessage(ChatColor.DARK_AQUA + "/cyanac help: Displays this help text");
				p.sendMessage(ChatColor.DARK_AQUA + "/cyanac resync: Resync the files");
				p.sendMessage(ChatColor.DARK_AQUA + "/cyanac license addkey <Key>: Set the license key");
				p.sendMessage(ChatColor.DARK_AQUA + "/cyanac license addpage <URL>: Set the servers page in the cyanacs server registry");
				
			}else if(args[0].equalsIgnoreCase("resync")){
				
				updateData();
				
			}else if(args[0].equalsIgnoreCase("license") && args[1].equalsIgnoreCase("addkey")){
				
				File licensekeyfile = new File("plugins/CyanAC/license.key");
				
				if(licensekeyfile.exists()){
					
					licensekeyfile.delete();
					
				}
				
				try {
					
					licensekeyfile.createNewFile();
					
					PrintWriter licensekeyfilewriter = new PrintWriter("plugins/CyanAC/license.key", "UTF-8");
					licensekeyfilewriter.println(args[2]);
					licensekeyfilewriter.close();
					
				} catch (IOException e) {
					
					e.printStackTrace();
					
				}
				
				
			}
			
		}
		
		return false;
		
	}
	
	public boolean updateData(){
       
		try {
		    Metrics metrics = new Metrics(this);

		    Graph internalActionsGraph = metrics.createGraph("Internal Actions");

		    internalActionsGraph.addPlotter(new Metrics.Plotter("Data Update") {
		    	
		            @Override
		            public int getValue() {
		                    return 1; // Number of players who used a diamond sword
		            }

		    });
		    
		    metrics.start();
		    
		} catch (IOException e) {
		}
		
		getServer().broadcastMessage(ChatColor.DARK_AQUA + "CyanAC: Starting Cache-Update...");
		
		try {
			
			URL file = new URL("http://api.cyanac.info/bl/words/get/");
			
			File cacheDir = new File("plugins/CyanAC/cache");
			
			cacheDir.mkdirs();
			
            InputStream is = file.openStream();
            File finaldest = new File(cacheDir + "/wbl.txt");
            finaldest.getParentFile().mkdirs();
            finaldest.createNewFile();
            OutputStream os = new FileOutputStream(finaldest);
            byte data[] = new byte[1024];
            int count;
            while ((count = is.read(data, 0, 1024)) != -1) {
                os.write(data, 0, count);
            }
            os.flush();
            is.close();
            os.close();
            
            getServer().broadcastMessage(ChatColor.DARK_AQUA + "CyanAC: Cache-Update successfull");
            
        } catch (Exception ec) {
            ec.printStackTrace();
            getServer().broadcastMessage(ChatColor.DARK_AQUA + "CyanAC: Cache-Update failed");
        }
		return false;
		
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChat(PlayerChatEvent e){
		
		Player p = e.getPlayer();
		String message = e.getMessage();
		
		try (BufferedReader br = new BufferedReader(new FileReader("plugins/CyanAC/cache/wbl.txt"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       
		    	if(message.contains(line)){
		    		
		    		e.setCancelled(true);
		    		
		    		getServer().broadcastMessage(p.getDisplayName() + ChatColor.DARK_AQUA + " tried to write a forbidden word.");
		    		
		    	}
		    	
		    }
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	@EventHandler
	public void onJoin(PlayerLoginEvent e){
		
		Player p = e.getPlayer();
		String name = p.getDisplayName();
		
		try (BufferedReader br = new BufferedReader(new FileReader("plugins/CyanAC/cache/wbl.txt"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       
		    	if(name.contains(line)){
		    		
		    		e.disallow(Result.KICK_OTHER, ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "CyanAC: " + ChatColor.RESET + "" + ChatColor.DARK_AQUA + "Your name contains a forbidden word.");
		    		
		    	}
		    	
		    }
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

}
