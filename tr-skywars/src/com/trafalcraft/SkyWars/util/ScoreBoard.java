package com.trafalcraft.SkyWars.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.trafalcraft.SkyWars.Main;

public class ScoreBoard {
	
	static ScoreboardManager manager = Bukkit.getScoreboardManager();
	static Scoreboard scoreinGame = manager.getNewScoreboard();
	public static Objective o = scoreinGame.registerNewObjective("SkyWars", "kill");
	public int Kills;
	static int restant = 0;
	
	public static void clearScoreBoard(){

		
	}
	public static void sendScoreBoard(){
//		o.getScoreboard().resetScores(arg0);
		if(Main.playerList.size() != restant){
			o.unregister();
			o = scoreinGame.registerNewObjective("SkyWars", "kill");
			restant = Main.playerList.size();
		}
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		o.getScore("§4Joueur restant§r: §6"+Main.playerList.size()).setScore(10);
		o.getScore("").setScore(9);
		o.getScore("§4Kills§r: ").setScore(8);
		o.getScore("").setScore(-1);
		o.getScore("§4Map§r:").setScore(-2);
		o.getScore("§aNetherland").setScore(-3);
		o.getScore("").setScore(-4);
		//getScore(pl.getName())
//		o.getScore("Pipinou").setScore(o.getScore("Pipinou").getScore()+1);
		
		for(Player OnlinePlayers : Bukkit.getOnlinePlayers()){
			OnlinePlayers.setScoreboard(scoreinGame);
		}
	}
	
	

}
