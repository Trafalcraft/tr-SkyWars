package com.trafalcraft.SkyWars.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.trafalcraft.SkyWars.Main;

public class Messages {
	static JavaPlugin plugin = Main.plugin;
	
	public static String NO_PERMISSIONS = "§4Erreur §9§l> §r§bVous n'avez pas la permission de faire sa.";
	public static String Command_Use = "§4Erreur §l> §r§cutilisation de la commande: §6/sw ";
	public static String Prefix =  "§bSkyWars §9§l> §r§b ";
	public static String ERREUR =  "§4Erreur §l> §r§c ";
	
	  public static void getHelp(Player sender){
        sender.sendMessage("");
        sender.sendMessage("§3§l-------------------SkyWars-------------------");
        sender.sendMessage("§3/sw setup <nom de l'arene> §b- crée l'arène.");
        sender.sendMessage("§3/sw spawn<numero> §b- Configurer le lieu de spawn des joueurs.");
        sender.sendMessage("                       §3Version: §6" + plugin.getDescription().getVersion());
        sender.sendMessage("§3------------------------------------------------");
        sender.sendMessage("");
        Bukkit.getLogger().info("\u001B[31m" + sender.getName() + "\u001B[36m" + " a regarde la page d'aide." + "\u001B[0m");
	  }
	  
	  
	  
}
