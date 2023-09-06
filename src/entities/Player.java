package entities;

import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;

public class Player extends Entity {

	private BufferedImage[][] animations;
	private int animationTick;
	private int animationIndex;
	private int animationSpeed = 25;
	private int playerAction = IDLE;
	private boolean playerIsMoving = false;
	private boolean playerIsAttacking = false;
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	private boolean jump;
	private float playerSpeed = 1.0f * Game.SCALE;
	private int[][] levelData;
	private float xDrawOffset = 21 * Game.SCALE;
	private float yDrawOffset = 4 * Game.SCALE;

	// Jumping and Gravity
	private float airSpeed = 0f;
	private float gravity = 0.04f * Game.SCALE;
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	private boolean inAir = false;

	// Status bar UI
	private BufferedImage statusBarImg;
	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);

	private int healthBarWidth = (int) (150 * Game.SCALE);
	private int healthBarHeight = (int) (4 * Game.SCALE);
	private int healthBarXStart = (int) (34 * Game.SCALE);
	private int healthBarYStart = (int) (14 * Game.SCALE);

	private int maxHealth = 100;
	private int currentHealth = maxHealth;
	private int healthWidth = healthBarWidth;

	// Attack box
	private Rectangle2D.Float attackBox;

	private int flipX = 0;
	private int flipW = 1;

	public Player(float x, float y, int width, int height) {
		super(x, y, width, height);
		loadAnimations();
		initializeHitbox(x, y, (int) (20 * Game.SCALE), (int) (27 * Game.SCALE));
		initAttackBox();
	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (20 * Game.SCALE), (int) (20 * Game.SCALE));

	}

	public void update() {
		updateHealthBar();
		updateAttackBox();

		updatePosition();
		updateAnimationTick();
		setAnimation();
	}

	private void updateAttackBox() {
		if (right) {
			attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE * 10);
		} else if (left) {
			attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 10);
		}

		attackBox.y = hitbox.y + (Game.SCALE * 10);
	}

	private void updateHealthBar() {
		healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
	}

	public void render(Graphics graphics, int levelOffset) {
		graphics.drawImage(animations[playerAction][animationIndex],
				(int) (hitbox.x - xDrawOffset) - levelOffset + flipX,
				(int) (hitbox.y - yDrawOffset),
				width * flipW, height, null);
//		drawHitbox(graphics, levelOffset);
		drawAttackBox(graphics, levelOffset);

		drawUI(graphics);
	}

	private void drawAttackBox(Graphics graphics, int levelOffsetX) {
		graphics.setColor(Color.RED);
		graphics.drawRect((int) attackBox.x - levelOffsetX, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
	}

	private void drawUI(Graphics graphics) {
		graphics.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		graphics.setColor(Color.RED);
		graphics.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
	}

	private void updatePosition() {
		playerIsMoving = false;

		if (jump)
			jump();

		if (!inAir) {
			if ((!left && !right) || (right && left))
				return;
		}

		float xSpeed = 0;

		if (left) {
			xSpeed -= playerSpeed;
			flipX = width;
			flipW = -1;
		}

		if (right) {
			xSpeed += playerSpeed;
			flipX = 0;
			flipW = 1;
		}

		if (!inAir) {
			if (!isEntityOnFloor(hitbox, levelData))
				inAir = true;
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

	public void changeHealth(int value) {
		currentHealth += value;

		if (currentHealth <= 0) {
			currentHealth = 0;
			// gameover();
		} else if (currentHealth >= maxHealth)
			currentHealth = maxHealth;
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

		animations = new BufferedImage[7][8];
		for (int j = 0; j < animations.length; j++) {
			for (int i = 0; i < animations[j].length; i++) {
				animations[j][i] = image.getSubimage(i * 64, j * 40, 64, 40);
			}
		}

		statusBarImg = LoadSave.getSpriteAtlas(LoadSave.STATUS_BAR);
	}

	public void loadLevelData(int[][] levelData) {
		this.levelData = levelData;
		if (!isEntityOnFloor(hitbox, levelData)) {
			inAir = true;
		}
	}

	private void setAnimation() {
		int startAnimation = playerAction;

		if (playerIsMoving)
			playerAction = RUNNING;
		else
			playerAction = IDLE;

		if (inAir) {
			if (airSpeed < 0)
				playerAction = JUMPING;
			else
				playerAction = FALLING;
		}

		if (playerIsAttacking)
			playerAction = ATTACK;

		if (startAnimation != playerAction)
			resetAnimationTick();
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
