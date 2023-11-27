package com.drav.dravparkour;

import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerData {
	
	private int totalJumps;
	private int highscore;
	private String name;
	private Material block;
	private int currScore;
	private Block currentBlock;
	private Block lastBlock;
	private Queue<Block> nextBlocks = new LinkedList<>();
	private boolean pking = false;
	private ItemStack[] inv;
	private boolean randBlock;
	private boolean completeRandBlock;
	private boolean rainbowBlock;
	private Inventory settingsInv;
	private Inventory blockInv;
	private int afkTimer;
	private int numBlocks;
	private boolean particles = true;
	private boolean sound = true;
	private Sound currSound;
	private float pitch;
	private int pitchLevel;
	private boolean pitchChange = false;
	private int afkTask;
	private int prevDirection;
	
	public PlayerData(String name, int numBlocks)
	{
		this.highscore = 0;
		this.name = name;
		this.block = Material.QUARTZ_BLOCK;
		this.currSound = Sound.BLOCK_NOTE_BLOCK_CHIME;
		this.pitch = 2^(-2/12);
		this.completeRandBlock = false;
		this.randBlock = false;
		this.rainbowBlock = false;
		this.numBlocks = numBlocks;
	}
	
	public PlayerData(int totalJumps, int hs, String name, Material block, int numBlocks, boolean randBlock, boolean completeRandBlock, boolean rainbowBlock, boolean particles, boolean sound, Sound currSound, float pitch, boolean pitchChange)
	{
		this.totalJumps = totalJumps;
		this.highscore = hs;
		this.name = name;
		this.block = block;
		this.numBlocks = numBlocks;
		this.randBlock = randBlock;
		this.completeRandBlock = completeRandBlock;
		this.rainbowBlock = rainbowBlock;
		this.particles = particles;
		this.sound = sound;
		this.currSound = currSound;
		this.pitch = pitch;
		this.pitchChange = pitchChange;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getPrevDirection() {
		return prevDirection;
	}
	
	public void setPrevDirection(int prevDirection) {
		this.prevDirection = prevDirection;
	}
	
	public void setAfkTask(int task) {
		afkTask = task;
	}
	
	public int getAfkTask() {
		return afkTask;
	}
	
	public int getTotalJumps() {
		return totalJumps;
	}
	
	public boolean isPitchChange() {
		return pitchChange;
	}
	
	public void increasePitch() {
		pitchLevel++;
		if(pitchLevel > 24) {
			pitchLevel = 0;
		}
		pitch = (float) Math.pow(2.0,((pitchLevel-12.0)/12.0));
	}
	
	public void decreasePitch() {
		pitchLevel--;
		if(pitchLevel < 0) {
			pitchLevel = 24;
		}
		pitch = (float) Math.pow(2.0,((pitchLevel-12.0)/12.0));
	}
	
	public int getPitchLevel() {
		return pitchLevel;
	}
	
	public void togglePitchChange() {
		if(pitchChange) pitchChange = false;
		else pitchChange = true;
	}
	
	public void toggleParticles() {
		if(particles) particles = false;
		else particles = true;
	}
	
	public boolean isParticles() {
		return particles;
	}
	
	public void toggleSound() {
		if(sound) sound = false;
		else sound = true;
	}
	
	public boolean isSound() {
		return sound;
	}
	
	public Sound getSound() {
		return currSound;
	}
	
	public void setSound(Sound currSound, Player p) {
		this.currSound = currSound;
		p.playSound(p.getLocation(), currSound, 10, pitch);
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	// inv
	public ItemStack[] getInventory() {
		return inv;
	}
	
	public Inventory getSettingsInv() {
		return settingsInv;
	}
	
	public void setSettingsInv(Inventory inv) {
		settingsInv = inv;
	}
	
	public Inventory getblockInv() {
		return blockInv;
	}
	
	public void setBlockInv(Inventory inv) {
		blockInv = inv;
	}
	
	public void setInventory(ItemStack[] inv) {
		this.inv = inv;
	}
	
	
	
	// high score
	public int getHighScore() {
		return highscore;
	}
	
	public void setHighScore(int hs) {
		this.highscore = hs;
	}
	
	// current score
	public int getCurrScore() {
		return currScore;
	}
	
	public void setCurrScore(int currScore) {
		this.currScore = currScore;
	}
	
	// player name
	public String getPlayerName() {
		return name;
	}
	
	// rand
	public boolean isRandBlock() {
		return randBlock;
	}
	
	public boolean isCompleteRandBlock() {
		return completeRandBlock;
	}
	
	public void toggleRandBlock() {
		if(randBlock) {
			randBlock = false;
			completeRandBlock = false;
			rainbowBlock = false;
		}
		else {
			randBlock = true;
			completeRandBlock = false;
			rainbowBlock = false;
		}
	}
	
	public boolean isRainbowBlock() {
		return rainbowBlock;
	}
	
	public void toggleRainbowBlock() {
		if(rainbowBlock) {
			rainbowBlock = false;
			randBlock = false;
			completeRandBlock = false;
		}
		else {
			rainbowBlock = true;
			randBlock = false;
			completeRandBlock = false;
		}
	}
	
	public void toggleCompleteRandBlock() {
		if(completeRandBlock) {
			completeRandBlock = false;
			randBlock = false;
			rainbowBlock = false;
		}
		else {
			rainbowBlock = false;
			completeRandBlock = true;
			randBlock = true;
		}
	}
	
	// material
	public Material getMaterial() {
		return block;
	}
	
	public void setMaterial(Material block) {
		this.block = block;
		afkTimer = 0;
	}
	
	// parkouring
	public void setParkouring(boolean pking) {
		this.pking = pking;
		if(pking) {
			afkTimer = 0;
		}
	}
	
	public int getAfkTimer() {
		return afkTimer;
	}
	
	public void incrementAfkTimer() {
		afkTimer++;
	}
	
	public void resetAfkTimer() {
		afkTimer = 0;
	}
	
	public boolean isParkouring() {
		return pking;
	}
	
	// block control
	public Block getCurrentBlock() {
		return currentBlock;
	}
	
	public void setCurrentBlock(Block currentBlock) {
		this.currentBlock = currentBlock;
		currentBlock.setType(block);
	}
	
	public void addNextBlock(Block nextBlock) {
		nextBlocks.add(nextBlock);
		if(numBlocks > 2) {
			lastBlock = nextBlock;
		}
	}
	
	public Block getLastBlock() {
		if(nextBlocks == null || nextBlocks.size() == 0) {
			return currentBlock;
		}
		else if(lastBlock == null) {
			return nextBlocks.peek();
		}
		return lastBlock;
	}
	
	public boolean nextBlockQueueIncomplete() {
		if(nextBlocks == null) {
			return true;
		}
		if(nextBlocks.size() < numBlocks-1) {
			return true;
		}
		return false;
	}
	
	public Block getNextBlock() {
		return nextBlocks.peek();
	}
	
	public void popCurrentBlock() {
		currentBlock.setType(Material.AIR);
		currentBlock = nextBlocks.poll();
		resetAfkTimer();
		currScore++;
	}
	
	// situations
	public void fall() {
		currentBlock.setType(Material.AIR);
		while(nextBlocks.size() > 0) {
			nextBlocks.poll().setType(Material.AIR);
		}
		totalJumps += currScore;
		currentBlock = null;
		currScore = 0;
		pking = false;
	}
}