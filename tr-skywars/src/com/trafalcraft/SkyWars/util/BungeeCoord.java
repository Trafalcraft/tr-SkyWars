package com.trafalcraft.SkyWars.util;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.trafalcraft.SkyWars.Main;

public class BungeeCoord implements Listener {
	
//	  public static ArrayList <Player> playerKickList = new ArrayList<Player>();
	
	public static void sendPlayerToHub(Player p) {
		final ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF("jeux");
		if (Bukkit.getOnlinePlayers().size() > 0) {
			p.sendPluginMessage(Main.plugin, "BungeeCord", out.toByteArray());
		}
	}
	
	public static void sendOtherPlayerToHub(String name, Player p) {
		final ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
//		out.writeUTF(name);
		out.writeUTF("jeux");
		if (Bukkit.getOnlinePlayers().size() > 0) {
			p.sendPluginMessage(Main.plugin, "BungeeCord", out.toByteArray());
		}
	}
/*	public static void sendAllPlayerToHub(){
    	//String joueurs = Bukkit.getOnlinePlayers().toString();
    	playerKickList.addAll(Bukkit.getOnlinePlayers());
    	//String joueur = joueurs.split(",")[Bukkit.getOnlinePlayers().size()-1].replace("[", "").replace("]", "").replace("}", "").replace("CraftPlayer{name=", "");

		Bukkit.getLogger().info(joueur);
    	Player p = Bukkit.getServer().getPlayer(joueur);
    	Bukkit.getLogger().warning(p+" a été teleporté");
    	p.sendMessage("tu va être teleporté");
    	BungeeCoord.sendPlayerToHub(p);
	}*/

	
}
