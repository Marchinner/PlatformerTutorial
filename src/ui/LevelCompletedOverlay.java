package ui;

import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import static utils.Constants.UI.URMButtons.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class LevelCompletedOverlay {

    private Playing playing;
    private UrmButton menu;
    private UrmButton next;
    private BufferedImage img;
    private int backgroundX;
    private int backgroundY;
    private int backgroundWidth;
    private int backgroundHeight;

    public LevelCompletedOverlay(Playing playing) {
        this.playing = playing;
        initializeImg();
        initializeButtons();
    }

    private void initializeButtons() {
        int menuX = (int) (330 * Game.SCALE);
        int nextX = (int) (445 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        next = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);

    }

    private void initializeImg() {
        img = LoadSave.getSpriteAtlas(LoadSave.COMPLETED_LEVEL_IMG);
        backgroundWidth = (int) (img.getWidth() * Game.SCALE);
        backgroundHeight = (int) (img.getHeight() * Game.SCALE);
        backgroundX = Game.GAME_WIDTH / 2 - backgroundWidth / 2;
        backgroundY = (int) (75 * Game.SCALE);

    }

    public void update() {
        next.update();
        menu.update();
    }

    private boolean isIn(UrmButton button, MouseEvent e) {
        return button.getBounds().contains(e.getX(), e.getY());
    }

    public void draw(Graphics graphics) {
        graphics.drawImage(img, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);
        next.draw(graphics);
        menu.draw(graphics);
    }

    public void mouseMoved(MouseEvent e) {
        next.setMouseOver(false);
        menu.setMouseOver(false);

        if (isIn(menu, e)) {
            menu.setMouseOver(true);
        } else if (isIn(next, e)) {
            next.setMouseOver(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)) {
            if (menu.isMousePressed()) {
                System.out.println("Menu!");
            }
        } else if (isIn(next, e)) {
            if (next.isMousePressed()) {
                System.out.println("Next!");
            }
        }

        menu.resetBooleans();
        next.resetBooleans();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e)) {
            menu.setMousePressed(true);
        } else if (isIn(next, e)) {
            next.setMousePressed(true);
        }
    }
}
