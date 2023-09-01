package entities;

import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;

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
	private boolean jump;
	private float playerSpeed = 2f;
	private int[][] levelData;
	private float xDrawOffset = 21 * Game.SCALE;
	private float yDrawOffset = 4 * Game.SCALE;

	// Jumping and Gravity
	private float airSpeed = 0f;
	private float gravity = 0.04f * Game.SCALE;
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	private boolean inAir = false;

	public Player(float x, float y, int width, int height) {
		super(x, y, width, height);
		loadAnimations();
		initializeHitbox(x, y, 20 * Game.SCALE, (int) (27 * Game.SCALE));
	}

	public void update() {
		updatePosition();
		updateAnimationTick();
		setAnimation();
	}

	public void render(Graphics graphics) {
		graphics.drawImage(animations[playerAction][animationIndex],
				(int) (hitbox.x - xDrawOffset), (int) (hitbox.y - yDrawOffset),
				width, height, null);
	}

	private void updatePosition() {
		playerIsMoving = false;

		if (jump) {
			jump();
		}
		if (!left && !right && !inAir) {
			return;
		}

		float xSpeed = 0;

		if (left) {
			xSpeed -= playerSpeed;
		}
		if (right) {
			xSpeed += playerSpeed;
		}

		if (!inAir) {
			if (!isEntityOnFloor(hitbox, levelData)) {
				inAir = true;
			}
		}

		if (inAir) {
			if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) {
				hitbox.y += airSpeed;
				airSpeed += gravity;
				updateXPos(xSpeed);
			} else {
				hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if (airSpeed > 0) {
					resetInAir();
				} else {
					airSpeed = fallSpeedAfterCollision;
				}

				updateXPos(xSpeed);
			}
		} else {
			updateXPos(xSpeed);
		}
		playerIsMoving = true;
	}

	private void jump() {
		if (inAir) {
			return;
		} else {
			inAir = true;
			airSpeed = jumpSpeed;
		}
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	private void updateXPos(float xSpeed) {
		if (canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
			hitbox.x += xSpeed;
		} else {
			hitbox.x = getEntityXPosNextToWall(hitbox, xSpeed);
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

	public void loadLevelData(int[][] levelData) {
		this.levelData = levelData;
		if (!isEntityOnFloor(hitbox, levelData)) {
			inAir = true;
		}
	}

	private void setAnimation() {
		int startAnimation = playerAction;

		if (playerIsMoving) {
			playerAction = RUNNING;
		} else {
			playerAction = IDLE;
		}

		if (inAir) {
			if (airSpeed < 0) {
				playerAction = JUMPING;
			} else {
				playerAction = FALLING;
			}
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

	public void setJump(boolean jump) {
		this.jump = jump;
	}
}
