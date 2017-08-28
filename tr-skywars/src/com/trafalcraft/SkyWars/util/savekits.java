package com.trafalcraft.SkyWars.util;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.trafalcraft.SkyWars.Main;

public class savekits {
	
    private static Material helmet = null;
    private static int nhelmet = 0;
//    private static Map<Enchantment, Integer> ehelmet = null;
    private static Material chestplate = null;
    private static int nchestplate = 0;
//    private static Map<Enchantment, Integer> echestplate = null;
    private static Material leggings = null;
    private static int nleggings = 0;
//    private static Map<Enchantment, Integer> eleggings = null;
    private static Material boots = null;
    private static int nboots = 0;
//    private static Map<Enchantment, Integer> eboots = null;
    private static int nullList = 0;
    
    private final static void clear(){
        helmet = null;
        chestplate = null;
        leggings = null;
        boots = null;
    }
	
    public final static String inventoryToString(final Player player){
        clear();
        if(player.getInventory().getHelmet() != null){
            helmet = player.getInventory().getHelmet().getType();
            nhelmet = player.getInventory().getHelmet().getAmount();
//            ehelmet = player.getInventory().getHelmet().getEnchantments();
        }
        
        if(player.getInventory().getChestplate() != null){
            chestplate = player.getInventory().getChestplate().getType();
            nchestplate = player.getInventory().getChestplate().getAmount();
//            echestplate = player.getInventory().getChestplate().getEnchantments();
        }

        if(player.getInventory().getLeggings() != null){
            leggings = player.getInventory().getLeggings().getType();
            nleggings = player.getInventory().getLeggings().getAmount();
 //           eleggings = player.getInventory().getLeggings().getEnchantments();
        }

        if(player.getInventory().getBoots() != null){
            boots = player.getInventory().getBoots().getType();
            nboots = player.getInventory().getBoots().getAmount();
 //           eboots = player.getInventory().getBoots().getEnchantments();
        }

        String inventory = helmet+"*"+nhelmet+";"+chestplate+"*"+nchestplate+";"+leggings+"*"+nleggings+";"+boots+"*"+nboots;
        nullList = 0;
        for(int i = 0;i<player.getInventory().getContents().length;i++){
            final ItemStack itemStack = player.getInventory().getContents()[i];
            if(itemStack!=null){
            	if(nullList>0){
            		inventory = inventory+";null*"+nullList;
            		nullList = 0;
            	}
            	Material m = itemStack.getType();
                int nItemStack = player.getInventory().getContents()[i].getAmount();
//                Map<Enchantment, Integer> eItemStack = player.getInventory().getContents()[i].getEnchantments();
                if(m.getData() != null){
                	@SuppressWarnings("deprecation")
					byte Data = itemStack.getData().getData();//getData().toString();	//getItemMeta();
                    inventory = inventory+";"+m+"*"+nItemStack+":"+Data;
                }else{
                    inventory = inventory+";"+m+"*"+nItemStack;
                }
	        }else{
	        	nullList = nullList+1;
	        }
        }
        return inventory;
    }
    
	public final static void StringToInvetory(final Player player, String sclasses){
//		player.sendMessage("test4savekit");
        clear();
        player.getInventory().clear();
        String classes = Main.plugin.getConfig().getString("arene.kits."+sclasses+".items");
    	int f1 = 0;
        for(int i = 0;i<player.getInventory().getContents().length;i++){
        	ItemStack itemStack = null;
        	f1 = f1 + 1;
    		try{
    			classes.split(";")[i].contains("null");
			}catch(Exception e){
				return;
			}
        	if(classes.split(";")[i].contains("null")){
        	}else{

            Material m = Material.matchMaterial(classes.split(";")[i].split("\\*")[0]);
            itemStack = new ItemStack(m, 1);
        		}
        	
        	if(itemStack != null){
            	if(classes.split(";")[i].split("\\*")[1].contains(":")){
            		itemStack.setAmount(Integer.parseInt(classes.split(";")[i].split("\\*")[1].split(":")[0]));
            		itemStack.setDurability((byte) Integer.parseInt(classes.split(";")[i].split("\\*")[1].split(":")[1]));
            	}else{
            	itemStack.setAmount(Integer.parseInt(classes.split(";")[i].split("\\*")[1]));
            	}
        	}

            if(i == 0){
            	player.getInventory().setHelmet(itemStack);
            }
            if(i == 1){
            	player.getInventory().setChestplate(itemStack);
            }
            if(i == 2){
            	player.getInventory().setLeggings(itemStack);
            }
            if(i == 3){
            	player.getInventory().setBoots(itemStack);
            }
            if(i!=0 && i!=1 && i!=2 && i!=3 && itemStack != null){
            	player.getInventory().setItem((f1-5), itemStack);
            	//BlockState.getS
            	player.updateInventory();
            }
            if(i!=0 && i!=1 && i!=2 && i!=3 && itemStack == null){
                String item = classes.split(";")[i].replace("null*", "");
                int item2 =Integer.parseInt(item)-1;
//                int f2 = f1;
                for(int f = 0;f<item2;f++){
//                	itemStack = new ItemStack(Material.WOOD);
//               	player.getInventory().setItem(((f2+f)-5), itemStack);
                	f1 = f1 + 1;
                }
            }
        }
        return;
    }

}
