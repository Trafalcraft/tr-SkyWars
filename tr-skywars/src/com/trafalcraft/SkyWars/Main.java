package com.trafalcraft.SkyWars;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.trafalcraft.SkyWars.Listener.PlayerListener;
import com.trafalcraft.SkyWars.util.BungeeCoord;
import com.trafalcraft.SkyWars.util.Locations;
import com.trafalcraft.SkyWars.util.Messages;
import com.trafalcraft.SkyWars.util.Msg;
import com.trafalcraft.SkyWars.util.savekits;
import com.trafalcraft.SkyWars.game.Game;

public class Main extends JavaPlugin /*implements PluginMessageListener*/{

	
	public static Main plugin;
	public int spawn;
	public static boolean gameon;
	public static boolean gamend;
	public static boolean lobbyCooldown;
	public static boolean gameCooldown;
	public static BungeeCoord	bungeeHandler;
	
	  public static ArrayList <Location> spawnList = new ArrayList<Location>();
	  public static ArrayList <Player> playerList = new ArrayList<Player>();
	  public static List<String> classelore = new LinkedList<String>();//= new List<String>();
	  
	@Override
	public void onEnable(){
		String version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
		Bukkit.getLogger().info(version);
		version = Bukkit.getVersion();
		Bukkit.getLogger().info(version);
		gameon = false;
		gamend = false;
		lobbyCooldown = false;
		gameCooldown = false;
		plugin = this;
		
		plugin = this;
		plugin.getConfig().options().copyDefaults(true);
		plugin.saveDefaultConfig();
		plugin.reloadConfig();
		spawn = plugin.getConfig().getInt("arene.nombre-spawns");
		
		Bukkit.getServer().getPluginManager().registerEvents(bungeeHandler = new BungeeCoord(), plugin);
	    this.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
	  //  this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		
	
		spawnList.clear();
		try{
			for(int i = 1;i<=spawn;i++){
				spawnList.add(Locations.StringToLoc(plugin.getConfig().getString("arene.spawns."+i)));
			}
		}catch(Exception e){
			Bukkit.getLogger().info("\u001B[31m"+"Il n'y a pas de spawn dans l'arene de SkyWars" +"\u001B[0m");
		}
		Bukkit.getServer().setWhitelist(false);
	}
	
