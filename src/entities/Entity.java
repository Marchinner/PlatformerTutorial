package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {

    protected float x;
    protected float y;
    protected int width;
    protected int height;
    protected Rectangle2D.Float hitbox;
    protected int animationTick;
    protected int animationIndex;
    protected int state;
    protected float airSpeed;
    protected boolean inAir = false;
    protected int maxHealth;
    protected int currentHealth;
    protected float walkSpeed;

    // Attack box
    protected Rectangle2D.Float attackBox;



    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void drawHitbox(Graphics graphics, int xLevelOffset) {
        // For debugging the hitbox
        graphics.setColor(Color.PINK);
        graphics.drawRect((int) hitbox.x - xLevelOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    protected void initializeHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }

    public int getEnemyState() {
        return state;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }
}
