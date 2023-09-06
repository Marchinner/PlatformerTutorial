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
        initalizeImg();
        initializeButtons();
    }

    private void initializeButtons() {
        int menuX = (int) (330 * Game.SCALE);
        int nextX = (int) (445 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        next = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);

    }

    private void initalizeImg() {
        img = LoadSave.getSpriteAtlas(LoadSave.COMPLETED_LEVEL_IMG);
        backgroundWidth = (int) (img.getWidth() * Game.SCALE);
        backgroundHeight = (int) (img.getHeight() * Game.SCALE);
        backgroundX = Game.GAME_WIDTH / 2 - backgroundWidth / 2;
        backgroundY = (int) (75 * Game.SCALE);

    }

    public void update() {

    }

    public void draw(Graphics graphics) {
        graphics.drawImage(img, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);
    }

    public void mouseMoved(MouseEvent e) {

    }

    public void mouseRelease(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

    }
}