	@Override
	public void onDisable(){
		for(Player p : Bukkit.getServer().getOnlinePlayers()){
			if(p.hasMetadata("swjeux")){
				p.removeMetadata("swjeux", plugin);
			}
			if(p.hasMetadata("swspec")){
				p.removeMetadata("swspec", plugin);
			}
			if(p.hasMetadata("swlobby")){
				p.removeMetadata("swlobby", plugin);
			}
		}
	}

	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[]args){
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("Skywars")){
			if(args.length == 0){
			Messages.getHelp(p);
			return true;
			}
			if(args[0].equalsIgnoreCase("help")){
				Messages.getHelp(p);
				return true;
			}
			if(args[0].equalsIgnoreCase("reload")){
				plugin.reloadConfig();
				p.sendMessage(Msg.Prefix+""+Msg.Reload_Ok);
				return true;
			}
			
			//setup
			if(!p.hasMetadata("swjeux")||!p.hasMetadata("swspec")||!p.hasMetadata("swlobby")){
				if(args[0].equalsIgnoreCase("addspawn")){
					if(p.hasPermission("sw.setup")){
						if(args.length == 1){
							String l = Locations.LocationToString(p.getLocation());
							spawn = spawn+1;
							plugin.getConfig().set("arene.spawns."+spawn, l);
							p.sendMessage(Msg.Prefix +Msg.set_spawn_success.toString().replace("$spawn", spawn+"") /*"Le spawn "+spawn+" a bien été configuré"*/);
							plugin.getConfig().set("arene.nombre-spawns", spawn);
							plugin.saveConfig();
							plugin.reloadConfig();
							spawnList.clear();
							for(int i = 1;i<=spawn;i++){
								spawnList.add(Locations.StringToLoc(plugin.getConfig().getString("arene.spawns."+i)));
							}
							return true;
						}else{
							p.sendMessage(Msg.Command_Use + "addspawn");
						}
						return true;
					}else{
						Messages.getHelp(p);
						return true;
					}
				}
				
				if(args[0].equalsIgnoreCase("setspawn")||args[0].equalsIgnoreCase("editspawn")){
					if(p.hasPermission("sw.setup")){
						if(args.length == 2){
							int spawn2 = 0;
							String l = Locations.LocationToString(p.getLocation());
							try {
							spawn2 = Integer.parseInt(args[1]);
							}
							catch (NumberFormatException nfe) { 
								p.sendMessage(Msg.ERREUR + Msg.String_to_int.toString().replace("$MaxPlayers", spawn+""));
							//p.sendMessage(Msg.ERREUR + "La valeur du spawn doit etre numérique et comprise entre 0 et "+ spawn);
							return false;
							}
							if(spawn >= spawn2){
								plugin.getConfig().set("arene.spawns."+spawn2, l);
								plugin.saveConfig();
								plugin.reloadConfig();
								spawnList.clear();
								for(int i = 1;i<=spawn;i++){
									spawnList.add(Locations.StringToLoc(plugin.getConfig().getString("arene.spawns."+i)));
								}
								p.sendMessage(Msg.Prefix + Msg.set_spawn_success.toString().replace("$spawn", spawn2+""));
								//p.sendMessage(Messages.Prefix + "Le spawn "+spawn2+" a bien été configuré");
							}else{
								p.sendMessage(Msg.ERREUR + Msg.String_to_int.toString().replace("$MaxPlayers", spawn+""));
//								p.sendMessage(Messages.ERREUR + "La valeur du spawn doit etre numérique et comprise entre 0 et "+ spawn);
								return false;
							}
						}else{
							p.sendMessage(Messages.Command_Use + "setspawn <numéro du spawn>");
						}
						return true;
					}else{
						Messages.getHelp(p);
					}
				}
				
				if(args[0].equalsIgnoreCase("delspawn")){
					if(p.hasPermission("sw.setup")){
						if(args.length == 2){
							int spawn2 = 0;
							try {
							spawn2 = Integer.parseInt(args[1]);
							}
							catch (NumberFormatException nfe) { 
								p.sendMessage(Msg.ERREUR + Msg.String_to_int.toString().replace("$MaxPlayers", spawn+""));						
//								p.sendMessage(Messages.ERREUR + "La valeur du spawn doit etre numérique et comprise entre 0 et "+ spawn);
							return false;
							}
							if(spawn >= spawn2){
								//plugin.getConfig().remove("arene.spawns."+spawn2, "");
								int spawn3 = spawn2;
								for(int i = spawn2; spawn >= i; i++){
									spawn3 = spawn3+1;
									plugin.getConfig().set("arene.spawns."+i,plugin.getConfig().getString("arene.spawns."+spawn3));
								}							
								spawn = spawn-1;
								plugin.getConfig().set("arene.nombre-spawns", spawn);
								plugin.saveConfig();
								plugin.reloadConfig();
								spawnList.clear();
								for(int i = 1;i<=spawn;i++){
									spawnList.add(Locations.StringToLoc(plugin.getConfig().getString("arene.spawns."+i)));
								}
								p.sendMessage(Msg.Prefix + Msg.suppr_spawn_success.toString().replace("$spawn", spawn2+"").replace("$rspawn", spawn+""));
//								p.sendMessage(Msg.Prefix + "Le spawn "+spawn2+" a bien été supprimé, il reste: "+spawn+" spawn");
							}else{
								p.sendMessage(Msg.ERREUR + Msg.String_to_int.toString().replace("$MaxPlayers", spawn+""));
								//p.sendMessage(Messages.ERREUR + "La valeur du spawn doit etre numérique et comprise entre 0 et "+ spawn);
								return false;
							}
						}else{
							p.sendMessage(Messages.Command_Use + "delspawn <numéro du spawn>");
						}
						return true;
					}else{
						Messages.getHelp(p);
					}
				}
				
				if(args[0].equalsIgnoreCase("setspec")){
					if(p.hasPermission("sw.setup")){
						if(args.length == 1){
							String l = Locations.LocationToString(p.getLocation());
							plugin.getConfig().set("arene.spawns.spec", l);
							plugin.saveConfig();
							plugin.reloadConfig();
							p.sendMessage(Msg.Prefix +""+Msg.spawn_spec);
//							p.sendMessage(Messages.Prefix + "Le spawn des spectateurs a bien été configuré");
							return true;
						}else{
							p.sendMessage(Messages.Command_Use + "setspec");
						}
						return true;
					}else{
						Messages.getHelp(p);
						return true;
					}
				}
				
				if(args[0].equalsIgnoreCase("setwb")){
					if(p.hasPermission("sw.setup")){
						if(args.length == 2){
							try {
								int wbsize = Integer.parseInt(args[1]);
								if(wbsize <= 0){
									p.sendMessage(Msg.ERREUR +""+Msg.set_size_Wb);
//									p.sendMessage(Messages.ERREUR + "La valeur du worldborder doit etre numérique et comprise entre 1 et 32 767");
									return false;
								}
								plugin.getConfig().set("arene.WorldBorder.taille", wbsize);
								Location l = p.getLocation();
								int X1 = (int) p.getLocation().getX()+(wbsize/2);
								int Z1 = (int) p.getLocation().getZ()+(wbsize/2);
								l.setX(X1);
								l.setZ(Z1);
								plugin.getConfig().set("arene.zones.spec.pos1", Locations.LocationToString(l));
								int X2 = (int) p.getLocation().getX()-(wbsize/2);
								int Z2 = (int) p.getLocation().getZ()-(wbsize/2);
								l.setX(X2);
								l.setZ(Z2);
								plugin.getConfig().set("arene.zones.spec.pos2", Locations.LocationToString(l));
							}catch (NumberFormatException nfe) { 
								p.sendMessage(Msg.ERREUR +""+Msg.set_size_Wb);
//								p.sendMessage(Messages.ERREUR + "La valeur du spawn doit etre numérique et comprise entre 1 et 32 767");
								return false;
							}
							plugin.getConfig().set("arene.WorldBorder.centre", Locations.LocationToString(p.getLocation()));
							plugin.saveConfig();
							plugin.reloadConfig();
							
							p.sendMessage(Msg.Prefix +""+ Msg.set_wb);
							return true;
						}else{
							p.sendMessage(Messages.Command_Use + "setwb <taille>");
							return false;
						}
					}
				}
				
				if(args[0].equalsIgnoreCase("addkit")){
					if(p.hasPermission("sw.setup")){
						if(args.length == 2){
							if(plugin.getConfig().getString("arene.kits."+args[1]) == null){
								//savekits.inventoryToString(p);
								String displayName = args[1].replace("&", "§");
								String name = args[1].replace("&", "");
								plugin.getConfig().set("arene.kits."+name+".enable" ,true);
								plugin.getConfig().set("arene.kits."+name+".icon" ,"BARRIER");
								plugin.getConfig().set("arene.kits."+name+".name" ,displayName);
								plugin.getConfig().set("arene.kits."+name+".items" , savekits.inventoryToString(p));
								plugin.getConfig().createSection("arene.kits."+name+".lore");
								classelore.clear();
								classelore.add("");
								classelore.add("test");
								plugin.getConfig().set("arene.kits."+name+".lore", classelore);
								plugin.saveConfig();
								plugin.reloadConfig();
								p.sendMessage(Msg.Prefix + Msg.setclasses.toString().replace("$classe", displayName));
//								p.sendMessage("La classe "+displayName+" §ra bien été sauvegardé");
							}
							return true ;
						}else{
							p.sendMessage(Messages.Command_Use + "addkit <nom du kit>");
							return false;
						}
					}else{
						Messages.getHelp(p);
						return false;
					}
				}
				if(args[0].equalsIgnoreCase("setkit")){
					if(p.hasPermission("sw.setup")){
						@SuppressWarnings("unused")
						boolean success = false; // sert a verifier si un kit éxiste
						for(String aclass : plugin.getConfig().getConfigurationSection("arene.kits.").getKeys(false)){
							if(args[1].equalsIgnoreCase(aclass)){
								success = true;
							}
						}
						if(success = false){
							p.sendMessage(Msg.ERREUR + Msg.classes_no_exist.toString().replace("$classe", args[1]));
//							p.sendMessage(Messages.Prefix+"La classe "+args[1]+" n'existe pas");
							return false;
						}
						if(args.length == 4){
							if(args[2].equalsIgnoreCase("icon")){
								try{
					            	@SuppressWarnings("unused")
									Material m = Material.getMaterial(args[3]);	//verifie si le joueur a bien mis un item en argument
					            	plugin.getConfig().set("arene.kits."+args[1]+".icon", args[3]);
					            	plugin.saveConfig();
									p.sendMessage(Messages.Prefix+"L'icon a bien été changé");
									return true;
								}catch(Exception e){
									p.sendMessage(Messages.ERREUR+"l'icon doit etre un Material");
									return false;
								}
							}
							if(args[2].equalsIgnoreCase("enable")){
								if(args[3].equalsIgnoreCase("true")){
					            	plugin.getConfig().set("arene.kits."+args[1]+".enable", true);
					            	plugin.saveConfig();
					            	return true;
								}
								if(args[3].equalsIgnoreCase("false")){
					            	plugin.getConfig().set("arene.kits."+args[1]+".enable", false);
					            	plugin.saveConfig();
					            	return true;
								}
							}
							
						}else{
						p.sendMessage(Messages.Command_Use + "set <nom du kit> <nom du parametre> <parametre>");
						return false;
						
					}
					}else{
						Messages.getHelp(p);
					}
				}
			}
			
			
			//jeux
			if(args[0].equalsIgnoreCase("join")){
					if(args.length == 1){
						if(p.hasMetadata("swjeux")||p.hasMetadata("swspec")||p.hasMetadata("swlobby")){
							p.sendMessage(Messages.ERREUR+"Vous êtes déja en jeux, utilisé /sw leave pour quitter");
							return false;
						}else{
							if(playerList.size()/*+1*/ <= spawn){
								Game.PlayerJoin(p);
//								p.setMetadata("swlobby", new FixedMetadataValue(plugin, p));
//								while(playerList.contains(p)){
//									playerList.remove(p);
//								}
//								playerList.add(p);
//								Random rand = new Random();
//								int rd = rand.nextInt(spawnList.size());
//								p.teleport(spawnList.get(rd));
//								spawnList.remove(rd);
//								p.setGameMode(GameMode.ADVENTURE);
//								for(Player allp:Bukkit.getOnlinePlayers()){
//									allp.sendMessage(Messages.Prefix+p.getName()+" a rejoin ("+(playerList.size())+"/"+plugin.getConfig().getString("arene.nombre-spawns")+")");
//								}
//								Game.onGameStart();
							}else{
								p.sendMessage(Messages.ERREUR+"l'arène est pleine");
							}
						}
					}else{
						p.sendMessage(Messages.Command_Use + "join");
					}
					return true;
			}
			
/*			if(args[0].equalsIgnoreCase("spec")){
				if(args.length == 1){
					if(p.hasMetadata("swjeux")||p.hasMetadata("swspec")||p.hasMetadata("swlobby")){
						p.sendMessage(Messages.ERREUR+"Vous êtes déja en jeux, utilisé /sw leave pour quitter");
						return false;
					}else{
						if(//la partie est déja lancé et il reste de la place){
							p.setMetadata("swspec", new FixedMetadataValue(plugin, p));
							int rd = rand.nextInt(playerList.size());
							Random rand = new Random();
							playerList.add(p);
							p.teleport(playerList.get(rd));
							p.setGameMode(GameMode.SPECTATOR);
							p.sendMessage(Messages.Prefix+"Vous avez bien été teleporté");
						}else{
							p.sendMessage(Messages.ERREUR+"l'arène est pleine");
						}
					}
				}else{
					p.sendMessage(Messages.Command_Use + "join");
				}
				return true;
		}*/
			if(args[0].equalsIgnoreCase("leave")){
				if(args.length == 1){
					if(p.hasMetadata("swjeux")||p.hasMetadata("swspec")||p.hasMetadata("swlobby")){
								if(p.hasMetadata("swjeux")){
									p.removeMetadata("swjeux", plugin);
									for(Player allp:Bukkit.getOnlinePlayers()){
										if(allp != p){
											allp.sendMessage(Messages.Prefix+p.getName()+" est partie, il reste "+(Main.playerList.size()+1)+" joueurs");	
										}
									}
									Main.playerList.remove(p);
									Game.endGame();
								}
								if(p.hasMetadata("swspec")){
									p.removeMetadata("swspec", plugin);
								}
								if(p.hasMetadata("swlobby")){
									p.removeMetadata("swlobby", plugin);
									for(Player allp:Bukkit.getOnlinePlayers()){
										if(allp != p){
											allp.sendMessage(Messages.Prefix+p.getName()+" a quitté ("+(playerList.size()-1)+"/"+plugin.getConfig().getString("arene.nombre-spawns")+")");	
										}
									}
									spawnList.add(p.getLocation());
								}
								//teleporter le joueur sur un autre serveur
								if(playerList.contains(p)){
									playerList.remove(p);
								}
								p.sendMessage(Messages.Prefix+"Vous avez quitté l'arêne");
								if(Main.playerList.size()+1 < 2 && Main.lobbyCooldown == true && Main.gameon == false){
									Main.lobbyCooldown = false;
								}
								BungeeCoord.sendPlayerToHub(p);
					}else{
						p.sendMessage(Messages.ERREUR+"Vous n'êtes pas en jeux, utilisé /sw join pour rejoindre une partie");
						return false;
					}
				}else{
					p.sendMessage(Messages.Command_Use + "leave");
				}
				return true;
		}
			if(args[0].equalsIgnoreCase("test")){
				ItemStack itemstack = p.getInventory().getItemInMainHand();
				p.sendMessage(itemstack.getData()+"");
				//savekits.StringToInvetory(p, "test6");
				//Title.sendTitle(p, "§6test", "test2", 20, 40, 20);
			}
		}
		Messages.getHelp(p);
		return false;
	}
	
}
