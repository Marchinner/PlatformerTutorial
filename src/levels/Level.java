package levels;

import entities.Crabby;
import main.Game;
import utils.HelpMethods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Level {

	private BufferedImage img;
	private int[][] levelData;
	private ArrayList<Crabby> crabs;
	private int levelTilesWide;
	private int maxTilesOffset;
	private int maxLevelOffsetX;
	private Point playerSpawn;

	public Level(BufferedImage img) {
		this.img = img;
		createLevelData();
		createEnemies();
		calculateLevelOffsets();
		calculatePlayerSpawn();
	}

	private void calculatePlayerSpawn() {
		playerSpawn = HelpMethods.getPlayerSpawn(img);
	}

	private void calculateLevelOffsets() {
		levelTilesWide = img.getWidth();
		maxTilesOffset = levelTilesWide - Game.TILES_IN_WIDTH;
		maxLevelOffsetX = Game.TILES_SIZE * maxTilesOffset;
	}

	private void createEnemies() {
		crabs = HelpMethods.getCrabs(img);
	}

	private void createLevelData() {
		levelData = HelpMethods.getLevelData(img);
	}

	public int getSpriteIndex(int x, int y) {
		return levelData[y][x];
	}

	public int getLevelOffset() {
		return maxLevelOffsetX;
	}

	public ArrayList<Crabby> getCrabs() {
		return crabs;
	}

	public int[][] getLevelData() {
		return levelData;
	}

	public Point getPlayerSpawn() {
		return playerSpawn;
	}
}
