package com.trafalcraft.SkyWars.game;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import com.trafalcraft.SkyWars.Main;
import com.trafalcraft.SkyWars.util.Locations;
import com.trafalcraft.SkyWars.util.Messages;
import com.trafalcraft.SkyWars.util.ScoreBoard;
import com.trafalcraft.SkyWars.util.savekits;

public class timer {
	
	static JavaPlugin plugin = Main.plugin;
	
    public static int task;
    public static int temps = 30;
    public static int temps2;
    public static double dtemps;
    public static int couleur;
    private static int seconde;
    private static String seconde2;
    
    
    
	public static boolean lobby(){
	    temps = 30;
        task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            
            @SuppressWarnings("deprecation")
			public void run() {

                    for(Player pl : Bukkit.getOnlinePlayers())
                    {
                    		if(Main.lobbyCooldown == false || Main.playerList.size()/*+1*/ < 2 ){
                    			Main.lobbyCooldown = false;
                                Bukkit.getServer().getScheduler().cancelTask(task);
                                pl.sendMessage(Messages.Prefix+"Un joueur a quitté compte à rebours annulé!");
                                temps = 30;
                                return;
                    		}
                            pl.setLevel(temps);
                            if(temps == 30 || temps == 20 || temps == 10 || (temps <= 5 && temps != 0))
                            {
                                    pl.sendMessage(Messages.Prefix+"La partie commence dans : "+temps+ "secondes");
                                    pl.sendTitle("§6" + temps, "");
                					pl.playSound(pl.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
                            }if(temps == 0){
                            		pl.playSound(pl.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                                    Location pll = pl.getLocation();
                                    pll.setY(pll.getY()-1);
                                	pll.getBlock().setType(Material.AIR, true);
                                	pl.setGameMode(GameMode.SURVIVAL);
                                	pl.removeMetadata("swlobby", plugin);
    								pl.setMetadata("swjeux", new FixedMetadataValue(plugin, pl));
                               //     pll.getBlock().setType(Material.GLASS, true);
                               //     pll.getBlock().breakNaturally();
                                    //TODO Téléportation
//                                    pl.teleport(Bukkit.getWorld("world").getSpawnLocation());
                                    pl.sendMessage(Messages.Prefix+"La partie commence !");
                                    pl.closeInventory();
//                                    pl.getInventory().clear();
                                    pl.setCompassTarget(Locations.StringToLoc(plugin.getConfig().getString("arene.WorldBorder.centre")));
                                    pl.setAllowFlight(false);
                                    pl.setExp(0);
                                    pl.setFoodLevel(20);
                                    pl.setHealth(20);
                                    pl.setNoDamageTicks(100);
                                    pl.setSaturation(5);
                                    pl.getWorld().setAutoSave(false);
                                    pl.getWorld().setDifficulty(Difficulty.HARD);
                                    pl.getWorld().setPVP(true);
                                    ItemMeta  meta = pl.getInventory().getItem(0).getItemMeta();
                                    String meta2 = meta.getDisplayName().toString();
//                					pl.sendMessage(meta2);
//                					pl.sendMessage(meta2.split("->")[1]);
                                    if(meta2.split("->")[1] == null){
//                                    	pl.sendMessage("test1");
                    					savekits.StringToInvetory(pl, "Défaut");
                                    }else{
//                                    	pl.sendMessage("test2");
                    					savekits.StringToInvetory(pl, meta2.split("->")[1]);
                                    }
                                    pl.sendTitle("§6Bonne chance","");
                            }
                    }
                    if(temps == 0){
                        Bukkit.getServer().getScheduler().cancelTask(task);
						Game.initGame();
    					jeux();
    					
    					return;
                    }
                	if(temps != 0){
	                        temps = temps - 1;
                	}
                   
            }
    }, 0, 20);
		return false;
	}
	
	public static boolean jeux(){
        
		temps = 3600;
		couleur = 0;
		temps2 = 0;
		dtemps = 0;
		seconde = 0;
		seconde2 = null;
		
        task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            
            public void run() {
            	
        		dtemps = temps/60;
        		//if((dtemps - temps)>=0.5){
        		//	dtemps = dtemps - 0.5;
        		//}
        		temps2 = (int)dtemps;
        		seconde = (temps-(temps2*60));
        		if(seconde < 10){
        			seconde2 = "0"+seconde;
        		}else{
        			seconde2 = seconde+"";
        		}
                
        			for(Player pl : Bukkit.getOnlinePlayers())
                    {
 /*                   		if(Main.gameCooldown == false){
                                Bukkit.getServer().getScheduler().cancelTask(task);
                                pl.sendMessage(Messages.Prefix+"La partie est terminé #winner et le gagnant!");
                                temps = 30;
                                return;
                    		}*/
        					if(Main.gamend == true){
                                Bukkit.getServer().getScheduler().cancelTask(task);
        					}
                    		if(temps == 3600){
                                pl.sendMessage(Messages.Prefix+"La partie à commencé vous avez : "+temps2+ "minutes pour tuer tous le monde");
                                pl.playSound(pl.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
                    		}
                            if(temps == 1800 || temps == 1200 || temps == 600 || temps == 300 ||(temps <= 10 && temps != 0))
                            {
                                    pl.sendMessage(Messages.Prefix+"Il reste : "+temps2+":"+ seconde2);
                                    pl.playSound(pl.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
                            }if(temps == 0){
                                    Bukkit.getServer().getScheduler().cancelTask(task);
                                    pl.playSound(pl.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                                	pl.setGameMode(GameMode.SPECTATOR);
                               //     pll.getBlock().setType(Material.GLASS, true);
                               //     pll.getBlock().breakNaturally();
                                    //TODO Téléportation
//                                    pl.teleport(Bukkit.getWorld("world").getSpawnLocation());
                                    pl.sendMessage(Messages.Prefix+"Attention les mur se rapproche!");
                                    Endtimer();
                            }
                        	if(temps != 0){
                        		couleur = couleur + 0x1 ;
                        		ScoreBoard.o.setDisplayName("§"+couleur+"SkyWars: §6" + temps2+":"+seconde2);
                        		
                        		if(couleur == 9){
                        			couleur = 0;
                        		}
                        			//Score score = ScoreBoard.o.getScore(pl.getName());
                            		ScoreBoard.o.getScore(pl.getName()).setScore(0);
                            		ScoreBoard.sendScoreBoard();
                        	}
                    }
                	if(temps != 0){
                        temps = temps - 1;
                	}              
                   
            }
    }, 0, 20);
		return false;
	}
	
	public static void Endtimer(){
        task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            
            public void run() {
        		String s = plugin.getConfig().getString("arene.WorldBorder.centre");
        		World world = Bukkit.getWorld(s.split(":")[0]);
        		WorldBorder wb = world.getWorldBorder();
        		if(wb.getSize() > 5){
            		wb.setSize(wb.getSize()-1);
        		}else{
        			for(Player p:Bukkit.getOnlinePlayers()){
        				p.getWorld().setStorm(true);
        				p.getWorld().createExplosion(p.getLocation(), 1);
        			}
        		}
            }
        }, 0, 10);
	}

	
}
