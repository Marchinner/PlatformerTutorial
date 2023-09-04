package utils;

import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LoadSave {

	public static final String PLAYER_ATLAS = "player_sprites.png";
	public static final String LEVEL_ATLAS = "outside_sprites.png";
//	public static final String LEVEL_ONE_DATA = "level_one_data.png";
	public static final String LEVEL_ONE_DATA = "level_one_data_long.png";
	public static final String MENU_BUTTONS = "button_atlas.png";
	public static final String MENU_BACKGROUND = "menu_background.png";
	public static final String MENU_BACKGROUND_IMAGE = "background_menu.png";
	public static final String PAUSE_BACKGROUND = "pause_menu.png";
	public static final String SOUND_BUTTONS = "sound_button.png";
	public static final String URM_BUTTONS = "urm_buttons.png";
	public static final String VOLUME_BUTTONS = "volume_buttons.png";

	public static BufferedImage getSpriteAtlas(String fileName) {
		BufferedImage image = null;

		InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
		try {
			image = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return image;
	}

	public static int[][] getLevelData() {
		BufferedImage image = getSpriteAtlas(LEVEL_ONE_DATA);
		int[][] levelData = new int[image.getHeight()][image.getWidth()];

		for (int j = 0; j < image.getHeight(); j++) {
			for (int i = 0; i < image.getWidth(); i++) {
				Color color = new Color(image.getRGB(i, j));
				int value = color.getRed();
				if (value >= 48) {
					value = 0;
				}
				levelData[j][i] = value;
			}
		}

		return levelData;
	}
}
