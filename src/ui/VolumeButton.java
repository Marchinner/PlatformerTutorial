package ui;

import utils.LoadSave;

import static utils.Constants.UI.VolumeButtons.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class VolumeButton extends PauseButton {

    private BufferedImage[] imgs;
    private BufferedImage slider;
    private int index = 0;
    private boolean mouseOver;
    private boolean mousePressed;
    private int buttonX;
    private int minX;
    private int maxX;

    public VolumeButton(int x, int y, int width, int height) {
        super(x + width / 2, y, VOLUME_WIDTH, height);
        bounds.x -= VOLUME_WIDTH / 2;
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;
        this.minX = x + VOLUME_WIDTH / 2;
        this.maxX = x + width - VOLUME_WIDTH / 2;
        loadImgs();
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        imgs = new BufferedImage[3];

        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
        }

        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
    }

    public void update() {
        index = 0;
        if (mouseOver) {
            index = 1;
        }
        if (mousePressed) {
            index = 2;
        }
    }

    public void draw(Graphics graphics) {
        graphics.drawImage(slider, x, y, width, height, null);
        graphics.drawImage(imgs[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);
    }

    public void changeX(int x) {
        if (x < minX) {
            buttonX = minX;
        } else if (x > maxX) {
            buttonX = maxX;
        } else {
            buttonX = x;
        }

        bounds.x = buttonX - VOLUME_WIDTH / 2;
    }

    public void resetBooleans() {
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }
}