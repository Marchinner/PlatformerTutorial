package entities;

import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utils.Constants.ANIMATION_SPEED;
import static utils.Constants.GRAVITY;
import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;

public class Player extends Entity {

	private Playing playing;
	private BufferedImage[][] animations;
	private boolean playerIsMoving = false;
	private boolean playerIsAttacking = false;
	private boolean left;
	private boolean right;
	private boolean jump;
	private int[][] levelData;
	private float xDrawOffset = 21 * Game.SCALE;
	private float yDrawOffset = 4 * Game.SCALE;

	// Jumping and Gravity
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

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

	private int healthWidth = healthBarWidth;



	private int flipX = 0;
	private int flipW = 1;

	private boolean attackChecked;

	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		this.state = IDLE;
		this.maxHealth = 100;
		this.currentHealth = maxHealth;
		this.walkSpeed = Game.SCALE * 1.0f;
		loadAnimations();
		initializeHitbox(20, 27);
		initAttackBox();
	}

	public void setSpawn(Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		hitbox.x = x;
		hitbox.y = y;
	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (20 * Game.SCALE), (int) (20 * Game.SCALE));
	}

	public void update() {
		updateHealthBar();
		if (currentHealth <= 0) {
			playing.setGameOver(true);
			return;
		}
		updateAttackBox();

		updatePosition();
		if (playerIsAttacking) {
			checkAttack();
		}
		updateAnimationTick();
		setAnimation();
	}

	private void checkAttack() {
		if (attackChecked || animationIndex != 1) {
			return;
		} else {
			attackChecked = true;
			playing.checkEnemyHit(attackBox);
		}
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
		graphics.drawImage(animations[state][animationIndex],
				(int) (hitbox.x - xDrawOffset) - levelOffset + flipX,
				(int) (hitbox.y - yDrawOffset),
				width * flipW, height, null);
//		drawHitbox(graphics, levelOffset);
//		drawAttackBox(graphics, levelOffset);

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
			xSpeed -= walkSpeed;
			flipX = width;
			flipW = -1;
		}

		if (right) {
			xSpeed += walkSpeed;
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
				airSpeed += GRAVITY;
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
		int startAnimation = state;

		if (playerIsMoving)
			state = RUNNING;
		else
			state = IDLE;

		if (inAir) {
			if (airSpeed < 0)
				state = JUMPING;
			else
				state = FALLING;
		}

		if (playerIsAttacking) {
			state = ATTACK;
			if (startAnimation != ATTACK) {
				animationIndex = 1;
				animationTick = 0;
				return;
			}
		}
		if (startAnimation != state)
			resetAnimationTick();
	}

	private void resetAnimationTick() {
		animationTick = 0;
		animationIndex = 0;
	}

	private void updateAnimationTick() {
		animationTick++;
		if (animationTick >= ANIMATION_SPEED) {
			animationTick = 0;
			animationIndex++;
			if (animationIndex >= getSpriteAmount(state)) {
				animationIndex = 0;
				playerIsAttacking = false;
				attackChecked = false;
			}
		}
	}

	public void resetDirectionsBooleans() {
		left = false;
		right = false;
	}

	public void setPlayerIsAttacking(boolean isAttacking) {
		this.playerIsAttacking = isAttacking;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public void resetAll() {
		resetDirectionsBooleans();
		inAir = false;
		playerIsAttacking = false;
		playerIsMoving = false;
		state = IDLE;
		currentHealth = maxHealth;

		hitbox.x = x;
		hitbox.y = y;

		if (!isEntityOnFloor(hitbox, levelData)) {
			inAir = true;
		}
	}
}
