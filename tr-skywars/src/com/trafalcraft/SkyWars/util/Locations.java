package com.trafalcraft.SkyWars.util;
	
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Locations {
	  public static String LocationToString(Location l)
	  {
	    return String.valueOf(new StringBuilder(String.valueOf(l.getWorld().getName())).append(":").append(l.getBlockX()).toString()) + ":" + String.valueOf(l.getBlockY()) + ":" + String.valueOf(l.getBlockZ());
	  }
	  
	  public static Location StringToLoc(String s)
	  {
	    Location l = null;
	    try
	    {
	      World world = Bukkit.getWorld(s.split(":")[0]);
	      Double x = Double.valueOf(Double.parseDouble(s.split(":")[1]));
	      Double y = Double.valueOf(Double.parseDouble(s.split(":")[2]));
	      Double z = Double.valueOf(Double.parseDouble(s.split(":")[3]));
	      
	      l = new Location(world, x.doubleValue()+0.5, y.doubleValue(), z.doubleValue()+0.5);
	    }
	    catch (Exception ex)
	    {
	      ex.printStackTrace();
	    }
	    return l;
	  }
}
