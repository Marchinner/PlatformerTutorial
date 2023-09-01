package entities;

import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.PlayerConstants.*;

public class Player extends Entity {

	private BufferedImage[][] animations;
	private int animationTick = 25;
	private int animationIndex = 25;
	private int animationSpeed = 25;
	private int playerAction = IDLE;
	private boolean playerIsMoving = false;
	private boolean playerIsAttacking = false;
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	private float playerSpeed = 1.5f;

	public Player(float x, float y, int width, int height) {
		super(x, y, width, height);
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
				(int) x, (int) y, width, height, null);
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
			BufferedImage image = LoadSave.getSpriteAtlas(LoadSave.PLAYER_ATLAS);

			animations = new BufferedImage[9][6];
			for (int j = 0; j < animations.length; j++) {
				for (int i = 0; i < animations[j].length; i++) {
					animations[j][i] = image.getSubimage(i * 64, j * 40, 64, 40);
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
