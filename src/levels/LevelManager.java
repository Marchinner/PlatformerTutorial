package levels;

import gamestates.Gamestate;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static main.Game.TILES_SIZE;

public class LevelManager {
	private Game game;
	private BufferedImage[] levelSprite;
	private ArrayList<Level> levels;
	private int levelIndex = 0;

	public LevelManager(Game game) {
		this.game = game;
		importOutsideSprites();
		levels = new ArrayList<>();
		buildAllLevels();
	}

	private void buildAllLevels() {
		BufferedImage[] allLevels = LoadSave.getAllLevels();
		for (BufferedImage img : allLevels) {
			levels.add(new Level(img));
		}
	}

	private void importOutsideSprites() {
		BufferedImage image = LoadSave.getSpriteAtlas(LoadSave.LEVEL_ATLAS);
		levelSprite = new BufferedImage[48];

		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 12; i++) {
				int index = j * 12 + i;
				levelSprite[index] = image.getSubimage(i * 32, j * 32, 32, 32);
			}
		}
	}

	public void draw(Graphics graphics, int levelOffset) {
		for (int j = 0; j < Game.TILES_IN_HEIGHT; j++) {
			for (int i = 0; i < levels.get(levelIndex).getLevelData()[0].length; i++) {
				int index = levels.get(levelIndex).getSpriteIndex(i, j);
				graphics.drawImage(levelSprite[index], TILES_SIZE * i - levelOffset, TILES_SIZE * j, TILES_SIZE, TILES_SIZE, null);
			}
		}
	}

	public void update() {

	}

	public Level getCurrentLevel() {
		return levels.get(levelIndex);
	}

	public int getAmountOfLevels() {
		return levels.size();
	}

	public void loadNextLevel() {
		levelIndex++;

		if (levelIndex >= levels.size()) {
			levelIndex = 0;
			System.out.println("GAME COMPLETED!");
			Gamestate.state = Gamestate.MENU;
		}

		Level newLevel = levels.get(levelIndex);
		game.getPlaying().getEnemyManager().loadEnemies(newLevel);
		game.getPlaying().getPlayer().loadLevelData(newLevel.getLevelData());
		game.getPlaying().setMaxLevelOffset(newLevel.getLevelOffset());
	}
}
