package com.trafalcraft.SkyWars.util;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Title {
	public static void sendTitle(Player p, String title, String subTitle, int fadeIn, int duration, int fadeOut)
	{
		CraftPlayer craftplayer = (CraftPlayer) p;
		PlayerConnection connection = craftplayer.getHandle().playerConnection;
		IChatBaseComponent titleJSON = IChatBaseComponent.ChatSerializer.a("{'text': '" + title + "'}");
		IChatBaseComponent subtitleJSON = IChatBaseComponent.ChatSerializer.a("{'text': '" + subTitle + "'}");
		PacketPlayOutTitle timesPacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, titleJSON, fadeIn, duration, fadeOut);
		PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleJSON);
		PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleJSON);
		connection.sendPacket(timesPacket);
		connection.sendPacket(titlePacket);
		connection.sendPacket(subtitlePacket);
	}
}
