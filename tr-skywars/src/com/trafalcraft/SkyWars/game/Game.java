package com.trafalcraft.SkyWars.game;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.trafalcraft.SkyWars.Main;
import com.trafalcraft.SkyWars.util.BungeeCoord;
import com.trafalcraft.SkyWars.util.Locations;
import com.trafalcraft.SkyWars.util.Messages;

public class Game {
	static JavaPlugin plugin = Main.plugin;
    public static int task;
    public static int temps = 5;
	 public static ArrayList <Player> playerKickList = new ArrayList<Player>();
	  
	public static void PlayerJoin(Player p){
		p.sendMessage(Messages.Prefix+"Bienvenue dans le SkyWars.");
		p.setMetadata("swlobby", new FixedMetadataValue(plugin, p));
		while(Main.playerList.contains(p)){
			Main.playerList.remove(p);
		}
		Main.playerList.add(p);
		Random rand = new Random();
		int rd = rand.nextInt(Main.spawnList.size());
		p.teleport(Main.spawnList.get(rd));
		p.getWorld().setSpawnLocation((int) Main.spawnList.get(rd).getX(),(int) Main.spawnList.get(rd).getY(),(int) Main.spawnList.get(rd).getZ());
		for(int i = 0; i<Main.spawnList.size();i++){
			System.out.println(Main.spawnList.get(i));
		}
		Main.spawnList.remove(rd);
		p.setGameMode(GameMode.ADVENTURE);
		for(Player allp:Bukkit.getOnlinePlayers()){
			allp.sendMessage(Messages.Prefix+p.getName()+" a rejoint ("+(Main.playerList.size())+"/"+plugin.getConfig().getString("arene.nombre-spawns")+")");
		}
		p.getInventory().clear();
		ItemMeta meta;
		ItemStack itemStack = new ItemStack(Material.CHEST, 1);
		meta = itemStack.getItemMeta();
		meta.setDisplayName("§aClasses§r->Défaut");
		itemStack.setItemMeta(meta);
		p.getInventory().setItem(0, itemStack);
		itemStack = new ItemStack(Material.REDSTONE_BLOCK, 1);
		meta = itemStack.getItemMeta();
		meta.setDisplayName("§4Quitter");
		itemStack.setItemMeta(meta);
		p.getInventory().setItem(8, itemStack);
		
		
		onGameStart();
	}
	
	public static boolean onGameStart(){
		if(Main.playerList.size()/*+1*/ == 2 && Main.lobbyCooldown == false && Main.gameon == false){
			startLobbyCooldown();
			return true;
		}
		return false;
	}
	
	public static boolean startLobbyCooldown(){
		Main.lobbyCooldown = true;
		timer.lobby();
		return false;
	}
	
	public static boolean stopLobbyCooldown(){
		
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean endGame(){
		if(Main.playerList.size()<=1){
			Player pwinner = Main.playerList.get(0);
			String trait = "§l§2"+"\u2014"+"§1"+"\u2014";
			for(int i = 0;i<29;i++){
				trait = trait+("§l§2"+"\u2014"+"§1"+"\u2014");
			}
			pwinner.setVelocity(new Vector(pwinner.getVelocity().getX(), pwinner.getVelocity().getY()+10,pwinner.getVelocity().getZ()));
			pwinner.setNoDamageTicks(100);
			Main.gameCooldown = false;
			for(Player p:Bukkit.getOnlinePlayers()){
				p.sendMessage(trait);
				p.sendMessage("");
				p.sendMessage("§l§9"+ pwinner.getName()+"§r§b a gagné.");
				p.sendMessage("");
				p.sendMessage(trait);
				
				if(p == pwinner){
					p.sendTitle("§a§lVous avez gagné", "§aFelicitation");
				}else{
					p.sendTitle("§4§lVous avez perdu", "§aFelicitation à " + pwinner.getName());
				}

				Main.gamend = true;
			}
			temps = 5;
        	playerKickList.addAll(Bukkit.getOnlinePlayers());
			temps = Bukkit.getOnlinePlayers().size()+temps;
	        task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
	            
	            private int List;
	            private int OnlinePlayers = Bukkit.getOnlinePlayers().size();

				public void run() {
                    if(temps == 0){
                    	Bukkit.shutdown();
                        Bukkit.getServer().getScheduler().cancelTask(task);
                    }
	            	if(temps <= OnlinePlayers)
                        {
                           	if(playerKickList.get(List) != null && playerKickList.size()<=OnlinePlayers){
                           		BungeeCoord.sendPlayerToHub(playerKickList.get(List));

                           	}
                       		List = List+1;
                       		Bukkit.getLogger().warning(temps+"");
                           	                          	
                        	
                        	

                        }else{
                        	//plus tard les fusée
                        }
                   
		            	temps = temps-1;
	            }
	            },0, 20);
		}
		return false;
	}
	
	public static void initGame(){
		String s = plugin.getConfig().getString("arene.WorldBorder.centre");
		World world = Bukkit.getWorld(s.split(":")[0]);;
		WorldBorder wb = world.getWorldBorder();
		wb.setSize(plugin.getConfig().getDouble("arene.WorldBorder.taille"));
		wb.setCenter(Locations.StringToLoc(plugin.getConfig().getString("arene.WorldBorder.centre")));
		Main.gameon = true;
		Bukkit.getServer().getWhitelistedPlayers().clear();
		Bukkit.getServer().setWhitelist(true);
/*		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.hasMetadata("swjeux")){
				return;
			}
		}*/
	}
	
}
