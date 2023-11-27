package com.drav.dravparkour;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomGUI {
	
	List<Material> blocksList = Arrays.asList(Material.WHITE_CONCRETE, Material.WHITE_WOOL, Material.WHITE_GLAZED_TERRACOTTA, Material.WHITE_STAINED_GLASS, Material.QUARTZ_BLOCK, Material.COBBLESTONE);
	
	List<Material> woolList = Arrays.asList(Material.WHITE_WOOL, Material.LIGHT_GRAY_WOOL, Material.GRAY_WOOL, Material.BLACK_WOOL, 
			Material.BROWN_WOOL, Material.RED_WOOL, Material.ORANGE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL, Material.GREEN_WOOL, 
			Material.CYAN_WOOL, Material.LIGHT_BLUE_WOOL, Material.BLUE_WOOL, Material.PURPLE_WOOL, Material.MAGENTA_WOOL, Material.PINK_WOOL);
	
	List<Material> concreteList = Arrays.asList(Material.WHITE_CONCRETE, Material.LIGHT_GRAY_CONCRETE, Material.GRAY_CONCRETE, Material.BLACK_CONCRETE, 
			Material.BROWN_CONCRETE, Material.RED_CONCRETE, Material.ORANGE_CONCRETE, Material.YELLOW_CONCRETE, Material.LIME_CONCRETE, Material.GREEN_CONCRETE, 
			Material.CYAN_CONCRETE, Material.LIGHT_BLUE_CONCRETE, Material.BLUE_CONCRETE, Material.PURPLE_CONCRETE, Material.MAGENTA_CONCRETE, Material.PINK_CONCRETE);
	
	List<Material> terracottaList = Arrays.asList(Material.WHITE_GLAZED_TERRACOTTA, Material.LIGHT_GRAY_GLAZED_TERRACOTTA, Material.GRAY_GLAZED_TERRACOTTA, Material.BLACK_GLAZED_TERRACOTTA, 
			Material.BROWN_GLAZED_TERRACOTTA, Material.RED_GLAZED_TERRACOTTA, Material.ORANGE_GLAZED_TERRACOTTA, Material.YELLOW_GLAZED_TERRACOTTA, Material.LIME_GLAZED_TERRACOTTA, Material.GREEN_GLAZED_TERRACOTTA, 
			Material.CYAN_GLAZED_TERRACOTTA, Material.LIGHT_BLUE_GLAZED_TERRACOTTA, Material.BLUE_GLAZED_TERRACOTTA, Material.PURPLE_GLAZED_TERRACOTTA, Material.MAGENTA_GLAZED_TERRACOTTA, Material.PINK_GLAZED_TERRACOTTA);
	
	List<Material> glassList = Arrays.asList(Material.WHITE_STAINED_GLASS, Material.LIGHT_GRAY_STAINED_GLASS, Material.GRAY_STAINED_GLASS, Material.BLACK_STAINED_GLASS, 
			Material.BROWN_STAINED_GLASS, Material.RED_STAINED_GLASS, Material.ORANGE_STAINED_GLASS, Material.YELLOW_STAINED_GLASS, Material.LIME_STAINED_GLASS, Material.GREEN_STAINED_GLASS, 
			Material.CYAN_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS, Material.BLUE_STAINED_GLASS, Material.PURPLE_STAINED_GLASS, Material.MAGENTA_STAINED_GLASS, Material.PINK_STAINED_GLASS);
	
	List<Material> stoneList = Arrays.asList(Material.COBBLESTONE, Material.STONE, Material.SMOOTH_STONE, Material.STONE_BRICKS, 
			Material.MOSSY_COBBLESTONE, Material.MOSSY_STONE_BRICKS, Material.ANDESITE, Material.POLISHED_ANDESITE, Material.DIORITE, Material.POLISHED_DIORITE, 
			Material.GRANITE, Material.POLISHED_GRANITE, Material.SANDSTONE, Material.RED_SANDSTONE, Material.BRICKS);
	
	List<Material> quartzList = Arrays.asList(Material.CRIMSON_HYPHAE, Material.CRIMSON_PLANKS, Material.CRIMSON_NYLIUM, Material.NETHER_WART_BLOCK, 
			Material.WARPED_HYPHAE, Material.WARPED_PLANKS, Material.WARPED_NYLIUM, Material.WARPED_WART_BLOCK, Material.NETHER_BRICKS, Material.RED_NETHER_BRICKS, 
			Material.BLACKSTONE, Material.POLISHED_BLACKSTONE_BRICKS, Material.QUARTZ_BLOCK, Material.QUARTZ_BRICKS, Material.BASALT, Material.POLISHED_BASALT);
	
	private int type;
	private PlayerData playerData;
	private Inventory inv;
	private Player player;
	
	public CustomGUI(int type, PlayerData data, Player player) {
		this.type = type;
		this.playerData = data;
		this.player = player;
	}
	
	public void setGUI(Player ent, int type) {
		playerData.resetAfkTimer();
		this.type = type;
		if(this.type == 0) {
			settingsGui();
		}
		else if(this.type == 1) {
			blockGui();
		}
		else if(this.type == 2) {
			particlesGui();
		}
		else if(this.type == 3) {
			soundGui();
		}
		player.openInventory(inv);
	}
	
	public int getCurrInv() {
		return type;
	}
	
	public void refreshGUI(Player ent, int type) {
		playerData.resetAfkTimer();
		if(this.type == 0) {
			inv.clear();
			initializeSettingsInv();
		}
		else if(this.type == 1) {
			inv.clear();
			initializeBlockInv();
		}
		else if(this.type == 2) {
			inv.clear();
			initializeParticlesInv();
		}
		else if(this.type == 3) {
			inv.clear();
			initializeSoundInv();
		}
	}
	
	// main settings gui
    public void settingsGui() {
        inv = Bukkit.createInventory(null, 9, "Parkour Settings");
        initializeSettingsInv();
    }

    public void initializeSettingsInv() {
        inv.setItem(1,createGuiItem(Material.COBBLESTONE, "Block Settings"));
        inv.setItem(4,createGuiItem(Material.NETHER_STAR, "Particle Settings"));
        inv.setItem(7,createGuiItem(Material.OBSIDIAN, "Sound Settings"));
        for(int i = 0; i < inv.getSize(); i++) {
        	if(inv.getItem(i) == null) {
        		inv.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        	}
        }
    }
	
    // particles gui
    public void particlesGui() {
        inv = Bukkit.createInventory(null, 9, "Particle Settings");
        initializeParticlesInv();
    }

    public void initializeParticlesInv() {
        if(playerData.isParticles()) inv.setItem(4, createGuiItem(Material.GREEN_STAINED_GLASS_PANE, "ON/OFF"));
    	else inv.setItem(4, createGuiItem(Material.RED_STAINED_GLASS_PANE, "ON/OFF"));
        inv.setItem(8, createGuiItem(Material.BARRIER, "Return"));
        for(int i = 0; i < inv.getSize(); i++) {
        	if(inv.getItem(i) == null) {
        		inv.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        	}
        }
    	
    }
    
    // sound gui
    public void soundGui() {
        inv = Bukkit.createInventory(null, 54, "Sound Settings");
        initializeSoundInv();
    }

    public void initializeSoundInv() {
        if(playerData.isSound()) inv.setItem(14, createGuiItem(Material.GREEN_STAINED_GLASS_PANE, "SOUND ON"));
    	else inv.setItem(14, createGuiItem(Material.RED_STAINED_GLASS_PANE, "SOUND OFF"));
        
        if(playerData.isPitchChange()) inv.setItem(16, createGuiItem(Material.GREEN_STAINED_GLASS_PANE, "PITCH INCREASE"));
    	else inv.setItem(16, createGuiItem(Material.RED_STAINED_GLASS_PANE, "SAME PITCH"));
        
        inv.setItem(32, createGuiItem(Material.NOTE_BLOCK, "Pitch: " + playerData.getPitchLevel()));
        inv.setItem(33, createGuiItem(Material.ARROW, "Higher Pitch"));
        inv.setItem(34, createGuiItem(Material.ARROW, "Lower Pitch"));
        
        inv.setItem(9,createGuiItem(Material.OAK_WOOD, "Bass"));
        inv.setItem(10,createGuiItem(Material.SAND, "Snare Drum"));
        inv.setItem(11,createGuiItem(Material.GLASS, "Clicks and Sticks"));
        inv.setItem(12,createGuiItem(Material.STONE, "Base Drum"));
        inv.setItem(18,createGuiItem(Material.GOLD_BLOCK, "Bells"));
        inv.setItem(19,createGuiItem(Material.CLAY, "Flute"));
        inv.setItem(20,createGuiItem(Material.PACKED_ICE, "Chimes"));
        inv.setItem(21,createGuiItem(Material.WHITE_WOOL, "Guitar"));
        inv.setItem(27,createGuiItem(Material.BONE_BLOCK, "Xylophone"));
        inv.setItem(28,createGuiItem(Material.IRON_BLOCK, "Iron Xylophone"));
        inv.setItem(29,createGuiItem(Material.SOUL_SAND, "Cow Bell"));
        inv.setItem(30,createGuiItem(Material.PUMPKIN, "Didgeridoo"));
        inv.setItem(36,createGuiItem(Material.EMERALD_BLOCK, "Bit"));
        inv.setItem(37,createGuiItem(Material.HAY_BLOCK, "Banjo"));
        inv.setItem(38,createGuiItem(Material.GLOWSTONE, "Pling"));
        inv.setItem(39,createGuiItem(Material.OAK_PLANKS, "Harp / Piano"));
        
        inv.setItem(53, createGuiItem(Material.BARRIER, "Return"));
        
        for(int i = 0; i < inv.getSize(); i++) {
        	if(inv.getItem(i) == null) {
        		inv.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        	}
        }
    	
    }
    
	// block gui
	public void blockGui() {
		inv = Bukkit.createInventory(null, 54, "Block Settings");
        initializeBlockInv();
	}
	
	public void initializeBlockInv() {
        // options list
		inv.setItem(20,createGuiItem(Material.RED_GLAZED_TERRACOTTA, "Terracotta"));
        inv.setItem(29,createGuiItem(Material.COBBLESTONE, "Stone"));
        inv.setItem(19,createGuiItem(Material.RED_WOOL, "Wool"));
        inv.setItem(28,createGuiItem(Material.QUARTZ_BRICKS, "Nether"));
        inv.setItem(27,createGuiItem(Material.RED_STAINED_GLASS, "Stained Glass"));
        inv.setItem(18,createGuiItem(Material.RED_CONCRETE, "Concrete"));
        
        List<Material> list;
        
        if(playerData.getMaterial().toString().contains("WOOL")) list = woolList;
		else if(playerData.getMaterial().toString().contains("CONCRETE")) list = concreteList;
		else if(playerData.getMaterial().toString().contains("TERRACOTTA")) list = terracottaList;
		else if(playerData.getMaterial().toString().contains("GLASS")) list = glassList;
		else if(playerData.getMaterial().toString().contains("CRIMSON") 
				|| playerData.getMaterial().toString().contains("WARPED") 
				|| playerData.getMaterial().toString().contains("NETHER") 
				|| playerData.getMaterial().toString().contains("BLACKSTONE") 
				|| playerData.getMaterial().toString().contains("QUARTZ") 
				|| playerData.getMaterial().toString().contains("BASALT")) list = quartzList;
		else list = stoneList;
        
        int pos = 14;
        for(Material mat : list) {
        	inv.setItem(pos, new ItemStack(mat));
        	if(pos == 17 || pos == 26 || pos == 35) {
        		pos += 5;
        	}
        	pos++;
        }
        
        if(list.equals(stoneList) && playerData.getHighScore() >= 700) inv.setItem(44, new ItemStack(Material.BEDROCK));
        
        // random with current block
        if(playerData.isRandBlock()) {
    		inv.setItem(45, createGuiItem(Material.GREEN_STAINED_GLASS_PANE, "Current Block Random On"));
    	}
    	else {
    		inv.setItem(45, createGuiItem(Material.RED_STAINED_GLASS_PANE, "Current Block Random Off"));
    	}
        
        // completely random
    	if(playerData.isCompleteRandBlock()) {
    		inv.setItem(46, createGuiItem(Material.GREEN_STAINED_GLASS_PANE, "Completely Random Blocks On"));
    	}
    	else {
    		inv.setItem(46, createGuiItem(Material.RED_STAINED_GLASS_PANE, "Completely Random Blocks Off"));
    	}
    	
    	// rainbow with current block
    	if(playerData.getMaterial().toString().contains("WOOL") || playerData.getMaterial().toString().contains("CONCRETE") || playerData.getMaterial().toString().contains("TERRACOTTA") || playerData.getMaterial().toString().contains("GLASS")) {
    		 if(playerData.isRainbowBlock()) {
    	    		inv.setItem(47, createGuiItem(Material.GREEN_STAINED_GLASS_PANE, "Rainbow Blocks On"));
    	    	}
    	    	else {
    	    		inv.setItem(47, createGuiItem(Material.RED_STAINED_GLASS_PANE, "Rainbow Blocks Off"));
    	    	}
    	}
    	else {
    		if(playerData.isRainbowBlock()) playerData.toggleRainbowBlock();
    	}
       
        
        // controls
        inv.setItem(1,createGuiItem(playerData.getMaterial(), "Current Block"));
        inv.setItem(53, createGuiItem(Material.BARRIER, "Return"));
        
        
        for(int i = 0; i < inv.getSize(); i++) {
        	if(inv.getItem(i) == null) {
        		inv.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        	}
        }
    }
	
    protected ItemStack createGuiItem(final Material material, final String name) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        
        // Set the name of the item
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        // Set the lore of the item
        return item;
    }
	
}
