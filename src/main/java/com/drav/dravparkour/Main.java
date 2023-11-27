package com.drav.dravparkour;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements Listener {
	
	// config settings
	private int numBlocks = 3;
	private Location corner1;
	private Location corner2;
	private Location spawnBlock;
	private Location startBlock;
	private Location sbBlock;
	private ArmorStand sootMC;
	private ArmorStand theLeaderboard;
	private ArmorStand info;
	private ArrayList<PlayerData> leaderboard = new ArrayList<PlayerData>();
	private ArrayList<ArmorStand> armorstands;
	
	private Main plugin;
	
	// list of players data
	private Hashtable<UUID, PlayerData> playerList = new Hashtable<UUID, PlayerData>();
	private Hashtable<UUID, CustomGUI> GUIList = new Hashtable<UUID, CustomGUI>();
	
	// block lists
	List<Material> blocksList = Arrays.asList(Material.WHITE_WOOL, Material.WHITE_CONCRETE, Material.WHITE_GLAZED_TERRACOTTA, Material.OAK_PLANKS, Material.STONE, Material.QUARTZ_BLOCK);
	
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
	
	List<Material> netherList = Arrays.asList(Material.CRIMSON_HYPHAE, Material.CRIMSON_PLANKS, Material.CRIMSON_NYLIUM, Material.NETHER_WART_BLOCK, 
			Material.WARPED_HYPHAE, Material.WARPED_PLANKS, Material.WARPED_NYLIUM, Material.WARPED_WART_BLOCK, Material.NETHER_BRICKS, Material.RED_NETHER_BRICKS, 
			Material.BLACKSTONE, Material.POLISHED_BLACKSTONE_BRICKS, Material.QUARTZ_BLOCK, Material.QUARTZ_BRICKS, Material.BASALT, Material.POLISHED_BASALT);
	
	// config files
    private File customConfigFile;
    private FileConfiguration customConfig;
    private FileConfiguration config;
    
	// On Enable
	@Override
	public void onEnable() {
		// config stuff
		createCustomConfig();
		config = getConfig();
		
		plugin = this;
		
		// check if corners and spawn were previously setup
		if(config.getString("corner1") != null && config.getString("corner2") != null && config.getString("spawn") != null && config.getString("start") != null) {
			corner1 = config.getLocation("corner1.location");
			corner2 = config.getLocation("corner2.location");
			spawnBlock = config.getLocation("spawn.location");
			startBlock = config.getLocation("start.location");
			if(config.getString("scoreboard") != null)		sbBlock = config.getLocation("scoreboard.location");
		}
		// command enable
		this.getCommand("PkCorner1").setExecutor(new CommandPos1());
		this.getCommand("PkCorner2").setExecutor(new CommandPos2());
		this.getCommand("PkSpawn").setExecutor(new CommandPkSpawn());
		this.getCommand("PkStart").setExecutor(new CommandPkStart());
		this.getCommand("PkScoreboard").setExecutor(new CommandPkLb());
		this.getCommand("PkUpdateScore").setExecutor(new CommandUpdateScore());
	
		// loading player data into playerList
		if(this.getCustomConfig().getConfigurationSection("players") != null) {
			for(String s: this.getCustomConfig().getConfigurationSection("players").getKeys(false)) {
				playerList.put(UUID.fromString(s)
						, new PlayerData(this.getCustomConfig().getInt("players." + s + ".totalJumps")
										, this.getCustomConfig().getInt("players." + s + ".score")
										, this.getCustomConfig().getString("players." + s + ".name")
										, Material.getMaterial(this.getCustomConfig().getString("players." + s + ".block"))
										, numBlocks
										, this.getCustomConfig().getBoolean("players." + s + ".randBlock")
										, this.getCustomConfig().getBoolean("players." + s + ".completeRandBlock")
										, this.getCustomConfig().getBoolean("players." + s + ".rainbowBlock")
										, this.getCustomConfig().getBoolean("players." + s + ".particles")
										, this.getCustomConfig().getBoolean("players." + s + ".sound")
										, Sound.valueOf(this.getCustomConfig().getString("players." + s + ".currSound"))
										, (float)this.getCustomConfig().getDouble("players." + s + ".pitch")
										, this.getCustomConfig().getBoolean("players." + s + ".pitchChange")
						));
				updateLeaderboard(UUID.fromString(s));
			}
		}
		
		// if the scoreboard has a location, go through all armorstands in the area and remove them
		// useful if server crashed previously and armorstands stayed behind from an old scoreboard
		if(sbBlock != null) {
			for(Entity e : sbBlock.getChunk().getEntities()){
				if(e instanceof ArmorStand && e.getLocation().getBlockX() == sbBlock.getBlockX() && sbBlock.getBlockZ() == e.getLocation().getBlockZ()){ 
					e.remove();
				}
			}
			setLeaderboard();
		}
		
		// create a sync schedule that syncs all local data to the file, runs every 30 minutes
		Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
            public void run() {
            	for(Entry<UUID, PlayerData> player : playerList.entrySet()) {
        			plugin.getCustomConfig().set("players." + player.getKey() + ".name",player.getValue().getPlayerName());
        			plugin.getCustomConfig().set("players." + player.getKey() + ".score",player.getValue().getHighScore());
        			plugin.getCustomConfig().set("players." + player.getKey() + ".block",player.getValue().getMaterial().toString());
        			plugin.getCustomConfig().set("players." + player.getKey() + ".randBlock",player.getValue().isRandBlock());
        			plugin.getCustomConfig().set("players." + player.getKey() + ".completeRandBlock",player.getValue().isCompleteRandBlock());
        			plugin.getCustomConfig().set("players." + player.getKey() + ".rainbowBlock",player.getValue().isRainbowBlock());
        			plugin.getCustomConfig().set("players." + player.getKey() + ".particles",player.getValue().isParticles());
        			plugin.getCustomConfig().set("players." + player.getKey() + ".sound",player.getValue().isSound());
        			plugin.getCustomConfig().set("players." + player.getKey() + ".currSound", player.getValue().getSound().toString());
        			plugin.getCustomConfig().set("players." + player.getKey() + ".pitch", player.getValue().getPitch());
        			plugin.getCustomConfig().set("players." + player.getKey() + ".pitchChange", player.getValue().isPitchChange());
        			plugin.getCustomConfig().set("players." + player.getKey() + ".totalJumps", player.getValue().getTotalJumps());
        			try {
						customConfig.save(customConfigFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
        		}
            	getLogger().info("DravParkour has been Sync'd!");
            }
		}, 36000, 36000);
		
		getLogger().info("DravParkour has been enabled!");
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	 
	// commands
    
    // command to set the first parkour corner
    public class CommandPos1 implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			// check if the command sender is a player and has permission
        	if (sender instanceof Player && sender.hasPermission("dravparkour.pkcontrol")) {
        		// checks if both corners are set, if not, set the new corner into the config, else check which corner is smaller and
				// set as corner 1
				corner1 = ((Player) sender).getPlayer().getLocation();
        		((Player) sender).getPlayer().sendRawMessage("\u00A7aCorner 1 Set.");
                if(corner1 != null && corner2 != null) {
                	((Player) sender).getPlayer().sendRawMessage("\u00A7aBoth Corners Set Successfully!");
                	if(corner1.getBlockX() < corner2.getBlockX()) {
                		int temp = corner1.getBlockX();
                		corner1.setX(corner2.getBlockX());
                		corner2.setX(temp);
                	}
                	if(corner1.getBlockY() < corner2.getBlockY()) {
                		int temp = corner1.getBlockY();
                		corner1.setY(corner2.getBlockY());
                		corner2.setY(temp);
                	}
                	if(corner1.getBlockZ() < corner2.getBlockZ()) {
                		int temp = corner1.getBlockZ();
                		corner1.setZ(corner2.getBlockZ());
                		corner2.setZ(temp);
                	}
                    config.set("corner2.location", corner2);
                }
                config.set("corner1.location", corner1);
        		saveConfig();
            }
            return true;
        }
    }
    
    // command to set the second parkour corner
    public class CommandPos2 implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			// check if the command sender is a player and has permission
        	if (sender instanceof Player && sender.hasPermission("dravparkour.pkcontrol")) {
        		// checks if both corners are set, if not, set the new corner into the config, else check which corner is smaller and
				// set as corner 1
				corner2 = ((Player) sender).getPlayer().getLocation();
        		((Player) sender).getPlayer().sendRawMessage("\u00A7aCorner 2 Set.");	
                if(corner1 != null && corner2 != null) {
                	((Player) sender).getPlayer().sendRawMessage("\u00A7aBoth Corners Set Successfully!");	
                	if(corner1.getBlockX() < corner2.getBlockX()) {
                		int temp = corner1.getBlockX();
                		corner1.setX(corner2.getBlockX());
                		corner2.setX(temp);
                	}
                	if(corner1.getBlockY() < corner2.getBlockY()) {
                		int temp = corner1.getBlockY();
                		corner1.setY(corner2.getBlockY());
                		corner2.setY(temp);
                	}
                	if(corner1.getBlockZ() < corner2.getBlockZ()) {
                		int temp = corner1.getBlockZ();
                		corner1.setZ(corner2.getBlockZ());
                		corner2.setZ(temp);
                	}   
                	config.set("corner1.location", corner1);
                }
                config.set("corner2.location", corner2);
        		saveConfig();
            }
            return true;
        }
    }
    
    // command to set the parkour spawn location
    public class CommandPkSpawn implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			// check if the command sender is a player and has permission, if they have permission, set the spawn block in config
        	if (sender instanceof Player && sender.hasPermission("dravparkour.pkcontrol")) {
        		spawnBlock = ((Player) sender).getPlayer().getLocation();
        		config.set("spawn.location", spawnBlock);
        		saveConfig();
        		((Player) sender).getPlayer().sendRawMessage("\u00A7aSpawn Block Set.");	
                }
        	return true;
        }
    }
    
    // command to set the parkour start location
    public class CommandPkStart implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			// check if the command sender is a player and has permission, if they have permission, set the start block in config
        	if (sender instanceof Player && sender.hasPermission("dravparkour.pkcontrol")) {
        		startBlock = ((Player) sender).getPlayer().getLocation();
        		config.set("start.location", startBlock);
        		saveConfig();
        		((Player) sender).getPlayer().sendRawMessage("\u00A7aStart Block Set.");	
                }
        	return true;
        }
    }
	
    // command to set leaderboard location
    public class CommandPkLb implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			// check if the command sender is a player and has permission, if they have permission, set the leaderboard block in config
        	if (sender instanceof Player && sender.hasPermission("dravparkour.pkcontrol")) {
        		if(armorstands != null)		deleteLeaderboard();
        		sbBlock = ((Player) sender).getPlayer().getLocation();
        		config.set("scoreboard.location", sbBlock);
        		saveConfig();
        		((Player) sender).getPlayer().sendRawMessage("\u00A7aScoreboard Location Set.");	
        		setLeaderboard();
                }
        	return true;
        }
    }
    
	// command to update the score for a given player
    public class CommandUpdateScore implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			// check if the command sender is a player and has permission, if they have permission, grab the uuid of the player
			// and update the leaderboard with the new information
        	if (sender.hasPermission("dravparkour.pkcontrol")) {
        		UUID uuid = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
        		if(uuid != null && playerList.get(uuid) != null){
            		playerList.get(uuid).setHighScore(Integer.parseInt(args[1]));
            		updateLeaderboard(((Player) sender).getUniqueId());
            		((Player) sender).getPlayer().sendRawMessage("\u00A7aPlayer " + args[0] + " has score updated to: " + args[1]);
        		}
        		else {
            		((Player) sender).getPlayer().sendRawMessage("\u00A74Player: " + args[0] + " could not be found!");
        		}
            }
        	return true;
        }
    }
	
	// deletes the in-game display leaderboard 
    public void deleteLeaderboard() {
    	if(sootMC != null) {
        	sootMC.remove();
        	theLeaderboard.remove();
        	info.remove();
        	for(ArmorStand armorstand: armorstands) {
        		armorstand.remove();
        	}	
    	}
    }
    
    public void setLeaderboard() {
    	armorstands = new ArrayList<ArmorStand>();
    	sootMC = createArmorStand(sbBlock.add(0,1,0), "\u00A76Mehow\u00A7cMC");
    	theLeaderboard = createArmorStand(sbBlock.add(0,-.25,0), "\u00A7aParkour Leaderboard");
    	info = createArmorStand(sbBlock.add(0,-.25,0), "\u00A73High Score \u00A7b / \u00A73Total Jumps");
    	for(PlayerData player : leaderboard)	
    		armorstands.add(createArmorStand(sbBlock.add(0,-.25,0), "\u00A7b" + (leaderboard.indexOf(player)+1) + ") \u00A7d" + player.getPlayerName() + ": \u00A73" + player.getHighScore() + "\u00A7b / \u00A73" + player.getTotalJumps()));
    }
    
    public ArmorStand createArmorStand(Location loc, String name) {
    	ArmorStand as = (ArmorStand) sbBlock.getWorld().spawn(loc, ArmorStand.class);
    	as.setCustomName(name);
    	as.setGravity(false);
    	as.setVisible(false);
    	as.setCustomNameVisible(true);
    	return as;
    }
    
    public void updateLeaderboardStands() {
    	if(sbBlock == null) {
    		return;
    	}
    	for(int i = 0; i < leaderboard.size(); i++) {
			if(armorstands.size() == i) {
				armorstands.add(createArmorStand(sbBlock.add(0,-.25,0), "\u00A7b" + (i+1) + ") \u00A7d" + leaderboard.get(i).getPlayerName() + ": \u00A73" + leaderboard.get(i).getHighScore() + "\u00A7b / \u00A73" + leaderboard.get(i).getTotalJumps()));
				break;
			}
			else if(leaderboard.get(i) != null)		armorstands.get(i).setCustomName("\u00A7b" + (i+1) + ") \u00A7d" + leaderboard.get(i).getPlayerName() + ": \u00A73" + leaderboard.get(i).getHighScore() + "\u00A7b / \u00A73" + leaderboard.get(i).getTotalJumps());
			else	break;
    	} 
    }
    
    public void updateLeaderboard(UUID player) {
    	if(sbBlock == null) {
    		return;
    	}
		for(int i = 0; i < leaderboard.size(); i++) {
			if(leaderboard.get(i).getPlayerName().equals(playerList.get(player).getPlayerName()))	leaderboard.remove(i);
    	}

    	if(leaderboard.size() >= 10) {
			if(playerList.get(player).getHighScore() > leaderboard.get(leaderboard.size()-1).getHighScore()) {
				for(int i = 0; i < 10; i++) {
					if(playerList.get(player).getHighScore() > leaderboard.get(i).getHighScore()) {
						leaderboard.add(i, playerList.get(player));
						leaderboard.remove(leaderboard.size()-1);
						if(armorstands != null)		updateLeaderboardStands();
						break;
					}
				}
			}
		}
		else {
			for(int i = 0; i < 10; i++) {
				if(leaderboard.size() == i) {
					leaderboard.add(playerList.get(player));
					if(armorstands != null)		updateLeaderboardStands();
					break;
				}
				else if(playerList.get(player).getHighScore() > leaderboard.get(i).getHighScore()) {
					leaderboard.add(i, playerList.get(player));
					if(armorstands != null)		updateLeaderboardStands();
					break;
				}
			}
		}
	}
    
	// event handler for player movement
	@EventHandler
	public void parkourStart(PlayerMoveEvent e)	{
		// first make sure area and spawn areas are set up
		if(startBlock == null || corner1 == null || corner2 == null || spawnBlock == null) return;
		
		// grabs data for the current player
		PlayerData currPlayer = playerList.get(e.getPlayer().getUniqueId());
		
		Block block = e.getTo().getBlock();
		
		if(block.getX() == startBlock.getBlockX() && block.getY() == startBlock.getBlockY() && block.getZ() == startBlock.getBlockZ())	{
			// initializes new player
			if(currPlayer == null) {
				playerList.put(e.getPlayer().getUniqueId(), new PlayerData(e.getPlayer().getName(), numBlocks));
				currPlayer = playerList.get(e.getPlayer().getUniqueId());
				e.getPlayer().sendRawMessage("\u00A7aParkour Started! \u00A77 Welcome to the Parkour!");
				//e.getPlayer().sendRawMessage("\u00A77Make sure your inventory isn't full! Lost rewards are on you.");
			}
			else	e.getPlayer().sendRawMessage("\u00A7aParkour Started! \u00A77Try and beat your current highscore of \u00A7f" + currPlayer.getHighScore() + "\u00A77.");	
			if(!e.getPlayer().getName().equals(currPlayer.getPlayerName())){
				currPlayer.setName(e.getPlayer().getName());
			}
			e.getPlayer().removePotionEffect(PotionEffectType.JUMP);
			e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
			e.getPlayer().setFoodLevel(25);
			
			currPlayer.setAfkTask(new BukkitRunnable() {
				public void run() {
					PlayerData currPlayer = playerList.get(e.getPlayer().getUniqueId());
            		currPlayer.incrementAfkTimer();
            		if(currPlayer.getAfkTimer() == 3)	e.getPlayer().sendRawMessage("\u00A74Move or you will be kicked off the parkour for afk!");
            		if(currPlayer.getAfkTimer() == 4) {
            			if(currPlayer.getHighScore() < currPlayer.getCurrScore()) {
        					e.getPlayer().sendRawMessage("\u00A74You were AFK! \u00A77You beat your previous highscore of \u00A7f" + currPlayer.getHighScore() + "\u00A77!");	
        					currPlayer.setHighScore(currPlayer.getCurrScore());
        				}
        				else	e.getPlayer().sendRawMessage("\u00A74You were AFK! \u00A77You didn't beat your previous highscore of \u00A7f" + currPlayer.getHighScore() + "\u00A77.");
        				
            			e.getPlayer().getInventory().setContents(currPlayer.getInventory());
            			//reward(currPlayer,e.getPlayer());
            			Bukkit.getServer().getScheduler().cancelTask(currPlayer.getAfkTask());
            			currPlayer.setAfkTask(-1);
            			
            			new BukkitRunnable() {
            				public void run() {
            					currPlayer.fall();
            				}
            			}.runTask(plugin);
        				updateLeaderboardStands();
        				new BukkitRunnable() {
        					public void run() {
        						e.getPlayer().teleport(spawnBlock);
        					}
        				}.runTask(plugin);
            		}
				}
			}.runTaskTimerAsynchronously(this, 0, 1200).getTaskId());
			
			// generate random location for first block
			int randX = (int)(Math.random()*(corner1.getBlockX()-corner2.getBlockX()+1)) + corner2.getBlockX();
			int randY = (int)(Math.random()*(corner1.getBlockY()-corner2.getBlockY()+1)) + corner2.getBlockY();
			int randZ = (int)(Math.random()*(corner1.getBlockZ()-corner2.getBlockZ()+1)) + corner2.getBlockZ();
			
			// set first block
			Location playerloc = new Location(e.getPlayer().getWorld(), randX,randY,randZ);
			currPlayer.setCurrentBlock(playerloc.getBlock());
			
			// store and clear inventory
			currPlayer.setInventory(e.getPlayer().getInventory().getContents());
			e.getPlayer().getInventory().clear();
			
			// add Settings in inventory
			final ItemStack item = new ItemStack(Material.CHEST, 1);
	        final ItemMeta meta = item.getItemMeta();
	        meta.setDisplayName("Settings");
	        item.setItemMeta(meta);
			e.getPlayer().getInventory().setItem(7, item);
			
			// teleport player to block
			e.getPlayer().teleport(playerloc.add(.5,1.5,.5));
			e.setTo(playerloc);
			
			// set player as parkouring
			currPlayer.setParkouring(true);
		}
		
		// run while parkouring
		if(playerList.get(e.getPlayer().getUniqueId()) == null)	return;
		if(currPlayer.isParkouring())	{
			// adds next blocks
			int attempts = 0;
			while(currPlayer.nextBlockQueueIncomplete())	{
				int dir = (int)(Math.random()*4);
				int depth = (int)(Math.random()*2)+3;
				int yAmp = (int)(Math.random()*3)-1;
				int xAmp = 0;
				int zAmp = 0;
				if((currPlayer.getPrevDirection() == 0 && dir == 1)
						|| (currPlayer.getPrevDirection() == 1 && dir == 0)
						|| (currPlayer.getPrevDirection() == 2 && dir == 3)
						|| (currPlayer.getPrevDirection() == 3 && dir == 2))	continue;
				
				if(dir == 0) xAmp += depth;
				else if(dir == 1) xAmp -= depth;
				else if(dir == 2) zAmp += depth;
				else zAmp -= depth;
				
				Location loca = new Location(e.getPlayer().getWorld(), currPlayer.getLastBlock().getX() + xAmp, currPlayer.getLastBlock().getY() + yAmp,currPlayer.getLastBlock().getZ() + zAmp);
				
				if((loca.getBlock().getType() == Material.AIR &&
						loca.getBlockX() <= corner1.getBlockX() && loca.getBlockX() >= corner2.getBlockX()) && 
						(loca.getBlockY() <= corner1.getBlockY() && loca.getBlockY() >= corner2.getBlockY()) &&
						(loca.getBlockZ() <= corner1.getBlockZ() && loca.getBlockZ() >= corner2.getBlockZ())) {
					currPlayer.addNextBlock(loca.getBlock());
					Block nextBlock = currPlayer.getLastBlock();
					if(currPlayer.isCompleteRandBlock()) {
						currPlayer.setMaterial(blocksList.get((int)(Math.random()*6)));
					}
					if(currPlayer.isRandBlock()) {
						Material mat = currPlayer.getMaterial();
		        		// WOOL
		        		if(mat.toString().contains("WOOL")) {
		        			currPlayer.setMaterial(woolList.get((int)(Math.random()*woolList.size())));
		        		}
		        		// CONCRETE
		        		else if(mat.toString().contains("CONCRETE")) {
		        			currPlayer.setMaterial(concreteList.get((int)(Math.random()*concreteList.size())));
		        		}
		        		
		        		// TERRACOTTA
		        		else if(mat.toString().contains("TERRACOTTA")) {
		        			currPlayer.setMaterial(terracottaList.get((int)(Math.random()*terracottaList.size())));
		        		}
		        		
		        		// GLASS
		        		else if(mat.toString().contains("GLASS")) {
		        			currPlayer.setMaterial(glassList.get((int)(Math.random()*glassList.size())));
		        		}
		        		
		        		// NETHER
		        		else if(mat.toString().contains("CRIMSON") || mat.toString().contains("WARPED") || mat.toString().contains("NETHER") || mat.toString().contains("BLACKSTONE") || mat.toString().contains("QUARTZ") || mat.toString().contains("BASALT")) {
		        			currPlayer.setMaterial(netherList.get((int)(Math.random()*netherList.size())));
		        		}
		        		else {
		        			currPlayer.setMaterial(stoneList.get((int)(Math.random()*stoneList.size())));
		        		}
					}
					if(currPlayer.isRainbowBlock()) {
						Material mat = currPlayer.getMaterial();
						if(mat.toString().contains("WOOL")) {
		        			if(woolList.indexOf(mat) == woolList.size()-1) currPlayer.setMaterial(woolList.get(5));
		        			else currPlayer.setMaterial(woolList.get(woolList.indexOf(mat)+1));
		        		}
		        		
		        		// CONCRETE
		        		else if(mat.toString().contains("CONCRETE")) {
		        			if(concreteList.indexOf(mat) == concreteList.size()-1) currPlayer.setMaterial(concreteList.get(5));
		        			else currPlayer.setMaterial(concreteList.get(concreteList.indexOf(mat)+1));
		        		}
		        		
		        		// TERRACOTTA
		        		else if(mat.toString().contains("TERRACOTTA")) {
		        			if(terracottaList.indexOf(mat) == terracottaList.size()-1) currPlayer.setMaterial(terracottaList.get(5));
		        			else currPlayer.setMaterial(terracottaList.get(terracottaList.indexOf(mat)+1));
		        		}
						
						// GLASS
		        		else if(mat.toString().contains("GLASS")) {
		        			if(glassList.indexOf(mat) == glassList.size()-1) currPlayer.setMaterial(glassList.get(5));
		        			else currPlayer.setMaterial(glassList.get(glassList.indexOf(mat)+1));
		        		}
					}
					nextBlock.setType(currPlayer.getMaterial());
					loca.add(.5,.5,.5);
					currPlayer.setPrevDirection(dir);
					if(currPlayer.isParticles())	e.getPlayer().spawnParticle(Particle.FIREWORKS_SPARK, loca, 25);
					if(currPlayer.isPitchChange())	currPlayer.increasePitch();
					if(currPlayer.isSound())	e.getPlayer().playSound(loca, currPlayer.getSound(), 10, currPlayer.getPitch());
				}
				attempts++;
				if(attempts >= 200) {
					Bukkit.getLogger().info("DRAV PARKOUR: WHILE LOOP OVERFLOW");
					e.getPlayer().teleport(spawnBlock);
					e.getPlayer().getInventory().setContents(currPlayer.getInventory());
					
					//reward(currPlayer,e.getPlayer());
								
					if(currPlayer.getHighScore() < currPlayer.getCurrScore()) {
						e.getPlayer().sendRawMessage("\u00A74Parkour Error ): Report to Drav! \u00A77You beat your previous highscore of \u00A7f" + currPlayer.getHighScore() + "\u00A77!");	
						currPlayer.setHighScore(currPlayer.getCurrScore());
						updateLeaderboard(e.getPlayer().getUniqueId());
						
					}
					else	e.getPlayer().sendRawMessage("\u00A74Parkour Error ): Report to Drav! \u00A77You didn't beat your previous highscore of \u00A7f" + currPlayer.getHighScore() + "\u00A77.");
					
					Bukkit.getServer().getScheduler().cancelTask(currPlayer.getAfkTask());
	    			currPlayer.setAfkTask(-1);
					currPlayer.fall();
					updateLeaderboardStands();
					return;
				}
			}
			
			if((int)e.getTo().getBlockX() == currPlayer.getNextBlock().getX() 
					&& (int)e.getTo().getBlockY()-1 == currPlayer.getNextBlock().getY() 
					&& (int)e.getTo().getBlockZ() == currPlayer.getNextBlock().getZ())
			{
				currPlayer.popCurrentBlock();
				e.getPlayer().removePotionEffect(PotionEffectType.JUMP);
				e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
				e.getPlayer().setFoodLevel(25);
				
				//if(currPlayer.getCurrScore() == 40) 	e.getPlayer().sendRawMessage("\u00A7a40 jumps? Pog! Keep it up! Here's some Iron.");
				
				//if(currPlayer.getCurrScore() == 60) 	e.getPlayer().sendRawMessage("\u00A7a60 jumps? Epic! Keep it going! Here's some Gold.");
					
				//if(currPlayer.getCurrScore() == 200) 	e.getPlayer().sendRawMessage("\u00A7a200 jumps? Ooooh you're a parkour god! Here's a Diamond!");
				
				//if(currPlayer.getCurrScore() == 500) 	e.getPlayer().sendRawMessage("\u00A7a500 jumps? Are you not bored of this yet? Here's some Netherite!");
				
				/*
				String message = "\u00A77 Score \u00A7f\u00A7l" + currPlayer.getCurrScore();
				IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
				PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc,ChatMessageType.GAME_INFO, e.getPlayer().getUniqueId());
		        ((CraftPlayer)e.getPlayer()).getHandle().playerConnection.sendPacket((Packet)ppoc);
				 */
			}

			
			if(e.getTo().getY() < currPlayer.getCurrentBlock().getY() - 5)
			{
				e.getPlayer().teleport(spawnBlock);
				e.getPlayer().getInventory().setContents(currPlayer.getInventory());
				
				if(currPlayer.getHighScore() < currPlayer.getCurrScore()) {
					e.getPlayer().sendRawMessage("\u00A74You fell! \u00A77You beat your previous highscore of \u00A7f" + currPlayer.getHighScore() + "\u00A77!");	
					currPlayer.setHighScore(currPlayer.getCurrScore());
					updateLeaderboard(e.getPlayer().getUniqueId());
				}
				else	e.getPlayer().sendRawMessage("\u00A74You fell! \u00A77You didn't beat your previous highscore of \u00A7f" + currPlayer.getHighScore() + "\u00A77.");
				
				Bukkit.getServer().getScheduler().cancelTask(currPlayer.getAfkTask());
    			currPlayer.setAfkTask(-1);
				currPlayer.fall();
				updateLeaderboardStands();
			}
			if(e.getPlayer().isFlying())
			{
				e.getPlayer().teleport(spawnBlock);
				e.getPlayer().getInventory().setContents(currPlayer.getInventory());
				
				if(currPlayer.getHighScore() < currPlayer.getCurrScore()) {
					e.getPlayer().sendRawMessage("\u00A74You flew! \u00A77You beat your previous highscore of \u00A7f" + currPlayer.getHighScore() + "\u00A77!");	
					currPlayer.setHighScore(currPlayer.getCurrScore());
					updateLeaderboard(e.getPlayer().getUniqueId());
				}
				else	e.getPlayer().sendRawMessage("\u00A74You flew! \u00A77You didn't beat your previous highscore of \u00A7f" + currPlayer.getHighScore() + "\u00A77.");
				
				Bukkit.getServer().getScheduler().cancelTask(currPlayer.getAfkTask());
    			currPlayer.setAfkTask(-1);
				currPlayer.fall();
				updateLeaderboardStands();
			}
		}
	}
	
	public void reward(PlayerData currPlayer, Player e) {
		if(currPlayer.getCurrScore() >= 500) {
			if(e.getPlayer().getInventory().addItem(new ItemStack(Material.NETHERITE_INGOT, 1)).size() != 0) {
				e.getPlayer().sendRawMessage("\u00A74Inventory is full! Netherite Reward was Dropped!");
				spawnBlock.getWorld().dropItem(spawnBlock.clone().add(0,1,0), new ItemStack(Material.NETHERITE_INGOT,1));
			}
		}
		
		if(currPlayer.getCurrScore() >= 200) {
			if(e.getPlayer().getInventory().addItem(new ItemStack(Material.DIAMOND, 1)).size() != 0)
			{
				e.getPlayer().sendRawMessage("\u00A74Inventory is full! Diamond Reward was Dropped!");
				spawnBlock.getWorld().dropItem(spawnBlock.clone().add(0,1,0), new ItemStack(Material.DIAMOND,1));
			}
		}
		
		if(currPlayer.getCurrScore() >= 60) {
			if(e.getPlayer().getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 2)).size() != 0) {
				e.getPlayer().sendRawMessage("\u00A74Inventory is full! Gold Reward was Dropped!");
				spawnBlock.getWorld().dropItem(spawnBlock.clone().add(0,1,0), new ItemStack(Material.GOLD_INGOT,2));
			}
		}
		
		if(currPlayer.getCurrScore() >= 40) {
			if(e.getPlayer().getInventory().addItem(new ItemStack(Material.IRON_INGOT, 2)).size() != 0)	
				{
					e.getPlayer().sendRawMessage("\u00A74Inventory is full! Iron Reward was Dropped!");
					spawnBlock.getWorld().dropItem(spawnBlock.clone().add(0,1,0), new ItemStack(Material.IRON_INGOT,2));
				}
		}
	}
	
	// disable certain commands during parkouring
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		if(playerList.get(e.getPlayer().getUniqueId()) != null && playerList.get(e.getPlayer().getUniqueId()).isParkouring()) {
			if(e.getMessage().contains("msg") || e.getMessage().contains("tell") || e.getMessage().contains("r ")) {
				return;
			}
			e.getPlayer().sendRawMessage("\u00A74Commands aren't allowed while Parkouring!");
			e.setCancelled(true);
		}
	}
	
    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
    	if(playerList.get(e.getWhoClicked().getUniqueId()) == null || !playerList.get(((Player)e.getWhoClicked()).getUniqueId()).isParkouring()) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        int slot = e.getRawSlot();

        final Player p = (Player) e.getWhoClicked();
        if(GUIList.get(p.getUniqueId()).getCurrInv() == 0) {
        	if(clickedItem.getType() == Material.COBBLESTONE) {
        		GUIList.get(p.getUniqueId()).setGUI(p,1);
        	}
        	if(clickedItem.getType() == Material.NETHER_STAR) {
        		GUIList.get(p.getUniqueId()).setGUI(p, 2);
        	}
        	if(clickedItem.getType() == Material.OBSIDIAN) {
        		GUIList.get(p.getUniqueId()).setGUI(p, 3);
        	}
        }
        else if(GUIList.get(p.getUniqueId()).getCurrInv() == 1) {
        	if(slot == 18 || slot == 19 || slot == 20 || slot == 27 || slot == 28 || slot == 29) {
        		playerList.get(p.getUniqueId()).setMaterial(e.getCurrentItem().getType());
        		GUIList.get(p.getUniqueId()).refreshGUI(p,1);
        	}
        	else if(slot == 53)	GUIList.get(p.getUniqueId()).setGUI(p, 0);
        	else if((slot >= 14 && slot <= 17) || (slot >= 24 && slot <= 26) || (slot >= 33 && slot <= 36) ||(slot >= 41 && slot <= 44)) {
        		playerList.get(p.getUniqueId()).setMaterial(e.getCurrentItem().getType());
        		GUIList.get(p.getUniqueId()).refreshGUI(p,1);
        	}
        	else if(slot == 46) {
        		playerList.get(p.getUniqueId()).toggleCompleteRandBlock();
        		GUIList.get(p.getUniqueId()).refreshGUI(p,1);
        	}
        	else if(slot == 45) {
        		playerList.get(p.getUniqueId()).toggleRandBlock();
        		GUIList.get(p.getUniqueId()).refreshGUI(p,1);
        	}
        	else if(slot == 47) {
        		playerList.get(p.getUniqueId()).toggleRainbowBlock();
        		GUIList.get(p.getUniqueId()).refreshGUI(p,1);
        	}
        }
        else if(GUIList.get(p.getUniqueId()).getCurrInv() == 2) {
        	if(slot == 4) {
        		playerList.get(p.getUniqueId()).toggleParticles();
        		GUIList.get(p.getUniqueId()).refreshGUI(p, 2);
        	}
        	else if(slot == 8)	GUIList.get(p.getUniqueId()).setGUI(p, 0);
        }
        else if(GUIList.get(p.getUniqueId()).getCurrInv() == 3) {
        	if(slot == 14) {
        		playerList.get(p.getUniqueId()).toggleSound();
        		GUIList.get(p.getUniqueId()).refreshGUI(p, 3);
        	}
        	else if(slot == 16) {
        		playerList.get(p.getUniqueId()).togglePitchChange();
        		GUIList.get(p.getUniqueId()).refreshGUI(p, 3);
        	}
        	
        	else if(slot == 53) 	GUIList.get(p.getUniqueId()).setGUI(p, 0);
        	else if(slot == 9)	playerList.get(p.getUniqueId()).setSound(Sound.BLOCK_NOTE_BLOCK_BASS, p);
        	else if(slot == 10)	playerList.get(p.getUniqueId()).setSound(Sound.BLOCK_NOTE_BLOCK_SNARE, p);
        	else if(slot == 11)	playerList.get(p.getUniqueId()).setSound(Sound.BLOCK_NOTE_BLOCK_HAT, p);
        	else if(slot == 12)	playerList.get(p.getUniqueId()).setSound(Sound.BLOCK_NOTE_BLOCK_BASEDRUM, p);
        	else if(slot == 18)	playerList.get(p.getUniqueId()).setSound(Sound.BLOCK_NOTE_BLOCK_BELL, p);
        	else if(slot == 19)	playerList.get(p.getUniqueId()).setSound(Sound.BLOCK_NOTE_BLOCK_FLUTE, p);
        	else if(slot == 20)	playerList.get(p.getUniqueId()).setSound(Sound.BLOCK_NOTE_BLOCK_CHIME, p);
        	else if(slot == 21)	playerList.get(p.getUniqueId()).setSound(Sound.BLOCK_NOTE_BLOCK_GUITAR, p);
        	else if(slot == 27)	playerList.get(p.getUniqueId()).setSound(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, p);
        	else if(slot == 28)	playerList.get(p.getUniqueId()).setSound(Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, p);
        	else if(slot == 29)	playerList.get(p.getUniqueId()).setSound(Sound.BLOCK_NOTE_BLOCK_COW_BELL, p);
        	else if(slot == 30)	playerList.get(p.getUniqueId()).setSound(Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, p);
        	else if(slot == 36)	playerList.get(p.getUniqueId()).setSound(Sound.BLOCK_NOTE_BLOCK_BIT, p);
        	else if(slot == 37)	playerList.get(p.getUniqueId()).setSound(Sound.BLOCK_NOTE_BLOCK_BANJO, p);
        	else if(slot == 38)	playerList.get(p.getUniqueId()).setSound(Sound.BLOCK_NOTE_BLOCK_PLING, p);
        	else if(slot == 39)	playerList.get(p.getUniqueId()).setSound(Sound.BLOCK_NOTE_BLOCK_HARP, p);
        
        	else if(slot == 33)	{
        		playerList.get(p.getUniqueId()).increasePitch();
        		((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), playerList.get(e.getWhoClicked().getUniqueId()).getSound(), 10, playerList.get(e.getWhoClicked().getUniqueId()).getPitch());
        	}
        	
        	else if(slot == 34) {
        		playerList.get(p.getUniqueId()).decreasePitch();
        		((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), playerList.get(e.getWhoClicked().getUniqueId()).getSound(), 10, playerList.get(e.getWhoClicked().getUniqueId()).getPitch());
        	}
        	
        	GUIList.get(p.getUniqueId()).refreshGUI(p, 3);
        }
    }
    
    @EventHandler
    public void onItemClick(final PlayerInteractEvent e) {
    	if(playerList.get(e.getPlayer().getUniqueId()) == null) return;
    	if (e.getItem() == null || e.getItem().getType() == Material.AIR) return;
    	if(playerList.get(e.getPlayer().getUniqueId()).isParkouring()) e.setCancelled(true);
    	if(playerList.get(e.getPlayer().getUniqueId()).isParkouring() && e.getItem().getType() == Material.CHEST) {
    		//e.getPlayer().getInventory().clear();
    		e.getPlayer().getInventory().setHeldItemSlot(8);
    		GUIList.put(e.getPlayer().getUniqueId(), new CustomGUI(
    				0,
    				playerList.get(e.getPlayer().getUniqueId())
    				,e.getPlayer()));
    		GUIList.get(e.getPlayer().getUniqueId()).setGUI(e.getPlayer(), 0);
    	}
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
    	if(playerList.get(e.getWhoClicked().getUniqueId()) == null) return;
        if (playerList.get(((Player)e.getWhoClicked()).getUniqueId()).isParkouring()) {
          e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onInventoryDrop(final PlayerDropItemEvent e) {
    	if(playerList.get(e.getPlayer().getUniqueId()) == null) return;
        if (playerList.get((e.getPlayer()).getUniqueId()).isParkouring()) {
          e.setCancelled(true);
        }
    }
	
    // event handler for when a player leaves while parkouring
    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
    	if (playerList.get(e.getPlayer().getUniqueId()) == null || !playerList.get((e.getPlayer()).getUniqueId()).isParkouring())	return;
    	PlayerData player = playerList.get(e.getPlayer().getUniqueId());
    	e.getPlayer().getInventory().setContents(player.getInventory());
    	//reward(player,e.getPlayer());
    	if(player.getHighScore() < player.getCurrScore()) {	
			player.setHighScore(player.getCurrScore());
			updateLeaderboard(e.getPlayer().getUniqueId());
		}
    	
    	Bukkit.getServer().getScheduler().cancelTask(playerList.get(e.getPlayer().getUniqueId()).getAfkTask());
		playerList.get(e.getPlayer().getUniqueId()).setAfkTask(-1);
    	player.fall();
		updateLeaderboardStands();
		e.getPlayer().teleport(spawnBlock);
    }
    
	// When Disabling
	@Override
	public void onDisable() {
		deleteLeaderboard();
		for(Entry<UUID, PlayerData> player : playerList.entrySet()) {
			if(player.getValue().isParkouring()) {
				Bukkit.getServer().getScheduler().cancelTask(player.getValue().getAfkTask());
				player.getValue().setAfkTask(-1);
				player.getValue().fall();
				Bukkit.getPlayer(player.getKey()).getInventory().setContents(player.getValue().getInventory());
				Bukkit.getPlayer(player.getKey()).teleport(spawnBlock);
			}
			this.getCustomConfig().set("players." + player.getKey() + ".name",player.getValue().getPlayerName());
			this.getCustomConfig().set("players." + player.getKey() + ".score",player.getValue().getHighScore());
			this.getCustomConfig().set("players." + player.getKey() + ".block",player.getValue().getMaterial().toString());
			this.getCustomConfig().set("players." + player.getKey() + ".randBlock",player.getValue().isRandBlock());
			this.getCustomConfig().set("players." + player.getKey() + ".completeRandBlock",player.getValue().isCompleteRandBlock());
			this.getCustomConfig().set("players." + player.getKey() + ".rainbowBlock",player.getValue().isRainbowBlock());
			this.getCustomConfig().set("players." + player.getKey() + ".particles",player.getValue().isParticles());
			this.getCustomConfig().set("players." + player.getKey() + ".sound",player.getValue().isSound());
			this.getCustomConfig().set("players." + player.getKey() + ".currSound", player.getValue().getSound().toString());
			this.getCustomConfig().set("players." + player.getKey() + ".pitch", player.getValue().getPitch());
			this.getCustomConfig().set("players." + player.getKey() + ".pitchChange", player.getValue().isPitchChange());
			this.getCustomConfig().set("players." + player.getKey() + ".totalJumps", player.getValue().getTotalJumps());
		}
		
		try {
			customConfig.save(customConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		getLogger().info("DravPack has been disabled!");
		
		
	}
	
	public FileConfiguration getCustomConfig() {
        return this.customConfig;
	}
	
	 private void createCustomConfig() {
	        customConfigFile = new File(getDataFolder(), "custom.yml");
	        if (!customConfigFile.exists()) {
	            customConfigFile.getParentFile().mkdirs();
	            saveResource("custom.yml", false);
	         }

	        customConfig= new YamlConfiguration();
	        try {
	            customConfig.load(customConfigFile);
	        } catch (IOException | InvalidConfigurationException e) {
	            e.printStackTrace();
	        }
	 }
}


