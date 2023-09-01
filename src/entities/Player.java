package entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utils.Constants.Directions.*;
import static utils.Constants.Directions.DOWN;
import static utils.Constants.PlayerConstants.*;

public class Player extends Entity {

	private BufferedImage[][] animations;
	private int animationTick;
	private int animationIndex;
	private int animationSpeed = 30;
	private int playerAction = IDLE;
	private boolean playerIsMoving = false;
	private boolean playerIsAttacking = false;
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	private float playerSpeed = 2.0f;

	public Player(float x, float y) {
		super(x, y);
		loadAnimations();
	}

	public void update() {
		updatePosition();
		updateAnimationTick();
		setAnimation();
	}

	public void render(Graphics graphics) {
		graphics.drawImage(
				animations[playerAction][animationIndex],
				(int) x, (int) y,
				256, 160, null);
	}

	private void updatePosition() {
		playerIsMoving = false;

		if (left && !right) {
			x -= playerSpeed;
			playerIsMoving = true;
		} else if (right && !left){
			x += playerSpeed;
			playerIsMoving = true;
		}

		if (up && !down) {
			y -= playerSpeed;
			playerIsMoving = true;
		} else if (down && !up) {
			y += playerSpeed;
			playerIsMoving = true;
		}
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	private void loadAnimations() {
		InputStream is = getClass().getResourceAsStream("/player_sprites.png");
		try {
			BufferedImage image = ImageIO.read(is);

			animations = new BufferedImage[9][6];
			for (int j = 0; j < animations.length; j++) {
				for (int i = 0; i < animations[j].length; i++) {
					animations[j][i] = image.getSubimage(i * 64, j * 40, 64, 40);
				}
			}
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

	private void setAnimation() {
		int startAnimation = playerAction;

		if (playerIsMoving) {
			playerAction = RUNNING;
		} else {
			playerAction = IDLE;
		}

		if (playerIsAttacking) {
			playerAction = ATTACK_1;
		}

		if (startAnimation != playerAction) {
			resetAnimationTick();
		}
	}

	private void resetAnimationTick() {
		animationTick = 0;
		animationIndex = 0;
	}

	private void updateAnimationTick() {
		animationTick++;
		if (animationTick >= animationSpeed) {
			animationTick = 0;
			animationIndex++;
			if (animationIndex >= getSpriteAmount(playerAction)) {
				animationIndex = 0;
				playerIsAttacking = false;
			}
		}
	}

	public void resetDirectionsBooleans() {
		left = false;
		right = false;
		up = false;
		down = false;
	}

	public void setPlayerIsAttacking(boolean isAttacking) {
		this.playerIsAttacking = isAttacking;
	}
}
