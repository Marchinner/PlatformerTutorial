package entities;

import main.Game;

import static utils.Constants.EnemyConstants.*;
import static utils.HelpMethods.*;
import static utils.Constants.Directions.*;

public abstract class Enemy extends Entity {

    protected int animationIndex;
    protected int enemyState;
    protected int enemyType;
    protected int animationTick;
    protected int animationSpeed = 25;
    protected boolean firstUpdate = true;
    protected boolean inAir = false;
    protected float fallSpeed;
    protected float gravity = 0.04f * Game.SCALE;
    protected float walkSpeed = 0.35f * Game.SCALE;
    protected int walkingDirection = LEFT;
    protected int tileY;
    protected float attackDistance = Game.TILES_SIZE;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initializeHitbox(x, y, width, height);
    }

    protected void firstUpdateCheck(int [][] levelData) {
        if (!isEntityOnFloor(hitbox, levelData)) {
            inAir = true;
        }
        firstUpdate = false;
    }

    protected void updateInAir(int[][] levelData) {
        if (canMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, levelData)) {
            hitbox.y += fallSpeed;
            fallSpeed += gravity;
        } else {
            inAir = false;
            hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
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
        this.enemyState = enemyState;
        animationTick = 0;
        animationIndex = 0;
    }

    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(enemyType, enemyState)) {
                animationIndex = 0;
                if (enemyState == ATTACK)
                    enemyState = IDLE;
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
            if (canMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, levelData)) {
                hitbox.y += fallSpeed;
                fallSpeed += gravity;
            } else {
                inAir = false;
                hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
            }
        } else {
            switch (enemyState) {
                case IDLE -> {
                    enemyState = RUNNING;
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

    public int getAnimationIndex() {
        return animationIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }
}
