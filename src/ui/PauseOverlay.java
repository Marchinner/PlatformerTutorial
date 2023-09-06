package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.PauseButtons.*;
import static utils.Constants.UI.URMButtons.*;
import static utils.Constants.UI.VolumeButtons.*;

public class PauseOverlay {

    private Playing playing;
    private BufferedImage backgroundImg;
    private int bgX;
    private int bgY;
    private int bgW;
    private int bgH;
    private SoundButton musicButton;
    private SoundButton sfxButton;
    private UrmButton menuButton;
    private UrmButton replayButton;
    private UrmButton unpauseButton;
    private VolumeButton volumeButton;

    public PauseOverlay(Playing playing) {
        this.playing = playing;

        loadBackground();
        createSoundButtons();
        createUrmButtons();
        createVolumeButton();
    }

    private void createVolumeButton() {
        int vX = (int) (309 * Game.SCALE);
        int vY = (int) (278 * Game.SCALE);
        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    private void createUrmButtons() {
        int menuX = (int) (313 * Game.SCALE);
        int replayX = (int) (387 * Game.SCALE);
        int unpauseX = (int) (462 * Game.SCALE);
        int buttonY = (int) (325 * Game.SCALE);

        menuButton = new UrmButton(menuX, buttonY, URM_SIZE, URM_SIZE, 2);
        replayButton = new UrmButton(replayX, buttonY, URM_SIZE, URM_SIZE, 1);
        unpauseButton = new UrmButton(unpauseX, buttonY, URM_SIZE, URM_SIZE, 0);
    }

    private void createSoundButtons() {
        int soundX = (int) (450 * Game.SCALE);
        int musicY = (int) (140 * Game.SCALE);
        int sfxY = (int) (185 * Game.SCALE);

        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);

    }

    private void loadBackground() {
        backgroundImg = LoadSave.getSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = (int) (backgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (backgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (25 * Game.SCALE);
    }

    public void update() {
        musicButton.update();
        sfxButton.update();
        menuButton.update();
        replayButton.update();
        unpauseButton.update();
        volumeButton.update();
    }

    public void draw(Graphics graphics) {
        // Background
        graphics.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);

        // Buttons
        musicButton.draw(graphics);
        sfxButton.draw(graphics);
        menuButton.draw(graphics);
        replayButton.draw(graphics);
        unpauseButton.draw(graphics);
        volumeButton.draw(graphics);
    }

    public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMousePressed()) {
            volumeButton.changeX(e.getX());
        }
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        menuButton.setMouseOver(false);
        replayButton.setMouseOver(false);
        unpauseButton.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if (isIn(e, musicButton)) {
            musicButton.setMouseOver(true);
        } else if (isIn(e, sfxButton)) {
            sfxButton.setMouseOver(true);
        } else if (isIn(e, menuButton)) {
            menuButton.setMouseOver(true);
        } else if (isIn(e, replayButton)) {
            replayButton.setMouseOver(true);
        } else if (isIn(e, unpauseButton)) {
            unpauseButton.setMouseOver(true);
        } else if (isIn(e, volumeButton)) {
            volumeButton.setMouseOver(true);
        }
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton)) {
            musicButton.setMousePressed(true);
        } else if (isIn(e, sfxButton)) {
            sfxButton.setMousePressed(true);
        } else if (isIn(e, menuButton)) {
            menuButton.setMousePressed(true);
        } else if (isIn(e, replayButton)) {
            replayButton.setMousePressed(true);
        } else if (isIn(e, unpauseButton)) {
            unpauseButton.setMousePressed(true);
        } else if (isIn(e, volumeButton)) {
            volumeButton.setMousePressed(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, musicButton)) {
            if (musicButton.isMousePressed()) {
                musicButton.setMuted(!musicButton.isMuted());
            }
        } else if (isIn(e, sfxButton)) {
            if (sfxButton.isMousePressed()) {
                sfxButton.setMuted(!sfxButton.isMuted());
            }
        } else if (isIn(e, menuButton)) {
            if (menuButton.isMousePressed()) {
                Gamestate.state = Gamestate.MENU;
            }
        } else if (isIn(e, replayButton)) {
            if (replayButton.isMousePressed()) {
                playing.resetAll();
                playing.unpauseGame();
            }
        } else if (isIn(e, unpauseButton)) {
            if (unpauseButton.isMousePressed()) {
                playing.unpauseGame();
            }
        }

        musicButton.resetBooleans();
        sfxButton.resetBooleans();
        menuButton.resetBooleans();
        replayButton.resetBooleans();
        unpauseButton.resetBooleans();
        volumeButton.resetBooleans();
}

    public void mouseOver(MouseEvent e) {

    }

    private boolean isIn(MouseEvent e, PauseButton button) {
        return button.getBounds().contains(e.getX(), e.getY());
    }
}
