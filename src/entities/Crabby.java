package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utils.Constants.Directions.*;
import static utils.Constants.EnemyConstants.*;

public class Crabby extends Enemy{

    private Rectangle2D.Float attackBox;
    private int attackBoxOffsetX;

    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initializeHitbox(x, y, (int) (22 * Game.SCALE), (int) (19 * Game.SCALE));
        initializeAttackBox();
    }

    private void initializeAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (82 * Game.SCALE), (int) (19 * Game.SCALE));
        attackBoxOffsetX = (int) (Game.SCALE * 30);
    }

    public void update(int[][] levelData, Player player) {
        updateMove(levelData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    private void updateMove(int[][] levelData, Player player) {
        if (firstUpdate)
            firstUpdateCheck(levelData);
        if (inAir)
            updateInAir(levelData);
        else {
            switch (enemyState) {
                case IDLE -> newState(RUNNING);
                case RUNNING -> {
                    if (canSeePlayer(levelData, player))
                        turnTowardsPlayer(player);
                    if (isPlayerCloseForAttack(player))
                        newState(ATTACK);

                    move(levelData);
                }
            }
        }
    }

    public void drawAttackBox(Graphics graphics, int xLevelOffset) {
        graphics.setColor(Color.RED);
        graphics.drawRect((int) (attackBox.x - xLevelOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    public int flipX() {
        if (walkingDirection == RIGHT)
            return width;
        else
            return 0;
    }

    public int flipW() {
        if (walkingDirection == RIGHT) {
            return -1;
        } else {
            return 1;
        }
    }
}
