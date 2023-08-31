package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;
import utils.Constants;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utils.Constants.PlayerConstants.*;
import static utils.Constants.Directions.*;

public class GamePanel extends JPanel {
	private MouseInputs mouseInputs;
	private float xDelta = 100;
	private float yDelta = 100;
	private BufferedImage image;
	private BufferedImage[][] animations;
	private int animationTick;
	private int animationIndex;
	private int animationSpeed = 15;
	private int playerAction = IDLE;
	private int playerDirection = -1;
	private boolean playerIsMoving = false;

	public GamePanel() {
		mouseInputs = new MouseInputs(this);

		importImage();
		loadAnimations();

		addKeyListener(new KeyboardInputs(this));
		setPanelSize();
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}

	private void loadAnimations() {
		animations = new BufferedImage[9][6];
		for (int j = 0; j < animations.length; j++) {
			for (int i = 0; i < animations[j].length; i++) {
				animations[j][i] = image.getSubimage(i * 64, j * 40, 64, 40);
			}
		}
	}

	private void importImage() {
		InputStream is = getClass().getResourceAsStream("/player_sprites.png");

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
	}

	private void setPanelSize() {
		Dimension size = new Dimension(1280, 800);
		setPreferredSize(size);
	}

	public void setPlayerDirection(int playerDirection) {
		this.playerDirection = playerDirection;
		playerIsMoving = true;
	}

	public void setPlayerIsMoving(boolean moving) {
		this.playerIsMoving = moving;
	}

	public void updateGame() {
		updateAnimationTick();
		setAnimation();
		updatePosition();
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);


		graphics.drawImage(
				animations[playerAction][animationIndex],
				(int) xDelta, (int) yDelta,
				256, 160, null);
	}

	private void updatePosition() {
		if (playerIsMoving) {
			switch (playerDirection) {
				case LEFT -> {
					xDelta -= 5;
				}
				case UP -> {
					yDelta -= 5;
				}
				case RIGHT -> {
					xDelta += 5;
				}
				case DOWN -> {
					yDelta += 5;
				}
			}
		}
	}

	private void setAnimation() {
		if (playerIsMoving) {
			playerAction = RUNNING;
		} else {
			playerAction = IDLE;
		}
	}


	private void updateAnimationTick() {
		animationTick++;
		if (animationTick >= animationSpeed) {
			animationTick = 0;
			animationIndex++;
			if (animationIndex >= getSpriteAmount(playerAction)) {
				animationIndex = 0;
			}
		}
	}
}
