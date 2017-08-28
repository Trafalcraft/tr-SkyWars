package com.trafalcraft.SkyWars.Listener;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import com.trafalcraft.SkyWars.Main;
import com.trafalcraft.SkyWars.game.Game;
import com.trafalcraft.SkyWars.util.BungeeCoord;
import com.trafalcraft.SkyWars.util.Locations;
import com.trafalcraft.SkyWars.util.Messages;
import com.trafalcraft.SkyWars.util.ScoreBoard;

public class PlayerListener implements Listener{
	static JavaPlugin plugin = Main.plugin;
	
	@EventHandler
	public void onPlayerlogin(PlayerLoginEvent e){
		if(Main.spawnList.size() < 1){
			e.disallow(Result.KICK_FULL, "Le SkyWars est complet");
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		Game.PlayerJoin(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e){
		Player p = e.getPlayer();
		if(Main.gamend == true){
			return;
		}
		if(Main.playerList.contains(p)){
			Main.playerList.remove(p);
		}
		if(p.hasMetadata("swjeux")||p.hasMetadata("swspec")||p.hasMetadata("swlobby")){
			if(p.hasMetadata("swjeux")){
				for(int i = 0;i<p.getInventory().getContents().length;i++){
					final ItemStack itemStack = p.getInventory().getContents()[i];
		            if(itemStack!=null){
		            	p.getWorld().dropItemNaturally(p.getLocation(), itemStack);
		            }
				}
				p.removeMetadata("swjeux", plugin);
				e.setQuitMessage(Messages.Prefix+p.getName()+" est partie, il reste "+(Main.playerList.size()+1)+" joueurs");
				Main.playerList.remove(p);
				Game.endGame();
			}
			if(p.hasMetadata("swspec")){
				p.removeMetadata("swspec", plugin);
			}
			if(p.hasMetadata("swlobby")){
				p.removeMetadata("swlobby", plugin);
				Main.spawnList.add(p.getLocation());
			}
			//teleporter le joueur sur un autre serveur
			Main.playerList.remove(p);
			p.sendMessage(Messages.Prefix+"Vous avez quitté l'arêne");
			if(Main.playerList.size()+1 < 2 && Main.lobbyCooldown == true && Main.gameon == false){
				Main.lobbyCooldown = false;
			}
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		Player p = e.getPlayer();
		
		if(p.getLocation().getY() <= 0){
			if(p.hasMetadata("swjeux")){
				if(Main.gamend == true){
					p.getInventory().clear();
					p.teleport(Locations.StringToLoc(plugin.getConfig().getString("arene.WorldBorder.centre")));
					p.sendMessage(Messages.ERREUR+"La partie est finit pas la peine de vous suicidé");
					return;
				}
				p.removeMetadata("swjeux", plugin);
				p.setMetadata("swspec", new FixedMetadataValue(plugin, p));
				p.getInventory().clear();
				p.setGameMode(GameMode.SPECTATOR);
				p.teleport(Locations.StringToLoc(plugin.getConfig().getString("arene.WorldBorder.centre")));
				p.sendMessage(Messages.ERREUR+"Vous êtes tombé et vous êtes mort :/");
				for(Player allp:Bukkit.getOnlinePlayers()){
					if(allp != p){
						Main.playerList.remove(p);
						allp.sendMessage(Messages.Prefix+p.getName()+" est mort en sautant dans le vide il reste "+(Main.playerList.size()/*+1*/)+ "joueurs");	
					}
				}
				Main.playerList.remove(p);
				Game.endGame();
				return;
			}
			if(p.hasMetadata("swspec")){
				p.teleport(Locations.StringToLoc(plugin.getConfig().getString("arene.WorldBorder.centre")));
				p.sendMessage(Messages.ERREUR+"Les spéctateurs n'ont pas le droit de se suicidé :/");
				return;
			}
		}
			if(p.hasMetadata("swspec")){
				Location l1 = Locations.StringToLoc(plugin.getConfig().getString("arene.zones.spec.pos1"));
				Location l2 = Locations.StringToLoc(plugin.getConfig().getString("arene.zones.spec.pos2"));
				int x1 = (int) l1.getX();
				int x2 = (int) l2.getX();
				int z1 = (int) l1.getZ();
				int z2 = (int) l2.getZ();
				if(p.getLocation().getX() > x1 || p.getLocation().getX() < x2 || p.getLocation().getZ() > z1 || p.getLocation().getZ() < z2){
					p.teleport(Locations.StringToLoc(plugin.getConfig().getString("arene.WorldBorder.centre")));
					p.sendMessage(Messages.ERREUR+"Vous ne pouvez pas allez plus loin!!!");	
			}
		}
	}
	
	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent e){
		Player p = e.getPlayer();
		if(p.hasMetadata("swlobby")){
		    e.setCancelled(true);
		    final Player player = e.getPlayer();
	        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable()
	        {
	          public void run()
	          {
	            player.updateInventory();
	          }
	        }, 1L);
		}
	}
	
	@EventHandler
	public void onPlayerDeath(EntityDamageEvent e){
		if(e.getCause() == DamageCause.FALL){
			if(Main.gamend == true){
				e.setCancelled(true);
				return;
			}
		}
		if(e.getEntity() instanceof Player){
			Player p;
			p = (Player) e.getEntity();
			
			try{
				if(e.getFinalDamage()>=p.getHealth()){
					if(p.hasMetadata("swjeux")){
						for(int i = 0;i<p.getInventory().getContents().length;i++){
							final ItemStack itemStack = p.getInventory().getContents()[i];
				            if(itemStack!=null){
				            	p.getWorld().dropItemNaturally(p.getLocation(), itemStack);
				            }
						}
						p.removeMetadata("swjeux", plugin);
						p.setMetadata("swspec", new FixedMetadataValue(plugin, p));
						p.getInventory().clear();
						p.setGameMode(GameMode.SPECTATOR);
						p.teleport(Locations.StringToLoc(plugin.getConfig().getString("arene.WorldBorder.centre")));
						p.sendMessage(Messages.ERREUR+"Vous êtes mort :/");
						e.setDamage(0);
						Main.playerList.remove(p);
						
						for(Player allp:Bukkit.getOnlinePlayers()){
							if(allp != p){
								allp.sendMessage(Messages.Prefix+p.getName()+" est mort "+e.getCause()+", il reste "+(Main.playerList.size())+ "joueurs");	
							}
						}
						Game.endGame();
						return;
					}
					e.setCancelled(true);
				}
			}catch(Exception ee){
				
			}
			
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerKill(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player){
		Player p;
		p = (Player) e.getEntity();
		Location nl = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() , p.getLocation().getZ());
		Location nl2 = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY()+1 , p.getLocation().getZ());
		if(e.getCause() == DamageCause.ENTITY_ATTACK || e.getCause() == DamageCause.PROJECTILE){
		//	Effects.playHologram(attacker, p.getLocation(), c + Double.toString(event.getDamage()), true, true);
			
			for(Player allp:Bukkit.getOnlinePlayers()){
				allp.playEffect(nl, Effect.STEP_SOUND, 152);
				allp.playEffect(nl2, Effect.STEP_SOUND, 152);
			}
		}
		if(e.getCause() == DamageCause.CONTACT){
			
		}
		try{
			if(e.getFinalDamage()>=p.getHealth()){
				if(p.hasMetadata("swjeux")){
					for(int i = 0;i<p.getInventory().getContents().length;i++){
						final ItemStack itemStack = p.getInventory().getContents()[i];
			            if(itemStack!=null){
			            	p.getWorld().dropItemNaturally(p.getLocation(), itemStack);
			            }
					}
					p.removeMetadata("swjeux", plugin);
					p.setMetadata("swspec", new FixedMetadataValue(plugin, p));
					p.getInventory().clear();
					p.setGameMode(GameMode.SPECTATOR);
					p.teleport(Locations.StringToLoc(plugin.getConfig().getString("arene.WorldBorder.centre")));
					p.sendMessage(Messages.ERREUR+"Vous êtes mort :/");
					
					String Damager = e.getDamager().getName();
					int killPlus = ScoreBoard.o.getScore(Damager).getScore()+1;
					int killsPlus = ScoreBoard.o.getScore("§4Kills§r: ").getScore()+1;
					if(killPlus == 0){
						killPlus = 1;
					}
					ScoreBoard.o.getScore(Damager).setScore(killPlus);
					ScoreBoard.o.getScore("§4Kills§r: ").setScore(killsPlus);
					e.setDamage(0);
					Main.playerList.remove(p);
					
					for(Player allp:Bukkit.getOnlinePlayers()){
						if(allp != p){
							allp.sendMessage(Messages.Prefix+p.getName()+" est mort, tué par "+	e.getDamager().getName()+", il reste "+(Main.playerList.size())+ "joueurs");	
						}
					}
					Game.endGame();
					return;
				}
				e.setCancelled(true);
			}
		}catch(Exception ee){
			
		}
	}else{
		return;
	}
	}
	
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e){
		String Message = e.getMessage();
		e.setCancelled(true);
		Player p = e.getPlayer();
		if(p.hasMetadata("swjeux")||p.hasMetadata("swlobby")){
			Message = (Messages.Prefix + "§6§l"+p.getDisplayName() + " >> §r" + Message);
			for(Player ps : Bukkit.getServer().getOnlinePlayers()){
		        ps.sendMessage(Message);
			}
		}
		if(p.hasMetadata("swspec")){
			Message = (Messages.Prefix + "§7Mort > "+p.getDisplayName() + ">>" + Message);
			for(Player ps : Bukkit.getServer().getOnlinePlayers()){
				if(ps.hasMetadata("swspec")){
			        ps.sendMessage(Message);
				}

			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(e.getPlayer().hasMetadata("swlobby")){
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK){
				if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.REDSTONE_BLOCK)){
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
									allp.sendMessage(Messages.Prefix+p.getName()+" a quitté ("+(Main.playerList.size()-1)+"/"+plugin.getConfig().getString("arene.nombre-spawns")+")");	
								}
							}
							Main.spawnList.add(p.getLocation());
						}
						//teleporter le joueur sur un autre serveur
						if(Main.playerList.contains(p)){
							Main.playerList.remove(p);
						}
						p.sendMessage(Messages.Prefix+"Vous avez quitté l'arêne");
						if(Main.playerList.size()+1 < 2 && Main.lobbyCooldown == true && Main.gameon == false){
							Main.lobbyCooldown = false;
						}
						BungeeCoord.sendPlayerToHub(p);
			}
				}
				if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.CHEST)){
					if(p.hasMetadata("swlobby")){
						Inventory inventory = Bukkit.createInventory(p, 18, "§bClasses");
						for(String aclass : plugin.getConfig().getConfigurationSection("arene.kits.").getKeys(false)){
							if(plugin.getConfig().getBoolean("arene.kits."+aclass+".enable") == true){
								ItemStack icon = new ItemStack(Material.getMaterial(plugin.getConfig().getString("arene.kits."+aclass+".icon")), 1);
								ItemMeta meta = icon.getItemMeta();
								meta.setDisplayName(plugin.getConfig().getString("arene.kits."+aclass+".name"));
								meta.setLore(plugin.getConfig().getStringList("arene.kits."+aclass+".lore"));
								icon.setItemMeta(meta);
								inventory.addItem(icon);
							}
						}
						p.openInventory(inventory);
					}
				}
			}	
		}
	}
	
	@EventHandler
	public void onPlayerHungerChange(FoodLevelChangeEvent event){
		if (event.getEntity().hasMetadata("swlobby")) {
	      event.setCancelled(true);
	    }
	  }

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e){
		if(e.getWhoClicked().hasMetadata("swspec")){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		if(e.getWhoClicked().hasMetadata("swlobby")){
			if(e.getInventory().getName().equalsIgnoreCase("§bClasses")){
				if(e.getCurrentItem().getType() != Material.AIR){
					ItemMeta meta = e.getCurrentItem().getItemMeta();
					ItemStack itemstack;
					itemstack = e.getWhoClicked().getInventory().getItem(0);
					ItemMeta meta1 = itemstack.getItemMeta();
					meta1.setDisplayName("§aClasses§r->"+meta.getDisplayName());
					itemstack.setItemMeta(meta1);
					e.getWhoClicked().getInventory().setItem(0, itemstack);
					e.getWhoClicked().sendMessage(Messages.Prefix+"Vous avez séléctionné la classe §r" + meta.getDisplayName());
					//String classe = plugin.getConfig().getString("arene.kits."+meta.getDisplayName()+".items");
					//savekits.StringToInvetory((Player) e.getWhoClicked(), meta.getDisplayName());
				}
			}
			e.setCancelled(true);
		}
		if(e.getWhoClicked().hasMetadata("swspec")){
			e.setCancelled(true);
		}
	}

/*	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e){
		
	}*/
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		if(e.getPlayer().hasMetadata("swlobby")){
			e.getPlayer().sendMessage(Messages.ERREUR+"Vous ne pourrez detruire les blocks que quand la partie aura débuté");
			e.setCancelled(true);
		}
	}
	
}
