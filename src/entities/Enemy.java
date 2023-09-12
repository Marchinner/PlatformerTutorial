package entities;

import main.Game;

import java.awt.geom.Rectangle2D;

import static utils.Constants.ANIMATION_SPEED;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.GRAVITY;
import static utils.HelpMethods.*;
import static utils.Constants.Directions.*;

public abstract class Enemy extends Entity {

    protected int enemyType;
    protected boolean firstUpdate = true;
    protected float walkSpeed = 0.35f * Game.SCALE;
    protected int walkingDirection = LEFT;
    protected int tileY;
    protected float attackDistance = Game.TILES_SIZE;
    protected boolean active = true;
    protected boolean attackChecked;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        maxHealth = getMaxHealth(enemyType);
        currentHealth = maxHealth;
        walkSpeed = Game.SCALE * 0.35f;
    }

    protected void firstUpdateCheck(int [][] levelData) {
        if (!isEntityOnFloor(hitbox, levelData)) {
            inAir = true;
        }
        firstUpdate = false;
    }

    protected void updateInAir(int[][] levelData) {
        if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        } else {
            inAir = false;
            hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            tileY = (int) (hitbox.y / Game.TILES_SIZE);
        }
    }

    protected void move(int[][] levelData) {
        float xSpeed = 0;

        if (walkingDirection == LEFT) {
            xSpeed = -walkSpeed;
        } else {
            xSpeed = walkSpeed;
        }

        if (canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
            if (isFloor(hitbox, xSpeed, levelData)) {
                hitbox.x += xSpeed;
                return;
            }
        }
        changeWalkingDirection();
    }

    protected void turnTowardsPlayer(Player player) {
        if (player.hitbox.x > hitbox.x)
            walkingDirection = RIGHT;
        else
            walkingDirection = LEFT;
    }

    protected boolean canSeePlayer(int[][] levelData, Player player) {
        int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE);
        if (playerTileY == tileY) {
            if (isPlayerInRange(player)) {
                if (isSightClear(levelData, hitbox, player.hitbox, tileY)) {
                    return true;
                }
            }
        }
        
        return false;
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int absoluteValue = (int) (Math.abs(player.hitbox.x - hitbox.x));
        return absoluteValue <= attackDistance;
    }

    protected boolean isPlayerInRange(Player player) {
        int absoluteValue = (int) (Math.abs(player.hitbox.x - hitbox.x));
        return absoluteValue <= attackDistance * 5;
    }

    protected void newState(int enemyState) {
        this.state = enemyState;
        animationTick = 0;
        animationIndex = 0;
    }

    protected void checkEnemyHit(Rectangle2D.Float attackBox, Player player) {
        if (attackBox.intersects(player.hitbox)) {
            player.changeHealth(-getEnemyDamage(enemyType));
        }
        attackChecked = true;
    }

    public void hurt(int amount) {
        currentHealth -= amount;
        if (currentHealth <= 0) {
            newState(DEAD);
        } else {
            newState(HIT);
        }
    }

    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(enemyType, state)) {
                animationIndex = 0;

                switch (state) {
                    case ATTACK, HIT -> state = IDLE;
                    case DEAD -> active = false;
                }
            }
        }
    }

    public void update(int[][] levelData) {
        updateMove(levelData);
        updateAnimationTick();
    }

    private void updateMove(int[][] levelData) {
        if (firstUpdate) {
            if (!isEntityOnFloor(hitbox, levelData)) {
                inAir = true;
            }
            firstUpdate = false;
        }

        if (inAir) {
            if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) {
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
            } else {
                inAir = false;
                hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            }
        } else {
            switch (state) {
                case IDLE -> {
                    state = RUNNING;
                }
                case RUNNING -> {
                    float xSpeed = 0;

                    if (walkingDirection == LEFT) {
                        xSpeed = -walkSpeed;
                    } else {
                        xSpeed = walkSpeed;
                    }

                    if (canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
                        if (isFloor(hitbox, xSpeed, levelData)) {
                            hitbox.x += xSpeed;
                            return;
                        }
                    }
                    changeWalkingDirection();
                }
            }
        }
    }

    protected void changeWalkingDirection() {
        if (walkingDirection == LEFT) {
            walkingDirection = RIGHT;
        } else {
            walkingDirection = LEFT;
        }
    }

    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        airSpeed = 0;
    }

    public boolean isActive() {
        return active;
    }
}
